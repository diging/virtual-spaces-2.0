/**
 * Shared behaviour for the custom space order pages (add and edit).
 */
function getSpaceOrder(container) {
    return $(container).children().map(function() {
        return this.children[0].id;
    }).get();
}

function initSpaceOrderSortable() {
    $("#spaceOrder").val(getSpaceOrder("#sortableSpaces").join(","));
    $("#sortableSpaces").sortable({
        update: function() {
            $("#spaceOrder").val(getSpaceOrder(this).join(","));
        }
    });
}

function initGoBack(url) {
    $("#goBackContainer").click(function() {
        window.location.href = url;
    });
}
