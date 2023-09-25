package com.example.diningroom.client;

import java.util.List;

public class Distribution {
    private String DocDetails;
    private String Date;
    private String DepartureDate;
    private List<distributionPassenger> Passangers;

    public String getDocDetails() {
        return DocDetails;
    }

    public String getDate() {
        return Date;
    }

    public String getDepartureDate() {
        return DepartureDate;
    }

    public List<distributionPassenger> getPassengers() {
        return Passangers;
    }

    public class distributionPassenger{
        private String Venue;
        private List<distributionTransport> Transport;

        public String getVenue() {
            return Venue;
        }

        public List<distributionTransport> getTransport() {
            return Transport;
        }
        public class distributionTransport{
            private String TransportType;
            private List<String> PersonIDs;

            public String getTransportType() {
                return TransportType;
            }

            public List<String> getPersonIDs() {
                return PersonIDs;
            }
        }

    }

}
