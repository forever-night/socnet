<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Main template" pageEncoding="UTF-8" %>
<%@ attribute name="head" fragment="true" %>
<!DOCTYPE html>
<html ng-app="app">
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value='/static/css/bootstrap.min.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/static/css/bootstrap-theme.min.css'/>"/>

    <script src="<c:url value='/static/js/lib/jquery-2.2.3.js'/>"></script>
    <script src="<c:url value='/static/js/lib/bootstrap.min.js'/>"></script>
    <script src="<c:url value='/static/js/lib/angular.min.js'/>"></script>

    <script src="<c:url value='/static/js/controllers.js'/>"></script>

    <jsp:invoke fragment="head"/>
</head>
<body ng-cloak ng-controller="MainCtrl">
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<c:url value='/'/>">SocNet</a>
        </div>

        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="<c:url value='/'/>">Home</a></li>
                <li><a href="<c:url value='/profile/{{id}}'/>">Profile</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li id="btn_sign_in" ng-hide="authorized"><a href="<c:url value='/sign_in'/>">Sign in</a></li>
                <li id="btn_sign_out"
                    ng-show="authorized"><a href="<c:url value='/sign_out'/>">Sign out</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container" style="margin-top:50px;">
    <jsp:doBody/>
</div>
</body>
</html>