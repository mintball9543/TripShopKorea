package com.example.tripshopkorea;

public interface Subject {
    void addObserver(DatabaseObserver observer);
    void removeObserver(DatabaseObserver observer);
    void notifyObservers();
}
