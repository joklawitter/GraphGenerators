package model;

import java.util.ArrayList;

/**
 * This class implements a vertex in a planar graph with, which stores adjacent
 * edges clockwise (cw).
 *
 * @author Jonathan Klawitter
 */
public class PlanarVertex {

	/** The index of the vertex. */
	private final int index;

	/** The edges of this vertex in cw order. */
	protected ArrayList<PlanarEdge> edges = new ArrayList<PlanarEdge>();

	/**
	 * Constructor setting index of vertex.
	 * 
	 * @param index
	 */
	public PlanarVertex(int index) {
		this.index = index;
	}

	// - - - CONCERNING PROPERTIES - - -
	/**
	 * @return the index of this vertex
	 */
	public int getId() {
		return this.index;
	}

	/**
	 * @return the degree of this vertex
	 */
	public int getDegree() {
		return this.edges.size();
	}

	// - - - CONCERNING EDGES - - -
	/**
	 * Adds an edge into the order after the given one.
	 * 
	 * @param toAdd
	 *            {@link PlanarEdge} to add
	 * @param after
	 *            {@link PlanarEdge} behind which other one gets added
	 */
	public void addEdgeAfter(PlanarEdge toAdd, PlanarEdge after) {
		int index = edges.indexOf(after) + 1;
		edges.add(index, toAdd);
	}

	/**
	 * Adds an edge into the order at the end.
	 * 
	 * @param toAdd
	 *            {@link PlanarEdge} to add
	 */
	public void addEdgeAtEnd(PlanarEdge toAdd) {
		edges.add(toAdd);
	}

	/**
	 * Removes the edge from its order.
	 * 
	 * @param edgeToRemove
	 *            edge to be removed
	 */
	public void removeEdge(PlanarEdge edgeToRemove) {
		this.edges.remove(edgeToRemove);
	}

	/**
	 * @param planarEdge
	 * @return the {@link PlanarEdge} after the given one in cw order
	 */
	public PlanarEdge getNextEdge(PlanarEdge planarEdge) {
		int index = edges.indexOf(planarEdge);
		index = (index == edges.size() - 1) ? 0 : index + 1;
		return edges.get(index);
	}

	/**
	 * @param planarEdge
	 * @return the {@link PlanarEdge} before the given one in cw order
	 */
	public PlanarEdge getPreviousEdge(PlanarEdge planarEdge) {
		int index = edges.indexOf(planarEdge);
		index = (index == 0) ? edges.size() - 1 : index - 1;
		return edges.get(index);
	}

	/**
	 * @return the edges of this graph in clockwise order
	 */
	public ArrayList<PlanarEdge> getEdges() {
		return this.edges;
	}

	// - - - CONCERNING NEIGHBORS - - -
	/**
	 * @return the neighbors of this vertex in clockwise order
	 */
	public ArrayList<PlanarVertex> getNeighborsCW() {
		ArrayList<PlanarVertex> neighbours = new ArrayList<PlanarVertex>(this.getDegree());
		for (PlanarEdge planarEdge : edges) {
			neighbours.add(planarEdge.getTarget());
		}
		return neighbours;
	}
	
	/**
	 * @return the neighbors of this vertex in counterclockwise order
	 */
	public ArrayList<PlanarVertex> getNeighborsCCW() {
		ArrayList<PlanarVertex> neighboursCW = getNeighborsCW();
		ArrayList<PlanarVertex> neighboursCCW = new ArrayList<PlanarVertex>(this.getDegree());
		for (int i = neighboursCW.size() - 1; i >= 0; i--) {
			neighboursCCW.add(neighboursCW.get(i));
		}
		return neighboursCCW;
	}

	/**
	 * Returns whether this vertex has the given vertex as neighbor.
	 * 
	 * @param possibleNeighbor
	 *            vertex to be checked if neighbor of this one
	 * @return whether given vertex is neighbor
	 */
	public boolean hasNeighbor(PlanarVertex possibleNeighbor) {
		for (PlanarEdge planarEdge : edges) {
			if (planarEdge.getTarget() == possibleNeighbor) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param target
	 * @return edge to target, or null if target is not neighbor of this vertex
	 */
	public PlanarEdge getEdgeTo(PlanarVertex target) {
		for (PlanarEdge planarEdge : edges) {
			if (planarEdge.getTarget() == target) {
				return planarEdge;
			}
		}

		return null;
	}

	// - - - MISC - - -
	/**
	 * Vertex is considered valid if
	 * <ul>
	 * <li>all edges have this vertex as start vertex</li>
	 * <li>faces between edges fit</li>
	 * </ul>
	 * 
	 * @return whether this vertex is valid
	 */
	public boolean isValid() {
		// edges in list have this face as left face
		for (PlanarEdge planarEdge : this.edges) {
			if (this != planarEdge.getStart()) {
				return false;
			}
		}

		// edges adjacent in list have same face between them
		PlanarEdge current = this.edges.get(getDegree() - 1);
		for (PlanarEdge next : this.edges) {
			if (current.getRightFace() != next.getLeftFace()) {
				System.out.println("Face not correct between " + current + " and " + next);
				System.out.println("> " + current.getRightFace() + " vs " + next.getLeftFace());
				return false;
			}
			current = next;
		}

		return true;
	}

	@Override
	public String toString() {
		String edgesString = "";
		for (PlanarEdge planarEdge : edges) {
			edgesString += planarEdge.toString();
		}
		// <0>[deg:3]{(0->2)(0->7)(0->9)}
		return "<" + this.getId() + ">[deg:" + this.getDegree() + "]{" + edgesString + "}";
	}

}
