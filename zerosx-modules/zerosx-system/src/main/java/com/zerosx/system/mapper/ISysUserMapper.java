package com.zerosx.system.mapper;

import com.zerosx.common.core.service.SuperMapper;
import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.anno.EncryptFields;
import com.zerosx.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户
 *
 * @author javacctvnews
 * @date 2023-07-20 13:48:04
 */
@Mapper
public interface ISysUserMapper extends SuperMapper<SysUser> {

    @EncryptFields({@EncryptField(field = "phoneNumber")})
    SysUser selectLoginSysUser(@Param("userName") String userName, @Param("phoneNumber") String phoneNumber);

}
