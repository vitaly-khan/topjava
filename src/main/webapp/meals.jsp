<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h3>Список еды</h3>
<a href="meals?action=create">Создать</a>
<table cellpadding="15">
    <tr style="font-style: italic">
            <td>Дата и время</td>
            <td>Описание</td>
            <td>Калории</td>
            <td></td>
            <td></td>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class=${meal.excess ? "excess" : "normal"}>
            <td>${meal.dateTime.toString().replace("T", " ")}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=delete&id=${meal.id}">Удалить</a></td>
            <td><a href="meals?action=edit&id=${meal.id}">Редактировать</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
