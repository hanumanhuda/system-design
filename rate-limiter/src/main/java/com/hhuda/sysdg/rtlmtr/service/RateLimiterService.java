package com.hhuda.sysdg.rtlmtr.service;

import com.hhuda.sysdg.rtlmtr.strategy.RateLimiter;
import com.hhuda.sysdg.rtlmtr.strategy.SlideWindowRateLimiter;

import java.util.HashMap;
import java.util.Map;


public class RateLimiterService {
    private static  RateLimiterService rateLimiterService = null;
    private Map<String, RateLimiter> rateLimiterMap;
    private int windowTimeInSec;
    private int requests;

    private RateLimiterService(int windowTimeInSec, int requests){
        this.windowTimeInSec = windowTimeInSec;
        this.requests = requests;
        rateLimiterMap =new HashMap<String, RateLimiter>();
    }

    public static RateLimiterService getInstance(int windowTimeInSec,int requests){
        if (rateLimiterService == null){
            synchronized (rateLimiterService){
                if(rateLimiterService == null){
                    rateLimiterService=new RateLimiterService(windowTimeInSec, requests);
                }
            }
        }
        return rateLimiterService;
    }

    public boolean isRateLimited(String ip){
        if (rateLimiterMap.get(ip) == null){
            rateLimiterMap.put(ip, new SlideWindowRateLimiter(windowTimeInSec,requests));
        }
        return rateLimiterMap.get(ip).isAllowed();
    }
}
