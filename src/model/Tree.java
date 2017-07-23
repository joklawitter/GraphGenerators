package model;

public class Tree {
	public final int color;
	public final PlanarVertex vertex;
	public Tree parent;
	public PlanarEdge leftEdge;
	public Tree leftChild;
	public PlanarEdge rightEdge;
	public Tree rightChild;

	public Tree(Tree parent, PlanarVertex vertex, int color) {
		this.parent = parent;
		this.vertex = vertex;
		this.color = color;
	}

	public Tree setLeftEdge(PlanarEdge edge) {
		this.leftEdge = edge;
		this.leftChild = new Tree(this, edge.getTarget(), this.color);
		return this.leftChild;
	}

	public Tree setRightEdge(PlanarEdge edge) {
		this.rightEdge = edge;
		this.rightChild = new Tree(this, edge.getTarget(), this.color);
		return this.rightChild;
	}

	public void printTree(String indentation) {
		System.out.println("[v" + vertex.getId() + "](c" + color + ")");

		if (leftChild != null) {
			System.out.print(indentation + "left: " + leftEdge.toString() + " -> ");
			leftChild.printTree(indentation + "|   ");
		}

		if (rightChild != null) {
			System.out.print(indentation + "right: " + rightEdge.toString() + " -> ");
			rightChild.printTree(indentation + "    ");
		}
	}
}
