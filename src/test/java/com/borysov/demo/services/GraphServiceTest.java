package com.borysov.demo.services;

import com.borysov.demo.models.AncestorsDto;
import com.borysov.demo.repositories.GraphRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class GraphServiceTest {
    private final GraphRepository graphRepository = new GraphRepository();
    protected final GraphService graphService = new GraphService(graphRepository);

    static Stream<Arguments> provideGraphAndLists() {
        return Stream.of(
                Arguments.of(new int[][]{{10, 3}, {2, 3}, {3, 6}, {5, 6}, {5, 17}, {4, 5}, {4, 8}, {8, 9}},
                        List.of(10, 2, 4),
                        List.of(5, 17, 8, 9)),
                Arguments.of(new int[][]{{10, 3}, {2, 3}, {3, 6}},
                        List.of(10, 2),
                        List.of(6)),
                Arguments.of(new int[][]{{10, 3}, {2, 3}, {3, 6}, {5, 6}, {5, 17}, {4, 5}, {4, 8}, {8, 9}, {9, 11}, {6, 13}, {14, 10}},
                        List.of(2, 4, 14),
                        List.of(10, 13, 11, 5, 17, 8, 9)),
                Arguments.of(new int[][]{{10, 3}, {2, 3}, {3, 6}, {5, 6}, {5, 17}, {4, 5}, {4, 8}, {8, 9}, {99, 88}, {100, 88}, {88, 77}},
                        List.of(10, 2, 4, 99, 100),
                        List.of(5, 17, 8, 9, 77))
        );
    }

    static Stream<Arguments> provideNodesAndCorrectResult() {
        return Stream.of(
                Arguments.of(5, 8, true),
                Arguments.of(6, 8, true),
                Arguments.of(3, 8, false),
                Arguments.of(3, 2, false),
                Arguments.of(13, 11, false),
                Arguments.of(88, 77, true),
                Arguments.of(88, 100, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideGraphAndLists")
    public void findZeroAndExactlyOneParentsGraphTest(int[][] dataset, List<Integer> zeroParents, List<Integer> exactlyOneParent) {
        Long idGraph = graphService.createGraph(dataset);

        AncestorsDto zeroAndExactlyOneParentsGraph = graphService.getZeroAndExactlyOneParents(idGraph);

        assertThat(zeroAndExactlyOneParentsGraph.getZeroParents(), containsInAnyOrder(zeroParents.toArray()));
        assertThat(zeroAndExactlyOneParentsGraph.getExactlyOneParent(), containsInAnyOrder(exactlyOneParent.toArray()));
    }

    @ParameterizedTest
    @MethodSource("provideNodesAndCorrectResult")
    public void isThereCommonAncestorTest(Integer node1, Integer node2, Boolean expected) {
        int[][] dataset = new int[][]{{10, 3}, {2, 3}, {3, 6}, {5, 6}, {5, 17}, {4, 5}, {4, 8}, {8, 9}, {99, 88}, {100, 88}, {88, 77}};
        Long idGraph = graphService.createGraph(dataset);
        Assertions.assertEquals(expected, graphService.isThereCommonAncestor(idGraph, node1, node2));
    }
}
