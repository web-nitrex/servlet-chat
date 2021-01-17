package sber;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ServletChat extends HttpServlet{
    private List<String> chatMessages = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        String cookieName = "login";
        Cookie cookie = null;
        if(cookies !=null) {
            for(Cookie c: cookies) {
                if(cookieName.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }

        //проверяем что пользователь уже авторизовался
        if(cookie==null)
        {
            //перенаправляем клиента на форму авторизации
            String path = "/authorization-form.html";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            requestDispatcher.forward(req, resp);
        }
        else{
            //отображаем все сообщения чата
            //передаём список со всеми сообщениями чата на JSP страницу
            chatMessages.add("Message 1");
            chatMessages.add("Message 2");
            chatMessages.add("Message 3");

            req.setAttribute("chatMessages", chatMessages);
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);

            System.out.println("ServletChat - doGet -"+cookie.getValue());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String login = request.getParameter("login");

        if(login!=null) {
            System.out.println("Connect to chat user - " + login);
            response.addCookie(new Cookie("login", login));

            //отображаем все сообщения чата
            String path = "/index.jsp";
            ServletContext servletContext = getServletContext();

            //chatMessages.add("Message form Pasha");
            //chatMessages.add("Message from Vova");

            //передаём список со всеми сообщениями чата на JSP страницу
            //request.setAttribute("chatMessages",chatMessages);
            //servletContext.getRequestDispatcher("/index.jsp").forward(request,response);
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);
        }
    }
}
