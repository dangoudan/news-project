function loginOut() {
    delCookie("userId");
    loginOrNot();
    if(getCookie("userId") == null) {
        $('.login-wrap').eq(0).find('.login-area')
            .find('img').attr('src', "")
            .end().find('.nick').text("")
            .end().find('.loginout').text("");
        $('#comment-wrap .box').eq(0)
            .find('.common-avatar img').attr('src', "")
            .end().find('.box-info .box-username').text("");
        $('.login-wrap').eq(0).addClass('hidden');
        $('.login-wrap').eq(1).removeClass('hidden');
        $('#comment-wrap .box').eq(0).addClass('hidden');
        $('#comment-wrap .box').eq(1).removeClass('hidden');
    }
}

function loginOrNot() {
    if(getCookie("userId") == null) {
        $('.reply-operate-item').unbind('click');
        $('.comment-operate-item').unbind('click');
        $('.repin').unbind('click');
        $('.thumb').unbind('click');
        $('.comment-report').unbind('click');
        $('.reply-operate-report').unbind('click');
        $('.reply-operate-item').click(function () {
            location.href = '/user/toLogin';
        });
        $('.comment-operate-item').click(function () {
            location.href = '/user/toLogin';
        });
        $('.box-btn').click(function () {
            location.href = '/user/toLogin';
        });
        $('.repin').click(function () {
            location.href = '/user/toLogin';
        });
        $('.thumb').click(function () {
            location.href = '/user/toLogin';
        });
        $('.comment-report').click(function () {
            location.href = '/user/toLogin';
        });
        $('.reply-operate-report').click(function () {
            location.href = '/user/toLogin';
        });
    }else {
        replyMsg();
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
    if(getCookie("userId") != null) {
        $('.login-wrap').eq(1).addClass('hidden');
        $('.login-wrap').eq(0).removeClass('hidden');
        $('#comment-wrap .box').eq(1).addClass('hidden');
        $('#comment-wrap .box').eq(0).removeClass('hidden');
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
                    .end().find('.loginout').text('[退出登录]');
                $('#comment-wrap .box').eq(0)
                    .find('.common-avatar img').attr('src', "/user/getPic?path=" + JSON.parse(data.picUrl).realFileName)
                    .end().find('.box-info .box-username').text(data.account)
            }
        });
    }
})();