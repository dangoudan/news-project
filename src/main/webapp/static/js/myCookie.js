function loginOut() {
    delCookie("userId");
    if(getCookie("userId") == null) {
        location.href = '/user/toNewsList'
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

$.ajax({
    url: '/user/myMsg',
    data: {
        "id": getCookie("userId")
    },
    type: 'get',
    datatype: 'json',
    success(data) {
        console.log(data)
        $('.loginWrap')
            .find('.Face').attr('src', "/user/getPic?path=" + JSON.parse(data.picUrl).realFileName)
            .end().find('.big-face').attr('src', "/user/getPic?path=" + JSON.parse(data.picUrl).realFileName)
            .end().find('.nick').text(data.account)
            .end().find('.loginout').text('[退出]');
        $('.layout')
            .find('.pic img').attr('src', "/user/getPic?path=" + JSON.parse(data.picUrl).realFileName)
            .end().find('.userMod .txt').html(data.account + '<span></span>');
        $('.iSetup').find('span').eq(0).text(data.account).end().eq(1).text(data.email)
    }
})