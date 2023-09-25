package com.example.diningroom.client;

import java.util.List;

public class ProcessedGUIDs {
    private String imei;
    private List<String> PropuskGUID;

    public ProcessedGUIDs(String imei, List<String> PropuskGUID) {
        this.imei = imei;
        this.PropuskGUID = PropuskGUID;
    }
}
