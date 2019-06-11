<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
//# sourceURL=click.js
var contentCount = ${fn:length(slideContents)};
	
function uploadImage() {
	var file = document.getElementById('file').files[0];
	var reader  = new FileReader();
	
	reader.onload = function () {
		var imageblock = $('<img src="#" />');
		imageblock.attr('src', reader.result);
		imageblock.attr('width', '800px');
		$('#slideSpace').append(imageblock);        
	}
	++contentCount;
	reader.readAsDataURL(file);
	
	var file = document.getElementById('file').files[0];
	var formData = new FormData();
	formData.append('file', file);
	formData.append('contentOrder', contentCount);
	if(localStorage.getItem('imgBlockId')) {
		formData.append('imageBlockId', localStorage.getItem('imgBlockId'));
	}
	if (localStorage.getItem('imgBlockId')){
        var url = "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image/edit?${_csrf.parameterName}=${_csrf.token}" />";
  } else {
      var url = "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image?${_csrf.parameterName}=${_csrf.token}" />";
  }
	$.ajax({
		enctype: 'multipart/form-data',
		// ------------- creating image content blocks ------------
		url: url,
		type: 'POST',
		cache       : false,
		contentType : false,
		processData : false,
		data: formData,
		
		success: function(data) {
			// do nothing for now
		},
		error: function(data) {
			console.log(data);
		}
	});
} 
	
$(document).ready(function() {
	localStorage.removeItem('textBlockId')
	localStorage.removeItem('imgBlockId')
	$("#addText").click(function() {
		$("#addTextAlert").show();
  	});
	
	$("#addImage").click(function() {
		$("#addImgAlert").show();
  	});
	
	$("#uploadImage").click(function(e) {
		e.preventDefault();
			$("#addImgAlert").hide();
			uploadImage();
  	});
	
	$("#cancelSubmitText").click(function() {
		$("#addTextAlert").hide();	
	});
	
	$("#cancelImageBtn").click(function() {
		$("#image1").remove();
		$("#addImgAlert").hide();	
	});
	
	$("#submitText").on("click", function(e) {
		$("#addTextAlert").hide();
		e.preventDefault();

		var payload = {};
		payload["content"] = $("#textBlockText").val();
		
		var textblock = $('<div class="card card-body">'+payload["content"]+'</div>');
		$(textblock).css({
			'margin': "10px"
		});
		$('#slideSpace').append(textblock);
		++contentCount;
		payload["contentOrder"] = contentCount;
		// ------------- creating text content blocks ------------
		$.post("<c:url value="/staff/module/${module.id}/slide/${slide.id}/textcontent?${_csrf.parameterName}=${_csrf.token}" />", payload, function(data) {
		});
		
		$("#textBlockText").val('')
	});
	$("#addImgAlert").draggable();
	$("#addTextAlert").draggable();
	
	// ------------- edit text block ----------------
	function onMouseEnter(e){
		var target = $( e.target );
        $(".hova").removeClass("hova");
        $(e.target).addClass("hova");
        // This is needed to prevent only the p tag from being highlighted
        if(target.is('p')){
            $(e.target).parent().addClass("hova");
        }
        //prevent normal hover actions
        e.preventDefault();
    }

    function onMouseLeave(e) {
        $(this).removeClass("hova");
    }

    function onDoubleClick(e){
        // get the id of the nearest div with id valueDiv
        var blockId = $(e.target).closest('div').attr('id');
        // if there is a block id we know its text block otherwise its an image block
        if(blockId){
        	//remove card border
            $("#" + blockId).css('border', 'none');
        	//set id in local storage so it can be retrieved in other functions
            localStorage.setItem('textBlockId', blockId)
            // get text from p tag
            var description = $("#" + blockId + " p").text();
            // insert text box and buttons
            $('<div class="col-xs-12" id="newTextBlockDiv" ><textarea id="newTextBlock" style="margin-top: 1%;" class="form-control" type="text">'+description+'</textarea></div>').insertBefore( "#" + blockId + " p" );
            $('<div class="col-xs-1" style="margin-top: 1%"><a id="cancelTextBlock" class="btn" href="#"style="float: right;"><i class="fas fa-times"></i></a><a id="submitTextBlock" class="btn" href="#"style="float: right;"><i class="fas fa-check"></i></a></div>').insertAfter( "#newTextBlockDiv" );
            $("#" + blockId + " p").remove();
            // unbind events to prevent multiple instances of buttons and constant highlighting
            $('.valueDiv').unbind('mouseenter mouseleave dblclick');
            // remove highlighting if present
            $(this).removeClass("hova");
        } else {
        	// Must use closest('div').children() instead of closet('img').
        	blockId = $(e.target).closest('div').children().attr('id');
        	console.log(blockId);
            $("#addImgAlert").show();
            localStorage.setItem('imgBlockId', blockId);
           
        }
        
        
    }
    
    function closeTextBox() {
        var description = $("#newTextBlock").val()
        var blockId = '#'+localStorage.getItem('textBlockId');
        // clear text box and buttons
        $(blockId).empty()
        $(blockId).append('<p>'+description+'</p>');
        // reset border of the card
        $(blockId).css('border', '1px solid rgba(0,0,0,.125)')
        //rebind event handlers
        $('.valueDiv').mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
        // remove id from storage so its not there on refresh
        localStorage.removeItem('textBlockId')
    }
    // must add the event to the document since the buttons are added dynamically
    $(document).on('click','#cancelTextBlock',function(){
        closeTextBox();
    });

	$('.valueDiv').mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);

    $(document).on('click','#submitTextBlock',function(){
       var formData = new FormData();
       formData.append('textBlockDesc', $("#newTextBlock").val());
       formData.append('textBlockId', localStorage.getItem('textBlockId'));
       $.ajax({
           url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/textcontent/edit?${_csrf.parameterName}=${_csrf.token}" />",
           type: 'POST',
           cache       : false,
           contentType : false,
           processData : false,
           data: formData,
           enctype: 'multipart/form-data',
           success: function(data) {
               // replace text box with new description
               closeTextBox()
           },
           error: function(data) {
               var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
               $('.error').append(alert);
           }
       });
    });
});
</script>
<ol class="breadcrumb">
    <li class="breadcrumb-item"><a
        href="<c:url value="/staff/dashboard" />"
    >Dashboard</a></li>
    <li class="breadcrumb-item"><a
        href="<c:url value="/staff/module/list" />"
    >Modules</a></li>
    <li class="breadcrumb-item"><a
        href="<c:url value="/staff/module/${module.id}" />"
    >${module.name}</a></li>
    <li class="breadcrumb-item active">${slide.name}</li>
</ol>
<h1>Slide: ${slide.name}</h1>
<div class="alert alert-light" role="alert">
    Created on <span class="date">${slide.creationDate}</span> by
    ${slide.createdBy}.<br> Modified on <span class="date">${slide.modificationDate}</span>
    by ${slide.modifiedBy}.
</div>
<h5>Description:</h5>
<p>${slide.description}</p>
<nav class="navbar navbar-expand-sm navbar-light bg-light">
    <div class="dropdown">
        <button class="btn btn-primary dropdown-toggle" type="button"
            id="dropdownMenuButton" data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="false"
        >Add content</button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <a id="addText" class="dropdown-item" href="#">Add Text</a>
            <a id="addImage" class="dropdown-item" href="#">Add
                Image</a>
        </div>
    </div>
</nav>
<div id="addTextAlert" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add new Text Block</h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close"
                >
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form name="textForm" id="textUploadForm"
                enctype="multipart/form-data" method="post"
            >
                <div class="modal-body">
                    <h6>
                        <small>Enter Text: </small>
                    </h6>
                    <textarea class="form-control" id="textBlockText"
                        rows="3"
                    ></textarea>
                </div>
                <div class="modal-footer">
                    <button id="cancelSubmitText" type="reset"
                        class="btn light"
                    >Cancel</button>
                    <button type="submit" id="submitText"
                        class="btn btn-primary"
                    >Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="addImgAlert" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add new Image Block</h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close"
                >
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form name="photoForm" id="imageUploadForm"
                enctype="multipart/form-data" method="post"
            >
                <div class="modal-body">
                    <h6>
                        <small>Upload Image: </small>
                    </h6>
                    <input class="form-control" type="file" name="file"
                        rows="5" cols="500" id="file"
                    />
                </div>
                <div class="modal-footer">
                    <button id="cancelImageBtn" type="reset"
                        class="btn light"
                    >Cancel</button>
                    <button type="submit" id="uploadImage"
                        class="btn btn-primary"
                    >Upload Image</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="slideSpace">
    <c:forEach items="${slideContents}" var="contents">
        <c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
            <div class="valueDiv">
                <img id="${contents.id}" width="800px"
                    style="margin: 10px;"
                    src="<c:url value="/api/image/${contents.image.id}" />"
                />
            </div>
        </c:if>
        <c:if test="${contents['class'].simpleName ==  'TextBlock'}">
            <div id="${contents.id}" class="valueDiv card card-body row" style="margin: 10px;">
                <p>${contents.text}</p>
            </div>
        </c:if>
    </c:forEach>
</div>
<style type="text/css">
    .hova {
    background-color: lightcyan;
    }
</style>