<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.lang.Math" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" class="main.Product" scope="request"/>
<jsp:useBean id="reviewDao" class="daos.impl.ReviewDaoImpl"/>
<c:set var="reviewList" value="${reviewDao.getProductReviews(product.productID)}" scope="page"/>
<jsp:useBean id="ShopDao" class="daos.impl.ShopDaoImpl"/>
<c:set var="shopsList" value="${ShopDao.getPhysicalShopsByProduct(product.productID)}" scope="page"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        ${product.productName}
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script>
            var map, infoWindow;

            function initMap() {
                map = new google.maps.Map(document.getElementById('map'), {
                    center: {lat: 46.074, lng: 11.121},
                    zoom: 7
                });
                infoWindow = new google.maps.InfoWindow;

                // Try HTML5 geolocation.
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        var pos = {
                            lat: position.coords.latitude,
                            lng: position.coords.longitude
                        };

                        infoWindow.setPosition(pos);
                        infoWindow.setContent('Location found.');
                        infoWindow.open(map);
                        map.setCenter(pos);
                    }, function () {
                        handleLocationError(true, infoWindow, map.getCenter());
                    });
                } else {
                    // Browser doesn't support Geolocation
                    handleLocationError(false, infoWindow, map.getCenter());
                }
                <c:forEach items="${shopsList}" var="shop">
                var mark = {lat: ${shop.latitude}, lng: ${shop.longitude}};
                infoWindow = new google.maps.InfoWindow({
                    content: '${shop.name}'
                });
                google.maps.event.addListenerOnce(map, 'idle', function () {
                    // map is ready
                });

                var marker = new google.maps.Marker({
                    position: mark,
                    title: '${shop.name}',
                    infowindow: infoWindow,
                    map: map
                });
                google.maps.event.addListener(marker, 'click', function () {

                    infoWindow.setContent('<a class="resetcolor" href="${pageContext.request.contextPath}/product.jsp?product=${product.productID}&shop=${shop.shopID}">${shop.name}</a>');
                    infoWindow.open(map, this);
                });
                google.maps.event.addListener(map, 'tilesloaded', function () {
                    // Visible tiles loaded!
                });
                </c:forEach>
            }

            function handleLocationError(browserHasGeolocation, infoWindow, pos) {
                infoWindow.setPosition(pos);
                infoWindow.setContent(browserHasGeolocation ?
                    'Error: The Geolocation service failed.' :
                    'Error: Your browser doesn\'t support geolocation.');
                infoWindow.open(map);
            }
        </script>

        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDNMIz_QgiWP6ayg3icP3ZmLXt6OE_Qync&callback=initMap">
        </script>
        <script src="${pageContext.request.contextPath}/js/addtocart.js"></script>
        <script src="${pageContext.request.contextPath}/js/product.js"></script>

    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <section class="section-white">
                        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <c:forEach items="${product.imgBase64}" var="imgBase64" varStatus="status">
                                    <div class="item <c:if test='${status.first}'>active</c:if>">
                                        <img src='${imgBase64}' alt='images Here' width="100" height="100"/>
                                    </div>
                                </c:forEach>
                            </div>

                            <!-- Controls -->
                            <a class="left carousel-control" href="#carousel-example-generic" data-slide="prev">
                                <span class="glyphicon glyphicon-chevron-left"></span>
                            </a>
                            <a class="right carousel-control" href="#carousel-example-generic" data-slide="next">
                                <span class="glyphicon glyphicon-chevron-right"></span>
                            </a>
                        </div>
                    </section>
                </div>
                <div class="col-md-8">
                    <h1>${product.productName}</h1>
                    <c:choose>
                        <c:when test="${product.price != product.actualPrice}">
                            <h2><span class="strikethrough"><fmt:formatNumber minFractionDigits="2"
                                                                              maxFractionDigits="2"
                                                                              value="${product.price}"/> €</span>&nbsp;<span
                                    class="text-right"><fmt:formatNumber minFractionDigits="2" maxFractionDigits="2"
                                                                         value="${product.actualPrice}"/> €</span></h2>
                        </c:when>
                        <c:otherwise>
                            <h2><fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${product.price}"/>
                                €</h2>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${Math.round(product.rating) == -1}">
                            Nessuna recensione
                        </c:when>
                        <c:otherwise>
                            <c:if test="${Math.round(product.rating) > 0}">
                                <c:forEach begin="0" end="${Math.round(product.rating) - 1}" varStatus="loop">
                                    <i class="fa fa-star" aria-hidden="true"></i>
                                </c:forEach>
                            </c:if>
                            <c:if test="${Math.round(product.rating) < 5}">
                                <c:forEach begin="0" end="${4 - Math.round(product.rating)}" varStatus="loop">
                                    <i class="fa fa-star-o" aria-hidden="true"></i>
                                </c:forEach>
                            </c:if>
                            <c:choose>
                                <c:when test="${fn:length(reviewList) == 1}">
                                    &nbsp&nbsp<span class="text-right">1 recensione</span>
                                </c:when>
                                <c:otherwise>
                                    &nbsp&nbsp<span class="text-right">${fn:length(reviewList)} recensioni</span>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    <h4><span style="font-size: 20px">Venduto da:</span> <a
                            href="${pageContext.request.contextPath}/shop.jsp?id=${product.shopID}">${product.shopName}</a>
                        <c:if test="${ShopDao.hasOtherShops(product.productID) > 1}">e da altri <a href="javascript:void(0);" onclick="openModal('${product.productName}');"> ${(ShopDao.hasOtherShops(product.productID))-1} </a> venditori</c:if>
                    </h4>
                    <a id="tomap" class="btn btn-primary" href="#"><i class="fa fa-fw fa-home pull-left"></i>Visualizza
                        venditori nelle
                        vicinanze</a>
                    <p><br>${product.description}</p>
                    <c:choose>
                        <c:when test="${product.quantity > 0}">
                            <h3>Disponibilità: <span>${product.quantity} pezzi</span></h3>
                        </c:when>
                        <c:otherwise>
                            <h3>Il prodotto non è al momento disponibile.</h3>
                        </c:otherwise>
                    </c:choose>
                    <a href="javascript:void(0);" class="btn btn-primary"
                       onclick="addToCart('${product.productID}','${product.shopID}');">
                        <i class="fa fa-fw pull-left fa-shopping-cart"></i> Aggiungi al carrello
                    </a>
                </div>
            </div>
        </div>
        <section id="reviewanchor"></section>
        <div class="container"  style="margin-top: 30px">
            <div class="row">
                <div class="col-md-12">
                    <h1 style="margin-top: 0">Recensioni</h1>
                    <c:if test="${fn:length(reviewList) > 0}">
                        <c:if test="${Math.round(product.rating) > 0}">
                            <c:forEach begin="0" end="${Math.round(product.rating) - 1}" varStatus="loop">
                                <i class="fa fa-star" aria-hidden="true"></i>
                            </c:forEach>
                        </c:if>
                        <c:if test="${Math.round(product.rating) < 5}">
                            <c:forEach begin="0" end="${4 - Math.round(product.rating)}" varStatus="loop">
                                <i class="fa fa-star-o" aria-hidden="true"></i>
                            </c:forEach>
                        </c:if>

                        <c:choose>
                            <c:when test="${fn:length(reviewList) == 1}">
                                &nbsp&nbsp<span class="text-right">1 recensione</span>
                            </c:when>
                            <c:otherwise>
                                &nbsp&nbsp<span class="text-right">${fn:length(reviewList)} recensioni</span>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </div>
            </div>
            <!-- inizio review -->
            <c:choose>
                <c:when test="${not empty reviewList}">
                    <c:forEach items="${reviewList}" var="review">
                        <div class="review">
                            <h3>${review.title}</h3>
                            <p>
                                <c:set var="author" value="${reviewDao.getReviewAuthor(review.userID)}" scope="page"/>
                                <c:set var="dateParts" value="${fn:split(review.creationDate, ' ')}" scope="page"/>
                                <c:set var="date" value="${fn:split(dateParts[0], '-')}" scope="page"/>
                                <c:set var="time" value="${fn:split(dateParts[1], ':')}" scope="page"/>

                                <b>${author.firstName} ${author.lastName}</b> - <span
                                    style="font-size: small;">${date[2]}/${date[1]} &nbsp;h: ${time[0]}:${time[1]}</span>
                            </p>
                            <p>
                                <c:if test="${review.rating > 0}">
                                    <c:forEach begin="0" end="${review.rating - 1}" varStatus="loop">
                                        <i class="fa fa-star" aria-hidden="true"></i>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${review.rating < 5}">
                                    <c:forEach begin="0" end="${4 - review.rating}" varStatus="loop">
                                        <i class="fa fa-star-o" aria-hidden="true"></i>
                                    </c:forEach>
                                </c:if>
                            </p>
                            <p> ${review.description}</p>
                            <c:if test="${reviewDao.isReviewReplied(review.reviewID) != 0}">
                                <c:set var="reviewReply" value="${reviewDao.getProductReviewReply(review.reviewID)}"
                                       scope="page"/>
                                <div class="row prodreviewreply">
                                    <div class="col-md-2 text-right">
                                        <h5><b>Risposta del venditore:</b></h5>
                                    </div>
                                    <div class="col-md-8">
                                        <p>${reviewReply.description}</p>
                                    </div>
                                </div>
                            </c:if>

                        </div>
                    </c:forEach>

                    <!-- fine review -->
                </c:when>
                <c:otherwise>
                    <h3>&nbsp;&nbsp;&nbsp;Non ci sono recensioni</h3>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="container" style="margin-top: 30px; margin-bottom: 30px">
            <h1 style="margin-top: 0; margin-bottom: 20px;">Negozi nelle vicinanze</h1>
            <div id="map"></div>

        </div>

        <!-- Modal -->
        <span class="vendormodal">
            <div class="modal fade" id="vendorsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="row">
                    <div class="card card-signup centerize" data-background-color="orange" id="signup_login_card">
                                <span class="form" id="form">
                                    <span style="float:right">
                                        <button type="button" class="close mod-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                    </span>
                                    <div class="header header-primary text-center">
                                        <h4 class="title title-up" id="card_titolo_vendors">Piano cottura</h4>
                                        <p class="white subtitolo">
                                            Disponibile anche da:
                                        </p>
                                    </div>
                                    <div class="content">
                                        <div class="row text-center">
                                            <div class="content-modal-vendors" id="content-modal-vendors">

                                            </div>
                                        </div>
                                    </div>
                                    <div class="footer text-center">
                                        <div id="footer_vendors">

                                        </div>
                                        <div class="row" style="margin-top:10px" id="pagination_numbers_vendors">

                                        </div>
                                    </div>
                                </span>
                    </div>
                </div>
            </div>
        </span>
    </jsp:body>

</t:genericpage>