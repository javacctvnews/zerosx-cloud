package com.zerosx.system.controller;

import com.zerosx.api.resource.IDGenClient;
import com.zerosx.common.base.utils.ResultVOUtil;
import com.zerosx.common.base.vo.ResultVO;
import com.zerosx.common.core.utils.LeafUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "分布式ID(分段式)测试")
public class LeafTestController {

    @Autowired
    private IDGenClient idGenClient;

    @Operation(summary = "创建分布式ID(分段式)-http")
    @GetMapping(value = "/segment_test/{bizTag}")
    public ResultVO<Long> segment1(@PathVariable("bizTag") String bizTag) {
        return idGenClient.segment(bizTag);
    }

    @Operation(summary = "创建分布式ID(分段式)-工具类")
    @GetMapping(value = "/segment_test2/{bizTag}")
    public ResultVO<Long> segment2(@PathVariable("bizTag") String bizTag) {
        Long uid = LeafUtils.uid(bizTag);
        System.out.println(uid);
        return ResultVOUtil.success(uid);
    }

}
