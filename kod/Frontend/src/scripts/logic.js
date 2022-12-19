var attr;

function setAttr(str){
    attr = str;
}

function showPlayers(str){
    console.log(attr)
     $.ajax({
        url:"/datatable",
        method: "POST",
        contentType: "application/json",
         data: JSON.stringify({attr: attr, query: str}),
         success: function(result){
              $('#table-update').html(result.html);
        }
    })
}