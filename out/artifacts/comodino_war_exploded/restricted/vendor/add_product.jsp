<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="utils.Utils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<jsp:useBean id="shop" class="main.Shop" scope="request"/>
<jsp:useBean id="shopproducts" class="java.util.HashMap" scope="request"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Aggiungi prodotto - ${shop.name}
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/add_product.css" >
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/add_product.js"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <c:if test="${!products.isEmpty()}">
                <div class="row">
                    <div class="col-md-12">
                    <h1>Trovati prodotti simili in altri negozi:</h1>
                    <ul class="list-group">
                        <c:forEach items="${products}" var="product">
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-md-2 col-sm-12 col-xs-12">
                                        <img class="img-rounded img-responsive" src="${product.imgBase64[0]}" alt="Product Image">
                                    </div>
                                    <div class="col-md-7 col-sm-8 col-xs-12">
                                        <h3 class="list-group-item-heading">${product.productName}</h3>
                                    </div>
                                    <div class="col-md-3 col-sm-4 col-xs-12 btncolumn">
                                        <div class="buttons">
                                            <div class="row">
                                                <button type="button" class="btn btn-default btn-block margin-btn" onclick="addNewProductModal(${product.productID})"><i class="fa fa-plus fa-fw fa-lg pull-left"></i>Aggiungi questo prodotto</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                </div>
                <div class="row">
                    <div class="col-md-2 col-md-offset-5 text-center" id="oppure">
                        <h5>-- oppure --</h5>
                    </div>
                </div>
            </c:if>
            <div class="row">
                <div class="col-md-4 col-md-offset-4 text-center">
                    <button class="btn btn-success addnew" data-toggle="modal" data-target="#newproductmodal"><i class="fa fa-plus"></i> Aggiungi nuovo prodotto</button>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addproductmodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="addproductcard" class="card card-signup centerize" data-background-color="orange">
                    <form id="addproductform" class="form" method="POST" action="${pageContext.request.contextPath}/restricted/vendor/insertintoshopproduct">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up" >Aggiungi nuovo prodotto</h4>
                        </div>
                        <div class="content">

                            <input id="addProductId" type="text" name="productID" class="hidden" value="">

                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-eur green" aria-hidden="true"></i>
                          </span>
                                <input name="Price" type="text" class="form-control"  placeholder="Prezzo..." maxlength="9">
                            </div>
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-percent green" aria-hidden="true"></i>
                          </span>
                                <input name="Discount" type="text" class="form-control"  placeholder="Sconto..." maxlength="3">
                            </div>

                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-hashtag green" aria-hidden="true"></i>
                          </span>
                                <input name="Quantity" type="text" class="form-control"  placeholder="Quantità..." maxlength="9">
                            </div>
                            <div class="footer text-center" style="margin-top: 15px;">
                                <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;" onclick="$('#addproductform').submit();">Invia</a>
                                <a class="btn btn-default" style="margin-left: 20px; padding-left: 25px; padding-right: 25px;" onclick="$(function(){$('#addproductmodal').modal('toggle');});">Chiudi</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="newproductmodal" tabindex="-1" role="dialog">
            <div class="row">
                <div id="newproductcard" class="card card-signup centerize" data-background-color="orange">
                    <form id="newproductform" enctype="multipart/form-data" class="form" method="POST" action="${pageContext.request.contextPath}/restricted/vendor/insertnewproduct">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up" >Aggiungi nuovo prodotto</h4>
                        </div>
                        <div class="content">
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-shopping-cart green" aria-hidden="true"></i>
                          </span>
                                <input name="Name" type="text" class="form-control"  placeholder="Nome..." maxlength="50">
                            </div>
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-pencil green" aria-hidden="true"></i>
                          </span>
                                <input name="Description" type="text" class="form-control"  placeholder="Descrizione..." maxlength="50">
                            </div>
                            <div class="form-group">
                                <select class="form-control minimal" name="Category" id="Category">
                                        <option value=""> -- Seleziona Categoria -- </option>
                                        <option value="Bagno">Bagno</option>
                                        <option value="Balcone e Giardino">Balcone e Giardino</option>
                                        <option value="Camera da Letto">Camera da Letto</option>
                                        <option value="Cucina">Cucina</option>
                                        <option value="Illuminazione">Illuminazione</option>
                                        <option value="Soggiorno">Soggiorno</option>
                                        <option value="Studio e Ufficio">Studio e Ufficio</option>
                                </select>
                            </div>
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-eur green" aria-hidden="true"></i>
                          </span>
                                <input name="Price" type="text" class="form-control"  placeholder="Prezzo..." maxlength="9">
                            </div>
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-percent green" aria-hidden="true"></i>
                          </span>
                                <input name="Discount" type="text" class="form-control"  placeholder="Sconto..." maxlength="3">
                            </div>

                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-hashtag green" aria-hidden="true"></i>
                          </span>
                                <input name="Quantity" type="text" class="form-control"  placeholder="Quantità..." maxlength="9">
                            </div>
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-camera green" aria-hidden="true"></i>
                          </span>
                                <input required type="file" name="productPhoto" accept="image/*" placeholder="Immagine...">
                            </div>

                            <div class="footer text-center" style="margin-top: 15px;">
                                <a class="btn btn-default" style="padding-left: 29px; padding-right: 29px;" onclick="$('#newproductform').submit();">Invia</a>
                                <a class="btn btn-default" style="margin-left: 20px; padding-left: 25px; padding-right: 25px;" onclick="$(function(){$('#newproductmodal').modal('toggle');});">Chiudi</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpage>

