package com.rdb.cache.db;

import java.io.Serializable;

public class BaseResult implements Serializable {

    protected long time;
    protected long deadline;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
