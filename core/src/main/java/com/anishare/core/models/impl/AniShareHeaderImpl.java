package com.anishare.core.models.impl;

import com.anishare.core.models.AniShareHeader;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.drew.lang.annotations.NotNull;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {AniShareHeader.class},
        resourceType = {AniShareHeaderImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class AniShareHeaderImpl implements AniShareHeader {

    static final String RESOURCE_TYPE = "/apps/anishare/components/content/AniShareHeader";
    private static final Logger logger = LoggerFactory.getLogger(AniShareHeaderImpl.class);

    @ValueMapValue
    private String image;

    @SlingObject
    Resource compResource;

    @ScriptVariable
    private ResourceResolver resolver;

    @ValueMapValue
    private String altText;

    @RequestAttribute
    @Default(values = "cq5dam.thumbnail.48.48.png")
    private String renditonName;


    @Override
    public List<String> getSeasons() {
        List<String> yearAndSeason = new ArrayList<>();
        try {
            Resource multiSeasons = compResource.getChild("multiSeasons");
            for (Resource child : multiSeasons.getChildren()) {
                yearAndSeason.add(child.getValueMap().get("yearAndSeason", String.class));
            }
        }
        catch (NullPointerException npe)
        {
            logger.error("NullPointerException occurred while fetching seasons details :: {}", npe);
        }
        catch (Exception e) {
            logger.error("Exception occurred while fetching seasons details :: {}", e);
        }
        return yearAndSeason;
    }

    @Override
    public String getAltText() {
        return altText;
    }

    @Override
    public String getImageRendition() {
        String imageRenditionPath = StringUtils.EMPTY;
        try {
            if (image != null && !image.isEmpty()) {
                Asset imageAsset = resolver.getResource(image).adaptTo(Asset.class);
                for (Rendition rendition : imageAsset.getRenditions()) {

                    if (rendition.getName().startsWith(renditonName)) {
                        imageRenditionPath = rendition.getPath();
                        break;
                    }
                }
            } else
                throw new IllegalStateException("Image is not configured");

        } catch (Exception e) {
            logger.error("Error occurred while fetching correct image rendition :: {}", e);
        }
        return imageRenditionPath;
    }

    /**
     * Checking if atleast image is configured correctly
     *
     * @return weather to show placeholder or not
     */
    @Override
    public boolean isEmpty() {
        logger.info("image value is  :: {}", image);
        return image == null || image.isEmpty();
    }
}
