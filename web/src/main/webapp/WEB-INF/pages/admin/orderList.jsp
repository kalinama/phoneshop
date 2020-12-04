<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Order List">
    <c:if test="${empty orders}">
        <h2>No orders.</h2>
    </c:if>
    <c:if test="${not empty orders}">
        <h3>Orders</h3>
        <p>
        <table class="table table-hover" style="table-layout: fixed">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Order number</th>
                <th scope="col">Customer</th>
                <th scope="col">Phone</th>
                <th scope="col">Address</th>
                <th scope="col">Date</th>
                <th scope="col">Total price</th>
                <th scope="col">Status</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="order" items="${orders}" varStatus="loop">
                <c:set var="orderDetailsLink" value="${pageContext.request.contextPath}/admin/orders/${order.id}"/>
                <tr onclick="redirect('${orderDetailsLink}')">
                    <td>
                        <a style="color: black" href="${pageContext.request.contextPath}/admin/orders/${order.id}">
                                ${order.id}
                        </a>
                    </td>
                    <td>${order.firstName} ${order.lastName}</td>
                    <td>${order.contactPhoneNo}</td>
                    <td>${order.deliveryAddress}</td>
                    <td>${order.orderingDate}</td>
                    <td>
                        <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="$"/>
                    </td>
                    <td>${order.status.name()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

</tags:master>
