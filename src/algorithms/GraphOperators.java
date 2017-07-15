package algorithms;

import java.util.Random;

import model.Edge;
import model.Graph;
import model.GraphClass;
import model.Vertex;

public class GraphOperators {

	/**
	 * Shuffles the graph.
	 * 
	 * @param graph
	 *            graph that has to be undirected
	 * @return a copy of graph with vertices shuffled
	 */
	public static Graph shuffleGraph(Graph graph) {
		if (graph == null) {
			throw new IllegalArgumentException();
		}

		Graph shuffledGraph = new Graph(graph.getN(), graph.getM(), graph.isDirected());
		shuffledGraph.setGraphClass(graph.getGraphClass());
		shuffledGraph.setName(graph.getName());

		int[] permutation = RandomUtil.randomPermutation(graph.getN());
		for (Edge edge : graph.getEdges()) {
			graph.addEdge(permutation[edge.getStartVertex().getId()],
					permutation[edge.getTargetVertex().getId()]);
		}

		for (Vertex vertex : graph.getVertices()) {
			vertex.shuffleEdges();
		}

		return shuffledGraph;
	}

	/**
	 * Thins out the given graph by only taking edges in the returned graph with
	 * given probability. New graph has given {@link GraphClass} and name.
	 * 
	 * @param graph
	 *            to be thinned out
	 * @param probability
	 *            to take edges
	 * @param random
	 *            using this random
	 * @param graphClass
	 * @param name
	 * @return copy of given graph thinned out by given probability
	 */
	public static Graph thinOutGraph(Graph graph, double probability, Random random,
			GraphClass graphClass, String name) {
		if (graph == null) {
			throw new IllegalArgumentException();
		}
		if ((probability < 0) || (probability > 1)) {
			throw new IllegalArgumentException("Illegal probaility: " + probability);
		}
		if (random == null) {
			random = RandomUtil.getRandom();
		}
		Graph thinGraph = new Graph(graph.getN(), graph.getM(), graph.isDirected());
		thinGraph.setGraphClass(graph.getGraphClass());
		thinGraph.setName(graph.getName());

		for (Edge edge : graph.getEdges()) {
			if (random.nextDouble() <= probability) {
				thinGraph.addEdge(edge.getStartVertex().getId(), edge.getTargetVertex().getId());
			}
		}

		return thinGraph;
	}

	/**
	 * Thins out the given graph by only taking edges in the returned graph with
	 * given probability. Passes on graph class and graph name to new graph.
	 * 
	 * @param graph
	 *            to be thinned out
	 * @param probability
	 *            to take edges
	 * @param random
	 *            using this random
	 * @return copy of given graph thinned out by given probability
	 */
	public static Graph thinOutGraph(Graph graph, double probability, Random random) {
		return thinOutGraph(graph, probability, random, graph.getGraphClass(), graph.getName());
	}

	/**
	 * Creates the Cartesian product of two given graphs. Resulting graph will
	 * only be directed if both input graphs were directed.
	 * 
	 * @param firstGraph
	 *            first {@link Graph} of the product
	 * @param secondGraph
	 *            second {@link Graph} of the product
	 * @return Cartesian product of the two given graphs
	 */
	public static Graph createGraphProduct(Graph firstGraph, Graph secondGraph) {
		if ((firstGraph == null) || (secondGraph == null)) {
			throw new IllegalArgumentException();
		}

		int n1 = firstGraph.getN();
		int n2 = secondGraph.getN();
		Graph productGraph = new Graph(n1 * n2, n1 * secondGraph.getM() + firstGraph.getM() * n2,
				firstGraph.isDirected() && secondGraph.isDirected());

		// make n2 copies of first graph
		for (Edge edge : firstGraph.getEdges()) {
			for (int i = 0; i < n2; i++) {
				int idAddtivite = i * n1;
				productGraph.addEdge(edge.getStartVertex().getId() + idAddtivite,
						edge.getTargetVertex().getId() + idAddtivite);
			}
		}

		// add connections between first graph copies according to second graph
		for (Edge edge : secondGraph.getEdges()) {
			for (int i = 0; i < n1; i++) {
				productGraph.addEdge(edge.getStartVertex().getId() * n1 + i,
						edge.getTargetVertex().getId() * n1 + i);
			}
		}

		return productGraph;
	}

}
