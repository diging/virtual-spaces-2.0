<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="jumbotron col-md-12">

<sec:authorize access="isAnonymous()">
<form name='f' class="form-inline pull-right" action="<c:url value="authenticate" />" method="POST">
				Login:
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	  			<input placeholder="Username" class="form-control input-sm" type="text" id="username" name="username"/>        
			    <br><input placeholder="Password" class="form-control input-sm" type="password" id="password" name="password"/>    
			    <br><button type="submit" class="btn btn-link"><i class="fas fa-sign-in-alt"></i></button>
			</form>
</sec:authorize>
</div>