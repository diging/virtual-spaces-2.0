<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

    
<h1>Slide: ${slide.name}</h1>
<h3>Description: ${slide.description}</h3>

<div class="alert alert-light" role="alert">
  Created on <span class="date">${slide.creationDate}</span> by ${slide.createdBy}.
  <br>
  Modified on <span class="date">${slide.modificationDate}</span> by ${slide.modifiedBy}.
</div>

<c:if test="${not empty slide.image}">
<div id="slide">
<img id="image" width="800px" src="<c:url value="/api/image/${slide.image.id}" />" />
</div>
</c:if>