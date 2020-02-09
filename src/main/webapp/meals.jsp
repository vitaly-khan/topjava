<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h3>Meals list</h3>
<table cellpadding="15">
    <tr>
        <b>
            <td>Date & time</td>
            <td>Description</td>
            <td>Calories</td>
            <td></td>
            <td></td>
        </b>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class=${meal.excess ? "excess" : "normal"}>
            <td>${meal.dateTime.toString().replace("T", " ")}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="/meals?action=delete&id=${meal.id}">Удалить</a></td>
            <td><a href="/meals?action=edit&id=${meal.id}">Редактировать</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
