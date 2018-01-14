<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="utils.Utils" %>
<%@ page import="java.lang.Math" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="categories" class="java.util.HashSet" scope="request"/>
<jsp:useBean id="vendors" class="java.util.HashSet" scope="request"/>
<jsp:useBean id="geozone" class="java.util.HashSet" scope="request"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Ricerca
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/animate.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/search.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/bootstrap-slider-master/bootstrap-slider-master/dist/css/bootstrap-slider.css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/addtocart.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/search.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-slider-master/bootstrap-slider-master/dist/bootstrap-slider.js"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="container banner">
            <h5>
                <a href="index.jsp">Home</a>
                <c:if test="${param.cat != null}">
                    &nbsp;<i class="fa fa-angle-right"></i>&nbsp;
                    <a class="text-capitalize" href="search.jsp?cat=${param.cat}&q=">${param.cat}</a>
                </c:if>

                <span class="text-capitalize">
                    :&nbsp;
                    <c:if test='${!param.q.equals("")}'>
                        <b>"${param.q}"</b>
                    </c:if>
                    <small>(${products.size()} Risultati)</small>
                </span>
            </h5>
        </div>

        <div class="container invisible-container">
            <div class="row">
                <div class="col-lg-2 col-md-3">
                    <div class="sidebar">
                        <h4 class="text-center collapsed" data-toggle="collapse" data-target="#categorie_accordion"
                            style="cursor:pointer;">Categorie <span class="caret"></span></h4>
                        <ul class="list-group collapse" aria-expanded="false" id="categorie_accordion">
                            <c:if test="${not empty categories}">
                                <c:forEach var="icat" items="${categories}">
                                    <c:choose>
                                        <c:when test="${param.cat == icat}">
                                            <li class="list-group-item"><input type="radio" name="${icat}" value="${icat}"
                                                                               onclick="filterRadio(this,'cat');"
                                                                               checked/> ${icat}</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="list-group-item"><input type="radio" name="${icat}" value="${icat}"
                                                                               onclick="filterRadio(this,'cat');"/> ${icat}</li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:if>
                        </ul>
                        <h4 class="text-center collapsed" data-toggle="collapse" data-target="#venditori_accordion"
                            style="cursor:pointer;">Venditori <span class="caret"></span></h4>
                        <ul class="list-group collapse" aria-expanded="false" id="venditori_accordion">
                            <c:set var="vendoritem" scope="page" value="${paramValues.get('vendor')}"/>

                            <c:if test="${not empty vendors}">
                                <c:choose>
                                    <c:when test="${not empty vendoritem}">
                                        <c:forEach var="iven" items="${vendors}">
                                            <c:set var="found" value="false" scope="page"/>
                                            <c:forEach var="ivan" items="${vendoritem}">
                                                <c:if test="${ivan eq iven}">
                                                    <c:set var="found" value="true" scope="page"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${found eq true}">
                                                    <li class="list-group-item"><input type="checkbox" iven="${iven}"
                                                                                       name="${iven}" value="${iven}"
                                                                                       onclick="filter(this,'vendor');"/> ${iven}
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="list-group-item"><input type="checkbox" civen="${iven}"
                                                                                       name="${iven}" value="${iven}"
                                                                                       onclick="filter(this,'vendor');"
                                                                                       checked/> ${iven}</li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="iven" items="${vendors}">
                                            <li class="list-group-item"><input type="checkbox" name="${iven}" value="${iven}"
                                                                               onclick="filter(this,'vendor');"
                                                                               checked/> ${iven}</li>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </ul>
                        <h4 class="text-center ${Utils.isNullOrEmpty(param.geo)?"collapsed" : ""}" data-toggle="collapse" data-target="#geozone_accordion" style="cursor:pointer;">
                            Area Geografica <span class="caret"></span></h4>
                        <ul class="list-group ${Utils.isNullOrEmpty(param.geo)?"collapse" : ""}" aria-expanded="false" id="geozone_accordion">
                            <c:set var="geozoneitem" scope="page" value="${paramValues.get('geo')}"/>

                            <c:if test="${not empty geozone}">
                                <c:choose>
                                    <c:when test="${not empty geozoneitem}">
                                        <c:forEach var="iven" items="${geozone}">
                                            <c:set var="found" value="false" scope="page"/>
                                            <c:forEach var="ivan" items="${geozoneitem}">
                                                <c:if test="${ivan eq iven}">
                                                    <c:set var="found" value="true" scope="page"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${found eq true}">
                                                    <li class="list-group-item"><input type="checkbox" iven="${iven}"
                                                                                       name="${iven}" value="${iven}"
                                                                                       onclick="filter(this,'geo');"/> ${iven}
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="list-group-item"><input type="checkbox" civen="${iven}"
                                                                                       name="${iven}" value="${iven}"
                                                                                       onclick="filter(this,'geo');"
                                                                                       checked/> ${iven}</li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="iven" items="${geozone}">
                                            <li class="list-group-item"><input type="checkbox" name="${iven}" value="${iven}"
                                                                               onclick="filter(this,'geo');" checked/> ${iven}
                                            </li>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </ul>
                        <h4 class="text-center" style="cursor: pointer;" onclick="changePriceOrder()">Prezzo <i id="priceOrderIcon" class="fa fa-arrow-up rotate" style="font-size:10px; margin-left:2px" aria-hidden="true"></i></h4>
                        <div class="row">
                            <div class="col-md-4 col-xs-3">
                                <h6 style="margin-left:25%;">Minimo</h6>
                                <h6 style="margin-left:25%;">Massimo</h6>
                            </div>
                            <div class="col-md-8 col-xs-9" style="text-align:right;">
                                <c:choose>
                                    <c:when test="${not empty param.minPrice}">
                                        <input id="PriceMin" class="form-control no-border input_prezzo" type="text"
                                               onkeyup="showApply();" value="${param.minPrice}" maxlength="9">
                                    </c:when>
                                    <c:otherwise>
                                        <input id="PriceMin" class="form-control no-border input_prezzo" type="text"
                                               onkeyup="showApply();" value="" maxlength="9">
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${not empty param.maxPrice}">
                                        <input id="PriceMax" class="form-control no-border input_prezzo" type="text"
                                               onkeyup="showApply();" value="${param.maxPrice}" maxlength="9">
                                    </c:when>
                                    <c:otherwise>
                                        <input id="PriceMax" class="form-control no-border input_prezzo" type="text"
                                               onkeyup="showApply();" value="" maxlength="9">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="col-xs-9 col-xs-offset-3 text-center">
                                <button id="PriceButton" class="btn btn-success" style="display: none" onclick="filterPrice();"><i class="fa fa-check" aria-hidden="true"></i></button>
                            </div>
                        </div>
                        <div class="row" style="padding-bottom:15px">
                            <h4 class="text-center">Valutazione</h4>
                            <div class="col-md-12 text-center">
                                <c:choose>
                                    <c:when test="${not empty param.minRat}">
                                        <c:if test="${param.minRat > 0}">
                                            <c:forEach begin="0" end="${param.minRat - 1}" varStatus="loop">
                                                <i class="fa fa-star rating_star" aria-hidden="true" id="stella_${loop.index}" onmouseover="setStar(this)"
                                                   onclick="setStarFilter()" style="cursor:pointer"></i>&nbsp;
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${param.minRat < 5}">
                                            <c:forEach begin="0" end="${4 - param.minRat}" varStatus="loop">
                                                <i class="fa fa-star-o rating_star" aria-hidden="true" id="stella_${param.minRat + loop.index}" onmouseover="setStar(this)"
                                                   onclick="setStarFilter()" style="cursor:pointer"></i>&nbsp;
                                            </c:forEach>
                                        </c:if>
                                        &nbsp;o più
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-star rating_star" aria-hidden="true" id="stella_0" onmouseover="setStar(this)"
                                           onclick="setStarFilter()" style="cursor:pointer"></i>&nbsp;
                                        <i class="fa fa-star-o rating_star" aria-hidden="true" id="stella_1" onmouseover="setStar(this)"
                                           onclick="setStarFilter()" style="cursor:pointer"></i>&nbsp;
                                        <i class="fa fa-star-o rating_star" aria-hidden="true" id="stella_2" onmouseover="setStar(this)"
                                           onclick="setStarFilter()" style="cursor:pointer"></i>&nbsp;
                                        <i class="fa fa-star-o rating_star" aria-hidden="true" id="stella_3" onmouseover="setStar(this)"
                                           onclick="setStarFilter()" style="cursor:pointer"></i>&nbsp;
                                        <i class="fa fa-star-o rating_star" aria-hidden="true" id="stella_4" onmouseover="setStar(this)"
                                           onclick="setStarFilter()" style="cursor:pointer"></i>&nbsp;
                                        &nbsp;o più
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-10 col-md-9">
                    <c:choose>
                        <c:when test="${not empty products}">
                            <ul class="list-group search">
                                <c:forEach var="prod" items="${products}" varStatus="status">
                                    <li class="list-group-item product">
                                        <div class="row">
                                            <div class="col-lg-2 col-md-2 col-xs-12">
                                                <a href="${pageContext.request.contextPath}/product.jsp?product=${prod.value.getList().get(0).getProductID()}&shop=${prod.value.getList().get(0).getShopID()}">
                                                    <img class="img-rounded img-responsive" src="${prod.value.getImageData()}" alt="product image">
                                                </a>
                                            </div>
                                            <div class="col-lg-7 col-md-5 col-xs-12">
                                                <h2 class="list-group-item-heading"><a class="resetcolor" href="${pageContext.request.contextPath}/product.jsp?product=${prod.value.getList().get(0).getProductID()}&shop=${prod.value.getList().get(0).getShopID()}">${prod.value.getList().get(0).getProductName()}</a></h2>
                                                <ul class="list-unstyled list-group-item-text">
                                                    <li>Venduto da: <a href="${pageContext.request.contextPath}/shop.jsp?id=${prod.value.getList().get(0).getShopID()}"><b>${prod.value.getList().get(0).getShopName()}</b></a>
                                                        <c:if test="${prod.value.getList().size() > 1}"><small>e da altri <a href="javascript:void(0);" onclick="openModal('${prod.value.getList().get(0).getProductName()}');">${(prod.value.getList().size())-1}</a> venditori</small></c:if>
                                                    </li>
                                                    <li class="price">
                                                            ${Utils.getNDecPrice(prod.value.getList().get(0).getActualPrice(),2)}&euro;
                                                        <c:if test="${prod.value.getList().get(0).getActualPrice() != prod.value.getList().get(0).getPrice()}">
                                                            <span class="badge badge-discount">In offerta!</span>
                                                        </c:if>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="col-lg-3 col-md-5 col-xs-12 text-center">
                                                <div class="row rating-field">
                                                    <c:set var="rating" value="${prod.value.getList().get(0).getRating()}" scope="page"/>
                                                    <c:choose>
                                                        <c:when test="${Math.round(rating) == -1}">
                                                            Nessuna recensione
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:if test="${Math.round(rating) > 0}">
                                                                <c:forEach begin="0" end="${Math.round(rating) - 1}" varStatus="loop">
                                                                    <i class="fa fa-star" aria-hidden="true"></i>
                                                                </c:forEach>
                                                            </c:if>
                                                            <c:if test="${Math.round(rating) < 5}">
                                                                <c:forEach begin="0" end="${4 - Math.round(rating)}" varStatus="loop">
                                                                    <i class="fa fa-star-o" aria-hidden="true"></i>
                                                                </c:forEach>
                                                            </c:if>
                                                            <fmt:formatNumber var="rc" groupingUsed = "false" maxFractionDigits = "0" value="${prod.value.getReviewCount()}"/>
                                                            <!-- da testare i due primi when -->
                                                            <c:choose>
                                                                <c:when test="${rc == 0}">
                                                                </c:when>
                                                                <c:when test="${rc == 1}">
                                                                    &nbsp&nbsp<span class="text-right">1 recensione</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    &nbsp&nbsp<span class="text-right">${rc} recensioni</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="row">
                                                    <button class="btn btn-default" onclick="addToCart('${prod.value.getList().get(0).getProductID()}','${prod.value.getList().get(0).getShopID()}');">
                                                        Aggiungi al carrello&nbsp&nbsp<i class="fa fa-angle-double-right" aria-hidden="true"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <div class="container">
                                <div class="jumbotron">
                                    <h2>Nessun risultato trovato per "${param.get("q")}"</h2>
                                    <p>Prova ad alleggerire la ricerca rimuovendo qualche parola o qualche filtro</p>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

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