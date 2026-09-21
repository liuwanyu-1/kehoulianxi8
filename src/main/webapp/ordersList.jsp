<%@ page import="lwy.study.mybatis.pojo.Order" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>订单分页</title>
    <style>
        table { border-collapse: collapse; width: 700px; }
        td, th { border: 1px solid #999; padding: 6px; text-align: center; }
    </style>
</head>
<body>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    Integer pageNum = (Integer) request.getAttribute("pageNum");
    Integer pageSize = (Integer) request.getAttribute("pageSize");
    if (pageNum == null) {
        pageNum = 1;
    }
    if (pageSize == null) {
        pageSize = 5;
    }
%>

<table>
    <tr>
        <th>订单编号</th>
        <th>订单号</th>
        <th>商品</th>
        <th>金额</th>
        <th>店铺id</th>
    </tr>
    <%
        if (orders != null) {
            for (Order order : orders) {
    %>
    <tr>
        <td><%= order.getOid() %></td>
        <td><%= order.getOno() %></td>
        <td><%= order.getOgoods() %></td>
        <td><%= order.getOprice() %></td>
        <td><%= order.getMid() %></td>
    </tr>
    <%
            }
        }
    %>
</table>

<br/>

<%-- 翻页链接要用 request.getContextPath() 拼上项目路径，不然会 404 --%>
<a href="<%= request.getContextPath() %>/queryOrders?pageNum=<%= pageNum - 1 %>&pageSize=<%= pageSize %>">上一页</a>

<a href="<%= request.getContextPath() %>/queryOrders?pageNum=<%= pageNum + 1 %>&pageSize=<%= pageSize %>">下一页</a>

</body>
</html>
