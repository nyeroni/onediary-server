package onediary.onediary.common.exception;

public class OAuthProviderMissMatchException extends RuntimeException{

    public OAuthProviderMissMatchException(String message) {
        super(message);
    }

    public OAuthProviderMissMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
