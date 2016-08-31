<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:attribute name="head">
        <title>error</title>
    </jsp:attribute>
    <jsp:body>
        <div class="col-sm-6" style="margin-top:60px;">
            <div><img width="505" height="396" src="${pageContext.request.contextPath}/static/img/grumpycat.png"/></div>
            <div class="alert alert-danger" role="alert">
                <b>Oops!</b><br>
                <c:choose>
                    <c:when test="${param.errorMessage != null}">
                        <p><c:out value="${param.errorMessage}"/></p>
                    </c:when>
                    <c:otherwise>
                        <p><c:out value='${errorMessage}'/></p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:template>