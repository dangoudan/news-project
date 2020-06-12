function update() {
    $('.updateMsg').click(function () {
        $('.txt').find('input').removeClass('dis').removeAttr('disabled');
        $('.del-icon').removeAttr('style');
        $(this).parent().append($('<div class="confirm">确认修改</div>'));
        $(this).remove();
        confirm();
    })
}

function confirm() {
    $('.confirm').click(function () {
        let account = $('.txt').find('input').eq(0).val();
        let email = $('.txt').find('input').eq(1).val();
        if(account == '' || email == '') {
            alert("用户名或邮箱为空");
            return;
        }
        if (account.length < 3 || account.length > 16){
            alert('用户名长度为3 ~ 16');
            return;
        }
        let file = pic[0];
        let form = new FormData();
        form.append("account", account);
        form.append("email", email);
        if(file != null) {
            form.append("file", file);
        }
        let xhr = new XMLHttpRequest();
        xhr.open("post", "/user/updateMsg");
        xhr.onreadystatechange = function () {
            if (xhr.status == 200 && xhr.readyState == 4) {
                let msg = xhr.responseText;
                alert(msg);
                $('.txt').find('input').addClass('dis').attr('disabled', true);
                $('.del-icon').css("display", 'none');
                $('.confirm').parent().append($('<div class="updateMsg">修改信息</div>'));
                $('.confirm').remove();
                update();
                window.location.href = '/user/toMy?type=myMsg';
            }
        };
        xhr.send(form);
    })
}

let pic = [];
function putChange() {
    $('#file').change(function () {
        $('.img_wrap').children().remove();
        $('.img_wrap').append($(
            '<img src="" alt="" class="pic-img"><i class="del-icon"></i>'
        ));
        let previewImg = document.querySelector('.pic-img');
        let file = this.files[0];
        pic.push(file);
        let reader = new FileReader();
        // 监听reader对象的的onload事件，当图片加载完成时，把base64编码賦值给预览图片
        reader.addEventListener("load", function () {
            previewImg.src = reader.result;
        }, false);
        // 调用reader.readAsDataURL()方法，把图片转成base64
        reader.readAsDataURL(file);
        delPic();
    });
}

function delPic() {
    //绑定删除键
    $('.del-icon').on('click', function () {
        $('.img_wrap').children().remove();
        $('.img_wrap').append($(
           '<div class="add-icon"></div><div class="box-upload-form"><input type="file" class="box-upload-file" id="file"></div>'
        ));
        $('#file').val('');
        pic.pop();
        putChange();
    })
}
delPic();

update();