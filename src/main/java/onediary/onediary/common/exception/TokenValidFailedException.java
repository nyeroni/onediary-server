package onediary.onediary.common.exception;

public class TokenValidFailedException extends RuntimeException{

    public TokenValidFailedException(String message) {
        super(message);
    }

    public TokenValidFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
