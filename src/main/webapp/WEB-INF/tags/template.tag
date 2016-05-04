<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Main template" pageEncoding="UTF-8" %>
<%@ attribute name="head" fragment="true" %>
<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value='/static/css/bootstrap.min.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/static/css/bootstrap-theme.min.css'/>"/>

    <jsp:invoke fragment="head"/>
</head>
<body ng-app="app" ng-cloak ng-controller="MainCtrl as main">
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<c:url value='/'/>">SocNet</a>
        </div>

        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="<c:url value='/'/>">Home</a></li>
                <li><a href="<c:url value='/profile'/>">Profile</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li id="btn_sign_in" ng-hide="auth"><a href="<c:url value='/sign_in'/>">Sign in</a></li>
                <li id="btn_sign_out" ng-show="auth"><a href="<c:url value='/sign_out'/>">Sign out</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container" style="margin-top:50px;">
    <jsp:doBody/>
</div>
<script src="<c:url value='/static/js/lib/angular.min.js'/>"></script>
<script src="<c:url value='/static/js/lib/jquery-2.2.3.js'/>"></script>
<script src="<c:url value='/static/js/lib/bootstrap.min.js'/>"></script>
<script src="<c:url value='/static/js/controllers.js'/>"></script>
</body>
</html>