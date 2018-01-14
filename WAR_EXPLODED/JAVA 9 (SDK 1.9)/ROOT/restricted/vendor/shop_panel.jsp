<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Math" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" class="main.User" scope="session"/>
<jsp:useBean id="shop" class="main.Shop" scope="request"/>
<jsp:useBean id="shopproducts" class="java.util.HashMap" scope="request"/>

<t:genericpage>
    <jsp:attribute name="pagetitle">
        Pannello di controllo del negozio
    </jsp:attribute>
    <jsp:attribute name="pagecss">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor.css" >
    </jsp:attribute>

    <jsp:attribute name="pagejavascript">
        <script src="${pageContext.request.contextPath}/js/shop.js"></script>
        <c:if test="${shop.getClass().simpleName == 'PhysicalShop'}">
            <script>
                var map, infoWindow;
                function initMap() {
                    map = new google.maps.Map(document.getElementById('map'), {
                        center: {lat: ${shop.latitude}, lng:  ${shop.longitude}},
                        zoom: 7
                    });

                    var mark = {lat: ${shop.latitude}, lng: ${shop.longitude}};
                    var marker = new google.maps.Marker({
                        position: mark,
                        map: map,
                        animation: google.maps.Animation.DROP,
                        title: '${shop.name}'
                    });
                }

                function handleLocationError(browserHasGeolocation, infoWindow, pos) {
                    infoWindow.setPosition(pos);
                    infoWindow.setContent(browserHasGeolocation ?
                        'Error: The Geolocation service failed.' :
                        'Error: Your browser doesn\'t support geolocation.');
                    infoWindow.open(map);
                }
            </script>
            <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDNMIz_QgiWP6ayg3icP3ZmLXt6OE_Qync&callback=initMap"></script>
        </c:if>
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="row main-content">
                <div class="col-md-4 col-xs-12" id="navbar">
                    <div class="col-md-12">
                        <c:if test="${!empty shop.shopphoto}">
                            <img style="margin-top: 20px; border-radius: 15px" src='${shop.shopphoto[0]}' alt='images Here' width="100%" />
                        </c:if>
                        <h1 id="shopTitle" class="text-center">${shop.name}</h1>
                        <h4 id="shopEmailWebsite" class="text-center text-info"><a style="color:dodgerblue" href="${shop.website}">${shop.website.toLowerCase()}</a></h4>
                        <p>${shop.description}</p>
                        <div class="row text-center">
                            <c:choose>
                                <c:when test="${Math.round(shop.rating) == -1}">
                                    Nessuna recensione
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${Math.round(shop.rating) > 0}">
                                        <c:forEach begin="0" end="${Math.round(shop.rating) - 1}" varStatus="loop">
                                            <i class="fa fa-star" aria-hidden="true"></i>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${Math.round(shop.rating) < 5}">
                                        <c:forEach begin="0" end="${4 - Math.round(shop.rating)}" varStatus="loop">
                                            <i class="fa fa-star-o" aria-hidden="true"></i>
                                        </c:forEach>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${shop.rating > 0}">
                                <a href="${pageContext.request.contextPath}/restricted/vendor/reviews.jsp">Vedi tutte</a>
                            </c:if>
                        </div>
                        <c:choose>
                            <c:when test="${shop.getClass().simpleName == 'PhysicalShop'}">
                                <div id="addShopDiv" class="row" style="margin-bottom: 15px">
                                    <div class="col-md-12 col-xs-12">
                                        <div class="row">
                                            <div class="col-md-12">
                                            <h2 id="realShop">Negozio fisico</h2>
                                            </div>
                                        </div>
                                        <p><b>Indirizzo:</b> ${shop.address}</p>
                                        <p><b>City:</b> ${shop.city}</p>
                                        <p><b>CAP:</b> ${shop.zip}</p>
                                        <p><b>Orari:</b> ${shop.openinghours}</p>
                                    </div>
                                </div>
                                <div id="map" style="margin: 15px auto; height:250px; width:100%"></div>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-block btn-primary" data-toggle="modal" data-target="#addPhysicalShop"><i class="fa fa-fw pull-left fa-cart-plus"></i>Aggiungi negozio fisico</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="col-md-8 col-xs-12" id="mainContent">
                    <div class="row" id="panelContainer" style="margin-bottom: 15px; margin-left:15px; margin-right: 15px">
                        <div class="col-md-4" id="photoPanel">
                            <div class="col-md-12">
                                <c:if test="${!empty shop.shopphoto}">
                                    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                                        <!-- Wrapper for slides -->
                                        <div class="carousel-inner">
                                            <c:forEach items="${shop.shopphoto}" var="image" varStatus="status">
                                                <div class="item <c:if test='${status.first}'>active</c:if>">
                                                    <img src='${image}' alt='images Here' width="400" height="300"/>
                                                </div>
                                            </c:forEach>
                                        </div>

                                        <!-- Controls -->
                                        <a class="left carousel-control" href="#carousel-example-generic" data-slide="prev">
                                            <span class="glyphicon glyphicon-chevron-left"></span>
                                        </a>
                                        <a class="right carousel-control" href="#carousel-example-generic" data-slide="next">
                                            <span class="glyphicon glyphicon-chevron-right"></span>
                                        </a>
                                    </div>
                                </c:if>
                                <a id="btnAddPhoto" class="btn btn-block btn-primary" data-toggle="modal" data-target="#uploadShopPhoto"><i class="fa fa-fw fa-camera"></i> Aggiungi foto</a>
                                <a class="btn btn-block btn-primary" data-toggle="modal" data-target="#editShopInfo"><i class="fa fa-fw fa-book"></i> Modifica negozio</a>
                            </div>
                        </div>
                        <div class="col-md-1" id="mySpace2">
                        </div>
                        <div class="col-md-7" id="buttonPanel">
                            <div class="row">
                                <div class="col-md-12">
                                    <a href="${pageContext.request.contextPath}/restricted/vendor/inventory.jsp" id="btnInventario" class="btn btn-lg btn-block btn-success"><i class="fa fa-cube fa-fw fa-lg pull-left"></i>Inventario</a>

                                    <a href="${pageContext.request.contextPath}/restricted/vendor/orderreceived.jsp" id="btnOrderList" class="btn btn-lg btn-block btn-success"><i class="fa fa-fw fa-lg fa-list-ul pull-left"></i>Ordini</a>

                                    <a href="${pageContext.request.contextPath}/restricted/vendor/reviews.jsp" id="btnReviews" class="btn btn-lg btn-block btn-success"><i class="fa fa-comments-o fa-fw fa-lg pull-left"></i>Recensioni</a>

                                    <a href="${pageContext.request.contextPath}/restricted/vendor/dispute_list.jsp" id="btnDispute" class="btn btn-lg btn-block btn-success"><i class="fa fa-fw fa-lg fa-warning pull-left"></i>Dispute</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row" id="productsLeftContainer" style="margin-bottom: 15px; margin-left:15px; margin-right: 15px">
                        <div class="col-md-12">
                            <div class="row">
                                <h1 id="productsLeftTitle">Prodotti in esaurimento</h1>
                            </div>
                            <div class="row" id="productsLeftRows">
                                <c:choose>
                                    <c:when test="${shop.expiringProducts.isEmpty()}">
                                        <h3>Nessun prodotto in esaurimento</h3>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${shop.expiringProducts}" var="product">
                                            <div class="row productRow">
                                                        <div class="productNameDiv col-md-5 col-sm-6 col-xs-12">
                                                            <h5 class="productName text-center">${product.productName}</h5>
                                                        </div>
                                                        <div class="itemsLeftDiv col-md-5 col-sm-6 col-xs-12">
                                                            <h5 class="itemsLeft text-center">Prodotti rimanenti: ${product.quantity}</h5>
                                                        </div>
                                                        <div class="btnSee col-md-2 col-xs-12 text-center">
                                                            <a href="${pageContext.request.contextPath}/restricted/vendor/inventory.jsp" class="showProduct btn-sm btn-default">Vedi</a>
                                                        </div>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="editShopInfo" tabindex="-1" role="dialog" aria-labelledby="editShopInfoLabel">
            <div class="row">
                <div class="card card-signup centerize" data-background-color="orange">
                    <form id="editShopInfoForm" class="form" method="POST" action="${pageContext.request.contextPath}/restricted/vendor/editshopinfo">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Modifica dati</h4>
                        </div>
                        <div class="content">
                            <div class="input-group form-group-no-border nologin">
                          <span class="input-group-addon">
                              <i class="fa fa-user green" aria-hidden="true"></i>
                          </span>
                                <input name="ShopName" type="text" class="form-control"  value="${shop.name}" maxlength="50">
                            </div>
                            <div class="input-group form-group-no-border">
                                <span class="input-group-addon">
                                    <i class="fa fa-book green" aria-hidden="true"></i>
                                </span>
                                <input name="ShopDescription" type="text" class="form-control" value="${shop.description}" maxlength="50"/>
                            </div>
                            <div class="input-group form-group-no-border">
                                <span class="input-group-addon">
                                    <i class="fa fa-globe green" aria-hidden="true"></i>
                                </span>
                                <input name="ShopWebsite" type="text" class="form-control" value="${shop.website}" maxlength="50"/>
                            </div>
                            <c:if test="${shop.getClass().simpleName == 'PhysicalShop'}">
                                <div class="input-group form-group-no-border nologin">
                              <span class="input-group-addon">
                                  <i class="fa fa-building green" aria-hidden="true"></i>
                              </span>
                                    <input name="ShopAddress" type="text" class="form-control" value="${shop.address}" maxlength="50"/>
                                </div>
                                <div class="input-group form-group-no-border nologin">
                                <span class="input-group-addon">
                                    <i class="fa fa-map-marker green" aria-hidden="true"></i>
                                </span>
                                    <input name="ShopCity" type="text" class="form-control" value="${shop.city}" maxlength="50"/>
                                </div>
                                <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-envelope green" aria-hidden="true"></i>
                          </span>
                                    <input name="ShopCAP" type="text" class="form-control" value="${shop.zip}" maxlength="10"/>
                                </div>
                                <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-flag green" aria-hidden="true"></i>
                          </span>
                                    <input name="ShopState" type="text" class="form-control" value="${shop.state}"/>
                                </div>
                                <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-clock-o green" aria-hidden="true"></i>
                          </span>
                                    <input name="ShopHours" type="text" class="form-control" value="${shop.openinghours}" maxlength="150"/>
                                </div>
                            </c:if>
                            <div class="footer text-center">
                                <a class="btn btn-default" onclick="$('#editShopInfoForm').submit();">Salva</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>



        <div class="modal fade" id="addPhysicalShop" tabindex="-1" role="dialog" aria-labelledby="addPhysicalShopLabel">
            <div class="row">
                <div class="card card-signup centerize" data-background-color="orange">
                    <form id="addPhysicalShopForm" class="form" method="POST" action="${pageContext.request.contextPath}/restricted/vendor/addphysicalshop">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Aggiungi negozio fisico</h4>
                        </div>
                        <div class="content">

                            <div class="input-group form-group-no-border nologin">
                              <span class="input-group-addon">
                                  <i class="fa fa-map-marker green" aria-hidden="true"></i>
                              </span>
                                <input name="ShopAddress" type="text" class="form-control" placeholder="Indirizzo..." maxlength="50"/>
                            </div>
                            <div class="input-group form-group-no-border nologin">
                                <span class="input-group-addon">
                                    <i class="fa fa-building green" aria-hidden="true"></i>
                                </span>
                                <input name="ShopCity" type="text" class="form-control" placeholder="Citta'..." maxlength="50"/>
                            </div>
                            <div class="input-group form-group-no-border">
                          <span class="input-group-addon">
                              <i class="fa fa-envelope green" aria-hidden="true"></i>
                          </span>
                                <input name="ShopCAP" type="text" class="form-control" placeholder="CAP..." maxlength="10"/>
                            </div>
                            <div class="input-group form-group-no-border">
                                <span class="input-group-addon">
                                    <i class="fa fa-globe green" aria-hidden="true"></i>
                                </span>
                                    <select class="form-control " name="ShopCountry">
                                        <option selected value="Italy">Italia</option>
                                        <option value="United States">United States</option>
                                        <option value="United Kingdom">United Kingdom</option>
                                        <option value="Afghanistan">Afghanistan</option>
                                        <option value="Albania">Albania</option>z
                                        <option value="Algeria">Algeria</option>
                                        <option value="American Samoa">American Samoa</option>
                                        <option value="Andorra">Andorra</option>
                                        <option value="Angola">Angola</option>
                                        <option value="Anguilla">Anguilla</option>
                                        <option value="Antarctica">Antarctica</option>
                                        <option value="Antigua and Barbuda">Antigua and Barbuda</option>
                                        <option value="Argentina">Argentina</option>
                                        <option value="Armenia">Armenia</option>
                                        <option value="Aruba">Aruba</option>
                                        <option value="Australia">Australia</option>
                                        <option value="Austria">Austria</option>
                                        <option value="Azerbaijan">Azerbaijan</option>
                                        <option value="Bahamas">Bahamas</option>
                                        <option value="Bahrain">Bahrain</option>
                                        <option value="Bangladesh">Bangladesh</option>
                                        <option value="Barbados">Barbados</option>
                                        <option value="Belarus">Belarus</option>
                                        <option value="Belgium">Belgium</option>
                                        <option value="Belize">Belize</option>
                                        <option value="Benin">Benin</option>
                                        <option value="Bermuda">Bermuda</option>
                                        <option value="Bhutan">Bhutan</option>
                                        <option value="Bolivia">Bolivia</option>
                                        <option value="Bosnia and Herzegovina">Bosnia and Herzegovina</option>
                                        <option value="Botswana">Botswana</option>
                                        <option value="Bouvet Island">Bouvet Island</option>
                                        <option value="Brazil">Brazil</option>
                                        <option value="British Indian Ocean Territory">British Indian Ocean Territory</option>
                                        <option value="Brunei Darussalam">Brunei Darussalam</option>
                                        <option value="Bulgaria">Bulgaria</option>
                                        <option value="Burkina Faso">Burkina Faso</option>
                                        <option value="Burundi">Burundi</option>
                                        <option value="Cambodia">Cambodia</option>
                                        <option value="Cameroon">Cameroon</option>
                                        <option value="Canada">Canada</option>
                                        <option value="Cape Verde">Cape Verde</option>
                                        <option value="Cayman Islands">Cayman Islands</option>
                                        <option value="Central African Republic">Central African Republic</option>
                                        <option value="Chad">Chad</option>
                                        <option value="Chile">Chile</option>
                                        <option value="China">China</option>
                                        <option value="Christmas Island">Christmas Island</option>
                                        <option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>
                                        <option value="Colombia">Colombia</option>
                                        <option value="Comoros">Comoros</option>
                                        <option value="Congo">Congo</option>
                                        <option value="Congo, The Democratic Republic of The">Congo, The Democratic Republic of The</option>
                                        <option value="Cook Islands">Cook Islands</option>
                                        <option value="Costa Rica">Costa Rica</option>
                                        <option value="Cote D'ivoire">Cote D'ivoire</option>
                                        <option value="Croatia">Croatia</option>
                                        <option value="Cuba">Cuba</option>
                                        <option value="Cyprus">Cyprus</option>
                                        <option value="Czech Republic">Czech Republic</option>
                                        <option value="Denmark">Denmark</option>
                                        <option value="Djibouti">Djibouti</option>
                                        <option value="Dominica">Dominica</option>
                                        <option value="Dominican Republic">Dominican Republic</option>
                                        <option value="Ecuador">Ecuador</option>
                                        <option value="Egypt">Egypt</option>
                                        <option value="El Salvador">El Salvador</option>
                                        <option value="Equatorial Guinea">Equatorial Guinea</option>
                                        <option value="Eritrea">Eritrea</option>
                                        <option value="Estonia">Estonia</option>
                                        <option value="Ethiopia">Ethiopia</option>
                                        <option value="Falkland Islands (Malvinas)">Falkland Islands (Malvinas)</option>
                                        <option value="Faroe Islands">Faroe Islands</option>
                                        <option value="Fiji">Fiji</option>
                                        <option value="Finland">Finland</option>
                                        <option value="France">France</option>
                                        <option value="French Guiana">French Guiana</option>
                                        <option value="French Polynesia">French Polynesia</option>
                                        <option value="French Southern Territories">French Southern Territories</option>
                                        <option value="Gabon">Gabon</option>
                                        <option value="Gambia">Gambia</option>
                                        <option value="Georgia">Georgia</option>
                                        <option value="Germany">Germany</option>
                                        <option value="Ghana">Ghana</option>
                                        <option value="Gibraltar">Gibraltar</option>
                                        <option value="Greece">Greece</option>
                                        <option value="Greenland">Greenland</option>
                                        <option value="Grenada">Grenada</option>
                                        <option value="Guadeloupe">Guadeloupe</option>
                                        <option value="Guam">Guam</option>
                                        <option value="Guatemala">Guatemala</option>
                                        <option value="Guinea">Guinea</option>
                                        <option value="Guinea-bissau">Guinea-bissau</option>
                                        <option value="Guyana">Guyana</option>
                                        <option value="Haiti">Haiti</option>
                                        <option value="Heard Island and Mcdonald Islands">Heard Island and Mcdonald Islands</option>
                                        <option value="Holy See (Vatican City State)">Holy See (Vatican City State)</option>
                                        <option value="Honduras">Honduras</option>
                                        <option value="Hong Kong">Hong Kong</option>
                                        <option value="Hungary">Hungary</option>
                                        <option value="Iceland">Iceland</option>
                                        <option value="India">India</option>
                                        <option value="Indonesia">Indonesia</option>
                                        <option value="Iran, Islamic Republic of">Iran, Islamic Republic of</option>
                                        <option value="Iraq">Iraq</option>
                                        <option value="Ireland">Ireland</option>
                                        <option value="Israel">Israel</option>
                                        <option value="Jamaica">Jamaica</option>
                                        <option value="Japan">Japan</option>
                                        <option value="Jordan">Jordan</option>
                                        <option value="Kazakhstan">Kazakhstan</option>
                                        <option value="Kenya">Kenya</option>
                                        <option value="Kiribati">Kiribati</option>
                                        <option value="Korea, Democratic People's Republic of">Korea, Democratic People's Republic of</option>
                                        <option value="Korea, Republic of">Korea, Republic of</option>
                                        <option value="Kuwait">Kuwait</option>
                                        <option value="Kyrgyzstan">Kyrgyzstan</option>
                                        <option value="Lao People's Democratic Republic">Lao People's Democratic Republic</option>
                                        <option value="Latvia">Latvia</option>
                                        <option value="Lebanon">Lebanon</option>
                                        <option value="Lesotho">Lesotho</option>
                                        <option value="Liberia">Liberia</option>
                                        <option value="Libyan Arab Jamahiriya">Libyan Arab Jamahiriya</option>
                                        <option value="Liechtenstein">Liechtenstein</option>
                                        <option value="Lithuania">Lithuania</option>
                                        <option value="Luxembourg">Luxembourg</option>
                                        <option value="Macao">Macao</option>
                                        <option value="Macedonia, The Former Yugoslav Republic of">Macedonia, The Former Yugoslav Republic of</option>
                                        <option value="Madagascar">Madagascar</option>
                                        <option value="Malawi">Malawi</option>
                                        <option value="Malaysia">Malaysia</option>
                                        <option value="Maldives">Maldives</option>
                                        <option value="Mali">Mali</option>
                                        <option value="Malta">Malta</option>
                                        <option value="Marshall Islands">Marshall Islands</option>
                                        <option value="Martinique">Martinique</option>
                                        <option value="Mauritania">Mauritania</option>
                                        <option value="Mauritius">Mauritius</option>
                                        <option value="Mayotte">Mayotte</option>
                                        <option value="Mexico">Mexico</option>
                                        <option value="Micronesia, Federated States of">Micronesia, Federated States of</option>
                                        <option value="Moldova, Republic of">Moldova, Republic of</option>
                                        <option value="Monaco">Monaco</option>
                                        <option value="Mongolia">Mongolia</option>
                                        <option value="Montserrat">Montserrat</option>
                                        <option value="Morocco">Morocco</option>
                                        <option value="Mozambique">Mozambique</option>
                                        <option value="Myanmar">Myanmar</option>
                                        <option value="Namibia">Namibia</option>
                                        <option value="Nauru">Nauru</option>
                                        <option value="Nepal">Nepal</option>
                                        <option value="Netherlands">Netherlands</option>
                                        <option value="Netherlands Antilles">Netherlands Antilles</option>
                                        <option value="New Caledonia">New Caledonia</option>
                                        <option value="New Zealand">New Zealand</option>
                                        <option value="Nicaragua">Nicaragua</option>
                                        <option value="Niger">Niger</option>
                                        <option value="Nigeria">Nigeria</option>
                                        <option value="Niue">Niue</option>
                                        <option value="Norfolk Island">Norfolk Island</option>
                                        <option value="Northern Mariana Islands">Northern Mariana Islands</option>
                                        <option value="Norway">Norway</option>
                                        <option value="Oman">Oman</option>
                                        <option value="Pakistan">Pakistan</option>
                                        <option value="Palau">Palau</option>
                                        <option value="Palestinian Territory, Occupied">Palestinian Territory, Occupied</option>
                                        <option value="Panama">Panama</option>
                                        <option value="Papua New Guinea">Papua New Guinea</option>
                                        <option value="Paraguay">Paraguay</option>
                                        <option value="Peru">Peru</option>
                                        <option value="Philippines">Philippines</option>
                                        <option value="Pitcairn">Pitcairn</option>
                                        <option value="Poland">Poland</option>
                                        <option value="Portugal">Portugal</option>
                                        <option value="Puerto Rico">Puerto Rico</option>
                                        <option value="Qatar">Qatar</option>
                                        <option value="Reunion">Reunion</option>
                                        <option value="Romania">Romania</option>
                                        <option value="Russian Federation">Russian Federation</option>
                                        <option value="Rwanda">Rwanda</option>
                                        <option value="Saint Helena">Saint Helena</option>
                                        <option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>
                                        <option value="Saint Lucia">Saint Lucia</option>
                                        <option value="Saint Pierre and Miquelon">Saint Pierre and Miquelon</option>
                                        <option value="Saint Vincent and The Grenadines">Saint Vincent and The Grenadines</option>
                                        <option value="Samoa">Samoa</option>
                                        <option value="San Marino">San Marino</option>
                                        <option value="Sao Tome and Principe">Sao Tome and Principe</option>
                                        <option value="Saudi Arabia">Saudi Arabia</option>
                                        <option value="Senegal">Senegal</option>
                                        <option value="Serbia and Montenegro">Serbia and Montenegro</option>
                                        <option value="Seychelles">Seychelles</option>
                                        <option value="Sierra Leone">Sierra Leone</option>
                                        <option value="Singapore">Singapore</option>
                                        <option value="Slovakia">Slovakia</option>
                                        <option value="Slovenia">Slovenia</option>
                                        <option value="Solomon Islands">Solomon Islands</option>
                                        <option value="Somalia">Somalia</option>
                                        <option value="South Africa">South Africa</option>
                                        <option value="South Georgia and The South Sandwich Islands">South Georgia and The South Sandwich Islands</option>
                                        <option value="Spain">Spain</option>
                                        <option value="Sri Lanka">Sri Lanka</option>
                                        <option value="Sudan">Sudan</option>
                                        <option value="Suriname">Suriname</option>
                                        <option value="Svalbard and Jan Mayen">Svalbard and Jan Mayen</option>
                                        <option value="Swaziland">Swaziland</option>
                                        <option value="Sweden">Sweden</option>
                                        <option value="Switzerland">Switzerland</option>
                                        <option value="Syrian Arab Republic">Syrian Arab Republic</option>
                                        <option value="Taiwan, Province of China">Taiwan, Province of China</option>
                                        <option value="Tajikistan">Tajikistan</option>
                                        <option value="Tanzania, United Republic of">Tanzania, United Republic of</option>
                                        <option value="Thailand">Thailand</option>
                                        <option value="Timor-leste">Timor-leste</option>
                                        <option value="Togo">Togo</option>
                                        <option value="Tokelau">Tokelau</option>
                                        <option value="Tonga">Tonga</option>
                                        <option value="Trinidad and Tobago">Trinidad and Tobago</option>
                                        <option value="Tunisia">Tunisia</option>
                                        <option value="Turkey">Turkey</option>
                                        <option value="Turkmenistan">Turkmenistan</option>
                                        <option value="Turks and Caicos Islands">Turks and Caicos Islands</option>
                                        <option value="Tuvalu">Tuvalu</option>
                                        <option value="Uganda">Uganda</option>
                                        <option value="Ukraine">Ukraine</option>
                                        <option value="United Arab Emirates">United Arab Emirates</option>
                                        <option value="United Kingdom">United Kingdom</option>
                                        <option value="United States">United States</option>
                                        <option value="United States Minor Outlying Islands">United States Minor Outlying Islands</option>
                                        <option value="Uruguay">Uruguay</option>
                                        <option value="Uzbekistan">Uzbekistan</option>
                                        <option value="Vanuatu">Vanuatu</option>
                                        <option value="Venezuela">Venezuela</option>
                                        <option value="Viet Nam">Viet Nam</option>
                                        <option value="Virgin Islands, British">Virgin Islands, British</option>
                                        <option value="Virgin Islands, U.S.">Virgin Islands, U.S.</option>
                                        <option value="Wallis and Futuna">Wallis and Futuna</option>
                                        <option value="Western Sahara">Western Sahara</option>
                                        <option value="Yemen">Yemen</option>
                                        <option value="Zambia">Zambia</option>
                                        <option value="Zimbabwe">Zimbabwe</option>
                                    </select>
                                </div>
                            <div class="input-group form-group-no-border">
                        <span class="input-group-addon">
                              <i class="fa fa-clock-o green" aria-hidden="true"></i>
                          </span>
                                <input name="ShopHours" type="text" class="form-control" placeholder="Orario di apertura..." maxlength="150"/>
                            </div>

                            <div class="footer text-center">
                                <a class="btn btn-default" onclick="$('#addPhysicalShopForm').submit();">Salva</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="uploadShopPhoto" tabindex="-1" role="dialog" aria-labelledby="uploadShopPhotoLabel">
            <div class="row">
                <div class="card card-signup centerize" data-background-color="orange">
                    <form id="uploadShopPhotoForm" class="form" method="POST" enctype = "multipart/form-data" action="${pageContext.request.contextPath}/restricted/uploadshopphoto">
                        <div class="header header-primary text-center">
                            <h4 class="title title-up">Carica foto</h4>
                        </div>
                        <div class="content">
                            <p style="color: white">Dimensioni ideali: 400x300px</p>
                            <div class="form-row">
                                <div class="col">
                                    <input required id="upload" type="file" name="shopPhoto" accept="image/*">
                                </div>
                                <div class="col">
                                    <input readonly type="text" style="background:transparent; border: none; color: white; margin-top: 5px" id="filename">
                                    <input hidden type="text" name="shopID" value="${shop.shopID}">
                                </div>
                            </div>
                            <div class="footer text-center">
                                <a class="btn btn-default" onclick="$('#uploadShopPhotoForm').submit();">Carica</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </jsp:body>
</t:genericpage>