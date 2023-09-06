package com.zerosx.common.core.utils;

import org.springframework.util.AntPathMatcher;

import java.util.List;

public class AntPathMatcherUtils {

    private final static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static boolean matchUrl(String matchPath, List<String> pathList){
        for (String path : pathList) {
            boolean match = antPathMatcher.match(path, matchPath);
            if(match){
                return true;
            }
        }
        return false;
    }
}
