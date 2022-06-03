package com.borysov.demo.services;

import com.borysov.demo.models.AncestorsDto;
import com.borysov.demo.models.PairNode;
import com.borysov.demo.repositories.GraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraphService {
    private final GraphRepository graphRepository;

    /**
     * Method for saving in memory an input graph
     *
     * @param dataset input graph
     * @return id of stored in memory graph
     */
    public Long createGraph(int[][] dataset) {
        return graphRepository.save(dataset);
    }

    /**
     * Receiving all individuals with zero known parents and all individuals with exactly one known parent
     *
     * @param idGraph stored in memory ID of graph
     * @return dto with zero and exactly one known parent lists
     */
    public AncestorsDto getZeroAndExactlyOneParents(Long idGraph) {
        int[][] dataset = graphRepository.get(idGraph);
        return findZeroAndExactlyOneParentsGraph(dataset);
    }

    /**
     * Receiving true if and only if two given individuals in dataset, share at least one known ancestor
     *
     * @param idGraph stored in memory ID of graph
     * @param node1   first node
     * @param node2   second node
     * @return boolean result
     */
    public Boolean isThereCommonAncestor(Long idGraph, Integer node1, Integer node2) {
        int[][] dataset = graphRepository.get(idGraph);
        return hasCommonAncestor(dataset, node1, node2);
    }

    private AncestorsDto findZeroAndExactlyOneParentsGraph(int[][] dataset) {
        Set<Integer> parents = new HashSet<>();
        List<Integer> children = new ArrayList<>();

        Arrays.stream(dataset).forEach(ints -> {
            parents.add(ints[0]);
            children.add(ints[1]);
        });

        List<Integer> zeroParents = getZeroParents(parents, children);
        List<Integer> exactlyOneParent = getExactlyOneParent(children);

        return new AncestorsDto(zeroParents, exactlyOneParent);
    }

    private List<Integer> getZeroParents(Set<Integer> parents, List<Integer> children) {
        return parents.stream().filter(value -> !children.contains(value)).collect(Collectors.toList());
    }

    private List<Integer> getExactlyOneParent(List<Integer> children) {
        return children.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private boolean hasCommonAncestor(int[][] dataset, Integer node1, Integer node2) {

        List<PairNode> pairs = new ArrayList<>();
        for (int[] ints : dataset) {
            PairNode pair = new PairNode(ints[0], ints[1]);
            pairs.add(pair);
        }
        List<Integer> node1Ancestor = new ArrayList<>();
        List<Integer> node2Ancestor = new ArrayList<>();
        addParentInList(node1Ancestor, pairs, node1);
        addParentInList(node2Ancestor, pairs, node2);
        return node1Ancestor.stream()
                .anyMatch(node2Ancestor::contains);
    }

    private void addParentInList(List<Integer> parents, List<PairNode> pairs, Integer node) {
        List<PairNode> parentChildPair = new ArrayList<>();
        for (PairNode pairNode : pairs) {
            if (pairNode.getChild().equals(node) && !parents.contains(pairNode.getParent())) {
                parentChildPair.add(pairNode);
            }
        }
        if (!parentChildPair.isEmpty()) {
            parentChildPair.forEach(pair -> {
                parents.add(pair.getParent());
                addParentInList(parents, pairs, pair.getParent());
            });
        }
    }
}
