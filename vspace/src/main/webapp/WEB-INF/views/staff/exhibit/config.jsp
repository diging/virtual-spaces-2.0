<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="t"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1>Exhibition Configuration</h1>

<div style="padding-bottom: 20px;">
	Select your exhibition and default space here
	<c:url value="/staff/exhibit/config" var="postUrl" />
	<t:htmlTag value="br" />
	<form:form method="POST"
		action="${postUrl}?${_csrf.parameterName}=${_csrf.token}"
		modelAttribute="exhibit,spaces">
		<select class="form-control" name="dexhibit">
		<option id="New" value="New">New Exhibition</option>
			<c:forEach items="${exhibit}" var="exhibit">
				<option id=${exhibit.id } value=${exhibit.id}>${exhibit.id}</option>
			</c:forEach>
		</select>
		<select class="form-control" name="dspace">
			<c:forEach items="${spaces}" var="space">
				<option id=${space.id } value=${space.id}>${space.name}</option>
			</c:forEach>
		</select>
		<input type="submit" value="submit" />
	</form:form>