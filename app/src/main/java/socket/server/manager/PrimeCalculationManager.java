package socket.server.manager;

import socket.server.App;
import socket.server.io.RequestObject;

class RequestProcessorThread implements Runnable
{
    private int number;
    private int res;
    RequestObject ob;
    public RequestProcessorThread(int n,RequestObject requestObject) {
        number=n;
        ob=requestObject;
    }
    @Override
    public void run() {
        int count = 0;
        for (int i = 2; i <= number; i++) {
            if (isPrime(i))
                count++;
        }
        res=count;
    }
    boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
    public String toString()
    {
        return ob.toString()+","+res;
    }
}
public class PrimeCalculationManager {
    /*
        Each Client creates a separate instance of this Class
    */

    public String findPrimes(Integer n,RequestObject ob)
    {
        try {
            RequestProcessorThread thread = new RequestProcessorThread(n,ob);
            Thread t = new Thread(thread);
            t.start();
            t.join();
            return thread.toString();
        }
        catch (Exception e){
            return ob.toString()+":"+-1;
        }
    }
}
