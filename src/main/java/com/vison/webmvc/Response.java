package com.vison.webmvc;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
import com.vison.webmvc.config.App;
import java.util.Optional;
import java.util.HashMap;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class Response {

    private int code;
    private String msg;
    private Object data;
    private String _uniq_req_no = App._uniq_req_no;

    public Response(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        if (data == null) {
            this.data = new HashMap<>();
        }
        if (data instanceof Optional) {
            Optional d = (Optional) data;
            if (!d.isPresent()) {
                this.data = new HashMap();
            }
        }
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
        Object d = new HashMap<>();
        this.data = d;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public String getUniq_req_no() {
        return App._uniq_req_no;
    }

    public void setUniq_req_no(String _uniq_req_no) {
        this._uniq_req_no = _uniq_req_no;
    }

}
