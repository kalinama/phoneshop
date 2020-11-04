<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<c:set var="pageValue" value="&page=${param.page}"/>
<c:set var="queryValue" value="&query=${param.query}"/>

<a href="?sort=${sort}&order=${order}${not empty param.query ? queryValue : ''}"
   style="color: #f0f8ff; ${sort eq param.sort and order eq param.order ? 'font-weight: bold' : ''}">
    ${'asc' eq order ? '&uArr;' : '&dArr;'}
</a>
