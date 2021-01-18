package sber;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackListFilter implements Filter {
    private List<String> blackList = new ArrayList<>();

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
            req.getRequestDispatcher("/admin.html").forward(req, res);
            System.out.println("User "+newUserToBlackList+" add to black list!");
        }

        //получаем логин из POST запроса
        String login = req.getParameter("login");

        if (login!=null)
        {
            if(blackList.stream().anyMatch(u->u.equals(login)))
            {
                //пользователь есть в черном списке
                req.getRequestDispatcher("/authorization-form.html").forward(req, res);
                System.out.println("User "+login+" is blocked!");
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
