<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product Details">

    <div class="row" >
        <div class="col-1 border-right" style="background-image: url(${pageContext.request.contextPath}/images/pattern.svg);"></div>
        <div class="col-7" style="table-layout: fixed; text-align: center;">
            <h4>${phone.model}</h4>
            <img style="width: 270px;"
                     src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
            <p>${phone.description}</p>
            <div style="color: #ffffff; font-size: 20px; font-weight:bold; background-color: #343a40">
                Price: <fmt:formatNumber value="${phone.price}" type="currency" currencySymbol="$"/>
            </div>
            <p></p>
            <div class="row justify-content-center">
                <input style="text-align: right; width: 200px; margin-right: 10px" class="form-control" type="text" id="quantityAddToCart-${phone.id}" value="1"/>
                <button class="btn btn-dark" onclick="add(${phone.id});"> Add to cart </button>
            </div>
            <p style="margin-top: 10px" id="message-${phone.id}"></p>
        </div>
        <div class="col-1 border-left" style="background-image: url(${pageContext.request.contextPath}/images/pattern.svg)"></div>
        <div class="col-3">
            <div class="p-2 bd-highlight">
                <table class="table" >
                    <thead class="thead-dark">
                    <th>Display</th>
                    <th></th>
                    <tr style="background-color: #f8f8f8">
                        <td>Size</td>
                        <td>${phone.displaySizeInches}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Resolution</td>
                        <td>${phone.displayResolution}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Technology</td>
                        <td>${phone.displayTechnology}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Pixel density</td>
                        <td>${phone.pixelDensity}</td>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="p-2 bd-highlight">
                <table class="table">
                    <thead class="thead-dark">
                    <th>Dimensions & weight</th>
                    <th></th>
                    <tr style="background-color: #f8f8f8">
                        <td>Length</td>
                        <td>${phone.lengthMm} ${not empty phone.lengthMm ? 'mm' : ''}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Width</td>
                        <td>${phone.widthMm} ${not empty phone.widthMm ? 'mm' : ''} </td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Weight</td>
                        <td>${phone.weightGr} ${not empty phone.weightGr ? 'gr' : ''}</td>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="p-2 bd-highlight">
                <table class="table">
                    <thead class="thead-dark">
                    <th>Camera</th>
                    <th></th>
                    <tr style="background-color: #f8f8f8">
                        <td>Front</td>
                        <td>${phone.frontCameraMegapixels} ${not empty phone.frontCameraMegapixels ? 'megapixels' : ''}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Back</td>
                        <td>${phone.backCameraMegapixels} ${not empty phone.backCameraMegapixels ? 'megapixels' : ''}</td>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="p-2 bd-highlight">
                <table class="table">
                    <thead class="thead-dark">
                    <th>Battery</th>
                    <th></th>
                    <tr style="background-color: #f8f8f8">
                        <td>Talk time</td>
                        <td>${phone.talkTimeHours} ${not empty phone.widthMm ? 'hours' : ''}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Stand by time</td>
                        <td>${phone.standByTimeHours} ${not empty phone.widthMm ? 'hours' : ''}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Battery capacity</td>
                        <td>${phone.batteryCapacityMah} ${not empty phone.widthMm ? 'mAh' : ''}</td>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="p-2 bd-highlight">
                <table class="table">
                    <thead class="thead-dark">
                    <th>Other</th>
                    <th></th>
                    <tr style="background-color: #f8f8f8">
                        <td>Colors</td>
                        <td>
                            <c:forEach var="color" items="${phone.colors}" varStatus="loopStatus">
                                ${color.code}
                                <c:if test="${!loopStatus.last}">,</c:if>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Device type</td>
                        <td>${phone.deviceType}</td>
                    </tr>
                    <tr style="background-color: #f8f8f8">
                        <td>Bluetooth</td>
                        <td>${phone.bluetooth}</td>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</tags:master>
