<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product List">

    <div class="d-flex">
        <div class="mr-auto p-2">
            <c:if test="${not empty phones}">
                <h3>1-${phones.size()} of ${allPhonesQuantity} phones</h3>
            </c:if>
            <c:if test="${empty phones}">
                <h3>Phones not found</h3>
            </c:if>
            <button class="btn btn-dark" onclick="redirect('${pageContext.request.contextPath}/quickOrderEntry')">Quick order</button>

        </div>
        <div class="p-2">
            <form>
                <input class="form-control" name="query" value="${param.query}" type="text" placeholder="Search"/>
            </form>
        </div>
    </div>
    <p>
    <c:if test="${not empty phones}">
        <table class="table table-hover" style="table-layout: fixed">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Image</th>
                <th scope="col">
                    Brand
                    <tags:sortLink sort="brand" order="asc"/>
                    <tags:sortLink sort="brand" order="desc"/>
                </th>
                <th scope="col">
                    Model
                    <tags:sortLink sort="model" order="asc"/>
                    <tags:sortLink sort="model" order="desc"/>
                </th>
                <th scope="col">Color</th>
                <th scope="col">
                    Display size
                    <tags:sortLink sort="displaySizeInches" order="asc"/>
                    <tags:sortLink sort="displaySizeInches" order="desc"/>
                </th>
                <th scope="col">
                    Price
                    <tags:sortLink sort="price" order="asc"/>
                    <tags:sortLink sort="price" order="desc"/>
                </th>
                <th scope="col" style="width: 220px">Quantity</th>
                <th scope="col">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="phone" items="${phones}">
                <c:set var="productDetailsLink" value="${pageContext.request.contextPath}/productDetails/${phone.id}"/>
                <tr>
                    <td onclick="redirect('${productDetailsLink}')">
                        <img width="150"
                             src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    </td>
                    <td onclick="redirect('${productDetailsLink}')">${phone.brand}</td>
                    <td onclick="redirect('${productDetailsLink}')">
                        <a href="${productDetailsLink}" style="color: black">${phone.model}</a>
                    </td>
                    <td onclick="redirect('${productDetailsLink}')">
                        <c:forEach var="color" items="${phone.colors}" varStatus="loopStatus">
                            ${color.code}
                            <c:if test="${!loopStatus.last}">,</c:if>
                        </c:forEach>
                    </td>
                    <td onclick="redirect('${productDetailsLink}')">${phone.displaySizeInches}"</td>
                    <td onclick="redirect('${productDetailsLink}')">
                        <fmt:formatNumber value="${phone.price}" type="currency" currencySymbol="$"/>
                    </td>
                    <td>
                        <input type="text" class="form-control" id="quantityAddToCart-${phone.id}" value="1"/>
                        <div id="message-${phone.id}"></div>
                    </td>
                    <td>
                        <button class="btn btn-dark" onclick="add(${phone.id});">Add</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <tags:productListPagination pageQuantity="7" pageBeforeCurrent="3" maxPageNumber="${maxPageNumber}"/>

        </p>
    </c:if>
</tags:master>