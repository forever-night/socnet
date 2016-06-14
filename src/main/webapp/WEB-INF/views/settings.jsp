<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <script>
            var profileId =<c:out value='${profileId}'/>;
        </script>
        <script src="<c:url value='/static/js/classes/profile.js'/>"></script>
        <script src="<c:url value='/static/js/settingsController.js'/>"></script>
        <title>settings</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <t:main_panel>
            <jsp:attribute name="panel_title">Settings</jsp:attribute>
            <jsp:attribute name="panel_body">
                <div ng-controller="SettingsCtrl" style="margin-top:-15px;">
                    <ul class="nav nav-tabs">
                        <li role="presentation" class="active"
                            ng-click="setProfileSelected(true)"><a href="#">Profile</a></li>
                        <li role="presentation" ng-click="setAccountSelected(true)"><a href="#">Account</a></li>
                    </ul>
                    <div ng-show="profileSelected" style="margin-top:30px;">
                        <form name="profileForm" class="ng-pristine ng-valid col-md-8 col-md-offset-2">
                            <div class="form-group input-group-sm">
                                <label>Name</label>
                                <input type="text" class="form-control" ng-model="profile.name"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label>Birthday</label>
                                <input type="date" class="form-control" ng-model="profile.dateOfBirth"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label>Country</label>
                                <input type="text" class="form-control" ng-model="profile.country"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label>City</label>
                                <input type="text" class="form-control" ng-model="profile.currentCity"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label>Phone</label>
                                <input type="tel" class="form-control" ng-model="profile.phone"/>
                            </div>
                            <div class="form-group input-group-sm">
                                <label>Info</label>
                                <textarea class="form-control" rows="4" ng-model="profile.info"></textarea>
                            </div>
                            <button class="btn btn-sm btn-default pull-left" ng-click="resetForm()">Cancel</button>
                            <div class="pull-right">
                                <button class="btn btn-sm btn-success" ng-click="saveForm()">Save</button>
                                <p id="status" style="visibility:hidden;"></p>
                            </div>
                        </form>
                    </div>
                </div>
            </jsp:attribute>
        </t:main_panel>
    </jsp:body>
</t:template>