package com.example.navigation.modules;

import java.util.Date;
import java.util.List;

public class Trip {
    private String tripId;
    private String name;
    private Date startDate;
    private Date endDate;
    private String destination;
    private List<ItineraryItem> itinerary;
    private List<Image> photos;
    //private Budget budget;
    //private List<Collaborator> collaborators;

    public void addItineraryItem(ItineraryItem item) {
        //TODO
    }
    public void removeItineraryItem(ItineraryItem item) {
        //TODO
    }
    public void uploadPhoto(Image img) {
        //TODO
    }
    public void shareTrip(User user) {
        //TODO
    }
}

