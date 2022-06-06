<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add meal</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
        <form action="${meal == null?'meals?action=insert':'meals?action=update'}" method="post">
        <table>
            <h2>
                <c:if test="${meal != null}">
                    Edit meal
                </c:if>
                <c:if test="${meal == null}">
                    Add New Meal
                </c:if>

            </h2>
            <c:if test="${meal != null}">
                <input type="hidden" name="id" value="<c:out value='${meal.id}' />" />
            </c:if>
            <tr>
                <th>DateTime: </th>
                <td>
                    <input type="datetime-local" name="dateTime" size="45"
                           value="<c:out value='${meal.dateTime}' />"
                    />
                </td>
            </tr>
            <tr>
                <th>Description: </th>
                <td>
                    <input type="text" name="description" size="45"
                           value="<c:out value='${meal.description}' />"
                    />
                </td>
            </tr>
            <tr>
                <th>Calories: </th>
                <td>
                    <input type="number" name="calories" size="15"
                           value="<c:out value='${meal.calories}' />"
                    />
                </td>
            </tr>
        </table>
            <div>
                <input type="submit" name="save" value="Save" />
                <input type="submit" name="cancel" value="Cancel" />
            </div>
        </form>
</body>
</html>
