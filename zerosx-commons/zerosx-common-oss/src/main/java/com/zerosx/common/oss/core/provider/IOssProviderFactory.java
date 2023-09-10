package com.zerosx.common.oss.core.provider;

import com.zerosx.common.oss.core.client.IOssClientService;

/**
 * oss实例建造者
 * @param <T>
 */
public interface IOssProviderFactory<T> {

    IOssClientService createClient(T ossConfig);

    IOssClientService refreshClient(T ossConfig);

}
