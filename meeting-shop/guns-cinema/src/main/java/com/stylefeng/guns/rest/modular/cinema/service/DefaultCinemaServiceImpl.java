package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.AreaVO;
import com.stylefeng.guns.api.cinema.vo.BrandVO;
import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.CinemaQueryVO;
import com.stylefeng.guns.api.cinema.vo.CinemaVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallTypeVO;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.rest.common.persistence.dao.MoocAreaDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocBrandDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MoocFieldT;
import com.stylefeng.guns.rest.common.persistence.model.MoocHallDictT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/10 11:04
 * @description
 */
@Component
@Service(interfaceClass = CinemaServiceAPI.class, executes = 10)
public class DefaultCinemaServiceImpl implements CinemaServiceAPI {

    @Autowired
    private MoocCinemaTMapper moocCinemaTMapper;
    @Autowired
    private MoocFieldTMapper moocFieldTMapper;
    @Autowired
    private MoocBrandDictTMapper moocBrandDictTMapper;
    @Autowired
    private MoocAreaDictTMapper moocAreaDictTMapper;
    @Autowired
    private MoocHallDictTMapper moocHallDictTMapper;

    /**
     * 查询影院列表
     *
     * @param cinemaQueryVO
     * @return
     */
    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {
        EntityWrapper<MoocCinemaT> entityWrapper = new EntityWrapper<>();
        // 判断是否传入查询条件 brandId hallType  districtId 是否为99
        if (cinemaQueryVO.getBrandId() != 99) {
            entityWrapper.eq("brand_id", cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getHallType() != 99) {
            entityWrapper.eq("", cinemaQueryVO.getHallType());
        }
        if (cinemaQueryVO.getDistrictId() != 99) {
            entityWrapper.like("area_id", "%#" + cinemaQueryVO.getDistrictId() + "#%");
        }
        Page<MoocCinemaT> page = new Page<>(cinemaQueryVO.getNowPage(), cinemaQueryVO.getPageSize());
        // 将数据实体转化为业务实体
        List<MoocCinemaT> moocCinemaTList = moocCinemaTMapper.selectPage(page, entityWrapper);
        List<CinemaVO> cinemas = new ArrayList<>();
        for (MoocCinemaT moocCinemaT : moocCinemaTList) {
            CinemaVO cinemaVO = new CinemaVO();
            cinemaVO.setUuid(moocCinemaT.getUuid() + "");
            cinemaVO.setCinemaName(moocCinemaT.getCinemaName());
            cinemaVO.setAddress(moocCinemaT.getCinemaAddress());
            cinemaVO.setMinimumPrice(moocCinemaT.getMinimumPrice() + "");
            cinemas.add(cinemaVO);
        }
        // 根据条件判断影院列表总数
        Integer total = moocCinemaTMapper.selectCount(entityWrapper);
        // 组织返回对象
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemas);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(total);
        return result;
    }

    /**
     * 根据条件获取品牌列表
     *
     * @param brandId
     * @return
     */
    @Override
    public List<BrandVO> getBrands(int brandId) {
        boolean flag = false;
        // 判断brandId是否存在（如果不存在则按照全部查询）
        MoocBrandDictT moocBrandDictT = moocBrandDictTMapper.selectById(brandId);
        // 判断brandId是否等于99
        if (brandId == 99 || moocBrandDictT == null || moocBrandDictT.getUuid() == null) {
            flag = true;
        }
        // 查询所有列表
        List<MoocBrandDictT> moocBrandDictTS = moocBrandDictTMapper.selectList(null);
        List<BrandVO> brandVOS = new ArrayList<>();
        // 判断flag如果为true，则将99设置为isActive
        for (MoocBrandDictT brand : moocBrandDictTS) {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandName(brand.getShowName());
            brandVO.setBrandId(brand.getUuid()+"");
            if (flag) {
                if (brand.getUuid() == 99){
                    brandVO.setActive(true);
                }
            } else {
                if (brand.getUuid() == brandId) {
                    brandVO.setActive(true);
                }
            }
            brandVOS.add(brandVO);
        }
        return brandVOS;
    }

    /**
     * 根据条件获取行政区域列表
     *
     * @param areaId
     * @return
     */
    @Override
    public List<AreaVO> getAreas(int areaId) {
        boolean flag = false;
        // 判断areaId是否存在（如果不存在则按照全部查询）
        MoocAreaDictT moocAreaDictT = moocAreaDictTMapper.selectById(areaId);
        // 判断areaId是否等于99
        if (areaId == 99 || moocAreaDictT == null || moocAreaDictT.getUuid() == null) {
            flag = true;
        }
        // 查询所有列表
        List<MoocAreaDictT>  moocAreaDictTS= moocAreaDictTMapper.selectList(null);
        List<AreaVO> areaVOS = new ArrayList<>();
        // 判断flag如果为true，则将99设置为isActive
        for (MoocAreaDictT area : moocAreaDictTS) {
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaName(area.getShowName());
            areaVO.setAreaId(area.getUuid()+"");
            if (flag) {
                if (area.getUuid() == 99){
                    areaVO.setActive(true);
                }
            } else {
                if (area.getUuid() == areaId) {
                    areaVO.setActive(true);
                }
            }
            areaVOS.add(areaVO);
        }
        return areaVOS;
    }

    /**
     * 获取影厅类型列表
     *
     * @param hallType
     * @return
     */
    @Override
    public List<HallTypeVO> getHallType(int hallType) {
        boolean flag = false;
        // 判断brandId是否存在（如果不存在则按照全部查询）
        MoocHallDictT moocHallDictT = moocHallDictTMapper.selectById(hallType);
        // 判断brandId是否等于99
        if (hallType == 99 || moocHallDictT == null || moocHallDictT.getUuid() == null) {
            flag = true;
        }
        // 查询所有列表
        List<MoocHallDictT> moocHallDictTS = moocHallDictTMapper.selectList(null);
        List<HallTypeVO> hallTypeVOS = new ArrayList<>();
        // 判断flag如果为true，则将99设置为isActive
        for (MoocHallDictT hallDictT : moocHallDictTS) {
            HallTypeVO hallTypeVO = new HallTypeVO();
            hallTypeVO.setHalltypeName(hallDictT.getShowName());
            hallTypeVO.setHalltypeId(hallDictT.getUuid()+"");
            if (flag) {
                if (hallDictT.getUuid() == 99){
                    hallTypeVO.setActive(true);
                }
            } else {
                if (hallDictT.getUuid() == hallType) {
                    hallTypeVO.setActive(true);
                }
            }
            hallTypeVOS.add(hallTypeVO);
        }
        return hallTypeVOS;
    }

    /**
     * 根据影院编号获取影院信息
     *
     * @param cinemaId
     * @return
     */
    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        // 数据实体
        MoocCinemaT moocCinemaT = moocCinemaTMapper.selectById(cinemaId);
        // 数据实体转为业务实体
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setCinemaAddress(moocCinemaT.getCinemaAddress());
        cinemaInfoVO.setImgUrl(moocCinemaT.getImgAddress());
        cinemaInfoVO.setCinemaPhone(moocCinemaT.getCinemaPhone());
        cinemaInfoVO.setCinemaName(moocCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaId(moocCinemaT.getUuid()+"");
        return cinemaInfoVO;
    }

    /**
     * 根据影院编号获取所有电影信息和对应的放映场次信息
     *
     * @param cinemaId
     * @return
     */
    @Override
    public List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId) {
        List<FilmInfoVO> filmInfos = moocFieldTMapper.getFilmInfos(cinemaId);
        return filmInfos;
    }

    /**
     * 根据放映场次id获取放映信息
     *
     * @param fieldId
     * @return
     */
    @Override
    public HallInfoVO getFilmFieldInfo(int fieldId) {
        HallInfoVO hallInfoVO = moocFieldTMapper.getHallInfo(fieldId);
        return hallInfoVO;
    }

    /**
     * 根据放映场次查询播放电影的编号，根据电影编号查询对应电影的信息
     *
     * @param fieldId
     * @return
     */
    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        FilmInfoVO filmInfoVO = moocFieldTMapper.getFilmInfoById(fieldId);
        return filmInfoVO;
    }

    /**
     * 订单模块所需信息
     *
     * @param fieldId
     * @return
     */
    @Override
    public OrderQueryVO getOrderNeeds(int fieldId) {
        OrderQueryVO orderQueryVO = new OrderQueryVO();
        MoocFieldT moocFieldT = moocFieldTMapper.selectById(fieldId);
        orderQueryVO.setCinemaId(moocFieldT.getCinemaId()+"");
        orderQueryVO.setFilmPrice(moocFieldT.getPrice()+"");
        return orderQueryVO;
    }
}
