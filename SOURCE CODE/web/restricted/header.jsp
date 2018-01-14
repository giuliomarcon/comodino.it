<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<jsp:useBean id="notificationdao" class="daos.impl.NotificationDaoImpl"/>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">

            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

            <a class="navbar-brand" href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/css/logo.svg"/>
                <c:if test="${!user.hasShop() && user.type == 0}">
                    <span id="headertitle">Comodino.it</span>
                </c:if>
            </a>

            <div id="searchWrapper" class="searchWrapperClass">
                <div class="input-group">
                    <input id="searchMobile" name="q" class="form-control" type="text" placeholder="Cerca" autofocus maxlength="50">
                    <div class="input-group-btn">
                        <button class="btn btn-search btn-default" onclick="doSearchMobile()">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-left">
                <c:if test="${user.hasShop()}">
                    <c:set var="vendor_notifications" value="${notificationdao.getVendorNotifications(user)}" scope="page"/>
                    <li>
                        <a href="#" class="dropdown-toggle mobilecenter" data-toggle="dropdown" onclick="readNotifications()" role="button" aria-haspopup="true" aria-expanded="false">
                            <span class="badge">
                                ${fn:length(vendor_notifications)}&nbsp;&nbsp;<i class="fa fa-truck" aria-hidden="true"></i>
                            </span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="dropdown-header">Notifiche Venditore</li>
                            <c:choose>
                                <c:when test="${fn:length(vendor_notifications) == 0}">
                                    <li><a>Nessuna nuova notifica</a></li>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${vendor_notifications}" var="n">
                                        <li class="header-notification">
                                            <a href="javascript:window.location=createNotificationURL('${n.getClass().simpleName}', '${pageContext.request.contextPath}', '${n.shopId}');">
                                                <c:if test="${n.shopStatus == 0}"><b>NEW </b></c:if>
                                                <c:choose>
                                                    <c:when test="${n.getClass().simpleName == 'NotificationProductReview'}">
                                                        Recensione prodotto:
                                                    </c:when>
                                                    <c:when test="${n.getClass().simpleName == 'NotificationShopReview'}">
                                                        Recensione negozio:
                                                    </c:when>
                                                    <c:when test="${n.getClass().simpleName == 'NotificationDispute'}">
                                                        Disputa:
                                                    </c:when>
                                                    <c:otherwise>
                                                        Qualcosa è andato storto...
                                                    </c:otherwise>
                                                </c:choose>
                                                <%-- Data notifica formattata --%>
                                                <c:set var="dateParts" value="${fn:split(n.creationDate, ' ')}" scope="page"/>
                                                <c:set var="date" value="${fn:split(dateParts[0], '-')}" scope="page"/>
                                                <c:set var="time" value="${fn:split(dateParts[1], ':')}" scope="page"/>
                                                &nbsp;&nbsp;&nbsp;&nbsp;${date[2]}/${date[1]} &nbsp;<span style="font-size: small">h: ${time[0]}:${time[1]}</span>
                                                <br>
                                                <b class="notif-title">${n.title}</b><br>
                                                <c:choose>
                                                    <c:when test="${n.getClass().simpleName == 'NotificationProductReview'}">
                                                        <span>
                                                            <c:if test="${n.rating > 0}">
                                                                <c:forEach begin="0" end="${n.rating - 1}" varStatus="loop">
                                                                    <i class="fa fa-star" aria-hidden="true"></i>
                                                                </c:forEach>
                                                            </c:if>
                                                            <c:if test="${n.rating < 5}">
                                                                <c:forEach begin="0" end="${4 - n.rating}" varStatus="loop">
                                                                    <i class="fa fa-star-o" aria-hidden="true"></i>
                                                                </c:forEach>
                                                            </c:if>
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${n.getClass().simpleName == 'NotificationShopReview'}">
                                                        <span>
                                                            <c:forEach begin="0" end="${n.rating - 1}" varStatus="loop">
                                                                <i class="fa fa-star" aria-hidden="true"></i>
                                                            </c:forEach>
                                                            <c:forEach begin="0" end="${4 - n.rating}" varStatus="loop">
                                                                <i class="fa fa-star-o" aria-hidden="true"></i>
                                                            </c:forEach>
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${n.getClass().simpleName == 'NotificationDispute'}">

                                                    </c:when>
                                                    <c:otherwise>
                                                        Qualcosa è andato storto...
                                                    </c:otherwise>
                                                </c:choose>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            <li role="separator" class="divider"></li>
                            <li class="text-center"><a href="${pageContext.request.contextPath}/restricted/vendor/notifications.jsp">Vedi Tutte</a></li>
                        </ul>
                    </li>
                </c:if>
                <c:if test="${user.type == 1}">
                    <c:set var="admin_notifications" value="${notificationdao.getAdminNotifications()}" scope="page"/>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle mobilecenter" data-toggle="dropdown" onclick="readNotifications()" role="button" aria-haspopup="true" aria-expanded="false">
                            <span class="badge">
                                ${fn:length(admin_notifications)}&nbsp;&nbsp;<i class="fa fa-hand-o-up" aria-hidden="true"></i>
                            </span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="dropdown-header">Notifiche Admin</li>
                            <c:choose>
                                <c:when test="${fn:length(admin_notifications) == 0}">
                                    <li><a>Nessuna nuova notifica</a></li>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${admin_notifications}" var="n" varStatus="i" end="3">
                                        <li class="notificationBlock">
                                            <a href="${pageContext.request.contextPath}/restricted/admin/admin_panel.jsp">
                                                <c:if test="${n.adminStatus == 0}"><b>NEW </b></c:if>Disputa:
                                                <c:set var="dateParts" value="${fn:split(n.creationDate, ' ')}" scope="page"/>
                                                <c:set var="date" value="${fn:split(dateParts[0], '-')}" scope="page"/>
                                                <c:set var="time" value="${fn:split(dateParts[1], ':')}" scope="page"/>
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                ${date[2]}/${date[1]} &nbsp;<span style="font-size: small">h: ${time[0]}:${time[1]}</span><br>
                                                <b class="notif-title">${n.title}</b><br>
                                                Product: ${n.productName}<br>
                                                Shop: ${n.shopName}
                                            </a>
                                        </li>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            <li role="separator" class="divider"></li>
                            <li>
                                <c:if test="${fn:length(admin_notifications) le 4}">
                                    <a href="${pageContext.request.contextPath}/restricted/admin/admin_panel.jsp">Vedi nel dettaglio</a>
                                </c:if>
                                <c:if test="${fn:length(admin_notifications) eq 5}">
                                    <a href="${pageContext.request.contextPath}/restricted/admin/admin_panel.jsp">Vedi tutte (c'è n'è una quinta)</a>
                                </c:if>
                                <c:if test="${fn:length(admin_notifications) gt 5}">
                                    <a href="${pageContext.request.contextPath}/restricted/admin/admin_panel.jsp">Vedi tutte (altre ${fn:length(admin_notifications)-4})</a>
                                </c:if>
                            </li>
                        </ul>
                    </li>
                </c:if>
            </ul>
            <div class="nav navbar-nav navbar-center" id="searchFullWidth">
                <form class="navbar-form navbar-search" role="search" type="GET" action="${pageContext.request.contextPath}/search">
                    <div class="input-group">

                        <div class="input-group-btn">
                            <button type="button" class="btn btn-search btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="label-icon">Categoria</span>
                                &nbsp;&nbsp;<span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu pull-left" role="menu">
                                <li><a href="#">Tutte le categorie</a></li>
                                <c:forEach items="${allcategories}" var="cat">
                                    <li><a  href="#">${cat.categoryName}</a></li>
                                    <input id="${fn:replace(cat.categoryName,' ', '')}-radio" name="cat" value="${cat.categoryName}" type="radio" hidden>
                                </c:forEach>
                            </ul>

                        </div>

                        <input id="search" name="q" class="form-control" type="text" placeholder="Cerca" autofocus maxlength="50">

                        <div class="input-group-btn">
                            <button type="submit" class="btn btn-search btn-default">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle text-center" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        <i class="fa fa-user-o" aria-hidden="true"></i>&nbsp;&nbsp; <span id="name">${user.firstName}</span> <span id="surname">${user.lastName}</span>  <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu centered">
                        <li class="dropdown-header">Il mio profilo</li>
                        <li><a href="${pageContext.request.contextPath}/restricted/profile.jsp">Il mio account</a></li>
                        <li><a href="${pageContext.request.contextPath}/restricted/orderhistory.jsp">I miei ordini</a></li>
                        <c:if test="${user.hasShop()}">
                            <li role="separator" class="divider"></li>
                            <li class="dropdown-header">Venditore</li>
                            <li><a href="${pageContext.request.contextPath}/restricted/vendor/shop_panel.jsp">Pannello Negozio</a></li>
                            <li><a href="${pageContext.request.contextPath}/restricted/vendor/inventory.jsp">Inventario</a></li>
                        </c:if>
                        <c:if test="${user.type == 1}">
                            <li role="separator" class="divider"></li>
                            <li class="dropdown-header">Admin</li>
                            <li><a href="${pageContext.request.contextPath}/restricted/admin/admin_panel.jsp">Pannello principale</a></li>
                        </c:if>
                        <li role="separator" class="divider"></li>
                        <li><a href="${pageContext.request.contextPath}/restricted/logout">Esci</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a id="cartdrop" onclick="openCart();" class="dropdown-toggle text-center" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        <span class="badge">
                            <i class="fa fa-shopping-cart" aria-hidden="true"></i> ${user.getCart(false).totalSize()}
                        </span>
                        &nbsp;&nbsp;Carrello <span class="caret"></span>
                    </a>
                    <ul id="cartheader" class="dropdown-menu right">
                        <!-- ORA L'INTERNO DEL CARRELLO è GESTITO CON AJAX-->
                        <c:if test="${user.getCart(false).size() == 0}">
                            <li class="text-center"><a>Carrello vuoto...</a></li>
                        </c:if>
                        <c:forEach var="cartItem" items="${user.getCart(false)}">
                            <li><a href="#">${cartItem.getProduct().getProductName()} N: ${cartItem.getQuantity()}</a></li>
                        </c:forEach>
                        <li class="divider"></li>
                        <li class="text-center"><a href="${pageContext.request.contextPath}/restricted/cart.jsp">Vedi carrello <i class="fa fa-angle-double-right" aria-hidden="true"></i></a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
