<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- Custom styles for this template -->
<link href="<c:url value="/resources/bootstrap-4.1.2/login.css" />"
	rel="stylesheet">

<div class="row">
<div class="col-sm"></div>
<div class="jumbotron col-sm">

	<sec:authorize access="isAnonymous()">
		<form name='f' class="form-horizontal"
			action="<c:url value="" />" method="POST">
			<h2 class="col-sm-12">Login</h2>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<div class="form-group">
				<label class="control-label col-sm-12" for="username">Username:</label>
				<div class="col-sm-12">
					<input placeholder="Username" class="form-control input-sm"
						type="text" id="username" name="username" />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-12" for="password">Password:</label>
				<div class="col-sm-12">
					<input placeholder="Password" class="form-control input-sm"
						type="password" id="password" name="password" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<button type="submit" class="btn btn-primary bg-dark-blue">Submit</button>
				</div>
			</div>
		</form>
		<div class="float-right">
		<small>
		Don't have an account yet? <a href="<c:url value="/register" />">Sign up!</a><br>
		Forgot your password? <a href="<c:url value="/reset/password/request" />">Reset it!</a>
		</small>
		</div>
	</sec:authorize>
</div>
<div class="col-sm"></div>
</div>

