<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="utils.Utils" %>
<%@ page import="java.lang.Math" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<jsp:useBean id="orderDao" class="daos.impl.OrderDaoImpl"/>
<c:set var="orders" value="${orderDao.getAllOrders(user)}" scope="page"/>


<t:genericpage>
    <jsp:attribute name="pagetitle">
        Ordini
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderhistory.css" >
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/validate/jquery.validate.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/orderhistory.js"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 id="title" class="text-uppercase">Lista ordini:</h1>
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#TuttiGliOrdini">Tutti gli ordini</a></li>
                        <li><a data-toggle="tab" href="#OrdiniInCorso">Ordini in corso</a></li>
                        <li><a data-toggle="tab" href="#OrdiniCompletati">Ordini completati</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="TuttiGliOrdini" class="tab-pane fade in active">
                            <c:choose>
                                <c:when test="${not empty orders}">
                                    <c:forEach items="${orders}" var="order">
                                        <div class="panel-group">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row text-left">
                                                        <div class="col-md-4 col-xs-4 text-left">
                                                            <h5> Ordine n: ${order.getOrderID()}</h5>
                                                        </div>
                                                        <div class="col-md-4 col-xs-4 text-center">
                                                            <h5> Totale: ${Utils.getNDecPrice(order.getTotal(),2)}&euro;</h5>
                                                        </div>
                                                        <div class="col-md-4 col-xs-4 text-right">
                                                            <h5> Effettuato il: ${order.getDate().toString()}</h5>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-body">
                                                    <ul class="list-group">
                                                        <c:forEach items="${order.getProductList()}" var="po">
                                                            <!-- inizio prodotto -->
                                                            <li class="list-group-item">
                                                                <div class="row">
                                                                    <div class="col-lg-2 col-md-2">
                                                                        <img class="img-rounded img-responsive" src="${po.getProduct().imgBase64[0]}" alt="product image">
                                                                    </div>
                                                                    <div class="col-lg-5 col-md-4 col-xs-6">
                                                                        <h3 class="list-group-item-heading"><a class="resetcolor" href="${pageContext.request.contextPath}/product.jsp?product=${po.product.productID}&shop=${po.product.shopID}">${po.getProduct().getProductName()}</a></h3>
                                                                        <ul class="list-unstyled list-group-item-text">
                                                                            <li>Venditore: <a class="resetcolor" href="${pageContext.request.contextPath}/shop.jsp?id=${po.product.shopID}"><b>${po.getProduct().getShopName()}</b></a></li>
                                                                            <li>Prezzo: ${Utils.getNDecPrice(po.getFinalPrice(),2)}&euro;</li>
                                                                            <li>Quantità: ${po.getQuantity()} pz</li>
                                                                        </ul>
                                                                    </div>
                                                                    <div class="col-lg-3 col-md-3 col-xs-6 text-right" style="padding-top: 10px">
                                                                        <c:choose>
                                                                            <c:when test = "${po.getAddress().getAddressID() == 0}">
                                                                                <h4 class="list-group-item-heading">Ritiro presso il negozio.</h4>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <h4 class="list-group-item-heading">Spedito a:</h4>
                                                                                <ul class="list-unstyled list-group-item-text">
                                                                                    <li><b>${po.getAddress().getFirstName()} ${po.getAddress().getLastName()}</b></li>
                                                                                    <li>${po.getAddress().getAddress()}</li>
                                                                                    <li>${po.getAddress().getZip()}, ${po.getAddress().getCity()}</li>
                                                                                    <li>+39 ${po.getAddress().getTelephoneNumber()}</li>
                                                                                </ul>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                    <div class="col-lg-2 col-md-3 col-xs-12 text-center">
                                                                        <c:choose>
                                                                            <c:when test="${po.getStatus() == 0}">
                                                                                <div class="row">
                                                                                    <div class="col-xs-12 prodbuttonscol">
                                                                                        <a href="${pageContext.request.contextPath}/restricted/finishorder?order=${order.orderID}&product=${po.product.productID}&shop=${po.product.shopID}" class="btn btn-default btn-block margin-btn">Prodotto ritirato!</a>
                                                                                    </div>
                                                                                    <c:if test="${empty po.dispute}">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <button type="button" class="btn btn-default btn-block margin-btn" onclick="openDisputeModal(${order.orderID},${po.product.productID},${po.product.shopID})">Apri disputa</button>
                                                                                        </div>
                                                                                    </c:if>
                                                                                </div>
                                                                            </c:when>
                                                                            <c:when test="${po.getStatus() == 1}">
                                                                                <div class="row">
                                                                                    <div class="col-xs-12 prodbuttonscol">
                                                                                        <h3>Ordine Completato</h3>
                                                                                    </div>
                                                                                    <c:if test="${empty po.review}">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <button type="button" class="btn btn-default btn-block margin-btn" onclick="openReviewModal(${order.orderID},${po.product.productID},${po.product.shopID})">Lascia una recensione</button>
                                                                                        </div>
                                                                                    </c:if>
                                                                                </div>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <h3>Errore status</h3>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                </div>
                                                                <c:if test="${not empty po.review}">
                                                                    <div class="row prodreviewdispute">
                                                                        <div class="col-md-2 text-right" >
                                                                            <h4><b>Recensione:</b></h4>
                                                                        </div>
                                                                        <div class="col-md-8" >
                                                                            <h4>${po.review.title}</h4>
                                                                            <p>${po.review.description}</p>
                                                                        </div>
                                                                        <div class="col-md-2 text-center">
                                                                            <h4>
                                                                                <c:choose>
                                                                                    <c:when test="${Math.round(po.review.rating) == -1}">
                                                                                        <p>Nessuna valutazione data</p>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <c:if test="${Math.round(po.review.rating) > 0}">
                                                                                            <c:forEach begin="0" end="${Math.round(po.review.rating) - 1}" varStatus="loop">
                                                                                                <i class="fa fa-star" aria-hidden="true"></i>
                                                                                            </c:forEach>
                                                                                        </c:if>
                                                                                        <c:if test="${Math.round(po.review.rating) < 5}">
                                                                                            <c:forEach begin="0" end="${4 - Math.round(po.review.rating)}" varStatus="loop">
                                                                                                <i class="fa fa-star-o" aria-hidden="true"></i>
                                                                                            </c:forEach>
                                                                                        </c:if>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </h4>
                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                                <c:if test="${not empty po.dispute}">
                                                                    <div class="row prodreviewdispute">
                                                                        <div class="col-md-2 text-right" >
                                                                            <h4><b>Disputa:</b></h4>
                                                                        </div>
                                                                        <div class="col-md-8">
                                                                            <h4>${po.dispute.title}</h4>
                                                                            <p>${po.dispute.description}</p>
                                                                        </div>
                                                                        <div class="col-md-2 prodbuttonscol">
                                                                            <h4>
                                                                                Stato:&nbsp;&nbsp;
                                                                                <c:choose>
                                                                                    <c:when test="${po.dispute.status == 1}">
                                                                                        <span class="badge badge-success">Approvata</span>
                                                                                    </c:when>
                                                                                    <c:when test="${po.dispute.status == 2}">
                                                                                        <span class="badge badge-danger">Declinata</span>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <span class="badge badge-warning">In attesa</span>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </h4>
                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                            </li>
                                                            <!-- fine prodotto -->
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- fine ordine -->
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <h3>&nbsp;&nbsp;&nbsp;Non hai ancora effettuato alcun ordine</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div id="OrdiniInCorso" class="tab-pane fade">
                            <c:choose>
                                <c:when test="${not empty orders}">
                                    <c:forEach items="${orders}" var="order">
                                        <!-- inizio ordine -->
                                        <c:if test="${!order.isFinalized()}">
                                        <div class="panel-group">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row text-left">
                                                        <div class="col-md-4 col-xs-4 text-left">
                                                            <h5> Ordine n: ${order.getOrderID()}</h5>
                                                        </div>
                                                        <div class="col-md-4 col-xs-4 text-center">
                                                            <h5> Totale: ${Utils.getNDecPrice(order.getTotal(),2)}&euro;</h5>
                                                        </div>
                                                        <div class="col-md-4 col-xs-4 text-right">
                                                            <h5> Effettuato il: ${order.getDate().toString()}</h5>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-body">
                                                    <ul class="list-group">
                                                        <c:forEach items="${order.getProductList()}" var="po">
                                                            <c:if test="${po.getStatus() == 0}">
                                                                <!-- inizio prodotto -->
                                                                <li class="list-group-item">
                                                                    <div class="row">
                                                                        <div class="col-lg-2 col-md-2">
                                                                            <img class="img-rounded img-responsive" src="${po.getProduct().imgBase64[0]}" alt="product image">
                                                                        </div>
                                                                        <div class="col-lg-5 col-md-4 col-xs-6">
                                                                            <h3 class="list-group-item-heading"><a class="resetcolor" href="${pageContext.request.contextPath}/product.jsp?product=${po.product.productID}&shop=${po.product.shopID}">${po.getProduct().getProductName()}</a></h3>
                                                                            <ul class="list-unstyled list-group-item-text">
                                                                                <li>Venditore: <a class="resetcolor" href="${pageContext.request.contextPath}/shop.jsp?id=${po.product.shopID}"><b>${po.getProduct().getShopName()}</b></a></li>
                                                                                <li>Prezzo: ${Utils.getNDecPrice(po.getFinalPrice(),2)}&euro;</li>
                                                                                <li>Quantità: ${po.getQuantity()} pz</li>
                                                                            </ul>
                                                                        </div>
                                                                        <div class="col-lg-3 col-md-3 col-xs-6 text-right" style="padding-top: 10px">
                                                                            <c:choose>
                                                                                <c:when test = "${po.getAddress().getAddressID() == 0}">
                                                                                    <h4 class="list-group-item-heading">Ritiro presso il negozio.</h4>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <h4 class="list-group-item-heading">Spedito a:</h4>
                                                                                    <ul class="list-unstyled list-group-item-text">
                                                                                        <li><b>${po.getAddress().getFirstName()} ${po.getAddress().getLastName()}</b></li>
                                                                                        <li>${po.getAddress().getAddress()}</li>
                                                                                        <li>${po.getAddress().getZip()}, ${po.getAddress().getCity()}</li>
                                                                                        <li>+39 ${po.getAddress().getTelephoneNumber()}</li>
                                                                                    </ul>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </div>
                                                                        <div class="col-lg-2 col-md-3 col-xs-12 text-center">
                                                                            <c:choose>
                                                                                <c:when test="${po.getStatus() == 0}">
                                                                                    <div class="row">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <a href="${pageContext.request.contextPath}/restricted/finishorder?order=${order.orderID}&product=${po.product.productID}&shop=${po.product.shopID}" class="btn btn-default btn-block margin-btn">Prodotto ritirato!</a>
                                                                                        </div>
                                                                                        <c:if test="${empty po.dispute}">
                                                                                            <div class="col-xs-12 prodbuttonscol">
                                                                                                <button type="button" class="btn btn-default btn-block margin-btn" onclick="openDisputeModal(${order.orderID},${po.product.productID},${po.product.shopID})">Apri disputa</button>
                                                                                            </div>
                                                                                        </c:if>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:when test="${po.getStatus() == 1}">
                                                                                    <div class="row">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <h3>Ordine Completato</h3>
                                                                                        </div>
                                                                                        <c:if test="${empty po.review}">
                                                                                            <div class="col-xs-12 prodbuttonscol">
                                                                                                <button type="button" class="btn btn-default btn-block margin-btn" onclick="openReviewModal(${order.orderID},${po.product.productID},${po.product.shopID})">Lascia una recensione</button>
                                                                                            </div>
                                                                                        </c:if>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <h3>Errore status</h3>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </div>
                                                                    </div>
                                                                    <c:if test="${not empty po.review}">
                                                                        <div class="row prodreviewdispute">
                                                                            <div class="col-md-2 text-right" >
                                                                                <h4><b>Recensione:</b></h4>
                                                                            </div>
                                                                            <div class="col-md-8" >
                                                                                <h4>${po.review.title}</h4>
                                                                                <p>${po.review.description}</p>
                                                                            </div>
                                                                            <div class="col-md-2 text-center">
                                                                                <h4>
                                                                                    <c:choose>
                                                                                        <c:when test="${Math.round(po.review.rating) == -1}">
                                                                                            <p>Nessuna valutazione data</p>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            <c:if test="${Math.round(po.review.rating) > 0}">
                                                                                                <c:forEach begin="0" end="${Math.round(po.review.rating) - 1}" varStatus="loop">
                                                                                                    <i class="fa fa-star" aria-hidden="true"></i>
                                                                                                </c:forEach>
                                                                                            </c:if>
                                                                                            <c:if test="${Math.round(po.review.rating) < 5}">
                                                                                                <c:forEach begin="0" end="${4 - Math.round(po.review.rating)}" varStatus="loop">
                                                                                                    <i class="fa fa-star-o" aria-hidden="true"></i>
                                                                                                </c:forEach>
                                                                                            </c:if>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </h4>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${not empty po.dispute}">
                                                                        <div class="row prodreviewdispute">
                                                                            <div class="col-md-2 text-right" >
                                                                                <h4><b>Disputa:</b></h4>
                                                                            </div>
                                                                            <div class="col-md-8">
                                                                                <h4>${po.dispute.title}</h4>
                                                                                <p>${po.dispute.description}</p>
                                                                            </div>
                                                                            <div class="col-md-2 prodbuttonscol">
                                                                                <h4>
                                                                                    Stato:&nbsp;&nbsp;
                                                                                    <c:choose>
                                                                                        <c:when test="${po.dispute.status == 1}">
                                                                                            <span class="badge badge-success">Approvata</span>
                                                                                        </c:when>
                                                                                        <c:when test="${po.dispute.status == 2}">
                                                                                            <span class="badge badge-danger">Declinata</span>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            <span class="badge badge-warning">In attesa</span>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </h4>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                </li>
                                                                <!-- fine prodotto -->
                                                            </c:if>
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        </c:if>
                                        <!-- fine ordine -->
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <h3>&nbsp;&nbsp;&nbsp;Non hai ancora effettuato alcun ordine</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div id="OrdiniCompletati" class="tab-pane fade">
                            <c:choose>
                                <c:when test="${not empty orders}">
                                    <c:forEach items="${orders}" var="order">
                                        <!-- inizio ordine -->
                                        <c:if test="${order.isFinalized()}">
                                        <div class="panel-group">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row text-left">
                                                        <div class="col-md-4 col-xs-4 text-left">
                                                            <h5> Ordine n: ${order.getOrderID()}</h5>
                                                        </div>
                                                        <div class="col-md-4 col-xs-4 text-center">
                                                            <h5> Totale: ${Utils.getNDecPrice(order.getTotal(),2)}&euro;</h5>
                                                        </div>
                                                        <div class="col-md-4 col-xs-4 text-right">
                                                            <h5> Effettuato il: ${order.getDate().toString()}</h5>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-body">
                                                    <ul class="list-group">
                                                        <c:forEach items="${order.getProductList()}" var="po">
                                                            <c:if test="${po.getStatus() == 1}">
                                                                <!-- inizio prodotto -->
                                                                <li class="list-group-item">
                                                                    <div class="row">
                                                                        <div class="col-lg-2 col-md-2">
                                                                            <img class="img-rounded img-responsive" src="${po.getProduct().imgBase64[0]}" alt="product image">
                                                                        </div>
                                                                        <div class="col-lg-5 col-md-4 col-xs-6">
                                                                            <h3 class="list-group-item-heading"><a class="resetcolor" href="${pageContext.request.contextPath}/product.jsp?product=${po.product.productID}&shop=${po.product.shopID}">${po.getProduct().getProductName()}</a></h3>
                                                                            <ul class="list-unstyled list-group-item-text">
                                                                                <li>Venditore: <a class="resetcolor" href="${pageContext.request.contextPath}/shop.jsp?id=${po.product.shopID}"><b>${po.getProduct().getShopName()}</b></a></li>
                                                                                <li>Prezzo: ${Utils.getNDecPrice(po.getFinalPrice(),2)}&euro;</li>
                                                                                <li>Quantità: ${po.getQuantity()} pz</li>
                                                                            </ul>
                                                                        </div>
                                                                        <div class="col-lg-3 col-md-3 col-xs-6 text-right" style="padding-top: 10px">
                                                                            <c:choose>
                                                                                <c:when test = "${po.getAddress().getAddressID() == 0}">
                                                                                    <h4 class="list-group-item-heading">Ritiro presso il negozio.</h4>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <h4 class="list-group-item-heading">Spedito a:</h4>
                                                                                    <ul class="list-unstyled list-group-item-text">
                                                                                        <li><b>${po.getAddress().getFirstName()} ${po.getAddress().getLastName()}</b></li>
                                                                                        <li>${po.getAddress().getAddress()}</li>
                                                                                        <li>${po.getAddress().getZip()}, ${po.getAddress().getCity()}</li>
                                                                                        <li>+39 ${po.getAddress().getTelephoneNumber()}</li>
                                                                                    </ul>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </div>
                                                                        <div class="col-lg-2 col-md-3 col-xs-12 text-center">
                                                                            <c:choose>
                                                                                <c:when test="${po.getStatus() == 0}">
                                                                                    <div class="row">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <a href="${pageContext.request.contextPath}/restricted/finishorder?order=${order.orderID}&product=${po.product.productID}&shop=${po.product.shopID}" class="btn btn-default btn-block margin-btn">Prodotto ritirato!</a>
                                                                                        </div>
                                                                                        <c:if test="${empty po.dispute}">
                                                                                            <div class="col-xs-12 prodbuttonscol">
                                                                                                <button type="button" class="btn btn-default btn-block margin-btn" onclick="openDisputeModal(${order.orderID},${po.product.productID},${po.product.shopID})">Apri disputa</button>
                                                                                            </div>
                                                                                        </c:if>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:when test="${po.getStatus() == 1}">
                                                                                    <div class="row">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <h3>Ordine Completato</h3>
                                                                                        </div>
                                                                                        <c:if test="${empty po.review}">
                                                                                            <div class="col-xs-12 prodbuttonscol">
                                                                                                <button type="button" class="btn btn-default btn-block margin-btn" onclick="openReviewModal(${order.orderID},${po.product.productID},${po.product.shopID})">Lascia una recensione</button>
                                                                                            </div>
                                                                                        </c:if>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <h3>Errore status</h3>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </div>
                                                                    </div>
                                                                    <c:if test="${not empty po.review}">
                                                                        <div class="row prodreviewdispute">
                                                                            <div class="col-md-2 text-right" >
                                                                                <h4><b>Recensione:</b></h4>
                                                                            </div>
                                                                            <div class="col-md-8" >
                                                                                <h4>${po.review.title}</h4>
                                                                                <p>${po.review.description}</p>
                                                                            </div>
                                                                            <div class="col-md-2 text-center">
                                                                                <h4>
                                                                                    <c:choose>
                                                                                        <c:when test="${Math.round(po.review.rating) == -1}">
                                                                                            <p>Nessuna valutazione data</p>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            <c:if test="${Math.round(po.review.rating) > 0}">
                                                                                                <c:forEach begin="0" end="${Math.round(po.review.rating) - 1}" varStatus="loop">
                                                                                                    <i class="fa fa-star" aria-hidden="true"></i>
                                                                                                </c:forEach>
                                                                                            </c:if>
                                                                                            <c:if test="${Math.round(po.review.rating) < 5}">
                                                                                                <c:forEach begin="0" end="${4 - Math.round(po.review.rating)}" varStatus="loop">
                                                                                                    <i class="fa fa-star-o" aria-hidden="true"></i>
                                                                                                </c:forEach>
                                                                                            </c:if>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </h4>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${not empty po.dispute}">
                                                                        <div class="row prodreviewdispute">
                                                                            <div class="col-md-2 text-right" >
                                                                                <h4><b>Disputa:</b></h4>
                                                                            </div>
                                                                            <div class="col-md-8">
                                                                                <h4>${po.dispute.title}</h4>
                                                                                <p>${po.dispute.description}</p>
                                                                            </div>
                                                                            <div class="col-md-2 prodbuttonscol">
                                                                                <h4>
                                                                                    Stato:&nbsp;&nbsp;
                                                                                    <c:choose>
                                                                                        <c:when test="${po.dispute.status == 1}">
                                                                                            <span class="badge badge-success">Approvata</span>
                                                                                        </c:when>
                                                                                        <c:when test="${po.dispute.status == 2}">
                                                                                            <span class="badge badge-danger">Declinata</span>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            <span class="badge badge-warning">In attesa</span>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </h4>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                </li>
                                                                <!-- fine prodotto -->
                                                            </c:if>
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        </c:if>
                                        <!-- fine ordine -->
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <h3>&nbsp;&nbsp;&nbsp;Non hai ancora effettuato alcun ordine</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="opendisputemodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="opendisputecard" class="card card-signup centerize" data-background-color="orange">
                    <form id="opendisputeform" class="form" method="POST" action="${pageContext.request.contextPath}/restricted/opendispute">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up" >Apri Disputa</h4>
                        </div>
                        <div class="content">
                            <input id="orderIdDisputeModal" type="text" name="orderID" hidden value="">
                            <input id="productIdDisputeModal" type="text" name="productID" hidden value="">
                            <input id="shopIdDisputeModal" type="text" name="shopID" hidden value="">

                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-thumbs-o-down green" aria-hidden="true"></i>
                          </span>
                                <input id="titleDisputeModal" type="text" class="form-control required" name="title" placeholder="Titolo..." maxlength="50">
                            </div>
                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-bars green" aria-hidden="true"></i>
                          </span>
                                <input id="descriptionDisputeModal" type="text" class="form-control required" name="description" placeholder="Descrivi anomalia..." maxlength="100">
                            </div>
                        </div>
                        <div class="footer text-center" style="margin-top: 15px;">
                            <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;" onclick="$('#opendisputeform').submit();">Invia</a>
                            <a class="btn btn-default" style="margin-left: 20px; padding-left: 25px; padding-right: 25px;" onclick="$(function(){$('#opendisputemodal').modal('toggle');});">Chiudi</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal fade" id="openreviewmodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="openreviewcard" class="card card-signup centerize" data-background-color="orange">
                    <form id="openreviewform" class="form" method="POST" action="${pageContext.request.contextPath}/restricted/openreview">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up" >Recensione Prodotto</h4>
                        </div>
                        <div class="content">
                            <input id="orderIdReviewModal" type="text" class="hidden" name="orderID" placeholder="">
                            <input id="productIdReviewModal" type="text" class="hidden" name="productID" placeholder="">
                            <input id="shopIdReviewModal" type="text" class="hidden" name="shopID" placeholder="">

                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-star-o green" aria-hidden="true"></i>
                          </span>
                                <input id="titleProductReviewModal" type="text" class="form-control required" name="ptitle" placeholder="Titolo..." maxlength="50">
                            </div>
                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-bars green" aria-hidden="true"></i>
                          </span>
                                <input id="descriptionProductReviewModal" type="text" class="form-control required" name="pdescription" placeholder="Descrivi prodotto..." maxlength="150">
                            </div>
                            <div class="col-md-12 text-center stelle">
                                <i class="fa fa-star rating_star" aria-hidden="true" id="pstella_1" onmouseover="setPStar(this)"
                                    style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="pstella_2" onmouseover="setPStar(this)"
                                    style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="pstella_3" onmouseover="setPStar(this)"
                                    style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="pstella_4" onmouseover="setPStar(this)"
                                    style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="pstella_5" onmouseover="setPStar(this)"
                                    style="cursor:pointer"></i>&nbsp;
                                <input type="text" name="prating" hidden>
                            </div>
                        <div class="header header-primary text-center">
                            <h4 class="title title-up" >Recensione Negozio</h4>
                        </div>
                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-star-o green" aria-hidden="true"></i>
                          </span>
                                <input id="titleShopReviewModal" type="text" class="form-control required" name="stitle" placeholder="Titolo..." maxlength="50">
                            </div>
                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-bars green" aria-hidden="true"></i>
                          </span>
                                <input id="descriptionShopReviewModal" type="text" class="form-control required" name="sdescription" placeholder="Descrivi negozio..." maxlength="150">
                            </div>
                            <div class="col-md-12 text-center stelle">
                                <i class="fa fa-star rating_star" aria-hidden="true" id="sstella_1" onmouseover="setSStar(this)"
                                   style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="sstella_2" onmouseover="setSStar(this)"
                                   style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="sstella_3" onmouseover="setSStar(this)"
                                   style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="sstella_4" onmouseover="setSStar(this)"
                                   style="cursor:pointer"></i>&nbsp;
                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="sstella_5" onmouseover="setSStar(this)"
                                   style="cursor:pointer"></i>&nbsp;
                                <input type="text" name="srating" hidden>
                            </div>
                        </div>
                        <div class="footer text-center" style="margin-top: 15px;">
                            <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;" onclick="$('#openreviewform').submit();">Invia</a>
                            <a class="btn btn-default" style="margin-left: 20px; padding-left: 25px; padding-right: 25px;" onclick="$(function(){$('#openreviewmodal').modal('toggle');});">Chiudi</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpage>