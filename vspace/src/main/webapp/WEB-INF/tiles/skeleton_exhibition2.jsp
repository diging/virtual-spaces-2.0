<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!doctype html>
<html lang="en" class="h-100">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Virtual Exhibition</title>

    <!-- Bootstrap core CSS -->
    <link href="<c:url value="/resources/bootstrap-4.3.1-dist/css/bootstrap.min.css" />" rel="stylesheet" >


    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
    <!-- Custom styles for this template -->
    <link href="<c:url value="/resources/bootstrap-4.3.1-dist/sticky-footer.css" />" rel="stylesheet">
  </head>
  <body class="d-flex flex-column h-100">
    <!-- Begin page content -->
	<main role="main" class="flex-shrink-0" style="padding-top: 20px;">
	  <tiles:insertAttribute name="content" />
	</main>

	<footer class="footer mt-auto">
	  <div class="container">
	    <span class="text-muted">This web application was build with Virtual Spaces 2.0 (https://github.com/diging/virtual-spaces-2.0).</span>
	  </div>
	</footer>

<!-- Icons -->
 <script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
 <script>
   feather.replace()
 </script>
</body>
</html>
