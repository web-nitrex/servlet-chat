<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="Cp1251" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=cp1251">
<title>Chat</title>
</head>
<body>
    <% response.setIntHeader("Refresh", 10); %>
    <h2>Chat</h2>
    <p>
    <c:forEach var="msg" items="${chatMessages}">
      <br><b><c:out value="${msg.from}" /></b></br>
      <br><c:out value="${msg.date}" /></br>
      <br><c:out value="${msg.message}" /></br>
    </c:forEach>

     <form action="chat" method="post">
          <p>Message [${cookie.login.value}]:<br>
          <textarea name="message" cols="40" rows="3"></textarea></p>
          <p><input type="submit" value="Send">
          <input type="reset" value="Clear"></p>
     </form>
     <form action="chat" method="POST">
        <p><input name="exit" type="submit" value="Exit" />
     </form>


</body>
</html>
