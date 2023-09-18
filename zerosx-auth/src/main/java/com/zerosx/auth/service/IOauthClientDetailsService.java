package com.zerosx.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.auth.dto.OauthClientDetailsDTO;
import com.zerosx.auth.dto.OauthClientDetailsPageDTO;
import com.zerosx.auth.entity.OauthClientDetails;
import com.zerosx.auth.vo.OauthClientDetailsPageVO;
import com.zerosx.auth.vo.OauthClientDetailsVO;
import com.zerosx.auth.vo.TokenQueryVO;
import com.zerosx.auth.vo.TokenVO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.vo.CustomPageVO;

import java.util.List;


public interface IOauthClientDetailsService extends IService<OauthClientDetails> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<OauthClientDetailsPageVO> listPage(RequestVO<OauthClientDetailsPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     *
     * @param query
     * @return
     */
    List<OauthClientDetails> dataList(OauthClientDetailsPageDTO query);

    /**
     * 保存客户端
     *
     * @param oauthClientDetailsDTO
     * @return
     */
    boolean saveOauthClient(OauthClientDetailsDTO oauthClientDetailsDTO);

    /**
     * 编辑客户端
     *
     * @param oauthClientDetailsEditDTO
     * @return
     */
    boolean editOauthClient(OauthClientDetailsDTO oauthClientDetailsEditDTO);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    OauthClientDetailsVO queryById(Long id);

    /**
     * 删除client
     *
     * @param id
     * @return
     */
    boolean deleteRecord(Long[] id);

    /**
     * 应用下拉框
     *
     * @return
     */
    List<SelectOptionVO> selectList();

    /**
     * 退出登录
     *
     * @param tokenQueryVO
     * @return
     */
    boolean logout(TokenQueryVO tokenQueryVO);

    /**
     * token令牌管理 分页
     *
     * @param requestVO
     * @param searchCount
     * @return
     */
    CustomPageVO<TokenVO> pageList(RequestVO<TokenQueryVO> requestVO, boolean searchCount);

    /**
     * 清理token令牌
     *
     * @param tokenQueryVO
     * @return
     */
    boolean cleanTokenData(TokenQueryVO tokenQueryVO);

    /**
     * 按clientId查询
     * @param clientId
     * @return
     */
    OauthClientDetailsBO getClient(String clientId);

}
