<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<div class="fullscreen-container">
    <div class="login-form" id="popup">
        <form class="form-container" action="${pageContext.request.contextPath}/login" method="post">
            <button onclick="popUpHide()" class="close" style="position: initial; color: #343a40">x</button>
            <h1>Log in</h1>
            <input type="text" name="username" placeholder="Enter Name" required>
            <input type="password" name="password" placeholder="Enter Password" required>

            <button type="submit" class="btn">Log in</button>
        </form>
    </div>
</div>