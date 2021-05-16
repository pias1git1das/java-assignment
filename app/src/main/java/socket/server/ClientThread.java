package socket.server;

import socket.server.io.RequestObject;
import socket.server.manager.PrimeCalculationManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket clientSocket;
    private final ObjectOutputStream serverOutputStream;
    private final ObjectInputStream serverInputStream;
    private static final String EXIT = "EXIT";
    private final App appServer;
    private Object reflectionObject;
    private Class<?> classInstance;
    private Method method;

    // Constructor
    public ClientThread(App ob, Socket socket) throws IOException {
        this.clientSocket = socket;
        this.appServer = ob;
        reflectionObject = null;
        serverInputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        serverOutputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void run() {
        RequestObject requestObject = null;
        try {
            while (true) {
                synchronized (serverInputStream) {
                    requestObject = (RequestObject) serverInputStream.readObject();
                }
                String message = requestObject.getMessage();
                /// if exit messages then exit the server
                if (message.equalsIgnoreCase(EXIT)) {
                    appServer.closeServer();
                    break;
                }
                String number = requestObject.getArgs().getOrDefault("n", "");
                int num = Integer.parseInt(number);
                ///PrimeCalculationManager ob = new PrimeCalculationManager(num, serverOutputStream, requestObject);
                classInstance = Class.forName("socket.server.manager." + requestObject.getManagerName());
                Constructor<?> cons = classInstance.getConstructor(Integer.class, ObjectOutputStream.class, RequestObject.class);
                reflectionObject = cons.newInstance(num, serverOutputStream, requestObject);
                new Thread((PrimeCalculationManager)reflectionObject).start();
            }
        } catch (Exception e) {

        }
    }
}