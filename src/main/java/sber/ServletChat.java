package sber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServletChat extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletChat.class);

    private static final List<Message> chatMessages = new CopyOnWriteArrayList<>();

    private String getLoginFromSession(HttpServletRequest request)
    {
        // получаем сессию
        HttpSession session = request.getSession();
        // получаем объект login
        String login = (String) session.getAttribute("login");
        return login;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = getLoginFromSession(request);

        //проверяем что пользователь уже авторизовался
        if(login==null)
        {
            //перенаправляем клиента на форму авторизации
            String path = "/authorization-form.html";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);

            LOGGER.info("Redirection to the authorization form.");
        }
        else{
            //отображаем все сообщения чата
            request.setAttribute("chatMessages", chatMessages);
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

            LOGGER.info("Displaying chat messages - {}",login);
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
            // получаем сессию
            HttpSession session = request.getSession();

            //устанавливаем в сессию пользователя логин
            session.setAttribute("login", login);

            //обновляем страницу
            response.sendRedirect(request.getContextPath() + request.getServletPath());

            LOGGER.info("Connect to chat user - {}", login);
        }
        else if(msg!=null) //добавление в чат нового сообщения
        {
            //формируем сообщение и добавляем его в список
            Message message = new Message(getLoginFromSession(request),msg,new Date());
            chatMessages.add(message);

            //обновляем страницу
            response.sendRedirect(request.getContextPath() + request.getServletPath());
            LOGGER.info("Add message to chat");
        }
        else if(exit!=null) //выход из чата
        {
            // получаем сессию
            HttpSession session = request.getSession();
            String removeLogin = getLoginFromSession(request);
            session.removeAttribute("login");
            //переходим на форму авторизации
            response.sendRedirect(request.getContextPath() + "/authorization-form.html");
            LOGGER.info("Exit from chat - {}", removeLogin);
        }

    }
}

