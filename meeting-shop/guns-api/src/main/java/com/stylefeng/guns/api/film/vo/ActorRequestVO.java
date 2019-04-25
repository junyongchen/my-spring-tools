package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author junyong.chen
 * @date 2019/4/9 16:01
 * @description
 */
@Data
public class ActorRequestVO implements Serializable {
    private ActorVO director;
    private List<ActorVO> actors;
}
