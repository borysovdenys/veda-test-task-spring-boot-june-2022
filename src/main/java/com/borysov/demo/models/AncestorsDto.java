package com.borysov.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AncestorsDto {
    private List<Integer> zeroParents;
    private List<Integer> exactlyOneParent;
}
