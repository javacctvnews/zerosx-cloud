package com.zerosx.common.core.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.vo.CustomPageVO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtils {

    /**
     * Page对象（当前页、列排序）
     *
     * @param requestVO
     * @param searchCount true:分页；false:导出（不分页）
     * @param <T>
     * @return
     */
    public static <T> Page<T> of(RequestVO requestVO, boolean searchCount) {
        Integer pageNum = requestVO.getPageNum();
        Integer pageSize = requestVO.getPageSize();
        if (!searchCount) {
            pageNum = 1;
            pageSize = Integer.MAX_VALUE;
        }
        Page<T> page = new Page<>(pageNum, pageSize, searchCount);
        if (!searchCount) {
            page.setMaxLimit(-1L);
        }
        List<RequestVO.SortVO> sortList = requestVO.getSortList();
        if (CollectionUtils.isNotEmpty(sortList)) {
            List<OrderItem> orderItems = new ArrayList<>();
            OrderItem orderItem;
            for (RequestVO.SortVO sortVO : sortList) {
                //转数据库列（驼峰）
                String sqlColumn = StrUtil.toUnderlineCase(sortVO.getOrderByColumn());
                //排序
                String sortType = sortVO.getSortType();
                boolean isAsc;
                if ("ascending".equals(sortType)) {
                    isAsc = true;
                } else if ("descending".equals(sortType)) {
                    isAsc = false;
                } else {
                    //throw new BusinessException("排序参数有误");
                    continue;
                }
                orderItems.add(new OrderItem(sqlColumn, isAsc));
            }
            page.addOrder(orderItems);
        }
        return page;
    }

    /**
     * 分页对象
     *
     * @param page
     * @param <T>
     * @return
     */
    public static <T> CustomPageVO<T> of(IPage<T> page) {
        CustomPageVO<T> rspData = new CustomPageVO<>();
        rspData.setList(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    /**
     * 分页对象
     *
     * @param page
     * @param <R>
     * @return
     */
    public static <T, R> CustomPageVO<R> of(IPage<T> page, Class<R> clz) {
        CustomPageVO<R> rspData = new CustomPageVO<>();
        rspData.setList(BeanCopierUtil.copyPropertiesOfList(page.getRecords(), clz));
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    /**
     * 分页对象
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> CustomPageVO<T> of(List<T> list) {
        return of(CollectionUtils.isEmpty(list) ? 0 : list.size(), list);
    }

    /**
     * 分页对象
     *
     * @param total 总数
     * @param list  分页数据
     * @param <T>
     * @return
     */
    public static <T> CustomPageVO<T> of(long total, List<T> list) {
        CustomPageVO<T> rspData = new CustomPageVO<>();
        rspData.setList(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * IPage<T> 转换 CustomPageVO<R></> 泛型类型
     *
     * @param page
     * @param targetClazz
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> CustomPageVO<R> convertPage(IPage<T> page, Class<R> targetClazz) {
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return CustomPageVO.EMPTY_PAGE;
        }
        List<R> targetList = page.getRecords().stream().map(e -> {
            try {
                R r = targetClazz.getDeclaredConstructor().newInstance();
                BeanCopierUtil.copyProperties(e, r);
                return r;
            } catch (Exception exception) {
                throw new RuntimeException("convertPage cause exception", exception);
            }
        }).collect(Collectors.toList());
        CustomPageVO<R> customPageVO = new CustomPageVO<>();
        customPageVO.setList(targetList);
        customPageVO.setTotal(page.getTotal());
        return customPageVO;
    }

    /**
     * @param page
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> CustomPageVO<R> convertPage(IPage<T> page, Function<? super T, ? extends R> mapper) {
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return CustomPageVO.EMPTY_PAGE;
        }
        CustomPageVO<R> customPageVO = new CustomPageVO<>();
        customPageVO.setList(page.getRecords().stream().map(mapper).collect(Collectors.toList()));
        customPageVO.setTotal(page.getTotal());
        return customPageVO;
    }

}
