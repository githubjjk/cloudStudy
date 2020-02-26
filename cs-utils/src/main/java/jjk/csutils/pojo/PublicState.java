package jjk.csutils.pojo;

public enum PublicState {
    //请求成功
    SUCCESS(0),
    //业务错误
    BUSSINSERROR(1),
    //鉴权失败
    AUTHFAILURE(2),
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
