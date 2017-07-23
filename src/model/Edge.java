package model;

/**
 * An edge of a {@link Graph}. If the edge is considered undirected, then the id
 * of the start {@link Vertex} should be smaller than the id of the target
 * vertex.
 * 
 * @author Jonathan Klawitter
 */
public class Edge implements Comparable<Edge> {

	public static final int NON_GRAPH_EDGE_ID = -2;
	private final int id;
	private final Vertex start;
	private final Vertex target;

	/**
	 * Creates an edge for given two vertices and given id number. If the graph
	 * of this is undirected, then start vertex should have smaller id than
	 * target vertex.
	 * 
	 * @param start
	 * @param target
	 * @param id
	 */
	public Edge(Vertex start, Vertex target, int id) {
		if ((start == null) || (target == null)) {
			throw new IllegalArgumentException("Edge can't initialise with null-vertex.");
		}
		if ((id < 0) && (id != NON_GRAPH_EDGE_ID)) {
			throw new IllegalArgumentException("Edge can't have negative id.");
		}

		this.id = id;
		this.start = start;
		this.target = target;

		start.addEdge(this);
		target.addEdge(this);
	}

	/**
	 * Returns whether this edge is valid, i.e. whether end vertices exist and
	 * have this edge in edge list, and if edge should be undirected, if
	 * vertices are in correct order.
	 * 
	 * @param isDirected
	 *            whether edge is directed or undirected
	 * @return whether this edge is valid
	 */
	public boolean isValid(boolean isDirected) {
		boolean valid = true;
		if (!isDirected) {
			valid &= (this.start.getId() < this.target.getId());
		}
		valid &= this.start.getEdges().contains(this);
		valid &= this.target.getEdges().contains(this);
		return valid;
	}

	public int getId() {
		return id;
	}

	public Vertex getStartVertex() {
		return start;
	}

	public Vertex getTargetVertex() {
		return target;
	}

	/**
	 * Returns both end vertices of edge.
	 * 
	 * @return both end vertices of edge
	 */
	public Vertex[] getVertices() {
		return new Vertex[] { start, target };
	}

	/**
	 * Returns for one given vertex the vertex on the end of the edge.
	 * 
	 * @param currentVertex
	 *            one vertex of the edge
	 * @return the other end of the edge
	 * @throws IllegalArgumentException
	 *             if vertex is not part of edge, can check with
	 *             {@link #isVertexOfEdge(Vertex)} beforehand
	 */
	public Vertex getOtherEnd(Vertex currentVertex) {
		if (currentVertex.equals(start)) {
			return getTargetVertex();
		} else if (currentVertex.equals(target)) {
			return getStartVertex();
		} else {
			throw new IllegalArgumentException("Vertex not part of this edge.");
		}
	}

	/**
	 * Returns whether given vertex is an end vertex of this edge.
	 * 
	 * @param vertexToCheck
	 *            vertex to check whether part of the edge
	 * @return whether given vertex is an end vertex of this edge
	 */
	public boolean isVertexOfEdge(Vertex vertexToCheck) {
		return (start.equals(vertexToCheck) || target.equals(vertexToCheck));
	}

	/**
	 * Returns true if index for start and source are the same. They don't have
	 * to be the same object instances.
	 * 
	 * @return if indices of vertices are the same
	 */
	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof Edge) {
			Edge that = (Edge) other;
			result = (this.getStartVertex().getId() == that.getStartVertex().getId()
					&& this.getTargetVertex().getId() == that.getTargetVertex().getId());
		}
		return result;
	}

	/**
	 * An edge is considered smaller than an other edge, if its start vertex has
	 * a smaller id, or in case of equality, if the end vertex has a smaller id.
	 */
	public int compareTo(Edge otherEdge) {
		int thisStartIndex = this.getStartVertex().getId();
		int otherStartIndex = otherEdge.getStartVertex().getId();
		int thisTargetIndex = this.getTargetVertex().getId();
		int otherTargetIndex = otherEdge.getTargetVertex().getId();

		int startComparison = Integer.compare(thisStartIndex, otherStartIndex);
		return (startComparison != 0) ? startComparison
				: Integer.compare(thisTargetIndex, otherTargetIndex);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = result * 4003 + getStartVertex().getId();
		result = result * 31 + getTargetVertex().getId();
		return result;
	}

	@Override
	public String toString() {
		return "E[" + id + "](" + start.getId() + "->" + target.getId() + ")";
	}

}
