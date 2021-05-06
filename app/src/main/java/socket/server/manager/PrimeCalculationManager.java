package socket.server.manager;

import socket.server.App;

public class PrimeCalculationManager {

    public boolean isPrime(int n) {
        if (n <= 1)
            return false;
        /// little bit optimization complexity becomes O(n*sqrt(n)) from O(n*n)
        for (int i = 2; i <=Math.sqrt(n);i++)
            if (n % i == 0)
                return false;
        return true;
    }
    /*
        Each Client creates a separate instance of this Class
    */
    public int findPrimes(Integer n)
    {
        /// synchronized for each client individual requests handling
        synchronized(this)
        {
            if(n==null || n<0)
                return 0;
            int count = 0;
            for (int i = 2; i <= n; i++) {
                if (isPrime(i))
                    count++;
            }
            return count;
        }
    }

    /*
        Each Client creates a separate instance of this Class
        This code can be run if the heap size is increased
    */
    public int findPrimesOptimized(Integer n)
    {
        /// synchronized for each client individual requests handling
        synchronized(this)
        {
            int cnt=0;
            if(n<2)
                return 0;
            /*
             resCache holding pre-processed result. It reduces the time complexity significantly if we pre-process prime list
             in a resCache & when get a new request just cnt the primes that are <= N
             As it's only count & get the result from already calculated resCache, for millions of request, the runtime will be super-fast
             */
            for(int i = 0; i< App.resCache.size(); i++)
            {
                if(App.resCache.get(i)>n)
                    break;
                cnt++;
            }
            return cnt;
        }
    }
}
