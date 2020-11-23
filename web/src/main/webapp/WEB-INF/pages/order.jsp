<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order">

    <c:if test="${empty order.orderItems}">
        <h2>You can not place an order because the cart is empty.</h2>
    </c:if>

    <c:if test="${not empty order.orderItems}">
        <h3>Order</h3>
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
                <tr class=" ${availableStockForOutOfStockPhones[orderItem.phone.id] eq 0 ? 'strikeout' : ''}"
                    style=" ${availableStockForOutOfStockPhones[orderItem.phone.id] eq 0 ? 'background-color : #FADDDD' : ''}
                            ${availableStockForOutOfStockPhones[orderItem.phone.id] > 0 ? 'background-color : #FFF1D4' : ''}">
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
                    <td>
                    <span style=" ${availableStockForOutOfStockPhones[orderItem.phone.id] > 0 ? 'text-decoration: line-through' : ''}">
                            ${orderItem.quantity}</span>
                        <c:if test="${availableStockForOutOfStockPhones[orderItem.phone.id] > 0}">
                            <br>
                            ${availableStockForOutOfStockPhones[orderItem.phone.id]}
                            <br>
                            <span style="color: orangered;">The quantity of product that you specified is out of stock. Update order</span>
                            <c:set var="updateFlag" value="true"/>
                        </c:if>
                        <c:if test="${availableStockForOutOfStockPhones[orderItem.phone.id] eq 0}">
                            <br>
                            <span style="color: red;">This product is out of stock. Delete it from order</span>
                            <button formaction="${pageContext.request.contextPath}/order/${orderItem.phone.id}"
                                    form="delete" class="close">x
                            </button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div style="text-align: end">
            <form method="post" action="${pageContext.request.contextPath}/order" id="update">
                <c:forEach var="entry" items="${availableStockForOutOfStockPhones}">
                    <c:if test="${entry.value > 0}">
                        <input type="hidden" name="phoneId" value="${entry.key}"/>
                        <input type="hidden" name="quantity" value="${entry.value}"/>
                    </c:if>
                </c:forEach>
                <input type="hidden" name="_method" value="put"/>
                <c:if test="${updateFlag eq true}">
                    <button style="margin-right: 10px" class="btn btn-success" type="submit">Update order</button>
                </c:if>
            </form>
        </div>

        <form id="delete" method="post">
            <input type="hidden" name="_method" value="delete"/>
        </form>

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

        <h4>Customer information:</h4>
        <form:form id="form" method="post" action="${pageContext.request.contextPath}/order" modelAttribute="order">
            <div style="max-width: 600px;">
                <p></p>
                <form:errors path="firstName" cssStyle="color: red"/>
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <span class="input-group-text">First name<span style="color: red">*</span></span>
                    </div>
                    <form:input class="form-control" path="firstName"/>
                </div>

                <p></p>
                <form:errors path="lastName" cssStyle="color: red"/>
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Last name<span style="color: red">*</span></span>
                    </div>
                    <form:input class="form-control" path="lastName"/>
                </div>

                <p></p>
                <form:errors path="contactPhoneNo" cssStyle="color: red"/>
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Phone number<span style="color: red">*</span></span>
                    </div>
                    <form:input path="contactPhoneNo" class="form-control" placeholder="+375-XX-XXX-XX-XX"/>
                </div>

                <p></p>
                <form:errors path="deliveryAddress" cssStyle="color: red"/>
                <div class="input-group mb-3 required">
                    <div class="input-group-prepend">
                        <span class="input-group-text required">Address<span style="color: red">*</span></span>
                    </div>
                    <form:input path="deliveryAddress" class="form-control"/>
                </div>

                <p></p>
                <form:errors path="additionalInformation" cssStyle="color: red"/>
                <div class="input-group ">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Additional information</span>
                    </div>
                    <form:textarea path="additionalInformation" class="form-control"/>
                </div>
                <p></p>

                <div class="d-flex justify-content-end">
                    <button class="btn btn-dark" ${not empty availableStockForOutOfStockPhones ? 'disabled' : ''}>
                        Place order
                    </button>
                </div>
            </div>
        </form:form>
    </c:if>
</tags:master>
