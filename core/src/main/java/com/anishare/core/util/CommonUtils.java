package com.anishare.core.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static JsonObject getJsonRequestBody(SlingHttpServletRequest request) {
        try {
            String inputString = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
            return new JsonParser().parse(inputString).getAsJsonObject();
        } catch (IOException ioException) {
            logger.error("IOException occurred in getJsonRequestBody :: {} ", ioException.getLocalizedMessage());
            ioException.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            logger.error("JsonSyntaxException occurred in getJsonRequestBody :: {} ", jsonSyntaxException.getLocalizedMessage());
            jsonSyntaxException.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception occurred in getJsonRequestBody :: {} ", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

}
