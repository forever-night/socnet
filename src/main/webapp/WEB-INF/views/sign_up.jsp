<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <script src="<c:url value='/static/js/signUpController.js'/>"></script>
        <title>sign up</title>
    </jsp:attribute>
    <jsp:body>
        <div ng-controller="SignUpCtrl">
            <form class="col-md-offset-4 col-md-4 form-horizontal">
                <h2>Sign up</h2>

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
                    <input type="password" class="form-control"
                           placeholder="Confirm Password" required/>
                </div>
                <br>
                <button class="btn btn-lg btn-success btn-block" ng-click="postAccount(account)">Sign up</button>
                <a href="${pageContext.request.contextPath}/sign_in" class="btn btn-default btn-block">I already have an
                    account</a>
            </form>
        </div>
    </jsp:body>
</t:template>