<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div style="min-height:780px">
<c:if test="${empty exhibition}">
<div class="jumbotron col-md-12">
<h1>Welcome!</h1>
<p>
This virtual exhibition has not yet been set up. Please check back later.
</p>
</div>
</c:if>
</div>
