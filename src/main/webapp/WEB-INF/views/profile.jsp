<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <title>profile</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <t:main_panel>
            <jsp:attribute name="panel_title">Name Surname</jsp:attribute>
            <jsp:attribute name="panel_body">
            <div ng-controller="ProfileCtrl as profile">
                <div ng-hide="isEditing">
                    <dl class="dl-horizontal">
                        <dt>Age:</dt>
                        <dd>11</dd>
                        <dt>Country:</dt>
                        <dd>ass</dd>
                        <dt>City:</dt>
                        <dd>sadf</dd>
                        <dt>Phone:</dt>
                        <dd>12</dd>
                        <dt>Info:</dt>
                        <dd>asdf</dd>
                    </dl>
                </div>
                <div class="pull-left">
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
        <script src="<c:url value='/static/js/profile.js'/>"></script>
    </jsp:body>
</t:template>