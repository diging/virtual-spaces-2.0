<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script src="<c:url value="/resources/bootpag/js/bootpag.min.js" />"></script>
<script>
  $( document ).ready(function() {
  $('#page-selection').bootpag({
	    total: ${totalPages},
	    page: ${currentPageNumber},
	    maxVisible: 10,
	    leaps: true,
	    firstLastUse: true,
	    first: '←',
	    last: '→',
	    wrapClass: 'pagination',
	    activeClass: 'active',
	    disabledClass: 'disabled',
	    nextClass: 'next',
	    prevClass: 'prev',
	    lastClass: 'last',
	    firstClass: 'first'
	}).on("page", function(event, num){
		window.location.assign("./"+num);
	}); 
  });
  
  function toggleChange(form) {
	  form.submit();
  }
</script>

<h1>Images</h1>
<style>
.pagination>li>a {
	border: 1px solid #009999;
	padding: 5px;
	color: #000;
	font-size: 15px;
}

.pagination>li.active>a {
	background: #009999;
	color: #fff;
	font-size: 15px;
}

.img-thumbnail {
    height:100px;
    width:140px;
}
</style>
<c:choose>
	<c:when test="${totalImageCount gt 0}">
		<div style="padding-bottom: 20px;">This virtual exhibition
			contains the following images.</div>
		<table class="table">
			<thead>
				<tr>
					<th scope="col">Filename</th>
					<th scope="col">Name</th>
					<th scope="col">Created By</th>
					<th scope="col">Created Date</th>
					<th scope="col">Tag</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${images}" var="image">
				
					<tr>
						<th scope="row"><a href="<c:url value="/api/image/${image.id}"/>"><img
							src="<c:url value="/api/image/${image.id}"/>"
							 class="img-thumbnail"> ${image.filename} 
						</a></th>
						<td>${image.name}</td>
						<td>${image.createdBy}</td>
						<td><span class="date">${image.creationDate}</span></td>
						<td><span class="tag">${image.tag}</span> 
						<Form id="changeTagForm" action="/vspace/staff/images/tag/${currentPageNumber}?${_csrf.parameterName}=${_csrf.token}" method="post">
							<input type="hidden" name="imageID" value="${image.id}" id="imageID" />
							<select id="changeTag" name="changeTag" onChange="toggleChange(this.form)" style="width:68px;">
								<option>Assign a tag</option>
								<c:forEach items="${tagList}" var="tag">
									<option value="${tag}">${tag}</option>
								</c:forEach>
							</select>
						</Form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div id="page-selection"></div>
	</c:when>
	<c:otherwise>
		<div style="padding-bottom: 20px;">There are no images in
			Virtual Space.</div>
	</c:otherwise>
</c:choose>