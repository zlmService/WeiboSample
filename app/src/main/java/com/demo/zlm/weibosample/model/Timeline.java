package com.demo.zlm.weibosample.model;

import java.util.List;

/**
 * Created by malinkang on 2016/3/31.
 */
public class Timeline {
    List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "statuses=" + statuses +
                '}';
    }
}
