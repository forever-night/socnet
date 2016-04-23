<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Main template" pageEncoding="UTF-8" %>
<%@ attribute name="head" fragment="true" %>
<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value='/static/css/bootstrap.min.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/static/css/bootstrap-theme.min.css'/>"/>
    <script src="<c:url value='/static/js/jquery-2.2.3.js'/>"></script>
    <script src="<c:url value='/static/js/bootstrap.min.js'/>"></script>

    <jsp:invoke fragment="head"/>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<c:url value="/"/>">SocNet</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="<c:url value="/"/>">Home</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container" style="margin-top:50px;">
    <jsp:doBody/>
</div>
</body>
</html>