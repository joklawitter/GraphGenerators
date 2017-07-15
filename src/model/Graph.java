package model;

import java.util.ArrayList;
import java.util.List;

import algorithms.GraphUtils;

/**
 * This class represents an object oriented implemented graph, containing
 * {@link Vertex vertices} and {@link Edge edges}.
 *
 * @author Jonathan Klawitter
 */
public class Graph {

	private final Vertex[] vertices;
	private List<Edge> edges = new ArrayList<Edge>();

	private final int n;
	private int m;
	private int edgeIdCounter = 0;
	private GraphClass graphClass = GraphClass.UNDEFINED;
	private String name = null;
	private final boolean isDirected;

	public Graph(final int n, boolean directed) {
		this(n, n, directed);
	}

	public Graph(final int n, int m, boolean directed) {
		this.n = n;
		this.vertices = GraphUtils.getInitialVertexArray(n);
		this.edges = new ArrayList<Edge>(m);
		this.m = 0;
		this.isDirected = directed;
	}

	public void addEdge(int firstVertexId, int secondVertexId) {
		this.addEdge(vertices[firstVertexId], vertices[secondVertexId]);
	}

	public void addEdge(Vertex firstVertex, Vertex secondVertex) {
		// if graph is not directed, we set start vertex to have lower id
		if (!isDirected) {
			if (firstVertex.getId() > secondVertex.getId()) {
				Vertex tmp = firstVertex;
				firstVertex = secondVertex;
				secondVertex = tmp;
			}
		}
		this.edges.add(new Edge(firstVertex, secondVertex, edgeIdCounter));
		this.edgeIdCounter++;
		this.m = edges.size();
	}

	public void removeEdge(Edge edgeToRemove) {
		this.edges.remove(edgeToRemove);
		edgeToRemove.getStartVertex().removeEdge(edgeToRemove);
		edgeToRemove.getTargetVertex().removeEdge(edgeToRemove);
	}

	/**
	 * Returns whether this graph is valid. This means that
	 * <ul>
	 * <li>all {@link Vertex vertices} are valid, {@link Vertex#isValid()}</li>
	 * <li>all {@link Edge edges} are valid, {@link Edge#isValid()}</li>
	 * <li>all {@link Edge edges} are valid</li>
	 * </ul>
	 * 
	 * @return whether this graph is valid
	 */
	public boolean isValid() {
		if (n != vertices.length) {
			return false;
		}

		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i].getId() != i) {
				return false;
			}
		}

		if (m != edges.size()) {
			return false;
		}

		// check vertices for validity
		for (Vertex vertex : vertices) {
			if (!vertex.isValid()) {
				return false;
			}
		}

		// check edges for validity
		int[] edgeIds = new int[edgeIdCounter];
		for (Edge edge : edges) {
			edgeIds[edge.getId()]++;
			if (!edge.isValid(isDirected)) {
				return false;
			}
		}
		for (int i = 0; i < edgeIds.length; i++) {
			if (edgeIds[i] > 1) {
				System.out.println("Edges invalid: Two edges have same id: " + i);
				return false;
			}
		}

		// check if incidences add up correctly
		int sumDegree = 0;
		for (Vertex vertex : vertices) {
			sumDegree += vertex.getDegree();
		}
		if (sumDegree != 2 * this.getM()) {
			System.out
					.println("Degree of vertices invalid: sum = " + sumDegree + ", 2m = " + 2 * m);
			return false;
		}

		return true;
	}

	public boolean isDirected() {
		return isDirected;
	}

	// -- GETTERS --
	/**
	 * Returns the number of vertices.
	 * 
	 * @return the number of vertices
	 */
	public int getN() {
		return n;
	}

	/**
	 * Returns the number of edges.
	 * 
	 * @return the number of edges
	 */
	public int getM() {
		return edges.size();
	}

	/**
	 * Returns the density of the graph, i.e. #edges / #possible edges.
	 * 
	 * @return the density of the graph in [0,1]
	 */
	public double getDensity() {
		int maxM = getN() * (getN() - 1) / 2;
		return ((double) getM()) / ((double) maxM);
	}

	/**
	 * Returns the vertex with given id.
	 * 
	 * @return the {@link Vertex} with given id
	 */
	public Vertex getVertexById(int vertexId) {
		return vertices[vertexId];
	}

	/**
	 * Returns the {@link Vertex vertices} of this graph.
	 * 
	 * @return the {@link Vertex vertices} of this graph
	 */
	public Vertex[] getVertices() {
		return vertices;
	}

	/**
	 * Returns the {@link Edge} with given id.
	 * 
	 * @return the {@link Edge} with given id
	 */
	public Edge getEdgeById(int edgeId) {
		for (Edge edge : edges) {
			if (edge.getId() == edgeId) {
				return edge;
			}
		}
		return null;
	}

	/**
	 * Returns the {@link Edge edges} of this graph.
	 * 
	 * @return the {@link Edge edges} of this graph
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * @return the graph class of this graph
	 */
	public GraphClass getGraphClass() {
		return graphClass;
	}

	/**
	 * @param newGraphClass
	 *            new graph class for this graph
	 */
	public void setGraphClass(GraphClass newGraphClass) {
		this.graphClass = newGraphClass;
	}

	/**
	 * Returns the name of this graph, which may be based on its
	 * {@link GraphClass}.
	 * 
	 * @return the name of this graph (might be the empty string or null)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            new name of the graph
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String str = "Graph: n = " + n + ", m = " + m + "\n";
		if (name != null) {
			str += "name = " + name + ", graph class = " + graphClass.name();
		}
		String vis = "";
		for (Vertex vertex : vertices) {
			vis += vertex.toString() + "\n";
		}

		return str + vis;
	}

	/**
	 * Returns a graph with the same structure but with new objects.
	 * 
	 * @return a copy of this graph
	 */
	public Graph copy() {
		Graph copy = new Graph(this.getN(), this.getM(), this.isDirected());

		for (Edge edge : edges) {
			copy.addEdge(edge.getStartVertex().getId(), edge.getTargetVertex().getId());
		}

		copy.setGraphClass(this.graphClass);
		copy.setName(this.name);
		return copy;
	}

}
