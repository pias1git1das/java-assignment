package socket.client;

import socket.server.io.RequestObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String args[]) throws Exception {
        Main ob = new Main();
        List<Client> clientList = new ArrayList<>();
        // creating a client pool
        for (int c = 1; c <= 10; c++) {
            Client client = new Client("client" + c, "127.0.0.1", 9876);
            clientList.add(client);
        }
        /// making 1000 request for 10 clients
        for (int i = 1; i <= 300; i++) {
            Random r = new Random();
            int randomClient = r.nextInt(Integer.MAX_VALUE) % clientList.size();
            Client client = clientList.get(randomClient);

            /// creating request object
            RequestObject req = new RequestObject();
            HashMap<String, String> hm = new HashMap<>();
            hm.put("n", "" + r.nextInt(3000));

            req.setManagerName("PrimeCalculationManager");
            req.setMethod("findPrimes");
            req.setRequestId(client.getClientId() + "-" + i);
            req.setArgs(hm);
            req.setMessage("request");
            client.sendRequest(req);
        }
        RequestObject exitRequest = ob.createExitRequest();
        for (int i = 1; i < clientList.size(); i++)
            clientList.get(i).closeClient();

        /// creating exit request to server from that last client
        if (clientList.size() > 0) {
            clientList.get(0).sendRequest(exitRequest);
            clientList.get(0).closeClient();
        }
    }

    RequestObject createExitRequest()
    {
        RequestObject requestObject=new RequestObject();
        requestObject.setMessage("EXIT");
        return requestObject;
    }
}