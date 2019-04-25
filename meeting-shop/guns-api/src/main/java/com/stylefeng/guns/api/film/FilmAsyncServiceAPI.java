package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.api.film.vo.FilmDescVO;
import com.stylefeng.guns.api.film.vo.ImgVO;

import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/9 16:55
 * @description Dubbo异步调用接口
 */
public interface FilmAsyncServiceAPI {

    /**
     *  获取影片描述信息
     * @param filmId
     * @return
     */
    FilmDescVO getFilmDesc(String filmId);

    /**
     *  获取图片信息
     * @param filmId
     * @return
     */
    ImgVO getImgs(String filmId);

    /**
     *  获取导演信息
     * @param filmId
     * @return
     */
    ActorVO getDectInfo(String filmId);

    /**
     *  获取演员信息
     * @param filmId
     * @return
     */
    List<ActorVO> getActors(String filmId);
}
