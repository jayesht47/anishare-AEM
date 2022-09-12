package com.anishare.core.services;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "userRegistrationLoginConfig",
        description = "Config for User Registration and Login Services.")
public interface UserRegistrationLoginConfigService {

    @AttributeDefinition(name = "Enable config",
            description = "This property indicates whether the configuration values will taken into account or not.",
            type = AttributeType.BOOLEAN)
    boolean enableConfig();

    @AttributeDefinition(name = "users json location",
            description = "location in DAM for users json file.",
            type = AttributeType.STRING)
    String usersJsonLocation();


}
