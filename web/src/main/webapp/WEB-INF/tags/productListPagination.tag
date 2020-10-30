<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="pageQuantity" required="true" type="java.lang.Integer" %>
<%@ attribute name="pageBeforeCurrent" required="true" type="java.lang.Integer" %>
<%@ attribute name="maxPageNumber" required="true" type="java.lang.Integer" %>

<c:set var="pageCurrent"
       value="${not empty pageContext.request.getParameter('page') ? pageContext.request.getParameter('page') : 1}"/>

<c:choose>
<c:when test="${pageCurrent <= pageBeforeCurrent}">
    <c:set var="pageStart" value="1"/>
</c:when>
<c:otherwise>
    <c:set var="pageStart" value="${pageCurrent - pageBeforeCurrent}"/>
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${pageStart + pageQuantity > maxPageNumber}">
    <c:set var="pageLast" value="${maxPageNumber}"/>
</c:when>
<c:otherwise>
    <c:set var="pageLast" value="${pageStart + pageQuantity}"/>
</c:otherwise>
</c:choose>


    <nav aria-label="Product List Page search result pages">
        <ul class="pagination justify-content-end">
            <li class="page-item ${pageCurrent eq 1 ? 'disabled' : ''}">
                <a class="page-link"
                   href="<tags:getUrlWithCurrentParams servletPath="/productList" newParamName="page" newParamValue="${pageCurrent - 1}"/>"
                   aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>

            <c:forEach begin="${pageStart}" end="${pageLast}" var="pageNum">
                <li class="page-item ${pageNum eq pageCurrent  ? 'active' : ''}">
                    <a class="page-link"
                       href="<tags:getUrlWithCurrentParams servletPath="/productList" newParamName="page" newParamValue="${pageNum}"/>">
                            ${pageNum}
                    </a>
                </li>
            </c:forEach>

            <li class="page-item ${pageCurrent eq maxPageNumber ? 'disabled' : ''}">
                <a class="page-link"
                   href="<tags:getUrlWithCurrentParams servletPath="/productList" newParamName="page" newParamValue="${pageCurrent + 1}"/>"
                   aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            </li>
        </ul>
    </nav>
