package com.zerosx.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Slf4j
public class AntPathMatcherUtils {

    private final static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static boolean matchUrl(String matchPath, List<String> pathList){
        for (String path : pathList) {
            boolean match = antPathMatcher.match(path, matchPath);
            //log.debug("{} {} {}", match, path, matchPath);
            if(match){
                return true;
            }
        }
        return false;
    }
}
