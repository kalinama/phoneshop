<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product List">

  <div class="d-flex">
    <div class="mr-auto p-2">
      <h3>Phones</h3>
    </div>
    <div class="p-2">
      <form>
        <input class="form-control" name="query" value="${param.query}" type="text" placeholder="Search" />
      </form>
    </div>
  </div>
  <p>
  <table class="table" style="table-layout: fixed">
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
        <tr>
          <td>
            <img width="150" src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
          </td>
          <td>${phone.brand}</td>
          <td>${phone.model}</td>
          <td>
            <c:forEach var="color" items="${phone.colors}" varStatus="loopStatus">
              ${color.code}
              <c:if test="${!loopStatus.last}">,</c:if>
            </c:forEach>
          </td>
          <td>${phone.displaySizeInches}"</td>
          <td>
            <fmt:formatNumber value="${phone.price}" type="currency" currencySymbol="$"/>
          </td>
          <td>
            <input type="text" class="form-control" name="quantity" id="quantityAddToCart-${phone.id}" value="1"/>
            <div style="" id="message-${phone.id}"></div>
          </td>
          <td>
            <button class="btn btn-dark" onclick="add(${phone.id});" id="test">
              Add
            </button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <tags:productListPagination pageQuantity="7" pageBeforeCurrent="3" maxPageNumber="${maxPageNumber}"/>

  </p>
</tags:master>