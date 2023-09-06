package com.zerosx.system.mapper;

import com.zerosx.system.entity.SysPost;
import com.zerosx.common.core.service.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 岗位管理
 * 
 * @author javacctvnews
 * @date 2023-07-28 15:40:01
 */
@Mapper
public interface ISysPostMapper extends SuperMapper<SysPost> {

    List<SysPost> queryUserPosts(Long userId);

}
