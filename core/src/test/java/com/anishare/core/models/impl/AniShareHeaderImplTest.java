package com.anishare.core.models.impl;

import com.anishare.core.models.AniShareHeader;
import com.google.common.collect.ImmutableList;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<List<String>> expectedValues = new ArrayList<>();

        List<String> firstSampleValue = Arrays.asList("Winter", "2023");
        List<String> secondSampleValue = Arrays.asList("Spring", "2023");

        expectedValues.add(firstSampleValue);
        expectedValues.add(secondSampleValue);

        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        assert aniShareHeader != null;
        List<List<String>> seasons = aniShareHeader.getSeasons();
        for (int i = 0; i < seasons.size(); i++) {
            Assertions.assertEquals(seasons.get(i), expectedValues.get(i));
        }
    }

    @Test
    void testGetAltText() {
        final String expected = "AniShare website logo";
        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        assert aniShareHeader != null;
        String actual = aniShareHeader.getAltText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetImageRendition() {

        String expectedRenditionName = "/content/dam/anishare/english/logos/website-home-logo.png/jcr:content/renditions/cq5dam.thumbnail.48.48.png";
        ctx.request().setAttribute("renditonName", "cq5dam.thumbnail.48.48.png");
        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        assert aniShareHeader != null;
        Assertions.assertEquals(expectedRenditionName, aniShareHeader.getImageRendition());

    }

    @Test
    void testIsEmpty_WithoutImage() {
        ctx.currentResource("/content/AniShareHeaderWithoutImage");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        assert aniShareHeader != null;
        Assertions.assertTrue(aniShareHeader.isEmpty());
    }

    @Test
    void testIsNotEmpty() {
        ctx.currentResource("/content/AniShareHeader");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        assert aniShareHeader != null;
        Assertions.assertFalse(aniShareHeader.isEmpty());
    }

    /**
     * Testing if Model is instantiated without required param.
     */
    @Test
    void testInitWithoutAltText() {
        ctx.currentResource("/content/AniShareHeaderWithoutAltText");
        AniShareHeader aniShareHeader = ctx.request().adaptTo(AniShareHeader.class);
        Assertions.assertNull(aniShareHeader);
    }

}
