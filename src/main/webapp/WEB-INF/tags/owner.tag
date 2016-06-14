<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Owner menu template" pageEncoding="UTF-8" %>

<div class="pull-left col-md-2 col-md-offset-2" style="margin-top:50px;">
    <ul class="nav nav-pills nav-stacked">
        <li><h6><a href="${pageContext.request.contextPath}/profile">Profile</a></h6></li>
        <li><h6><a href="${pageContext.request.contextPath}/account">Account</a></h6></li>
        <li><h6><a href="${pageContext.request.contextPath}/private">Messages</a></h6></li>
        <li><h6><a href="${pageContext.request.contextPath}/followers">Followers</a></h6></li>
        <li><h6><a href="${pageContext.request.contextPath}/communities">Communities</a></h6></li>
        <li><h6><a href="${pageContext.request.contextPath}/settings/">Settings</a></h6></li>
    </ul>
</div>