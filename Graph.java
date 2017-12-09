import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.lang.Math;

// TO-DO: generateSTs()

class Graph {
	private int[][] am;
	private int n;
	private int m;
	private Set<Edge> edges;
	
	public Graph (int n) {
		// INITIALIZES EMPTY GRAPH
		this.n = n;
		am = new int[n][n];
		// HAVE TO CALL RANDOMIZE IN ORDER TO ACTUALLY GET EDGES
	}
	
	public Graph (int[][] am) {
		this.am = am;
		this.n = am.length;
		makeEdgeSet();
	}
	
	public void setEdges(Set<Edge> es) {
		edges = es;
	}
	
	void randomize(double p) {
		// Build random graph using Erdos-Renyi construction
		if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        for (int v = 0; v < n; v++) {
            for (int w = v + 1; w < n; w++) {
                if (StdRandom.bernoulli(p)) {
                    addEdge(v, w, 1);
                }
            }
        }
        // Confirm that each vertex has degree >= 2
        for (int i = 0; i < n; i++) {
        		int count = 0;
        		for (int j = 0; j < n; j++) {
        			if (am[i][j] != 0) {
        				count++;
        			}
        		}
        		while (count < 2) {
        			int e = (int) Math.round(Math.random() * (n-1));
        			while (e == i || am[i][e] != 0) {
        				e = (int) Math.round(Math.random() * (n-1));
        			}
        			addEdge(i, e, 1);
        			count++;
        		}
        }
        
        // CONFIRM THAT IT'S CONNECTED
        
        makeEdgeSet();
	}
	
	void addEdge(int u, int v, int w) {
		if (u >= am.length || v >= am.length || u < 0 || v < 0) {
			throw new IllegalArgumentException();
		}
		am[u][v] = w;
		am[v][u] = w;
		m++;
	}
	
	void printGraphMatrix() {
		System.out.print("    ");
		for (int i = 0; i < n; i++) {
			if (i < 10) {
				System.out.print(0);
			}
			System.out.print(i + "  ");
		}
		System.out.println();
		for (int i = 0; i < n; i++) {
			if (i < 10) {
				System.out.print("0");
			}
			System.out.print(i + ": ");
			
			for (int j = 0; j < n; j++) {
				if (am[i][j] < 10) {
					System.out.print(0);
				}
				System.out.print(am[i][j] + "  ");
			}
			System.out.println();
		}
	}
	
	void assignRandomWeights() {
		// Create randomly ordered list of weights between 0 and m
		List<Integer> s = new ArrayList<Integer>();
		for (int i = 1; i <= m; i++) {
			s.add(i);
		}
		Collections.shuffle(s);
		
		// Assign weights to edges
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (am[i][j] != 0) {
					addEdge(i, j, s.remove(0));
				}
			}
		}
	}
	
	Graph kruskals() {
		// Create MST structure
		int[][] mst = new int[n][n];
		int edgeCount = 0;
		
        // more efficient to build heap by passing array of edges
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        for (int i = 0; i < n; i++) {
        		for (int j = i + 1; j < n; j++) {
        			if (am[i][j] > 0) {
        				Edge e = new Edge(i, j, am[i][j]);
        				pq.add(e);
        			}
        		}
        }

        // run greedy algorithm
        UF uf = new UF(n);
        while (!pq.isEmpty() && edgeCount < n - 1) {
            Edge e = pq.remove();
            int u = e.getU();
            int v = e.getV();
            if (!uf.connected(u, v)) { // u-v does not create a cycle
                uf.union(u, v);  // merge u and v components
                mst[u][v] = e.getEdgeWeight();  // add edge e to mst
                mst[v][u] = e.getEdgeWeight();
                edgeCount++;
                // weight += e.weight();
            }
        }
        Graph mstGraph = new Graph(mst);
        mstGraph.makeEdgeSet();
        return mstGraph;

        // check optimality conditions
        //assert check(G);
    }
	
	boolean isConnected() {
		boolean[] seen = new boolean[n];
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(0);
		seen[0] = true;
		while (!queue.isEmpty()) {
			int curr = queue.remove();
			for (int j = 0; j < n; j++) {
				if (am[curr][j] != 0 && !seen[j]) {
					queue.add(j);
					seen[j] = true;
				}
			}
		}
		for (int i = 0; i < n; i++) {
			if (!seen[i]) {
				return false;
			}
		}
		return true;
	}

	// isa start
	void removeEdge(Edge e) {
		//if (edges.contains(e)) {
	        am[e.getU()][e.getV()] = 0;
	        am[e.getV()][e.getU()] = 0;
	        edges.remove(e); // This might not work b/c different references
		//}
    }

    void addEdge(Edge e) {
	    addEdge(e.getU(), e.getV(), e.getEdgeWeight());
	    edges.add(e);
    }

	Set<Edge> getEdgeSet() {
	    return edges;
    }

	Graph subtractGraph(Graph g) {
	    Graph subtraction = new Graph(am);
	    for (Edge e : g.getEdgeSet()) {
	        subtraction.removeEdge(e);
        }
        return subtraction;
    }

    Set<Edge> getNotRemoved(Map<Edge, Boolean> removed) {
	    Set<Edge> notRemoved = new HashSet<Edge>();
	    for (Edge e : removed.keySet()) {
	        if (!removed.get(e)) { notRemoved.add(e); }
        }
        return notRemoved;
    }

	Set<Graph> generateSTs() {
		Set<Graph> sts = new HashSet<Graph>(); // Might not all be STs in the case where the complement works 
		Set<Edge> notRemovedEdges = new HashSet<Edge>();
		
		// Originally, no edges have been seen so add them all to the set
		for (Edge e : edges) {
		    notRemovedEdges.add(e);
        }
		Graph curr = new Graph(am);
		// Generate the first ST
		Graph firstST = curr.kruskals();
		System.out.println("FIRST ST--------");
		firstST.printGraphMatrix();
		System.out.println();
		sts.add(firstST);
		
//		Graph firstComplement = curr.subtractGraph(firstST);
		
		Graph firstComplement = new Graph(am);
		System.out.println("firstComplement original edges = " + firstComplement.getEdgeSet().size());
		for (Edge e : firstST.getEdgeSet()) {
			firstComplement.removeEdge(e);
		}
		
		System.out.println("FIRST COMPLEMENT--------");
		firstComplement.printGraphMatrix();
		System.out.println();
		for (Edge e : firstComplement.getEdgeSet()) {
			// All of the edges not in the ST have been removed
			notRemovedEdges.remove(e);
		}
		
		// While there are still things that haven't been removed
		while (!notRemovedEdges.isEmpty()) {
			// Make copy of original graph to modify
			curr = new Graph(am);
			
			// Remove all not yet removed edges from the original graph and see if it connects
			for (Edge e : notRemovedEdges) {
				curr.removeEdge(e);
			}

			// Initialize a union-find structure
			UF uf = new UF(n);
			// System.out.println("numComps before = " + uf.count());
			// Union components that have edges
			for (Edge e : curr.getEdgeSet()) {
				uf.union(e.getU(), e.getV());
			}
			// System.out.println("numComps after = " + uf.count());
			
			while (uf.count() != 1) { // POTENTIAL INFINITE LOOP IF IT DOESN'T WORK
				// A	dd back edges
				int index = (int) (Math.random() * notRemovedEdges.size()); // Add back random edge from the ST, but NO GUARANTEE THEY ALL GET PICKED?
				Edge e = (Edge) notRemovedEdges.toArray()[index];
				int u = e.getU();
				int v = e.getV();
				if (!uf.connected(u, v)) { // u-v does not create a cycle
					uf.union(u, v);  // merge u and v components
					curr.addEdge(e);
	            }
			}
			
			sts.add(curr);
			Graph complement = subtractGraph(curr);
			for (Edge e : complement.getEdgeSet()) {
				// All of the edges not in the ST have been removed
				notRemovedEdges.remove(e);
			}
		}
		return sts;
	}
	
	public void makeEdgeSet() {
		edges = new HashSet<Edge>();
		for (int i = 0; i < n; i++) {
	    		for (int j = i + 1; j < n; j++) {
	    			if (am[i][j] > 0) {
	    				Edge e = new Edge(i, j, am[i][j]);
	    				edges.add(e); // NOTE: random edge weights assigned?
	    			}
	    		}
		}
	}
	
	boolean allRemoved(Map<Edge, Boolean> removedEdges) {
		for (Edge e : removedEdges.keySet()) {
			if (!removedEdges.get(e)) {
				return false;
			}
		}
		return true;
	}
	




    public static void main(String[] args) {
        Graph g = new Graph(5);
        g.randomize(.5);
        //g.assignRandomWeights();
        g.printGraphMatrix();
        System.out.println();
        System.out.println();
        Set<Graph> sts = g.generateSTs();
        for (Graph gr : sts) {
        		gr.printGraphMatrix();
        		System.out.println();
        }
        //System.out.println(g.isConnected());
//		Graph mst = new Graph(g.kruskals());
//		mst.printGraphMatrix();
    }
	

	
	private class Edge implements Comparable<Edge>{
		int u;
		int v;
		int w;
		
		public Edge(int u, int v, int w) {
			if (u < 0 || v < 0 || u >= n || v >= n) {
				throw new IllegalArgumentException();
			}
			this.u = u;
			this.v = v;
			this.w = w;
		}
		
		public int getEdgeWeight() {
			return w;
		}
		
		public int getU() {
			return u;
		}
		
		public int getV() {
			return v;
		}
		
		@Override
		public int compareTo(Edge e) {
			if (this.w > e.getEdgeWeight()) {
				return 1;
			} else if (this.w == e.getEdgeWeight()) {
				return 0;
			} else {
				return -1;
			}
		}
		
		@Override
	    public boolean equals(Object o) {
			// ISSUE: DOESNT GET CALLED BY CONTAINS()
			
			
			System.out.println("EQUALS");
	        // If the object is compared with itself then return true  
	        if (o == this) {
	            return true;
	        }
	 
	        /* Check if o is an instance of Complex or not
	          "null instanceof [type]" also returns false */
	        if (!(o instanceof Edge)) {
	            return false;
	        }
	         
	        // typecast o to Complex so that we can compare data members 
	        Edge e = (Edge) o;
	         
	        // Compare the data members and return accordingly
	        System.out.println("U: " + this.u + " " + e.u);
	        System.out.println("V: " + this.v + " " + e.v);
	        System.out.println("W: " + this.w + " " + e.w);
	        return (this.u == e.u) && (this.v == e.v) && (this.w == e.w);
	    }
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

}
