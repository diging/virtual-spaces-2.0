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

#dismiss {
	width: 35px;
	height: 35px;
	position: absolute;
	/* top right corner of the sidebar */
	top: 10px;
	right: 10px;
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

.scroll {
	overflow-x: hidden;
	overflow-x: auto;
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
	function openNav() {
		document.getElementById("mySidenav").style.width = "250px";
	}

	function closeNav() {
		document.getElementById("mySidenav").style.width = "0";
	}
	/* $(document).ready(function(){
		$(".Back_to_first_Class").on("click", function(e){
			$('#slidesList').first().children().first().children()[0].click();
		});
	}) */
	$(document).ready(function () {
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
				<div class="Component_19___6 Component_19___6_Class">
					<svg class="Ellipse_2">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_2_Class" rx="22"
							ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
					<svg class="Icon_material_slideshow" viewBox="4.5 4.5 24 24">
			<path fill="rgba(150,45,62,1)" class="Icon_material_slideshow_Class"
							d="M 13.83333206176758 11.16666698455811 L 13.83333206176758 21.83333206176758 L 20.49999809265137 16.49999809265137 L 13.83333206176758 11.16666698455811 Z M 25.83333206176758 4.499999523162842 L 7.166666507720947 4.499999523162842 C 5.69999885559082 4.499999523162842 4.499999523162842 5.69999885559082 4.499999523162842 7.166666507720947 L 4.499999523162842 25.83333206176758 C 4.499999523162842 27.29999923706055 5.69999885559082 28.49999809265137 7.166666507720947 28.49999809265137 L 25.83333206176758 28.49999809265137 C 27.29999923706055 28.49999809265137 28.49999809265137 27.29999923706055 28.49999809265137 25.83333206176758 L 28.49999809265137 7.166666507720947 C 28.49999809265137 5.69999885559082 27.29999923706055 4.499999523162842 25.83333206176758 4.499999523162842 Z M 25.83333206176758 25.83333206176758 L 7.166666507720947 25.83333206176758 L 7.166666507720947 7.166666507720947 L 25.83333206176758 7.166666507720947 L 25.83333206176758 25.83333206176758 Z">
			</path>
		</svg>
				</div>
				<div class="Group_7_Class">
					<c:if test="${prevSlide !=  ''}">
						<a
							href="/vspace/exhibit/module/${module.id}/sequence/${currentSequenceId}/slide/${prevSlide}/slideShow">
							<div class="Slideshow_previous Slideshow_previous_Class">
								<svg class="Ellipse_11">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_11_Class" rx="36"
										ry="36" cx="36" cy="36">
				</ellipse>
			</svg>
								<svg class="Icon_ionic_ios_arrow_back"
									viewBox="11.251 6.194 20.021 35.021">
				<path fill="rgba(101,101,101,1)"
										class="Icon_ionic_ios_arrow_back_Class"
										d="M 17.28610038757324 23.69940376281738 L 30.53693389892578 10.45899486541748 C 31.51693534851074 9.478996276855469 31.51693534851074 7.894316673278809 30.53693389892578 6.92474365234375 C 29.55693244934082 5.944746017456055 27.97225761413574 5.95517110824585 26.99225997924805 6.92474365234375 L 11.9795093536377 21.92706680297852 C 11.03078651428223 22.87578582763672 11.00993728637695 24.39791297912598 11.90652942657471 25.3779125213623 L 26.98183441162109 40.48448944091797 C 27.47183418273926 40.97449111938477 28.11821746826172 41.21427917480469 28.75417137145996 41.21427917480469 C 29.39012908935547 41.21427917480469 30.0365104675293 40.97449111938477 30.52651405334473 40.48448944091797 C 31.50650978088379 39.50448989868164 31.50650978088379 37.91981506347656 30.52651405334473 36.95023727416992 L 17.28610038757324 23.69940376281738 Z">
				</path>
			</svg>
							</div>
						</a>
					</c:if>
					<c:if test="${nextSlide !=  ''}">
						<a
							href="/vspace/exhibit/module/${module.id}/sequence/${currentSequenceId}/slide/${nextSlide}/slideShow">
							<div class="slideshow_next slideshow_next_Class">
								<svg class="Ellipse_12">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_12_Class" rx="36"
										ry="36" cx="36" cy="36">
				</ellipse>
			</svg>
								<svg class="Icon_ionic_ios_arrow_forward"
									viewBox="11.246 6.196 20.021 35.017">
				<path fill="rgba(101,101,101,1)"
										class="Icon_ionic_ios_arrow_forward_Class"
										d="M 25.23231887817383 23.69813346862793 L 11.98148345947266 10.45771980285645 C 11.00148582458496 9.47772216796875 11.00148582458496 7.893041610717773 11.98148345947266 6.923468589782715 C 12.96148300170898 5.953895568847656 14.54616165161133 5.953895092010498 15.52616119384766 6.923468589782715 L 30.53891181945801 21.92579460144043 C 31.48763275146484 22.8745174407959 31.50848388671875 24.39664649963379 30.61189270019531 25.37664222717285 L 15.53658676147461 40.48322677612305 C 15.04658699035645 40.97322463989258 14.40020561218262 41.2130126953125 13.76424789428711 41.2130126953125 C 13.1282901763916 41.2130126953125 12.48190879821777 40.97322463989258 11.99190902709961 40.48322677612305 C 11.0119104385376 39.50322723388672 11.0119104385376 37.91854858398438 11.99190902709961 36.948974609375 L 25.23231887817383 23.69813346862793 Z">
				</path>
			</svg>
							</div>
						</a>
					</c:if>
				</div>

				<a
					href="/vspace/exhibit/module/${module.id}/sequence/${startSequenceId}/slide/${firstSlide}">
					<div class="Back_to_first Back_to_first_Class">
						<svg class="Ellipse_5">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_5_Class" rx="22"
								ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
						<svg class="Icon_material_first_page" viewBox="9 9 18 17.405">
			<path fill="rgba(150,45,62,1)" class="Icon_material_first_page_Class"
								d="M 27 24.36019325256348 L 20.34246635437012 17.70265769958496 L 27 11.04512405395508 L 24.95487976074219 9 L 16.25221633911133 17.70265769958496 L 24.95487976074219 26.40531730651855 L 27 24.36019325256348 Z M 9.000000953674316 9 L 11.90088748931885 9 L 11.90088748931885 26.40531730651855 L 9.000000953674316 26.40531730651855 L 9.000000953674316 9 Z">
			</path>
		</svg>
					</div>
				</a>

				<div class="Slide_1_14_Class">
					<span>Slide ${currentNumOfSlides}/${numOfSlides}</span>
				</div>
				<div class="Map Map_Class">
					<svg class="Ellipse_6">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_6_Class" rx="22"
							ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
					<div class="Icon_awesome_map Icon_awesome_map_Class">
						<svg class="Icon_awesome_map_bg" viewBox="0 2.25 24 18.667">
				<path fill="rgba(150,45,62,1)" class="Icon_awesome_map_bg_Class"
								d="M 0 5.819166660308838 L 0 20.24916839599609 C 0 20.72083282470703 0.476250022649765 21.04333114624023 0.9141667485237122 20.86833572387695 L 6.666666984558105 18.25 L 6.666666984558105 2.25 L 0.8383333683013916 4.581250190734863 C 0.3321118056774139 4.783724784851074 0.0001215051670442335 5.273954391479492 1.412850849646929e-07 5.819165706634521 Z M 8 18.25 L 16 20.91666793823242 L 16 4.916666984558105 L 8 2.25 L 8 18.25 Z M 23.08583641052246 2.298333644866943 L 17.33333587646484 4.916666984558105 L 17.33333587646484 20.91666793823242 L 23.16166496276855 18.58541870117188 C 23.66796493530273 18.38303375244141 23.99999618530273 17.89274597167969 24 17.34749984741211 L 24 2.917500257492065 C 24 2.445833444595337 23.52375030517578 2.12333345413208 23.08583641052246 2.298333644866943 Z">
				</path>
			</svg>
					</div>
				</div>
				<div class="_DigInG_Class">
					<span>©DigInG</span>
				</div>
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
				<div onclick="application.goToTargetView(event)"
					class="exit_to_space exit_to_space_Class">
					<svg class="Ellipse_5_bp">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_5_bp_Class"
							rx="22" ry="22" cx="22" cy="22">
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