package site.forum.web.common.data.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * web层统一返回类型
 *
 * @Author skyyemperor
 * Created by skyyemperor on 2021-01-30
 * Description : 通用异常返回的父接口
 */
@ApiModel("通用返回值包装")
public class Result <T>{
    @ApiModelProperty("状态码")
    private int code = 0;

    @ApiModelProperty("message")
    private String message;

    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ResultError resultError) {
        this.code = resultError.getCode();
        this.message = resultError.getMessage();
    }

    public  Result<T> getResult(int code, String message) {
        return getResult(code, message, null);
    }

    public  Result<T> getResult(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public  Result<T> success() {
        return success(null);
    }

    public  Result<T> success(T data) {
        return success("success", data);
    }

    public  Result<T> success(String message, T data) {
        return new Result<>(0, message, data);
    }

    public  Result<T> fail() {
        return fail(null);
    }

    public  Result<T> fail(T data) {
        return fail("请求失败", data);
    }

    public  Result<T> fail(String message, T data) {
        return new Result<>(-1, message, data);
    }

    public  Result<T> getResult(ResultError resultError) {
        return getResult(resultError.getCode(), resultError.getMessage());
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Info{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

