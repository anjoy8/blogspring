package club.neters.blogspring.utils;


public class ResponseUtil {
    public static final int Code = 200;

    public int code = 200;
    public String msg = "success";
    public Object data;
    public Boolean success = true;

    public static ResponseUtil response(int code, String msg, Object data) {
        ResponseUtil resp = new ResponseUtil();
        resp.success = code == Code;
        resp.code = (code);
        resp.msg = (msg);
        resp.data = data;
        return resp;
    }

    public static Object response(Object data) {
        ResponseUtil resp = new ResponseUtil();
        resp.data = data;
        return resp;
    }

}