<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <title>sign up</title>
    </jsp:attribute>
    <jsp:body>
        <div ng-controller="SignUpCtrl as signup">
            <form class="col-md-offset-4 col-md-4 form-horizontal">
                <h2>Sign up</h2>

                <div class="form-group">
                    <label for="inputLogin" class="sr-only">Login</label>
                    <input type="text" id="inputLogin" class="form-control" placeholder="Login" required autofocus>
                </div>
                <div class="form-group">
                    <label for="inputEmail" class="sr-only">e-mail</label>
                    <input type="email" id="inputEmail" class="form-control" placeholder="e-mail" required>
                </div>
                <div class="form-group">
                    <label for="inputPassword" class="sr-only">Password</label>
                    <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
                </div>
                <div class="form-group">
                    <label for="inputConfirmPassword" class="sr-only">Confirm Password</label>
                    <input type="password" id="inputConfirmPassword" class="form-control"
                           placeholder="Confirm Password" required>
                </div><br>
                <button class="btn btn-lg btn-success btn-block" type="submit">Sign up</button>
                <a href="${pageContext.request.contextPath}/sign_in" class="btn btn-default btn-block">I already have an account</a>
            </form>
        </div>
        <script src="<c:url value='/static/js/sign_up.js'/>"></script>
    </jsp:body>
</t:template>