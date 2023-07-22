package com.rdb.cache.db;

import java.util.List;

public class ObjectListResult<T> extends BaseResult {

    private List<T> values;

    public ObjectListResult(long time, long deadline, List<T> values) {
        this.time = time;
        this.deadline = deadline;
        this.values = values;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ObjectListResult<?>) {
            return this.values.equals(((ObjectListResult<?>) o).values);
        }
        return false;
    }
}
