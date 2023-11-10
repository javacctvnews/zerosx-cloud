package com.zerosx.system.test;

import com.zerosx.common.utils.DateUtils2;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestFileUtil {

    @Test
    public void test12(){
        long time1 = Timestamp.valueOf(LocalDateTime.now()).getTime();
        Date date2 = DateUtils2.dateMax(1000);
        long time2 = date2.getTime();
        System.out.println("time1 = " + time1);
        System.out.println("time2 = " + time2);
    }

    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    public static String getPath() {
        return TestFileUtil.class.getResource("/").getPath();
    }

    public static TestPathBuild pathBuild() {
        return new TestPathBuild();
    }

    public static File createNewFile(String pathName) {
        File file = new File(getPath() + pathName);
        if (file.exists()) {
            file.delete();
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    public static File readFile(String pathName) {
        return new File(getPath() + pathName);
    }

    public static File readUserHomeFile(String pathName) {
        return new File(System.getProperty("user.home") + File.separator + pathName);
    }

    /**
     * build to test file path
     **/
    public static class TestPathBuild {
        private TestPathBuild() {
            subPath = new ArrayList<>();
        }

        private final List<String> subPath;

        public TestPathBuild sub(String dirOrFile) {
            subPath.add(dirOrFile);
            return this;
        }

        public String getPath() {
            if (CollectionUtils.isEmpty(subPath)) {
                return TestFileUtil.class.getResource("/").getPath();
            }
            if (subPath.size() == 1) {
                return TestFileUtil.class.getResource("/").getPath() + subPath.get(0);
            }
            StringBuilder path = new StringBuilder(TestFileUtil.class.getResource("/").getPath());
            path.append(subPath.get(0));
            for (int i = 1; i < subPath.size(); i++) {
                path.append(File.separator).append(subPath.get(i));
            }
            return path.toString();
        }

    }

}
