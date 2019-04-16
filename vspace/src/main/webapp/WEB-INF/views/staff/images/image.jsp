<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>



<h1>Image: ${image.filename} <small style="margin-left: 10px;"><a href="<c:url value="/staff/image/${image.id}/edit" />"><span data-feather="edit"></span></a></small></h1> 
 
<div class="alert alert-light" role="alert">
  Created on <span class="date">${image.creationDate}</span> by ${image.createdBy}.
  <br>
  Modified on <span class="date">${image.modificationDate}</span> by ${image.modifiedBy}.     
</div>

<h5>Description:</h5>
<p style="max-height: 100px; overflow-y: scroll;">
${image.description}
</p>

<p></p>

<c:if test="${not empty image}">
<div id="image">
<img id="bgImage" width="800px" src="<c:url value="/api/image/${image.id}" />" />
</div>
</c:if>

