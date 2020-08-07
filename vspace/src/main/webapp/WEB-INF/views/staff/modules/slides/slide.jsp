<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<link rel="stylesheet"
	href="https://unpkg.com/easymde/dist/easymde.min.css">
<script src="https://unpkg.com/easymde/dist/easymde.min.js"></script>

<script>
//# sourceURL=click.js

var contentCount = ${fn:length(slideContents)};
var markDown = null;
var defaultValue;

function createImageBlock(reader, width) {
    var imageblock = $('<div id="current" style="margin: 1%; border: 1px solid rgba(0, 0, 0, 0.125);" class="valueDiv card-body"><img src="#" style="margin: 1%;"/><input type="hidden" id="deleteImageId" /><a class="btn deleteImage" href="javascript:;" style="float: right;"><i style="color: black;" class="fas fa-trash-alt"></i></a></div>');
    imageblock.find('img').attr('src', reader.result);
    if(width > 800)
    	imageblock.find('img').attr('width', '800px');
    return imageblock;
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
    // get to the nearest divs class attribute
    $(".EasyMDEContainer").remove();
    var divName = $(e.target).closest('div').attr('class');
    //if the div is a text content block 
    if(divName.includes("textDiv")){

    	$(e.target).closest('.textDiv').addClass("open");
        var description = $("div.open p:eq(0)").text();
    	defaultValue = description;

        $("#editTextAlert").show();
        markDown = new EasyMDE({element: $('#editTextBlock')[0]});
        markDown.value(description.trim());
        //remove card border
        $(".open").css('display', 'none');
    } else {
    	
    	// store image ID selected by the user to replace, onDoubleClick
    	var imgID = $(e.target).attr('id'); 
    	if(imgID != ''){
        	$("#uploadImage").data('value', imgID); // sets image ID value
        	$("#addImgAlert").show();
    	}
	
    }
}
    
function uploadImage() {
   	
	var imgID = $("#uploadImage").data('value') // gets image ID
    var file = $('#file')[0].files[0];
	var fileName = file.name;
    var reader  = new FileReader();
    var formData = new FormData();
    formData.append('file', file);
    formData.append('content', file.name);
    formData.append('contentOrder', contentCount);
    var imageblock = "";
 	var oldImageBlock = "";
    
    // Ashmi changes for Story VSPC-64
    
	// checks if image ID is present to replace
    if (imgID != '') {
    	oldImageBlock = $("#" + imgID);
    	var imageBlockId = imgID;
    	formData.append('imageBlockId',imageBlockId);
        var url = "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image/" />" + imageBlockId + "?${_csrf.parameterName}=${_csrf.token}";
        reader.onload = function (theFile) {
	        var image = new Image();
	        image.src = theFile.target.result;
	        image.onload = function () {
	            imageblock = createImageBlock(reader, this.width);
	            $("#" + imageBlockId).replaceWith(imageblock);	
	            $(imageblock[0]).mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
        	};
        }
        // Reset data-attribute to get current selected image ID
        $("#uploadImage").data('value', '');
      }
  	else {
        var url = "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image?${_csrf.parameterName}=${_csrf.token}" />";
        reader.onload = function (theFile) {        	
        	var image = new Image();
            image.src = theFile.target.result;
            image.onload = function() {
            	imageblock = createImageBlock(reader, this.width);  
            	$('#slideSpace').append(imageblock); 
                $(imageblock[0]).mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
            };          
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
           	var img = imageblock.find('img');
           	if(data != ''){
           		img.attr('id', data);
           		$("#current").find('#deleteImageId').val(data);
           		$("#current").attr('id', data);
           	}
           	else{
           		img.attr('id', imgID);
           		$("#current").find('#deleteImageId').val(imgID);
           		$("#current").attr('id', imgID);
           	}
        },
        error: function(data) {
        	if (imgID == '') {
        		$("#current").remove();
        	}
        	else {
        		$("#current").replaceWith(oldImageBlock);
        	}
        	if (data.status == 403) {
        		$("#loginAlert").show();
        	}
        }
    });
    // Reset the image file name 
   	$('#file').val('');
}

$(document).ready(function() {
	if(${slide['class'].simpleName == 'BranchingPoint'}) {
		$('#addChoice').show();
		$('#choiceSpace').show();	
	}			

    //-------- edit contentblock description --------

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
	            $('<p id="description"style="margin-top: .5rem; margin-bottom: .5rem;"></p>').insertBefore( "#newDescription" );
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
        $('<p id="description" style="margin-top: .5rem; margin-bottom: .5rem;"></p>').insertBefore( "#newDescription" );
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
        $("#title").text("Silde: " + getTitleText);
   });
   
    $("#addText").click(function() {
        $(".EasyMDEContainer").remove();
    	markDown = new EasyMDE({element: $('#textBlockText')[0]});
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
    
    $("#addChoice").click(function() {
        $("#addChoiceAlert").show();
    });
    
    $(document).on("click", ".deleteText", function(e) {
		$("#confirmDeleteTextAlert").show();
		var alert = $('<input type="hidden" id="deleteTextId">');
		$(alert).attr( 'value', $(this).siblings('input').val());
		$('.modal-footer').append(alert); 
  	});
	
	$(document).on("click", ".deleteImage", function(e) {
		$("#confirmDeleteImageAlert").show();
		var alert = $('<input type="hidden" id="deleteImageId">');
		$(alert).attr( 'value', $(this).siblings('input').val());
		$('.modal-footer').append(alert); 
  	});
	
	$(document).on("click", ".deleteChoiceBlock", function(e) {
		$("#confirmDeleteChoiceBlockAlert").show();
		var alert = $('<input type="hidden" id="deleteChoiceBlockId">');
		$(alert).attr( 'value', $(this).siblings('input').val());
		$('.modal-footer').append(alert); 
  	});
    
    $("#cancelSubmitText").click(function() {
		$("#addTextAlert").hide();	
	});
	
	$("#cancelDeleteText").click(function() {
		$("#confirmDeleteTextAlert").find('input').remove();
		$("#confirmDeleteImageAlert").find('input').remove();
		$("#confirmDeleteChoiceBlockAlert").find('input').remove();
		$("#confirmDeleteTextAlert").hide();
	});
	
	$("#cancelDeleteImage").click(function() {
		$("#confirmDeleteImageAlert").hide();
	});
	
	$("#cancelDeleteChoiceBlock").click(function() {
	    $("#confirmDeleteChoiceBlockAlert").find('input').remove();
	    $("#confirmDeleteTextAlert").find('input').remove();
		$("#confirmDeleteImageAlert").find('input').remove();
		$("#confirmDeleteChoiceBlockAlert").hide();
	});
	
	$("#cancelDelete").click(function() {
		$("#confirmDeleteTextAlert").find('input').remove();
		$("#confirmDeleteImageAlert").find('input').remove();
		$("#confirmDeleteChoiceBlockAlert").find('input').remove();
		$("#confirmDeleteImageAlert").hide();	
	});
	
    	// ---------- Delete Text Block -------------
	
		$("#deleteText").on("click", function(e) {
		e.preventDefault();
		$("#confirmDeleteImageAlert").find('input').remove();
		$("#confirmDeleteChoiceBlockAlert").find('input').remove();
		$("#confirmDeleteTextAlert").hide();
		var blockId = $('#deleteTextId').attr('value');
		$('#deleteTextId').remove()
		
		// ------------- delete text content blocks ------------
		$.ajax({
		    url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/text/" />" + blockId + "?${_csrf.parameterName}=${_csrf.token}",
		    type: 'DELETE',
		    success: function(result) {
		        $('#' + blockId).remove();
		        
		    },
			error: function(data) {
				var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
				$('.error').append(alert); 
				$(".error").delay(4000).slideUp(500, function(){
				    $(".error").empty();
				});
			}
		});
	});
	
	// ---------- Delete Image Block -----------
	
	$("#deleteImage").on("click", function(e) {
		e.preventDefault();
		$("#confirmDeleteTextAlert").find('input').remove();
		$("#confirmDeleteChoiceBlockAlert").find('input').remove();
		$("#confirmDeleteImageAlert").hide();
		var blockId = $('#deleteImageId').attr('value');
		if(blockId == null || blockId == ''){
			var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
			$('.error').append(alert); 
			$(".error").delay(4000).slideUp(500, function(){
			    $(".error").empty();
			});
		} else{
			$('#deleteImageId').remove()
			// ------------- delete image content blocks ------------
			$.ajax({
			    url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/image/" />" + blockId + "?${_csrf.parameterName}=${_csrf.token}",
			    type: 'DELETE',
			    success: function(result) {
			    	$('#' + blockId).remove();
			    },
				error: function(data) {
					var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
					$('.error').append(alert); 
					$(".error").delay(4000).slideUp(500, function(){
					    $(".error").empty();
					});
				}
			});
		}
	});
	
	$("#deleteChoiceBlock").on("click", function(e) {
	    e.preventDefault();
	    $("#confirmDeleteTextAlert").find('input').remove();
	    $("#confirmDeleteImageAlert").find('input').remove();
		$("#confirmDeleteChoiceBlockAlert").hide();
		var blockId = $('#deleteChoiceBlockId').attr('value');
		if(blockId == null || blockId == ''){
			var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
			$('.error').append(alert); 
			$(".error").delay(4000).slideUp(500, function(){
			    $(".error").empty();
			});
		} else{
			$('#deleteChoiceBlockId').remove()
			// ------------- delete choices content blocks ------------
			$.ajax({
			    url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/choiceBlock/" />" + blockId + "?${_csrf.parameterName}=${_csrf.token}",
			    type: 'DELETE',
			    success: function(result) {
			    	$('#' + blockId).remove();
			    },
				error: function(data) {
					var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try to delete again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
					$('.error').append(alert); 
					$(".error").delay(4000).slideUp(500, function(){
					    $(".error").empty();
					});
				}
			});
		}
	});
	
					
    
    $("#confirmDeleteTextAlert").draggable();
	$("#confirmDeleteImageAlert").draggable();
	$("#confirmDeleteChoiceBlockAlert").draggable();
    
	$("#cancelSubmitChoice").click(function() {
        $("#addChoiceAlert").hide();	
    });
	
    $("#cancelImageBtn").click(function() {
    	// Initialize selected image ID to blank, on clicking cancel button
    	$("#uploadImage").data('value', '');
        $("#addImgAlert").hide();
        $(".open").removeClass("open");
    });
    
    $("#submitText").on("click", function(e) {
        $("#addTextAlert").hide();
        e.preventDefault();
        // ------------- creating text content blocks ------------
        
        var formData = new FormData();
        var text = markDown.value();
        formData.append('content', text);
        ++contentCount;
        formData.append('contentOrder', contentCount);
        markDown.value('');
        $.ajax({
            url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/textcontent?${_csrf.parameterName}=${_csrf.token}" />",
            type: 'POST',
            cache       : false,
            contentType : false,
            processData : false,
            data: formData,
            enctype: 'multipart/form-data',
            success: function(data) {

                var textblock = $('<div id="'+ data +'" class="textDiv card card-body row"><p>'+text+'<input type="hidden" id="deleteTextId" value="'+data+'" /><a class="btn deleteText" href="javascript:;" style="float: right;"><i style="color: black;" class="fas fa-trash-alt"></i></a></p></div>');
                $(textblock).css({
                    'margin': "10px"
                });
                $(textblock[0]).mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
                $('#slideSpace').append(textblock);             
            },
            error: function(data) {
            	if (data.status == 403){
            		$("#loginAlert").show();
            	}
            	else {
                	var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                	$('.error').append(alert);
            	}
            }
        });
    });
    
    $('input:checkbox').click(function() {
        if ($(this).is(':checked')) {
        	$('#submitChoices').prop("disabled", false);
        } else {
        if ($('.choiceOptions').filter(':checked').length < 1){
        	$('#submitChoices').attr('disabled',true);}
        }
    });
    
    $("#submitChoices").on("click", function(e) {
        e.preventDefault();
        $("#addChoiceAlert").hide();
        var selectedChoice = [];
        var showsAll = false;
        $('#selectChoices :checked').each(function() {
        	selectedChoice.push($(this).attr("id"));
          });
        // ------------- creating choice content blocks ------------
        if ($('#showAllChoices').is(':checked')) {
            showsAll = true;
        }
        var formData = new FormData();
        formData.append('selectedChoices', selectedChoice);
        ++contentCount;
        formData.append('contentOrder', contentCount);
        formData.append('showsAll', showsAll);
        $.ajax({
            url: "<c:url value="/staff/module/${module.id}/slide/${slide.id}/choice/content?${_csrf.parameterName}=${_csrf.token}" />",
            type: 'POST',
            cache       : false,
            contentType : false,
            processData : false,
            data: formData,
            enctype: 'multipart/form-data',
            success: function(data) {
                var choiceblock = $('<div id="'+ data['choiceBlock'].id +'" class="card card-body row" style="margin: 10px;">');
                if (data['choiceBlock'].showsAll == false){
                    $.each(data['selectedSequences'], function(index, sequence) {
                        choiceblock.append('<a href="<c:url value="/staff/module/${module.id}/sequence/"/>'+sequence.id+'" >'+sequence.name+'</a>');
            	});
                }else{
                	$(function() {
                        var links = $("#choiceSpace a").map(function(e) {
                            text = '<a href='+this.href+'>'+this.name+'</a>';
                            return text;
                        }).get();
                        $(links).each(function(index, choice) {
                            choiceblock.append(choice);
                        });
                   });
                }
                choiceblock.append('<input type="hidden" id="deleteChoiceBlockId" value="'+ data['choiceBlock'].id +'"><a class="btn deleteChoiceBlock" href="javascript:;" style="float: right; position: absolute; right: 20px; top: 0;"><i style="color: black;" class="fas fa-trash-alt"></i></a></div>')
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
        $('#choiceForm')[0].reset();
    });

    $("#addImgAlert").draggable();

    // ------------- edit text block ----------------
    
    $("#addChoiceAlert").draggable();
    
    function updateTextBox(blockId, text) {
        // clear text box and buttons
        $(".open").empty();
        $(".open").append('<p>'+text+'<input type="hidden" id="deleteTextId" value="'+blockId+'" /><a class="btn deleteText" href="javascript:;" style="float: right;"><i style="color: black; float: right;" class="fas fa-trash-alt"></i></a></p>');
        // reset border of the card
        $(".open").css('border', '1px solid rgba(0,0,0,.125)');
        //rebind event handlers
        
        $('.textDiv').mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
        $(".open").css('display', 'block');
        // remove id from storage so its not there on refresh
        $(".open").removeClass("open");
    }
    
    // must add the event to the document since the buttons are added dynamically
    $('#cancelTextBlock').on('click',function(){
    	$("#editTextAlert").hide();
    	var blockId = $(this).closest(".open").attr("id");
    	updateTextBox(blockId, defaultValue);
    });

    $('.valueDiv').mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);
    $('.textDiv').mouseenter(onMouseEnter).mouseleave(onMouseLeave).dblclick(onDoubleClick);

    $("#submitTextBlock").on("click", function(e){
    	e.preventDefault();
        // ------------- editting text content blocks ------------
        
        var formData = new FormData();
        var text = markDown.value();
        markDown.value('');
        var formData = new FormData();
        var blockId = $(".open").attr("id");
        formData.append('textBlockDesc', text);
        formData.append('textBlockId', blockId);
    	$("#editTextAlert").hide();
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
               updateTextBox(blockId, text)
               $(".open").removeClass("open");
           },
           error: function(data) {
        		if (data.status == 403) {
           			$("#loginAlert").show();
           		}
           		else {
               		var alert = $('<div class="alert alert-danger alert-dismissible fade show" role="alert"><p>We are sorry but something went wrong. Please try again later.</p><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
               		$('.error').append(alert);
           		}
           }
       });
    });    
});

$(window).on('load', function () {
	var divWindow = $(".valueDiv");
	$(".deleteText").css('float', 'right');
	var images = $(".imgDiv");
	resizeImage(images);
	
	function resizeImage(images) {
		$(".valueDiv").css('border', '1px solid rgba(0,0,0,.125)');
		for(var i =0; i < images.length; i++) {
			if (images[i].width > divWindow.width() || images[i].width >= 800) {
				$(".valueDiv").find("#"+images[i].id).css("width", 800);
				images[i].width = 800;
			} else {
				$(".valueDiv").find("#"+images[i].id).css("width", images[i].width);
			}
		}
	}
});

</script>
<ol class="breadcrumb">
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/dashboard" />">Dashboard</a></li>
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/module/list" />">Modules</a></li>
	<li class="breadcrumb-item"><a
		href="<c:url value="/staff/module/${module.id}" />">${module.name}</a></li>
	<li class="breadcrumb-item active">${slide.name}</li>
</ol>
<div class="error"></div>
<!-- title -->
<div class="row align-items-center">
	<h1 id="title" style="margin-bottom: 0%; margin-left: 1%;">Slide:
		${slide.name}</h1>
        <a href="<c:url value="/staff/module/${module.id}/slide/${slide.id}/edit" />"><span data-feather="edit"></span></a>
</div>
<div class="alert alert-light" role="alert">
	Created on <span class="date">${slide.creationDate}</span> by
	${slide.createdBy}.<br> Modified on <span class="date">${slide.modificationDate}</span>
	by ${slide.modifiedBy}.
</div>
<!-- description -->
<div style="margin-left: .1%;" class="row align-items-center">
	<h5 style="margin-bottom: 0px;">Description:</h5>
	<p id="description" style="margin-top: .5rem; margin-bottom: .5rem;">${slide.description}</p>
</div>
<!-- SlideSequences -->
<div style="margin-left: .1%;" class="row align-items-center">
    <h5 style="margin-bottom: 0px;">
        This slide belongs to:<br>
    </h5>
</div>
<c:forEach items="${slideSequences}" var="slideSequence">
    <div style="margin-left: .1%;" class="row align-items-center">
        <a style="margin-left: .5rem;"
            href="<c:url value="/staff/module/${module.id}/sequence/${slideSequence.id}" />"
            name="${slideSequence.name}">${slideSequence.name}</a><br>

    </div>
</c:forEach>
<div style="margin-top: 1%; margin-bottom: 2%;">
	<a class="btn btn-primary"
		href="<c:url value="/staff/module/${module.id}" />">Go Back</a>
</div>
<!-- choices -->
<div id="choiceSpace" style="margin-left: .1%;display:none;" class="row align-items-center">
	<h5 style="margin-bottom: 0px;">Choices: </h5>
	<c:forEach items="${choices}" var="choice">
		<a style="margin-left: .5rem;" href="<c:url value="/staff/module/${module.id}/sequence/${choice.sequence.id}" />" name="${choice.sequence.name}">${choice.sequence.name}</a>
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
            <a id="addChoice" class="dropdown-item" href="#" style="display:none;">Add Choices (Links to other sequences)</a>    
        </div>
        <p style="float:right; margin-left: 1rem; margin-top:.5rem;">Double Click on a Block to Edit it<p>
    </div>
</nav>

<!-- Delete Text Modal -->
<div id="confirmDeleteTextAlert" class="modal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Confirm Delete</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<h6>Are you sure you want to delete this text block?</h6>
			</div>
			<div class="modal-footer">
				<button id="cancelDeleteText" type="reset" class="btn light">Cancel</button>
				<button type="submit" id="deleteText" class="btn btn-primary">Submit</button>
			</div>
		</div>
	</div>
</div>
<!-- Delete Image Modal -->
<div id="confirmDeleteImageAlert" class="modal" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Confirm Delete</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<h6>Are you sure you want to delete this image block?</h6>
			</div>
			<div class="modal-footer">
				<button id="cancelDelete" type="reset" class="btn light">Cancel</button>
				<button type="submit" id="deleteImage" class="btn btn-primary">Submit</button>
			</div>
		</div>
	</div>
</div>

<!-- Delete ChoiceBlock Modal -->
<div id="confirmDeleteChoiceBlockAlert" class="modal" tabindex="-1"
    role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Delete</h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <h6>Are you sure you want to delete this choices Block?</h6>
            </div>
            <div class="modal-footer">
                <button id="cancelDeleteChoiceBlock" type="reset" class="btn light">Cancel</button>
                <button type="submit" id="deleteChoiceBlock" class="btn btn-primary">Submit</button>
            </div>
        </div>
    </div>
</div>



<div id="addTextAlert" class="modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Add new Text Block</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form name="textForm" id="textUploadForm"
				enctype="multipart/form-data" method="post">
				<div class="modal-body">
					<h6>
						<small>Enter Text: </small>
					</h6>
					<textarea class="form-control" id="textBlockText" rows="3"></textarea>
				</div>
				<div class="modal-footer">
					<button id="cancelSubmitText" type="reset" class="btn light">Cancel</button>
					<button type="submit" id="submitText" class="btn btn-primary">Submit</button>
				</div>
			</form>
		</div>
	</div>
</div>

<div id="editTextAlert" class="modal" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Edit Text Block</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form name="textForm" id="textEditUploadForm"
				enctype="multipart/form-data" method="post">
				<div class="modal-body">
					<h6>
						<small>Edit Text: </small>
					</h6>
					<textarea class="form-control" id="editTextBlock" rows="3"></textarea>
				</div>
				<div class="modal-footer">
					<button id="cancelTextBlock" type="reset" class="btn light">Cancel</button>
					<button type="submit" id="submitTextBlock" class="btn btn-primary">Submit</button>
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
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form name="photoForm" id="imageUploadForm"
				enctype="multipart/form-data" method="post">
				<div class="modal-body">
					<h6>
						<small>Upload Image: </small>
					</h6>
					<input class="form-control" type="file" name="file" rows="5"
						cols="500" id="file" />
				</div>
				<div class="modal-footer">
					<button id="cancelImageBtn" type="reset" class="btn light">Cancel</button>
					<button type="submit" id="uploadImage" class="btn btn-primary"
						data-value="">Upload Image</button>
				</div>
			</form>
		</div>
	</div>
</div>

<div id="loginAlert" class="modal" tabindex="-1" role="dialog"
	backdrop="static"
	style="display: none; background-color: rgba(0, 0, 0, 0.5);">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Login</h5>
			</div>
			<div class="modal-body">
				<h6>You are not logged in, please login.</h6>
			</div>
			<div class="modal-footer">
				<a class="btn btn-primary" style="color: white;"
					onClick="window.location.reload();">Login</a>
			</div>
		</div>
	</div>
</div>

<div id="addChoiceAlert" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Select from the Choices</h5>
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form name="choiceForm" id="choiceForm"
                enctype="multipart/form-data" method="post">
                <div id = "choiceDiv" class="modal-body">
                    <input class="choiceOptions" id="showAllChoices" type="checkbox" name="showAll" value="showAll" checked="true"/>
                    <label for="showAll">Always show all choices</label><br/>
                    <div id = "selectChoices">
                        <c:forEach items="${choices}" var="choice">
                            <input class="choiceOptions" id="${choice.id}" type="checkbox" name="${choice.sequence.name}" value="${choice.sequence.name}" />
                            <label for="${choice.id}">${choice.sequence.name}</label><br/>
                        </c:forEach>
                    </div>                   
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
		<c:if test="${contents['class'].simpleName == 'ImageBlock'}">
			<div style="margin: 1%;" class="valueDiv card-body"
				id="${contents.id}">
				<img id="${contents.id}" class="imgDiv"
					src="<c:url value="/api/image/${contents.image.id}" />" /> <input
					type="hidden" id="deleteImageId" value="${contents.id}"> <a
					class="btn deleteImage" href="javascript:;" style="float: right;">
					<i style="color: black;" class="fas fa-trash-alt"></i>
				</a>
			</div>
		</c:if>
		<c:if test="${contents['class'].simpleName ==  'TextBlock'}">
			<div id="${contents.id}" class="textDiv card card-body row"
				style="margin: 1%;">
				<p>${contents.text}
					<input type="hidden" id="deleteTextId" value="${contents.id}" /> <a
						class="btn deleteText" href="javascript:;" style="float: right;">
						<i style="color: black;" class="fas fa-trash-alt"></i>
					</a>
				</p>
			</div>
		</c:if>
        <c:if test="${contents['class'].simpleName ==  'ChoiceBlock'}">
            <div id="${contents.id}" class="textDiv card card-body row"
                style="margin: 1%;">
                <c:if test="${contents.showsAll eq true}">
                    <c:forEach items="${choices}" var="choice">
                        <a href="<c:url value="/staff/module/${module.id}/sequence/${choice.sequence.id}" />">${choice.sequence.name}</a>
                    </c:forEach>
                </c:if>
                <c:if test="${contents.showsAll eq false}">
                    <c:forEach items="${contents.choices}" var="choice">
                        <a href="<c:url value="/staff/module/${module.id}/sequence/${choice.sequence.id}" />">${choice.sequence.name}</a>
                    </c:forEach>
                </c:if>
                <input type="hidden" id="deleteChoiceBlockId"
                    value="${contents.id}"> <a
                    class="btn deleteChoiceBlock" href="javascript:;"
                    style="float: right; position: absolute; right: 20px; top: 0;">
                    <i style="color: black;" class="fas fa-trash-alt"></i>
                </a>
            </div>
        </c:if>
    </c:forEach>
</div>

<style type="text/css">
.hova {
	background-color: #bfb168;
}
</style>
