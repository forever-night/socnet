<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<t:template>
<jsp:attribute name="head">
    <script src="<c:url value="/static/js/classes/profile.js"/>"></script>
    <c:if test="${param.query != null}">
        <script>paramSearchQuery = "<c:out value="${param.query}"/>";</script>
    </c:if>
    <sec:csrfMetaTags/>
    <sec:authentication property="principal.username" var="currentLogin"/>
    <script>var currentLogin = "${currentLogin}";</script>
    <script src="<c:url value="/static/js/searchResultController.js"/>"></script>
    <script src="<c:url value="/static/js/profileController.js"/>"></script>
    <title>search</title>
</jsp:attribute>
<jsp:body>
    <t:owner></t:owner>
    <div ng-controller="SearchResultCtrl">
        <div class="col-sm-6" style="margin-top:50px;">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="Search" ng-model="searchQuery" required>
                <span class="input-group-btn">
                <a href="${pageContext.request.contextPath}/search?query={{searchQuery}}"
                   class="btn btn-default" type="button">
                    <span class="glyphicon glyphicon-search"></span>
                </a>
            </span>
            </div>
            <div id="status" class="alert" role="alert" style="visibility: hidden; margin-top:20px;"></div>
            <div style="margin-top:50px;">
                <div class="panel panel-primary" ng-repeat="result in searchResult">
                    <div class="panel-body">
                    <div class="pull-right col-sm-4" ng-if="result.login != currentLogin">
                        <div class="btn-group" role="group">
                            <button type="button" class="btn btn-default"
                                    ng-click="follow(result.login, $index)">Follow</button>
                            <button type="button" class="btn btn-primary">
                                <span class="glyphicon glyphicon-envelope"></span>
                            </button>
                        </div>
                    </div>
                    <div class="col-sm-8"><span class="glyphicon glyphicon-user"></span>
                        <a href="${pageContext.request.contextPath}/profile/{{result.login}}">
                            <b>{{result.login}}</b></a>
                    </div>
                    <div class="col-sm-8">{{result.profile.name}}</div>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>
</jsp:body>
</t:template>