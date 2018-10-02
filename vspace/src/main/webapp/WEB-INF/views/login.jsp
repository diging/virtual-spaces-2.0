<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- Custom styles for this template -->
<link href="<c:url value="/resources/bootstrap-4.1.2/login.css" />"
	rel="stylesheet">

<div class="jumbotron col-md-12">

	<sec:authorize access="isAnonymous()">
		<form name='f' class="form-horizontal pull-right"
			action="<c:url value="" />" method="POST">
			<h2 class="col-sm-10">Login</h2>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<div class="form-group">
				<label class="control-label col-sm-2" for="username">Username:</label>
				<div class="col-sm-10">
					<input placeholder="Username" class="form-control input-sm"
						type="text" id="username" name="username" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2" for="password">Password:</label>
				<div class="col-sm-10">
					<input placeholder="Password" class="form-control input-sm"
						type="password" id="password" name="password" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary bg-dark-blue">Submit</button>
				</div>
			</div>
		</form>
	</sec:authorize>
</div>