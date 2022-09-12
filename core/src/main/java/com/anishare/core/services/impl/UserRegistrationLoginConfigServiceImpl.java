package com.anishare.core.services.impl;

import com.anishare.core.services.UserRegistrationLoginConfigService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import java.lang.annotation.Annotation;

@Component(service = UserRegistrationLoginConfigService.class, immediate = true)
@Designate(ocd = UserRegistrationLoginConfigService.class)
public class UserRegistrationLoginConfigServiceImpl implements UserRegistrationLoginConfigService {

    private UserRegistrationLoginConfigService userRegistrationLoginConfigService;


    @Activate
    protected void activate(UserRegistrationLoginConfigService userRegistrationLoginConfigService) {
        this.userRegistrationLoginConfigService = userRegistrationLoginConfigService;
    }

    @Override
    public boolean enableConfig() {
        return userRegistrationLoginConfigService.enableConfig();
    }

    @Override
    public String usersJsonLocation() {
        return userRegistrationLoginConfigService.usersJsonLocation();
    }

}
