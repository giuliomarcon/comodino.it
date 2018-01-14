<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utils.Utils" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<c:set var="cart" value="${user.getCart(false)}" scope="page"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Pagamento
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment.css" >
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/cleave.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validate/jquery.validate.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validate/additional-methods.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/payment.js"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <h2>Pagamento</h2>

            <form id="PaymentForm" class="form-horizontal" action="${pageContext.request.contextPath}/restricted/completeorder" method="post">
                <div class="col-sm-8">
                    <div class=" col-sm-offset-3 btn-group" style="margin-bottom: 10px;">
                        <a class="btn btn-sm btn-primary mycard mastercard">Mastercard</a>
                        <a class="btn btn-sm btn-primary mycard amex">American Express</a>
                        <a class="btn btn-sm btn-primary mycard visa">VISA</a>
                        <a class="btn btn-sm btn-primary mycard diners">Diners</a>
                        <a class="btn btn-sm btn-primary mycard jcb">Jcb</a>
                        <a class="btn btn-sm btn-primary mycard discover">Discover</a>
                    </div>
                    <fieldset>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="card-holder-name">Intestatario Carta</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="card-holder-name" id="card-holder-name" placeholder="Nome e Cognome" required maxlength="50">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="card-number">Numero Carta</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control input-credit-card" name="card-number" id="card-number" placeholder="Numero carta" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="expiry-month">Data di Scadenza</label>
                            <div class="col-sm-9">
                                <div class="row">
                                    <div class="col-xs-6">
                                        <select class="form-control col-sm-2 minimal" name="expiry-month" id="expiry-month">
                                            <option value="01">01</option>
                                            <option value="02">02</option>
                                            <option value="03">03</option>
                                            <option value="04">04</option>
                                            <option value="05">05</option>
                                            <option value="06">06</option>
                                            <option value="07">07</option>
                                            <option value="08">08</option>
                                            <option value="09">09</option>
                                            <option value="10">10</option>
                                            <option value="11">11</option>
                                            <option value="12">12</option>
                                        </select>
                                    </div>
                                    <div class="col-xs-6">
                                        <select class="form-control minimal" name="expiry-year">
                                            <option value="2018">2018</option>
                                            <option value="2019">2019</option>
                                            <option value="2020">2020</option>
                                            <option value="2021">2021</option>
                                            <option value="2022">2022</option>
                                            <option value="2023">2023</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="cvv">Codice di sicurezza</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" name="cvv" id="cvv" placeholder="CCV" required>
                            </div>
                        </div>

                    </fieldset>
                </div>
                <div class="col-sm-4 text-center">
                    <h4>Totale: ${Utils.getNDecPrice(cart.getTotal(),2)} &euro;</h4>
                </div>
                <div class="form-group">
                    <div class="col-sm-12 text-center">
                        <a class="btn btn-default" href="checkout.jsp" style="margin-top: 20px">Indietro</a>
                        <button type="submit" class="btn btn-success" style="margin-top: 20px">Paga ora</button>
                    </div>
                </div>
            </form>



        </div>
    </jsp:body>
</t:genericpage>