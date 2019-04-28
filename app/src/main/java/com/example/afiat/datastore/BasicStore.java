package com.example.afiat.datastore;

public abstract class BasicStore {
    boolean isSynchronized = true;
    abstract void persist();
    abstract void flush();
    abstract void recover();
}
