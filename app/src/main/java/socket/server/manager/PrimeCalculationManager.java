package socket.server.manager;

import socket.server.io.RequestObject;

import java.io.ObjectOutputStream;

public class PrimeCalculationManager implements Runnable {

    int num;
    RequestObject requestObject;
    ObjectOutputStream outputStream;

    public PrimeCalculationManager(Integer n, ObjectOutputStream out, RequestObject ob) {
        num = n;
        outputStream = out;
        requestObject = ob;
    }

    @Override
    public void run() {
        String r = findPrimes(num, requestObject);
        try {
            synchronized (outputStream) {
                outputStream.writeObject(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Each Client creates a separate instance of this Class
    public String findPrimes(Integer n, RequestObject ob) {
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i))
                count++;
        }
        return ob.toString() + ":" + count;
    }

    boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
