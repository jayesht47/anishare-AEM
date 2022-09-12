package com.anishare.core.util;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DamUtils {

    private static final Logger logger = LoggerFactory.getLogger(DamUtils.class);

    /**
     * Returns resource resolver used to access Resources/DAM Assets with Asset API.
     *
     * @param resourceResolverFactory resourceResolverFactory to get resourceResolver
     * @return returns resourceResolver used to access Assets.
     */
    public static ResourceResolver getResourceResolver(ResourceResolverFactory resourceResolverFactory) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "damutils");
        ResourceResolver resourceResolver;
        try {
            if (resourceResolverFactory != null) {
                logger.trace("getResourceResolver called with param :: {}", resourceResolverFactory);
                resourceResolver = resourceResolverFactory.getServiceResourceResolver(params);
                return resourceResolver;
            } else
                throw new IllegalArgumentException("got resourceResolverFactory as null");
        } catch (LoginException loginException) {
            logger.error("LoginException in getResourceResolver :: {}", loginException.getLocalizedMessage());
            loginException.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception in getResourceResolver :: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads Json file stored on DAM.
     *
     * @param resourceResolver resourceResolver to read DAM Asset.
     * @param completePath     complete path of file
     * @return {@link JsonObject}
     */
    public static JsonObject readJsonFromDAM(ResourceResolver resourceResolver, String completePath) {
        try {
            logger.trace("readJsonFromDAM invoked with params :: {} {}", resourceResolver, completePath);
            Resource resource = resourceResolver.getResource(completePath);
            if (resource != null) {
                Asset asset = resource.adaptTo(Asset.class);
                if (asset != null) {
                    Resource orignalResource = asset.getOriginal();
                    InputStream is = orignalResource.adaptTo(InputStream.class);
                    if (is != null) {
                        return new JsonParser().parse(new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
                    }
                }
            }
        } catch (JsonIOException jsonIOException) {
            logger.error("JsonIOException occurred in readJsonFromDAM {} ", jsonIOException.getLocalizedMessage());
            jsonIOException.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            logger.error("JsonSyntaxException occurred in readJsonFromDAM {} ", jsonSyntaxException.getLocalizedMessage());
            jsonSyntaxException.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception occurred in readJsonFromDAM {} ", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates  a DAM Asset containing a {@link JsonObject}
     *
     * @param jsonObject       jsonObject to be saved to DAM.
     * @param resourceResolver resourceResolver used to create Asset.
     * @param completePath     complete path where the resource is to be saved.
     * @return returns weather operation is successful or not.
     */
    public static boolean writeJsonToDAM(JsonObject jsonObject, ResourceResolver resourceResolver, String completePath) {
        InputStream is = null;
        try {
            logger.trace("writeJsonToDAM invoked with params :: {} {} {}", jsonObject, resourceResolver, completePath);

            if (resourceResolver != null) {
                AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
                if (assetManager != null) {
                    String jsonString = jsonObject.toString();
                    is = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
                    Asset resultAsset = assetManager.createAsset(completePath, is, "application/json", true); // last param is to save changes to jcr
                    return resultAsset != null;
                }
            }
        } catch (JsonIOException jsonIOException) {
            logger.error("JsonIOException occurred in readJsonFromDAM {} ", jsonIOException.getLocalizedMessage());
            jsonIOException.printStackTrace();
        } catch (JsonSyntaxException jsonSyntaxException) {
            logger.error("JsonSyntaxException occurred in readJsonFromDAM {} ", jsonSyntaxException.getLocalizedMessage());
            jsonSyntaxException.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception occurred in readJsonFromDAM {} ", e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ioException) {
                logger.error("IOException occurred while closing InputStream :: {}", ioException.getLocalizedMessage());
                ioException.printStackTrace();
            }
        }
        return false;
    }
}
