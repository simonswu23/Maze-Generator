package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {

        List<EdgeWithData<Room, Wall>> edgeList = new ArrayList<>();

        for (Wall w: walls) {
            Room from = w.getRoom1();
            Room to = w.getRoom2();
            double weight = rand.nextDouble();
            EdgeWithData<Room, Wall> edgeWall = new EdgeWithData<>(from, to, weight, w);
            edgeList.add(edgeWall);
        }

        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> temp =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edgeList));

        Collection<EdgeWithData<Room, Wall>> edgeColl = temp.edges();

        Set<Wall> mstWall = new HashSet<>();

        for (EdgeWithData<Room, Wall> w: edgeColl) {
            mstWall.add(w.data());
        }

        return mstWall;
    }
}
