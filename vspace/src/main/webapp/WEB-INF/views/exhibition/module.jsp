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
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous">
	
</script>

<script
	src="<c:url value="/resources/bootstrap-4.3.1-dist/js/bootstrap.min.js" />"></script>

<link id="applicationStylesheet" rel="stylesheet"
	href="<c:url value="/resources/extra/Module_1.css" />">
<script id="applicationScript"
	src="<c:url value="/resources/extra/Module_1.js" />"></script>

<link rel="stylesheet" type="text/css" id="applicationStylesheet"
	href="<c:url value="/resources/extra/Module_1___slideshow___1.css" />">
<script id="applicationScript"
	src="<c:url value="/resources/extra/Module_1___slideshow___1.js" />"></script>

<style type="text/css">

a, a:hover, a:focus {
	color: inherit;
	text-decoration: none;
	transition: all 0.3s;
}

.scroll {
	overflow-x: hidden;
	overflow-x: auto;
}

#wrapper {
	display: flex;
	width: 100%;
	align-items: stretch;
}

#slide_sidebar {
	min-width: 250px;
	max-width: 250px;
	transition: all 0.3s;
}

#slide_sidebar.active {
	margin-left: -250px;
}

#slide_sidebar .slide_sidebar-header {
	padding: 10px;
}

.slide_sidebar-header h3 a:hover {
	color: #7386D5;
	background: #fff;
}

#slide_sidebar ul li {
	padding-left: 35px;
}

#slide_sidebar ul li a:hover {
	color: #7386D5;
	background: #fff;
}

#slide_content {
	width: 100%;
	padding: 20px;
	min-height: 100vh;
	transition: all 0.3s;
}

@media ( max-width : 768px) {
	#slide_sidebar {
		margin-left: -250px;
	}
	#slide_sidebar.active {
		margin-left: 0;
	}
	#slide_sidebarCollapse span {
		display: none;
	}
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
					images[i].width = 705;
					images[i].height = 433;
				} else {
					$(".valueDiv").css("width", images[i].width);
					$(".valueDiv").css("height", images[i].height);
				}
			}
		}
	});
	$(document).ready(function () {
		$('#slide_sidebar').removeClass('active');
         $('#slide_sidebarCollapse').on('click', function () {
             $('#slide_sidebar').toggleClass('active');
         });
     });
</script>
<div class="container">
	<div id="Module_1" class="Module_1_Class">
		<div id="wrapper" class="toggled">
			<!-- Sidebar Holder -->
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
			<!-- Page Content Holder -->
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
				<div class="Map Map_Class">
					<svg class="Ellipse_6">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_6_Class" rx="22"
							ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
					<div class="Icon_awesome_map Icon_awesome_map_Class">
						<svg class="Icon_awesome_map_" viewBox="0 2.25 24 18.667">
				<path fill="rgba(150,45,62,1)" class="Icon_awesome_map__Class"
								d="M 0 5.819166660308838 L 0 20.24916839599609 C 0 20.72083282470703 0.476250022649765 21.04333114624023 0.9141667485237122 20.86833572387695 L 6.666666984558105 18.25 L 6.666666984558105 2.25 L 0.8383333683013916 4.581250190734863 C 0.3321118056774139 4.783724784851074 0.0001215051670442335 5.273954391479492 1.412850849646929e-07 5.819165706634521 Z M 8 18.25 L 16 20.91666793823242 L 16 4.916666984558105 L 8 2.25 L 8 18.25 Z M 23.08583641052246 2.298333644866943 L 17.33333587646484 4.916666984558105 L 17.33333587646484 20.91666793823242 L 23.16166496276855 18.58541870117188 C 23.66796493530273 18.38303375244141 23.99999618530273 17.89274597167969 24 17.34749984741211 L 24 2.917500257492065 C 24 2.445833444595337 23.52375030517578 2.12333345413208 23.08583641052246 2.298333644866943 Z">
				</path>
			</svg>
					</div>
				</div>
				<div class="_DigInG_Class">
					<span>©DigInG</span>
				</div>
				<a
					href="/vspace/exhibit/module/${moduleId}/sequence/${sequenceId}/slide/${slideId}/slideShow">
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


				<div class="SLideshow_option SLideshow_option_be_Class">
					<svg class="Ellipse_2_bf">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_2_bf_Class"
							rx="22" ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
					<svg class="Icon_ionic_md_play_circle_bg"
						viewBox="3.375 3.375 24 24">
			<path fill="rgba(150,45,62,1)"
							class="Icon_ionic_md_play_circle_bg_Class"
							d="M 15.37499809265137 3.374999761581421 C 8.751922607421875 3.374999761581421 3.374999761581421 8.751922607421875 3.374999761581421 15.37499809265137 C 3.374999761581421 21.99807357788086 8.751922607421875 27.37499809265137 15.37499809265137 27.37499809265137 C 21.99807357788086 27.37499809265137 27.37499809265137 21.99807357788086 27.37499809265137 15.37499809265137 C 27.37499809265137 8.751922607421875 21.99807357788086 3.374999761581421 15.37499809265137 3.374999761581421 Z M 12.97499847412109 20.77499580383301 L 12.97499847412109 9.974998474121094 L 20.17499732971191 15.37499809265137 L 12.97499847412109 20.77499580383301 Z">
			</path>
		</svg>
				</div>
				<div class="SLideshow_option SLideshow_option_bh_Class">
					<svg class="Ellipse_2_bi">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_2_bi_Class"
							rx="22" ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
					<svg class="Icon_ionic_md_play_circle_bj"
						viewBox="3.375 3.375 24 24">
			<path fill="rgba(150,45,62,1)"
							class="Icon_ionic_md_play_circle_bj_Class"
							d="M 15.37499809265137 3.374999761581421 C 8.751922607421875 3.374999761581421 3.374999761581421 8.751922607421875 3.374999761581421 15.37499809265137 C 3.374999761581421 21.99807357788086 8.751922607421875 27.37499809265137 15.37499809265137 27.37499809265137 C 21.99807357788086 27.37499809265137 27.37499809265137 21.99807357788086 27.37499809265137 15.37499809265137 C 27.37499809265137 8.751922607421875 21.99807357788086 3.374999761581421 15.37499809265137 3.374999761581421 Z M 12.97499847412109 20.77499580383301 L 12.97499847412109 9.974998474121094 L 20.17499732971191 15.37499809265137 L 12.97499847412109 20.77499580383301 Z">
			</path>
		</svg>
				</div>
				<div class="Text_Version_Class">
					<span>Text Version</span>
				</div>
				<div class="Movie_Class">
					<span>Movie</span>
				</div>
				<div class="Alternative_Movie_Class">
					<span>Alternative Movie</span>
				</div>
				<div class="exit_to_space exit_to_space_Class">
					<svg class="Ellipse_5">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_5_Class" rx="22"
							ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
					<svg class="Icon_awesome_angle_double_left"
						viewBox="1.734 6.746 24 19.269">
			<path fill="rgba(150,45,62,1)"
							class="Icon_awesome_angle_double_left_Class"
							d="M 13.71564102172852 15.35773658752441 L 21.90250396728516 7.170876979827881 C 22.46835899353027 6.605020999908447 23.38335990905762 6.605020999908447 23.94320678710938 7.170876979827881 L 25.30366897583008 8.531341552734375 C 25.86952209472656 9.097198486328125 25.86952209472656 10.0122013092041 25.30366897583008 10.57204055786133 L 19.50664520263672 16.38109588623047 L 25.30968856811523 22.18413925170898 C 25.87554550170898 22.74999809265137 25.87554550170898 23.66500091552734 25.30968856811523 24.22484016418457 L 23.94922256469727 25.59131622314453 C 23.38335990905762 26.15717315673828 22.46835899353027 26.15717315673828 21.90852546691895 25.59131622314453 L 13.72165966033936 17.40445518493652 C 13.1497859954834 16.83859825134277 13.1497859954834 15.92359733581543 13.71564102172852 15.35773658752441 Z M 2.157721757888794 17.40445518493652 L 10.34458351135254 25.59131622314453 C 10.91043758392334 26.15717315673828 11.825439453125 26.15717315673828 12.38527584075928 25.59131622314453 L 13.7457389831543 24.23085784912109 C 14.31159782409668 23.66500091552734 14.31159782409668 22.74999809265137 13.7457389831543 22.19015884399414 L 7.948722839355469 16.38109588623047 L 13.75175952911377 10.57805633544922 C 14.31761646270752 10.0122013092041 14.31761646270752 9.097198486328125 13.75175952911377 8.537361145019531 L 12.39129734039307 7.170876979827881 C 11.825439453125 6.605020999908447 10.91043758392334 6.605020999908447 10.35060119628906 7.170876979827881 L 2.16374135017395 15.35773658752441 C 1.591865301132202 15.92359733581543 1.591865301132202 16.83859825134277 2.157721757888794 17.40445518493652 Z">
			</path>
		</svg>
				</div>
			</div>
		</div>
	</div>
</div>