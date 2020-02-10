<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
</head>
<body>
<h3>${meal.id == null ? "Создание новой еды" : "Редактирование еды"}</h3>
<form method="post" action="meals">
    <input type="hidden" name="id" value=${meal.id}>
    <table cellspacing="10">
        <tr>
            <td>Дата и время:</td>
            <td>
                <input type="text" name="datetime" value="${meal.dateTime.withSecond(0).withNano(0).toString().replace("T", " ")}">
            </td>
            <td>(формат: гггг-мм-дд чч:мм)</td>
        </tr>
        <tr>
            <td>Описание:</td>
            <td>
                <input type="text" name="description" value="${meal.description}">
            </td>
        </tr>
        <tr>
            <td>Калории:</td>
            <td>
                <input type="number" name="calories" value="${meal.calories}">
            </td>
        </tr>
    </table>
    <br>
    <button type="submit">Сохранить</button>
    <button type="reset" onclick="window.history.back()">Отменить</button>
</form>
</body>
</html>
