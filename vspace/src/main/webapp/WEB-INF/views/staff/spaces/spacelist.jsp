<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<h1>Spaces</h1>

<div style="padding-bottom: 20px;">This virtual exhibition
	contains the following spaces.</div>

<ul class="list-group list-group-flush">
	<c:forEach items="${spaces}" var="space">
		<li class="list-group-item"><a
			href="<c:url value="/staff/space/${space.id}" />"> <span
				data-feather="box"></span> ${space.name}
		</a> (Created on <span class="date">${space.creationDate}</span> by
			${space.createdBy})</li>
	</c:forEach>
</ul>
