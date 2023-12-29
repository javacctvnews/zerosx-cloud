package com.zerosx.system.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zerosx.common.core.utils.EasyTransUtils;
import com.zerosx.common.utils.BeanCopierUtils;
import com.zerosx.common.utils.JacksonUtil;
import com.zerosx.system.SystemApplication;
import com.zerosx.system.entity.SysUser;
import com.zerosx.system.entity.SystemOperatorLog;
import com.zerosx.system.mapper.ISysUserMapper;
import com.zerosx.system.service.ISystemOperatorLogService;
import com.zerosx.system.vo.SystemOperatorLogPageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@SpringBootTest(classes = SystemApplication.class)
public class TestCase {

    @Autowired
    private ISysUserMapper sysUserMapper;
    @Autowired
    private ISystemOperatorLogService systemOperatorLogService;
    @Resource
    private ThreadPoolExecutor messageConsumeDynamicExecutor;
    @Resource
    private ThreadPoolExecutor messageProduceDynamicExecutor;

    @Test
    public void Test011() throws Exception {
        int a = 100;
        for (int i = 0; i < a; i++) {
            int as = i;
            messageConsumeDynamicExecutor.execute(() -> {
                log.debug("messageConsumeDynamicExecutor:{}", as);
            });
        }
        for (int i = 0; i < a; i++) {
            int as = i;
            messageProduceDynamicExecutor.execute(() -> {
                log.debug("messageProduceDynamicExecutor:{}", as);
            });
        }
    }


    @Test
    public void testCopy1() {

        List<SystemOperatorLog> res = new ArrayList<>();
        List<SystemOperatorLog> list = systemOperatorLogService.list();
        int loop = 5;
        for (int i = 0; i < loop; i++) {
            res.addAll(list);
        }

        long t1 = System.currentTimeMillis();
        List<SystemOperatorLogPageVO> vos = BeanCopierUtils.copyProperties(res, SystemOperatorLogPageVO.class);
        long t2 = System.currentTimeMillis();
        System.out.println("拷贝条数：" + res.size() + "，耗时 = " + (t2 - t1));

        long t21 = System.currentTimeMillis();
        EasyTransUtils.easyTrans(vos);
        long t22 = System.currentTimeMillis();
        System.out.println("翻译条数：" + res.size() + "，耗时 = " + (t22 - t21));

        // 方法1: 如果写到同一个sheet
        long t31 = System.currentTimeMillis();
        String fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, SystemOperatorLogPageVO.class).sheet("模板").doWrite(vos);
        long t32 = System.currentTimeMillis();
        System.out.println("导出条数：" + res.size() + "，耗时 = " + (t32 - t31));
    }

    /**
     * 最简单的写
     * <p>1. 创建excel对应的实体对象
     * <p>2. 直接写即可
     */
    @Test
    public void simpleWrite() {
        long t1 = System.currentTimeMillis();
        LambdaQueryWrapper<SystemOperatorLog> pageQw = Wrappers.lambdaQuery(SystemOperatorLog.class);
        pageQw.eq(SystemOperatorLog::getOperatorId, "423956");
        long totalNum = systemOperatorLogService.count(pageQw);
        int sheetNum = 50000;

        // 方法1: 如果写到同一个sheet
        String fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, SystemOperatorLogPageVO.class).build()) {
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            int loopNum = (int) Math.ceil((double) totalNum / sheetNum);
            int startIndex = 0;
            for (int i = 0; i < loopNum; i++) {
                if (startIndex > totalNum) {
                    break;
                }
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + (i + 1)).build();
                Page<SystemOperatorLog> page = new Page<>(i + 1, sheetNum, false);
                Page<SystemOperatorLog> pageData = systemOperatorLogService.getBaseMapper().selectPage(page, pageQw);
                List<SystemOperatorLog> records = pageData.getRecords();
                List<SystemOperatorLogPageVO> vos = EasyTransUtils.copyTrans(records, SystemOperatorLogPageVO.class);
                excelWriter.write(vos, writeSheet);
                startIndex += sheetNum;
            }
        }
        System.out.println("导出条数：" + totalNum + "，耗时 = " + (System.currentTimeMillis() - t1));
    }


    @Test
    public void test01() {
        String userName = "user01";
        String phone = "19829929293";
        SysUser sysUser = sysUserMapper.selectLoginSysUser(userName, phone);
        log.debug("查询结果:{}", JacksonUtil.toJSONString(sysUser));
    }

    @Test
    public void testCopy() {

        List<SystemOperatorLog> res = new ArrayList<>();
        List<SystemOperatorLog> list = systemOperatorLogService.list();
        int loop = 1000;
        for (int i = 0; i < loop; i++) {
            res.addAll(list);
        }

        long t1 = System.currentTimeMillis();
        List<SystemOperatorLogPageVO> vos = BeanCopierUtils.copyProperties(res, SystemOperatorLogPageVO.class);
        long t2 = System.currentTimeMillis();
        System.out.println("拷贝条数：" + res.size() + "，耗时 = " + (t2 - t1));

        long t21 = System.currentTimeMillis();
        EasyTransUtils.easyTrans(vos);
        long t22 = System.currentTimeMillis();
        System.out.println("翻译条数：" + res.size() + "，耗时 = " + (t22 - t21));
    }

}
