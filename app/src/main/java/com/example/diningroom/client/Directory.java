package com.example.diningroom.client;

import java.util.List;

public class Directory {
    private int MessageNumber;
    private List<directoryData> Area;
    private List<directoryData> Venue;
    private List<directoryData> TransportType;

    public int getMessageNumber() {
        return MessageNumber;
    }

    public List<directoryData> getArea() {
        return Area;
    }

    public List<directoryData> getVenue() {
        return Venue;
    }

    public List<directoryData> getTransportType() {
        return TransportType;
    }

    public class directoryData{
        String Name;
        String ID;

        public String getName() {
            return Name;
        }

        public String getID() {
            return ID;
        }
    }

}
