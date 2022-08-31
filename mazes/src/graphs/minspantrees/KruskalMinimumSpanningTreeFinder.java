package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    @SuppressWarnings("checkstyle:CommentsIndentation")
    protected DisjointSets<V> createDisjointSets() {
        //return new QuickFindDisjointSets<>();
        return new UnionBySizeCompressingDisjointSets<>();
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();

        List<V> vertices = new ArrayList<>(graph.allVertices());

        Set<E> mstEdgeCol = new HashSet<>();

        int numEdge = 0;

        for (V vertex: vertices) {
            disjointSets.makeSet(vertex);
        }

        for (E edge: edges) {
            V from = edge.from();
            V to = edge.to();
            int head1 = disjointSets.findSet(from);
            int head2 = disjointSets.findSet(to);
            if (head1 != head2) {
                disjointSets.union(from, to);
                mstEdgeCol.add(edge);
                numEdge++;
            }
        }

        // ask TA why there is an error here.

        if ((vertices.size() -1 != numEdge && numEdge != 0) || 
            (edges.size() == 0 && (vertices.size() > 1))) {
            return new MinimumSpanningTree.Failure<>();
        }

        return new MinimumSpanningTree.Success<>(mstEdgeCol);
    }
}
