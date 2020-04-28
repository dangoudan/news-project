function loginOut() {
    delCookie("userId");
    if(getCookie("userId") == null) {
        $('.quickArea').css('display', 'none');
        $('.btn-login').css('display', 'block');
    }
}

//取cookies
function getCookie(name){
    let arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) return unescape(arr[2]);
    return null;
}
//删除cookie
function delCookie(name){
    let exp = new Date();
    exp.setTime(exp.getTime() - 1);
    let cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString()+";path=/user";
}


(function () {
    if (getCookie("userId") != null) {
        $('.btn-login').css('display', 'none');
        $.ajax({
            url: '/user/myMsg',
            data: {
                "id": getCookie("userId")
            },
            type: 'get',
            datatype: 'json',
            success(data) {
                console.log(data);
                $('.loginWrap')
                    .find('.Face').attr('src', "/user/getPic?path=" + JSON.parse(data.picUrl).realFileName)
                    .end().find('.big-face').attr('src', "/user/getPic?path=" + JSON.parse(data.picUrl).realFileName)
                    .end().find('.nick').text(data.account)
                    .end().find('.loginout').text('[退出]');
            }
        });
    }else {
        $('.quickArea').css('display', 'none');
        $('.btn-login').css('display', 'block');
    }
})();