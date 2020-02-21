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
			<c:if test="${module.startSequence.id != null}">
				<div id="slideSpace">
					<section>
						<div id="myCarousel" class="carousel slide" data-ride="carousel"
							data-interval="false">
							<!-- Indicators -->
							<!-- Wrapper for slides -->
							<div class="carousel-inner">
								<c:forEach items="${slideContents}" var="contents"
									varStatus="loop">
									<c:if test="${loop.index == 0}">
										<c:out value="${innerContents}"></c:out>
										<div class="carousel-item active">
											<c:forEach items="${contents}" var="innerContents">
												<c:if
													test="${innerContents['class'].simpleName ==  'ImageBlock'}">
													<div class="valueDiv img-responsive center-block"
														id="${innerContents.id}">
														<img id="${innerContents.id}" class="imgDiv"
															style="margin: 1%;"
															src="<c:url value="/api/image/${contents.get(i).image.id}" />" />
													</div>
												</c:if>
												<c:if
													test="${innerContents['class'].simpleName ==  'TextBlock'}">

													<div id="${innerContents.id} "
														class="textDiv img-responsive center-block"
														style="margin: 10px; width: 800px;">
														<p style="margin-right: 50px; text-align: justify;">${innerContents.text}</p>
													</div>
												</c:if>
												<div id=number
													style="position: absolute; bottom: 0; right: 0; margin-right: 10px;">${loop.count}</div>
											</c:forEach>
										</div>
									</c:if>
									<c:if test="${loop.index != 0}">
										<div class="carousel-item">
											<c:forEach items="${contents}" var="innerContents">
												<c:if
													test="${innerContents['class'].simpleName ==  'ImageBlock'}">
													<div class="valueDiv img-responsive center-block"
														id="${innerContents.id}">
														<img id="${innerContents.id}" class="imgDiv"
															style="margin: 1%;"
															src="<c:url value="/api/image/${innerContents.image.id}" />" />
													</div>
												</c:if>
												<c:if
													test="${innerContents['class'].simpleName ==  'TextBlock'}">
													<div id="${innerContents.id}"
														class="textDiv img-responsive center-block"
														style="margin: 10px; width: 800px;">
														<p style="margin-right: 50px; text-align: justify;">${innerContents.text}</p>
													</div>
												</c:if>
												<div id=number
													style="position: absolute; bottom: 0; right: 0; margin-right: 10px;">${loop.count}</div>
											</c:forEach>
										</div>
									</c:if>
								</c:forEach>
							</div>
							<!-- Left and right controls -->
							<a class="left carousel-control" href="#myCarousel" role="button"
								data-slide="prev"> <span
								class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
								<span class="sr-only">Previous</span>
							</a> <a class="right carousel-control" href="#myCarousel"
								role="button" data-slide="next"> <span
								class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
								<span class="sr-only">Next</span>
							</a>
						</div>
					</section>
				</div>
			</c:if>
			<c:if test="${module.startSequence.id == null}">
				<div id="message">
					<p style="color: red;">Sorry, this module has not been
						configured yet.</p>
				</div>
			</c:if>
		</div>
	</div>
</div>