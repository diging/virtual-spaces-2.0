<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="t"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1>Exhibition Configuration</h1>

<div style="padding-bottom: 20px;">
  <c:url value="/staff/exhibit/config" var="postUrl" />
  <form:form method="POST"
             action="${postUrl}?${_csrf.parameterName}=${_csrf.token}">
	<input type="hidden" name="exhibitionParam" value="${exhibition.id}" />
	<label for="space">Select the start space of the exhibition:</label>
	<select class="form-control" name="spaceParam">
      <c:forEach items="${spacesList}" var="space">
		<option id=${space.id} value=${space.id} <c:if test="${space==exhibition.startSpace}">selected</c:if>>${space.name}</option>
	  </c:forEach>
	</select>
	<div style="padding-top: 10px;" >
	<label for="title">Exhibition Title:</label>
	<input type="text" class="form-control" name="title" value="${exhibition.title}" />
	</div>
	
	<p style="padding-top: 10px;"><input class="btn btn-primary" type="submit" value="submit" /></p>
 
  </form:form>
</div>