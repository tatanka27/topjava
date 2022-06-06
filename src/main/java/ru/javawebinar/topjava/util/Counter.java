package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private final AtomicInteger id;

    public Counter(int from) {
        id = new AtomicInteger(from);
    }

    public int getId() {
        return id.getAndIncrement();
    }
}
