package com.zerosx.sas.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zerosx.common.base.vo.OauthClientDetailsBO;
import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.sas.dto.Oauth2RegisteredClientDTO;
import com.zerosx.sas.dto.Oauth2RegisteredClientPageDTO;
import com.zerosx.sas.entity.Oauth2RegisteredClient;
import com.zerosx.sas.vo.Oauth2RegisteredClientPageVO;
import com.zerosx.sas.vo.Oauth2RegisteredClientVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public interface IOauth2RegisteredClientService extends IService<Oauth2RegisteredClient> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<Oauth2RegisteredClientPageVO> listPage(RequestVO<Oauth2RegisteredClientPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     *
     * @param query
     * @return
     */
    List<Oauth2RegisteredClient> dataList(Oauth2RegisteredClientPageDTO query);

    /**
     * 保存客户端
     *
     * @param oauth2RegisteredClientDTO
     * @return
     */
    boolean saveOauthClient(Oauth2RegisteredClientDTO oauth2RegisteredClientDTO);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Oauth2RegisteredClientVO queryById(String id);

    /**
     * 删除client
     *
     * @param id
     * @return
     */
    boolean deleteRecord(String[] id);

    /**
     * 应用下拉框
     *
     * @return
     */
    List<SelectOptionVO> selectList();

    /**
     * 按clientId查询
     *
     * @param clientId
     * @return
     */
    OauthClientDetailsBO getClient(String clientId);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<Oauth2RegisteredClientPageDTO> requestVO, HttpServletResponse response);

    /**
     * 修改密码
     *
     * @param oauth2RegisteredClientDTO
     * @return
     */
    boolean editOauthClientPwd(Oauth2RegisteredClientDTO oauth2RegisteredClientDTO);

}
