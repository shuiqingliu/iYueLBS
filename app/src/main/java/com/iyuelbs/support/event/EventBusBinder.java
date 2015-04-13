package com.iyuelbs.support.event;

/**
 * Created by Bob Peng on 2015/3/2.
 */
public interface EventBusBinder {

    public void registerEventBus();
    public void unregisterEventBus();
    public void postEvent(Object event);
}
