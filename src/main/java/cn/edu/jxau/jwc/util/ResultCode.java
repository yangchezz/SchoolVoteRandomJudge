package cn.edu.jxau.jwc.util;

public  enum ResultCode {
    //失败
    FAIL(-1),
    //成功
    SUCCESS(200),
    //错误
    ERROR(500),
    //未找到
    NOT_FOUND(404),
    //拒绝
    REFUSE(305),
    //非法
    ILLEGALITY(400),

    //超时
    TIME_OUT(304);


    private int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
