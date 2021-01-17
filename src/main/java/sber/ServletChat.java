package sber;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServletChat extends HttpServlet{
    private List<Message> chatMessages = new ArrayList<>();

    private String getLoginFromCookie(HttpServletRequest request)
    {
        String login = null;

        Cookie[] cookies = request.getCookies();
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

        if(cookie!=null)
        {
            login = cookie.getValue();
            return login;
        }

        return login;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = getLoginFromCookie(request);

        //проверяем что пользователь уже авторизовался
        if(login==null)
        {
            //перенаправляем клиента на форму авторизации
            String path = "/authorization-form.html";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);
        }
        else{
            //отображаем все сообщения чата
            request.setAttribute("chatMessages", chatMessages);
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

            System.out.println("ServletChat - doGet -"+login);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String login = request.getParameter("login");
        String msg = request.getParameter("message");
        String exit = request.getParameter("exit");

        //получение логина из формы авторизации
        if(login!=null) {
            //устанавливаем в cookie пользователя значение логина, которое мы получили из формы авторизации
            response.addCookie(new Cookie("login", login));

            //передаём список со всеми сообщениями чата на JSP страницу
            request.setAttribute("chatMessages", chatMessages);
            //getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);  <--Post-Redirect-Get
            response.sendRedirect(request.getContextPath() + request.getServletPath());
            System.out.println("Connect to chat user - " + login);
        }

        //добавление в чат нового сообщения
        if(msg!=null)
        {
            //формируем сообщение и добавляем его в список
            Message message = new Message(getLoginFromCookie(request),msg,new Date());
            chatMessages.add(message);

            //передаём список со всеми сообщениями чата на JSP страницу
            request.setAttribute("chatMessages", chatMessages);
            //getServletContext().getRequestDispatcher("/index.jsp").forward(request, response); <--Post-Redirect-Get
            response.sendRedirect(request.getContextPath() + request.getServletPath());
            System.out.println("Add message to chat");
        }

        //выход из чата
        if(exit!=null)
        {
            //устанавливаем в cookie время жизни 0, тем самым очищаем их и передаем обратно клиенту
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }

            //переходим на форму авторизации
            getServletContext().getRequestDispatcher("/authorization-form.html").forward(request, response);
            System.out.println("Exit from chat");
        }

    }
}

