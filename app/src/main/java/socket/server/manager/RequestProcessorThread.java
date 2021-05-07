package socket.server.manager;

import socket.server.io.RequestObject;

public class RequestProcessorThread implements Runnable {
    private int number;
    private int res;
    RequestObject ob;

    public RequestProcessorThread(int n, RequestObject requestObject) {
        number = n;
        ob = requestObject;
    }

    @Override
    public void run() {
        int count = 0;
        for (int i = 2; i <= number; i++) {
            if (isPrime(i))
                count++;
        }
        res = count;
    }

    boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return ob.toString() + "," + res;
    }
}