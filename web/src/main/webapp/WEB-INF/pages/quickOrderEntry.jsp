<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Cart">


    <c:forEach var="messageSuccess" items="${success}">
        <p style="color: #4CAF50">${messageSuccess}</p>
    </c:forEach>
    <c:if test="${not empty errors}">
        <p style="color: red">There were errors</p>
    </c:if>
    <table class="table table-hover" style="table-layout: fixed">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Model</th>

            <th scope="col">
               Quantity
            </th>
        </tr>
        </thead>
        <tbody>
        <form  id="addToCartQuickForm" action="${pageContext.request.contextPath}/quickOrderEntry" method="post">

        <c:forEach begin="0" end="9" varStatus="loop">
            <tr>
                <c:if test="${not empty errors[loop.index]}">
                    <c:set var="listErrors" value="${ errors[loop.index]}"/>

                    <c:set var="errorModel" value=""/>
                    <c:set var="errorQuantity" value=""/>


                    <c:forEach var="messageError" items="${listErrors}">

                        <c:if test="${messageError.type.name() eq 'MODEL'}">
                            <c:set var="errorModel" value="${errorModel}${messageError.message}"/>
                        </c:if>

                        <c:if test="${messageError.type.name() eq 'QUANTITY'}">
                            <c:set var="errorQuantity" value="${errorQuantity} ${messageError.message}"/>
                        </c:if>

                    </c:forEach>

                    <td>
                       <p style="color:red"> ${errorModel}</p>
                        <input type="text" name="model" value="${paramValues['model'][loop.index]}" />
                    </td>
                <td>
                    <p style="color:red">  ${errorQuantity}</p>

                    <input type="text" name="quantity" value="${paramValues['quantity'][loop.index]}"/>
                </td>
                </c:if>

                <c:if test="${empty errors[loop.index]}">

                    <td>
                        <input type="text" name="model" />
                    </td>
                    <td>
                        <input type="text" name="quantity"/>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </form>
        </tbody>
    </table>
    <button class="btn btn-dark" form="addToCartQuickForm" >
        Add to cart
    </button>



</tags:master>
