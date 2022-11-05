package com.anishare.core.models;

import java.util.List;

public interface AniShareHeader {


    List<String> getSeasons();

    String getImageRendition();

    String getAltText();

    boolean isEmpty();

}
