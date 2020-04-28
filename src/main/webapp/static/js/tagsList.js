let tagsList = null;

$('.open').click(function () {
    $(this).css('display', 'none');
    $('.close').css('display', 'block');
    $('.sub-nav').css('display', 'block');
});
$('.close').click(function () {
    $(this).css('display', 'none');
    $('.open').css('display', 'block');
    $('.sub-nav').css('display', 'none');
});


function setTags() {
    let tagsArr = JSON.parse(getCookie('tagsArr'));
    let cTagsArr = JSON.parse(getCookie('cTagsArr'));
    let moreTagsArr = JSON.parse(getCookie('moreTagsArr'));
    if (tagsArr != null && moreTagsArr != null && cTagsArr != null) {
        $('.main-list li').remove();
        $('.my-list li').remove();
        $('.hot-list li').remove();
        $('.more-list li').remove();
        for (let i = 0; i < tagsArr.length; i++) {
            if (i === 0) {
                $('.main-list').append($('<li class="item active"><a href="/user/toNewsList" data-id="' + tagsArr[i].id + '">' + tagsArr[i].tag + '</a></li>'));
                $('.my-list').append($('<li class="disabled"><a href="/user/toNewsList" data-id="' + tagsArr[i].id + '">' + tagsArr[i].tag + '</a></li>'))
            } else {
                $('.main-list').append($('<li class="item"><a href="/user/toNewsTagsList?tagsId=' + tagsArr[i].id + '" data-id="' + tagsArr[i].id + '">' + tagsArr[i].tag + '</a></li>'));
                $('.my-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + tagsArr[i].id + '">' + tagsArr[i].tag + '</a><i></i></li>'))
            }
        }
        if (tagsId != null) {
            $('.main-list li').eq(0).removeClass('active');
            $('.main-list li').each(function (index, ele) {
                let id = $(ele).find('a').data('id');
                if (tagsId === id) {
                    $(ele).addClass('active');
                }
            })
        }
        for (let i = 0; i < cTagsArr.length; i++) {
            $('.hot-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + cTagsArr[i].id + '">' + cTagsArr[i].tag + '</a></li>'))
        }
        for (let i = 0; i < moreTagsArr.length; i++) {
            $('.more-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + moreTagsArr[i].id + '">' + moreTagsArr[i].tag + '</a></li>'))
        }
        myClick();
        hotClick();
        moreClick();
    }
}

function myClick() {
    $('.my-list i').unbind('click');
    $('.my-list i').click(function () {
        let ele = $(this).parent().remove();
        let id = $(ele).find('a').data('id');
        let tag = $(ele).find('a').text();
        let tagsType = 0;
        for (let i = 0; i < tagsList.length; i++) {
            if (tagsList[i].tag === tag) {
                tagsType = tagsList[i].tagsType;
            }
        }
        if (tagsType === 0) {
            $('.more-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + id + '">' + tag + '</a></li>'));
            moreClick();
        } else {
            $('.hot-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + id + '">' + tag + '</a></li>'));
            hotClick();
        }
    });
}

function hotClick() {
    $('.hot-list li').unbind('click');
    $('.hot-list li').click(function () {
        let len = $('.my-list').children().length;
        if (len >= 10) {
            let myEle = $('.my-list li').eq(-1).remove();
            let id = $(myEle).find('a').data('id');
            let tag = $(myEle).find('a').text();
            let tagsType = 0;
            for (let i = 0; i < tagsList.length; i++) {
                if (tagsList[i].tag === tag) {
                    tagsType = tagsList[i].tagsType;
                }
            }
            if(tagsType === 0) {
                $('.more-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + id + '">' + tag + '</a></li>'));
                moreClick();
            }else {
                $('.hot-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + id + '">' + tag + '</a></li>'));
                hotClick();
            }
        }
        let ele = $(this).remove();
        $(ele).append($('<i></i>'));
        $('.my-list').append($(ele));
        myClick();
    })
}

function moreClick() {
    $('.more-list li').unbind('click');
    $('.more-list li').click(function () {
        let len = $('.my-list').children().length;
        if (len >= 10) {
            let myEle = $('.my-list li').eq(-1).remove();
            let id = $(myEle).find('a').data('id');
            let tag = $(myEle).find('a').text();
            let tagsType = 0;
            for (let i = 0; i < tagsList.length; i++) {
                if (tagsList[i].tag === tag) {
                    tagsType = tagsList[i].tagsType;
                }
            }
            if(tagsType === 0) {
                $('.more-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + id + '">' + tag + '</a></li>'));
                moreClick();
            }else {
                $('.hot-list').append($('<li class="item"><a href="javascript:void 0" data-id="' + id + '">' + tag + '</a></li>'));
                hotClick();
            }
        }
        let ele = $(this).remove();
        $(ele).append($('<i></i>'));
        $('.my-list').append($(ele));
        myClick();
    })
}

$('.submit').click(function () {
    $('.close').css('display', 'none');
    $('.open').css('display', 'block');
    $('.sub-nav').css('display', 'none');
    let tagsArr = [];
    let cTagsArr = [];
    let moreTagsArr = [];
    let myList = [];
    let hotList = [];
    let moreList = [];
    $('.my-list a').each(function (index, ele) {
        myList.push($(ele).data('id'));
    });
    console.log(myList);
    $('.hot-list a').each(function (index, ele) {
        hotList.push($(ele).data('id'));
    });
    console.log(hotList);
    $('.more-list a').each(function (index, ele) {
        moreList.push($(ele).data('id'));
    });
    console.log(moreList);
    for (let i = 0; i < myList.length; i++) {
        for (let j = 0; j < tagsList.length; j++) {
            if (tagsList[j].id === myList[i]) {
                tagsArr.push(tagsList[j]);
                break;
            }
        }
    }
    for (let i = 0; i < hotList.length; i++) {
        for (let j = 0; j < tagsList.length; j++) {
            if (tagsList[j].id === hotList[i]) {
                cTagsArr.push(tagsList[j]);
                break;
            }
        }
    }
    for (let i = 0; i < moreList.length; i++) {
        for (let j = 0; j < tagsList.length; j++) {
            if (tagsList[j].id === moreList[i]) {
                moreTagsArr.push(tagsList[j]);
                break;
            }
        }
    }
    console.log(tagsArr);
    console.log(cTagsArr);
    console.log(moreTagsArr);
    setCookie("tagsArr", JSON.stringify(tagsArr));
    setCookie("cTagsArr", JSON.stringify(cTagsArr));
    setCookie("moreTagsArr", JSON.stringify(moreTagsArr));
    setTags();
});
$('.reset').click(function () {
    $('.close').css('display', 'none');
    $('.open').css('display', 'block');
    $('.sub-nav').css('display', 'none');
    delCookie('tagsArr');
    delCookie('cTagsArr');
    delCookie('moreTagsArr');
    setTagsList();
});

$('.cancel').click(function () {
    $('.close').css('display', 'none');
    $('.open').css('display', 'block');
    $('.sub-nav').css('display', 'none');
    delCookie('tagsArr');
    delCookie('cTagsArr');
    delCookie('moreTagsArr');
    setTagsList();
});

setInterval(function () {
    let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop >= $('.qq_navWrap').height()) {
        $('.qq_navWrap').css('position', 'fixed')
    } else {
        $('.qq_navWrap').css('position', 'relative')
    }
}, 1000);

function setTagsList() {
    let tagsArr = getCookie('tagsArr');
    let moreTagsArr = getCookie('moreTagsArr');
    let cTagsArr = getCookie('cTagsArr');
    tagsList = JSON.parse(getCookieDecode("tags"));
    console.log(tagsList);
    if (tagsArr == null || tagsArr === "[]" || moreTagsArr == null || moreTagsArr === "[]" || cTagsArr == null) {
        tagsArr = [];
        moreTagsArr = [];
        cTagsArr = [];
        for (let i = 0; i < 10; i++) {//初始值
            tagsArr.push(tagsList[i]);
        }
        for (let i = 10; i < tagsList.length; i++) {
            if (tagsList[i].tagsType === 1) {
                cTagsArr.push(tagsList[i]);
            }
        }
        for (let i = 10; i < tagsList.length; i++) {//初始值
            if (tagsList[i].tagsType === 0) {
                moreTagsArr.push(tagsList[i]);
            }
        }
        setCookie("tagsArr", JSON.stringify(tagsArr));
        setCookie("cTagsArr", JSON.stringify(cTagsArr));
        setCookie("moreTagsArr", JSON.stringify(moreTagsArr));
    }
    setTags();
}

setTagsList();
