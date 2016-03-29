package bean;

/**
 * Created by joehu on 2016/3/29.
 */
public class CommonException extends Exception {

    public CommonException() {
        super();
    }

    public CommonException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CommonException(String detailMessage) {
        super(detailMessage);
    }

    public CommonException(Throwable throwable) {
        super(throwable);
    }
}
