var success = "SUCCESS";
$(function(){
    loadData();
    bindClick("app-type", "appType");
    $("#loadMore").click(function(){
        $("#start").val(parseInt($("#start").val()) + parseInt($("#rows").val()));
        loadMoreData();
    });
    $("#searchImg").click(loadData);
});

function loadData(){
    $.ajax({
        type: "POST",
        url: "manage/list",
        data: $("#appInfoForm").serialize(),
        success: function(data){
            $("#data").empty();
            if(data.length == 0){
                $("#data").append("<div class=\"data-no-div\" id=\"noData\"><div>暂无记录</div></div>");
                return;
            }
            renderData(data);
        }
    });
}

function loadMoreData(){
    $.ajax({
        type: "POST",
        url: "manage/list",
        data: $("#appInfoForm").serialize(),
        success: function(data){
            if(data.length == 0){
                $("#noMore").show();
                setTimeout(function(){$("#noMore").hide()}, 2000);
                return;
            }
            renderData(data);
        }
    });
}

/**
 * 渲染数据
 * @param data
 */
function renderData(data){
    $("#noData").hide();
    for(var i in data){
        var o = data[i];
        var appType="ios";
        if(o.appType == "2"){
            appType="android";
        }
        msg = "<div id=\"" + o.code + "\"class=\"data-div\" ><div class=\"data-info-div\">\n" +
            "            <div class=\"data-img-div\">\n" +
            "                <img src=\"apps/" + appType + "/" + o.code + ".png\">\n" +
            "            </div>\n" +
            "            <div class=\"data-detail-div\">\n" +
            "                <div style=\"height:28px;\">\n" +
            "                    <img src=\"img/" + appType + ".ico\" style=\"width: 40px;float: left;\"><div class=\"data-label-span\">" + o.label + "</div>\n" +
            "                </div><br/>\n" +
            "                <span class=\"data-detail-label-span\">包名：</span><span class=\"data-detail-span\">" + o.packageName + "</span><br/>\n" +
            "                <span class=\"data-detail-label-span\">版本：</span><span class=\"data-detail-span\">" + o.versionName + "(Build " + o.versionCode + ")</span><br/>\n" +
            "                <span class=\"data-detail-label-span\">时间：</span><span class=\"data-detail-span\">" + o.time + "</span><br/>\n" +
            "            </div>\n" +
            "            <div>\n" +
            "                <div class=\"del data-del-div\" code=\"" + o.code + "\"><img src=\"img/del.ico\" style=\"width: 20px;\"></div>\n" +
            "            </div>\n" +
            "            <div>\n" +
            "                <div class=\"data-del-div\"><img src=\"apps/" + appType + "/" + o.code + ".jpg\" class='qrCode' style=\"width: 27px;margin: -3px 0 0 0;\"></div>\n" +
            "            </div>\n" +
            "        </div></div><div style=\"height: 10px;\"></div>";
        $("#data").append(msg);

        //绑定删除事件
        $(".del").each(function(){
            $(this).unbind().click(function(){
                var code = $(this).attr("code");
                $.ajax({
                    type: "POST",
                    url: "manage/del",
                    data: {code:code},
                    success: function(data){
                        $("#" + code).remove();
                    }
                });
            });
        });

        //绑定二维码显示事件
        $(".qrCode").each(function(){
            var src = $(this).attr("src");
            var tooltip = $("#tooltip");
            $(this).unbind().mouseover(function(e){
                var position = mousePosition(e);
                var xOffset = 350;
                var yOffset = 21;
                tooltip.css("display","block").css("position","absolute").css("top",(position.y - yOffset) + "px").css("left",(position.x - xOffset) + "px");
                tooltip.append("<img src='" + src + "' style='width:220px;' onerror='this.src=\"img/default.png\"'>");
            });
            $(this).mouseout(function(e){
                tooltip.empty();
                tooltip.css("display","none");
            });
        });
    }
}

//获取鼠标坐标
function mousePosition(ev){
    ev = ev || window.event;
    if(ev.pageX || ev.pageY){
        return {x:ev.pageX, y:ev.pageY};
    }
    return {
        x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
        y:ev.clientY + document.body.scrollTop - document.body.clientTop
    };
}

/**
 * 选择按钮绑定事件
 * @param clzss
 * @param id
 */
function bindClick(clzss, id){
    $("." + clzss).each(function(){
        $(this).click(function(){
            $("." + clzss).each(function(){
                $(this).removeClass("search-select-div");
                $(this).find("div").removeClass("search-select-font-div");
            });
            $(this).addClass("search-select-div");
            $(this).find("div").addClass("search-select-font-div");
            $("#" + id).val($(this).find("div").attr("type"));
            loadData();
        });
    });
}

function uploadData(){
    var file = document.getElementById('file');
    if(file.files[0] == undefined){
        swal({title: "", text: "请选择文件", type: "error", confirmButtonText: "关闭"});
        return;
    }
    var fileName = file.files[0].name;
    if(fileName.indexOf(".ipa") < 0 && fileName.indexOf(".apk") < 0){
        swal({title: "", text: "请选择IOS/Android对应的.ipa/.apk安装包", type: "error", confirmButtonText: "关闭"});
        return;
    }
    var formData = new FormData();
    formData.append("file", file.files[0]);
    $.ajax({
        url: 'manage/upload',
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false
    }).done(function(data) {
        if(data == success){
            $("#uploadDiv").hide();
            loadData();
        }else{
            swal({title: "", text: data, type: "error", confirmButtonText: "关闭"});
        }
    }).fail(function(res) {
        console.info(res);
    });
}