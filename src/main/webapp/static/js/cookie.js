function loginOut() {
    delCookie("userId");
    if(getCookie("userId") == null) {
        $('.login-wrap').eq(0).find('.login-area')
            .find('img').attr('src', "")
            .end().find('.nick').text("")
            .end().find('.loginout').text("");
        $('.login-wrap').eq(0).addClass('hidden');
        $('.login-wrap').eq(1).removeClass('hidden');
    }
}

//存cookie
function setCookie(name,value) {
    let exp = new Date();
    exp.setTime(exp.getTime() + 60 * 60 * 1000);
    document.cookie = name + "=" + escape (value) + ";expires=" + exp.toGMTString() + ";path=/user";//escape编码可存入中文
}

//取cookies
function getCookie(name){
    let arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) return unescape(arr[2]); //unescape解码
    return null;
}

//取cookiesDecode
function getCookieDecode(name){
    let arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) return decodeURIComponent(arr[2]).substr(1, decodeURIComponent(arr[2]).length - 2);
    return null;
}

//删除cookie
function delCookie(name){
    let exp = new Date();
    exp.setTime(exp.getTime() - 1);
    let cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/user";
}

(function () {
    if(getCookie("userId") != null) {
        $('.login-wrap').eq(1).addClass('hidden');
        $('.login-wrap').eq(0).removeClass('hidden');
        $.ajax({
            url: '/user/myMsg',
            data: {
                "id": getCookie("userId")
            },
            type: 'get',
            datatype: 'json',
            success(data) {
                console.log(data);
                $('.login-wrap').eq(0).find('.login-area')
                    .find('img').attr('src', "/user/getPic?path=" + JSON.parse(data.picUrl).realFileName)
                    .end().find('.nick').text('你好' + data.account)
                    .end().find('.loginout').text('[退出登录]')
            }
        })

    }
})();