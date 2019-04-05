<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<div style="padding-top: 60px;">

<h1>You dont seem to have the authority to access the requested page!</h1>
<h2><a href="<c:url value="/staff/dashboard"/>">Go to dashboard</a></h2>
</div>

