var success = "SUCCESS";

function ShowDiv(s)
{
    if(s>0)
    {
        document.getElementById('ceng').style.display='block';
        document.getElementById('close').style.display='block';
    }else{
        document.getElementById('ceng').style.display='none';
        document.getElementById('close').style.display='none';
    }
}

$(function(){
    loadData();
    bindClick("app-type", "appType");
    bindClick("env-type", "enVType");
    bindClick("sys-type", "sysType");
    $("#loadMore").click(function(){
        $("#start").val(parseInt($("#start").val()) + parseInt($("#rows").val()));
        loadMoreData();
    });
    $("#searchImg").click(loadData);

    // $(".upload-btn").click(uploadData);
});



function loadData(){
    $.ajax({
        type: "POST",
        url: "manage/list",
        data: $("#appInfoForm").serialize(),
        success: function(data){
            if(data.length == 0){
                $("#noData").show();
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
        var appType="ios.ico";
        if(o.appType == "2"){
            appType="android.ico";
        }
        var envType = "生产";
        if(o.envType == "2"){
            envType = "预发布";
        }else if(o.envType == "3"){
            envType = "测试";
        }
        var sysType="乾坤袋";
        if(o.sysType == "2"){
            envType = "鸿坤金服";
        }else if(o.sysType == "3"){
            envType = "财享+";
        }else if(o.sysType == "4"){
            envType = "前生活";
        }
        msg = "<div id=\"" + o.code + "\"class=\"data-div\" ><div class=\"data-info-div\">\n" +
            "            <div class=\"data-img-div\">\n" +
            "                <img src=\"img/default.png\">\n" +
            "            </div>\n" +
            "            <div class=\"data-detail-div\">\n" +
            "                <div style=\"height:28px;\">\n" +
            "                    <img src=\"img/" + appType + "\" style=\"width: 40px;float: left;\"><div class=\"data-label-span\">" + o.label + "</div>\n" +
            "                </div><br/>\n" +
            "                <span class=\"data-detail-label-span\">环境：</span><span class=\"data-detail-span\">" + envType + "</span><br/>\n" +
            "                <span class=\"data-detail-label-span\">系统：</span><span class=\"data-detail-span\">" + sysType + "</span><br/>\n" +
            "                <span class=\"data-detail-label-span\">包名：</span><span class=\"data-detail-span\">" + o.packageName + "</span><br/>\n" +
            "                <span class=\"data-detail-label-span\">版本：</span><span class=\"data-detail-span\">" + o.versionName + "(Build " + o.versionCode + ")</span><br/>\n" +
            "                <span class=\"data-detail-label-span\">时间：</span><span class=\"data-detail-span\">" + o.time + "</span><br/>\n" +
            "            </div>\n" +
            "            <div>\n" +
            "                <div class=\"del data-del-div\" code=\"" + o.code + "\"><img src=\"img/del.ico\" style=\"width: 20px;\"></div>\n" +
            "            </div>\n" +
            "        </div></div><div style=\"height: 10px;\"></div>";
        $("#data").append(msg);

        //绑定删除事件
        $(".del").each(function(){
            $(this).click(function(){
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
    }
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
            console.info($(this).find("div").attr("type"));
            $("#" + id).val($(this).find("div").attr("type"));
        });
    });
}

function uploadData(){
    var file = document.getElementById('file');
    if(file.files[0] == undefined){
        swal({title: "", text: "请选择文件", type: "error", confirmButtonText: "Cool"});
        return;
    }
    var fileName = file.files[0].name;
    if(fileName.indexOf(".ipa") < 0 && fileName.indexOf(".apk") < 0){
        swal({title: "", text: "请选择IOS/Android对应的.ipa/.apk安装包", type: "error", confirmButtonText: "Cool"});
        return;
    }
    var formData = new FormData();
    formData.append("file", file.files[0]);
    formData.append("envType", $("#envTypeSelect").val());
    formData.append("sysType", $("#sysTypeSelect").val());
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
        }else{
            swal({title: "", text: data, type: "error", confirmButtonText: "Cool"});
        }
    }).fail(function(res) {
        console.info(res);
    });
}