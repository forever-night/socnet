<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<t:template>
<jsp:attribute name="head">
    <sec:authentication property="principal.username" var="currentLogin"/>
    <c:if test = "${profileLogin != null}">
        <script>var profileOwner = "${profileLogin}";</script>
    </c:if>
    <script>var login = "${currentLogin}";</script>
    <script src="<c:url value='/static/js/classes/profile.js'/>"></script>
    <script src="<c:url value='/static/js/profileController.js'/>"></script>
    <title>profile</title>
</jsp:attribute>
<jsp:body>
    <t:owner></t:owner>
    <div ng-controller="ProfileCtrl">
        <t:main_panel>
        <jsp:attribute name="panel_title"><strong>{{profile.name}}</strong></jsp:attribute>
        <jsp:attribute name="panel_body">
        <div>
            <div>
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
            <div class="pull-left">
                <c:if test="${profileLogin != null && profileLogin != currentLogin}">
                    <ul class="nav nav-pills nav-stacked">
                        <li><h6><a href="#">Follow</a></h6></li>
                        <li><h6><a href="#">Private Message</a></h6></li>
                    </ul>
                </c:if>
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