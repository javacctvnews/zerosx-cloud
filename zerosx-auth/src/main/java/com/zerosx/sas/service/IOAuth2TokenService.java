package com.zerosx.sas.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.sas.vo.TokenQueryVO;
import com.zerosx.sas.vo.TokenVO;
import jakarta.servlet.http.HttpServletResponse;

public interface IOAuth2TokenService {

    /**
     * token令牌管理 分页
     *
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<TokenVO> pageList(RequestVO<TokenQueryVO> requestVO, boolean searchCount);

    /**
     * 登出
     *
     * @param tokenQueryVO
     * @return
     */
    boolean logout(TokenQueryVO tokenQueryVO);

    /**
     * 清理token
     *
     * @param tokenQueryVO
     * @return
     */
    boolean cleanTokenData(TokenQueryVO tokenQueryVO);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<TokenQueryVO> requestVO, HttpServletResponse response);
}
