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
	<label for="exhibition">Select the exhibition you would like to configure:
	</label>
	<select class="form-control" name="exhibitionParam">
      <option id="New" value="">New Exhibition</option>
	  <c:forEach items="${exhibitionsList}" var="exhibition">
	    <option id=${exhibition.id} value=${exhibition.id}>${exhibition.id}</option>
	  </c:forEach>
	</select>
	<label for="space">Select the start space of the exhibition:</label>
	<select class="form-control" name="spaceParam">
      <c:forEach items="${spacesList}" var="space">
		<option id=${space.id} value=${space.id}>${space.name}</option>
	  </c:forEach>
	</select>
	<input type="submit" value="submit" />
  </form:form>
</div>