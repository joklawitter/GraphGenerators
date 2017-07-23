package model;

/**
 * This class implements a directed edge in a planar graph, which has a
 * partnerEdge pointing in the opposite direction. It also stores the face left
 * to it. Edges go ccw along the face.
 *
 * @author Jonathan Klawitter
 */
public class PlanarEdge {

	/** The index of the face. */
	private int index;

	/** The partner/reversed edge of this one. */
	private PlanarEdge partnerEdge;

	/** The face left to this edge. */
	private PlanarFace leftFace;

	/** The start vertex of this edge. End vertex of partner edge. */
	private PlanarVertex start;

	/**
	 * Private constructor. Edges should be generated with factory method.
	 * 
	 * @param startVertex
	 *            start vertex of this edge
	 * @param leftFace
	 *            left face of this edge
	 * @param baseEdge
	 *            underlying simple edge
	 */
	private PlanarEdge(PlanarVertex startVertex, PlanarFace leftFace, int index) {
		this.start = startVertex;
		this.leftFace = leftFace;
		this.index = index;
	}

	/**
	 * Factory method to create a {@link PlanarEdge} and its partner/reversed
	 * edge. Edge goes from start to target, has leftFace to the left and
	 * rightFace to the right. Does not add the edges to vertices!
	 * 
	 * @param start
	 * @param target
	 * @param leftFace
	 * @param rightFace
	 * @param baseEdge
	 * @return {@link PlanarEdge} from start to target, partnerEdge stored
	 *         inside
	 */
	public static PlanarEdge edgeFactory(PlanarVertex start, PlanarVertex target,
			PlanarFace leftFace, PlanarFace rightFace, int index) {
		PlanarEdge leftEdge = new PlanarEdge(start, leftFace, index);
		PlanarEdge rightEdge = new PlanarEdge(target, rightFace, index);
		leftEdge.setReversedEdge(rightEdge);
		rightEdge.setReversedEdge(leftEdge);

		return leftEdge;
	}

	/**
	 * @return start vertex of this edge
	 */
	public PlanarVertex getStart() {
		return this.start;
	}

	/**
	 * @return target vertex of this edge
	 */
	public PlanarVertex getTarget() {
		return this.getReversedEdge().getStart();
	}

	/**
	 * Reset the start vertex of this edge.
	 * 
	 * @param newStart
	 *            new start {@link PlanarVertex}
	 */
	public void setStart(PlanarVertex newStart) {
		this.start = newStart;
	}

	/**
	 * Reset the target vertex of this edge.
	 * 
	 * @param newTarget
	 *            new target {@link PlanarVertex}
	 */
	public void setTarget(PlanarVertex newTarget) {
		this.getReversedEdge().setStart(newTarget);
	}

	/**
	 * @return the index of this face
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Sets the reversed edge of this edge. Can only be done once.
	 * 
	 * @param partnerEdge
	 */
	private void setReversedEdge(PlanarEdge partnerEdge) {
		if (this.partnerEdge != null) {
			throw new IllegalStateException("Edge already has partner edge. " + this + "/"
					+ this.getReversedEdge());
		}
		this.partnerEdge = partnerEdge;
	}

	/**
	 * @return the edge with reversed direction, partner edge
	 */
	public PlanarEdge getReversedEdge() {
		return this.partnerEdge;
	}

	/**
	 * @return the edge next clockwise at the start vertex
	 */
	public PlanarEdge getNextEdgeAtStart() {
		return this.start.getNextEdge(this);
	}

	/**
	 * @return the edge next counter-clockwise along the face (ccw)
	 */
	public PlanarEdge getNextEdge() {
		return this.getReversedEdge().getNextEdgeAtStart();
	}

	/**
	 * @return the edge previous along the face (ccw)
	 */
	public PlanarEdge getPreviousEdge() {
		return this.start.getPreviousEdge(this).getReversedEdge();
	}

	/**
	 * @return the edge next counterclockwise at the start vertex (previous
	 *         clockwise)
	 */
	public PlanarEdge getPreviousEdgeAtStart() {
		return this.start.getPreviousEdge(this);
	}

	/**
	 * @return face to the left of this edge
	 */
	public PlanarFace getLeftFace() {
		return this.leftFace;
	}

	/**
	 * @param leftFace
	 *            the leftFace to set
	 */
	public void setLeftFace(PlanarFace leftFace) {
		this.leftFace = leftFace;
	}

	/**
	 * @return face to the right of this edge
	 */
	public PlanarFace getRightFace() {
		return this.getReversedEdge().getLeftFace();
	}

	/**
	 * Edge is considered valid if
	 * <ul>
	 * <li>left face of next and previous edge is correct</li>
	 * <li>previous edge of next edge at start is partner edge</li>
	 * </ul>
	 * 
	 * @return whether this vertex is valid
	 */
	public boolean isValid() {
		// faces are the same
		if (this.getLeftFace() != this.getNextEdge().getLeftFace()) {
			System.out.println("Left face of " + this + " not same as of its next "
					+ this.getNextEdge());
			System.out.println("> this.leftFace " + this.getLeftFace());
			System.out.println("> next.leftFace " + this.getNextEdge().getLeftFace());
			return false;
		}
		if (this.getLeftFace() != this.getPreviousEdge().getLeftFace()) {
			System.out.println("Left face of " + this + " not same as of its previous "
					+ this.getPreviousEdge());
			System.out.println("> this.leftFace " + this.getLeftFace());
			System.out.println("> next.leftFace " + this.getPreviousEdge().getLeftFace());
			return false;
		}

		// previous edge of next edge at start is partner edge
		if (this.getReversedEdge() != this.getNextEdgeAtStart().getPreviousEdge()) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "(" + this.getStart().getId() + "->" + this.getTarget().getId() + ")["
				+ this.getLeftFace().getIndex() + "/" + this.getRightFace().getIndex() + "]";
	}
}
