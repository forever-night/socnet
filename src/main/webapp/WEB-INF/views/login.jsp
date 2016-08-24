<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <script src="<c:url value='/static/js/classes/account.js'/>"></script>
        <script src="<c:url value='/static/js/loginController.js'/>"></script>
        <title>log in</title>
    </jsp:attribute>
    <jsp:body>
        <div ng-controller="LoginCtrl">
            <form class="col-md-offset-4 col-md-4 form-horizontal" method="post" action="<c:url value="/login"/>">
                <c:if test="${param.error != null}">{{loginError()}}</c:if>
                <c:if test="${param.logout != null}">{{logoutSuccessful()}}</c:if>
                <c:if test="${param.signup != null}">{{signupSuccessful()}}</c:if>
                <c:if test="${param.delete != null}">{{deleteSuccessful()}}</c:if>
                <h2 style="margin-bottom: 15px;">Log in</h2>
                <div class="form-group" ng-click="hideStatus()">
                    <label for="username" class="sr-only">Login</label>
                    <input type="text" id="username" name="username" class="form-control" placeholder="Login"
                           required/>
                </div>
                <div class="form-group" ng-click="hideStatus()">
                    <label for="password" class="sr-only">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password"
                           required/>
                </div>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="remember-me">Remember me
                    </label>
                </div>
                <div id="status" class="alert" role="alert" style="visibility: hidden; margin-top:20px;"></div>
                <br>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn btn-lg btn-success btn-block" type="submit">Log In</button>
                <a href="${pageContext.request.contextPath}/signup" class="btn btn-default btn-block">Sign Up</a>
            </form>
        </div>
    </jsp:body>
</t:template>