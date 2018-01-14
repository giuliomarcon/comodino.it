<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="user" class="main.User" scope="session"/>

<jsp:useBean id="shopDao" class="daos.impl.ShopDaoImpl"/>
<c:set var="shop" value="${shopDao.getShop(user.shopID)}" scope="page"/>

<jsp:useBean id="notificationdao" class="daos.impl.NotificationDaoImpl"/>
<c:set var="prodreview_notifications" value="${notificationdao.getProductReviewNotifications(user.shopID)}"
       scope="page"/>
<c:set var="shopreview_notifications" value="${notificationdao.getShopReviewNotifications(user.shopID)}" scope="page"/>
<c:set var="shopdispute_notifications" value="${notificationdao.getDisputeNotifications(user.shopID)}" scope="page"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Notifiche - ${shop.name}
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor_notifications.css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 id="title" class="text-uppercase">Notifiche:</h1>
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#ProductReview">Recensioni Prodotti</a></li>
                        <li><a data-toggle="tab" href="#ShopReview">Recensioni Negozio</a></li>
                        <li><a data-toggle="tab" href="#Dispute">Dispute</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="ProductReview" class="tab-pane fade in active">
                            <c:choose>
                                <c:when test="${not empty prodreview_notifications}">
                                    <c:forEach items="${prodreview_notifications}" var="prodrev">
                                        <%-- inizio notifica--%>
                                        <div class="notification">
                                            <div class="row">
                                                <div class="col-md-9 col-xs-12">
                                                    <a class="resetcolor" href="orderreceived.jsp"><h4>${prodrev.title}</h4></a>
                                                    <p>
                                                        <c:set var="dateParts"
                                                               value="${fn:split(prodrev.creationDate, ' ')}"
                                                               scope="page"/>
                                                        <c:set var="date" value="${fn:split(dateParts[0], '-')}"
                                                               scope="page"/>
                                                        <c:set var="time" value="${fn:split(dateParts[1], ':')}"
                                                               scope="page"/>

                                                        <b>Effettuata alle: </b>${date[2]}/${date[1]}&nbsp;h: ${time[0]}:${time[1]}
                                                    </p>
                                                </div>
                                                <div class="col-md-3 col-xs-12">
                                                    <p class="pull-right" style="margin-top: 20px"><b>Rating:</b>&nbsp;&nbsp;
                                                        <c:if test="${prodrev.rating > 0}">
                                                            <c:forEach begin="0" end="${prodrev.rating - 1}" varStatus="loop">
                                                                <i class="fa fa-star" aria-hidden="true"></i>
                                                            </c:forEach>
                                                        </c:if>
                                                        <c:if test="${prodrev.rating < 5}">
                                                            <c:forEach begin="0" end="${4 - prodrev.rating}" varStatus="loop">
                                                                <i class="fa fa-star-o" aria-hidden="true"></i>
                                                            </c:forEach>
                                                        </c:if>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                        <%-- fine notifica--%>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <h4>&nbsp;&nbsp;&nbsp;Non ci sono notifiche</h4>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div id="ShopReview" class="tab-pane fade">
                            <c:choose>
                                <c:when test="${not empty shopreview_notifications}">
                                    <c:forEach items="${shopreview_notifications}" var="shoprev">
                                        <%-- inizio notifica--%>
                                        <div class="notification">
                                            <div class="row">
                                                <div class="col-md-9 col-xs-12">
                                                    <a class="resetcolor" href="${pageContext.request.contextPath}/shop.jsp?id=${shop.shopID}"><h4>${shoprev.title}<small>&nbsp;&nbsp;da parte di ${shoprev.userID}</small></h4></a>
                                                    <p>
                                                        <c:set var="dateParts"
                                                               value="${fn:split(shoprev.creationDate, ' ')}"
                                                               scope="page"/>
                                                        <c:set var="date" value="${fn:split(dateParts[0], '-')}"
                                                               scope="page"/>
                                                        <c:set var="time" value="${fn:split(dateParts[1], ':')}"
                                                               scope="page"/>

                                                        <b>Effettuata alle: </b>${date[2]}/${date[1]}&nbsp;h: ${time[0]}:${time[1]}
                                                    </p>
                                                </div>
                                                <div class="col-md-3 col-xs-12">
                                                    <p class="pull-right" style="margin-top: 20px"><b>Rating:</b>&nbsp;&nbsp;
                                                        <c:forEach begin="0" end="${shoprev.rating - 1}" varStatus="loop">
                                                            <i class="fa fa-star" aria-hidden="true"></i>
                                                        </c:forEach>
                                                        <c:forEach begin="0" end="${4 - shoprev.rating}" varStatus="loop">
                                                            <i class="fa fa-star-o" aria-hidden="true"></i>
                                                        </c:forEach>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                        <%-- fine notifica--%>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <h4>&nbsp;&nbsp;&nbsp;Non ci sono notifiche</h4>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div id="Dispute" class="tab-pane fade in">
                            <!-- inizio disputa -->
                            <c:choose>
                                <c:when test="${not empty shopdispute_notifications}">
                                    <c:forEach items="${shopdispute_notifications}" var="shopdispute">
                                        <%-- inizio notifica--%>
                                        <div class="notification">
                                            <div class="row">
                                                <div class="col-md-9 col-xs-12">
                                                    <a class="resetcolor" href="dispute_list.jsp"><h4>${shopdispute.title}<small>&nbsp;&nbsp;per il prodotto n. ${shopdispute.productId} all'ordine n. ${shopdispute.orderId}</small></h4></a>
                                                    <p>
                                                        <c:set var="dateParts"
                                                               value="${fn:split(shopdispute.creationDate, ' ')}"
                                                               scope="page"/>
                                                        <c:set var="date" value="${fn:split(dateParts[0], '-')}"
                                                               scope="page"/>
                                                        <c:set var="time" value="${fn:split(dateParts[1], ':')}"
                                                               scope="page"/>

                                                        <b>Effettuata alle: </b>${date[2]}/${date[1]}&nbsp;h: ${time[0]}:${time[1]}
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                        <%-- fine notifica--%>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <h4>&nbsp;&nbsp;&nbsp;Non ci sono notifiche</h4>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </jsp:body>
</t:genericpage>
