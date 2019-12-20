package cn.edu.jxau.jwc.util;


/**
 * @Author vector
 * @Since 1.0.0
 * @Date 2019/10/23 21:36
 */
public class JsonResult<T> {

    //说明信息
    private String message;

    //发送的数据
    private T data;

    //状态码
    private Integer code;

    //结果
    private boolean result;

    //身份信息代码
    private String roleStatue;


    public JsonResult() {
    }

    public JsonResult(String message, T data, ResultCode code, boolean result) {
        this.message = message;
        this.data = data;
        this.code = code.getCode();
        this.result = result;
    }

    public JsonResult(String message, ResultCode code, boolean result) {
        this.message = message;
        this.code = code.getCode();
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getRoleStatue() {
        return roleStatue;
    }

    public void setRoleStatue(String roleStatue) {
        this.roleStatue = roleStatue;
    }


    @Override
    public String toString() {
        return "JsonResult{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", code=" + code +
                ", result=" + result +
                ", roleStatue='" + roleStatue + '\'' +
                '}';
    }
}
