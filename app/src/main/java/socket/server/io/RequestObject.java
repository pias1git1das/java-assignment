package socket.server.io;

import java.io.Serializable;
import java.util.Map;

public class RequestObject implements Serializable {

    private String managerName, method, requestId, message;
    public Map<String, String> args;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String toString() {
        return this.requestId + "," + args;
    }

}
