<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<form action="meals" method="post">
    <table>
        <h2>
            ${meal.id == 0 ? 'Add New Meal' : ' Edit meal'}
        </h2>

        <input type="hidden" name="id" value="${meal.id}" />
        <tr>
            <th>DateTime:</th>
            <td>
                <input type="datetime-local" name="dateTime" size="45" value="${meal.dateTime}" />
            </td>
        </tr>
        <tr>
            <th>Description:</th>
            <td>
                <input type="text" name="description" size="45" value="${meal.description}" />
        </tr>
        <tr>
            <th>Calories:</th>
            <td>
                <input type="number" name="calories" size="45" value="${meal.calories}" />
            </td>
        </tr>
    </table>
    <div>
        <input type="submit" value="Save"/>
        <input type="button" value="Cancel" onClick='location.href="meals"' />
    </div>
</form>
</body>
</html>
