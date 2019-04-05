<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script src="<c:url value="/resources/bootpag/js/bootpag.min.js" />" ></script>
  <script>
  $( document ).ready(function() {
  $('#page-selection').bootpag({
	    total: ${totalpages},
	    page: ${currentpage},
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
    </script>
<h1>Images</h1>
<style>
.pagination>li>a {
	border: 1px solid #009999;
	padding:5px;
	color: #000;
	font-size: 15px;
}

.pagination>li.active>a {
	background: #009999;
	color: #fff;
	font-size: 15px;
}
</style>
<div style="padding-bottom: 20px;">This virtual exhibition
	contains the following images.</div>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Filename</th>
      <th scope="col">Name</th>
      <th scope="col">Created By</th>
      <th scope="col">Created Date</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach items="${images}" var="image">
    <tr>
      <th scope="row"><a href="<c:url value="/api/image/${image.id}" />"> <span data-feather="box"></span> ${image.filename}</a></th>
      <td>${image.name}</td>
      <td>${image.createdBy}</td>
      <td><span class="date">${image.creationDate}</span></td>
    </tr>
	</c:forEach>
  </tbody>
</table>

<div id="page-selection"></div>

