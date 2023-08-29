package com.khamenev.dryke.model;

public class ApiResponse<T> {
    private T data;
    private ErrorInfo error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }
}
