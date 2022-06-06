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
    <a href="meals?action=new">Add Meal</a>
    <br />
    <table>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>

        <jsp:useBean id="meal" scope="request" class="ru.javawebinar.topjava.model.Meal"/>
        <c:forEach items="${ meals }" var="meal">
            <tr style="color:${ meal.excess ? 'red' : 'green'}">
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
                <td><a href="meals?action=edit&id=<c:out value="${ meal.id }"/>">Edit</a></td>
                <td><a href="meals?action=delete&id=<c:out value="${ meal.id }"/>">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
