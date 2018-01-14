<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="utils.Utils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<c:set var="cart" value="${user.getCart(true)}" scope="page"/>
<c:set var="total" value="0" scope="page"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Il mio carrello
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css" >
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/cart.js"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="display-1">Riepilogo Carrello</h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <ul id="cartlist" class="list-group">
                        <c:choose>
                            <c:when test="${not empty cart}">
                                <c:forEach items="${cart}" var="item">
                                    <c:set var="total" value="${total + item.getProduct().getActualPrice() * item.getQuantity()}" scope="page"/>
                                    <li id="${item.getProduct().getProductID()}_${item.getProduct().getShopID()}" class="list-group-item">
                                        <div class="cart-item">
                                            <div id="c_row-4col" class="row pi-draggable" draggable="true">
                                                <div class="col-md-2 itemimg" id="prodimg">
                                                    <img class="img-fluid d-block my-2" src="${item.getProduct().imgBase64[0]}">
                                                </div>
                                                <div class="col-md-8">
                                                    <a class="resetcolor" href="${pageContext.request.contextPath}/product.jsp?product=${item.getProduct().productID}&shop=${item.getProduct().shopID}"><h3 class="itemtitle">${item.getProduct().getProductName()}</h3></a>
                                                    <p id="c_lead" class="lead pi-draggable itemseller" draggable="true">Venduto da:&nbsp;
                                                        <a href="${pageContext.request.contextPath}/shop.jsp?id=${item.getProduct().shopID}" style="font-size: 18px">${item.getProduct().getShopName()}</a>
                                                    </p>
                                                    <h2 class="itemprice" id="price_${item.getProduct().getProductID()}_${item.getProduct().getShopID()}">${Utils.getNDecPrice(item.getProduct().getActualPrice(),2)}&euro;</h2>
                                                </div>
                                                <div class="col-md-1">
                                                    <div class="itemquantity">
                                                        <p>Quantità:</p>
                                                        <div class="quantity">
                                                            <input type="number" data-prod="${item.getProduct().getProductID()}" data-shop="${item.getProduct().getShopID()}" min="1" step="1" value="${item.getQuantity()}" id="quantity_${item.getProduct().getProductID()}_${item.getProduct().getShopID()}" onfocus="updatePrice(${item.getProduct().getProductID()},'+',${item.getProduct().getShopID()});">
                                                            <div class="quantity-nav">
                                                                <div class="quantity-button quantity-up" onclick="updatePrice(${item.getProduct().getProductID()},'+',${item.getProduct().getShopID()});">+</div>
                                                                <div class="quantity-button quantity-down" onclick="updatePrice(${item.getProduct().getProductID()},'-',${item.getProduct().getShopID()});">-</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-1 text-center">
                                                    <div class="btn btn-danger btn-xs cestino" onclick="removeItem(${item.getProduct().getProductID()}, ${item.getProduct().getShopID()})">
                                                        <i class="fa fa-trash" aria-hidden="true"></i>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <li class="list-group-item text-center nessunbordo"><h3>Il carrello è vuoto, aggiungi qualche prodotto!</h3></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12" style="padding-right: 30px">
                    <h1 class="total" id="total" style="text-align: right; margin: 5px 0">Totale: ${Utils.getNDecPrice(total,2)}&euro;</h1>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <c:choose>
                                <c:when test="${total > 0}">
                                    <a class="btn btn-primary btn-xs" href="<c:url value="/restricted/checkout.jsp"/>" id="nextbtn">Procedi all'acquisto <i class="fa fa-angle-double-right" aria-hidden="true"></i></a>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-primary btn-xs" href="<c:url value="/index.jsp"/>">Vai alla homepage</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpage>