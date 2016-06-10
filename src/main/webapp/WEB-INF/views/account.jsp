<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <title>account</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <t:main_panel>
            <jsp:attribute name="panel_title">Account</jsp:attribute>
            <jsp:attribute name="panel_body">
                <div ng-controller="AccountCtrl">
                    <dl class="dl-horizontal">
                        <dt>login: </dt>
                        <dd>asf</dd>
                        <dt>e-mail: </dt>
                        <dd>asdf</dd>
                    </dl>
                    <div class="col-md-offset-2 col-md-4">
                        <a class="btn btn-default btn-block"
                           href="${pageContext.request.contextPath}/email/change">Change
                            e-mail</a>
                        <a class="btn btn-primary btn-block"
                           href="${pageContext.request.contextPath}/password/change">Change
                            password</a>
                    </div>
                </div>
            </jsp:attribute>
        </t:main_panel>
        <script src="<c:url value='/static/js/account.js'/>"></script>
    </jsp:body>
</t:template>