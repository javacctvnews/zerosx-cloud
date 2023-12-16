package com.zerosx.resource.core.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IDGenRes {

    private long id;

    private IDGenResStatus idGenResStatus;

    public IDGenRes() {
    }

    public IDGenRes(long id, IDGenResStatus IDGenResStatus) {
        this.id = id;
        this.idGenResStatus = IDGenResStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("id=").append(id);
        sb.append(", status=").append(idGenResStatus);
        sb.append('}');
        return sb.toString();
    }
}
