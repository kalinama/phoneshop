<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">

    <script src="http://code.jquery.com/jquery-1.10.2.min.js" type="text/javascript"></script>

    <script>const myContextPath = "${pageContext.request.contextPath}";</script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajaxCart.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajaxOrder.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/headerFixedOnScroll.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/additionalFunctions.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/csrfSetUp.js"></script>

    <sec:csrfMetaTags/>
</head>
<body>
<div class="card">
    <div id="myHeader" class="card-header"
         style="background-color: #e7e7e7; background-image: radial-gradient(white, transparent);">
        <div class="d-flex flex-row ">
            <div class="mr-auto p-2 ">
                <a title="Product List" href="${pageContext.request.contextPath}">
                    <img width="200" src="${pageContext.request.contextPath}/images/logo.svg"/>
                    <img width="55" src="${pageContext.request.contextPath}/images/phone.png"/>
                </a>
            </div>
            <div class="flex-column">
                <sec:authorize access="hasAuthority('ROLE_ANONYMOUS')">
                    <tags:loginPopUp/>
                    <div class="p-2" style="text-align: end;">
                        <a title="Log in as Admin" href="javascript:popUpShow()">
                            <img width="30" src="${pageContext.request.contextPath}/images/login.svg"/>
                        </a>
                    </div>

                </sec:authorize>
                <sec:authorize access="hasAuthority('ROLE_ADMIN')">
                    <div class="p-2" style="text-align: end; font-weight: bold; font-size: medium;">
                        Admin
                        <a title="Log out" href="javascript:formSubmit()">
                            <img width="30" src="${pageContext.request.contextPath}/images/logout.svg"/>
                            <form action="${pageContext.request.contextPath}/logout" method="post" id="logoutForm">

                            </form>
                        </a>
                    </div>
                </sec:authorize>
                <div class="d-flex flex-row ">
                    <a style="color: #343a40" href="${pageContext.request.contextPath}/cart" title="Cart">
                        <div id="miniCart"
                             class="border-dark p-2 border rounded d-flex align-items-center justify-content-end border-container-black">
                        </div>
                    </a>
                    <sec:authorize access="hasAuthority('ROLE_ADMIN')">
                        <a style="color: #343a40; margin-left: 10px"
                           href="${pageContext.request.contextPath}/admin/orders" title="Orders">
                            <div class=" border-dark p-2 border rounded d-flex align-items-center justify-content-end border-container-black">
                                All Orders
                            </div>
                        </a>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </div>
    <div class="card-body">
        <jsp:doBody/>
    </div>
    <div class="card-footer" style="background-color: #e7e7e7; background-image: radial-gradient(white, transparent);">
        <p> &copy; Expert-Soft </p>
    </div>
</div>
</body>
</html>