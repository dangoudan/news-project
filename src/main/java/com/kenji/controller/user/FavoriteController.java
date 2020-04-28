package com.kenji.controller.user;

import com.alibaba.fastjson.JSON;
import com.kenji.domain.Favorite;
import com.kenji.domain.News;
import com.kenji.service.FavoriteService;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.util.List;

@Controller
@RequestMapping("/user")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping("/addFavor")
    public void addFavor(@CookieValue("userId") int userId, int newsId, HttpServletResponse response) throws Exception {
        Favorite favorite = new Favorite();
        favorite.setNewsId(newsId);
        favorite.setUserId(userId);
        favorite.setCtime(TimeUtil.getNow());
        favoriteService.addFavorite(favorite);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"收藏成功",null);
    }

    @RequestMapping("/favorList")
    public void favorList(@CookieValue("userId") int userId, int offset, int size, HttpServletResponse response) throws Exception {
        List<News> result = favoriteService.getFavorList(userId, offset, size);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"查询成功", JSON.toJSONString(result));
    }

    @ResponseBody
    @RequestMapping("/getFavorCount")
    public int getFavorCount(@CookieValue("userId") int userId) {
        return favoriteService.getFavorCount(userId);
    }

    @RequestMapping("/getFavor")
    public void getFavor(@CookieValue("userId") int userId, int newsId, HttpServletResponse response) throws Exception {
        Favorite favorite = favoriteService.getFavorOne(userId, newsId);
        if(favorite != null) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"已收藏", null);
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"未收藏", null);
        }
    }

    @RequestMapping("/cancelFavor")
    public void cancelFavor(@CookieValue("userId") int userId, int newsId, HttpServletResponse response) throws Exception {
        favoriteService.cancelFavor(userId,newsId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"取消成功",null);
    }
}
