package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Counted {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    private final int id;

    public Counted() {
        id = COUNTER.getAndIncrement();
    }

    public int getId() {
        return id;
    }
}
