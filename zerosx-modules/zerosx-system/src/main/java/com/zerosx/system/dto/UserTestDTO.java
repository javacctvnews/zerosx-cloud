package com.zerosx.system.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * UserTestDTO
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-12-26 15:47
 **/
@Setter
@Getter
public class UserTestDTO {

    private Integer num;

    private String prefix;

    private long delayTime;

    private String operatorId = "000000";

}
