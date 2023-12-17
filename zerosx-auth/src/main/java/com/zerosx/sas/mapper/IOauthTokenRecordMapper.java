package com.zerosx.sas.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.sas.entity.OauthTokenRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志
 * 
 * @author javacctvnews
 * @date 2023-04-09 15:33:32
 */
@Mapper
public interface IOauthTokenRecordMapper extends SuperMapper<OauthTokenRecord> {

}
