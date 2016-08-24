<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <script>
            var login = "<c:out value='${login}'/>";
        </script>
        <script src="<c:url value='/static/js/classes/profile.js'/>"></script>
        <script src="<c:url value='/static/js/classes/account.js'/>"></script>
        <script src="<c:url value='/static/js/settingsController.js'/>"></script>
        <title>settings</title>
        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <t:main_panel>
            <jsp:attribute name="panel_title">Settings</jsp:attribute>
            <jsp:attribute name="panel_body">
                <div ng-controller="SettingsCtrl" style="margin-top:-15px;">
                    <ul class="nav nav-tabs">
                        <li id="tabProfile" role="presentation"
                            ng-click="setProfileSelected(true)"><a href="#">Profile</a></li>
                        <li id="tabAccount" role="presentation"
                            ng-click="setAccountSelected(true)"><a href="#">Account</a></li>
                    </ul>
                    <div ng-show="profileSelected" style="margin-top:30px;">
                        <form name="profileForm" class="ng-pristine ng-valid col-md-8 col-md-offset-2">
                            <div class="form-group input-group-sm">
                                <label for="inputName">Name</label>
                                <input id="inputName" type="text" class="form-control" ng-model="profile.name"
                                       required/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label for="inputBirthday">Birthday</label>
                                <input id="inputBirthday" type="date" class="form-control"
                                       ng-model="profile.dateOfBirth"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label for="inputCountry">Country</label>
                                <input id="inputCountry" type="text" class="form-control" ng-model="profile.country"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label for="inputCity">City</label>
                                <input id="inputCity" type="text" class="form-control" ng-model="profile.city"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label for="inputPhone">Phone</label>
                                <input id="inputPhone" type="tel" class="form-control" ng-model="profile.phone"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label for="inputInfo">Info</label>
                                <textarea id="inputInfo" class="form-control" rows="4"
                                          ng-model="profile.info"></textarea>
                            </div>
                            <button class="btn btn-sm btn-default pull-left" ng-click="resetForm()">Cancel</button>
                            <div class="pull-right">
                                <button class="btn btn-sm btn-success" ng-click="saveForm()">Save</button>
                            </div>
                            <div id="statusProfile" class="alert" role="alert"
                                 style="visibility:hidden; margin-top:60px;"></div>
                        </form>
                    </div>
                    <div ng-show="accountSelected" style="margin-top:30px;">
                        <ul class="list-group">
                            <li class="list-group-item">
                                <h3>Change e-mail</h3>
                                <form class="ng-valid">
                                    <label>E-mail</label>
                                    <p>{{account.email}}</p>
                                    <div class="form-group input-group-sm">
                                        <label for="inputEmail">New e-mail</label>
                                        <input id="inputEmail" type="email" class="form-control"
                                               ng-model="newAccount.email" required/>
                                    </div>
                                    <button class="btn btn-sm btn-success" ng-click="changeEmail()">Change e-mail
                                    </button>
                                    <div id="statusEmail" class="alert" role="alert"
                                          style="visibility: hidden; margin-top:10px;"></div>
                                </form>
                            </li>
                            <li class="list-group-item">
                                <h3>Change password</h3>
                                <form class="ng-valid">
                                    <div class="form-group input-group-sm">
                                        <label for="inputOldPassword">Old password</label>
                                        <input id="inputOldPassword" type="password" class="form-control"
                                               ng-model="passwordChange.oldPassword" required/>
                                    </div>
                                    <div class="form-group input-group-sm">
                                        <label for="inputNewPassword">New password</label>
                                        <input id="inputNewPassword" type="password" class="form-control"
                                               ng-model="passwordChange.newPassword" required/>
                                    </div>
                                    <div class="form-group input-group-sm">
                                        <label for="inputConfirmPassword">Confirm new password</label>
                                        <input id="inputConfirmPassword" type="password" class="form-control"
                                               ng-model="passwordChange.confirmPassword" required/>
                                    </div>
                                    <button class="btn btn-sm btn-success" ng-click="changePassword()">Change password
                                    </button>
                                    <div id="statusPassword" class="alert" role="alert"
                                          style="visibility: hidden; margin-top:10px;"></div>
                                </form>
                            </li>
                            <li class="list-group-item">
                                <h3>Delete account</h3>
                                <div>
                                    <button class="btn btn-sm btn-danger" ng-click="deleteAccount()">Delete account
                                    </button>
                                </div>
                                <div id="statusDelete" class="alert" role="alert"
                                     style="visibility: hidden; margin-top:10px;"></div>
                            </li>
                        </ul>
                    </div>
                </div>
            </jsp:attribute>
        </t:main_panel>
    </jsp:body>
</t:template>