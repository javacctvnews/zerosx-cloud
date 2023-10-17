package com.zerosx.system.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.base.vo.SelectOptionVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.MutiTenancyGroupEditDTO;
import com.zerosx.system.dto.MutiTenancyGroupQueryDTO;
import com.zerosx.system.dto.MutiTenancyGroupSaveDTO;
import com.zerosx.system.entity.MutiTenancyGroup;
import com.zerosx.system.vo.MutiTenancyGroupPageVO;
import com.zerosx.system.vo.MutiTenancyGroupVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName IMutiTenancyGroupService
 * @Description 租户集团公司接口
 * @Author javacctvnews
 * @Date 2023/3/13 10:45
 * @Version 1.0
 */
public interface IMutiTenancyGroupService extends ISuperService<MutiTenancyGroup> {

    /**
     * 分页
     * @param requestVO
     * @return
     */
    CustomPageVO<MutiTenancyGroupPageVO> listPages(RequestVO<MutiTenancyGroupQueryDTO> requestVO, boolean searchCount);

    /**
     * @MethodName saveMutiTenancyGroup
     * @Description  租户集团公司保存
     * @param mutiTenancyGroupSaveDTO
     * @return boolean
     * @Author javacctvnews
     * @Date 2023/3/13 10:55
     */
    boolean saveMutiTenancyGroup(MutiTenancyGroupSaveDTO mutiTenancyGroupSaveDTO);
    /**
     * @MethodName editMutiTenancyGroup
     * @Description  租户集团更新
     * @param mutiTenancyGroupEditDTO
     * @return boolean
     * @Author javacctvnews
     * @Date 2023/3/13 11:39
     */
    boolean editMutiTenancyGroup(MutiTenancyGroupEditDTO mutiTenancyGroupEditDTO);

    /**
     * 租户集团下拉选择框数据
     * @return
     */
    List<SelectOptionVO> selectOptions();

    MutiTenancyGroupVO getTenantById(Long id);

    boolean deleteGroupCompany(Long[] ids);

    List<MutiTenancyGroup> listData(MutiTenancyGroupQueryDTO query);

    String transIdName(String operatorId);

    /**
     * 导出Excel
     *
     * @param requestVO requestVO
     * @param response  response
     */
    void excelExport(RequestVO<MutiTenancyGroupQueryDTO> requestVO, HttpServletResponse response);
}
