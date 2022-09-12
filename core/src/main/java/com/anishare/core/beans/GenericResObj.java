package com.anishare.core.beans;

import com.google.gson.JsonObject;
import lombok.Data;


/**
 * Generic Response object all responses to follow this common template
 */
@Data
public class GenericResObj {

    private String error;
    private JsonObject data;

}
