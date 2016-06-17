<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <title>error</title>
    </jsp:attribute>
    <jsp:body>
        <t:owner></t:owner>
        <div class="col-md-6" style="margin-top:60px;">
            <div class="alert alert-danger" role="alert">
                <b>Oops!</b><br>
                <p><c:out value='${errorMessage}'/></p>
            </div>
        </div>
    </jsp:body>
</t:template>