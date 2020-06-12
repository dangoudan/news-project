let lastEditRange; //最后光标对象
//插入图片第一种方法
let fileArr = [];
$('#imgs').change(function () {
    // $('.ProseMirror').focus();
    // 获取选定对象
    let selection = getSelection();
    // 判断是否有最后光标对象存在
    if (lastEditRange) {
        // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
        selection.removeAllRanges();
        selection.addRange(lastEditRange)
    }
    let file = this.files[0];
    fileArr.push(file);
    let reader = new FileReader();
    reader.addEventListener("load", function () {
        document.execCommand('insertImage', false, reader.result);
        $('#imgs').val('');
    }, false);
    reader.readAsDataURL(file);
});

//文本
$('.ProseMirror').focus(function () {
    $(this).addClass('ProseMirror-focused');
});
$('.ProseMirror').blur(function () {
    $(this).removeClass('ProseMirror-focused');
    // 获取选定对象
    let selection = getSelection();
    // 设置最后光标对象
    lastEditRange = selection.getRangeAt(0)
});
$('.ProseMirror').on('input', function (e) {
    $(this).removeClass('placeholeder');
    let $imgs = $(this).find('img');
    if ($imgs.length != fileArr.length) {
        let len = fileArr.length - $imgs.length;
        for (let i = 0; i < len; i++) {
            fileArr.pop();
        }
    }
    if ($('.ProseMirror').children().length == 0) {
        $(this).addClass('placeholeder');
        $(this).append('<p><br></p>')
    }
});
$('.iconbold').click(function () {
    // $('.ProseMirror').focus();
    // 获取选定对象
    let selection = getSelection();
    // 判断是否有最后光标对象存在
    if (lastEditRange) {
        // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
        selection.removeAllRanges();
        selection.addRange(lastEditRange)
    }
    document.execCommand('bold', false, null);
    // if ($(this).hasClass('icolor')) {
    //     $(this).removeClass('icolor');
    //
    // } else {
    //     $(this).addClass('icolor');
    //     document.execCommand('bold', false, null);
    // }
});

$('.iconxiahuaxian').click(function () {
    let selection = getSelection();
    // 判断是否有最后光标对象存在
    if (lastEditRange) {
        // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
        selection.removeAllRanges();
        selection.addRange(lastEditRange)
    }
    document.execCommand('underline', false, null);
});

$('.iconwuxupailie').click(function () {//无序
    let selection = getSelection();
    // 判断是否有最后光标对象存在
    if (lastEditRange) {
        // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
        selection.removeAllRanges();
        selection.addRange(lastEditRange)
    }
    let ele = selection.anchorNode.parentElement.nodeName == 'DIV' ?
        selection.anchorNode : selection.anchorNode.parentElement;
    let $ul = $(ele).closest('ul');
    if ($ul[0] == null || $ul.length == 0) {
        document.execCommand('insertUnorderedList', false, null);
        let $p = $(ele).closest('p');
        let html = $(ele).closest('p').html();
        $(html).insertBefore($p);
        $p.remove();
    } else {
        let count = getSelectEleLen();
        if ($ul.find('li').length == count) {
            $ul.find('li').each(function (index, ele) {
                $('<p>' + $(ele).text() + '</p>').insertBefore($ul);
            });
            let $p = $ul.prev();
            let p = $p[0];
            setRange(p);
            $ul.remove();
        } else if (count < $ul.find('li').length && lastEditRange.startContainer.nodeName != 'LI') {
            let startEle = lastEditRange.startContainer.parentElement.nodeName == 'UL' ?
                lastEditRange.startContainer : lastEditRange.startContainer.parentElement;
            let removeEle = null;
            let list = '';
            for (let i = 0; i < count; i++) {
                list += '<p>' + $(startEle).text() + '</p>';
                removeEle = startEle;
                startEle = startEle.nextElementSibling;
                $(removeEle).remove();
            }
            $ul.after($(list));
            let li = $ul.find('li').eq(-1)[0];
            setRange(li);
        } else if (count == 1 && lastEditRange.startContainer.nodeName == 'LI') {
            let startEle = lastEditRange.startContainer;
            $(startEle).remove();
            $ul.after($('<p><br></p>'));
            let p = $ul.next()[0];
            setRange(p);
        }
    }
});

function setRange(ele) {
    lastEditRange.selectNodeContents(ele); //选中节点的内容
    lastEditRange.collapse(true);
    if (ele.innerHTML.length > 0 && ele.innerHTML != '<br>') {
        lastEditRange.setStart(ele.childNodes[0], ele.childNodes[0].length);
    }
    let selection = getSelection();
    selection.removeAllRanges();
    selection.addRange(lastEditRange);
    lastEditRange = selection.getRangeAt(0);
}

function getSelectEleLen() {
    let startEle = lastEditRange.startContainer.parentElement;
    let endEle = lastEditRange.endContainer.parentElement;
    let count = 1;
    while (true) {
        if (startEle == endEle) {
            break;
        }
        count++;
        startEle = startEle.nextElementSibling;
    }
    return count;
}

$('.iconyouxuliebiao1').click(function () {//有序
    let selection = getSelection();
    // 判断是否有最后光标对象存在
    if (lastEditRange) {
        // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
        selection.removeAllRanges();
        selection.addRange(lastEditRange)
    }
    let ele = selection.anchorNode.parentElement.nodeName == 'DIV' ?
        selection.anchorNode : selection.anchorNode.parentElement;
    let $ol = $(ele).closest('ol');
    if ($ol[0] == null || $ol.length == 0) {
        document.execCommand('insertOrderedList', false, null);
        let $p = $(ele).closest('p');
        let html = $(ele).closest('p').html();
        $(html).insertBefore($p);
        $p.remove();
    } else {
        let count = getSelectEleLen();
        if ($ol.find('li').length == count) {
            $ol.find('li').each(function (index, ele) {
                $('<p>' + $(ele).text() + '</p>').insertBefore($ol);
            });
            let $p = $ol.prev();
            let p = $p[0];
            setRange(p);
            $ol.remove();
        } else if (count < $ol.find('li').length && lastEditRange.startContainer.nodeName != 'LI') {
            let startEle = lastEditRange.startContainer.parentElement.nodeName == 'OL' ?
                lastEditRange.startContainer : lastEditRange.startContainer.parentElement;
            let removeEle = null;
            let list = '';
            for (let i = 0; i < count; i++) {
                list += '<p>' + $(startEle).text() + '</p>';
                removeEle = startEle;
                startEle = startEle.nextElementSibling;
                $(removeEle).remove();
            }
            $ol.after($(list));
            let li = $ol.find('li').eq(-1)[0];
            setRange(li);
        } else if (count == 1 && lastEditRange.startContainer.nodeName == 'LI') {
            let startEle = lastEditRange.startContainer;
            $(startEle).remove();
            $ol.after($('<p><br></p>'));
            let p = $ol.next()[0];
            setRange(p);
        }
    }
});

$('.iconundo').click(function () {
    let selection = getSelection();
    // 判断是否有最后光标对象存在
    if (lastEditRange) {
        // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
        selection.removeAllRanges();
        selection.addRange(lastEditRange)
    }
    document.execCommand('undo', false, null);
});

$('.iconredo').click(function () {
    let selection = getSelection();
    // 判断是否有最后光标对象存在
    if (lastEditRange) {
        // 存在最后光标对象，选定对象清除所有光标并添加最后光标还原之前的状态
        selection.removeAllRanges();
        selection.addRange(lastEditRange)
    }
    document.execCommand('redo', false, null);
});

$('.ProseMirror').on('paste', function (e) {
    e.preventDefault(); // 阻止默认粘贴
    // 粘贴事件有一个clipboardData的属性，提供了对剪贴板的访问
    // clipboardData的getData(fomat) 从剪贴板获取指定格式的数据
    let text = (e.originalEvent || e).clipboardData.getData('text/plain') || prompt('在这里输入文本');
    // 插入
    document.execCommand("insertText", false, text);
});

//封面上传
let pics = [];

function putChange() {
    $('#file').change(function () {
        $('.article-cover-images').children().remove();
        $('.article-cover-images').append($(
            '<div class="article-cover-img-wrap"><img src="" alt="" class="pic"><i class="article-cover-delete"></i></div>'
        ));
        let previewImg = document.querySelector('.pic');
        let file = this.files[0];
        pics.push(file);
        let reader = new FileReader();
        // 监听reader对象的的onload事件，当图片加载完成时，把base64编码賦值给预览图片
        reader.addEventListener("load", function () {
            previewImg.src = reader.result;
        }, false);
        // 调用reader.readAsDataURL()方法，把图片转成base64
        reader.readAsDataURL(file);
        //绑定删除键
        $('.article-cover-delete').on('click', function () {
            $('.article-cover-images').children().remove();
            $('.article-cover-images').append($(
                '<div class="article-cover-add"><div class="article-cover-add-icon"></div><div class="box-upload-form"><input type="file" class="box-upload-file" id="file"></div></div>'
            ));
            pics.pop();
            $('#file').val('');
            putChange();
        })
    });
}

putChange();