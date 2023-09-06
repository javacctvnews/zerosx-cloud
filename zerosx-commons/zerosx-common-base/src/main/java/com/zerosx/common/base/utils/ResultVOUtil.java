package com.zerosx.common.base.utils;


import com.zerosx.common.base.enums.ResultEnum;
import com.zerosx.common.base.vo.ResultVO;

public class ResultVOUtil {

    /**
     * 请求成功
     */
    public static ResultVO<?> success() {
        return success(null);
    }

    /**
     * 请求成功
     */
    public static <T> ResultVO<T> success(T object) {
        return new ResultVO<>(ResultEnum.SUCCESS, object);
    }

    /**
     * 增、删、改返回结果封装
     *
     * @param row 修改的记录数
     * @return ResultVO
     */
    public static ResultVO<?> successCUD(int row) {
        return row > 0 ? success() : error(ResultEnum.FAIL);
    }

    /**
     * boolean结果
     *
     * @param row
     * @return
     */
    public static ResultVO<?> successBoolean(boolean row) {
        return row ? success() : error(ResultEnum.FAIL);
    }

    /**
     * 请求失败
     *
     * @param errorCode
     * @param errorMsg
     * @return
     */
    public static <T> ResultVO<T> error(Integer errorCode, String errorMsg) {
        return new ResultVO<>(errorCode, errorMsg, null);
    }

    /**
     * 请求失败
     *
     * @param errorMsg
     * @return
     */
    public static ResultVO<?> error(String errorMsg) {
        return error(ResultEnum.FAIL.getCode(), errorMsg);
    }

    /**
     * 请求失败
     *
     * @param resultEnum
     * @return
     */
    public static ResultVO<?> error(ResultEnum resultEnum) {
        return error(resultEnum.getCode(), resultEnum.getMessage());
    }

    /**
     * data为空
     *
     * @return
     */
    public static <T> ResultVO<T> emptyData() {
        return feignFail("data为空");
    }

    public static <T> ResultVO<T> feignFail(String errMsg) {
        return new ResultVO<>(ResultEnum.FAIL.getCode(), errMsg);
    }
}
