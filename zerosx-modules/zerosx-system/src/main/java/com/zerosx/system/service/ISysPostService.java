package com.zerosx.system.service;

import com.zerosx.common.base.vo.RequestVO;
import com.zerosx.common.core.service.ISuperService;
import com.zerosx.common.core.vo.CustomPageVO;
import com.zerosx.system.dto.SysPostDTO;
import com.zerosx.system.dto.SysPostPageDTO;
import com.zerosx.system.entity.SysPost;
import com.zerosx.system.vo.SysPostPageVO;
import com.zerosx.system.vo.SysPostVO;

import java.util.List;

/**
 * 岗位管理
 *
 * @author javacctvnews
 * @Description
 * @date 2023-07-28 15:40:01
 */
public interface ISysPostService extends ISuperService<SysPost> {

    /**
     * 分页查询
     *
     * @param requestVO
     * @return
     */
    CustomPageVO<SysPostPageVO> pageList(RequestVO<SysPostPageDTO> requestVO, boolean searchCount);

    /**
     * 分页查询的data
     *
     * @param query
     * @return
     */
    List<SysPost> dataList(SysPostPageDTO query);

    /**
     * 新增
     *
     * @param sysPostDTO
     * @return
     */
    boolean add(SysPostDTO sysPostDTO);

    /**
     * 编辑
     *
     * @param sysPostDTO
     * @return
     */
    boolean update(SysPostDTO sysPostDTO);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SysPostVO queryById(Long id);

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    boolean deleteRecord(Long[] ids);

    List<SysPost> queryUserPosts(Long userId);
}

