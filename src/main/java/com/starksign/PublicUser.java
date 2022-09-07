package com.starksign;

import com.starksign.utils.Check;


public class PublicUser{
    public final String environment;

    public PublicUser(String environment) throws Exception {
        this.environment = Check.environment(environment);
    }
}
