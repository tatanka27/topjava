<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meals</title>
    <link rel='stylesheet' href='style.css' type='text/css' />
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>

        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <c:forEach items="${meals }" var="meal">
            <tr style="color:${ meal.excess ? 'green' : 'red'}">
                <td>
                    <jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
                    <c:out value="${ meal.dateTime.format(dateTimeFormatter) }" />
                </td>
                <td>
                    <c:out value="${ meal.description }" />
                </td>
                <td>
                    <c:out value="${ meal.calories }" />
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
