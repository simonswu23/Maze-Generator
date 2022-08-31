package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }


    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> pred = new HashMap<>();
        if (Objects.equals(start, end)) {
            return pred;
        }
        ExtrinsicMinPQ<V> unprocessed = createMinPQ();
        Map<V, Double> distance = new HashMap<>();
        HashSet<V> processed = new HashSet<>();

        V thisVertex = start;
        double thisWeight = 0.0;
        unprocessed.add(thisVertex, thisWeight);
        V endCheck = null;
        while (!unprocessed.isEmpty()) {
            processed.add(thisVertex);
            endCheck = unprocessed.removeMin();
            if (Objects.equals(endCheck, end)) {
                break;
            }
            for (E edge : graph.outgoingEdgesFrom(thisVertex)) {
                V toVertex = edge.to();
                if (!processed.contains(toVertex)) {
                    double totalWeight = thisWeight + edge.weight();
                    if (!pred.containsKey(toVertex)) {
                        unprocessed.add(toVertex, totalWeight);
                        pred.put(toVertex, edge);
                        distance.put(toVertex, totalWeight);
                    } else if (totalWeight < distance.get(toVertex)) {
                        unprocessed.changePriority(toVertex, totalWeight);
                        pred.put(toVertex, edge);
                        distance.put(toVertex, totalWeight);
                    }
                }
            }
            if (!unprocessed.isEmpty()) {
                thisVertex = unprocessed.peekMin();
                thisWeight = distance.get(thisVertex);
            }
        }
        return pred;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        E edge = spt.get(end);
        V vertex = end;
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }
        List<E> path = new ArrayList<>();
        while (!Objects.equals(vertex, start)) {
            path.add(edge);
            vertex = edge.from();
            edge = spt.get(vertex);
        }
        Collections.reverse(path);
        return new ShortestPath.Success<>(path);
    }

}
