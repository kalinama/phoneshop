<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Order Details">

    <div class="d-flex flex-row">
        <div class="mr-auto p-2">
            <h4>Order number #${order.id}</h4>
        </div>
        <div class="ml-auto p-2">
            <h4>Order status: <a id="status">${order.status.name()}</a></h4>
        </div>
    </div>
    <p>
    <table class="table" style="table-layout: fixed">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Image</th>
            <th scope="col">Model</th>
            <th scope="col">Color</th>
            <th scope="col">Price</th>
            <th scope="col" style="width: 320px">Quantity</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="orderItem" items="${order.orderItems}" varStatus="loop">
            <tr>
                <td>
                    <img width="60"
                         src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${orderItem.phone.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/productDetails/${orderItem.phone.id}"
                       style="color: black">${orderItem.phone.model}</a>
                </td>
                <td>
                    <c:forEach var="color" items="${orderItem.phone.colors}" varStatus="loopStatus">
                        ${color.code}
                        <c:if test="${!loopStatus.last}">,</c:if>
                    </c:forEach>
                </td>
                <td>
                    <fmt:formatNumber value="${orderItem.phone.price}" type="currency" currencySymbol="$"/>
                </td>
                <td>${orderItem.quantity}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div style="text-align: center">
        <div style="color: #000000; font-size: 20px; font-weight: lighter; background-color: #dbdbdb;">
            Subtotal: <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="$"/>

            <hr style="margin-bottom: 0; margin-top: 0">
            Delivery price: <fmt:formatNumber value="${order.deliveryPrice}" type="currency" currencySymbol="$"/>
        </div>
        <div style="color: #ffffff; font-size: 20px; font-weight:bold; background-color: #343a40">
            Total price: <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="$"/>
        </div>
    </div>
    <p></p>

    <div class="d-flex flex-row">
        <div class="mr-auto p-2">
            <button class="btn btn-dark" onclick="redirect('${pageContext.request.contextPath}/admin/orders')">Back
            </button>
        </div>
        <div class="ml-auto p-2">
            <c:if test="${order.status.name().equals('NEW')}">
                <button class="btn btn-dark" id="button-delivered" onclick="changeStatus(${order.id}, 'delivered')">
                    Delivered
                </button>
                <button class="btn btn-dark" id="button-rejected" onclick="changeStatus(${order.id}, 'rejected')">
                    Rejected
                </button>
            </c:if>
        </div>
    </div>
    <p></p>

    <h4>Customer information:</h4>

    <p></p>
    <div style="max-width: 600px">
        <table class="table">
            <tr>
                <td style="font-weight: bold">First name</td>
                <td>${order.firstName}</td>
            </tr>
            <tr>
                <td style="font-weight: bold">Last name</td>
                <td>${order.lastName}</td>
            </tr>
            <tr>
                <td style="font-weight: bold">Phone number</td>
                <td>${order.contactPhoneNo}</td>
            </tr>
            <tr>
                <td style="font-weight: bold">Address</td>
                <td>${order.deliveryAddress}</td>
            </tr>
            <c:if test="${not empty order.additionalInformation}">
                <tr>
                    <td style="font-weight: bold"> Additional information</td>
                    <td>${order.additionalInformation}</td>
                </tr>
            </c:if>
        </table>
    </div>


</tags:master>
