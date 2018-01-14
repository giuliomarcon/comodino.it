<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        404
    </jsp:attribute>

    <jsp:attribute name="pagecss">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
    </jsp:attribute>
    <jsp:body>
        <div class="jumbotron" style="background-color: rgb(243, 243, 244)">
            <div class="container-fluid text-center" style="margin-top: 20px; margin-left: 20px;">
                <h2>Pagina non trovata</h2>
                <p>La risorsa richiesta non esiste</p>
                <p><a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/" role="button">Torna alla Home</a></p>
            </div>
        </div>
    </jsp:body>
</t:genericpage>