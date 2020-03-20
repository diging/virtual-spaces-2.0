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
        link.attr('href', '<c:url value="/exhibit/${link.link.space.id}/module/${link.link.module.id}" />');
        
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
							class="btn btn-info">
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
		<svg class="Ellipse_5">
			<ellipse fill="rgba(255,255,255,1)" class="Ellipse_5_Class" rx="22"
				ry="22" cx="22" cy="22">
			</ellipse>
		</svg>
		<div class="Icon_ionic_md_home Icon_ionic_md_home_Class">
			<svg class="Icon_ionic_md_home_" viewBox="3.375 4.5 24 24">
				<path fill="rgba(150,45,62,1)" class="Icon_ionic_md_home__Class"
					d="M 12.60576820373535 28.5 L 12.60576820373535 20.5 L 18.14423179626465 20.5 L 18.14423179626465 28.5 L 23.77499771118164 28.5 L 23.77499771118164 16.50000190734863 L 27.375 16.50000190734863 L 15.375 4.500000476837158 L 3.375 16.50000190734863 L 6.974999904632568 16.50000190734863 L 6.974999904632568 28.5 L 12.60576820373535 28.5 Z">
				</path>
			</svg>
		</div>
	</div>
	<div class="col-md-2 Map Map_Class">
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
	<div class="col-md-2 Home__Image__Class">
		<div id="space"
			style="width: ${display.width}px; height: ${display.height}px; min-height: 500px;  margin: auto; background-size: cover; background-image:url('<c:url value="/api/image/${space.image.id}" />')">
		</div>
	</div>
</div>
<div class="row">
	<div class="Bottom_nav Bottom_nav_Class">
		<svg class="Rectangle_10">
			<rect fill="rgba(255,255,255,1)" class="Rectangle_10_Class" rx="25"
				ry="25" x="0" y="0" width="590" height="85">
			</rect>
		</svg>
		<div class="Share Share_Class">
			<svg class="Ellipse_7">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_7_Class" rx="22"
					ry="22" cx="22" cy="22">
				</ellipse>
			</svg>
			<div class="Icon_awesome_share_alt Icon_awesome_share_alt_Class">
				<svg class="Icon_awesome_share_alt_be" viewBox="0 0 21 24">
					<path fill="rgba(52,54,66,1)"
						class="Icon_awesome_share_alt_be_Class"
						d="M 16.4999885559082 14.9999885559082 C 15.44023990631104 14.9999885559082 14.46622467041016 15.36650466918945 13.69733333587646 15.97944164276123 L 8.893307685852051 12.97691059112549 C 9.035573959350586 12.33338451385498 9.035573959350586 11.66654872894287 8.893307685852051 11.02302265167236 L 13.69733333587646 8.020493507385254 C 14.46622467041016 8.633479118347168 15.44023990631104 8.999994277954102 16.4999885559082 8.999994277954102 C 18.98525428771973 8.999994277954102 20.9999885559082 6.985260963439941 20.9999885559082 4.499997138977051 C 20.9999885559082 2.014732837677002 18.98525428771973 0 16.4999885559082 0 C 14.014723777771 0 11.99999046325684 2.014733076095581 11.99999046325684 4.499997138977051 C 11.99999046325684 4.835527896881104 12.03702068328857 5.162340641021729 12.1066780090332 5.476916790008545 L 7.302650451660156 8.479448318481445 C 6.533760547637939 7.866508960723877 5.559744834899902 7.499994277954102 4.499997138977051 7.499994277954102 C 2.014733076095581 7.499994277954102 0 9.514728546142578 0 11.99999046325684 C 0 14.48525428771973 2.014733076095581 16.4999885559082 4.499997138977051 16.4999885559082 C 5.559744834899902 16.4999885559082 6.533760547637939 16.13347244262695 7.302650451660156 15.52053737640381 L 12.1066780090332 18.52306747436523 C 12.03566932678223 18.84385681152344 11.99989414215088 19.17143440246582 11.99999046325684 19.49998664855957 C 11.99999046325684 21.98524475097656 14.014723777771 23.99998092651367 16.4999885559082 23.99998092651367 C 18.98525428771973 23.99998092651367 20.9999885559082 21.98524475097656 20.9999885559082 19.4999885559082 C 20.9999885559082 17.01472473144531 18.98525428771973 14.9999885559082 16.4999885559082 14.9999885559082 Z">
					</path>
				</svg>
			</div>
		</div>
		<div class="Full_screen Full_screen_Class">
			<svg class="Ellipse_2">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_2_Class" rx="22"
					ry="22" cx="22" cy="22">
				</ellipse>
			</svg>
			<div
				class="Icon_material_zoom_out_map Icon_material_zoom_out_map_Class">
				<svg class="Icon_material_zoom_out_map_bi" viewBox="4.5 4.5 24 24">
					<path fill="rgba(150,45,62,1)"
						class="Icon_material_zoom_out_map_bi_Class"
						d="M 20.5 4.5 L 23.5666675567627 7.566666126251221 L 19.71333503723145 11.39333248138428 L 21.60666847229004 13.28666687011719 L 25.4333324432373 9.433334350585938 L 28.5 12.5 L 28.5 4.5 L 20.5 4.5 Z M 4.5 12.5 L 7.566666126251221 9.433334350585938 L 11.39333248138428 13.28666687011719 L 13.28666687011719 11.39333343505859 L 9.433334350585938 7.566666126251221 L 12.5 4.5 L 4.5 4.5 L 4.5 12.5 Z M 12.5 28.5 L 9.433334350585938 25.4333324432373 L 13.28666687011719 21.60666465759277 L 11.39333343505859 19.71333122253418 L 7.566666126251221 23.5666675567627 L 4.5 20.5 L 4.5 28.5 L 12.5 28.5 Z M 28.5 20.5 L 25.4333324432373 23.5666675567627 L 21.60666465759277 19.71333503723145 L 19.71333122253418 21.60666847229004 L 23.56666374206543 25.43333625793457 L 20.5 28.5 L 28.5 28.5 L 28.5 20.5 Z">
					</path>
				</svg>
			</div>
		</div>
		<div class="Zoom_out Zoom_out_Class">
			<svg class="Ellipse_3">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_3_Class" rx="22"
					ry="22" cx="22" cy="22">
				</ellipse>
			</svg>
			<svg class="Icon_metro_zoom_out" viewBox="2.571 1.928 24 24">
				<path fill="rgba(150,45,62,1)" class="Icon_metro_zoom_out_Class"
					d="M 25.82685279846191 22.35138893127441 L 20.14203834533691 17.5163688659668 C 19.55436706542969 16.98747825622559 18.92586517333984 16.74466514587402 18.41816329956055 16.76810264587402 C 19.76010131835938 15.19619560241699 20.57071113586426 13.15689754486084 20.57071113586426 10.9280366897583 C 20.57071113586426 5.957456111907959 16.54128646850586 1.928031921386719 11.57070732116699 1.928031921386719 C 6.600172996520996 1.928031921386719 2.570701599121094 5.957456111907959 2.570701599121094 10.9280366897583 C 2.570701599121094 15.89861679077148 6.600126266479492 19.92804145812988 11.57070732116699 19.92804145812988 C 13.79956817626953 19.92804145812988 15.83886623382568 19.117431640625 17.4107723236084 17.77544593811035 C 17.3873348236084 18.28314971923828 17.63014793395996 18.91164970397949 18.15903854370117 19.49932098388672 L 22.99407958984375 25.18413925170898 C 23.82193946838379 26.10396957397461 25.17428207397461 26.1815013885498 25.99923706054688 25.35654640197754 C 26.82418823242188 24.53159141540527 26.7467041015625 23.17924690246582 25.82687759399414 22.35138893127441 Z M 11.57070636749268 16.92804145812988 C 8.257017135620117 16.92804145812988 5.57070255279541 14.24172687530518 5.57070255279541 10.92803859710693 C 5.57070255279541 7.614350318908691 8.257017135620117 4.928035259246826 11.57070636749268 4.928035259246826 C 14.88439559936523 4.928035259246826 17.57070922851562 7.614349365234375 17.57070922851562 10.92803859710693 C 17.57070922851562 14.24172687530518 14.88444328308105 16.92804145812988 11.57070732116699 16.92804145812988 Z M 7.070703506469727 9.428036689758301 L 16.07070922851562 9.428036689758301 L 16.07070922851562 12.42803859710693 L 7.070703506469727 12.42803859710693 L 7.070703506469727 9.428036689758301 Z">
				</path>
			</svg>
		</div>
		<div class="Zoom_in Zoom_in_Class">
			<svg class="Ellipse_4">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_4_Class" rx="22"
					ry="22" cx="22" cy="22">
				</ellipse>
			</svg>
			<svg class="Icon_metro_zoom_in" viewBox="2.571 1.928 24 24">
				<path fill="rgba(150,45,62,1)" class="Icon_metro_zoom_in_Class"
					d="M 25.82685279846191 22.35138893127441 L 20.14203834533691 17.5163688659668 C 19.55436706542969 16.98747825622559 18.92586517333984 16.74466514587402 18.41816329956055 16.76810264587402 C 19.76010131835938 15.19619560241699 20.57071113586426 13.15689754486084 20.57071113586426 10.9280366897583 C 20.57071113586426 5.957456111907959 16.54128646850586 1.928031921386719 11.57070732116699 1.928031921386719 C 6.600172996520996 1.928031921386719 2.570701599121094 5.957456111907959 2.570701599121094 10.9280366897583 C 2.570701599121094 15.89861679077148 6.600126266479492 19.92804145812988 11.57070732116699 19.92804145812988 C 13.79956817626953 19.92804145812988 15.83886623382568 19.117431640625 17.4107723236084 17.77544593811035 C 17.3873348236084 18.28314971923828 17.63014793395996 18.91164970397949 18.15903854370117 19.49932098388672 L 22.99407958984375 25.18413925170898 C 23.82193946838379 26.10396957397461 25.17428207397461 26.1815013885498 25.99923706054688 25.35654640197754 C 26.82418823242188 24.53159141540527 26.7467041015625 23.17924690246582 25.82687759399414 22.35138893127441 Z M 11.57070636749268 16.92804145812988 C 8.257017135620117 16.92804145812988 5.57070255279541 14.24172687530518 5.57070255279541 10.92803859710693 C 5.57070255279541 7.614350318908691 8.257017135620117 4.928035259246826 11.57070636749268 4.928035259246826 C 14.88439559936523 4.928035259246826 17.57070922851562 7.614349365234375 17.57070922851562 10.92803859710693 C 17.57070922851562 14.24172687530518 14.88444328308105 16.92804145812988 11.57070732116699 16.92804145812988 Z M 13.07070636749268 6.428036212921143 L 10.07070541381836 6.428036212921143 L 10.07070541381836 9.428036689758301 L 7.070703506469727 9.428036689758301 L 7.070703506469727 12.42803859710693 L 10.07070541381836 12.42803859710693 L 10.07070541381836 15.42804050445557 L 13.07070636749268 15.42804050445557 L 13.07070636749268 12.42803859710693 L 16.07070922851562 12.42803859710693 L 16.07070922851562 9.428036689758301 L 13.07070636749268 9.428036689758301 L 13.07070636749268 6.428036212921143 Z">
				</path>
			</svg>
		</div>
		<div class="Back Back_Class">
			<svg class="Ellipse_1">
				<ellipse fill="rgba(255,255,255,1)" class="Ellipse_1_Class" rx="22"
					ry="22" cx="22" cy="22">
				</ellipse>
			</svg>
			<div
				class="Icon_ionic_md_arrow_round_back Icon_ionic_md_arrow_round_back_Class">
				<svg class="Icon_ionic_md_arrow_round_back_bs"
					viewBox="5.625 6.33 24 22.639">
					<path fill="rgba(101,101,101,1)"
						class="Icon_ionic_md_arrow_round_back_bs_Class"
						d="M 27.5448112487793 15.46711158752441 L 12.94978809356689 15.46711158752441 L 18.61047744750977 10.05195236206055 C 19.42207145690918 9.19943904876709 19.42207145690918 7.82177734375 18.61047744750977 6.969267845153809 C 17.79888343811035 6.116754531860352 16.48260307312012 6.116754531860352 15.66419124603271 6.969267845153809 L 6.238809585571289 16.10820198059082 C 5.82960319519043 16.50376892089844 5.625 17.04255867004395 5.625 17.63590621948242 L 5.625 17.66318702697754 C 5.625 18.25653839111328 5.82960319519043 18.79532623291016 6.238809585571289 19.19089508056641 L 15.65736865997314 28.329833984375 C 16.47578239440918 29.18234443664551 17.79206466674805 29.18234443664551 18.6036548614502 28.329833984375 C 19.41525077819824 27.47731590270996 19.41525077819824 26.09965515136719 18.6036548614502 25.24714469909668 L 12.94296646118164 19.83198547363281 L 27.53798866271973 19.83198547363281 C 28.69058799743652 19.83198547363281 29.62494087219238 18.85671043395996 29.62494087219238 17.64955139160156 C 29.63176155090332 16.42192649841309 28.69740676879883 15.46711158752441 27.5448112487793 15.46711158752441 Z">
					</path>
				</svg>
			</div>
		</div>
	</div>
</div>
<script>
$(function() {
    
});
</script>
