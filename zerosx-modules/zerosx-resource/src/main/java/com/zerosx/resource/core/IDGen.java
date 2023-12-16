package com.zerosx.resource.core;


import com.zerosx.resource.core.common.IDGenRes;

public interface IDGen {

    Long idLong(String key);

    String idStr(String key);

    IDGenRes get(final String key);

    boolean init();

}
