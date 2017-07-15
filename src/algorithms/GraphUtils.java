package algorithms;

import model.Graph;
import model.Vertex;

import java.util.Arrays;
import java.util.Stack;

public class GraphUtils {
	
	/**
	 * Initializes a {@link Vertex} array with vertices.
	 * 
	 * @param vertices
	 *            array to be initialized
	 * @return vertex array with set indices
	 */
	public static Vertex[] initVerticesArray(Vertex[] vertices) {
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex(i);
		}
		return vertices;
	}
	
	public static Vertex[] getInitialVertexArray(int n) {
		Vertex[] vertices = new Vertex[n]; 
		return initVerticesArray(vertices);
	}
	
	public static boolean isBipartite(Graph graph) {
		if (graph.getN() == 0) return true;
		int[] partition = new int[graph.getN()];
		Arrays.fill(partition, -1);

		Stack<Vertex> stack = new Stack<Vertex>();		
		stack.add(graph.getVertexById(0));
		partition[0] = 0;

		while (!stack.empty()) {
			Vertex u = stack.pop();
			for (Vertex v : u.getNeighbors()) {				
				if (partition[v.getId()] == partition[u.getId()]) return false;
				if (partition[v.getId()] == -1) {
					partition[v.getId()] = 1 - partition[u.getId()];
					stack.add(v);
				}
			}
		}

		return true;
	}

	public static boolean isTree(Graph graph) {
		if (graph.getM() == graph.getN() - 1) { // check connectedness
			boolean[] visited = new boolean[graph.getN()];
			Arrays.fill(visited, false);

			Stack<Vertex> stack = new Stack<Vertex>();
			stack.add(graph.getVertexById(0));
			visited[0] = true;
			int numVisited = 1;
			while (!stack.empty()) {
				Vertex u = stack.pop();
				for (Vertex v : u.getNeighbors()) {
					if (!visited[v.getId()]) {
						numVisited++;
						visited[v.getId()] = true;
						stack.add(v);
					}
				}
			}

			return numVisited == graph.getN();
		}

		return false;
	}

}
