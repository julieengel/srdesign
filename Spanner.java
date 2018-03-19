import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Spanner {
    MultiGraph baseGraph;
    double coefficient;

    public Spanner(MultiGraph g, double c) {
        baseGraph = g;
        coefficient = c;
    }

    // TODO: more in depth check if this works
    public MultiGraph buildSpanner() {
        MultiGraph output = new MultiGraph(baseGraph.getSize());
        buildSpannerFromBase(output);
        return output;
    }

    public void buildSpannerFromBase(MultiGraph start) {
        Object[] arr = baseGraph.getEdges().toArray();
        MultiEdge[] edges = new MultiEdge[arr.length];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = (MultiEdge) arr[i];
        }
        Arrays.sort(edges);

        for (int i = 0; i < edges.length; i++) {
            if (!start.containsEdge(edges[i])) {

                int u = edges[i].getU();
                int v = edges[i].getV();
                int currDist = start.getDistance(u, v);

                if (coefficient * edges[i].getEdgeWeight() < currDist) {
                    start.addEdge(edges[i]);
                }
            }
        }
    }
}
