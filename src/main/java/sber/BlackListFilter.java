package sber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlackListFilter implements Filter {
    private static final List<String> blackList = new CopyOnWriteArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException{}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //если пришел новый логин из админки то добавляем его в черный список
        String newUserToBlackList = req.getParameter("black-login");
        if(newUserToBlackList!=null) {
            blackList.add(newUserToBlackList);
            res.sendRedirect(req.getContextPath() + "/admin.html");
            LOGGER.info("User {} add to black list!", newUserToBlackList);
        }

        //получаем логин из POST запроса
        String login = req.getParameter("login");

        if (login!=null)
        {
            if(blackList.stream().anyMatch(u->u.equals(login)))
            {
                //пользователь есть в черном списке
                res.sendRedirect(req.getContextPath() + "/authorization-form.html");
                LOGGER.info("User {} is blocked!", login);
            }
            else
            {
                chain.doFilter(request, response);
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }

}
