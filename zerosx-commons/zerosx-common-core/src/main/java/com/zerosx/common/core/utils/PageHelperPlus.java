//package com.zerosx.common.core.utils;
//
//import com.github.pagehelper.Page;
//import com.zerosx.common.core.vo.CustomPageVO;
//import com.zerosx.common.core.service.PageInfoConvertor;
//import org.apache.commons.collections4.CollectionUtils;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @ClassName PageHelperUtil
// * @Description 处理场景：controller层返回的 PageInfo 泛型类型通常会定义为 VO 等类型。
// * 但是在 mapper 层，返回的 PageInfo 对象通常是 model 类型。
// * @Author javacctvnews
// * @Date 2023/5/12 22:32
// * @Version 1.0
// */
//public class PageHelperPlus {
//
//    /**
//     * convertPageInfo 转换 PageInfo 泛型类型
//     *
//     * @param sourcePageInfo 原始的 pageInfo 对象
//     * @param convertor      自定义转换器
//     * @param <T>            原始类型
//     * @param <R>            目标类型
//     * @return 转换之后的 PageInfo 对象
//     */
//    public static <T, R> PageInfo<R> convertPageInfo(PageInfo<T> sourcePageInfo, PageInfoConvertor<T, R> convertor) {
//        long total = sourcePageInfo.getTotal();
//        if (total <= 0) {
//            return PageInfo.emptyPageInfo();
//        }
//        Page<R> targetPage = new Page<>(sourcePageInfo.getPageNum(), sourcePageInfo.getPageSize());
//        targetPage.setTotal(total);
//        PageInfo<R> targetPageInfo = new PageInfo<>(targetPage);
//        List<R> targetList = sourcePageInfo.getList().stream().map(convertor::convert).collect(Collectors.toList());
//        targetPageInfo.setList(targetList);
//        targetPageInfo.setStartRow(sourcePageInfo.getStartRow());
//        targetPageInfo.setEndRow(sourcePageInfo.getEndRow());
//        targetPageInfo.setSize(targetList.size());
//        return targetPageInfo;
//    }
//
//    /**
//     * convertPageInfo 转换 PageInfo 泛型类型
//     *
//     * @param sourcePageInfo 原始的 pageInfo 对象
//     * @param targetClazz    需要转换的 PageInfo 泛型类型
//     * @param <T>            原始类型
//     * @param <R>            目标类型
//     * @return 转换之后的 PageInfo 对象
//     */
//    public static <T, R> PageInfo<R> convertPageInfo(PageInfo<T> sourcePageInfo, Class<R> targetClazz) {
//        //空数据时的返回
//        if (emptyPageInfoList(sourcePageInfo)) {
//            return PageInfo.emptyPageInfo();
//        }
//        //数据不为空
//        Page<R> page = new Page<>(sourcePageInfo.getPageNum(), sourcePageInfo.getPageSize());
//        page.setTotal(sourcePageInfo.getTotal());
//
//        PageInfo<R> targetPageInfo = new PageInfo<>(page);
//        List<R> targetList = sourcePageInfo.getList().stream().map(e -> {
//            try {
//                R r = targetClazz.getDeclaredConstructor().newInstance();
//                BeanCopierUtil.copyProperties(e, r);
//                return r;
//            } catch (Exception exception) {
//                throw new RuntimeException("convertPageInfo cause exception", exception);
//            }
//        }).collect(Collectors.toList());
//        targetPageInfo.setList(targetList);
//        targetPageInfo.setStartRow(sourcePageInfo.getStartRow());
//        targetPageInfo.setEndRow(sourcePageInfo.getEndRow());
//        targetPageInfo.setSize(targetList.size());
//        return targetPageInfo;
//    }
//
//    private static <T, R> boolean emptyPageInfoList(PageInfo<T> sourcePageInfo) {
//        //空数据时的返回
//        List<T> pageData = sourcePageInfo.getList();
//        return CollectionUtils.isEmpty(pageData);
//    }
//
//}
