<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h1>Spaces</h1>

<div style="padding-bottom: 20px;">
This virtual exhibition contains the following spaces.
</div>
<div id="errorMessage" class="alert alert-danger alert-dismissible fade show" role="alert" style="display: none;">
  <strong></strong>
  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
    <span aria-hidden="true">&times;</span>
  </button>
</div>
<ul class="list-group list-group-flush">
<c:forEach items="${spaces}" var="space">
	<li class="list-group-item">
		<a href="<c:url value="/staff/space/${space.id}" />">
		<span data-feather="box"></span> ${space.name}
		</a>
		
		(Created on <span class="date">${space.creationDate}</span> by ${space.createdBy})
		<a href="#" data-url="<c:url value="/staff/space/${space.id}?${_csrf.parameterName}=${_csrf.token}"/>"  data-call-on-success = "<c:url value="/staff/space/list"/>" data-toggle="modal" data-target="#confirm-delete"><span data-feather="trash-2"></span></a>
	</li>
</c:forEach>
</ul>
