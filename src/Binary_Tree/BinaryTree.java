package Binary_Tree;

public class BinaryTree {
	Node root;
	
	public BinaryTree(int value) {
		root = new Node(value);
	}
	
	public void addLeft(Node node) {
		node.left = new Node(0);
	}
	
	public void addRight(Node node) {
		node.right = new Node(1);
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
}