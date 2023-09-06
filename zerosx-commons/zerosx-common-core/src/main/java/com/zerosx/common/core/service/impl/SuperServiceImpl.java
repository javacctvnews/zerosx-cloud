package com.zerosx.common.core.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerosx.common.core.service.ISuperService;

/**
 * service实现父类
 *
 * @param <M>
 * @param <T>
 */
public class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ISuperService<T> {


}
