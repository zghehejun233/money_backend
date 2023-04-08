package site.surui.web.common.data.vo.result;


public enum AuthError implements ResultError {
    PASSWORD_WRONG(40100, "学号或统一身份认证密码错误"),
    LOGIN_STATUS_IS_INVALID(40101, "登录状态已失效"),
    LOGIN_STATUS_WRONG(40102, "登录状态错误，请重新登录"),
    PERM_NOT_ALLOW(40103, "无权限访问"),
    HAD_BIND_MINOR_ACCOUNT(40104, "辅修账号不能重复绑定"),
    MINOR_USER_EQUAL_ORIGINAL_USER(40105, "辅修账号不能和原账号相同"),
    ACCOUNT_NOT_EXISTS(40106, "账户不存在"),
    VERIFY_CODE_NOT_FOUND(40107, "未找到对应的验证码"),
    VERIFY_CODE_WRONG(40108, "验证码错误"),
    DUPLICATED_PASSWORD(40109, "两密码相同"),
    WRONG_OLD_PASSWORD(40110,"旧密码错误"),
    EMAIL_NOT_NULL(40111,"您已绑定邮箱，无法重复绑定"),
    EMAIL_NULL(40112,"未绑定邮箱"),
    WRONG_EMAIL(40113,"邮箱错误");

    private int code;

    private String message;


    AuthError(int code, String message) {
        this.code = code;
        this.message = message;
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
}

