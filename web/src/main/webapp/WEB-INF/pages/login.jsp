<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
    <script src="http://code.jquery.com/jquery-1.10.2.min.js" type="text/javascript"></script>

    <script>const myContextPath = "${pageContext.request.contextPath}";</script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajaxCart.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/headerFixedOnScroll.js"></script>
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
                <div class="p-2" style="min-height: 30px"></div>
                <a style="color: #343a40" href="${pageContext.request.contextPath}/cart" title="Cart">
                    <div id="miniCart"
                         class="border-dark p-2 border rounded d-flex align-items-center justify-content-end border-container-black">
                    </div>
                </a>
            </div>
        </div>
    </div>
    <div class="card-body">
        <div class="login-form">
            <form class="form-container" action="${pageContext.request.contextPath}/login" method="post">
                <h1>Log in</h1>
                <c:if test="${not empty error}">
                    <a style="color: red; font-weight: bold"> ${error} </a><br>
                    <a style="color: red">Invalid username or password</a>
                </c:if>
                <input type="text" name="username" placeholder="Enter Name" required>
                <input type="password" name="password" placeholder="Enter Password" required>
                <button type="submit" class="btn">Log in</button>
            </form>
        </div>
    </div>
    <div class="card-footer" style="background-color: #e7e7e7; background-image: radial-gradient(white, transparent);">
        <p> &copy; Expert-Soft </p>
    </div>
</div>
</body>
</html>