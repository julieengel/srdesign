import java.util.*;

class MultiGraph {
    private int N;
    private MultiNode[] nodes; // by id
    private List<MultiEdge> edges; // all edges
    GraphVis gv;

    public MultiGraph(int n) { // For random graphs
        N = n;
        nodes = new MultiNode[n];
        for (int i = 0; i < N; i++) {
            nodes[i] = new MultiNode(i, "Station " + i);
        }
        edges = new ArrayList<>();
    }

    public MultiGraph(CSVParser parser) {
        N = parser.getN();
        // Add nodes
        nodes = new MultiNode[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new MultiNode(i, parser.getStationNames().get(i));
        }
        // Use addEdge() function to make the edges
        edges = new ArrayList<>();
        int[][] trainEdges = parser.getTrainEdges();
        int[][] walkingEdges = parser.getWalkingEdges();
        Line[][] edgeColors = parser.getEdgeColors();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // Add train edge if exists
                if (trainEdges[i][j] != 0) {
                    addEdge(i, j, trainEdges[i][j], false, edgeColors[i][j]);
                }
                if (walkingEdges[i][j] != 0) {
                    addEdge(i, j, walkingEdges[i][j], true, Line.WALK);
                }
            }
        }
    }

    public MultiGraph(MultiGraph g) { // For spanner copying
        N = g.getSize();
        nodes = new MultiNode[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new MultiNode(i, g.getNodes()[i].getStationName());
        }
        edges = new ArrayList<>();

        for (MultiEdge e : g.getEdges()) {
            addEdge(e.getU(), e.getV(), e.getEdgeWeight(), e.getWalking(), e.getLine());
        }
    }

    int getSize() {
        return N;
    }

    public void addEdge(int u, int v, int weight, boolean walking, Line line) {
        MultiEdge newEdge = new MultiEdge(u, v, weight, walking, line);
        addEdge(newEdge);
    }

    public void addEdge(MultiEdge newEdge) {
        int u = newEdge.getU();
        int v = newEdge.getV();

        nodes[u].addEdge(v, newEdge);
        nodes[v].addEdge(u, newEdge);

        edges.add(newEdge);
    }

    public void removeEdge(int u, int v, int w) {
        // TODO: will need to change this for multi graph
        MultiEdge edgeToRemove = nodes[u].getEdge(v);
        nodes[u].removeEdge(v);
        nodes[v].removeEdge(u);
        edges.remove(edgeToRemove);

    }

    public boolean containsEdge(MultiEdge e) {
        return edges.contains(e);
    }

    public void removeEdge(MultiEdge e) {
        nodes[e.getU()].removeEdge(e.getV());
        nodes[e.getV()].removeEdge(e.getU());
        edges.remove(e);
    }

    public List<MultiEdge> getEdges() {
        return edges;
    }

    public MultiNode[] getNodes() {
        return nodes;
    }

    public int[] getNeighbors(int u) {
        return nodes[u].getNeighbors();
    }

    public int getEdgeWeight(int u, int v) {
        // TODO: check about getting correct edge when multigraph
        return getEdge(u, v).getEdgeWeight();
    }

    public MultiEdge getEdge(int u, int v) {
        return nodes[u].getEdge(v);
    }

    MultiGraph subtractGraph(MultiGraph g) {
        MultiGraph subtraction = new MultiGraph(this);
        for (MultiEdge e : g.getEdges()) {
            subtraction.removeEdge(e);
        }
        return subtraction;
    }



    // TODO: dont have any of the randomizing graph and connected stuff transferred over
    // Didn't update to include walking or line because we won't need it
//    void randomize(double p) {
//        // Build random graph using Erdos-Renyi construction
//        if (p < 0.0 || p > 1.0)
//            throw new IllegalArgumentException("Probability must be between 0 and 1");
//        for (int v = 0; v < N; v++) {
//            for (int u = v + 1; u < N; u++) {
//                if (Math.random() <= p) {
//
//                    // TODO: check if we still want weight 1
//                    addEdge(v, u, 1);
//                }
//            }
//        }
//        // Confirm that each vertex has degree >= 2
//        for (int i = 0; i < N; i++) {
//            int count = nodes[i].getNeighbors().length;
//            while (count < 2) {
//                int randNode = (int) Math.round(Math.random() * (N - 1));
//                while (randNode == i || nodes[i].isNeighbor(randNode)) {
//                    randNode = (int) Math.round(Math.random() * (N - 1));
//                }
//                addEdge(i, randNode, 1);
//                count++;
//            }
//        }
//
//        // TODO: CONFIRM THAT IT'S CONNECTED
//    }



    // ---- distance calcs -----

    private static int minVertex(int[] dist, boolean[] v) {
        int x = Integer.MAX_VALUE;
        int y = -1;
        for (int i = 0; i < dist.length; i++) {
            if (!v[i] && dist[i] < x) {
                y = i;
                x = dist[i];
            }
        }
        return y;
    }

    int[] dijsktra(int u) {
        int[] dist = new int[N];
        boolean[] visited = new boolean[N];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[u] = 0;

        for (int i = 0; i < dist.length; i++) {
            int next = minVertex(dist, visited);

            // TODO: check this is what we want to do
            if (next == -1) {
                continue;
            }

            visited[next] = true;

            int[] neighbors = getNeighbors(next);
            for (int j = 0; j < neighbors.length; j++) {
                int v = neighbors[j];
                int d = dist[next] + getEdge(next, v).getEdgeWeight();
                if (dist[v] > d) {
                    dist[v] = d;
                }
            }
        }

        return dist;
    }

    int[] dijsktraForPath(int u) {
        int[] dist = new int[N];
        int[] pred = new int[N];
        boolean[] visited = new boolean[N];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[u] = 0;

        for (int i = 0; i < dist.length; i++) {
            int next = minVertex(dist, visited);

            // TODO: check this is what we want to do
            if (next == -1) {
                continue;
            }

            visited[next] = true;

            int[] neighbors = getNeighbors(next);
            for (int j = 0; j < neighbors.length; j++) {
                int v = neighbors[j];
                int d = dist[next] + getEdge(next, v).getEdgeWeight();
                if (dist[v] > d) {
                    dist[v] = d;
                    pred[v] = next;
                }
            }
        }

        return pred;
    }

    List<Integer> getPathFromTo(int u, int v) {
        List<Integer> path = new ArrayList<>();
        int[] paths = dijsktraForPath(u);

        int index = v;
        path.add(v);
        while (index != u) {
            int prev = paths[index];
            path.add(0, prev);
            index = prev;
        }

        return path;
    }

    int getDistance(int u, int v) {
        int[] distances = dijsktra(u);
        return distances[v];
    }



    // ----------  -------------


    // TODO: need to figure this out
    // CHANGED WITH THE ADDING OF WALKING EDGES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public Set<MultiGraph> generateSpanners(double coefficient, double baseline) {
        Set<MultiGraph> spanners = new HashSet<MultiGraph>();
        Set<MultiEdge> notRemovedEdges = new HashSet<MultiEdge>();

        // Originally, no edges have been seen so add them all to the set
        for (MultiEdge e : edges) {
            if (!e.getWalking()) {
                // Only add when it's not a walking edge!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                notRemovedEdges.add(e);
            }
        }

        MultiGraph curr = new MultiGraph(this);

        // Generate the first spanner
        Spanner spanner = new Spanner(this, coefficient, baseline);
        MultiGraph firstSpanner = spanner.buildSpanner();
        spanners.add(firstSpanner);

        MultiGraph firstComplement = curr.subtractGraph(firstSpanner);

        for (MultiEdge e : firstComplement.getEdges()) {
            // All of the edges not in the spanner have been removed
            notRemovedEdges.remove(e);
        }

        // value to determine whether we reach infinite loop
        int notRemovedPrevCount = notRemovedEdges.size();

        // While there are still things that haven't been removed
        while (!notRemovedEdges.isEmpty()) {

            // Make copy of original graph to modify
            curr = new MultiGraph(this);

            // Remove all not yet removed edges from the original graph and see if it connects
            for (MultiEdge e : notRemovedEdges) {
                curr.removeEdge(e);
            }

            spanner.buildSpannerFromBase(curr);

            spanners.add(curr);
            MultiGraph complement = subtractGraph(curr);

            for (MultiEdge e : complement.getEdges()) {
                notRemovedEdges.remove(e);
            }

            if (notRemovedEdges.size() == notRemovedPrevCount) {
                System.out.println("HAD TO EXIT");
                System.out.println("NOT REMOVED = " + notRemovedPrevCount);
                for (MultiEdge edge : notRemovedEdges) {
                    int u = edge.getU();
                    int v = edge.getV();
                    System.out.println("" + u + " - " + v + " : "
                            + nodes[u].getStationName() + " - " + nodes[v].getStationName()
                            + " : " + edge.getEdgeWeight() + " min");
                }
                break;
            } else {
                notRemovedPrevCount = notRemovedEdges.size();
            }
            System.out.println("SPANNERS count = " + spanners.size());

        }
        return spanners;
    }

    // TODO: BE CAREFUL OF WALKING EDGES ONCE MERGED
    public Set<Set<MultiEdge>> generateMaintenanceSets(Set<MultiGraph> spanners) {
        Set<Set<MultiEdge>> sets = new HashSet<>();
        for (MultiGraph s : spanners) {
            Set<MultiEdge> subset = new HashSet<>();
            MultiGraph complement = subtractGraph(s);
            for (MultiEdge e : complement.getEdges()) {
                if (!e.getWalking()) {
                    subset.add(e);
                }
            }
            sets.add(subset);
        }
        return sets;
    }




    // ----------- GRAPH VIS ------------


    public void createGraphVis() {
        gv = new GraphVis(this);
    }

    public void displayGraphVis() {
        gv.display();
    }


    private class UF {
        private int[] parent;  // parent[i] = parent of i
        private byte[] rank;   // rank[i] = rank of subtree rooted at i (never more than 31)
        private int count;     // number of components

        /**
         * Initializes an empty union–find data structure with {@code n} sites
         * {@code 0} through {@code n-1}. Each site is initially in its own
         * component.
         *
         * @param  n the number of sites
         * @throws IllegalArgumentException if {@code n < 0}
         */
        public UF(int n) {
            if (n < 0) throw new IllegalArgumentException();
            count = n;
            parent = new int[n];
            rank = new byte[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        /**
         * Returns the component identifier for the component containing site {@code p}.
         *
         * @param  p the integer representing one site
         * @return the component identifier for the component containing site {@code p}
         * @throws IllegalArgumentException unless {@code 0 <= p < n}
         */
        public int find(int p) {
            validate(p);
            while (p != parent[p]) {
                parent[p] = parent[parent[p]];    // path compression by halving
                p = parent[p];
            }
            return p;
        }

        /**
         * Returns the number of components.
         *
         * @return the number of components (between {@code 1} and {@code n})
         */
        public int count() {
            return count;
        }

        /**
         * Returns true if the the two sites are in the same component.
         *
         * @param  p the integer representing one site
         * @param  q the integer representing the other site
         * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
         *         {@code false} otherwise
         * @throws IllegalArgumentException unless
         *         both {@code 0 <= p < n} and {@code 0 <= q < n}
         */
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        /**
         * Merges the component containing site {@code p} with the
         * the component containing site {@code q}.
         *
         * @param  p the integer representing one site
         * @param  q the integer representing the other site
         * @throws IllegalArgumentException unless
         *         both {@code 0 <= p < n} and {@code 0 <= q < n}
         */
        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;

            // make root of smaller rank point to root of larger rank
            if      (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
            else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
            else {
                parent[rootQ] = rootP;
                rank[rootP]++;
            }
            count--;
        }

        // validate that p is a valid index
        private void validate(int p) {
            int n = parent.length;
            if (p < 0 || p >= n) {
                throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));
            }
        }
    }


    public void testAlgo(int u, int v, Set<MultiGraph> spanners) {
        if (u < 0 || v < 0 || u >= N || v >= N) {
            System.out.println("Invalid u or v");
            return;
        }

        double coefficient = 1.5;
        double baseline = 15;

//        Set<MultiGraph> spanners = this.generateSpanners(coefficient, baseline);
//        Set<Set<MultiEdge>> sets = this.generateMaintenanceSets(spanners);

        int count = 0;
        for (MultiGraph spanner : spanners) {
            System.out.println("SPANNER #" + count);
            count++;


            System.out.println("Route from " + u + " to " + v);

            System.out.println("Original time: " + this.getDistance(u, v));
            System.out.println("New time: " + spanner.getDistance(u, v));

            List<Integer> path = this.getPathFromTo(u, v);
            System.out.print("Old path:");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(" " + path.get(i));
            }
            System.out.println();

            path = spanner.getPathFromTo(u, v);
            System.out.print("New path:");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(" " + path.get(i));
            }
            System.out.println();

            System.out.println();
        }
    }




    public static void main(String[] args) {
        // baseline 20 works, 14 makes an extra spanner
        double coefficient = 2.5;
        double additive = 5;


        String trainEdgesFilename = "TrainEdgesCSV.csv";
        String walkingEdgesFilename = "WalkingEdgesCSV.csv";
        String edgeColorsFilename = "EdgeColorsCSV.csv";
        String stationNamesFilename = "StationNamesCSV.csv";
        CSVParser parser = new CSVParser(trainEdgesFilename, walkingEdgesFilename, edgeColorsFilename,
                stationNamesFilename);
        parser.parse();
        MultiGraph g = new MultiGraph(parser);

        // remove later
        Line line = g.getEdge(34, 35).getLine();
        g.removeEdge(34, 35, 1);
        g.addEdge(34, 35, 2, false, line);


        Set<MultiGraph> spanners = g.generateSpanners(coefficient, additive);
        for (MultiGraph sp : spanners) {
            sp.createGraphVis();
            sp.displayGraphVis();
        }

        g.createGraphVis();
        g.displayGraphVis();

        GraphIO.outputMaintenanceSets(g.generateMaintenanceSets(spanners), coefficient, additive);

        // problem old time 11 min new time 17 min why
        g.testAlgo(7, 15, spanners);
        g.testAlgo(9, 19, spanners);
        g.testAlgo(34, 35, spanners);

//        MultiGraph ugh = new MultiGraph(g);
//        ugh.removeEdge(34, 35, 1);
//        System.out.println(ugh.getDistance(34, 35));
//
//        for (MultiGraph spanner : spanners) {
//            ugh = spanner;
//
//            for (int k = 0; k < ugh.getSize(); k++) {
//                for (int l = k + 1; l < ugh.getSize(); l++) {
//                    System.out.println("old: " + g.getDistance(k, l) + " new: " + ugh.getDistance(k, l));
//                }
//            }
//
//            System.out.println();
//        }


        // debugging and info prints
        int count = 0;
        Set<Set<MultiEdge>> sets = g.generateMaintenanceSets(spanners);
        for (Set<MultiEdge> set : sets) {
            System.out.println("MAINTENANCE SET #" + count);
            count++;
            for (MultiEdge e : set) {
                System.out.println(g.getNodes()[e.getU()].getStationName()
                        + " - " + g.getNodes()[e.getV()].getStationName());
            }
            System.out.println();
        }

        count = 0;
        for (MultiGraph spanner : spanners) {
            System.out.println("SPANNER #" + count);
            count++;
            System.out.println(spanner.getDistance(19, 9));
            List<Integer> path = spanner.getPathFromTo(19, 9);
            for (int i = 0; i < path.size(); i++) {
                System.out.println(path.get(i));
            }
            System.out.println();
        }
    }

}


class MultiEdge implements Comparable<MultiEdge> {
    private final int u;
    private final int v;
    private final int weight;
    private final boolean walking;
    private final Line line;


    public MultiEdge(int u, int v, int weight, boolean walking, Line line) {
        this.u = u;
        this.v = v;
        this.weight = weight;
        this.walking = walking;
        this.line = line;
    }

    public int getEdgeWeight() {
        return weight;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public boolean getWalking() {
        return walking;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public int compareTo(MultiEdge o) {
        if (this.weight < o.weight) {
            return -1;
        } else if (this.weight > o.weight) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
           "null instanceof [type]" also returns false */
        if (!(o instanceof MultiEdge)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        MultiEdge e = (MultiEdge) o;

        // Compare the data members and return accordingly
        return (this.u == e.u) && (this.v == e.v) && (this.weight == e.weight)
                && (this.line == e.line) && (this.walking == e.walking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v, weight, walking, line);
    }
}

class MultiNode {
    int id;
    String stationName;
    Map<Integer, List<MultiEdge>> neighbors;

    public MultiNode(int id, String stationName) {
        this.id = id;
        this.stationName = stationName;
        neighbors = new HashMap<>();
    }

    public void addEdge(int u, MultiEdge e) {
        if (!neighbors.containsKey(u)) {
            neighbors.put(u, new ArrayList<>());
        }
        neighbors.get(u).add(e);

    }

    public void removeEdge(int v) {
        neighbors.remove(v);
    }

    // TODO: right now only returns first edge in list
    public MultiEdge getEdge(int v) {
        if (neighbors.containsKey(v)) {
            return neighbors.get(v).get(0);
        }
        return null;
    }

    public boolean isNeighbor(int neighborID) {
        return neighbors.keySet().contains(neighborID);
    }

    public int[] getNeighbors() {
        Integer[] intArr = new Integer[neighbors.keySet().size()];
        intArr = neighbors.keySet().toArray(intArr);
        int[] output = new int[intArr.length];
        for (int i = 0; i < intArr.length; i++) {
            output[i] = intArr[i];
        }
        return output;
    }

    public String getStationName() {
        return stationName;
    }

}
