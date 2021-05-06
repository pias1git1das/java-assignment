package socket.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class App {
    private static final int PORT = 9876;
    private List<ClientThread> clientList=new ArrayList<>();
    public static List<Integer> resCache=new ArrayList<>();
    /// this block will be executed first & we pre-process the prime result cache here
    /// more details of it's use & benefits will be found at PrimeCalculationManager class
    /// heap size needs to be increased to run the optimized version
    /*
    static {
            int n=Integer.MAX_VALUE;
            HashMap<Integer, Boolean> primeMap=new HashMap<>();
            // 0,1 is not prime so we start from 2
            for(int i=2;i<=n;i++)
                primeMap.put(i,true);

            for (int p = 2; p * p <= n; p++)
            {
                if (primeMap.get(p))
                {
                    /// as 2 is prime, 4,6,8,10,12....will be not prime,
                    // in that way we will discard number that is not prime & mark it as false
                    for (int i = p * 2; i <= n; i += p)
                    {
                        primeMap.put(i,false);
                    }
                }
            }
            /// now the remaining that is still marked as true, are prime numbers
            for (int i = 2; i <= n; i++) {
                if (primeMap.get(i)) {
                    resCache.add(i);
                }
            }
           /// System.out.println(resCache);
    }
    */
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
