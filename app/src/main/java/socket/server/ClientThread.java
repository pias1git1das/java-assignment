package socket.server;

import socket.server.io.RequestObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket clientSocket;
    private final ObjectOutputStream serverOutputStream;
    private final ObjectInputStream serverInputStream;
    private static final String EXIT = "EXIT";
    private final App app_server;
    // Constructor
    public ClientThread(App ob,Socket socket) throws IOException
    {
        this.clientSocket = socket;
        this.app_server=ob;
        serverInputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        serverOutputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
    }
    public Socket getClientSocket()
    {
        return this.clientSocket;
    }
    public void run()
    {
        RequestObject requestObject=null;
        try {
            while(true) {
                requestObject = (RequestObject) serverInputStream.readObject();
                String message= requestObject.getMessage();
                /// if exit messages then exit the server
                if(message.equalsIgnoreCase(EXIT))
                {
                    app_server.closeServer();
                    break;
                }
                /// reflection api to get the class name & method name & method invocation
                Class<?> c = Class.forName("socket.server.manager."+requestObject.getManagerName());
                Object object = c.newInstance();
                Method method = c.getDeclaredMethod(requestObject.getMethod(), Integer.class);

                /// getting the number from HashMap
                String number = requestObject.getArgs().getOrDefault("n", "");

                try {
                    int num = Integer.parseInt(number);
                    /// if num is negative or exceeds the MAX VALUE then throw an checked Exception
                    if (num < -1 || num > Integer.MAX_VALUE) {
                        serverOutputStream.writeObject(requestObject.toString() + " :" + "Number must be positive integer");
                    }
                    else {
                        /// invode the findPrime method via reflection api along with that client object
                        int res = (int) method.invoke(object, num);
                        serverOutputStream.writeObject(requestObject.toString() + " :" + res);
                    }
                }
                catch (NumberFormatException e) ///if not integer when parsing then it will throw this exception
                {
                    serverOutputStream.writeObject(requestObject.toString() + " ," + "Please provide Integer number");
                }
            }
        }
        catch(Exception e)
        {
            //System.out.println("Exception:"+e.getMessage());
        }

    }
}