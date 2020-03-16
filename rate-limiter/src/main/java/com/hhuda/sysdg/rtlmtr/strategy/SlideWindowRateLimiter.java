package com.hhuda.sysdg.rtlmtr.strategy;

import javax.inject.Named;
import java.util.LinkedList;
import java.util.Queue;

@Named
public class SlideWindowRateLimiter implements RateLimiter {
    private int windowTimeInSec;
    private int requests;
    private Queue<Long> queue;

    /**
     * initialize SlideWindowStrategy with window in seconds
     * @param windowTimeInSec
     * @param requests
     */
    public SlideWindowRateLimiter(int windowTimeInSec, int requests){
            this.windowTimeInSec = windowTimeInSec;
            this.requests = requests;
            this.queue = new LinkedList<Long>();
    }

    /**
     *
     * @return
     */
    public boolean isAllowed() {
        removeOlderTimestamps();
        queue.add(this.getCurrentTimestamp());
        if (queue.size()>requests){
            return false;
        }
        return true;
    }

    private Long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    private void removeOlderTimestamps() {
        synchronized (queue){
            while(queue.element()<this.getCurrentTimestamp()-windowTimeInSec){
                queue.remove();
            }
        }
    }
}
