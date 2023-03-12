package com.xuyao.chat.bean.vo;

import java.util.List;

public class Results {
    private List<Long> ids;
    private Status status;

    public Results() {

    }
    public Results(List<Long> ids, Status status) {
        this.ids = ids;
        this.status = status;
    }

    public List<Long> getIds() {
        return ids;
    }

    public Status getStatus() {
        return status;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Results{");
        sb.append("ids=").append(ids);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
