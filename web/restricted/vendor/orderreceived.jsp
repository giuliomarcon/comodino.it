<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="utils.Utils" %>
<%@ page import="java.lang.Math" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="shop" class="main.Shop" scope="request"/>
<jsp:useBean id="orderDao" class="daos.impl.OrderDaoImpl"/>
<c:set var="orders" value="${orderDao.getAllOrders(shop)}" scope="page"/>


<t:genericpage>
    <jsp:attribute name="pagetitle">
        Ordini
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderhistory.css" >
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/validate/jquery.validate.min.js"></script>
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
                                        <!-- inizio ordine -->
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
                                                                                <h4 class="list-group-item-heading">Da spedire a:</h4>
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
                                                                                    <h3>Ordine non ricevuto</h3>
                                                                                </div>
                                                                            </c:when>
                                                                            <c:when test="${po.getStatus() == 1}">
                                                                                <div class="row">
                                                                                    <div class="col-xs-12">
                                                                                        <h3>Ordine Completato</h3>
                                                                                    </div>
                                                                                </div>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <h3>Errore status</h3>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                </div>
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
                                                                                    <h4 class="list-group-item-heading">Da spedire a:</h4>
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
                                                                                        <h3>Ordine non ricevuto</h3>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:when test="${po.getStatus() == 1}">
                                                                                    <div class="row">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <h3>Ordine Completato</h3>
                                                                                        </div>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <h3>Errore status</h3>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- fine ordine -->
                                        </c:if>
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
                                                                                    <h4 class="list-group-item-heading">Da spedire a:</h4>
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
                                                                                        <h3>Ordine non ricevuto</h3>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:when test="${po.getStatus() == 1}">
                                                                                    <div class="row">
                                                                                        <div class="col-xs-12 prodbuttonscol">
                                                                                            <h3>Ordine Completato</h3>
                                                                                        </div>
                                                                                    </div>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <h3>Errore status</h3>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </div>
                                                                    </div>
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
    </jsp:body>
</t:genericpage>