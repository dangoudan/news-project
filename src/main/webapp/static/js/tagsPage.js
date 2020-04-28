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
    queryList(offset * size, size);
    offset++;
    if (offset * size >= total) {
        $('.more').eq(0).html('已经到底了');
        clearInterval(timeId);
    }
}

function queryList(offset, size) {
    $.ajax({
        url: '/user/tagsPage',
        data: {
            tagsId,
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
                    $('.hotnews ul').append($('<li class="item clearfix">' +
                        '<a href="/user/toDetail?id=' + result[i].id + '" class="picture">' +
                        '<img src="/user/getPic?path=' + JSON.parse(result[i].picUrl).realFileName + '" alt="' + result[i].title + '" data-id="' + result[i].id + '">' +
                        '</a>' +
                        '<div class="detail">' +
                        '<h3>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '">' +
                        '' + result[i].title + '' +
                        '</a>' +
                        '</h3>' +
                        '<div class="tags"></div>' +
                        '<div class="binfo clearfix">' +
                        '<div class="left">' +
                        '<a href="/user/toDetail?id=' + result[i].id + '" class="source">' + result[i].author + '</a>' +
                        '<span class="time">' + timestampToTime2(result[i].ctime) + '</span>' +
                        '</div>' +
                        '<div class="right">' +
                        '<div class="i-share">' +
                        '<span>分享</span>' +
                        '</div>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '" class="cmt">' + count[i] + '</a>' +
                        '</div></div></div></li>'));
                }else {
                    $('.hotnews ul').append($('<li class="item clearfix">' +
                        '<div class="text">' +
                        '<h3>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '">' +
                        '' + result[i].title + '' +
                        '</a>' +
                        '</h3>' +
                        '<div class="binfo clearfix">' +
                        '<div class="left">' +
                        '<a href="/user/toDetail?id=' + result[i].id + '" class="source">' + result[i].author + '</a>' +
                        '<span class="time">' + timestampToTime2(result[i].ctime) + '</span>' +
                        '</div>' +
                        '<div class="right">' +
                        '<div class="i-share">' +
                        '<span>分享</span>' +
                        '</div>' +
                        '<a href="/user/toDetail?id=' + result[i].id + '" class="cmt">' + count[i] + '</a>' +
                        '</div></div></div></li>'));
                }
            }
            // setSrc();
        }
    })
}

$('.hot-bar .more').click(function () {
    hotList(hoffset * hsize, hsize);
    hoffset++;
    if (hoffset * hsize >= total) {
        $(this).html('没有更多了').css('cursor', 'default');
        $('.hot-bar .more').unbind('click')
    }
});

function hotList(offset, size) {
    $.ajax({
        url: '/user/hotTagsPage',
        data: {
            tagsId,
            offset,
            size
        },
        type: 'get',
        dateType: 'json',
        success(data) {
            let list = '';
            for (let i = 0; i < data.length; i++) {
                list += '<li class="clearfix"><div class="pic">' +
                    '<a href="/user/toDetail?id=' + data[i].id + '"><img src="/user/getPic?path=' + JSON.parse(data[i].picUrl).realFileName + '" alt="' + data[i].title + '" data-id="' + data[i].id + '"></a></div>' +
                    '<div class="txt"><a href="/user/toDetail?id=' + data[i].id + '">' + data[i].title + '</a></div></li>';
            }
            $('.bar-con ul').append($(list));
            // setHotSrc();
        }
    })
}



(function () {
    function getCount() {
        //查看总条数，判断能不能向后翻页
        let xhr = new XMLHttpRequest();
        xhr.open("get", "/user/getCountByTagsId?tagsId=" + tagsId);
        xhr.onreadystatechange = function () {
            if (xhr.status == 200 && xhr.readyState == 4) {
                total = JSON.parse(xhr.responseText);
                next();
                $('.hot-bar .more').trigger('click');
            }
        };
        xhr.send();
    }
    setSrc();
    // setHotSrc();
    getCount();
})();

