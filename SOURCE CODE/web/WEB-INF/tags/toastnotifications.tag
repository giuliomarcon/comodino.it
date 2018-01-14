<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Toast Notifications template" pageEncoding="UTF-8"%>
<!-- general error, see passed parameter -->
<c:if test="${not empty param.success}">
    <div id="popup" class="alert alert-success alert-dismissable fade in">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            ${param.success}
    </div>
</c:if>
<c:if test="${not empty param.warning}">
    <div id="popup" class="alert alert-warning alert-dismissable fade in">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        Attenzione: ${param.warning}
    </div>
</c:if>
<c:if test="${not empty param.error}">
    <div id="popup" class="alert alert-danger alert-dismissable fade in">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        Errore: ${param.error}
    </div>
</c:if>