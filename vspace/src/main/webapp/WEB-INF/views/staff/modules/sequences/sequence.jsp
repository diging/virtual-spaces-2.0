<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<body>
	<ol class="breadcrumb">
	  <li class="breadcrumb-item"><a href="<c:url value="/staff/dashboard" />">Dashboard</a></li>
	  <li class="breadcrumb-item"><a href="<c:url value="/staff/module/list" />">Modules</a></li>
	  <li class="breadcrumb-item"><a href="<c:url value="/staff/module/${module.id}" />">${module.name}</a></li>
	  <li class="breadcrumb-item active">${sequence.name}</li>
	</ol>
	
	<h1>Sequence: ${sequence.name}</h1>
	<div class="alert alert-light" role="alert">
	  Created on <span class="date">${sequence.creationDate}</span> by ${sequence.createdBy}.<br>
	  Modified on <span class="date">${sequence.modificationDate}</span> by ${sequence.modifiedBy}.
	</div>
	<h5>Description:</h5>
	<p>${sequence.description}</p>
	    
	<div id="slideSpace">
		<table width="100%" height="50%" style="margin-top: 50px;">
			<c:forEach items="${slides}" var="slide">
				<tr>
					<td
						style="padding-left: 22px; border-style: hidden; padding-top: 6px; padding-bottom: 6px;">
						<div class="card" style="max-width: 18rem;">
							<div align="left" class="card-body">
								<a
									href="<c:url value="/staff/module/${module.id}/slide/${slide.id}" />">
									<h5 class="card-title">${slide.name}</h5>
									<p class="card-text">${slide.description}</p>
								</a>
							</div>
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>


