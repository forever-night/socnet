<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="head">
        <title>password change</title>
    </jsp:attribute>
    <jsp:body>
        <div ng-controller="PasswordChangeCtrl as passwordChange">
            <form class="col-md-offset-4 col-md-4 form-horizontal">
                <h2>Change Password</h2>
                <div class="form-group input-group-sm">
                    <label for="inputOld" class="sr-only">Old password</label>
                    <input type="password" id="inputOld" class="form-control" placeholder="Old password" required autofocus>
                </div>
                <div class="form-group input-group-sm">
                    <label for="inputNew" class="sr-only">New password</label>
                    <input type="password" id="inputNew" class="form-control" placeholder="New password" required autofocus>
                </div>
                <div class="form-group input-group-sm">
                    <label for="inputConfirm" class="sr-only">Confirm new password</label>
                    <input type="password" id="inputConfirm" class="form-control" placeholder="Confirm new password"
                           required autofocus>
                </div>
                <button class="btn btn-success" type="submit">Save</button>
            </form>
        </div>
        <script src="<c:url value='/static/js/password_change.js'/>"></script>
    </jsp:body>
</t:template>
