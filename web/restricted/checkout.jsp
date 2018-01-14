<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<c:set var="cart" value="${user.getCart(true)}" scope="page"/>
<jsp:useBean id="AddressDao" class="daos.impl.AddressDaoImpl"/>
<c:set var="addressList" value="${AddressDao.getAllAddresses(user)}" scope="page"/>
<jsp:useBean id="shopDao" class="daos.impl.ShopDaoImpl"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Checkout
    </jsp:attribute>

    <jsp:attribute name="pagecss">
        <link href="${pageContext.request.contextPath}/css/checkout.css" rel="stylesheet" type="text/css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/index.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container">
            <form action="${pageContext.request.contextPath}/restricted/setorderaddresses" method="post">
                <div class="row">
                    <div class="col-md-12">
                        <h2 class="display-1">Modalità di consegna</h2>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <ul class="list-group">
                            <c:choose>
                                <c:when test="${not empty cart}">
                                    <c:forEach items="${cart}" var="cartItem">
                                        <li class="list-group-item">
                                            <div class="row pi-draggable" id="c_row-4col" draggable="true">
                                                <div class="col-md-9">
                                                    <h3 class="itemtitle">${cartItem.getProduct().productName} <small>x${cartItem.getQuantity()}</small></h3>
                                                    <p class="itemseller">Venduto da:
                                                        <a href="${pageContext.request.contextPath}/shop.jsp?id=${cartItem.getProduct().shopID}">${cartItem.getProduct().shopName}</a>
                                                    </p>
                                                </div>
                                                <c:if test="${shopDao.getShop(cartItem.getProduct().shopID).getClass().simpleName == 'PhysicalShop'}">
                                                    <div class="col-md-3 text-right">
                                                            <%-- Ogni entry se checkata restituisce productID_shopID nel campo 'ritiro' del form--%>
                                                        <h4 style="margin-top: 15px">Ritiro in negozio <input type="checkbox" name="ritironegozio" value="${cartItem.getProduct().productID}_${cartItem.getProduct().shopID}"></h4>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </ul>
                    </div>
                </div>
                <div class="row ">
                    <div class="col-md-12 ">
                        <div class="row ">
                            <div class="col-md-9">
                                <h2 style="margin-top: 0">I tuoi indirizzi di consegna</h2>
                            </div>
                            <div class="col-md-3 text-right">
                                <a href="<c:url value="/restricted/addresses.jsp?from=/restricted/checkout.jsp"/>" class="btn btn-primary"><i
                                        class="fa fa-fw fa-plus"></i> Aggiungi Nuovo Indirizzo</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:choose>
                            <c:when test="${not empty addressList}">
                                <c:forEach items="${addressList}" var="address" varStatus="status">
                                    <div class="radio">&nbsp;
                                        <label>
                                            <input ${status.first ? 'checked' : ''}
                                                    type="radio"
                                                    name="address" value="${address.addressID}">${address.firstName} ${address.lastName}, ${address.address}, ${address.zip} ${address.city} ${address.state}
                                        </label>
                                    </div>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 text-center">
                        <a href="<c:url value="/restricted/cart.jsp"/>" class="btn btn-default">Indietro</a>
                        <button type="submit" class="btn btn-primary">Prosegui <i class="fa fa-angle-double-right" aria-hidden="true"></i></button>
                    </div>
                </div>
            </form>
        </div>
    </jsp:body>
</t:genericpage>