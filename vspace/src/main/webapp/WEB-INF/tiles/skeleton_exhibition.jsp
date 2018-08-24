<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>Virtual Exhibition</title>

<link href="https://fonts.googleapis.com/css?family=Acme"
	rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Catamaran"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.2.0/css/all.css"
	integrity="sha384-hWVjflwFxL6sNzntih27bfxkr27PmbbK/iSvJ+a4+0owXq79v+lsFkW54bOGbiDQ"
	crossorigin="anonymous">

<script src="<c:url value="/resources/jquery/jquery-3.3.1.min.js" />"></script>
<script src="<c:url value="/resources/jquery/jquery-ui.min.js" />"></script>

<!-- Bootstrap core CSS -->
<link
	href="<c:url value="/resources/bootstrap-4.1.2/css/bootstrap.min.css" />"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link
	href="<c:url value="/resources/bootstrap-4.1.2/navbar-top-fixed.css" />"
	rel="stylesheet">
</head>

<body>

	<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark-blue">
		<a class="navbar-brand" href="#">VSpace</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarCollapse" aria-controls="navbarCollapse"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarCollapse">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item active"><a class="nav-link"
					href="<%=request.getContextPath()+"#"%>">Home <span
						class="sr-only">(current)</span></a></li>

			</ul>
			<div class="form-horizontal mt-2 mt-md-0">
				<sec:authorize access="isAuthenticated()">
					<form action="<c:url value="/logout" />" method="POST">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<button class="btn btn-link" type="submit" title="Logout">
							Logout</button>
					</form>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
					<a class="href-attr"
						href="<%=request.getContextPath()+"/login/authenticate" %>">Login</a>
				</sec:authorize>
			</div>
		</div>
	</nav>

	<main role="main" class="container"> <tiles:insertAttribute
		name="content" /> </main>

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="<c:url value="/resources/bootstrap-4.1.2/assets/js/popper.min.js" />"></script>
	<script
		src="<c:url value="/resources/bootstrap-4.1.2/js/bootstrap.min.js" />"></script>

	<!-- Icons -->
	<script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
	<script>
      feather.replace()
    </script>

</body>
</html>
