package com.anishare.core.models.impl;

import com.anishare.core.models.AniShareHeader;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    @Required
    private String altText;

    @RequestAttribute
    @Default(values = "cq5dam.thumbnail.48.48.png")
    private String renditonName;


    @Override
    public List<List<String>> getSeasons() {
        List<List<String>> yearAndSeason = new ArrayList<>();
        try {
            Resource multiSeasons = compResource.getChild("multiSeasons");
            assert multiSeasons != null;
            for (Resource child : multiSeasons.getChildren()) {
                List<String> oneSeasonYear = new ArrayList<>();
                String seasonName = child.getValueMap().get("seasonName", String.class);
                String seasonYear = child.getValueMap().get("seasonYear", String.class);
                oneSeasonYear.add(seasonName);
                oneSeasonYear.add(seasonYear);
                yearAndSeason.add(oneSeasonYear);
            }
        } catch (NullPointerException npe) {
            logger.error("NullPointerException occurred while fetching seasons details :: {}", npe.toString());
        } catch (Exception e) {
            logger.error("Exception occurred while fetching seasons details :: {}", e.toString());
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
                Asset imageAsset = Objects.requireNonNull(resolver.getResource(image)).adaptTo(Asset.class);
                assert imageAsset != null;
                for (Rendition rendition : imageAsset.getRenditions()) {

                    if (rendition.getName().startsWith(renditonName)) {
                        imageRenditionPath = rendition.getPath();
                        break;
                    }
                }
            } else
                throw new IllegalStateException("Image is not configured");

        } catch (Exception e) {
            logger.error("Error occurred while fetching correct image rendition :: {}", e.toString());
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
