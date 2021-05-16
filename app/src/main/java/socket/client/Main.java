package socket.client;

import socket.server.io.RequestObject;

import java.util.*;

public class Main {

    public void singleClientParallelRequest() throws Exception {
        int totalRequest = 100;
        Client client = new Client("client", "127.0.0.1", 9876, totalRequest);
        RequestThread readThread = new RequestThread(client, null);
        Thread readThreadObject = new Thread(readThread);
        readThreadObject.start();

        for (int i = 1; i <= totalRequest; i++) {
            Random r = new Random();
            /// creating request object
            RequestObject req = new RequestObject();
            HashMap<String, String> hm = new HashMap<>();
            hm.put("n", "" + r.nextInt(70000));

            req.setManagerName("PrimeCalculationManager");
            req.setMethod("findPrimes");
            req.setRequestId(client.getClientId() + "-Req" + i);
            req.setArgs(hm);
            req.setMessage("request");
            RequestThread thread = new RequestThread(client, req);
            Thread t = new Thread(thread);
            t.start();
            t.join();
            Thread.sleep(40);
        }
        readThreadObject.join();
        client.closeClient();
    }

    public void multiClientMultiParallelRequest() throws Exception {
        List<Client> clientList = new ArrayList<>();
        List<Thread> readThreadClientList = new ArrayList<>();

        // creating a client pool
        for (int c = 1; c <= 10; c++) {
            int totalRequest = 10;
            Client client = new Client("client" + c, "127.0.0.1", 9876, totalRequest);
            clientList.add(client);
            /// this thread are only used by each socket to read response from server
            RequestThread readThread = new RequestThread(client, null);
            Thread t = new Thread(readThread);
            t.start();
            readThreadClientList.add(t);
        }

        for (Client client : clientList) {
            for (int i = 1; i <= client.totalRequest; i++) {
                Random r = new Random();
                /// creating request object
                RequestObject req = new RequestObject();
                HashMap<String, String> hm = new HashMap<>();
                hm.put("n", "" + r.nextInt(70000));

                req.setManagerName("PrimeCalculationManager");
                req.setMethod("findPrimes");
                req.setRequestId(client.getClientId() + "-Req" + i);
                req.setArgs(hm);
                req.setMessage("request");
                RequestThread thread = new RequestThread(client, req);
                Thread t = new Thread(thread);
                t.start();
                t.join();
                Thread.sleep(30);
            }
        }

        for (Thread readThread : readThreadClientList)
            readThread.join();
        for (Client c : clientList)
            c.closeClient();
    }

    public static void main(String args[]) throws Exception {
        Main ob = new Main();
        ob.singleClientParallelRequest();
        //ob.multiClientMultiParallelRequest();
    }

    RequestObject createExitRequest() {
        RequestObject requestObject = new RequestObject();
        requestObject.setMessage("EXIT");
        return requestObject;
    }
}
