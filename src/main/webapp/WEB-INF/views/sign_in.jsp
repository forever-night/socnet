<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <title>sign in</title>
    </jsp:attribute>
    <jsp:body>
        <div ng-controller="SignInCtrl as signin">
            <form class="col-md-offset-4 col-md-4 form-horizontal">
                <h2>Sign in</h2>

                <div class="form-group">
                    <label for="inputLogin" class="sr-only">Login</label>
                    <input type="text" id="inputLogin" class="form-control" placeholder="Login" required autofocus>
                </div>
                <div class="form-group">
                    <label for="inputPassword" class="sr-only">Password</label>
                    <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
                </div>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="remember-me"> Remember me
                    </label>
                </div><br>
                <button class="btn btn-lg btn-success btn-block" type="submit">Sign in</button>
                <a href="${pageContext.request.contextPath}/sign_up" class="btn btn-default btn-block">Sign up</a>
            </form>
        </div>
        <script src="<c:url value='/static/js/sign_in.js'/>"></script>
    </jsp:body>
</t:template>