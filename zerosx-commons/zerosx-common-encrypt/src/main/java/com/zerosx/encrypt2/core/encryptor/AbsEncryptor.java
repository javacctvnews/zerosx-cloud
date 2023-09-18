package com.zerosx.encrypt2.core.encryptor;

public abstract class AbsEncryptor implements IEncryptor {

    @Override
    public String encrypt(String content) {
        return content;
    }

    @Override
    public String decrypt(String content) {
        return content;
    }

}
