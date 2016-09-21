<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sherry
  Date: 16/9/21
  Time: 下午9:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="BASE" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
    <title>用户管理</title>
</head>
<body>
<h1>客户列表</h1>
<table>
    <tr>
        <th>客户名称</th>
        <th>联系人</th>
        <th>联系电话</th>
        <th>邮箱地址</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${customerList}" var="customer">
        <tr>
            <td>${customer.name}</td>
            <td>${customer.contact}</td>
            <td>${customer.telephone}</td>
            <td>${customer.email}</td>
            <td><a href="${BASE}/customer_edit?id=${customer.id}">编辑</a></td>
            <td><a href="${BASE}/customer_del?id=${customer.id}">删除</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
