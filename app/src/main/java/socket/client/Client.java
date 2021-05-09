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
    private volatile int sent, received;
    private boolean running;

    public Client(String cId, String hostName, int PORT) throws Exception {
        clientId = cId;
        clientSocket = new Socket(hostName, PORT);
        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        running = true;
        sent = received = 0;
    }

    public String getClientId() {
        return clientId;
    }

    public void sendRequest(RequestObject requestObject) {
        try {
            synchronized (objectOutputStream) {
                sent++;
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
                received++;
                System.out.println("Result from server:" + res);
                if (received == sent)
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
            running = false;

        } catch (Exception e) {
            System.out.println("Exception Message:" + e.getMessage());
        }
    }
}
