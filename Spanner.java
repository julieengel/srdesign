import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Spanner {
//    Graph baseGraph;
//    double coefficient;
//
//    public Spanner(Graph g, double c) {
//        baseGraph = g;
//        coefficient = c;
//    }
//
//    // TODO: check if this works
//    public Graph buildSpanner() {
//        Edge[] edges = (Edge[]) baseGraph.getEdgeSet().toArray();
//        Arrays.sort(edges);
//
//        Graph output = new Graph(baseGraph.getSize());
//
//        for (int i = 0; i < edges.length; i++) {
//            int u = edges[i].getU();
//            int v = edges[i].getV();
//            int currDist = output.getDistance(u, v);
//            if (currDist < coefficient * baseGraph.getDistance(u, v)) {
//                output.addEdge(edges[i]);
//            }
//        }
//
//        return output;
//    }




    // -------- multigraph version ---------


    MultiGraph baseGraph;
    double coefficient;

    public Spanner(MultiGraph g, double c) {
        baseGraph = g;
        coefficient = c;
    }

    // TODO: check if this works
    public MultiGraph buildSpanner() {
        MultiEdge[] edges = (MultiEdge[]) baseGraph.getEdges().toArray();
        Arrays.sort(edges);

        MultiGraph output = new MultiGraph(baseGraph.getSize());

        for (int i = 0; i < edges.length; i++) {
            int u = edges[i].getU();
            int v = edges[i].getV();
            int currDist = output.getDistance(u, v);
            if (currDist < coefficient * baseGraph.getDistance(u, v)) {
                output.addEdge(edges[i]);
            }
        }

        return output;
    }
}
