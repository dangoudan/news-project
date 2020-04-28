let offset = 0;
let size = 10;
let hoffset = 0;
let hsize = 5;
let total = 0;

let timeId = setInterval(function () {
    let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    let clientHeight = document.documentElement.clientHeight || document.body.clientHeight;
    let scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
    if (scrollTop + clientHeight === scrollHeight) {
        next();
    }
}, 1000);

function next() {
    if (offset * size >= total) {
        $('.more').eq(1).html('已经到底了');
        clearInterval(timeId);
    } else {
        queryList(offset * size, size);
        offset++;
    }
}

function queryList(offset, size) {
    $.ajax({
        url: '/user/setPage',
        data: {
            offset,
            size
        },
        type: 'get',
        dateType: 'json',
        success(data) {
            data = JSON.parse(JSON.parse(data).data);
            let result = data.newsList;
            let count = data.commentCounts;
            for (let i = 0; i < result.length; i++) {
                if(result[i].picUrl != null) {
                    $('.main .list').append($('<li class="clearfix">' +
                        '<a class="picture" href="/user/toDetail?id=' + result[i].id + '">' +
                        '<img src="/user/getPic?path=' + JSON.parse(result[i].picUrl).realFileName + '" alt="' + result[i].title + '" data-id="' + result[i].id + '">' +
                        '</a>' +
                        '<div class="detail">' +
                        '<h3>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '">' +
                        '<span class="iconfont iconhot"></span>' +
                        '' + result[i].title + '' +
                        '</a>' +
                        '</h3>' +
                        '<div class="tags"></div>' +
                        '<div class="info clearfix">' +
                        '<div class="left">' +
                        '<a href="#">' + result[i].author + '</a>' +
                        '<span class="time">' + timestampToTime2(result[i].ctime) + '</span>' +
                        '</div>' +
                        '<div class="right">' +
                        '<div class="share">' +
                        '<span>分享</span>' +
                        '</div>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '">' + count[i] + '</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</li>'));
                }else {
                    $('.main .list').append($('<li class="clearfix">' +
                        '<div class="text">' +
                        '<h3>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '">' +
                        '<span class="iconfont iconhot"></span>' +
                        '' + result[i].title + '' +
                        '</a>' +
                        '</h3>' +
                        '<div class="info clearfix">' +
                        '<div class="left">' +
                        '<a href="#">' + result[i].author + '</a>' +
                        '<span class="time">' + timestampToTime2(result[i].ctime) + '</span>' +
                        '</div>' +
                        '<div class="right">' +
                        '<div class="share">' +
                        '<span>分享</span>' +
                        '</div>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '">' + count[i] + '</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</li>'));
                }
            }
            // setSrc();
        }
    })
}

$('.hot-bar .more').click(function () {
    setHotComment(hoffset * hsize, hsize);
    hoffset++;
    if (hoffset * hsize >= 15) {
        $(this).html('没有更多了').css('cursor', 'default');
        $(this).unbind('click')
    }
});

function setHotComment(offset, size) {
    let list = '';
    for (let i = offset; i < offset + size; i++) {
        list += '<li class="clearfix"><div class="pic">' +
            '<a href="/user/toDetail?id=' + hotCommentNews[i].id + '"><img src="/user/getPic?path=' + JSON.parse(hotCommentNews[i].picUrl).realFileName + '" alt="' + hotCommentNews[i].title + '" data-id="' + hotCommentNews[i].id + '"></a></div>' +
            '<div class="txt"><a href="/user/toDetail?id=' + hotCommentNews[i].id + '">' + hotCommentNews[i].title + '</a></div></li>';
    }
    $('.bar-con ul').append($(list));
    // setHotCommentSrc();
}


(function () {
    function getCount() {
        //查看总条数，判断能不能向后翻页
        let xhr = new XMLHttpRequest();
        xhr.open("get", "/user/getNewsTagsCount");
        xhr.onreadystatechange = function () {
            if (xhr.status == 200 && xhr.readyState == 4) {
                total = JSON.parse(xhr.responseText);
                next();
                $('.hot-bar .more').trigger('click')
            }
        };
        xhr.send();
    }

    // setSrc();
    // setHotCommentSrc();
    getCount();
})();

