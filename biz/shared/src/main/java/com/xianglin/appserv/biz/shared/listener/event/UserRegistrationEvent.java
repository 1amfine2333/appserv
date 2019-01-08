package com.xianglin.appserv.biz.shared.listener.event;

import com.xianglin.appserv.common.dal.dataobject.User;
import org.springframework.context.ApplicationEvent;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/5 21:37.
 */

public class UserRegistrationEvent extends ApplicationEvent {

    private static final long serialVersionUID = 781081039574768463L;

    private User user;

    public UserRegistrationEvent(Object source, User user) {

        super(source);
        this.user = user;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }
}
