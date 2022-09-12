package com.anishare.core.servlets;


import com.anishare.core.beans.ErrorResObjFactory;
import com.anishare.core.beans.GenericResObj;
import com.anishare.core.beans.RegisterUserDetails;
import com.anishare.core.constants.CommonConstants;
import com.anishare.core.services.UserRegistrationService;
import com.anishare.core.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component(service = {Servlet.class})
@SlingServletResourceTypes(resourceTypes = "anishare/components/page", extensions = "json", methods = HttpConstants.METHOD_POST, selectors = "registerUser")
@ServiceDescription("Servlet for registering users.")
public class registerUserServlet extends SlingAllMethodsServlet {

    private static final Logger logger = LoggerFactory.getLogger(registerUserServlet.class);

    @Reference
    private transient ResourceResolverFactory resolverFactory;

    @Reference
    private transient UserRegistrationService userRegistrationService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        Gson gson = new Gson();
        try {
            request.getParameterMap();
            JsonObject requestBody = CommonUtils.getJsonRequestBody(request);
            RegisterUserDetails registerUserDetails = gson.fromJson(requestBody, RegisterUserDetails.class);
            JsonObject registrationResult = userRegistrationService.registerUser(registerUserDetails);
            response.setContentType("application/json");
            if (requestBody != null && registrationResult != null) {
                GenericResObj genericResObj = new GenericResObj();
                genericResObj.setError("");
                genericResObj.setData(registrationResult);
                response.getWriter().write(gson.toJson(genericResObj));
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                ErrorResObjFactory errorResObjFactory = new ErrorResObjFactory();
                GenericResObj resObj = errorResObjFactory.createErrorResObj(CommonConstants.INTERNAL_APP_ERROR);
                response.getWriter().write(gson.toJson(resObj));
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception occurred in registerUserServlet :: {}", e.getLocalizedMessage());
            e.printStackTrace();
            ErrorResObjFactory errorResObjFactory = new ErrorResObjFactory();
            GenericResObj resObj = errorResObjFactory.createErrorResObj(CommonConstants.INTERNAL_APP_ERROR);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(resObj));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (response.getWriter() != null)
                response.getWriter().close();
        }
    }
}
