<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="t"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link
	href="<c:url value="/resources/bootstrap-4.3.1-dist/css/bootstrap.min.css" />"
	rel="stylesheet">
<script src="<c:url value="/resources/jquery/jquery-3.3.1.min.js" />"></script>
<script src="<c:url value="/resources/jquery/jquery-ui.min.js" />"></script>

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

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
	console.log("In document ready");
 $('#browse').hide();
 
 $("#toggle").click().toggle(function() {
	 console.log("In toggle ready");
		$('#browse').animate({
			width: 'show',
			opacity: 'show'
		}, 'slow');
	}, function() {
		$('#browse').animate({
			width: 'hide',
			opacity: 'hide'
		}, 'fast');
	});
 
 
 $(".sequence").click().toggle(function() {
	 console.log("In toggle sequence ready");
		$('#selectedSequence').animate({
			width: 'show',
			opacity: 'show'
		}, 'slow');
	}, function() {
		$('#selectedSequence').animate({
			width: 'hide',
			opacity: 'hide'
		}, 'fast');
	});
 
 
 $(".sequence").on("click", function(e) {
		var sequenceId = this.id;
		console.log("Sequence id is :- "+sequenceId);
		$('#selectedSequence').empty();
		$.ajax({
			type: "GET",
			url: "<c:url value="/staff/module/${module.id}/sequence/"/>" +sequenceId+  "/slides?${_csrf.parameterName}=${_csrf.token}",
			async: false,
			success: function(response) {
				console.log("response :- "+response);
				$.each(response, function (index, slide) {
					console.log("Slide ID is :- "+slide.id);
					$('#selectedSequence').append(''+
							'<div class="card sequence" style="max-width: 18rem; margin-bottom:10px;">'+
							'<div align="left" class="card-body">'+
							'<a href="<c:url value="/staff/module/${module.id}/slide/"/>'+slide.id+'" >'+
							'<h5 class="card-title">'+slide.name+'</h5></a>'+
							'</div></div>');
					});
				}
		});
	});
 
});
</script>

<div id="show">
	<a href="#" id="toggle" title="ein-/ausblenden">Module Menu</a>
</div>

<div id="browse"
	style="display: none; position: absolute; right: 0; width: 300px; height: 1000px; z-index: 10">
	<c:forEach items="${sequences}" var="sequences">
		<div class="accordion" id="myAccordion">
			<div class="card">
				<div class="card-header" id="headingOne">
					<h2 class="mb-0">
						<button type="button" class="btn btn-link" data-toggle="collapse"
							data-target="#collapseOne">${sequences.name}</button>
					</h2>
				</div>
				<div id="${sequences.id}" class="collapse show"
					aria-labelledby="${sequences.id}" data-parent="${sequences.id}">
				</div>
			</div>
			<c:forEach items="${moduleSlides}" var="slides">
				<div class="card">
				<div class="card-header" id="headingOne">
					<h2 class="mb-0">
						<button type="button" class="btn btn-link" data-toggle="collapse"
							data-target="#collapseOne">${slides.name}</button>
					</h2>
				</div>
				<div id="${slides.id}" class="collapse show"
					aria-labelledby="${slides.id}" data-parent="${slides.id}">
				</div>
			</div>
			</c:forEach>
		</div>
		
	</c:forEach>
</div>



<div class="container">
	<div class="row">
		<div class="col-sm">
			<button type="button" class="btn btn-secondary" data-container="body"
				data-toggle="popover" data-placement="bottom"
				data-content="Write this data">Module Menu</button>
			<h3>Description: ${module.description}</h3>
			<div id="message" style="text-align: center; color: #008000"></div>
			<div class="alert alert-light" role="alert">
				Created on <span class="date">${module.creationDate}</span> by
				${module.createdBy}. <br> Modified on <span class="date">${module.modificationDate}</span>
				by ${module.modifiedBy}.
			</div>

			<div id="slideSpace">
				<c:forEach items="${startSlideContents}" var="contents">
					<c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
						<div style="margin: 1%;" class="valueDiv" id="${contents.id}">
							<img id="${contents.id}" class="imgDiv" style="margin: 1%;"
								src="<c:url value="/api/image/${contents.image.id}" />" />
						</div>
					</c:if>
					<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
						<div id="${contents.id}" class="textDiv card card-body row"
							style="margin: 10px;">
							<p>${contents.text}</p>
						</div>
					</c:if>
				</c:forEach>
			</div>

			<div class="accordion" id="myAccordion">
				<div class="card">
					<div class="card-header" id="headingOne">
						<h2 class="mb-0">
							<button type="button" class="btn btn-link" data-toggle="collapse"
								data-target="#collapseOne">${slide.id}</button>
						</h2>
					</div>
					<div id="collapseOne" class="collapse show"
						aria-labelledby="headingOne" data-parent="#myAccordion">
						<div class="card-body">
							<div id="slideSpace">
								<c:forEach items="${slideContents}" var="contents">
									<c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
										<div style="margin: 1%;" class="valueDiv" id="${contents.id}">
											<img id="${contents.id}" class="imgDiv" style="margin: 1%;"
												src="<c:url value="/api/image/${contents.image.id}" />" />
										</div>
									</c:if>
									<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
										<div id="${contents.id}" class="textDiv card card-body row"
											style="margin: 10px;">
											<p>${contents.text}</p>
										</div>
									</c:if>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
				<div class="card">
					<div class="card-header" id="headingTwo">
						<h2 class="mb-0">
							<button type="button" class="btn btn-link collapsed"
								data-toggle="collapse" data-target="#collapseTwo">2.
								What is Bootstrap?</button>
						</h2>
					</div>
					<div id="collapseTwo" class="collapse" aria-labelledby="headingTwo"
						data-parent="#myAccordion">
						<div class="card-body">
							<p>
								Bootstrap is a sleek, intuitive, and powerful front-end
								framework for faster and easier web development. It is a
								collection of CSS and HTML conventions. <a
									href="https://www.tutorialrepublic.com/twitter-bootstrap-tutorial/"
									target="_blank">Learn more.</a>
							</p>
						</div>
					</div>
				</div>
				<div class="card">
					<div class="card-header" id="headingThree">
						<h2 class="mb-0">
							<button type="button" class="btn btn-link collapsed"
								data-toggle="collapse" data-target="#collapseThree">3.
								What is CSS?</button>
						</h2>
					</div>
					<div id="collapseThree" class="collapse"
						aria-labelledby="headingThree" data-parent="#myAccordion">
						<div class="card-body">
							<p>
								CSS stands for Cascading Style Sheet. CSS allows you to specify
								various style properties for a given HTML element such as
								colors, backgrounds, fonts etc. <a
									href="https://www.tutorialrepublic.com/css-tutorial/"
									target="_blank">Learn more.</a>
							</p>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>