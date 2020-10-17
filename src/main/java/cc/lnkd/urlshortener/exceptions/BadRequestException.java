package cc.lnkd.urlshortener.exceptions;

import java.io.IOException;

public class BadRequestException extends  IOException {
    private static final long serialVersionUID = 1L;

    private boolean status;
    private String message;
    private Object data;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

    public BadRequestException(boolean status, String message, Object data) {
        super(message);
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
