<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1>Exhibition Configuration</h1>

<div style="padding-bottom: 20px;">
This virtual exhibition contains the following spaces.

Select your default space here.
<c:url value="/staff/exhibit/config_add" var="postUrl" />
<form:form method="POST" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" modelAttribute="spaces">
    <select class="form-control" name="dspace">
	<c:forEach items="${spaces}" var="space">
		<option id=${space.id}>${space.name}</option>
	</c:forEach>
	</select>
<input type="submit" value="submit" />
</form:form>

