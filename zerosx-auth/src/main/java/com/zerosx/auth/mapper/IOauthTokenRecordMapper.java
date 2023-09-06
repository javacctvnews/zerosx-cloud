package com.zerosx.auth.mapper;

import com.zerosx.auth.entity.OauthTokenRecord;
import com.zerosx.common.core.service.SuperMapper;
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
