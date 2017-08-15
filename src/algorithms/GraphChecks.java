package algorithms;

import java.util.Arrays;
import java.util.Stack;

import model.Graph;
import model.Vertex;

public class GraphChecks {

	/**
	 * Returns whether the given graph is connected.
	 * 
	 * @param graph
	 *            to be checked if it is connected
	 * @return whether the given graph is connected
	 */
	public static boolean isConnected(Graph graph) {
		if (graph == null) {
			throw new IllegalArgumentException("Can't check a null graph.");
		}

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

	/**
	 * Returns whether the given graph is bipartite.
	 * 
	 * @param graph
	 *            to be checked if it is bipartite
	 * @return whether the given graph is bipartite
	 */
	public static boolean isBipartite(Graph graph) {
		if (graph.getN() == 0)
			return true;
		int[] partition = new int[graph.getN()];
		Arrays.fill(partition, -1);

		Stack<Vertex> stack = new Stack<Vertex>();
		stack.add(graph.getVertexById(0));
		partition[0] = 0;

		while (!stack.empty()) {
			Vertex u = stack.pop();
			for (Vertex v : u.getNeighbors()) {
				if (partition[v.getId()] == partition[u.getId()])
					return false;
				if (partition[v.getId()] == -1) {
					partition[v.getId()] = 1 - partition[u.getId()];
					stack.add(v);
				}
			}
		}

		return true;
	}

	/**
	 * Returns whether the given graph is a tree. Note that it returns false if
	 * the graph is a forest.
	 * 
	 * @param graph
	 *            to be checked if it is a tree
	 * @return whether the given graph is a tree
	 */
	public static boolean isTree(Graph graph) {
		if (graph.getM() != (graph.getN() - 1)) {
			return false;
		} else {
			return isConnected(graph);
		}
	}
}
