<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- Custom styles for this template -->
<link href="<c:url value="/resources/bootstrap-4.1.2/login.css" />"
	rel="stylesheet">

<div style="min-height:780px">
<div class="row justify-content-center">
	<div class="col-8">
		<div class="jumbotron">
            <h2>Setup Complete</h2>
			<p>You've successfully setup an admin user for Virtual Spaces. You can now 
            login as ${adminUsername}.</p>
            
            <p>It is recommend to use the admin user only for administrative purposes, 
            and create separate user accounts for every user working on the Virtual Space.</p>
            
            <a href="<c:url value="/login" />">Login to Virtual Spaces</a>
		</div>
	</div>
</div>
</div>

