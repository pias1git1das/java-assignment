package socket.client;

import socket.server.io.RequestObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    final String clientId;
    final Socket clientSocket;
    final  ObjectOutputStream objectOutputStream;
    final ObjectInputStream objectInputStream;

    public Client(String cId,String hostName, int PORT) throws  Exception{
            clientId=cId;
            clientSocket = new Socket(hostName, PORT);
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public String getClientId()
    {
        return clientId;
    }
    public void sendRequest(RequestObject requestObject)
    {
        ///System.out.println("Request:" + requestObject.toString());
        synchronized (this) {
            try {
                objectOutputStream.writeObject(requestObject);
                if(!requestObject.getMessage().equalsIgnoreCase("EXIT")) {
                    String message = (String) objectInputStream.readObject();
                    System.out.println("Result from server: " + message);
                }
            }
            catch (Exception e) {
                System.out.println("Exception:" + e.getMessage());
            }
        }
    }

    public void closeClient()
    {
        try {
            objectOutputStream.close();
            objectInputStream.close();
            clientSocket.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception Message:"+e.getMessage());
        }
    }
}
