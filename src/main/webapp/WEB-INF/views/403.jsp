<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>
    <jsp:attribute name="head">
        <title>HTTP 403</title>
    </jsp:attribute>
    <jsp:body>
        <div class="col-md-6" style="margin-top:60px;">
            <div><img width="505" height="396" src="${pageContext.request.contextPath}/static/img/grumpycat.png"/></div>
            <div class="alert alert-danger" role="alert">
                <b>Oops!</b><br>
                <p>Access denied!</p><br>
                <p><a href="${pageContext.request.contextPath}/">Home</a></p>
            </div>
        </div>
    </jsp:body>
</t:template>
