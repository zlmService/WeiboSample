package com.demo.zlm.weibosample.User;

import java.util.List;

/**
 * Created by malinkang on 2016/3/31.
 */
public class WeiBo {
    List<Statuses> statuses;

    public List<Statuses> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Statuses> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return "WeiBo{" +
                "statuses=" + statuses +
                '}';
    }
}
