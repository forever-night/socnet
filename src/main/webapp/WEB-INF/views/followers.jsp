<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<t:template>
<jsp:attribute name="head">
    <sec:authentication property="principal.username" var="currentLogin"/>
    <c:if test="${profileLogin != null}">
        <script>var ownerLogin = "${profileLogin}";</script>
    </c:if>
    <script>var currentLogin = "${currentLogin}";</script>
    <script src="<c:url value="/static/js/classes/profile.js"/>"></script>
    <script src="<c:url value="/static/js/profileController.js"/>"></script>
    <script src="<c:url value="/static/js/followersController.js"/>"></script>
    <title>followers</title>
</jsp:attribute>
<jsp:body>
    <t:owner></t:owner>
    <div ng-controller="FollowersCtrl">
        <div class="col-sm-6" style="margin-top:50px;">
            <div id="status" class="alert" role="alert" style="visibility: hidden; margin-top:20px;"></div>
            <div><h3>{{profileLogin}}'s followers</h3></div>
            <div style="margin-top:50px;">
                <div class="panel panel-primary" ng-repeat="follower in followers">
                <div class="panel-body">
                    <div class="pull-right">
                        <button type="button" class="btn btn-primary" ng-show="follower.login != currentLogin">
                            <span class="glyphicon glyphicon-envelope"></span>
                        </button>
                    </div>
                    <div class="col-sm-8"><span class="glyphicon glyphicon-user"></span>
                        <a href="${pageContext.request.contextPath}/profile/{{follower.login}}">
                            <b>{{follower.login}}</b></a>
                    </div>
                    <div class="col-sm-8">{{follower.profile.name}}</div>
                </div>
                </div>
            </div>
        </div>
    </div>
</jsp:body>
</t:template>