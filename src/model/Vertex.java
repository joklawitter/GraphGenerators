package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A vertex of a {@link Graph}.
 * 
 * @author Jonathan Klawitter
 */
public class Vertex implements Comparable<Vertex> {

	private final int id;

	private List<Edge> edges = new ArrayList<Edge>();

	public Vertex(int id) {
		if (id < 0) {
			throw new IllegalArgumentException("Vertex can't have negative id.");
		}
		this.id = id;
	}

	public int getId() {
		return id;
	}

	protected void addEdge(Edge edge) {
		if ((edge.getStartVertex() != this) && (edge.getTargetVertex() != this)) {
			throw new IllegalArgumentException("Edge added to wrong vertex. Edge: "
					+ edge.toString() + ", this vertex: " + this.toString());
		}
		edges.add(edge);
	}

	protected void removeEdge(Edge edgeToRemove) {
		edges.remove(edgeToRemove);
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void shuffleEdges() {
		Collections.shuffle(edges);
	}

	public int getDegree() {
		return edges.size();
	}

	public Vertex[] getNeighbors() {
		Vertex[] neighbors = new Vertex[getDegree()];
		List<Edge> edges = getEdges();
		for (int i = 0; i < edges.size(); ++i) {
			if (edges.get(i).getStartVertex().equals(this)) {
				neighbors[i] = edges.get(i).getTargetVertex();
			} else {
				neighbors[i] = edges.get(i).getStartVertex();
			}
		}

		return neighbors;
	}

	private boolean isNeighbor(Vertex vertexToCheck) {
		for (Vertex neighbor : getNeighbors()) {
			if (neighbor.equals(vertexToCheck)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether this vertex is valid, meaning it has valid id, no loop
	 * edges, no multi-edges and each of its neighbor sees it as a neighbor.
	 * 
	 * @return whether this vertx is valid
	 */
	public boolean isValid() {
		if (id < 0) {
			return false;
		}

		for (Edge edge : edges) {
			Vertex neighbor = edge.getOtherEnd(this);

			// check for loops
			if (neighbor == this || neighbor.getId() == this.getId()) {
				System.out.println("Error, edge loop: " + this);
				return false;
			}

			// check if neighbor vertex has this vertex as neighbor
			if (!neighbor.isNeighbor(this)) {
				return false;
			}

			// check for multi edge
			for (Edge secondEdge : edges) {
				if (edge == secondEdge) {
					continue;
				} else {
					if (secondEdge.getOtherEnd(this).equals(neighbor)) {
						System.out.println("Error, edge twice:" + this);
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String str = "V<" + id + ">[dgr" + getDegree() + "]";
		for (Edge edge : edges) {
			str += edge.toString();
		}
		return str;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Vertex))
			return false;
		Vertex v = (Vertex) other;

		return this.id == v.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public int compareTo(Vertex otherVertex) {
		return Integer.compare(this.getId(), otherVertex.getId());
	}
}
