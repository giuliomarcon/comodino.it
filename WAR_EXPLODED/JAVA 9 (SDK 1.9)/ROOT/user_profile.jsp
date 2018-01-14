<%@ page import="main.User" %>
<!--%@ page import="main.Product" %-->
<!--%@ page import="java.util.List" %-->

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!doctype html>
<html lang="it">
    <head>
        <title>Profilo</title>
    </head>
    <body>
        <jsp:include page="header_anonimo.jsp" flush="true" />
        <%
            User user = (User) session.getAttribute("user");
            String fullName = user.getFirstName() + user.getLastName();
        %>
        <div class="container">
            <h1 style="font-size: 100px"><%=fullName%></h1>
            <button>Modifica Password</button>
            <button>Modifica Dati</button>
            <button>Modifica Indirizzo</button>
            <button>Ordini Effettuati</button>
            <button>Dispute</button>
            <button>Negozio</button>
        </div>
    </body>
</html>
