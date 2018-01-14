<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="user" class="main.User" scope="session"/>

<jsp:useBean id="shopDao" class="daos.impl.ShopDaoImpl"/>
<c:set var="shop" value="${shopDao.getShop(user.shopID)}" scope="page"/>

<jsp:useBean id="reviewDao" class="daos.impl.ReviewDaoImpl"/>
<c:set var="productReviewList" value="${reviewDao.getVendorProductReviews(shop.shopID)}" scope="page"/>
<c:set var="shopReviewList" value="${reviewDao.getShopReviews(shop.shopID)}" scope="page"/>


<t:genericpage>
    <jsp:attribute name="pagetitle">
        Recensioni
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reviews.css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/reviews.js"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 id="title" class="text-uppercase">Recensioni</h1>
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#RecensioniProdotti">Recensioni Prodotti</a></li>
                        <li><a data-toggle="tab" href="#RecensioniNegozio">Recensioni Negozio</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="RecensioniProdotti" class="tab-pane fade in active">
                            <!-- inizio review -->
                            <c:choose>
                                <c:when test="${not empty productReviewList}">
                                    <c:forEach items="${productReviewList}" var="review">
                                        <c:set var="product" value="${reviewDao.getReviewProduct(review.productID, shop.shopID)}"/>

                                        <div class="review">
                                            <div class="row ordBody">
                                                <div class="row ordItem">
                                                    <div class="col-xs-12 col-md-2">
                                                        <figure class="pull-left">
                                                            <img class="media-object img-rounded img-responsive image-centre" src="${product.imgBase64[0]}" alt="product image" height="" width="200px"> </figure>
                                                    </div>
                                                    <div class="col-xs-12 col-md-7">
                                                        <h3><a style="color:#2c3e50" href="${pageContext.request.contextPath}/product.jsp?product=${product.productID}&shop=${shop.shopID}">${product.productName}</a></h3>
                                                        <p>
                                                            <c:set var="author" value="${reviewDao.getReviewAuthor(review.userID)}"
                                                                   scope="page"/>

                                                            <c:set var="dateParts" value="${fn:split(review.creationDate, ' ')}"
                                                                   scope="page"/>
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
                                                        <p><b>${review.title}</b></p>
                                                        <p> ${review.description}</p>
                                                    </div>
                                                    <div class="col-lg-2 col-md-3 col-xs-12 text-center">
                                                        <c:choose>
                                                            <c:when test="${reviewDao.isReviewReplied(review.reviewID) == 0}">
                                                                <button type="button" class="btn btn-default margin-btn" onclick="openReviewReplyModal(${review.reviewID})">Rispondi alla recensione</button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <h4>La recensione ha già una risposta.</h4>
                                                            </c:otherwise>
                                                        </c:choose>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>

                                    <!-- fine review -->

                        </c:when>
                        <c:otherwise>
                            <h3>&nbsp;&nbsp;&nbsp;Non ci sono recensioni</h3>
                        </c:otherwise>
                        </c:choose>
                        </div>
                        <div id="RecensioniNegozio" class="tab-pane fade">
                            <!-- inizio review -->
                            <c:choose>
                                <c:when test="${not empty shopReviewList}">
                                    <c:forEach items="${shopReviewList}" var="review">

                                        <div class="review">
                                            <div class="row ordBody">
                                                <div class="row ordItem">
                                                    <div class="col-xs-12 col-md-12">
                                                        <h3>${review.title}</h3>
                                                        <p>
                                                            <c:set var="dateParts" value="${fn:split(review.creationDate, ' ')}"
                                                                   scope="page"/>
                                                            <c:set var="date" value="${fn:split(dateParts[0], '-')}" scope="page"/>
                                                            <c:set var="time" value="${fn:split(dateParts[1], ':')}" scope="page"/>

                                                            <span style="font-size: small;">${date[2]}/${date[1]} &nbsp;h: ${time[0]}:${time[1]}</span>
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
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>

                                    <!-- fine review -->

                                </c:when>
                                <c:otherwise>
                                    <h3>&nbsp;&nbsp;&nbsp;Non ci sono recensioni</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="openreviewreplymodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="openreviewreplycard" class="card card-signup centerize" data-background-color="orange">
                    <form id="openreviewreplyform" class="form" method="POST"
                          action="${pageContext.request.contextPath}/restricted/vendor/openreviewreply">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Rispondi alla recensione</h4>
                        </div>
                        <div class="content">
                            <input id="reviewIdReviewReplyModal" type="text" hidden required name="reviewID" placeholder="">

                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-user-o green" aria-hidden="true"></i>
                          </span>
                                <input type="text" class="form-control" name="description" rows="5"
                                       placeholder="Risposta..." maxlength="250">
                            </div>
                        </div>
                        <div class="footer text-center" style="margin-top: 15px;">
                            <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;"
                               onclick="$('#openreviewreplyform').submit();">Invia</a>
                            <a class="btn btn-default"
                               style="margin-left: 20px; padding-left: 25px; padding-right: 25px;"
                               onclick="$(function(){$('#openreviewreplymodal').modal('toggle');});">Chiudi</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpage>