<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <title><tiles:insertAttribute name="title" /></title>

    <!-- Bootstrap core CSS -->
    <link href="<c:url value="/resources/bootstrap-4.1.2/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/jquery/jquery-ui.min.css" />" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<c:url value="/resources/bootstrap-4.1.2/dashboard.css" />" rel="stylesheet">
 	<link href="https://fonts.googleapis.com/css?family=Quicksand" rel="stylesheet">
 	<script src="<c:url value="/resources/jquery/jquery-3.3.1.min.js" />" ></script>
 	<script src="<c:url value="/resources/jquery/jquery-ui.min.js" />" ></script>
    
  </head>

  <body>
  	<tiles:importAttribute name="currentPage" scope="request" />
    <nav class="navbar navbar-dark fixed-top bg-purple flex-md-nowrap p-0 shadow">
      <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="<c:url value="/" />">Virtual Spaces 2.0</a>
      <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
      <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
          <form action="<c:url value="/logout" />" method="POST">
      	 	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<button class="btn btn-link" type="submit" title="Logout"><span data-feather="log-out"></span> Logout</button>
      	  </form>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link ${currentPage == "home" ? "active" : ""}" href="<c:url value="/staff/dashboard" />">
                  <span data-feather="home"></span>
                  Dashboard <span class="sr-only">(current)</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link ${currentPage == "space" ? "active" : ""}" href="<c:url value="/staff/space/list" />">
                  <span data-feather="box"></span>
                  Spaces
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link ${currentPage == "module" ? "active" : ""}" href="<c:url value="/staff/module/list" />">
                  <span data-feather="share-2"></span>
                  Modules
                </a>
              </li>
               <sec:authorize access="hasRole('ADMIN')">
              <li class="nav-item">
                <a class="nav-link ${currentPage == "images" ? "active" : ""}" href="<c:url value="/staff/images/list/1" />">
                  <span data-feather="image"></span>
                  Images
                </a>
              </li>
              </sec:authorize>
              <sec:authorize access="hasRole('ADMIN')">
              <li class="nav-item">
                <a class="nav-link ${currentPage == "users" ? "active" : ""}" href="<c:url value="/staff/user/list" />">
                  <span data-feather="users"></span>
                  Users
                </a>
              </li>
              </sec:authorize>
            </ul>

            <tiles:insertAttribute name="sidemenu" />
          </div>
        </nav>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
        
          <div class="row">
	          <div class="col-sm" style="padding-top: 20px;">
	            <c:if test="${param.showAlert eq true}">
	          	  <div id="errorMsg" class="alert alert-${param.alertType}">
				  	${param.message}
				  </div>
	 		    </c:if>     
	          <tiles:insertAttribute name="content" />
	          </div>
          </div>
        </main>
      </div>
    </div>
    
    <script>
	//# sourceURL=date.js
	$( document ).ready(function() {
		var longDateFormat  = 'MMM dd, yyyy, HH:mm:ss';
	
	    jQuery(".date").each(function (idx, elem) {
	        if (jQuery(elem).is(":input")) {
	            jQuery(elem).val(DateFormat.format.date(jQuery(elem).val(), longDateFormat));
	        } else {
	            jQuery(elem).text(DateFormat.format.date(jQuery(elem).text(), longDateFormat));
	        }
	    });
	    
	    $('#deleteAlert').delay(2000).fadeOut();
	});
	</script>
	
	<script src="<c:url value="/resources/extra/dateFormat.min.js" />" />
	<script src="<c:url value="/resources/extra/bootbox.min.js" />" />
	
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')</script>
    <script src="<c:url value="/resources/bootstrap-4.1.2/assets/js/popper.min.js" />"></script>
    <script src="<c:url value="/resources/bootstrap-4.1.2/js/bootstrap.min.js" />"></script>
	
    <!-- Icons -->
    <script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
    <script>
      feather.replace()
    </script>
    

  </body>
</html>
