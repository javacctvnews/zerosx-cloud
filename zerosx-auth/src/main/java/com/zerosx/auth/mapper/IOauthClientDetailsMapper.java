package com.zerosx.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerosx.auth.entity.OauthClientDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOauthClientDetailsMapper extends BaseMapper<OauthClientDetails> {

}
