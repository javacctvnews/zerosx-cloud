package com.zerosx.common.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SysPermissionBO
 * @Description 用户的权限集合
 * @Author javacctvnews
 * @Date 2023/3/24 17:09
 * @Version 1.0
 */
@Setter
@Getter
public class SysPermissionBO implements Serializable {

    private static final long serialVersionUID = 7776762995233371515L;

    private List<SysMenuBO> permissionUrls = new ArrayList<>();

}
