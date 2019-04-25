package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.api.film.vo.BannerVO;
import com.stylefeng.guns.api.film.vo.CatVO;
import com.stylefeng.guns.api.film.vo.FilmDescVO;
import com.stylefeng.guns.api.film.vo.FilmDetailVO;
import com.stylefeng.guns.api.film.vo.FilmInfoVO;
import com.stylefeng.guns.api.film.vo.FilmVO;
import com.stylefeng.guns.api.film.vo.ImgVO;
import com.stylefeng.guns.api.film.vo.SourceVO;
import com.stylefeng.guns.api.film.vo.YearVO;

import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/7 19:57
 * @description
 */
public interface FilmServiceAPI {

    /**
     *  获取banners
     * @return
     */
    List<BannerVO> getBanners();

    /**
     *  获取热映影片
     * @param isLimit
     * @param nums
     * @param nowPage
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param catId
     * @return
     */
    FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    /**
     * 获取即将上映影片，根据受欢迎程度排序
     * @param isLimit
     * @param nums
     * @param nowPage
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param catId
     * @return
     */
    FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    /**
     *  获取经典影片
     * @param nums
     * @param nowPage
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param catId
     * @return
     */
    FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    /**
     *  获取票房排行
     * @return
     */
    List<FilmInfoVO> getBoxRanking();

    /**
     *  获取人气排行榜
     * @return
     */
    List<FilmInfoVO> getExpectRanking();

    /**
     *  获取前100
     * @return
     */
    List<FilmInfoVO> getTop();

    /**
     *  获取影片条件接口：分类条件
     * @return
     */
    List<CatVO> getCats();

    /**
     *  获取影片条件接口：片源条件
     * @return
     */
    List<SourceVO> getSources();

    /**
     *  获取影片条件接口：年代条件
     * @return
     */
    List<YearVO> getYears();

    /**
     *  根据影片id或名称查询影片详细信息
     * @param searchType
     * @param searchParam
     * @return
     */
    FilmDetailVO getFilmDetail(int searchType, String searchParam);

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
