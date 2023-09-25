package com.example.diningroom.client;

import java.util.List;

public class BoardingDocument {
    private String DocDetails;
    private String Area;
    private String TransportType;
    private String Date;
    private String imei;
    private List<Person> Persons;

    public BoardingDocument(String DocDetails, String Area, String TransportType, String Date, String imei, List<Person> Persons) {
        this.DocDetails = DocDetails;
        this.Area = Area;
        this.TransportType = TransportType;
        this.Date = Date;
        this.imei = imei;
        this.Persons = Persons;
    }

    public String getDocDetails() {
        return DocDetails;
    }

    public String getArea() {
        return Area;
    }

    public String getTransportType() {
        return TransportType;
    }

    public String getDate() {
        return Date;
    }

    public String getImei() {
        return imei;
    }

    public static class Person {
        private String PersonID;
        private String Venue;

        public String getPersonID() {
            return PersonID;
        }

        public String getVenue() {
            return Venue;
        }

        public String getDateTime() {
            return DateTime;
        }

        private String DateTime;

        public Person(String PersonID, String Venue, String DateTime) {
            this.PersonID = PersonID;
            this.Venue = Venue;
            this.DateTime = DateTime;
        }
    }
}
