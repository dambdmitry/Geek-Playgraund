package edu.mitin.performance.model;

import java.util.LinkedList;
import java.util.List;

public class GamesGrid {
    private List<Pair> pairs;

    public GamesGrid() {
        pairs = new LinkedList<>();
    }

    public void addPair(Pair pair) {
        pairs.add(pair);
    }

    public List<Pair> getPairs() {
        return pairs;
    }
}
