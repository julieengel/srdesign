import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Spanner {
    MultiGraph baseGraph;
    double coefficient;
    double additive = 13;

    public Spanner(MultiGraph g, double c, double a) {
        baseGraph = g;
        coefficient = c;
        additive = a;
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

                // Old additive way
                if ((coefficient * edges[i].getEdgeWeight()) + additive < currDist) {

                // this is wrong
//                MultiGraph check = new MultiGraph(start);
//                check.addEdge(edges[i]);
//                boolean underBaseline = true;
//
//                for (int k = 0; k < baseGraph.getSize(); k++) {
//                    for (int l = k + 1; l < baseGraph.getSize(); l++) {
//                        int dist = check.getDistance(k, l);
//                        if (dist > coefficient * edges[i].getEdgeWeight() && dist > baseline) {
//                            underBaseline = false;
//                        }
//                    }
//                }

//                if (currDist > (coefficient * edges[i].getEdgeWeight())
//                        && currDist > baseline) {
//                System.out.println()
//                if (currDist > (coefficient * edges[i].getEdgeWeight())
//                        && newDist > baseline) {
//                        && !underBaseline) {
//                        ) {
                    start.addEdge(edges[i]);
                }
            }
        }

        for (int i = 0; i < edges.length; i++) {
            if (!start.containsEdge(edges[i])) {

                int u = edges[i].getU();
                int v = edges[i].getV();
                int currDist = start.getDistance(u, v);

                if ((coefficient * edges[i].getEdgeWeight()) + additive < currDist) {
                    start.addEdge(edges[i]);
                }
            }
        }


//        for (int i = 0; i < edges.length; i++) {
//            if (!start.containsEdge(edges[i])) {
//                MultiGraph check = new MultiGraph(start);
//                check.addEdge(edges[i]);
//
////                boolean underBaseline = true;
//
//                for (int u = 0; u < baseGraph.getSize(); u++) {
//                    for (int v = u + 1; v < baseGraph.getSize(); v++) {
//                        int dist = check.getDistance(u, v);
//                        int oldDist = start.getDistance(u, v);
//                        if (oldDist > coefficient * edges[i].getEdgeWeight() + additive) {
//                            //underBaseline = false;
//                            System.out.println("HI");
//                        }
//                    }
//                }
//
//                //if (underBaseline) { start.addEdge(edges[i]); }
//            }
//        }

        for (int u = 0; u < baseGraph.getSize(); u++) {
            for (int v = u + 1; v < baseGraph.getSize(); v++) {
                int currDist = start.getDistance(u, v);
                if (currDist > coefficient * baseGraph.getDistance(u, v) + additive) {
                    System.out.println("HI");
                }
            }
        }
    }
}
