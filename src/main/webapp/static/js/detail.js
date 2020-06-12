function replyMsg() {
    unbindEvent($('.comment-operate-reply'));
    unbindEvent($('.reply-inner'));
    $('.comment-operate-reply').click(function () {
        if (!$(this).hasClass('operate-visited')) {
            removeArea();
            $(this).addClass('operate-visited').text('收起')
                .closest('.comment-block').append($(
                '<div class="reply-box"><div class="reply-box-block"><textarea class="reply-box-textarea"></textarea></div><div class="reply-box-btn">回复</div></div>'
            ));
            replyBtn();
        } else {
            removeArea();
        }
    });

    $('.reply-inner').click(function () {
        if (!$(this).hasClass('operate-visited')) {
            removeArea();
            $(this).addClass('operate-visited').text('收起')
                .closest('.reply-block').append($(
                '<div class="reply-box"><div class="reply-box-block"><textarea class="reply-box-textarea"></textarea></div><div class="reply-box-btn">回复</div></div>'
            ));
            replyBtn();
        } else {
            removeArea();
        }
    });
}

function removeArea() {
    $('.comment-operate-reply.operate-visited').text('回复')
        .removeClass('operate-visited')
        .closest('.comment-block')
        .find('.reply-box').remove();
    $('.reply-inner.operate-visited').text('回复')
        .removeClass('operate-visited')
        .closest('.comment-block')
        .find('.reply-box').remove();
}

let replyCount = null;
let $moreBtn = null;
let $moreReply = null;

function allBtn() {
    unbindEvent($('.reply-allBtn'));
    $('.reply-allBtn').on('click', function () {
        $moreBtn = $(this).clone().removeClass('reply-allBtn')
            .addClass('reply-moreBtn').text('查看更多回复');
        let $reply = $(this).parent();
        replyCount = $(this).find("i").text();
        $moreBtn.appendTo($reply);
        let commentId = $(this).closest('.comment').data('id');
        $(this).remove();

        getMoreReply(commentId);
    });
}

let page = {
    offset: 0,
    size: 10
};

function nextReply(commentId) {
    nextPage(commentId, page.offset, page.size);
    page.offset += page.size;
    if (page.offset >= replyCount) {
        $moreReply = $moreBtn.prev();
        $moreBtn.remove();
        $moreBtn = null;
    }
}

function nextPage(commentId, offset, size) {
    $.ajax({
        url: '/user/getAllReply',
        data: {
            commentId,
            offset,
            size
        },
        type: 'get',
        dataType: 'json'
    }).then(data => {
        let $replyMore = $moreBtn == null ? $moreReply : $moreBtn.prev();
        if (offset === 0) {
            let count = 0;
            for (let i = 0; i < data.length; i++) {
                if (data[i].repliedName == null) {
                    data.splice(i, 1);
                    i--;
                    count++;
                }
                if (count === 2) {
                    break;
                }
            }
            if (count <= 2) {
                $replyMore.append($('<div class="reply-title">' +
                    '<p class="reply-title-text">更多回复</p>' +
                    '<span class="reply-title-mark"></span>' +
                    '<div class="reply-title-line"><span></span></div>' +
                    '</div>' +
                    '<div class="reply-more-content"></div>'));
            }
        }
        let $replyContent = $replyMore.find('.reply-more-content');
        for (let i = 0; i < data.length; i++) {
            if (data[i].repliedName == null) {
                $replyContent.append($('<div class="reply-block" data-reply-id="' + data[i].id + '">' +
                    '<div class="reply-content">' +
                    '<span class="reply-user">' +
                    '<b class="reply-user-nick">' + data[i].userName + '</b> :' +
                    '</span>' +
                    '' + data[i].content + '' +
                    '</div>' +
                    '<div class="reply-operate">' +
                    '<span class="reply-up reply-operate-item">赞<i>' + (data[i].thumbs === 0 ? '' : data[i].thumbs) + '</i></span>' +
                    '<i class="reply-dot">·</i>' +
                    '<span class="reply-operate-item reply-inner">回复</span>' +
                    '<i class="reply-dot">·</i>' +
                    '<span>' + timestampToTime2(data[i].ctime) + '</span>' +
                    '<i class="reply-dot reply-operate-report">·</i>' +
                    '<span class="reply-operate-item reply-operate-report">举报</span>' +
                    '</div>' +
                    '</div>'));
                findOneReply(data[i].id);
            } else {
                $replyContent.append($('<div class="reply-block" data-reply-id="' + data[i].id + '">' +
                    '<div class="reply-content">' +
                    '<span class="reply-user">' +
                    '<b class="reply-user-nick">' + data[i].userName + '</b> &nbsp;<i class="reply-reply">回复</i>&nbsp; <b class="reply-user-nick">' + data[i].repliedName + '</b> :' +
                    '</span>' +
                    '' + data[i].content + '' +
                    '</div>' +
                    '<div class="reply-operate">' +
                    '<span class="reply-up reply-operate-item">赞<i>' + (data[i].thumbs === 0 ? '' : data[i].thumbs) + '</i></span>' +
                    '<i class="reply-dot">·</i>' +
                    '<span class="reply-operate-item reply-inner">回复</span>' +
                    '<i class="reply-dot">·</i>' +
                    '<span>' + timestampToTime2(data[i].ctime) + '</span>' +
                    '<i class="reply-dot reply-operate-report">·</i>' +
                    '<span class="reply-operate-item reply-operate-report">举报</span>' +
                    '</div>' +
                    '</div>'));
                findOneReply(data[i].id);
            }
        }
        replyUp();
        commentReport();
        loginOrNot();
    })
}

// '<b class="reply-user-nick">' + data[i].userName + '</b> &nbsp;<i class="reply-reply">回复</i>&nbsp; <b class="reply-user-nick">' + data[i].repliedName + '</b> :'' +

function getMoreReply(commentId) {
    page.offset = 0;
    nextReply(commentId);
    unbindEvent($('.reply-moreBtn'));
    $('.reply-moreBtn').click(function () {
        let commentId = $(this).closest('.comment').data('id');
        $moreBtn = $(this);
        let replySize = $moreBtn.parent().find('.reply-block').length;
        page.offset = replySize;
        $.ajax({
            url: '/user/getReplyCount',
            data: {
                commentId
            },
            type: 'get',
            dataType: 'json',
            success(data) {
                replyCount = data;
                nextReply(commentId);
            }
        });
    })
}

$('.nav-search input').keypress((e) => {
    if (e.keyCode === 13) {
        toSearch();
    }
});

function toSearch() {
    let keyword = $('.nav-search input').val();
    if (keyword !== '') {
        window.location.href = '/user/toSearch?keyword=' + keyword;
    }
}


(function () {
    if (getCookie("userId") != null) {
        $('.repin').unbind('click');
        $('.thumb').unbind('click');
        $.ajax({
            url: '/user/findOneNewsThumb',
            data: {
                'newsId': news.id
            },
            type: 'get',
            dataType: 'json'
        }).then(data => {
            if (data.status === 'OK') {
                $('.thumb').addClass('blue')
            }
        });
        $.ajax({
            url: '/user/getFavor',
            data: {
                'newsId': news.id
            },
            type: 'get',
            dataType: 'json'
        }).then(data => {
            if (data.status === 'OK') {
                $('.repin').addClass('blue')
            }
        });
        $('.thumb').click(function () {
            if (!$(this).hasClass('blue')) {
                $(this).addClass('blue');
                $.ajax({
                    url: '/user/addThumbToNews',
                    data: {
                        'newsId': news.id
                    },
                    type: 'get',
                    dataType: 'json'
                });
            } else {
                $(this).removeClass('blue');
                $.ajax({
                    url: '/user/delThumbToNews',
                    data: {
                        'newsId': news.id
                    },
                    type: 'get',
                    dataType: 'json'
                })
            }
        });
        $('.repin').click(function () {
            if (!$(this).hasClass('blue')) {
                $(this).addClass('blue');
                $.ajax({
                    url: '/user/addFavor',
                    data: {
                        'newsId': news.id
                    },
                    type: 'get',
                    dataType: 'json'
                })
            } else {
                $(this).removeClass('blue');
                $.ajax({
                    url: '/user/cancelFavor',
                    data: {
                        'newsId': news.id
                    },
                    type: 'get',
                    dataType: 'json'
                })
            }
        });
    }
})();

function findOneComment(commentId) {
    $.ajax({
        url: '/user/findOneCommentThumb',
        data: {
            commentId
        },
        type: 'get',
        dataType: 'json'
    }).then(data => {
        if (data.status === 'OK') {
            let $comment = $('div[data-id=' + commentId + ']');
            $comment.find('.comment-operate-up').addClass('operate-visited')
        }
    });
}

function findOneReply(replyId) {
    $.ajax({
        url: '/user/findOneReplyThumb',
        data: {
            replyId
        },
        type: 'get',
        dataType: 'json'
    }).then(data => {
        if (data.status === 'OK') {
            let $reply = $('div[data-reply-id=' + replyId + ']');
            $reply.find('.reply-up').addClass('operate-visited')
        }
    });
}

function commentUp() {
    if(getCookie("userId") != null) {
        $('.comment-operate-up').unbind('click');
        $('.comment-operate-up').click(function () {
            let commentId = $(this).closest('.comment').data('id');
            let count = $(this).find('i').text();
            if(!$(this).hasClass('operate-visited')) {
                count = +count + 1;
                $(this).find('i').text(count);
                $(this).addClass('operate-visited');
                $.ajax({
                    url: '/user/addThumbToComment',
                    data: {
                        commentId
                    },
                    type: 'get',
                    dataType: 'json'
                })
            }else {
                count = +count - 1;
                if(count === 0) {
                    $(this).find('i').text('');
                }else {
                    $(this).find('i').text(count);
                }
                $(this).removeClass('operate-visited');
                $.ajax({
                    url: '/user/delThumbToComment',
                    data: {
                        commentId
                    },
                    type: 'get',
                    dataType: 'json'
                })
            }
        })
    }
}

function replyUp() {
    if(getCookie("userId") != null) {
        $('.reply-up').unbind('click');
        $('.reply-up').click(function () {
            let replyId = $(this).closest('.reply-block').data('replyId');
            let count = $(this).find('i').text();
            if(!$(this).hasClass('operate-visited')) {
                count = +count + 1;
                $(this).find('i').text(count);
                $(this).addClass('operate-visited');
                $.ajax({
                    url: '/user/addThumbToReply',
                    data: {
                        replyId
                    },
                    type: 'get',
                    dataType: 'json'
                })
            }else {
                count = +count - 1;
                if(count === 0) {
                    $(this).find('i').text('');
                }else {
                    $(this).find('i').text(count);
                }
                $(this).removeClass('operate-visited');
                $.ajax({
                    url: '/user/delThumbToReply',
                    data: {
                        replyId
                    },
                    type: 'get',
                    dataType: 'json'
                })
            }
        })
    }
}

function setHotClick() {
    if(!$('.hot').hasClass('comment-sort-cur')) {
        $('.hot').click(function () {
            $('.comment-short').children().remove();
            more.offset = 1;
            type = "hot";
            setComment();
        });
    }
    if(!$('.latest').hasClass('comment-sort-cur')) {
        $('.latest').click(function () {
            $('.comment-short').children().remove();
            more.offset = 1;
            type = "latest";
            setComment();
        })
    }
}

function toLogin() {
    window.location.href = "/user/toLogin"
}