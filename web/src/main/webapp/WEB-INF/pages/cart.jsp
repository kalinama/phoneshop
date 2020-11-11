<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Cart">

    <c:if test="${empty cart.cartItems}">
        <h2>Your cart is empty.</h2>
    </c:if>
    <c:if test="${not empty cart.cartItems}">
        <h3>Cart</h3>
        <c:if test="${empty errors and pageContext.request.method eq 'PUT'}">
            <p style="color: green">Cart is updated successfully</p>
        </c:if>
        <c:if test="${not empty errors}">
            <p style="color: red">Cart isn't updated because of incorrect quantity input</p>
        </c:if>
        <p>
        <table class="table table-striped" style="table-layout: fixed">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Image</th>
            <th scope="col">Model</th>
            <th scope="col">Color</th>
            <th scope="col">Price</th>
            <th scope="col" style="width: 220px">Quantity</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="loop">
            <tr>
                <td>
                    <img width="80" src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${cartItem.phone.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/productDetails/${cartItem.phone.id}"
                       style="color: black">${cartItem.phone.model}</a>
                </td>
                <td>
                    <c:forEach var="color" items="${cartItem.phone.colors}" varStatus="loopStatus">
                        ${color.code}
                        <c:if test="${!loopStatus.last}">,</c:if>
                    </c:forEach>
                </td>
                <td>
                    <fmt:formatNumber value="${cartItem.phone.price}" type="currency" currencySymbol="$"/>
                </td>
                <td>
                    <input form="cartUpdate" type="hidden" name="phoneId" value="${cartItem.phone.id}"/>
                    <input form="cartUpdate" type="text" class="form-control" id="quantityAddToCart-${cartItem.phone.id}"
                           value="${not empty errors ? paramValues.quantity[loop.index] : cartItem.quantity}" name="quantity"/>
                    <div style="color: red"> <c:if test="${not empty errors[cartItem.phone.id]}">${errors[cartItem.phone.id]} </c:if>
                    </div>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/cart/${cartItem.phone.id}" method="post">
                        <input type="hidden" name="_method" value="delete"/>
                        <button class="btn btn-dark" type="submit" >Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="d-flex justify-content-end" >
        <button style="margin-right: 10px" class="btn btn-dark" type="submit" form="cartUpdate" >Update</button>
            <button class="btn btn-dark" form="orderForm" >Order</button>
    </div>

        <form id="orderForm" action="${pageContext.request.contextPath}/order"></form>

        <form method="post" action="${pageContext.request.contextPath}/cart" id="cartUpdate">
            <input type="hidden" name="_method" value="put"/>
        </form>

    </c:if>
</tags:master>
