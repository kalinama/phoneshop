<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Cart">

    <c:forEach var="messageSuccess" items="${success}">
        <p style="color: #4CAF50">${messageSuccess}</p>
    </c:forEach>

    <spring:hasBindErrors name="quickOrderEntriesForm">
        <c:if test="${errors.hasErrors()}">
            <p style="color: red">There were errors</p>
        </c:if>
    </spring:hasBindErrors>

    <table class="table table-hover" style="table-layout: fixed">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Model</th>

            <th scope="col">Quantity</th>
        </tr>
        </thead>
        <tbody>
        <form:form id="addToCartQuickForm" method="post"
                   action="${pageContext.request.contextPath}/quickOrderEntry" modelAttribute="quickOrderEntriesForm">
            <c:forEach begin="0" end="9" varStatus="loop">
                <c:set var="index" value="${loop.index}"/>
                <tr>
                    <td style="vertical-align: bottom">
                        <form:errors path="entries[${index}].model" cssStyle="color: red"/>
                        <form:input class="form-control" path="entries[${index}].model"/>
                    </td>
                    <td style="vertical-align: bottom">
                        <form:errors path="entries[${index}].quantity" cssStyle="color: red"/>
                        <form:input class="form-control" path="entries[${index}].quantity"/>
                    </td>
                </tr>
            </c:forEach>
        </form:form>
        </tbody>
    </table>
    <button class="btn btn-dark" form="addToCartQuickForm">
        Add to cart
    </button>

</tags:master>
