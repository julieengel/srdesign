import scala.collection.mutable.MutableList;

import java.util.*;

class MultiGraph {
    private int N;
    private MultiNode[] nodes;
    //private Map<Integer, List<MultiEdge>> neighbors;
    private List<MultiEdge> edges;
    GraphVis gv;

    public MultiGraph(int n) {
        N = n;
        nodes = new MultiNode[n];
        for (int i = 0; i < N; i++) {
            nodes[i] = new MultiNode(i);
        }
//        neighbors = new HashMap<>();
//        for (int i = 0; i < n; i++) {
//            neighbors.put(i, new ArrayList<>());
//        }
        edges = new ArrayList<>();
    }

    int getSize() {
        return N;
    }

    public void addEdge(int u, int v, int weight) {
        MultiEdge newEdge = new MultiEdge(u, v, weight);
        addEdge(newEdge);
    }

    public void addEdge(MultiEdge newEdge) {
        int u = newEdge.getU();
        int v = newEdge.getV();

//        neighbors.get(u).add(newEdge);
//        neighbors.get(v).add(newEdge);

        nodes[u].addEdge(v, newEdge);
        nodes[v].addEdge(u, newEdge);

        edges.add(newEdge);
    }

    // TODO: probably want a way to not make this public
    public List<MultiEdge> getEdges() {
        return edges;
    }

    public MultiNode[] getNodes() {
        return nodes;
    }

    public int[] getNeighbors(int u) {
        // TODO: check if correct
        return nodes[u].getNeighbors();
    }

    public int getEdgeWeight(int u, int v) {
        // TODO: check about getting correct edge
        return getEdge(u, v).getEdgeWeight();
    }

    public MultiEdge getEdge(int u, int v) {
        return nodes[u].getEdge(v);
    }



    // TODO: dont have any of the randomizing graph and connected stuff transferred over
    void randomize(double p) {
        // Build random graph using Erdos-Renyi construction
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        for (int v = 0; v < N; v++) {
            for (int u = v + 1; u < N; u++) {
                if (Math.random() <= p) {

                    // TODO: check if we still want weight 1
                    addEdge(v, u, 1);
                }
            }
        }
        // Confirm that each vertex has degree >= 2
        for (int i = 0; i < N; i++) {
            int count = nodes[i].getNeighbors().length;
            while (count < 2) {
                int randNode = (int) Math.round(Math.random() * (N - 1));
                while (randNode == i || nodes[i].isNeighbor(randNode)) {
                    randNode = (int) Math.round(Math.random() * (N - 1));
                }
                addEdge(i, randNode, 1);
                count++;
            }
        }

        // TODO: CONFIRM THAT IT'S CONNECTED
    }


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
        //int[] pred = new int[n];
        boolean[] visited = new boolean[N];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[u] = 0;

        for (int i = 0; i < dist.length; i++) {
            int next = minVertex(dist, visited);
            visited[next] = true;

            int[] neighbors = getNeighbors(next);
            for (int j = 0; j < neighbors.length; j++) {
                int v = neighbors[j];
                int d = dist[next] + getEdge(next, v).getEdgeWeight();
                if (dist[v] > d) {
                    dist[v] = d;
                    //pred[v] = next;
                }
            }
        }

        return dist;
    }

    int getDistance(int u, int v) {
        int[] distances = dijsktra(u);
        return distances[v];
    }


    // ----------- GRAPH VIS ------------


    public void createGraphVis() {
        gv = new GraphVis(this);
    }

    public void displayGraphVis() {
        gv.display();
    }

}




class MultiEdge implements Comparable<MultiEdge> {
    private int u;
    private int v;
    private int weight;

    public MultiEdge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
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
        return (this.u == e.u) && (this.v == e.v) && (this.weight == e.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v, weight);
    }
}

class MultiNode {
    int id;
    Map<Integer, List<MultiEdge>> neighbors;
    //List<MultiEdge> edges;

    public MultiNode(int id) {
        this.id = id;
        neighbors = new HashMap<>();
    }

    public void addEdge(int u, MultiEdge e) {
        //edges.add(e);
        if (!neighbors.containsKey(u)) {
            neighbors.put(u, new ArrayList<>());
        }
        neighbors.get(u).add(e);

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

}
