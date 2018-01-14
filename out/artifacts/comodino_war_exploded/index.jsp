<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Home
    </jsp:attribute>

    <jsp:attribute name="pagecss">
        <link href="${pageContext.request.contextPath}/css/index.css" rel="stylesheet" type="text/css">
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/validate/jquery.validate.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/validate/additional-methods.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/index.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container-fluid dynamicTile">
            <div class="row">
                <div class="col-sm-2 col-xs-4">
                    <div id="tile1" class="tile kitchen">

                        <div class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <div class="item active">
                                    <h3 class="tilecaption noselect"><i class="fa fa-cutlery myiconresize"></i></h3>
                                </div>
                                <div class="item">
                                    <h3 class="tilecaption noselect">Cucina</h3>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-sm-4 col-xs-8">
                    <div class="tile kitchen">

                        <img src="img/cucina1.jpg" class="img-responsive"/>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div id="bagno" class="tile bagno">

                        <div class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <div class="item active">
                                    <h3 class="tilecaption noselect">Bagno</h3>
                                </div>
                                <div class="item">
                                    <h3 class="tilecaption noselect"><i class="fa fa-bath myiconresize"></i></h3>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div class="tile bagno">

                        <img src="img/bagno1.jpg" class="img-responsive"/>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div id="garden" class="tile garden">

                        <div class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <div class="item active">
                                    <h3 class="tilecaption noselect"><i class="fa fa-leaf myiconresize"></i></h3>
                                </div>
                                <div class="item">
                                    <h3 class="tilecaption noselect">Giardino</h3>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-4 col-xs-8">
                    <div class="tile bedroom">

                        <img src="img/bedroom1.jpg" class="img-responsive"/>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div id="bedroom" class="tile bedroom">

                        <div class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <div class="item active">
                                    <h3 class="tilecaption noselect">Camera da<br>letto</h3>
                                </div>
                                <div class="item">
                                    <h3 class="tilecaption noselect"><i class="fa fa-bed myiconresize"></i></h3>
                                </div>

                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div id="illumination" class="tile illumination">

                        <div class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <div class="item active">
                                    <h3 class="tilecaption noselect"><i class="fa fa-lightbulb-o myiconresize"></i></h3>
                                </div>
                                <div class="item">
                                    <h3 class="tilecaption noselect">Illuminazione</h3>
                                </div>

                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-sm-4 col-xs-8">
                    <div class="tile garden">

                        <img src="img/garden1.jpg" class="img-responsive"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-2 col-xs-4">
                    <div id="livingroom" class="tile livingroom">

                        <div class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <div class="item active">
                                    <h3 class="tilecaption noselect">Soggiorno</h3>
                                </div>
                                <div class="item">
                                    <h3 class="tilecaption noselect"><i class="fa fa-television myiconresize"></i></h3>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-sm-4 col-xs-8">
                    <div class="tile livingroom">

                        <img src="img/salotto2.jpg" class="img-responsive"/>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div class="tile illumination">

                        <img src="img/illumination2.jpg" class="img-responsive"/>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div id="studio" class="tile studio">

                        <div class="carousel slide" data-ride="carousel">
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner">
                                <div class="item active">
                                    <h3 class="tilecaption noselect"><i class="fa fa-book myiconresize"></i></h3>
                                </div>
                                <div class="item">
                                    <h3 class="tilecaption noselect">Studio</h3>
                                </div>

                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-sm-2 col-xs-4">
                    <div class="tile studio">

                        <img src="img/studio2.jpg" class="img-responsive"/>

                    </div>
                </div>

            </div>
        </div>

        <div class="modal fade" id="resetpasswordmodal" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog">
            <div class="row">
                <div id="resetpasswordcard" class="card card-signup centerize" data-background-color="orange">
                    <form id="resetpasswordform" class="form" method="POST" action="${pageContext.request.contextPath}/passwordReset">
                        <input type="password" class="hidden" name="token" placeholder="" value="${param.token}">
                        <input type="password" class="hidden" name="email" placeholder="" value="${param.email}">

                        <div class="header header-primary text-center">
                            <h4 class="title title-up" >Reset della password</h4>
                        </div>
                        <div class="content">
                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-key green" aria-hidden="true"></i>
                          </span>
                                <input id="pwda" type="password" class="form-control" name="pwda" placeholder="Password" onkeyup="check_pws();">
                            </div>
                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-key green" aria-hidden="true"></i>
                          </span>
                                <input id="pwdb" type="password" class="form-control" name="pwdb" placeholder="Ripeti password" onkeyup="check_pws();">
                            </div>

                            <div class="row text-center">
                                <span class="white" style="opacity:0;display:none" id="error_message">Le password non coincidono</span>
                            </div>

                        </div>
                        <div class="footer text-center" style="margin-top: 15px;">
                            <a class="btn btn-default" id="doButtonReset" style="padding-left: 29px; padding-right: 29px; pointer-events: none;" onclick="$('#resetpasswordform').submit();">Invia</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpage>