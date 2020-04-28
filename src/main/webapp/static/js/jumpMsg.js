function jumpBlock(msg) {
    console.log('asd');
    $('<div class="comment-toast" style="opacity: 1; margin-left: -240px; top: 300px;">'+ msg +'</div>').appendTo($('body'));
    setTimeout(interval, 1300);
    function interval() {
        let time = setInterval(function () {
            let speed = 0.05;
            let opacity = $('.comment-toast').css('opacity');
            if (opacity == 0) {
                clearInterval(time);
                $('.comment-toast').remove();
            }
            $('.comment-toast').css('opacity', opacity - speed);
        }, 20);
    }
}