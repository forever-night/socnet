<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="head">
        <title>communities</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <t:main_panel>
            <jsp:attribute name="panel_title">Communities</jsp:attribute>
            <jsp:attribute name="panel_body">
                <div ng-controller="CommunitiesCtrl as communities">
                </div>
            </jsp:attribute>
        </t:main_panel>
    </jsp:body>
</t:template>