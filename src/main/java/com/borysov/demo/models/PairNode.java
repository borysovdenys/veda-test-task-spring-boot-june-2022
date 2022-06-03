package com.borysov.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PairNode {
    private Integer parent;
    private Integer child;
}
