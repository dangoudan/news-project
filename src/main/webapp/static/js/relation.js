function relation() {
    $.ajax({
        url: '/user/getRelation',
        data: {
            'id': news.id
        },
        type: 'get',
        dataType: 'json',
    }).then(data => {
        $('.bar-con').find('.item').remove();
        for(let i = 0; i < data.length; i++) {
            $('.bar-con').append($('<div class="item">' +
                '<div class="pic">' +
                '<a href="/user/toDetail?id=' + data[i].id + '">' +
                '<img src="/user/getPic?path=' + JSON.parse(data[i].picUrl).realFileName + '" alt="' + data[i].title + '">' +
                '</a></div>' +
                '<div class="txt">' +
                '<a href="/user/toDetail?id=' + data[i].id + '">' + data[i].title + '</a>' +
                '</div></div>'))
        }
    })
}
relation();

//为你推荐
let recommend = {
    offset: 0,
    size: 10,
    total: 0
};

let timeId = setInterval(function () {
    let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    let clientHeight = document.documentElement.clientHeight || document.body.clientHeight;
    let scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
    if (scrollTop + clientHeight === scrollHeight) {
        nextMsg();
    }
}, 1000);

function nextMsg() {
    queryList(recommend.offset * recommend.size, recommend.size);
    recommend.offset++;
    if (recommend.offset * recommend.size >= recommend.total) {
        $('.more').html('已经到底了');
        clearInterval(timeId);
    }
}

function queryList(offset, size) {
    $.ajax({
        url: '/user/getRecommend',
        data: {
            'id': news.id,
            offset,
            size
        },
        type: 'get',
        dateType: 'json',
        success(data) {
            for (let i = 0; i < data.length; i++) {
                if(data[i].picUrl != null) {
                    $('.recommend-list').append($('<li class="clearfix">' +
                        '<div class="picture">' +
                        '<a href="/user/toDetail?id=' + data[i].id + '">' +
                        '<img src="/user/getPic?path=' + JSON.parse(data[i].picUrl).realFileName + '" alt="' + data[i].title + '">' +
                        '</a></div>' +
                        '<div class="detail"><h3>' +
                        '<a href="/user/toDetail?id=' + data[i].id + '">' + data[i].title + '</a>' +
                        '</h3><div class="info">' +
                        '<a href="/user/toDetail?id=' + data[i].id + '">' + data[i].author + '</a> &nbsp;<span>' + timestampToTime2(data[i].ctime) + '</span>' +
                        '</div></div></li>'));
                }else {
                    $('.recommend-list').append($('<li class="clearfix">' +
                        '<div class="text"><h3>' +
                        '<a href="/user/toDetail?id=' + data[i].id + '">' + data[i].title + '</a>' +
                        '</h3><div class="info">' +
                        '<a href="/user/toDetail?id=' + data[i].id + '">' + data[i].author + '</a> &nbsp;<span>' + timestampToTime2(data[i].ctime) + '</span>' +
                        '</div></div></li>'));
                }
            }
        }
    })
}

$.ajax({
    url: '/user/getRecommendCount',
    data: {
        'id': news.id
    },
    type: 'get',
    dateType: 'json',
    success(data) {
        recommend.total = data;
        nextMsg();
    }
});

//举报
function commentReport() {
    if(getCookie("userId") != null) {
        $('.comment-report').unbind('click');
        $('.comment-report').click(function () {
            $('.report-block').remove();
            $(this).addClass('comment-report--click');
            $(this).after($('<div class="report-block">' +
                '<p class="report-title">举报</p>' +
                '<div class="report-content">' +
                '<p class="report-text">您为什么要举报此信息？</p></div>' +
                '<ul class="report-item">' +
                '<li>色情淫秽</li>' +
                '<li>骚扰谩骂</li>' +
                '<li>广告欺诈</li>' +
                '<li>地域攻击</li>' +
                '<li>其他</li>' +
                '</ul>' +
                '<p class="report-info">举报说明(可选)</p>' +
                '<span class="report-remove">' +
                '<i class="report-remove-icon"></i>' +
                '</span>' +
                '<div class="report-input">' +
                '<input type="text" placeholder="请说明举报原因">' +
                '</div>' +
                '<div class="report-btn">' +
                '<span class="report-btn-cancel">取消</span>' +
                '<span class="report-btn-confirm">确定</span>' +
                '<i class="report-btn-line"></i>' +
                '</div></div>'));
            $(this).next().addClass('report-revert--first');
            reportConfirm();
            reportSelect();
            cancelReport();
        });
        $('.reply-operate-report').unbind('click');
        $('.reply-operate-report').click(function () {
            $('.report-block').remove();
            let $reply = $(this).parent();
            $reply.after($('<div class="report-block">' +
                '<p class="report-title">举报</p>' +
                '<div class="report-content">' +
                '<p class="report-text">您为什么要举报此信息？</p></div>' +
                '<ul class="report-item">' +
                '<li>色情淫秽</li>' +
                '<li>骚扰谩骂</li>' +
                '<li>广告欺诈</li>' +
                '<li>地域攻击</li>' +
                '<li>其他</li>' +
                '</ul>' +
                '<p class="report-info">举报说明(可选)</p>' +
                '<span class="report-remove">' +
                '<i class="report-remove-icon"></i>' +
                '</span>' +
                '<div class="report-input">' +
                '<input type="text" placeholder="请说明举报原因">' +
                '</div>' +
                '<div class="report-btn">' +
                '<span class="report-btn-cancel">取消</span>' +
                '<span class="report-btn-confirm">确定</span>' +
                '<i class="report-btn-line"></i>' +
                '</div></div>'));
            $reply.next().addClass('report-reply report-revert');
            reportConfirm();
            reportSelect();
            cancelReport();
        });
    }
}

function cancelReport() {
    $('.report-btn-cancel').click(function () {
        $(this).closest('.report-block').prev().removeClass('comment-report--click');
        $(this).closest('.report-block').remove();
    });
    $('.report-remove-icon').click(function () {
        $(this).closest('.report-block').prev().removeClass('comment-report--click');
        $(this).closest('.report-block').remove();
    })
}

function reportSelect() {
    $('.report-item').find('li').click(function () {
        $(this).parent().find('.report-item--select').removeClass('report-item--select');
        $(this).addClass('report-item--select');
    })
}

function reportConfirm() {
    $('.report-btn-confirm').click(function () {
        let $reportBlock = $(this).closest('.report-block');
        let $parent = $reportBlock.parent();
        let reportTagItem = $reportBlock.find('.report-item').find('li.report-item--select');
        if(reportTagItem == null || reportTagItem.length === 0) {
            jumpBlock("您必须选择一项才能举报");
            return;
        }
        let reportTag = reportTagItem.text();
        let reportContent = $reportBlock.find('.report-input input').val();

        if($parent.hasClass('comment')) {
            let commentId = $parent.data('id');
            $.ajax({
                url: "/user/addReportToComment",
                data: {
                    commentId,
                    reportTag,
                    reportContent
                },
                type: 'get',
                dataType: 'json'
            }).then(data => {
                jumpBlock(data.msg);
                $reportBlock.prev().removeClass('comment-report--click');
                $reportBlock.remove();
            })
        }else if ($parent.hasClass('reply-block')){
            let replyId = $parent.data('replyId');
            $.ajax({
                url: "/user/addReportToReply",
                data: {
                    replyId,
                    reportTag,
                    reportContent
                },
                type: 'get',
                dataType: 'json'
            }).then(data => {
                jumpBlock(data.msg);
                $reportBlock.remove();
            })
        }
    })
}
