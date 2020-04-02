<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet" type="text/css" id="applicationStylesheet"
	href="<c:url value="/resources/extra/Home.css" />">

<script>
//# sourceURL=click.js
$( document ).ready(function() {
    
	$('#slide_sidebar').removeClass('active');
	$('#slide_sidebarCollapse').on('click', function() {
		$('#slide_sidebar').toggleClass('active');
	});
	
    drawLinks();
    
    $( window ).resize(function() {
        $("#space a").remove();
        drawLinks();
    });
});

function drawLinks() {
    <c:forEach items="${spaceLinks}" var="link" varStatus="loop">
    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', '<c:url value="/exhibit/space/${link.link.targetSpace.id}" />');
        
        if ("${link.type}" == 'ALERT') {
            var linkDisplay = $('<div class="alert alert-primary" role="alert">');
        } else if ("${link.type}" == 'IMAGE' && "${link.image}" != '') {
            var linkDisplay = $('<img id="${link.image.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
        } else {
            var linkDisplay = $('<span data-feather="navigation-2" class="flex"></span><p class="label-${loop.index}" data-link-id="${link.link.id}">${link.link.name}</p>');
        }
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'red'); 
        linkDisplay.css('color', 'red');
        linkDisplay.css('font-size', "12px");	
         
        link.append(linkDisplay);
        $("#space").append(link);
        
        $(".label-${loop.index}").css({
          'transform': 'rotate(0deg)',
          'left': ${link.positionX} + posX - 10,
          'top': ${link.positionY} + posY + 16,
          'color': 'red'
        });	
    }
    </c:forEach>
    
    <c:forEach items="${moduleList}" var="link">
    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', '<c:url value="/exhibit/module/${link.link.module.id}" />');
        
        var linkDisplay = $('<span class="fas fa-book-open"></span>');
     
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('transform', 'rotate(${link.rotation}deg)');
        linkDisplay.css('fill', 'red');
        linkDisplay.css('color', 'red');
        linkDisplay.css('font-size', "15px");
         
        link.append(linkDisplay);
        $("#space").append(link);
    }
    </c:forEach>

    <c:forEach items="${externalLinkList}" var="link">
    {
        var posX = parseInt($("#space").css('margin-left')) + $("#space").position().left; 
        var posY = $("#space").position().top;
        var link = $('<a></a>');
        link.attr('href', "${link.externalLink.externalLink}");
        link.attr('target', "_blank");
        
        var linkDisplay = $('<span class="fa fa-globe"></span>');
       
        if ("${link.type}" == 'IMAGE' && "${link.image}" != '') {
            var linkDisplay = $('<img id="${link.image.id}" src="<c:url value="/api/image/${link.image.id}" />" />');
        } else {
            var linkDisplay = $('<span class="fa fa-globe"></span>');
        }
        
        linkDisplay.css('position', 'absolute');
        linkDisplay.css('left', ${link.positionX} + posX);
        linkDisplay.css('top', ${link.positionY} + posY);
        linkDisplay.css('fill', 'red');
        linkDisplay.css('color', 'red');
        linkDisplay.css('font-size', "15px");
         
        link.append(linkDisplay);
        $("#space").append(link);
    }
    </c:forEach>
    
    feather.replace();
}
</script>
<div class="row">
	<div id="wrapper" class="toggled col-md-1">
		<nav id="slide_sidebar">
			<div class="slide_sidebar-header">
				<h3>MODULES</h3>
			</div>
			<ul class="list-unstyled components">
				<li class="active"><c:forEach items="${moduleList}" var="link">
						<li><a href="/vspace/exhibit/module/${link.link.module.id}">${link.link.module.name}</a></li>
					</c:forEach></li>
			</ul>
		</nav>
		<div id="slide_content">
			<nav class="slide_navbar navbar-default">
				<div class="container-fluid">
					<div class="navbar-header">
						<button type="button" id="slide_sidebarCollapse"
							class="btn">
							<i class="fas fa-align-justify"></i>
						</button>
					</div>
				</div>
			</nav>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-2 Home Home__Class">
		<div>
			${space.name}
			<div class="Group_2_c_Class">
			<svg class="Ellipse_8_da">
				<ellipse fill="rgba(222,222,222,1)" class="Ellipse_8_da_Class" rx="20.5" ry="20.5" cx="20.5" cy="20.5">
				</ellipse>
			</svg>
			<svg class="Ellipse_10_da">
				<ellipse fill="rgba(240,240,240,1)" class="Ellipse_10_da_Class" rx="17.5" ry="17.5" cx="17.5" cy="17.5">
				</ellipse>
			</svg>
			<svg class="Ellipse_9_da">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_9_da_Class" rx="14.5" ry="14.5" cx="14.5" cy="14.5">
				</ellipse>
			</svg>
			<svg class="Icon_awesome_walking_c" viewBox="0.002 0 12.5 20">
				<path fill="rgba(128,128,128,1)" class="Icon_awesome_walking_c_Class" d="M 8.124590873718262 3.749450445175171 C 9.15959358215332 3.749450445175171 9.999313354492188 2.909729480743408 9.999313354492188 1.874725222587585 C 9.999313354492188 0.8397207260131836 9.15959358215332 0 8.124590873718262 0 C 7.089585304260254 0 6.249865531921387 0.8397207260131836 6.249865531921387 1.874725222587585 C 6.249865531921387 2.909729480743408 7.089585304260254 3.749450445175171 8.124590873718262 3.749450445175171 Z M 11.81545448303223 9.57281494140625 L 10.90543174743652 9.111946105957031 L 10.52658081054688 7.963677406311035 C 9.952446937561035 6.221744537353516 8.351119041442871 5.003173351287842 6.534979343414307 4.999267101287842 C 5.12893533706665 4.995361804962158 4.351705551147461 5.393741607666016 2.890982151031494 5.983498573303223 C 2.047355890274048 6.323291778564453 1.356051206588745 6.967730045318604 0.9498608708381653 7.78792142868042 L 0.688180685043335 8.319092750549316 C 0.3835378587245941 8.936191558837891 0.6295955777168274 9.686080932617188 1.242786765098572 9.994629859924316 C 1.852072596549988 10.30317878723145 2.594150543212891 10.05321407318115 2.90269947052002 9.436119079589844 L 3.164379835128784 8.904943466186523 C 3.301078796386719 8.631547927856445 3.527608156204224 8.416736602783203 3.808816909790039 8.303471565246582 L 4.855537891387939 7.88165807723999 L 4.261875152587891 10.25240325927734 C 4.058779716491699 11.06478500366211 4.2774977684021 11.92793941497803 4.843821048736572 12.54894065856934 L 7.183321475982666 15.10325527191162 C 7.464531421661377 15.41180610656738 7.663721084594727 15.78284358978271 7.765267372131348 16.18512725830078 L 8.480005264282227 19.04798889160156 C 8.647951126098633 19.71586036682129 9.327539443969727 20.12595558166504 9.99540901184082 19.95801544189453 C 10.66327953338623 19.79006767272949 11.07337665557861 19.11048316955566 10.90543079376221 18.4426097869873 L 10.03837013244629 14.96655559539795 C 9.936823844909668 14.56427192687988 9.737632751464844 14.18932628631592 9.456423759460449 13.88468170166016 L 7.679341316223145 11.94355964660645 L 8.351118087768555 9.260359764099121 L 8.565930366516113 9.904797554016113 C 8.772932052612305 10.53361129760742 9.218178749084473 11.05306720733643 9.804030418395996 11.34989643096924 L 10.71405124664307 11.81076717376709 C 11.32333564758301 12.11931705474854 12.06541538238525 11.86935329437256 12.37396621704102 11.25225639343262 C 12.67470359802246 10.63906383514404 12.42864513397217 9.881362915039062 11.81545448303223 9.572813987731934 Z M 2.875359773635864 15.06810283660889 C 2.750378370285034 15.38446426391602 2.562905788421631 15.66957473754883 2.320753812789917 15.9078254699707 L 0.3679153323173523 17.86456680297852 C -0.1202943176031113 18.35278129577637 -0.1202943176031113 19.1456356048584 0.3679153323173523 19.63384056091309 C 0.8561248779296875 20.1220531463623 1.645071387290955 20.1220531463623 2.133280754089355 19.63384056091309 L 4.453253746032715 17.31387138366699 C 4.691499710083008 17.07562637329102 4.878972053527832 16.79050827026367 5.007859706878662 16.47414779663086 L 5.535125732421875 15.15402889251709 C 3.375286817550659 12.79890632629395 4.023629665374756 13.52145671844482 3.683835744857788 13.05668163299561 L 2.875360012054443 15.06810474395752 Z">
				</path>
			</svg>
		</div>
		</div>
		
	</div>
</div>
<div class="row">
	<div class="col-md-2 Home__Image__Class">
		<div id="space"
			style="width: ${display.width}px; height: ${display.height}px; min-height: 500px;  margin: auto; background-size: cover; background-image:url('<c:url value="/api/image/${space.image.id}" />')">
		</div>
	</div>
</div>
<script>
$(function() {
    
});
</script>
