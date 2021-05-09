package socket.client;

import socket.server.io.RequestObject;

public class RequestThread implements Runnable {
    Client client;
    RequestObject requestObject;

    public RequestThread(Client c, RequestObject requestObject) {
        client = c;
        this.requestObject = requestObject;
    }

    @Override
    public void run() {
        if (requestObject == null) {
            client.readRequest();
        } else
            client.sendRequest(requestObject);
    }
}