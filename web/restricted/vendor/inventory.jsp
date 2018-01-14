<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="utils.Utils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<jsp:useBean id="shop" class="main.Shop" scope="request"/>
<jsp:useBean id="shopproducts" class="java.util.HashMap" scope="request"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Inventario - ${shop.name}
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory.css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/inventory.js"></script>
    </jsp:attribute>

    <jsp:body>
        <div id="inventory" class="container">
            <div class="row">
                <div class="col-xs-6">
                    <h1>Inventario:</h1>
                </div>
                <div class="col-xs-6">
                    <button class="btn btn-success pull-right" data-toggle="modal" data-target="#addproductmodal"
                            style="margin-top: 20px"><i class="fa fa-plus"></i> Aggiungi nuovo prodotto
                    </button>
                </div>
            </div>
            <ul class="list-group">
                <c:choose>
                    <c:when test="${not empty shop.products}">
                        <c:forEach items="${shop.products}" var="product">

                            <!-- inizio prodotto -->
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-lg-2 col-md-2">
                                        <img class="img-rounded img-responsive" src="${product.imgBase64[0]}"
                                             alt="Product Image">
                                    </div>
                                    <div class="col-lg-5 col-md-3 col-xs-12">
                                        <h3 class="list-group-item-heading"><a class="resetcolor"
                                                                               href="${pageContext.request.contextPath}/product.jsp?product=${product.productID}&shop=${shop.shopID}">${product.productName}</a>
                                        </h3>
                                        <ul class="list-unstyled list-group-item-text">
                                            <c:choose>
                                                <c:when test="${product.price == product.actualPrice}">
                                                    <li><h4>Prezzo: ${Utils.getNDecPrice(product.price,2)}&euro;</h4>
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li>
                                                        <h4>Prezzo:
                                                            <del>${Utils.getNDecPrice(product.price, 2)}&euro;</del>
                                                                ${Utils.getNDecPrice(product.actualPrice,2)}&euro;
                                                        </h4>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                            <li>Disponibilità: <b>${product.quantity} pezzi &nbsp;</b>
                                                <c:if test="${product.quantity < 20}">
                                                    <span class="badge badge-discount">In esaurimento!</span>
                                                </c:if>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="col-lg-2 col-md-3 col-xs-12 text-center">
                                        <div class="buttons">
                                            <div class="row">
                                                <button type="button" class="btn btn-default btn-block margin-btn"
                                                        onclick="editPriceModal(${product.productID})"><i
                                                        class="fa fa-eur fa-fw fa-lg pull-left"></i>Modifica Prezzo
                                                </button>
                                            </div>
                                            <div class="row">
                                                <button type="button" class="btn btn-default btn-block margin-btn"
                                                        onclick="editQuantityModal(${product.productID})"><i
                                                        class="fa fa-hashtag fa-fw fa-lg pull-left"></i>Modifica Disponibilità
                                                </button>
                                            </div>
                                            <div class="row">
                                                <button type="button" class="btn btn-default btn-block margin-btn"
                                                onclick="addProductPhotoModal(${product.productID})"><i
                                                        class="fa fa-camera fa-fw fa-lg pull-left"></i>Aggiungi foto prodotto
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-2 col-md-3 col-xs-12 col-md-offset-1 col-lg-offset-1 text-center">
                                        <div class="buttons">
                                            <div class="row">
                                                <button type="button" class="btn btn-primary btn-block margin-btn"
                                                        onclick="location.href='${pageContext.request.contextPath}/product.jsp?product=${product.productID}&shop=${shop.shopID}#reviewanchor'">
                                                    <i class="fa fa-comments-o fa-fw fa-lg pull-left"></i>Recensioni
                                                </button>
                                            </div>
                                            <div class="row">
                                                <button type="button" class="btn btn-danger btn-block margin-btn"
                                                        onclick="removeProductModal(${product.productID})"><i
                                                        class="fa fa-trash fa-fw fa-lg pull-left"></i>Rimuovi
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <!-- fine prodotto -->
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <h3>&nbsp;&nbsp;&nbsp;Non ci sono prodotti nel tuo inventario</h3>
                    </c:otherwise>
                </c:choose>

            </ul>
        </div>

        <div class="modal fade" id="editquantitymodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="editquantitycard" class="card card-signup centerize" data-background-color="orange">
                    <form id="editquantityform" class="form" method="POST"
                          action="${pageContext.request.contextPath}/restricted/vendor/editproductquantity">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Modifica Quantità</h4>
                        </div>
                        <div class="content">

                            <input id="productIdQuantityModal" type="text" name="productID" class="hidden" value="">

                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-user green" aria-hidden="true"></i>
                          </span>
                                <input name="Quantity" type="text" class="form-control" placeholder="Quantità..." maxlength="9">
                            </div>
                            <div class="footer text-center" style="margin-top: 15px;">
                                <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;"
                                   onclick="$('#editquantityform').submit();">Invia</a>
                                <a class="btn btn-default"
                                   style="margin-left: 20px; padding-left: 25px; padding-right: 25px;"
                                   onclick="$(function(){$('#editquantitymodal').modal('toggle');});">Chiudi</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addproductphotomodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="addproductphotocard" class="card card-signup centerize" data-background-color="orange">
                    <form id="addproductphotoform" enctype = "multipart/form-data" class="form" method="POST"
                          action="/restricted/vendor/uploadproductphoto">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Aggiungi foto</h4>
                        </div>
                        <div class="content">

                            <input id="productIDPhotoModal" type="text" name="productID" class="hidden" value="">

                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-camera green" aria-hidden="true"></i>
                          </span>
                                <input required id="productPhoto" type="file" name="productPhoto" accept="image/*">
                            </div>
                            <div class="footer text-center" style="margin-top: 15px;">
                                <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;"
                                   onclick="$('#addproductphotoform').submit();">Invia</a>
                                <a class="btn btn-default"
                                   style="margin-left: 20px; padding-left: 25px; padding-right: 25px;"
                                   onclick="$(function(){$('#addproductphotomodal').modal('toggle');});">Chiudi</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="editpricemodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="editpricecard" class="card card-signup centerize" data-background-color="orange">
                    <form id="editpriceform" class="form" method="POST"
                          action="${pageContext.request.contextPath}/restricted/vendor/editproductprice">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Modifica Prezzo</h4>
                        </div>
                        <div class="content">

                            <input id="productIdPriceModal" type="text" name="productID" class="hidden" value="">

                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-eur green" aria-hidden="true"></i>
                          </span>
                                <input name="Price" type="text" class="form-control" placeholder="Prezzo..." maxlength="9">
                            </div>

                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-percent green" aria-hidden="true"></i>
                          </span>
                                <input name="Discount" type="text" class="form-control"
                                       placeholder="Sconto (in decimali)..." maxlength="3">
                            </div>
                            <div class="footer text-center" style="margin-top: 15px;">
                                <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;"
                                   onclick="$('#editpriceform').submit();">Invia</a>
                                <a class="btn btn-default"
                                   style="margin-left: 20px; padding-left: 25px; padding-right: 25px;"
                                   onclick="$(function(){$('#editpricemodal').modal('toggle');});">Chiudi</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="removeproductmodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="removeproductcard" class="card card-signup centerize" data-background-color="orange">
                    <form id="removeproductform" class="form" method="POST"
                          action="${pageContext.request.contextPath}/restricted/vendor/removeproduct">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Rimuovere prodotto?</h4>
                        </div>
                        <div class="content">

                            <input id="productIDRemoveModal" type="text" name="productID" class="hidden" value="">

                            <div class="footer text-center" style="margin-top: 15px;">
                                <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;"
                                   onclick="$('#removeproductform').submit();">Rimuovi</a>
                                <a class="btn btn-default"
                                   style="margin-left: 20px; padding-left: 25px; padding-right: 25px;"
                                   onclick="$(function(){$('#removeproductmodal').modal('toggle');});">Annulla</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addproductmodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="addproductcard" class="card card-signup centerize" data-background-color="orange">
                    <form id="addproductform" class="form" method="POST"
                          action="${pageContext.request.contextPath}/restricted/vendor/addproduct">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Aggiungi Nuovo Prodotto</h4>
                        </div>
                        <div class="content">
                            <p style="color: #ffffff; padding-bottom: 20px; text-align: justify">Se vuoi aggiungere un prodotto già venduto da altri venditori puoi cercarlo utilizzando l'apposita barra, altrimenti clicca su "Aggiungi".</p>
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-search green" aria-hidden="true"></i>
                          </span>
                                <input id="addproduct" name="productName" type="text" class="form-control"
                                       placeholder="Nome prodotto..." maxlength="50">
                            </div>
                            <div class="footer text-center" style="margin-top: 15px;">
                                <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;"
                                   onclick="$('#addproductform').submit();">Aggiungi</a>
                                <a class="btn btn-default"
                                   style="margin-left: 20px; padding-left: 25px; padding-right: 25px;"
                                   onclick="$(function(){$('#addproductmodal').modal('toggle');});">Chiudi</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>


    </jsp:body>
</t:genericpage>

