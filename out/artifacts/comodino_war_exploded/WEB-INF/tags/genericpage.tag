<%@ tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pagetitle" fragment="true" %>
<%@ attribute name="pagecss" fragment="true" %>
<%@ attribute name="pagejavascript" fragment="true" %>

<jsp:useBean id="categoryDao" class="daos.impl.CategoryDaoImpl"/>
<c:set var="allcategories" value="${categoryDao.getCategories()}" scope="application"/>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=0.9">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>

    <link rel="apple-touch-icon" sizes="180x180" href="${pageContext.request.contextPath}/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/favicon-16x16.png">
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="mask-icon" href="${pageContext.request.contextPath}/safari-pinned-tab.svg" color="#5bbad5">
    <meta name="theme-color" content="#ffffff">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css" media="screen">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquery-ui-1.12.1/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/my.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" >
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.css">
    <jsp:invoke fragment="pagecss"/>
    <title><jsp:invoke fragment="pagetitle"/> - Comodino.it</title>
    <style>
        #overlay{
            background-color: #F3F3F4;
            position: fixed;
            height: 100%;
            width: 100%;
            z-index: 999999;
            top: 0;
            left: 0;
        }
    </style>
</head>
<body>

<div id="overlay"></div>

<c:if test="${not empty sessionScope.user}">
    <jsp:include page="/restricted/header.jsp" flush="true"/>
</c:if>
<c:if test="${empty sessionScope.user}">
    <jsp:include page="/header_anonimo.jsp" flush="true"/>
</c:if>

<t:toastnotifications/>

<jsp:doBody/>

<script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-1.12.1/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/custom.js"></script>
<script src="${pageContext.request.contextPath}/js/now-ui-kit.js"></script>
<script src="${pageContext.request.contextPath}/js/validate/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/js/validate/additional-methods.min.js"></script>
<script src="${pageContext.request.contextPath}/js/header.js"></script>

<jsp:invoke fragment="pagejavascript"/>

<script>
    $(document).ready(function() {
        openCart();
        window.scrollTo(0,0);
        $('#overlay').fadeOut(100);

        var ua = navigator.userAgent,
            iOS = /iPad|iPhone|iPod/.test(ua),
            iOS11 = /OS 11_0_1|OS 11_0_2|OS 11_0_3|OS 11_1|OS 11_1_1|OS 11_1_2|OS 11_2|OS 11_2_1/.test(ua);

        // ios 11 bug caret position
        if ( iOS && iOS11 ) {

            // Add CSS class to body
            $("body").addClass("iosBugFixCaret");

        }
    });

</script>

</body>
</html>