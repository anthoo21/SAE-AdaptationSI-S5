package com.example.saedolistocks5.outilsdivers;

public class Quartet<A, B, C, D> {
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    public Quartet(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    public C third() {
        return third;
    }

    public D fourth() {
        return fourth;
    }
}
