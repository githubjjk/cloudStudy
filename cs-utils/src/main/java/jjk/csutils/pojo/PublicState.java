package jjk.csutils.pojo;

public enum PublicState {
    //请求成功
    SUCCESS(0),
    //业务错误
    BUSSINS_ERROR(1),
    //鉴权失败
    AUTH_FAILURE(2),
    //未发现资源
    NOT_FOUND(3);

    int value;

    PublicState(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
