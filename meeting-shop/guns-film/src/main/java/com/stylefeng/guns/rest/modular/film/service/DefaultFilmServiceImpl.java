package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceAPI;
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
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocActorTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocBannerTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocCatDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocSourceDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocYearDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocActorT;
import com.stylefeng.guns.rest.common.persistence.model.MoocBannerT;
import com.stylefeng.guns.rest.common.persistence.model.MoocCatDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilmInfoT;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilmT;
import com.stylefeng.guns.rest.common.persistence.model.MoocSourceDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocYearDictT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/7 20:32
 * @description todo 异常处理
 */
@Component
@Service(interfaceClass = FilmServiceAPI.class)
public class DefaultFilmServiceImpl implements FilmServiceAPI {

    @Autowired
    private MoocBannerTMapper moocBannerTMapper;
    @Autowired
    private MoocFilmTMapper moocFilmTMapper;
    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;
    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;
    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;
    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;
    @Autowired
    private MoocActorTMapper moocActorTMapper;

    /**
     * 获取banners
     *
     * @return
     */
    @Override
    public List<BannerVO> getBanners() {
        List<MoocBannerT> moocBanners = moocBannerTMapper.selectList(null);
        List<BannerVO> result = new ArrayList<>();
        for (MoocBannerT moocBannerT : moocBanners) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(moocBannerT.getUuid() + "");
            bannerVO.setBannerUrl(moocBannerT.getBannerUrl());
            bannerVO.setBannerAddress(moocBannerT.getBannerAddress());
            result.add(bannerVO);
        }
        return result;
    }

    private List<FilmInfoVO> getFilmInfos(List<MoocFilmT> moocFilms) {
        List<FilmInfoVO> filmInfos = new ArrayList<>();
        for (MoocFilmT moocFilmT : moocFilms) {
            FilmInfoVO filmInfo = new FilmInfoVO();
            filmInfo.setScore(moocFilmT.getFilmScore());
            filmInfo.setImgAddress(moocFilmT.getImgAddress());
            filmInfo.setFilmType(moocFilmT.getFilmType());
            filmInfo.setFilmScore(moocFilmT.getFilmScore());
            filmInfo.setFilmName(moocFilmT.getFilmName());
            filmInfo.setFilmId(moocFilmT.getUuid() + "");
            filmInfo.setExpectNum(moocFilmT.getFilmPresalenum());
            filmInfo.setBoxNum(moocFilmT.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(moocFilmT.getFilmTime()));
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    /**
     * 获取热映影片
     *
     * @return
     */
    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfoVO> filmInfos;
        // 热映影片限制
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        // 判断是否是首页需要的内容
        if (isLimit) {
            // 如果是，则限制条数、限制内容为热映影片
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmNum(filmInfos.size());
            filmVO.setFilmInfo(filmInfos);
        } else {
            // 如果不是，则是列表页，同样需要限制内容为热映影片
            Page<MoocFilmT> page = null;
            // 排序方式，1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
            }
            // 如果传入的参数不是默认条件，按照条件查询
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmInfo(filmInfos);
            filmVO.setFilmNum(filmInfos.size());
            int totalCount = moocFilmTMapper.selectCount(entityWrapper);
            int totalPage = (totalCount / nums) + 1;
            filmVO.setTotalPage(totalPage);
            filmVO.setNowPage(nowPage);
        }
        return filmVO;
    }

    /**
     * 获取即将上映
     *
     * @return
     */
    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfoVO> filmInfos;
        // 热映影片限制
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        // 判断是否是首页需要的内容
        if (isLimit) {
            Page<MoocFilmT> page = new Page<>(1, nums);
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmNum(filmInfos.size());
            filmVO.setFilmInfo(filmInfos);
        } else {
            // 如果不是，则是列表页，同样需要限制内容为即将上映影片
            Page<MoocFilmT> page = null;
            // 排序方式，1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId) {
                case 1:
                    // 即将上映的电影根据预售数量对热度进行排序
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
            }
            // 如果传入的参数不是默认条件，按照条件查询
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(moocFilms);
            filmVO.setFilmInfo(filmInfos);
            filmVO.setFilmNum(filmInfos.size());
            int totalCount = moocFilmTMapper.selectCount(entityWrapper);
            int totalPage = (totalCount / nums) + 1;
            filmVO.setTotalPage(totalPage);
            filmVO.setNowPage(nowPage);
        }
        return filmVO;
    }

    /**
     * 获取经典影片
     *
     * @return
     */
    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfoVO> filmInfos;
        // 热映影片限制
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "3");
        Page<MoocFilmT> page = null;
        // 排序方式，1-按热门搜索，2-按时间搜索，3-按评价搜索
        switch (sortId) {
            case 1:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
            case 2:
                page = new Page<>(nowPage, nums, "film_time");
                break;
            case 3:
                page = new Page<>(nowPage, nums, "film_score");
                break;
            default:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
        }
        // 如果传入的参数不是默认条件，按照条件查询
        if (sourceId != 99) {
            entityWrapper.eq("film_source", sourceId);
        }
        if (yearId != 99) {
            entityWrapper.eq("film_date", yearId);
        }
        if (catId != 99) {
            String catStr = "%#" + catId + "#%";
            entityWrapper.like("film_cats", catStr);
        }
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
        filmInfos = getFilmInfos(moocFilms);
        filmVO.setFilmInfo(filmInfos);
        filmVO.setFilmNum(filmInfos.size());
        int totalCount = moocFilmTMapper.selectCount(entityWrapper);
        int totalPage = (totalCount / nums) + 1;
        filmVO.setTotalPage(totalPage);
        filmVO.setNowPage(nowPage);
        return null;
    }

    /**
     * 获取票房排行
     *
     * @return
     */
    @Override
    public List<FilmInfoVO> getBoxRanking() {
        // 正在上映，票房前10名
        // 热映影片限制
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        // 查询需要对票房字段排序，默认倒序
        Page<MoocFilmT> page = new Page<>(1, 10, "film_box_office");
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
        return getFilmInfos(moocFilms);
    }

    /**
     * 获取人气排行榜
     *
     * @return
     */
    @Override
    public List<FilmInfoVO> getExpectRanking() {
        // 即将上映，预售票房前10名
        // 热映影片限制
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
        return getFilmInfos(moocFilms);
    }

    /**
     * 获取前100
     *
     * @return
     */
    @Override
    public List<FilmInfoVO> getTop() {
        // 正在上映，评分前10名
        // 热映影片限制
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        Page<MoocFilmT> page = new Page<>(1, 10, "film_score");
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, entityWrapper);
        return getFilmInfos(moocFilms);
    }

    /**
     * 获取影片条件接口：分类条件
     *
     * @return
     */
    @Override
    public List<CatVO> getCats() {
        List<CatVO> cats = new ArrayList<>();
        List<MoocCatDictT> moocCatDictTList = moocCatDictTMapper.selectList(null);
        for (MoocCatDictT moocCatDictT : moocCatDictTList) {
            CatVO catVO = new CatVO();
            catVO.setCatId(moocCatDictT.getUuid() + "");
            catVO.setCatName(moocCatDictT.getShowName());

            cats.add(catVO);
        }
        return cats;
    }

    /**
     * 获取影片条件接口：片源条件
     *
     * @return
     */
    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sources = new ArrayList<>();
        List<MoocSourceDictT> moocSourceDictTList = moocSourceDictTMapper.selectList(null);
        for (MoocSourceDictT moocSourceDictT : moocSourceDictTList) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(moocSourceDictT.getUuid() + "");
            sourceVO.setSourceName(moocSourceDictT.getShowName());

            sources.add(sourceVO);
        }
        return sources;
    }

    /**
     * 获取影片条件接口：年代条件
     *
     * @return
     */
    @Override
    public List<YearVO> getYears() {
        List<YearVO> years = new ArrayList<>();
        List<MoocYearDictT> moocYearDictTList = moocYearDictTMapper.selectList(null);
        for (MoocYearDictT moocYearDictT : moocYearDictTList) {
            YearVO yearVO = new YearVO();
            yearVO.setYearId(moocYearDictT.getUuid() + "");
            yearVO.setYearName(moocYearDictT.getShowName());

            years.add(yearVO);
        }
        return years;
    }

    /**
     * 根据影片id或名称查询影片详细信息
     *
     * @param searchType  1-按名称查询，2-按id查询
     * @param searchParam 查询参数
     * @return
     */
    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        FilmDetailVO filmDetailVO;
        if (searchType==1){
            filmDetailVO=moocFilmTMapper.getFilmDetailByName("%"+searchParam+"%");
        } else {
            filmDetailVO=moocFilmTMapper.getFilmDetailById(searchParam);
        }
         return filmDetailVO;
    }

    private MoocFilmInfoT getFilmInfo(String filmId){
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        moocFilmInfoT.setFilmId(filmId);
        moocFilmInfoT = moocFilmInfoTMapper.selectOne(moocFilmInfoT);
        return moocFilmInfoT;
    }

    /**
     * 获取影片描述信息
     *
     * @param filmId
     * @return
     */
    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(moocFilmInfoT.getBiography());
        filmDescVO.setFilmId(moocFilmInfoT.getFilmId());
        return filmDescVO;
    }

    /**
     * 获取图片信息
     *
     * @param filmId
     * @return
     */
    @Override
    public ImgVO getImgs(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        // 图片地址是一条由英文逗号分隔的包含五张图片地址的字符串
        String filmImgs = moocFilmInfoT.getFilmImgs();
        String[] imgs = filmImgs.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(imgs[0]);
        imgVO.setImg01(imgs[1]);
        imgVO.setImg02(imgs[2]);
        imgVO.setImg03(imgs[3]);
        imgVO.setImg04(imgs[4]);
        return imgVO;
    }

    /**
     * 获取导演信息
     *
     * @param filmId
     * @return
     */
    @Override
    public ActorVO getDectInfo(String filmId) {
        MoocFilmInfoT moocFilmInfoT = getFilmInfo(filmId);
        Integer directorId = moocFilmInfoT.getDirectorId();
        MoocActorT moocActorT = moocActorTMapper.selectById(directorId);
        ActorVO actorVO = new ActorVO();
        actorVO.setDirectorName(moocActorT.getActorName());
        actorVO.setImgAddress(moocActorT.getActorImg());
        return actorVO;
    }

    /**
     * 获取演员信息
     *
     * @param filmId
     * @return
     */
    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actors = moocActorTMapper.getActors(filmId);
        return actors;
    }
}
