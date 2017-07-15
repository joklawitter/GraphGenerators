package algorithms;

import model.Graph;
import model.GraphClass;

public class GraphGeneration {

	/**
	 * Returns a graph without edges, called
	 * <a href = "https://en.wikipedia.org/wiki/Null_graph#Edgeless_graph">
	 * edgeless graph</a>.
	 * 
	 * @param numVertices
	 *            number of vertices the new edgeless graph with have
	 * @return an edgeless graph of given size
	 */
	public static Graph edgelessGraph(int numVertices) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		Graph edgelessGraph = new Graph(numVertices, 0, false);
		edgelessGraph.setName("\bar{K_{" + numVertices + "}}");
		edgelessGraph.setGraphClass(GraphClass.EDGELESS);
		return edgelessGraph;
	}

	/**
	 * Creates a <a href = "https://en.wikipedia.org/wiki/Path_graph">path
	 * graph</a> of given length (in terms of edges). This means it has (length
	 * + 1) vertices.
	 * 
	 * @param length
	 *            in terms of edges of path
	 * @return path of given length
	 */
	public static Graph createPathGraph(int length) {
		if (length < 0) {
			throw new IllegalArgumentException("Path graph can't have negative length.");
		}

		Graph pathGraph = new Graph(length + 1, length, false);
		pathGraph.setName("P_{" + length + "}");
		pathGraph.setGraphClass(GraphClass.PATH);
		for (int i = 0; i < length; i++) {
			pathGraph.addEdge(i, i + 1);
		}
		return pathGraph;

	}

	/**
	 * Creates a <a href = "https://en.wikipedia.org/wiki/Cycle_graph">cycle</a>
	 * with given number of vertices.
	 * 
	 * @param numVertices
	 *            number of vertices the new cycle will have
	 * @return cycle with given number of vertices
	 */
	public static Graph createCycle(int numVertices) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		Graph cycle = new Graph(numVertices, numVertices, false);
		cycle.setName("C_{" + numVertices + "}");
		cycle.setGraphClass(GraphClass.CYCLE);
		if (numVertices > 1) {
			for (int i = 0; i < numVertices - 1; i++) {
				cycle.addEdge(i, i + 1);
			}
		}
		if (numVertices > 2) {
			cycle.addEdge(0, numVertices - 1);
		}
		return cycle;
	}

	/**
	 * Creates a
	 * <a href = "https://en.wikipedia.org/wiki/Star_(graph_theory)">star</a>
	 * with given number of vertices.
	 * 
	 * @param numVertices
	 *            number of vertices the new star will have
	 * @return star with given number of vertices
	 */
	public static Graph createStar(int numVertices) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		Graph star = new Graph(numVertices, numVertices, false);
		star.setName("S_{" + numVertices + "}");
		star.setGraphClass(GraphClass.STAR);
		for (int i = 1; i < numVertices; i++) {
			star.addEdge(0, i);
		}
		return star;
	}
}
