<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="user" class="main.User" scope="session"/>

<jsp:useBean id="shopDao" class="daos.impl.ShopDaoImpl"/>
<c:set var="shop" value="${shopDao.getShop(user.shopID)}" scope="page"/>

<jsp:useBean id="disputeDao" class="daos.impl.DisputeDaoImpl"/>
<c:set var="allDisputeList" value="${disputeDao.getDisputeByShop(shop.shopID)}" scope="page"/>

<jsp:useBean id="productDao" class="daos.impl.ProductDaoImpl"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Dispute - ${shop.name}
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dispute_list.css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 id="title" class="text-uppercase">Dispute</h1>
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#DisputeInCorso">Dispute in Corso</a></li>
                        <li><a data-toggle="tab" href="#DisputeTerminate">Dispute Terminate</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="DisputeInCorso" class="tab-pane fade in active">
                            <!-- inizio disputa -->
                            <c:choose>
                                <c:when test="${not empty allDisputeList}">
                                    <c:forEach items="${allDisputeList}" var="dispute">
                                        <c:set var="product"
                                               value="${productDao.getProduct(dispute.productID, dispute.shopID)}"
                                               scope="page"/>
                                        <div class="dispute">
                                            <div class="row dispBody">
                                                <div class="row dispItem">
                                                    <div class="col-xs-12 col-md-2">
                                                        <figure class="pull-left">
                                                            <img class="media-object img-rounded img-responsive image-centre"
                                                                 src="${product.imgBase64[0]}" alt="product image"
                                                                 height="" width="200px"></figure>
                                                    </div>
                                                    <div class="col-xs-12 col-md-7">
                                                        <h3>${dispute.title}</h3>
                                                        <p>${dispute.description}</p>
                                                    </div>
                                                    <div class="col-lg-2 col-md-3 col-xs-12">
                                                        <h4>
                                                            <b>Stato:</b>&nbsp;&nbsp;
                                                            <c:choose>
                                                                <c:when test="${dispute.status == 1}">
                                                                    <span class="badge badge-danger">Approvata</span>
                                                                </c:when>
                                                                <c:when test="${dispute.status == 2}">
                                                                    <span class="badge badge-success">Declinata</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge badge-warning">In attesa</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </h4>
                                                        <p>
                                                            <c:set var="dateParts"
                                                                   value="${fn:split(dispute.creationDate, ' ')}"
                                                                   scope="page"/>
                                                            <c:set var="date" value="${fn:split(dateParts[0], '-')}"
                                                                   scope="page"/>
                                                            <c:set var="time" value="${fn:split(dateParts[1], ':')}"
                                                                   scope="page"/>

                                                            <b>Effettuata alle: </b>${date[2]}/${date[1]}
                                                            &nbsp;h: ${time[0]}:${time[1]}
                                                        </p>
                                                        <p><b>Prodotto: </b><a style="color:#2c3e50"
                                                                               href="${pageContext.request.contextPath}/product.jsp?product=${dispute.productID}&shop=${dispute.shopID}">${product.productName}</a>
                                                        </p>
                                                        <p><b>Ordine numero: </b>${dispute.orderID}</p>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>

                                    <!-- fine disputa -->

                                </c:when>
                                <c:otherwise>
                                    <h3>&nbsp;&nbsp;&nbsp;Non ci sono dispute</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div id="DisputeTerminate" class="tab-pane fade">
                            <!-- inizio disputa -->
                            <c:choose>
                                <c:when test="${(not empty allDisputeList)}">
                                    <c:set var="allCompleted" value="0"/>
                                    <c:forEach items="${allDisputeList}" var="dispute">
                                        <c:if test="${dispute.status != 0}">
                                            <c:set var="allCompleted" value="1"/>
                                            <c:set var="product"
                                                   value="${productDao.getProduct(dispute.productID, dispute.shopID)}"
                                                   scope="page"/>
                                            <div class="dispute">
                                                <div class="row dispBody">
                                                    <div class="row dispItem">
                                                        <div class="col-xs-12 col-md-2">
                                                            <figure class="pull-left">
                                                                <img class="media-object img-rounded img-responsive image-centre"
                                                                     src="${product.imgBase64[0]}" alt="product image"
                                                                     height="" width="200px"></figure>
                                                        </div>
                                                        <div class="col-xs-12 col-md-7">
                                                            <h3>${dispute.title}</h3>
                                                            <p>${dispute.description}</p>
                                                        </div>
                                                        <div class="col-lg-2 col-md-3 col-xs-12">
                                                            <h4>
                                                                <b>Stato:</b>&nbsp;&nbsp;
                                                                <c:choose>
                                                                    <c:when test="${dispute.status == 1}">
                                                                        <span class="badge badge-success">Approvata</span>
                                                                    </c:when>
                                                                    <c:when test="${dispute.status == 2}">
                                                                        <span class="badge badge-danger">Declinata</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge badge-warning">In attesa</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </h4>
                                                            <p>
                                                                <c:set var="dateParts"
                                                                       value="${fn:split(dispute.creationDate, ' ')}"
                                                                       scope="page"/>
                                                                <c:set var="date" value="${fn:split(dateParts[0], '-')}"
                                                                       scope="page"/>
                                                                <c:set var="time" value="${fn:split(dateParts[1], ':')}"
                                                                       scope="page"/>

                                                                <b>Effettuata alle: </b>${date[2]}/${date[1]}
                                                                &nbsp;h: ${time[0]}:${time[1]}
                                                            </p>
                                                            <p><b>Prodotto: </b><a style="color:#2c3e50"
                                                                                   href="${pageContext.request.contextPath}/product.jsp?product=${dispute.productID}&shop=${dispute.shopID}">${product.productName}</a>
                                                            </p>
                                                            <p><b>Ordine numero: </b>${dispute.orderID}</p>

                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${allCompleted == 0}">
                                        <h3>&nbsp;&nbsp;&nbsp;Non ci sono dispute completate</h3>
                                    </c:if>
                                    <!-- fine disputa -->

                                </c:when>
                                <c:otherwise>
                                    <h3>&nbsp;&nbsp;&nbsp;Non ci sono dispute completate</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </jsp:body>
</t:genericpage>