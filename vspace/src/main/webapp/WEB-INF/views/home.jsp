<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="jumbotron col-md-12">

<sec:authorize access="isAnonymous()">
<h1>Congratulation!</h1>
<p>
You did it. This basic webapp is all set up now. Try to login as "admin" with password "admin".
</p>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
<h1>Whoohoo!</h1>
<p>You are logged in.</p>
</sec:authorize>
</div>