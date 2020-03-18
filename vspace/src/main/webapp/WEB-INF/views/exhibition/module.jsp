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
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
	integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
	crossorigin="anonymous">
	
</script>
<!-- <script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous">
	
</script> -->

<script
	src="<c:url value="/resources/bootstrap-4.3.1-dist/js/bootstrap.min.js" />"></script>

<link id="applicationStylesheet" rel="stylesheet"
	href="<c:url value="/resources/extra/Module_1.css" />">
<script id="applicationScript"
	src="<c:url value="/resources/extra/Module_1.js" />"></script>

<link rel="stylesheet" type="text/css" id="applicationStylesheet"
	href="<c:url value="/resources/extra/Module_1___slideshow___1.css" />">

<script>
	$(window).on('load', function() {
		var divWindow = $(".valueDiv");
		var images = $(".imgDiv");
		resizeImage(images);
		function resizeImage(images) {
			for (var i = 0; i < images.length; i++) {
				if (images[i].width > divWindow.width()) {
					images[i].width = 705;
					images[i].height = 433;
				} else {
					$(".valueDiv").css("width", images[i].width);
					$(".valueDiv").css("height", images[i].height);
				}
			}
		}
	});
	$(document).ready(function() {
		$('#slide_sidebar').removeClass('active');
		$('#slide_sidebarCollapse').on('click', function() {
			$('#slide_sidebar').toggleClass('active');
		});
	});
</script>
<div class="container">
	<div id="Module_1" class="Module_1_Class">
		<div id="wrapper" class="toggled">
			<nav id="slide_sidebar">
				<div class="slide_sidebar-header">
					<h3>
						<a
							href="/vspace/exhibit/module/${module.id}/sequence/${startSequenceId}/slide/${firstSlide}">${module.name}</a>
					</h3>
				</div>
				<ul class="list-unstyled components">
					<li class="active"><c:forEach items="${slides}" var="slides">
							<li><a
								href="/vspace/exhibit/module/${module.id}/sequence/${currentSequenceId}/slide/${slides.id}">${slides.name}</a></li>
						</c:forEach></li>
				</ul>
			</nav>
			<div id="slide_content">
				<nav class="slide_navbar navbar-default">
					<div class="container-fluid">
						<div class="navbar-header">
							<button type="button" id="slide_sidebarCollapse"
								class="btn btn-info">
								<i class="fas fa-align-justify"></i>
							</button>
						</div>
					</div>
				</nav>
				<div class="Group_8_Class scroll">
					<c:forEach items="${currentSlideCon.contents}" var="contents">
						<c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
							<div style="margin: 1%;" class="valueDiv" id="${contents.id}">
								<img id="${contents.id}" class="imgDiv" style="margin: 1%;"
									src="<c:url value="/api/image/${contents.image.id}" />" />
							</div>
						</c:if>
						<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
							<div id="${contents.id}" class="textDiv" style="margin: 10px;">
								<p>${contents.text}</p>
							</div>
						</c:if>
					</c:forEach>
				</div>
				<div class="_DigInG_Class">
					<span>©DigInG</span>
				</div>
				<a
					href="/vspace/exhibit/module/${moduleId}/sequence/${sequenceId}/slide/${firstSlide}/slideShow">
					<div class="SLideshow_option SLideshow_option_Class">
						<svg class="Ellipse_2">
							<ellipse fill="rgba(255,255,255,1)" class="Ellipse_2_Class"
								rx="21.923076629638672" ry="22" cx="21.923076629638672" cy="22">
							</ellipse>
						</svg>
						<svg class="Icon_ionic_md_play_circle" viewBox="3.375 3.375 24 24">
							<path fill="rgba(150,45,62,1)"
								class="Icon_ionic_md_play_circle_Class"
								d="M 15.37499809265137 3.374999761581421 C 8.751922607421875 3.374999761581421 3.374999761581421 8.751922607421875 3.374999761581421 15.37499809265137 C 3.374999761581421 21.99807357788086 8.751922607421875 27.37499809265137 15.37499809265137 27.37499809265137 C 21.99807357788086 27.37499809265137 27.37499809265137 21.99807357788086 27.37499809265137 15.37499809265137 C 27.37499809265137 8.751922607421875 21.99807357788086 3.374999761581421 15.37499809265137 3.374999761581421 Z M 12.97499847412109 20.77499580383301 L 12.97499847412109 9.974998474121094 L 20.17499732971191 15.37499809265137 L 12.97499847412109 20.77499580383301 Z">
							</path>
						</svg>
					</div>
				</a>
				<div class="Text_Version_Class">
					<span>Text Version</span>
				</div>
			</div>
		</div>
	</div>
</div>