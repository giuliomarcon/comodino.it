<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<jsp:useBean id="disputeDao" class="daos.impl.DisputeDaoImpl"/>
<c:set var="disputes" value="${disputeDao.allDisputes()}" scope="page"/>

<jsp:useBean id="productDao" class="daos.impl.ProductDaoImpl"/>
<c:set var="products" value="${productDao.allProducts()}" scope="page"/>

<jsp:useBean id="shopDao" class="daos.impl.ShopDaoImpl"/>
<c:set var="shops" value="${shopDao.allShops()}" scope="page"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Pannello Amministratore
    </jsp:attribute>

    <jsp:attribute name="pagecss">
        <link href="${pageContext.request.contextPath}/css/admin_panel.css" rel="stylesheet" type="text/css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/admin_panel.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#Dispute">Dispute</a></li>
                        <li><a data-toggle="tab" href="#Negozi">Negozi</a></li>
                        <li><a data-toggle="tab" href="#Prodotti">Prodotti</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="Dispute" class="tab-pane fade in active">
                            <div class="row" style="padding-left: 20px; padding-right: 20px">
                                <div class="col-xs-6">
                                    <h3 style="margin-top: 0">Dispute</h3>
                                </div>
                                <div class="col-xs-6">
                                    <button id="filtrodispute" type="button" class="btn btn-primary pull-right"
                                            onclick="filtraInAttesa()">
                                        Vedi solo dispute aperte
                                    </button>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <table id="disputeList" class="table table-striped table-hover">
                                    <thead>
                                    <tr>
                                        <th>
                                            Title
                                        </th>
                                        <th class="hidden-xs hidden-sm">
                                            Descrizione
                                        </th>
                                        <th>
                                            Data Creazione
                                        </th>
                                        <th style="text-align: center">
                                            Ord-Prod-Neg
                                        </th>
                                        <th style="text-align: center">
                                            Status
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${disputes}" var="dispute">
                                        <tr class="disputa ${(dispute.status == 0) ? 'inattesa':'terminata'}">
                                            <td>
                                                <b>${dispute.title}</b>
                                            </td>
                                            <td class="text hidden-xs hidden-sm">
                                                <span>${dispute.description}</span>
                                            </td>
                                            <td>
                                                <c:set var="dateParts" value="${fn:split(dispute.creationDate, ' ')}"
                                                       scope="page"/>
                                                <c:set var="date" value="${fn:split(dateParts[0], '-')}" scope="page"/>
                                                <c:set var="time" value="${fn:split(dateParts[1], ':')}" scope="page"/>
                                                    ${date[2]}/${date[1]} &nbsp;<span
                                                    style="font-size: small">h: ${time[0]}:${time[1]}</span>
                                            </td>
                                            <td style="text-align: center">
                                                    ${dispute.orderID} -
                                                        <a class="resetcolor" href="${pageContext.request.contextPath}/product.jsp?product=${dispute.productID}&shop=${dispute.shopID}">${dispute.productID}</a> -
                                                        <a class="resetcolor" href="${pageContext.request.contextPath}/shop.jsp?id=${dispute.shopID}">${dispute.shopID}</a>
                                            </td>
                                            <td style="text-align: center">
                                                <c:choose>
                                                    <c:when test="${dispute.status == 1}">
                                                        Prodotto Rimborsato e Venditore Segnalato
                                                    </c:when>
                                                    <c:when test="${dispute.status == 2}">
                                                        Disputa Declinata
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form action="${pageContext.request.contextPath}/restricted/admin/updatedispute"
                                                              method="POST">
                                                            <div class="btn-group">

                                                                <input name="orderID" value="${dispute.orderID}"
                                                                       type="text" hidden>
                                                                <input name="productID" value="${dispute.productID}"
                                                                       type="text" hidden>
                                                                <input name="shopID" value="${dispute.shopID}"
                                                                       type="text" hidden>

                                                                <button class="btn btn-default dropdown-toggle"
                                                                        data-toggle="dropdown">
                                                                    Seleziona azione &nbsp;&nbsp;<span
                                                                        class="caret"></span>
                                                                </button>
                                                                <button class="btn btn-success" data-toggle="salva"
                                                                        style="display: none;" type="submit">
                                                                    Salva
                                                                </button>
                                                                <ul class="dropdown-menu">
                                                                    <li>
                                                                        <a href="#">Rimborsa e Segnala</a>
                                                                        <input name="status" value="1" type="radio"
                                                                               hidden>
                                                                    </li>
                                                                    <li class="divider">
                                                                    <li>
                                                                        <a href="#">Declina</a>
                                                                        <input name="status" value="2" type="radio"
                                                                               hidden>
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div id="Negozi" class="tab-pane fade in">
                            <table id="shopList" class="table table-striped">
                                <thead>
                                <tr>
                                    <th>
                                        ShopID
                                    </th>
                                    <th>
                                        Nome
                                    </th>
                                    <th class="hidden-xs hidden-sm">
                                        Descrizione
                                    </th>
                                    <th style="text-align: center">
                                        Rating
                                    </th>
                                    <th style="text-align: center">
                                        Pagina
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${shops}" var="shop">
                                    <tr>
                                        <td>
                                            <b>${shop.shopID}</b>
                                        </td>
                                        <td>
                                                ${shop.name}
                                        </td>
                                        <td class="text hidden-xs hidden-sm">
                                            <span>${shop.description}</span>
                                        </td>
                                        <td style="text-align: center">
                                            <c:choose>
                                                <c:when test="${Math.round(shop.rating) == -1}">
                                                    Nessuna recensione
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${Math.round(shop.rating) > 0}">
                                                        <c:forEach begin="0" end="${Math.round(shop.rating) - 1}"
                                                                   varStatus="loop">
                                                            <i class="fa fa-star" aria-hidden="true"></i>
                                                        </c:forEach>
                                                    </c:if>
                                                    <c:if test="${Math.round(shop.rating) < 5}">
                                                        <c:forEach begin="0" end="${4 - Math.round(shop.rating)}"
                                                                   varStatus="loop">
                                                            <i class="fa fa-star-o" aria-hidden="true"></i>
                                                        </c:forEach>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="text-align: center">
                                            <a style="text-decoration: none"
                                               href="${pageContext.request.contextPath}/shop.jsp?id=${shop.shopID}">
                                                Vedi >>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div id="Prodotti" class="tab-pane fade in">
                            <table id="productList" class="table table-striped">
                                <thead>
                                <tr>
                                    <th>
                                        ProductID
                                    </th>
                                    <th>
                                        Titolo
                                    </th>
                                    <th class="hidden-xs hidden-sm">
                                        Descrizione
                                    </th>
                                    <th style="text-align: center">
                                        Rating
                                    </th>
                                    <th style="text-align: center">
                                        Pagina Prodotto
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${products}" var="prod">
                                    <tr>
                                        <td>
                                            <b>${prod.productID}</b>
                                        </td>
                                        <td>
                                                ${prod.productName}
                                        </td>
                                        <td class="text hidden-xs hidden-sm">
                                            <span>${prod.description}</span>
                                        </td>
                                        <td style="text-align: center">
                                            <c:choose>
                                                <c:when test="${Math.round(prod.rating) == -1}">
                                                    Nessuna recensione
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${Math.round(prod.rating) > 0}">
                                                        <c:forEach begin="0" end="${Math.round(prod.rating) - 1}"
                                                                   varStatus="loop">
                                                            <i class="fa fa-star" aria-hidden="true"></i>
                                                        </c:forEach>
                                                    </c:if>
                                                    <c:if test="${Math.round(prod.rating) < 5}">
                                                        <c:forEach begin="0" end="${4 - Math.round(prod.rating)}"
                                                                   varStatus="loop">
                                                            <i class="fa fa-star-o" aria-hidden="true"></i>
                                                        </c:forEach>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="text-align: center">
                                            <a style="text-decoration: none"
                                               href="${pageContext.request.contextPath}/product.jsp?product=${prod.productID}&shop=${prod.shopID}">
                                                Vedi >>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpage>