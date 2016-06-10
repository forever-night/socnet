<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <script>
            var profileId =<c:out value='${profileId}'/>;
            console.log("building jsp for id " + profileId);
        </script>
        <script src="<c:url value='/static/js/classes/Profile.js'/>"></script>
        <script src="<c:url value='/static/js/profileController.js'/>"></script>
        <title>profile</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <div ng-controller="ProfileCtrl">
            <t:main_panel>
            <jsp:attribute name="panel_title">{{profile.name}}</jsp:attribute>
            <jsp:attribute name="panel_body">
            <div>
                <div ng-hide="isEditing">
                    <dl class="dl-horizontal">
                        <dt>Birthday:</dt>
                        <dd>{{profile.dateOfBirth}}</dd>
                        <dt>Country:</dt>
                        <dd>{{profile.country}}</dd>
                        <dt>City:</dt>
                        <dd>{{profile.city}}</dd>
                        <dt>Phone:</dt>
                        <dd>{{profile.phone}}</dd>
                        <dt>Info:</dt>
                        <dd>{{profile.info}}</dd>
                    </dl>
                </div>
                <div ng-show="isEditing">
                    <div class="form-group">
                        <label>Birthday</label>
                        <input ng-model="profile.dateOfBirth" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Country</label>
                        <input ng-model="profile.country" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>City</label>
                        <input ng-model="profile.city" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Phone</label>
                        <input ng-model="profile.phone" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Info</label>
                        <input ng-model="profile.info" class="form-control" />
                    </div>
                    <button class="btn btn-sm btn-success" ng-click="putProfile(profile)">Save</button>
                </div>
                <div class="pull-left">
                    <button class="btn btn-sm btn-default" ng-if="isOwner && !isEditing" ng-click="setEditing(true)">
                        Edit</button>
                    <br><br>
                    <ul ng-hide="isOwner" class="nav nav-pills nav-stacked">
                        <li><h6><a href="#">Follow</a></h6></li>
                        <li><h6><a href="#">Private Message</a></h6></li>
                    </ul>
                    <ul class="nav nav-pills nav-stacked">
                        <li><h6><a href="${pageContext.request.contextPath}/followers">Followers</a></h6></li>
                        <li><h6><a href="${pageContext.request.contextPath}/communities">Communities</a></h6></li>
                    </ul>
                </div>
            </div>
            </jsp:attribute>
            </t:main_panel>
        </div>
    </jsp:body>
</t:template>