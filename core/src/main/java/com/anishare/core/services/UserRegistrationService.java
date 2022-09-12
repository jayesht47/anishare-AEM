package com.anishare.core.services;


import com.anishare.core.beans.RegisterUserDetails;
import com.google.gson.JsonObject;

public interface UserRegistrationService {

    JsonObject registerUser(RegisterUserDetails registerUserDetails);

}
