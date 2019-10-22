<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">
<head>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>

<script>
$(document).ready(function(){
	$("#imageId").select2({
		ajax: {
		    url: function (params) {
		      return '<c:url value="/staff/images/search" />';
		    },
		    dataType: 'json',
		    processResults: function (data) {
		        return {
		          results: data,
		        };
		      },
		},
		templateResult: formatImage,
		templateSelection: formatSelection
    });
    
    function formatImage (image) {
    	if (!image.id) {
    		return image.text;
    	}
    	var tbaseUrl = '<c:url value="/api/image/" />';
    	var $image = $(
    			'<span><img src="' + tbaseUrl + image.id + '" class="img-thumbnail" /> ' + image.text + '</span>'
    	);
    	return $image;
    };
    
    function formatSelection(image) {
    	if (!image.id) {
            return image.text;
        }
        var tbaseUrl = '<c:url value="/api/image/" />';
        var $image = $(
                '<span>' + image.text + '</span>'
        );
        $("#selectedImage").empty();
        $("#selectedImage").append('<img width="300px" src="' + tbaseUrl + image.id + '" class="img-thumbnail" />');
        return $image;
    }

});
</script>
</head>

<body>
<h1>Add new Space</h1>

<c:url value="/staff/space/add" var="postUrl" />
<form:form method="POST" action="${postUrl}?${_csrf.parameterName}=${_csrf.token}" modelAttribute="space" enctype="multipart/form-data">
	<div class="form-group row">
		<label for="name" class="col-md-2 col-form-label">Space Name:</label>
		<form:input type="text" class="form-control col-md-10" id="name"
			path="name" />
	</div>

	<div class="form-group row">
		<label for="description" class="col-md-2 col-form-label">Description:</label>
		<form:textarea class="form-control col-md-10" rows="5" cols="30"
			id="description" path="description" />
	</div>

	<div class="form-group row">
		<label for="image" class="col-md-2 col-form-label">Background image:</label>
		<input type="file" name="file" class="form-control col-md-10" rows="5" cols="30" id="file" />
		
	</div>
    
    <div class="form-group row">
        <label for="description" class="col-md-2 col-form-label"></label>
            <div>
            <select id="imageId" name="imageId" class="form-control-xs target" style="height:50px;width:300px;">
                <option selected value="">Choose from existing</option>
            </select>   
            </div>
            <div>
            <span id="selectedImage" width="300px"></span>
            </div>
    </div>

	<button class="btn btn-primary btn-sm" type="submit" value="submit">Create
		Space</button>
</form:form>
</body>
</html>