<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="head">
        <title>followers</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <t:main_panel>
            <jsp:attribute name="panel_title">Followers</jsp:attribute>
            <jsp:attribute name="panel_body">
                <div ng-controller="FollowersCtrl as followers">
                </div>
            </jsp:attribute>
        </t:main_panel>
    </jsp:body>
</t:template>