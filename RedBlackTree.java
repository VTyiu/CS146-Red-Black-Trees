package cs146F20.tang.project4;

/*
 * Models a balanced Red Black Tree that holds nodes with String values
 * extends Comparable<Key> for comparing the Nodes
 */
public class RedBlackTree<Key extends Comparable<Key>> {	
		private RedBlackTree.Node<String> root;

		public static class Node<Key extends Comparable<Key>> { //changed to static 
			
			  Key key;  		  
			  Node<String> parent;
			  Node<String> leftChild;
			  Node<String> rightChild;
			  boolean isRed;
			  int color;
			  
			  public Node(Key data){
				  this.key = data;
				  leftChild = null;
				  rightChild = null;
			  }		
			  
			  public int compareTo(Node<Key> n){ 	//this < that  <0
			 		return key.compareTo(n.key);  	//this > that  >0
			  }
			  
			  public boolean isLeaf(){
				  //if (this.equals(root) && this.leftChild == null && this.rightChild == null) return true;
				  //if (this.equals(root)) return false;
				  if (this.leftChild == null && this.rightChild == null){
					  return true;
				  }
				  return false;
			  }
		}

		 public boolean isLeaf(RedBlackTree.Node<String> n){
			  if (n.equals(root) && n.leftChild == null && n.rightChild == null) return true;
			  if (n.equals(root)) return false;
			  if (n.leftChild == null && n.rightChild == null){
				  return true;
			  }
			  return false;
		  }
		
		public interface Visitor<Key extends Comparable<Key>> {
			/**
			This method is called at each node.
			@param n the visited node
			*/
			void visit(Node<Key> n);
		}
		
		public void visit(Node<Key> n){
			System.out.println(n.key);
		}
		
		public void printTree(){  //preorder: visit, go left, go right
			RedBlackTree.Node<String> currentNode = root;	
			printTree(currentNode);
		}
		
		public void printTree(RedBlackTree.Node<String> node){
			System.out.print(node.key);
			if (node.isLeaf()){
				return;
			}
			printTree(node.leftChild);
			printTree(node.rightChild);
		}
		
		/*
		 * place a new node in the RB tree with data the parameter and color it red. 
		 * @param data the String value to set the newNode with
		 */
		public void addNode(String data){  	//this < that  <0.  this > that  >0
			Node<String> newNode = new Node<String>(data);
			newNode.isRed = true;
			newNode.color = 0;
			
			// newNode is root
			if(root == null) {
				root = newNode;
			}
			else {
				Node<String> current = root;
				while(current != null) {
					if(newNode.compareTo(current) < 0) { // if newNode is less than current
						if(current.leftChild == null) {
							current.leftChild = newNode;
							newNode.parent = current;
							break;
						}
						current = current.leftChild;
					}
					else if(newNode.compareTo(current) > 0) { // if newNode is greater than current
						if(current.rightChild == null) {
							current.rightChild = newNode;
							newNode.parent = current;
							break;
						}
						current = current.rightChild;
					}
				}
			}
			this.fixTree(newNode);
		}	

		public void insert(String data){
			addNode(data);	
		}
		
		/*
		 * looks up the String target in the RedBlackTree
		 * @param k the String target to look up
		 * @return the node containing the target String that is being looked up or null if the target is not found
		 */
		public RedBlackTree.Node<String> lookup(String k){ 
			// case 1: empty tree
			Node<String> searchNode = new Node<String>(k);
			if(root == null) {
				return null;
			}
			else {
				Node<String> current = root;
				while(!current.key.equalsIgnoreCase(k)) { // loop until string is found
					if(searchNode.compareTo(current) < 0) { // case if searchNode is less than current
						if(current.leftChild == null) {
							return null; // searchNode does not exist
						}
						current = current.leftChild; // sets current to its left node
						
					}
					else if(searchNode.compareTo(current) > 0) { // case if searchNode is greater than current
						if(current.rightChild == null) {
							return null; // searchNode does not exist
						}
						current = current.rightChild; // sets current to its right node
						
					}
				}
				return current;
			}
		}
	 	
		/*
		 * gets the sibling of the node
		 * @param n the Node to get the sibling of
		 * @return the Node that is the sibling of n Node. Null if a sibling does not exist
		 */
		public RedBlackTree.Node<String> getSibling(RedBlackTree.Node<String> n){
			Node<String> parent = n.parent;
			if(n == parent.leftChild) { // node is the left child
				if(parent.rightChild == null) {
					return null; // sibling does not exist
				}
				return parent.rightChild;
			}
			else { // node is the right child
				if(parent.leftChild == null) {
					return null; // sibling does not exist
				}
				return parent.leftChild;
			}
		}
		
		/*
		 * Gets the aunt of the node
		 * @param n the Node to get the aunt of
		 * @return the Node that is the aunt of n Node.  Null if an aunt does not exist
		 */
		public RedBlackTree.Node<String> getAunt(RedBlackTree.Node<String> n){
			
			return this.getSibling(n.parent);
			
		}
		
		public RedBlackTree.Node<String> getGrandparent(RedBlackTree.Node<String> n){
			return n.parent.parent;
		}
		
		/*
		 * rotates left about the input Node
		 * @param x the Node input to rotate left about
		 */
		public void rotateLeft(RedBlackTree.Node<String> x){
			// followed pseudocode from the lecture slides
			Node<String> y = x.rightChild; // set y
			x.rightChild = y.leftChild; // turn y's left subtree into x's right subtree
			if(y.leftChild != null) {
				y.leftChild.parent = x;
			}
			y.parent = x.parent; // link x's parent to y
			if(x.parent == null) {
				root = y;
			}
			else if(x == x.parent.leftChild) {
				x.parent.leftChild = y;
			}
			else {
				x.parent.rightChild = y;
			}
			y.leftChild = x; // put x on y's left
			x.parent = y;
		}
		
		/*
		 * rotates right about the input Node
		 * @param x the Node input to rotate right about
		 */
		public void rotateRight(RedBlackTree.Node<String> y){
			
			// reverse of left rotate
			Node<String> x = y.leftChild; // set x
			y.leftChild = x.rightChild; // turn x's right subtree into y's left subtree
			if(x.rightChild != null) {
				x.rightChild.parent = y;
			}
			x.parent = y.parent; // link y's parent to x
			if(y.parent == null) {
				root = x;
			}
			else if(y == y.parent.rightChild) {
				y.parent.rightChild = x;
			}
			else {
				y.parent.leftChild = x;
			}
			x.rightChild = y; // put y on x's right
			y.parent = x;
			
		}
		
		/*
		 * Recursively traverse the tree to make it a Red Black tree
		 * @param current the Node that was inputted that could have caused a violation of the RBT
		 */
		public void fixTree(RedBlackTree.Node<String> current) {
			
			// red = 0, black = 1
			// false 1, true 0
			if(current == root) { // current is the root node
				current.isRed = false;
				current.color = 1;
				return;
			}
			if(!current.parent.isRed) { // parent is black, quit: the tree is valid
				return;
			}
			// the current node is red and the parent node is red
			if(current.isRed && current.parent.isRed) {
				// if aunt node is empty or black: 4 sub cases
				if(this.getAunt(current) == null || !this.getAunt(current).isRed){
					// grandparent - parent is left child and current is right child
					if(current.parent == this.getGrandparent(current).leftChild && current == current.parent.rightChild) {
						this.rotateLeft(current.parent);
						this.fixTree(current.parent);
					}
					// grandparent - parent is right child and current is left child
					else if(current.parent == this.getGrandparent(current).rightChild && current == current.parent.leftChild) {
						this.rotateRight(current.parent);
						this.fixTree(current.parent);
					}
					// grandparent - parent is left child and current is left child
					else if(current.parent == this.getGrandparent(current).leftChild && current == current.parent.leftChild) {
						current.parent.isRed = false;
						current.parent.color = 1;
						this.getGrandparent(current).isRed = true;
						this.getGrandparent(current).color = 0;
						this.rotateRight(this.getGrandparent(current));
						return;
					}
					// grandparent - parent is right child and current is right child
					else if(current.parent == this.getGrandparent(current).rightChild && current == current.parent.rightChild) {
						current.parent.isRed = false;
						current.parent.color = 1;
						this.getGrandparent(current).isRed = true;
						this.getGrandparent(current).color = 0;
						this.rotateLeft(this.getGrandparent(current));
						return;
					}
				}
				// aunt is red
				else if(this.getAunt(current).isRed) {
					current.parent.isRed = false;
					current.parent.color = 1;
					this.getAunt(current).isRed = false;
					this.getAunt(current).color = 1;
					this.getGrandparent(current).isRed = true;
					this.getGrandparent(current).color = 0;
					this.fixTree(this.getGrandparent(current));
				}
			}
		}
		
		public boolean isEmpty(RedBlackTree.Node<String> n){
			if (n.key == null){
				return true;
			}
			return false;
		}
		 
		public boolean isLeftChild(RedBlackTree.Node<String> parent, RedBlackTree.Node<String> child)
		{
			if (child.compareTo(parent) < 0 ) {//child is less than parent
				return true;
			}
			return false;
		}

		public void preOrderVisit(Visitor<String> v) {
		   	preOrderVisit(root, v);
		}
		 
		 
		private static void preOrderVisit(RedBlackTree.Node<String> n, Visitor<String> v) {
		  	if (n == null) {
		  		return;
		  	}
		  	v.visit(n);
		  	preOrderVisit(n.leftChild, v);
		  	preOrderVisit(n.rightChild, v);
		}	
	}

