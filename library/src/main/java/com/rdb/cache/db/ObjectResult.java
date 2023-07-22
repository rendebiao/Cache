package com.rdb.cache.db;

public class ObjectResult<T> extends BaseResult {

    private T value;

    public ObjectResult(long time, long deadline, T value) {
        this.time = time;
        this.deadline = deadline;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ObjectResult<?>) {
            return this.value.equals(((ObjectResult<?>) o).value);
        }
        return false;
    }
}
