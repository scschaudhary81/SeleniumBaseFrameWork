package generics;

public enum StatusCode {
    OK(200),
    INTERNAL_SERVER_ERROR(500);


    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int get(){
        return this.code;
    }
}
