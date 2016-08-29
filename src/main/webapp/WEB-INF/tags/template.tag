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

    <script>context = "${pageContext.request.contextPath}"; </script>
    <script src="<c:url value='/static/js/controllers.js'/>"></script>

    <jsp:invoke fragment="head"/>
</head>
<body ng-cloak ng-controller="MainCtrl">
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<c:url value='/'/>">SocNet</a>
        </div>

        <div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="<c:url value='/'/>">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                <li><a href="${pageContext.request.contextPath}/settings">Settings</a></li>
            </ul>
            <form action="${pageContext.request.contextPath}/logout" method="post"
                  class="navbar-form navbar-right">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-warning">Log Out</button>
            </form>
        </div>
    </div>
</nav>

<div class="container" style="margin-top:50px;">
    <jsp:doBody/>
</div>
</body>
</html>