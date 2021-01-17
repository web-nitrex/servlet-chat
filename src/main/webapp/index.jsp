<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP Application</title>
</head>
<body>
        <ul>
            <c:forEach var="msg" items="${chatMessages}">
                <li><c:out value="${msg}" /></li>
            </c:forEach>
        </ul>
</body>
</html>
