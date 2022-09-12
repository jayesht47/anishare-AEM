package com.anishare.core.beans;


/**
 * Simple factory impl for generation error res objects.
 */
public class ErrorResObjFactory {

    public GenericResObj createErrorResObj(String errorType) {
        switch (errorType) {
            case "BAD_REQUEST": {
                GenericResObj resObj = new GenericResObj();
                resObj.setError("BAD_REQUEST");
                return resObj;
            }
            case "INTERNAL_APP_ERROR": {
                GenericResObj resObj = new GenericResObj();
                resObj.setError("INTERNAL_APP_ERROR");
                return resObj;
            }
            default:
                throw new IllegalArgumentException("Illegal value for errorType :: " + errorType);
        }

    }

    public GenericResObj createErrorResObj(String errorType, String customMessage) {
        GenericResObj resObj = new GenericResObj();
        resObj.setError(customMessage);
        return resObj;
    }

}
