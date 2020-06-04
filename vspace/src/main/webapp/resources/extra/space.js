function checkSpaceLinkPresent(spaceId, baseUrl, params, header ){
    var urlToLoad = baseUrl + "space/list";
    var dataUrl = baseUrl + "space/" +spaceId + params;
    var warningMessage = "Warning! Other spaces have links to this space! Do you still want to delete?";  

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
               $('#warning').text(warningMessage);
               $('#deleteSpace').attr('data-warning', warningMessage);
               $('#confirm-space-delete').modal('show');
            }
            else{
                
                $('#deleteSpace').attr('data-toggle', 'modal');
                $('#deleteSpace').attr('data-target', "#confirm-space-delete");
                $('#deleteSpace').attr('data-url', dataUrl);
                $('#deleteSpace').attr('data-record-id', spaceId);
                $('#deleteSpace').attr('data-call-on-success', urlToLoad);
                $('#deleteSpace').attr('data-call-on-error', urlToLoad);
                $('#deleteSpace').attr('data-warning', '');
                $('#warning').text('');
                $('#confirm-space-delete').modal('show');
              } 
        }
    });
}

function readyDoc(deleteSpace, confirmDelete) {
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