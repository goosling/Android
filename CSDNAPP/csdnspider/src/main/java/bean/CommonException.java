package bean;

/**
 * Created by joe on 2016/4/10.
 */
public class CommonException extends Exception {

    public CommonException() {
        super();
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }


}
