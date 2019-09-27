<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
//# sourceURL=click.js
var contentCount = ${fn:length(slideContents)};

function createImageBlock(reader) {
    var imageblock = $('<div style="margin: 1%" class="valueDiv"><img style="margin: 1%;" src="#" /></div>');
    imageblock.find('img').attr('src', reader.result);
    imageblock.find('img').attr('width', '800px');
    return imageblock
}

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
    $(e.target).closest('.valueDiv').addClass("open");
    // if there is a block id we know its text block otherwise its an image block
    if(blockId){
        //remove card border
        $(".open").css('border', 'none');
        // get text from p tag
        var description = $(".open").children("p:first").text();
        // insert text box and buttons
        $('<div class="col-xs-12" id="newTextBlockDiv" ><textarea id="newTextBlock" style="margin-top: 1%;" class="form-control" type="text">'+description+'</textarea></div>').insertBefore( ".open p" );
        $('<div class="col-xs-1" style="margin-top: 1%"><a id="cancelTextBlock" class="btn" href="#"style="float: right;"><i class="fas fa-times"></i></a><a id="submitTextBlock" class="btn" href="#"style="float: right;"><i class="fas fa-check"></i></a></div>').insertAfter( "#newTextBlockDiv" );
        $(".open").children("p:first").remove();
        // unbind events to prevent multiple instances of buttons and constant highlighting
        $('.valueDiv').unbind('mouseenter mouseleave dblclick');
        // remove highlighting if present
        $(this).removeClass("hova");
    } else {
        $("#addImgAlert").show();
    }
}
    
function uploadImage() {
    var file = document.getElementById('file').files[0];
    var reader  = new FileReader();
    var formData = new FormData();
    formData.append('file', file);
    formData.append('contentOrder', contentCount);
    if ($(".open")[0]){
        var imageBlockId = $('.open img').attr('id')
        formData.append('imageBlockId',imageBlockId);
        var url = "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image/" />" + imageBlockId + "?${_csrf.parameterName}=${_csrf.token}";
        reader.onload = function () {
            imageblock = createImageBlock(reader);
            $("#" + imageBlockId).replaceWith(imageblock);
        }
       
    } else {
        var url = "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image?${_csrf.parameterName}=${_csrf.token}" />";
        reader.onload = function () {
            imageblock = createImageBlock(reader);
            $('#slideSpace').append(imageblock);
            $(imageblock[0]).mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
        }
        ++contentCount;
    }
    reader.readAsDataURL(file);
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
            $(".open").removeClass("open");
            var $imgTag = imageblock.find('img[id]');
            if($imgTag.length == 0){
            	var img = imageblock.find('img')
            	img.attr('id', data);
            }
        },
        error: function(data) {
        	$(".open").removeClass("open");
        }
    });
}
    
$(document).ready(function() {
	
	if(${slide['class'].simpleName ==  'BranchingPoint'}) {
		$('#addChoice').show();
		$('#choiceSpace').show();	
	}
			
    //-------- edit description --------
    $("#submitDescription").hide()
    $("#cancelEditDescription").hide()
    var description = $("#description").text()
    $("#editDescription").click(function() {
        $('<textarea id="newDescription" style="margin-top: 1%;" class="form-control" type="text">'+description+'</textarea>').insertBefore( "#description" );
        $("#description").remove()
        $("#editDescription").hide()
        $("#submitDescription").show()
        $("#cancelEditDescription").show()
        
    });
    
    $("#submitDescription").click(function() {
        var formData = new FormData();
        formData.append('description', $("#newDescription").val());
        $.ajax({
        url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/edit/description?${_csrf.parameterName}=${_csrf.token}" />",
        type: 'POST',
        cache       : false,
        contentType : false,
        processData : false,
        data: formData,
        enctype: 'multipart/form-data',
        success: function(data) {
            // replace text box with new description
            $("#submitDescription").hide()
            $("#cancelEditDescription").hide()
            $("#editDescription").show()
            var val = $("#newDescription").val();
            $('<p id="description"style="margin-top: .5rem; margin-bottom: .5rem;">val</p>').insertBefore( "#newDescription" );
            $("#newDescription").remove();
            $("#description").text(val)
        },
        error: function(data) {
            $(".open").removeClass("open");
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
        
      });
    
    $("#cancelEditDescription").click(function(){
        $("#submitDescription").hide()
        $("#editDescription").show()
        $("#cancelEditDescription").hide()
        $('<p id="description" style="margin-top: .5rem; margin-bottom: .5rem;">val</p>').insertBefore( "#newDescription" );
        $("#newDescription").remove()
        $("#description").text(description)
        
    });
    
    //------- edit title --------
    $("#submitTitle").hide();
    $("#cancelEditTitle").hide();
    var getTitleText = $("#title").text().split(": ")[1]
    $("#editTitle").click(function() {
        $('<div class="col-4"><input id="newTitle" class="form-control" type="text"></div>').insertAfter( "#title" );
        $('#title').text('Slide: ')
        $("#newTitle").val(getTitleText)
        $("#editTitle").hide()
        $("#submitTitle").show()
        $("#cancelEditTitle").show()
        
    });
    
    $("#submitTitle").click(function() {
        var formData = new FormData();
        formData.append('title', $("#newTitle").val());
        $.ajax({
            url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/edit/title?${_csrf.parameterName}=${_csrf.token}" />",
            type: 'POST',
            cache       : false,
            contentType : false,
            processData : false,
            data: formData,
            enctype: 'multipart/form-data',
            success: function(data) {
                // replace text box with new description
                $("#submitTitle").hide()
                $("#cancelEditTitle").hide();
                $("#editTitle").show()
                var val = $("#newTitle").val();
                $("#newTitle").closest('div').remove();
                $("#title").text("Silde: " + val)
                
            },
            error: function(data) {
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
      });
   $("#cancelEditTitle").click(function(){
        $("#submitTitle").hide()
        $("#editTitle").show()
        $("#cancelEditTitle").hide()
        $("#newTitle").closest('div').remove();
        $("#title").text("Silde: " + getTitleText)
        
    });
   
   $('.choice_check').each(function() {
	    $(this).addClass('unselected');
	  });
   
   $('.choice_check').on('click', function() {
	    $(this).toggleClass('unselected');
	    $(this).toggleClass('selected');
	    $('.choice_check').not(this).prop('checked', false);
	    $('.choice_check').not(this).removeClass('selected');
	    $('.choice_check').not(this).addClass('unselected');
	  });
   
    $("#addText").click(function() {
        $("#addTextAlert").show();
      });
    
    $("#addImage").click(function() {
        $("#addImgAlert").show();
      });
    
    $("#addChoice").click(function() {
        $("#addChoiceAlert").show();
      });
    
    $("#uploadImage").click(function(e) {
        e.preventDefault();
            $("#addImgAlert").hide();
            uploadImage();
      });
  
    $("#cancelSubmitText").click(function() {
        $("#addTextAlert").hide();	
    });
    
    $("#cancelSubmitChoice").click(function() {
        $("#addChoiceAlert").hide();	
    });
    
    
    $("#cancelImageBtn").click(function() {
        $("#image1").remove();
        $("#addImgAlert").hide();
        $(".open").removeClass("open");
    });
    
    $("#submitText").on("click", function(e) {
        $("#addTextAlert").hide();
        e.preventDefault();
        // ------------- creating text content blocks ------------
        
        var formData = new FormData();
        var text = $("#textBlockText").val()
        formData.append('content', $("#textBlockText").val());
        ++contentCount;
        formData.append('contentOrder', contentCount);
        
        $.ajax({
            url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/textcontent?${_csrf.parameterName}=${_csrf.token}" />",
            type: 'POST',
            cache       : false,
            contentType : false,
            processData : false,
            data: formData,
            enctype: 'multipart/form-data',
            success: function(data) {
                var textblock = $('<div id="'+ data +'" class="valueDiv card card-body row"><p>'+text+'</p></div>');
                $(textblock).css({
                    'margin': "10px"
                });
                $(textblock[0]).mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
                $('#slideSpace').append(textblock);
               
            },
            error: function(data) {
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
        $("#textBlockText").val('')
    });
    
    $("#submitChoices").on("click", function(e) {
        e.preventDefault();
        $("#addChoiceAlert").hide();
        var allchoiceVals = [];
        $('#choiceDiv :checked').each(function() {
        	allchoiceVals.push($(this).attr("id"));
          });

        // ------------- creating choice content blocks ------------
        
        var formData = new FormData();
        formData.append('content', allchoiceVals);
        ++contentCount;
        formData.append('contentOrder', contentCount);
        
        $.ajax({
            url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/choicecontent?${_csrf.parameterName}=${_csrf.token}" />",
            type: 'POST',
            cache       : false,
            contentType : false,
            processData : false,
            data: formData,
            enctype: 'multipart/form-data',
            success: function(data) {
                var choiceblock = $('<div id="'+ data.id +'" class="valueDiv card card-body row">'+
						'<a href="<c:url value="/staff/module/${module.id}/sequence/"/>'+data.id+'" >'+
						'<h5 class="card-title">'+data.name+'</h5>'+
						'</a></div>');            
						
                $(choiceblock).css({
                    'margin': "10px"
                });
                $('#slideSpace').append(choiceblock);  
               
            },
            error: function(data) {
                var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                $('.error').append(alert);
            }
        });
        $('input:checkbox').removeAttr('checked');
    });

    $("#addImgAlert").draggable();
    $("#addTextAlert").draggable();
    $("#addChoiceAlert").draggable();
    
    
    // ------------- edit text block ----------------
    
    
    function closeTextBox() {
        var description = $("#newTextBlock").val()
        // clear text box and buttons
        $(".open").empty()
        $(".open").append('<p>'+description+'</p>');
        // reset border of the card
        $(".open").css('border', '1px solid rgba(0,0,0,.125)');
        //rebind event handlers
        $('.valueDiv').mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
        // remove id from storage so its not there on refresh
        $(".open").removeClass("open");
    }
    
    // must add the event to the document since the buttons are added dynamically
    $(document).on('click','#cancelTextBlock',function(){
    	document.getElementById("newTextBlock").value = document.getElementById("newTextBlock").defaultValue;
    	closeTextBox();
    });

    $('.valueDiv').mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);

    $(document).on('click','#submitTextBlock',function(){
       var formData = new FormData();
       formData.append('textBlockDesc', $("#newTextBlock").val());
       formData.append('textBlockId',  $(this).closest(".open").attr("id"));
       $.ajax({
           url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/text/edit?${_csrf.parameterName}=${_csrf.token}" />",
           type: 'POST',
           cache       : false,
           contentType : false,
           processData : false,
           data: formData,
           enctype: 'multipart/form-data',
           success: function(data) {
               // replace text box with new description
               closeTextBox()
               $(".open").removeClass("open");
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
<div class="error"></div>
<!-- title -->
<div class="row align-items-center">
    <h1 id="title" style="margin-bottom: 0%; margin-left: 1%;">Slide: ${slide.name}</h1>
    <a id="editTitle" class="btn" href="#"
        style="float: left; margin-right: 1%;"
    ><i class="fas fa-edit"></i></a>
    <button id="submitTitle" type="button"
        class="btn btn-primary"
        style="float: left; margin-right: 1%;"
    >Save</button>
    <button id="cancelEditTitle" type="button"
        class="btn btn-primary" style="margin-top: 1%; margin-bottom: 1%; margin-left: .5rem;"
    >Cancel</button>
</div>
<div class="alert alert-light" role="alert">
    Created on <span class="date">${slide.creationDate}</span> by
    ${slide.createdBy}.<br> Modified on <span class="date">${slide.modificationDate}</span>
    by ${slide.modifiedBy}.
</div>
<!-- description -->
<div style="margin-left: .1%;" class="row align-items-center">
    <h5 style="margin-bottom: 0px;">Description:</h5>
    <a id="editDescription" class="btn" href="#"
        style="font-size: .66rem; border-radius: .15rem; padding-top: .5%;"><i class="fas fa-edit"></i></a>
    <p id="description"
        style="margin-top: .5rem; margin-bottom: .5rem;"
    >${slide.description}</p>
    <button id="submitDescription" type="button"
        class="btn btn-primary btn-sm" style="margin-top: 1%; margin-bottom: 1%;"
    >Save</button>
    <button id="cancelEditDescription" type="button"
        class="btn btn-primary btn-sm" style="margin-top: 1%; margin-bottom: 1%; margin-left: 1%;"
    >Cancel</button>
</div>
<!-- choices -->
<div id="choiceSpace" style="margin-left: .1%;display:none;" class="row align-items-center">
	<h5 style="margin-bottom: 0px;">Choices: </h5>
	<c:forEach items="${choices}" var="choice">
		<div style="margin-left: .5rem;" class="p-3 mb-2 bg-light text-dark"><a href="<c:url value="/staff/module/${module.id}/sequence/${sequences.id}" />">${choice.sequence.name}</a></div>
	</c:forEach>
</div>

<nav class="navbar navbar-expand-sm navbar-light bg-light">
    <div class="dropdown">
        <button class="btn btn-primary dropdown-toggle" type="button"
            id="dropdownMenuButton" data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="false" style="float:left;"
        >Add content</button>
         <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <a id="addText" class="dropdown-item" href="#">Add Text</a>
            <a id="addImage" class="dropdown-item" href="#">Add Image</a>          
            <a id="addChoice" class="dropdown-item" href="#" style="display:none;">Add Choice</a>    
        </div>
        <p style="float:right; margin-left: 1rem; margin-top:.5rem;">Double Click on a Block to Edit it<p>
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
                enctype="multipart/form-data" method="post">
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
<div id="addChoiceAlert" class="modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Select from the Choices</h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close"
                >
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form name="choiceForm" id="choiceForm"
                enctype="multipart/form-data" method="post">
                <div id = "choiceDiv" class="modal-body">
                	<c:forEach items="${choices}" var="choice">
	                	<input class="choice_check" id=${choice.sequence.id} type="checkbox" name=${choice.sequence.name} value=${choice.sequence.name} />
						<label for=${choice.sequence.name}>${choice.sequence.name}</label><br/>
                	</c:forEach>                   
				</div>
                <div class="modal-footer">
                    <button id="cancelSubmitChoice" type="reset" class="btn light">Cancel</button>
                    <button type="submit" id="submitChoices" class="btn btn-primary">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="slideSpace">
    <c:forEach items="${slideContents}" var="contents">
        <c:if test="${contents['class'].simpleName ==  'ImageBlock'}">
            <div style="margin: 1%" class="valueDiv">
                <img id="${contents.id}" width="800px"
                    style="margin: 1%;"
                    src="<c:url value="/api/image/${contents.image.id}" />"
                />
            </div>
        </c:if>
        <c:if test="${contents['class'].simpleName ==  'TextBlock'}">
            <div id="${contents.id}" class="valueDiv card card-body row"
                style="margin: 10px;">
                <p>${contents.text}</p>
            </div>
        </c:if>
        <c:if test="${contents['class'].simpleName ==  'ChoiceBlock'}">
            <div id="${contents.sequence.id}" class="card card-body row"
                style="margin: 10px;">
                <a href="<c:url value="/staff/module/${module.id}/sequence/${contents.sequence.id}" />">${contents.sequence.name}</a>
            </div>
        </c:if>
    </c:forEach>
</div>
<style type="text/css">
.hova {
    background-color: #bfb168;
}
</style>
