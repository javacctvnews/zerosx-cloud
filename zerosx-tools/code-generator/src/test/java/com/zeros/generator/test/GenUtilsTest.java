package com.zeros.generator.test;

import com.zerosx.generator.utils.GenUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @ClassName GenUtilsTest
 * @Description 单元测试
 * @Author javacctvnews
 * @Date 2023/4/7 11:19
 * @Version 1.0
 */
public class GenUtilsTest {


    @Test
    public void testTableToJava() {
        String javaName = GenUtils.tableToJava("t_event_message", "t_");
        Assertions.assertThat(javaName).isEqualTo("EventMessage");
    }

}
