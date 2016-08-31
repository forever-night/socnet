<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <script src="<c:url value='/static/js/classes/account.js'/>"></script>
        <script src="<c:url value='/static/js/signupController.js'/>"></script>
        <title>sign up</title>
        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>
    </jsp:attribute>
    <jsp:body>
        <div ng-controller="SignupCtrl">
            <form class="col-sm-offset-4 col-sm-4 form-horizontal" method="post">
                <h2 style="margin-bottom: 15px;">Sign up</h2>
                <div class="form-group">
                    <label class="sr-only">Login</label>
                    <input type="text" ng-model="account.login" class="form-control" placeholder="Login"
                           required autofocus/>
                </div>
                <div class="form-group">
                    <label class="sr-only">e-mail</label>
                    <input type="email" ng-model="account.email" class="form-control" placeholder="e-mail"
                           required/>
                </div>
                <div class="form-group">
                    <label class="sr-only">Password</label>
                    <input type="password" ng-model="account.password" class="form-control"
                           placeholder="Password" required/>
                </div>
                <div class="form-group">
                    <label class="sr-only">Confirm Password</label>
                    <input type="password" ng-model="confirmPassword" class="form-control"
                           placeholder="Confirm Password" required/>
                </div><br>
                <div id="status" class="alert" role="alert" style="visibility: hidden;">
                </div>
                <button class="btn btn-lg btn-success btn-block" ng-click="signUp()">Sign up</button>
                <a href="${pageContext.request.contextPath}/signIn" class="btn btn-default btn-block">I already have an
                    account</a>
            </form>
        </div>
    </jsp:body>
</t:template>