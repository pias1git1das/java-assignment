package socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class App {
    private static final int PORT = 9876;
    private List<ClientThread> clientList=new ArrayList<>();
    final ServerSocket server;

    public App() throws IOException
    {
        server=new ServerSocket(PORT);
    }
    public void startServerAndAcceptRequest() throws IOException, ClassNotFoundException
    {
        System.out.println("Starting socket server");
        while (true) {
            Socket client = server.accept();
            System.out.println("New client connected:" + client.getInetAddress().getHostAddress());
            // create a new thread object for each client
            ClientThread clientSock = new ClientThread(this,client);
            clientList.add(clientSock);
            // handle the client separately by creating new thread for that client
            new Thread(clientSock).start();
        }
    }

    public void closeServer() throws IOException
    {
        /// if any other client socket still open check it & close that socket before shutting
        /// down server
        for(ClientThread client:clientList)
        {
            Socket clientSocket=client.getClientSocket();
            if(!clientSocket.isClosed())
                clientSocket.close();
        }
        System.out.println("Shutting down socket server");
        if(!server.isClosed())
            server.close();
    }

    public static void main(String[] args){
        try {
            App app_ob = new App();
            app_ob.startServerAndAcceptRequest();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
