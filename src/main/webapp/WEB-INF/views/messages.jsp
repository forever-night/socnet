<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
<jsp:attribute name="head">
    <title>messages</title>
</jsp:attribute>
<jsp:body>
    <t:owner></t:owner>
    <t:main_panel>
        <jsp:attribute name="panel_title">Private Messages</jsp:attribute>
        <jsp:attribute name="panel_body">
            <div ng-controller="PrivateMessageCtrl as pm">
            </div>
        </jsp:attribute>
    </t:main_panel>
</jsp:body>
</t:template>