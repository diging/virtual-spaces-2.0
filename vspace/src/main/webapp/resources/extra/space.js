function checkExhibitionStartSpace(baseUrl) {
	return $.ajax({
        url: baseUrl + "exhibit/start",
        type: 'GET',
        cache       : false,
        contentType : false
    });
}


function checkSpaceLinkPresent(spaceId, baseUrl, params, header){
	$('#finalWarning').hide();
	checkExhibitionStartSpace(baseUrl).done(function(value) {
		var obj = JSON.parse(value);
    	if(obj.startSpace == spaceId) {
    		$('#finalWarning').show();
    		$('#exhibitionMessage').show();
    	}
    	else {
    		$('#exhibitionMessage').hide();
    	}
	});
    var urlToLoad = baseUrl + "space/list";
    var dataUrl = baseUrl + "space/" +spaceId + params;

    $.ajax({
        url: baseUrl + "spaceLink/" + spaceId + "/spaces" + params,
        type: 'GET',
        cache       : false,
        contentType : false,
        success: function(data) {
            if(data.length > 0){
               header.data('spaceValue', spaceId);
               $('#deleteSpace').attr('data-toggle', 'modal');
               $('#deleteSpace').attr('data-target', "#confirm-space-delete");
               $('#deleteSpace').attr('data-url', dataUrl);
               $('#deleteSpace').attr('data-record-id', spaceId);
               $('#deleteSpace').attr('data-call-on-success', urlToLoad);
               $('#deleteSpace').attr('data-call-on-error', urlToLoad);
               $('#warningMessage').show();
               $('#finalWarning').show();
            }
            else{
                $('#deleteSpace').attr('data-toggle', 'modal');
                $('#deleteSpace').attr('data-target', "#confirm-space-delete");
                $('#deleteSpace').attr('data-url', dataUrl);
                $('#deleteSpace').attr('data-record-id', spaceId);
                $('#deleteSpace').attr('data-call-on-success', urlToLoad);
                $('#deleteSpace').attr('data-call-on-error', urlToLoad);
                $('#warningMessage').hide();
              }
            $('#confirm-space-delete').modal('show');
        }
    });
}

function onPageReady(deleteSpace, confirmDelete) {
    confirmDelete.on('click', '.btn-ok', function(e) {
        var url = deleteSpace.data('url');
        var urlToLoad = deleteSpace.data('call-on-success');
        $.ajax({
            url : url,
            type : 'DELETE',
            success : function(response) {
                window.location.href = urlToLoad;
                },
            error : function(errorMessage) {
                window.location.href = urlToLoad+"?showAlert=true&alertType=danger&message="+errorMessage.responseText;
                }
            });
  });
}