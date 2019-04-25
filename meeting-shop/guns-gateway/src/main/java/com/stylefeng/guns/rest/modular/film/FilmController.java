package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.stylefeng.guns.api.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.ActorRequestVO;
import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.api.film.vo.CatVO;
import com.stylefeng.guns.api.film.vo.FilmDescVO;
import com.stylefeng.guns.api.film.vo.FilmDetailVO;
import com.stylefeng.guns.api.film.vo.FilmVO;
import com.stylefeng.guns.api.film.vo.ImgVO;
import com.stylefeng.guns.api.film.vo.InfoRequestVO;
import com.stylefeng.guns.api.film.vo.SourceVO;
import com.stylefeng.guns.api.film.vo.YearVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author junyong.chen
 * @date 2019/4/7 19:17
 * @description todo 外部资源调用加 try catch
 */
@RequestMapping("/film/")
@RestController
public class FilmController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = FilmServiceAPI.class, check = false)
    private FilmServiceAPI filmServiceAPI;
    @Reference(interfaceClass = FilmAsyncServiceAPI.class, async = true, check = false)
    private FilmAsyncServiceAPI filmAsyncServiceAPI;

    @GetMapping("getIndex")
    public ResponseVO getIndex() {
        FilmIndexVO filmIndexVO = new FilmIndexVO();
        // 获取banner信息
        filmIndexVO.setBanners(filmServiceAPI.getBanners());
        // 获取正在热映的电影
        filmIndexVO.setHotFilms(filmServiceAPI.getHotFilms(true, 8, 1, 1, 99, 99, 99));
        // 即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceAPI.getSoonFilms(true, 8, 1, 1, 99, 99, 99));
        // 票房排行
        filmIndexVO.setBoxRanking(filmServiceAPI.getBoxRanking());
        // 获取受欢迎的榜单
        filmIndexVO.setExpectRanking(filmServiceAPI.getExpectRanking());
        // 获取前100
        filmIndexVO.setTop100(filmServiceAPI.getTop());
        return ResponseVO.success(IMG_PRE, filmIndexVO);
    }

    @GetMapping("getConditionList")
    public ResponseVO getConditionList(@RequestParam(name = "catId", required = false, defaultValue = "99")String catId,
                                       @RequestParam(name = "sourceId", required = false, defaultValue = "99")String sourceId,
                                       @RequestParam(name = "yearId", required = false, defaultValue = "99")String yearId){

        FilmConditionVO filmConditionVO = new FilmConditionVO();
        boolean flag = false;
        // 类型集合
        List<CatVO> cats = filmServiceAPI.getCats();
        List<CatVO> catResult = new ArrayList<>();
        CatVO cat = null;
        for (CatVO catVO : cats) {
            // 判断集合是否存在catId，如果存在则将对应实体变成isActive状态
            if (StringUtils.equals(catVO.getCatId(), "99")) {
                cat = catVO;
                continue;
            }
            if (StringUtils.equals(catVO.getCatId(), catId)) {
                catVO.setIsActive(true);
                flag = true;
            } else {
                catVO.setIsActive(false);
            }
            catResult.add(catVO);
        }
        // 如果不存在，则默认将全部变为Active状态
        if (!flag){
            cat.setIsActive(true);
            catResult.add(cat);
        } else {
            catResult.add(cat);
        }

        flag = false;
        // 片源集合
        List<SourceVO> sources = filmServiceAPI.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        SourceVO source = null;
        for (SourceVO sourceVO : sources) {
            // 判断集合是否存在catId，如果存在则将对应实体变成isActive状态
            if (StringUtils.equals(sourceVO.getSourceId(), "99")) {
                source = sourceVO;
                continue;
            }
            if (StringUtils.equals(sourceVO.getSourceId(), sourceId)) {
                sourceVO.setIsActive(true);
                flag = true;
            } else {
                sourceVO.setIsActive(false);
            }
            sourceResult.add(sourceVO);
        }
        // 如果不存在，则默认将全部变为Active状态
        if (!flag){
            source.setIsActive(true);
            sourceResult.add(source);
        } else {
            source.setIsActive(false);
            sourceResult.add(source);
        }

        flag = false;
        // 片源集合
        List<YearVO> years = filmServiceAPI.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        YearVO year = null;
        for (YearVO yearVO : years) {
            // 判断集合是否存在catId，如果存在则将对应实体变成isActive状态
            if (StringUtils.equals(yearVO.getYearId(), "99")) {
                year = yearVO;
                continue;
            }
            if (StringUtils.equals(yearVO.getYearId(), yearId)) {
                yearVO.setIsActive(true);
                flag = true;
            } else {
                yearVO.setIsActive(false);
            }
            yearResult.add(yearVO);
        }
        // 如果不存在，则默认将全部变为Active状态
        if (!flag){
            year.setIsActive(true);
            yearResult.add(year);
        } else {
            year.setIsActive(false);
            yearResult.add(year);
        }
        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);
        return ResponseVO.success(filmConditionVO);
    }


    @GetMapping("getFilms")
    public ResponseVO getFilms(FilmRequestVO filmRequestVO){
        FilmVO filmVO;
        // 根据showType判断影片查询类型
        switch (filmRequestVO.getShowType()){
            case 1:
                filmVO = filmServiceAPI.getHotFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
                break;
            case 2:
                filmVO = filmServiceAPI.getSoonFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
                break;
            case 3:
                filmVO = filmServiceAPI.getClassicFilms(filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
            default:
                filmVO = filmServiceAPI.getHotFilms(false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
                break;
        }
        return ResponseVO.success(filmVO.getNowPage(), filmVO.getTotalPage(), IMG_PRE, filmVO.getFilmInfo());
    }

    /**
     *
     * @param searchParam
     * @param searchType searchType : 0表示按照编号查找，1表示按照名称查找’
     * @return
     */
    @GetMapping("films/{searchParam}")
    public ResponseVO films(@PathVariable("searchParam")String searchParam,
                            int searchType) throws ExecutionException, InterruptedException {
        // 根据searchType判断查询类型
        FilmDetailVO filmDetail = filmServiceAPI.getFilmDetail(searchType,searchParam);
        // 不同的查询类型传入的条件不同
        // 查询影片的详细信息 --  dubbo的异步调用
        String filmId = filmDetail.getFilmId();
        // 获取影片描述信息
        //FilmDescVO filmDescVO =
        filmAsyncServiceAPI.getFilmDesc(filmId);
        Future<FilmDescVO> filmDescVOFuture = RpcContext.getContext().getFuture();
        // 获取影片图片信息
        //ImgVO imgVO =
        filmAsyncServiceAPI.getImgs(filmId);
        Future<ImgVO> imgVOFuture = RpcContext.getContext().getFuture();
        // 获取导演信息
        //ActorVO director =
        filmAsyncServiceAPI.getDectInfo(filmId);
        Future<ActorVO> directorVOFuture = RpcContext.getContext().getFuture();
        // 获取演员信息
        //List<ActorVO> actors =
        filmAsyncServiceAPI.getActors(filmId);
        Future<List<ActorVO>> actorsFuture = RpcContext.getContext().getFuture();
        InfoRequestVO info04 = new InfoRequestVO();
        ActorRequestVO actorRequestVO = new ActorRequestVO();
        actorRequestVO.setDirector(directorVOFuture.get());
        actorRequestVO.setActors(actorsFuture.get());
        info04.setActors(actorRequestVO);
        info04.setFilmId(filmId);
        info04.setImgs(imgVOFuture.get());
        info04.setBiography(filmDescVOFuture.get().getBiography());
        filmDetail.setInfo04(info04);
        return ResponseVO.success(IMG_PRE, filmDetail);
    }

}

