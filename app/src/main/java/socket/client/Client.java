package socket.client;

import socket.server.io.RequestObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    final String clientId;
    final Socket clientSocket;
    final ObjectOutputStream objectOutputStream;
    final ObjectInputStream objectInputStream;
    public final int totalRequest;
    private volatile int received;

    public Client(String cId, String hostName, int PORT, int requestNum) throws Exception {
        clientId = cId;
        clientSocket = new Socket(hostName, PORT);
        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        totalRequest = requestNum;
        received = 0;
    }

    public String getClientId() {
        return clientId;
    }

    public void sendRequest(RequestObject requestObject) {
        try {
            synchronized (objectOutputStream) {
                System.out.println("Sending Request:" + requestObject.toString());
                objectOutputStream.writeObject(requestObject);
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    public void readRequest() {
        try {
            while (true) {
                String res = (String) objectInputStream.readObject();
                System.out.println("Result from server:" + res);
                received++;
                if (received == totalRequest)
                    break;
            }
        } catch (Exception e) {

        }
    }

    public void closeClient() {
        try {
            objectOutputStream.close();
            objectInputStream.close();
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Exception Message:" + e.getMessage());
        }
    }
}
