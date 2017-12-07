
class Graph {
	private int[][] am;
	private int n;
	
	public Graph (int n) {
		this.n = n;
		am = new int[n][n];
	}
	
	void randomize(double p) {
		// Build random graph using Erdos-Renyi construction
		if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        for (int v = 0; v < n; v++) {
            for (int w = v+1; w < n; w++) {
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
	}
	
	void addEdge(int u, int v, int w) {
		if (u >= am.length || v >= am.length || u < 0 || v < 0) {
			throw new IllegalArgumentException();
		}
		am[u][v] = w;
		am[v][u] = w;
	}
	
	void printGraphMatrix() {
		System.out.print("    ");
		for (int i = 0; i < n; i++) {
			System.out.print(i + "  ");
		}
		System.out.println();
		for (int i = 0; i < n; i++) {
			System.out.print(i + ": ");
			if (i < 10) {
				System.out.print(" ");
			}
			for (int j = 0; j < n; j++) {
				System.out.print(am[i][j] + "  ");
				if (j >= 10) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Graph g = new Graph(12);
		g.randomize(.5);
		g.printGraphMatrix();
	}
}
