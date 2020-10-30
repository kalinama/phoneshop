<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="servletPath" required="true" %>
<%@ attribute name="newParamName" required="true" %>
<%@ attribute name="newParamValue" required="true" %>


<c:url value="${servletPath}">
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != newParamName}">
            <c:param name="${entry.key}" value="${entry.value}" />
        </c:if>
    </c:forEach>
    <c:param name="${newParamName}" value="${newParamValue}" />
</c:url>
