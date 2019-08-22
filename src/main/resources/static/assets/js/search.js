$(function () {
    $('#search-btn').bind('click', function () {
        var keyword = $('#search-keyword').val();
         url = getLocalhostPath() + 'index/search/' + keyword;
        window.open(url);
    });

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
});
