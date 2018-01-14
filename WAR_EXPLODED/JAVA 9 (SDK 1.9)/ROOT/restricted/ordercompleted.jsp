<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Ordine Completato
    </jsp:attribute>

    <jsp:attribute name="pagecss">
        <link href="${pageContext.request.contextPath}/css/ordercompleted.css" rel="stylesheet" type="text/css">
    </jsp:attribute>

    <jsp:body>
        <div class="container text-center">
            <h1><i class="fa fa-rocket fa-4x"></i></h1>
            <h1>Pagamento avvenuto con successo!</h1>
            <h3>Ordine N: ${param.orderid}</h3>
            <a id="ordini" href="${pageContext.request.contextPath}/restricted/orderhistory.jsp" class="btn btn-primary">I Miei Ordini</a>
        </div>
    </jsp:body>
</t:genericpage>


