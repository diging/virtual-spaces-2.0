<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<h1>Images</h1>
<style>
.pagination>li>a {
	border: 1px solid #009999;
	color: #000
}

.pagination>li.active>a {
	background: #009999;
	color: #fff;
}
</style>
<div style="padding-bottom: 20px;">This virtual exhibition
	contains the following images.</div>

<ul class="list-group list-group-flush">
	<c:forEach items="${images}" var="image">
		<li class="list-group-item"><a
			href="<c:url value="/staff/images/${image.id}" />"> <span
				data-feather="box"></span> ${image.filename}
		</a> <!-- (Created on <span class="date">${space.creationDate}</span> by ${space.createdBy}) -->
		</li>
	</c:forEach>
</ul>
<nav aria-label="...">
	<ul class="pagination">
		<li class="${currentpage == 1 ? " page-item disabled" : "page-item"}">
			<a class="page-link" aria-label="Previous"
			href="<c:url value="/staff/images/list/${currentpage-1}" />"> <span
				aria-hidden="true">&laquo;</span> <span class="sr-only">Previous</span>
		</a>
		</li>
		<c:forEach begin="1" end="${totalpages}" varStatus="loop">
			<li class="${currentpage == loop.index ? "active" : ""}"><a
				class="page-link"
				href="<c:url value="/staff/images/list/${loop.index}" />">
					${loop.index} </a></li>
		</c:forEach>
		<li class="${currentpage >= totalpages ? " page-item disabled" : "page-item"}">
			<a class="page-link" aria-label="Next"
			href="<c:url value="/staff/images/list/${currentpage+1}" />"> <span
				aria-hidden="true">&raquo;</span> <span class="sr-only">Next</span>
		</a>
		</li>
	</ul>
</nav>
<div class="pagination"></div>

