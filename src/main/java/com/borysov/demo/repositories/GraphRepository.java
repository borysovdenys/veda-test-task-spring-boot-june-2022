package com.borysov.demo.repositories;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class GraphRepository {
    private static final Map<Long, int[][]> GRAPH = new ConcurrentHashMap<>();
    private static final AtomicLong SEQUENCE = new AtomicLong();

    public int[][] get(Long id) {
        return GRAPH.get(id);
    }

    public Long save(int[][] dataset) {
        GRAPH.put(SEQUENCE.incrementAndGet(), dataset);
        return SEQUENCE.get();
    }
}
