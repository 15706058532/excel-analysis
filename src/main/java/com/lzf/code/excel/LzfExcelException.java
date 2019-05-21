package com.lzf.code.excel;

/**
 * excel异常类
 * <br/>
 * Created in 2019-05-20 22:57
 *
 * @author Zhenfeng Li
 */
public class LzfExcelException extends Exception {
    public LzfExcelException() {
    }

    public LzfExcelException(String message) {
        super(message);
    }

    public LzfExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public LzfExcelException(Throwable cause) {
        super(cause);
    }

    public LzfExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
