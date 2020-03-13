<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="<c:url value="/resources/bootpag/js/bootpag.min.js" />"></script>
<script>
//# sourceURL=js.js

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
		window.location.assign("./"+num+"?sort=${sortProperty}&order=${order}");
	});
  
  $("table.table thead th").each(function(){
      var head = $(this);
      if(head.attr('sort-prop')=='${sortProperty}'){
          head.append('${order}'=='desc'?'▾':'▴');
      }
      });

   //set click action, reload page on clicking with all query params
   $("table.table thead th").click(function() {
    var headerSortPropName = $(this).attr("sort-prop");
    if(headerSortPropName != 'tag') {
    	if(headerSortPropName=='${sortProperty}'){
        	window.location.href = window.location.pathname+
        	'?sort='+ headerSortPropName+'&order='+
        	('${order}' == 'desc'?'asc':'desc');
    	}else{
         	window.location.href = window.location.pathname+
       	 	'?sort='+ headerSortPropName+'&order=asc';
    	}
    }
    });
  });
  
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
	height: 100px;
	width: 140px;
}
</style>
<c:choose>
	<c:when test="${totalImageCount gt 0}">
		<div style="padding-bottom: 20px;">This virtual exhibition
			contains the following images.</div>
		<table class="table">
			<thead>
				<tr>
					<th scope="col" sort-prop="filename"><a href="#">Filename</a></th>
					<th scope="col" sort-prop="name"><a href="#">Name</a></th>
					<th scope="col" sort-prop="createdBy"><a href="#">Created
							By</a></th>
					<th scope="col" sort-prop="creationDate"><a href="#">Created
							Date</a></th>
					<th scope="col" sort-prop="tag">Tag</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${images}" var="image">
					<tr>
						<th scope="row"><a
							href="<c:url value="/staff/display/image/${image.id}"/>"><img
								src="<c:url value="/api/image/${image.id}"/>"
								class="img-thumbnail"> ${image.filename} </a></th>
						<td>${image.name}</td>
						<td>${image.createdBy}</td>
						<td><span class="date">${image.creationDate}</span></td>
						<td><span id="tags-${image.id}" class="tag"> <c:forEach
									items="${image.categories}" var="cat">
									<span id="category-badge-${image.id}-${cat}"
										data-category="${cat}" class="badge badge-warning"> <spring:eval
											expression="@configFile.getProperty('image_category_' + cat)" />
										<i data-image-id="${image.id}" data-category="${cat}"
										class="fas fa-times removeTag"></i>
									</span>
								</c:forEach>
						</span>
							<form id="changeTagForm"
								action="<c:url value="/staff/images/" />${image.id}/tag?${_csrf.parameterName}=${_csrf.token}"
								method="post">
								<input type="hidden" name="imageID" value="${image.id}"
									id="imageID" /> <select id="changeTag" name="tag"
									onChange="toggleChange(this.form, '${image.id}')"
									class="form-control form-control-sm" style="width: 68px;">
									<option>Assign a tag</option>
									<c:forEach items="${imageCategories}" var="catTag">
										<option value="${catTag}"><spring:eval
												expression="@configFile.getProperty('image_category_' + catTag)" /></option>
									</c:forEach>
								</select>
							</form></td>
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

<script>
//# sourceURL=removeTag.js

function removeTag(event) {
    var imageId = $(event.target).data("image-id");
    var category = $(event.target).data("category");
    $.ajax({
          url: "<c:url value="/staff/images/" />" + imageId + "/tag?${_csrf.parameterName}=${_csrf.token}&tag=" + category,
          method: "DELETE",
       }).done(function() {
            $("#category-badge-" + imageId + "-" + category).remove();
       });
}

$(".removeTag").click(function(e) {
	removeTag(e);
});

$(".removeTag").css('cursor', 'pointer');

function toggleChange(form, imageId) {
	var catValue = $(form).find("option:selected").val();
    $.post($(form).attr('action') + "&tag=" + catValue, function( data ) {
        var category = $('<span class="badge badge-warning"></span>');
        category.attr('id', 'category-badge-' + imageId + "-" + catValue);
        category.attr("data-category", catValue);
        var catText = $(form).find("option:selected").text();
        category.append(catText + " ");
        var removeTagButton = $('<i data-image-id="' + imageId + '" data-category="' + catValue + '" class="fas fa-times removeTag"></i>');
        removeTagButton.click(removeTag);
        removeTagButton.css('cursor', 'pointer');
        category.append(removeTagButton);
        var isDuplicate = false;
        $("#tags-" + imageId).children().each(function(idx,elem) {
            if ($(elem).data('category') == catValue) {
                isDuplicate = true;
            }
        });
        if (!isDuplicate) {
            $("#tags-" + imageId).append(category);
        }
        
        $(form)[0].reset();
    });
}
</script>