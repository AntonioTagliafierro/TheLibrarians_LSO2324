package com.project.thelibrarians_lso2324.events;

public class BagUpdateEvent {

    private int bagSize;

    public BagUpdateEvent(int cartSize) {
        this.bagSize = bagSize;
    }

    public int getBagSize() {
        return bagSize;
    }
}
