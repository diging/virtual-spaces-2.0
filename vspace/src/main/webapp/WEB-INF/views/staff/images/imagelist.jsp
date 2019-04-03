<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h1>Images</h1>

<div style="padding-bottom: 20px;">
This virtual exhibition contains the following images.

</div>

<ul class="list-group list-group-flush">
<c:forEach items="${images}" var="image">
	<li class="list-group-item">
		<a href="<c:url value="/staff/images/${image.id}" />">
		<span data-feather="box"></span> ${image.filename}
		</a>
		
		<!-- (Created on <span class="date">${space.creationDate}</span> by ${space.createdBy}) -->
	</li>
</c:forEach>
</ul>
