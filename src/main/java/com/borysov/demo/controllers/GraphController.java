package com.borysov.demo.controllers;

import com.borysov.demo.models.AncestorsDto;
import com.borysov.demo.services.GraphService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GraphController {
    private final GraphService graphService;

    @ApiOperation(value = "graphs")
    @PostMapping("graphs")
    public Long graphs(@ApiParam(value = "dataset", required = true,
            example = "[ [10, 3], [2, 3], [3, 6], [5, 6], [5, 17], [4, 5], [4, 8], [8, 9] ]")
                       @RequestBody int[][] dataset) {
        return graphService.createGraph(dataset);
    }

    @ApiOperation(value = "task1 - receiving all individuals with zero known parents and all individuals with exactly one known parent.")
    @GetMapping("graphs/{id}/zero-and-exactly-one-parents")
    public AncestorsDto task1(@PathVariable Long id) {
        return graphService.getZeroAndExactlyOneParents(id);
    }

    @ApiOperation(value = "task2 - receiving 'true' if and only if two given individuals in dataset, share at least one known ancestor.")
    @GetMapping("graphs/{id}/is-there-common-ancestor")
    public Boolean task2(@PathVariable Long id, @RequestParam Integer node1, @RequestParam Integer node2) {
        return graphService.isThereCommonAncestor(id, node1, node2);
    }

}
