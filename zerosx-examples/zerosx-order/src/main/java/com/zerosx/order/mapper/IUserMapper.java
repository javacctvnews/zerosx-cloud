package com.zerosx.order.mapper;

import com.zerosx.encrypt2.anno.EncryptField;
import com.zerosx.encrypt2.anno.EncryptFields;
import com.zerosx.encrypt2.core.encryptor.AesEncryptor;
import com.zerosx.encrypt2.core.enums.EncryptMode;
import com.zerosx.order.dto.UserDTO;
import com.zerosx.order.entity.User;
import com.zerosx.common.core.service.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 加解密DEMO
 *
 * @author javacctvnews
 * @date 2023-09-22 15:32:00
 */
@Mapper
public interface IUserMapper extends SuperMapper<User> {
    //入参是对象类型
    List<User> queryList(UserDTO userDTO);

    //入参是数据或集合类型
    @EncryptField(mode = EncryptMode.ENC)
    List<User> queryByPhones(List<String> phones);

    //入参是数据或集合类型
    @EncryptField(mode = EncryptMode.ENC)
    List<User> queryByPhoneArr(String[] phones);

    //查询结果是集合
    @EncryptField(mode = EncryptMode.DEC)
    List<String> queryPhones();

    //入参是Map
    @EncryptFields({@EncryptField(field = "phone"),
            @EncryptField(field = "cardId"),
            @EncryptField(field = "email", algo = AesEncryptor.class)})
    List<User> queryByMap(Map<String, String> map);
}
