var testEditor;

function themeSelect(id, themes, lsKey, callback)
{
    var select = $("#" + id);

    for (var i = 0, len = themes.length; i < len; i++)
    {
        var theme = themes[i];
        var selected = (localStorage[lsKey] === theme) ? " selected=\"selected\"" : "";

        select.append("<option value=\"" + theme + "\"" + selected + ">" + theme + "</option>");
    }

    select.bind("change", function () {
        var theme = $(this).val();

        if (theme === "")
        {
            alert("theme == \"\"");
            return false;
        }

        console.log("lsKey =>", lsKey, theme);

        localStorage[lsKey] = theme;
        callback(select, theme);
    });

    return select;
}
function getLocalhostPath() {

    //获取当前网址，如： http://localhost:8080/Tmall/index.jsp 
    var curWwwPath = window.document.location.href;

    //获取主机地址之后的目录如：/Tmall/index.jsp 
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);

    //获取主机地址，如： http://localhost:8080 
    var localhostPath = curWwwPath.substring(0, pos);
    return localhostPath + "/";

    //获取带"/"的项目名，如：/Tmall 
    //var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);     
}


$(function () {
    testEditor = editormd("test-editormd", {
        width: "100%",
        //height: 700,
        //height: 600,
        //autoHeight      : true,
        height:document.body.clientHeight,
        path: getLocalhostPath() + 'assets/plugins/markdown/lib/',
        codeFold: true,
        //syncScrolling : false,
        saveHTMLToTextarea: true, // 保存 HTML 到 Textarea
        searchReplace: true,
        watch: false, // 关闭实时预览
        htmlDecode: "style,script,iframe|on*", // 开启 HTML 标签解析，为了安全性，默认不开启    
        //toolbar  : false,             //关闭工具栏
        emoji: true,
        taskList: true,
        tocm: true, // Using [TOCM]
        tex: true, // 开启科学公式TeX语言支持，默认关闭
        flowChart: true, // 开启流程图支持，默认关闭
        sequenceDiagram: true, // 开启时序/序列图支持，默认关闭,
        //dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为true
        //dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
        //dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为true
        //dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为0.1
        //dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
        placeholder: "",
        theme: (localStorage.theme) ? localStorage.theme : "default",
        previewTheme: (localStorage.previewTheme) ? localStorage.previewTheme : "default",
        editorTheme: (localStorage.editorTheme) ? localStorage.editorTheme : "default",
        readOnly: true,
        onload: function () {
            console.log('onload', this);
            testEditor.previewing();
            //this.fullscreen();
            //console.log('onload', this);
            //this.fullscreen();//全屏
            //this.unwatch();
            //this.watch().fullscreen();
            //this.setMarkdown("#PHP");
            //this.width("100%");
            //this.height("100%");
            //this.resize("100%", 640);

            //自定义快捷键
            var keyMap = {
                "Ctrl-S": function (cm) {

                },
                "Ctrl-A": function (cm) { // default Ctrl-A selectAll
                    cm.execCommand("selectAll");
                }
            };
            var keyMap2 = {
                "Ctrl-T": function (cm) {
                    alert("Ctrl+T");
                }
            };

            this.addKeyMap(keyMap);
            this.addKeyMap(keyMap2);
            this.removeKeyMap(keyMap2);  // remove signle key
        }
    });

    themeSelect("editormd-theme-select", editormd.themes, "theme", function ($this, theme) {
        testEditor.setTheme(theme);
    });

    themeSelect("editor-area-theme-select", editormd.editorThemes, "editorTheme", function ($this, theme) {
        testEditor.setCodeMirrorTheme(theme);
    });

    themeSelect("preview-area-theme-select", editormd.previewThemes, "previewTheme", function ($this, theme) {
        testEditor.setPreviewTheme(theme);
    });

    $("#return-btn").bind("click", function () {
        window.location.href = getLocalhostPath();
    });
});