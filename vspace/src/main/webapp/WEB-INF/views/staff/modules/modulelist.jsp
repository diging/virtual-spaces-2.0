<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<h1>Modules</h1>

<div style="padding-bottom: 20px;">This virtual exhibition
	contains the following modules.</div>

<ul class="list-group list-group-flush">
	<c:forEach items="${vspacMmodules}" var="module">
		<li class="list-group-item">${module.name}</li>
	</c:forEach>
</ul>
