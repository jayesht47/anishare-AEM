package com.anishare.core.services.impl;

import com.anishare.core.beans.RegisterUserDetails;
import com.anishare.core.services.UserRegistrationLoginConfigService;
import com.anishare.core.services.UserRegistrationService;
import com.anishare.core.util.DamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = UserRegistrationService.class, immediate = true)
public class UserRegistrationServiceImpl implements UserRegistrationService {


    @Reference
    UserRegistrationLoginConfigService userRegistrationLoginConfigService;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public JsonObject registerUser(RegisterUserDetails registerUserDetails) {
        //Currently, using AEM DAM as simple DB Can expand to RDBMS solution.

        if (registerUserDetails != null) {
            String usersJsonPath = userRegistrationLoginConfigService.usersJsonLocation();
            ResourceResolver resourceResolver = DamUtils.getResourceResolver(resourceResolverFactory);
            JsonObject currentUsersJsonObject = DamUtils.readJsonFromDAM(resourceResolver, usersJsonPath);
            if (currentUsersJsonObject != null) {
                currentUsersJsonObject.addProperty(registerUserDetails.getUserName(), registerUserDetails.getPassword());
                DamUtils.writeJsonToDAM(currentUsersJsonObject, resourceResolver, usersJsonPath);
                return new Gson().toJsonTree(registerUserDetails).getAsJsonObject();
            } else
                throw new IllegalArgumentException("Got currentUsersJsonObject as null");

        } else
            throw new IllegalArgumentException("Got RegisterUserDetails as null");
    }
}
