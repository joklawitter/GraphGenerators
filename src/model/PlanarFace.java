package model;

import java.util.ArrayList;
import java.util.List;

public class PlanarFace {

	/** The index of the face. */
	private final int index;

	private ArrayList<PlanarEdge> edges = new ArrayList<PlanarEdge>(3);

	/**
	 * Constructor setting index of face.
	 * 
	 * @param index
	 */
	public PlanarFace(int index) {
		this.index = index;
	}

	/**
	 * @return the edges, which go ccw around it
	 */
	public List<PlanarEdge> getEdges() {
		return edges;
	}

	/**
	 * @return vertices ccw around the face
	 */
	public ArrayList<PlanarVertex> getVertices() {
		ArrayList<PlanarVertex> vertices = new ArrayList<PlanarVertex>(edges.size());
		for (PlanarEdge planarEdge : edges) {
			vertices.add(planarEdge.getStart());
		}
		return vertices;
	}
	
	/**
	 * @param edges
	 *            the edges to set
	 */
	public void setEdges(ArrayList<PlanarEdge> edges) {
		this.edges = edges;
	}

	/**
	 * @param edgeToAdd
	 * @return whether edge was successful added
	 */
	public boolean addEdgeAtEnd(PlanarEdge edgeToAdd) {
		return edges.add(edgeToAdd);
	}

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
	 * @param edgeToRemove
	 * @return whether edge was successful removed
	 */
	public boolean removeEdge(PlanarEdge edgeToRemove) {
		return edges.remove(edgeToRemove);
	}

	/**
	 * @return the index of this face
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * @return the number of edges around this face
	 */
	public int getSize() {
		return this.edges.size();
	}

	/**
	 * Face is considered valid if
	 * <ul>
	 * <li>all boundary edges have this face as left face</li>
	 * <li>boundary edges form proper chain</li>
	 * </ul>
	 * 
	 * @return whether this face is valid
	 */
	public boolean isValid() {
		// edges in list have this face as left face
		for (PlanarEdge planarEdge : this.edges) {
			if (this != planarEdge.getLeftFace()) {
				return false;
			}
		}

		// edges in list have next edge as next edge
		PlanarEdge current = this.edges.get(getSize() - 1);
		for (PlanarEdge next : this.edges) {
			if (current != next.getPreviousEdge()) {
				System.out.println("Previous edge not correct for " + next + ", should be "
						+ current);
				System.out.println("> next.previous: " + next.getPreviousEdge());
				return false;
			}
			if (current.getNextEdge() != next) {
				System.out.println("Next edge not correct for " + current + ", should be " + next);
				System.out.println("> next.previous: " + current.getNextEdge());
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
		// <0>[size:3]{(0->2)(2->7)(7->0)}
		return "<" + this.getIndex() + ">[size:" + this.getSize() + "]{" + edgesString + "}";
	}


}
