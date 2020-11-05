<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

 <html>
 <head>
    <title>${pageTitle}</title>
     <meta charset="utf-8">
     <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
     <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
     <script src="http://code.jquery.com/jquery-1.10.2.min.js" type="text/javascript"></script>

     <script>const myContextPath = "${pageContext.request.contextPath}";</script>
     <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajaxCart.js"></script>
     <script type="text/javascript" src="${pageContext.request.contextPath}/js/headerFixedOnScroll.js"></script>
     <script type="text/javascript" src="${pageContext.request.contextPath}/js/clickableTableRow.js"></script>
 </head>
 <body>
 <div class="card">
     <div id="myHeader" class="card-header" style="background-color: #f8f8f8;">
         <div class="d-flex flex-row ">
             <div class="mr-auto p-2 ">
                 <a href="${pageContext.request.contextPath}">
                     <img width="200" src="${pageContext.request.contextPath}/images/logo.svg"/>
                     <img width="55" src="${pageContext.request.contextPath}/images/phone.png"/>
                 </a>
             </div>
             <div class="flex-column">
                 <div class="p-2"></div>
                 <div class="p-2"></div>
                 <a style="color: #343a40" href="${pageContext.request.contextPath}/cart">
                     <div  id="miniCart" class=" border-dark p-2 border rounded d-flex align-items-center justify-content-end"
                       style="color: #ffffff; font-size: 20px; font-weight:bold; background-color: #343a40"></div>
                 </a>
                 <div class="p-2"></div>
             </div>
         </div>
     </div>
     <div class="card-body">
        <jsp:doBody/>
    </div>
     <div class="card-footer">
        <p> &copy; Expert-Soft </p>
    </div>
 </div>
 </body>
 </html>