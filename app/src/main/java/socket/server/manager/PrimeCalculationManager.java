package socket.server.manager;

import socket.server.io.RequestObject;

public class PrimeCalculationManager {
    // Each Client creates a separate instance of this Class
    public String findPrimes(Integer n, RequestObject ob) {
        try {
            RequestProcessorThread thread = new RequestProcessorThread(n, ob);
            Thread t = new Thread(thread);
            t.start();
            t.join();
            return thread.toString();
        } catch (Exception e) {
            return ob.toString() + ":" + -1;
        }
    }
}
