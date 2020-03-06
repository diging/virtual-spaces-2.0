<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="t"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<link
	href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="<c:url value="/resources/bootstrap-4.3.1-dist/js/bootstrap.min.js" />"></script>

<style type="text/css">
section {
	background-color: #f2f2f2;
	width: 815px;
}
</style>

<script>
	$(window).on('load', function() {
		var divWindow = $(".valueDiv");
		var images = $(".imgDiv");
		resizeImage(images);
		function resizeImage(images) {
			for (var i = 0; i < images.length; i++) {
				if (images[i].width > divWindow.width()) {
					images[i].width = 800;
				} else {
					$(".valueDiv").css("width", images[i].width);
				}
			}
		}
	});
</script>
<div class="container">
	<div class="row">
		<div class="col-sm">
			<h1>Name: ${module.name}</h1>
			<h3>Description: ${module.description}</h3>
			<div class="alert alert-light" role="alert">
				Created on <span class="date">${module.creationDate}</span> by
				${module.createdBy}. <br> Modified on <span class="date">${module.modificationDate}</span>
				by ${module.modifiedBy}.
			</div>
			<c:if test="${error == null}">
				<div id="slides">
					<p>Slides List</p>
					<c:forEach items="${slides}" var="slides">
						<a
							href="/vspace/exhibit/module/${module.id}/sequence/${currentSequenceId}/slide/${slides.id}">${slides.name}<br></a>
					</c:forEach>
				</div>
				<div id="sequences">
					<p>Sequences List</p>
					<c:forEach items="${currentSlideCon.sequence}" var="sequences">
						<a
							href="/vspace/exhibit/module/${module.id}/sequence/${sequences.id}">${sequences.name}<br></a>
					</c:forEach>
				</div>
				<div id="firstSlide"
					style="position: fixed; top: 300px; right: 800px">
					<a
						href="/vspace/exhibit/module/${module.id}/sequence/${startSequenceId}/slide/${firstSlide}">Jump
						to First Slide</a>
				</div>
				<div id="slideSpace">
					<section>
						<c:forEach items="${currentSlideCon.contents}" var="contents">
							<c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
								<div class="valueDiv" id="${contents.id}">
									<img id="${contents.id}" class="imgDiv" style="margin: 1%;"
										src="<c:url value="/api/image/${contents.image.id}" />" />
								</div>
							</c:if>
							<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
								<div id="${contents.id}" class="textDiv"
									style="margin: 10px; width: 800px;">
									<p style="margin-right: 50px; text-align: justify;">${contents.text}</p>
								</div>
							</c:if>
						</c:forEach>
					</section>
				</div>
			</c:if>
			<c:if test="${error != null}">
				<div id="message">
					<p style="color: red;">${error}</p>
				</div>
			</c:if>
		</div>
	</div>
</div>