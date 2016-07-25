<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Main content panel template" pageEncoding="UTF-8" %>
<%@ attribute name="panel_title" fragment="true" %>
<%@ attribute name="panel_body" fragment="true" %>

<div class="panel panel-default col-md-6" style="margin-top:50px;">
    <div class="panel-heading">
        <h2 class="panel-title text-center">
            <jsp:invoke fragment="panel_title" />
        </h2>
    </div>
    <div class="panel-body">
        <jsp:invoke fragment="panel_body" />
    </div>
</div>