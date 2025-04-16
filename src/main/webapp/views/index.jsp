<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>DevCalculator</title>
    <link rel="stylesheet" href="./css/Style.css">
</head>
<body>
    <h2>Developer_Calculator_WebApp_Webhook</h2>
    <form action="calculate" method="post">
        <input type="number" name="num1" placeholder="Enter number 1" required><br><br>
        <input type="number" name="num2" placeholder="Enter number 2" required><br><br>
        <button type="submit" name="operation" value="add">Add</button>
        <button type="submit" name="operation" value="subtract">Subtract</button>
        <button type="submit" name="operation" value="multiply">Multiply</button>
        <button type="submit" name="operation" value="divide">Divide</button>
    </form>
</body>
</html>
