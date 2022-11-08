package com.anishare.core.models.impl;

import com.anishare.core.models.AniShareHeader;
import com.google.common.collect.ImmutableList;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;


@ExtendWith(AemContextExtension.class)
public class AniShareHeaderImplTest {

    private final AemContext ctx = new AemContext();


    @BeforeEach
    void setup() {

        ctx.addModelsForClasses(AniShareHeaderImpl.class);
        ctx.load().json("/com.anishare.core/models/impl/AniShareHeaderImplTest.json", "/content");
        ctx.load().json("/com.anishare.core/models/impl/sampleAsset.json", "/content/dam/anishare/english/logos/website-home-logo.png");
    }

    @Test
    void testGetSeasons() {
        List<String> expectedValues = new ImmutableList.Builder<String>().add("Winter 2023").add("Spring 2023").add("Summer 1999").build();
        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        List<String> seasons = aniShareHeader.getSeasons();
        Assertions.assertEquals(seasons, expectedValues);
    }

    @Test
    void testGetAltText() {
        final String expected = "Sample Alt Text";
        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        String actual = aniShareHeader.getAltText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetImageRendition() {

        String expectedRenditionName = "/content/dam/anishare/english/logos/website-home-logo.png/jcr:content/renditions/cq5dam.thumbnail.48.48.png";
        ctx.request().setAttribute("renditonName", "cq5dam.thumbnail.48.48.png");
        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        Assertions.assertEquals(expectedRenditionName, aniShareHeader.getImageRendition());

    }

    @Test
    void testIsEmpty_WithoutImage() {
        ctx.currentResource("/content/AniShareHeaderWithoutImage");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        Assertions.assertTrue(aniShareHeader.isEmpty());
    }

    @Test
    void testIsNotEmpty() {
        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        Assertions.assertFalse(aniShareHeader.isEmpty());
    }

    /**
     * Testing if Model is instantiated without required param.
     */
    @Test
    void testInitWithoutAltText(){
        ctx.currentResource("/content/AniShareHeaderWithoutAltText");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        Assertions.assertNull(aniShareHeader);
    }

}
