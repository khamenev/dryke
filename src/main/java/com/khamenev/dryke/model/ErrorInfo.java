package com.khamenev.dryke.model;

public class ErrorInfo {
    private String message;
    private String code;
    private long timestamp;

    public ErrorInfo() {
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorInfo(String message) {
        this();
        this.message = message;
    }

    public ErrorInfo(String message, String code) {
        this(message);
        this.code = code;
    }

    // Standard getters and setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
