package com.example.stefan.manifesto.utils;

public class ResponseMessage<T> {

    private boolean success;
    private String message;
    private T responseBody;


    public ResponseMessage(boolean success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public ResponseMessage(boolean success, String message, T responseBody) {
        super();
        this.success = success;
        this.message = message;
        this.responseBody = responseBody;
    }


    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }

    public static <T> ResponseMessage<T> error() {
        return new ResponseMessage<>(false, "Some error happend");
    }

    public static <T> ResponseMessage<T> success() {
        return new ResponseMessage<>(true, "Operation successfull");
    }

}
