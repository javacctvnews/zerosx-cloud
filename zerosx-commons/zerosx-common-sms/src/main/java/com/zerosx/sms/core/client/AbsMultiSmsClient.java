package com.zerosx.sms.core.client;

import org.springframework.web.client.RestTemplate;

/**
 * AbsMultiSmsClient
 * <p>
 *
 * @author: javacctvnews
 * @create: 2023-08-30 14:19
 **/
public abstract class AbsMultiSmsClient implements IMultiSmsClient {

    protected RestTemplate restTemplate;

}
