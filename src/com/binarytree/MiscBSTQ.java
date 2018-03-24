package com.binarytree;

import java.util.Stack;

public class MiscBSTQ {
	/**
	 * Description - Find the node with minimum value in a Binary Search Tree
	 * @param root - Input root of BST
	 * @return - Min key node
	 */
	public static Node getMinFromBST(Node root){
		if(root.getLeft()==null)
			return root;
		
		return getMinFromBST(root.getLeft());
	}
	/**
	 * Description - A binomial coefficient based function to find nth ctalon number. O(n)
	 * 
	 * @param n
	 * @return
	 */
	public static int catalon(int n){
		return binomialCoeff(2*n, n)/(n+1);
	}
	private static int binomialCoeff(int n, int r){
		if(r>n-r)
			r=n-r;
		int res=1;
		for (int i = 0; i < r; i++) {
			res*=(n-i);
			res/=(i+1);
		}
		//System.out.println("Binomial Coeff = "+(res));
		return res;
	}
	/**
	 * Description - Printing in-order traversal from given level-order traversal without sorting. O(n).
	 * 
	 * @param levelOrder
	 */
	public static void inorderFromLevelOrder(int[] levelOrder){
		in4mLevelOrderUtil(levelOrder, 0, levelOrder.length-1);
	}
	private static void in4mLevelOrderUtil(int[] arr, int start, int end){
		if(start>end)
			return;
		
		in4mLevelOrderUtil(arr, 2*start+1, end);
		System.out.print(arr[start] + " ");
		in4mLevelOrderUtil(arr, 2*start+2, end);
	}
	/**
	 * Description - Inorder Successor in Binary Search Tree.
	 * @param root - Root node
	 * @param node - Node against which successor need to be find
	 * @return - successor
	 */
	public static Node inorderSuccessor(Node root, Node node){
		if(node.getRight()!=null){
			Node temp=node.getRight();
			while(temp.getLeft()!=null)
				temp=temp.getLeft();
			
			return temp;
		}
		
		Node succ=null;
		while(root!=null){
			if(root.getData()>node.getData()){
				succ=root;
				root=root.getLeft();
			}else if(root.getData()>node.getData()){
				root=root.getRight();
			}else 
				break;
		}
		
		return succ;
	}
	/**
	 * Description - Print BST keys in the given range
					 Given two values k1 and k2 (where k1 < k2) and a root pointer to a Binary Search Tree. Print all the keys of tree in range k1 to k2. i.e. print all x such that k1<=x<=k2 and x is a key of given BST. Print all the keys in increasing order.
	 * @param root - BST root node
	 * @param k1
	 * @param k2
	 */
	public static void printbetweenk1k2(Node root, int k1, int k2){
		if(root == null)
			return;
		if(root.getLeft()!=null && root.getLeft().getData()>=k1)
			printbetweenk1k2(root.getLeft(), k1, k2);
		
		System.out.print(root.getData()+" ");
			
		if(root.getRight()!=null && root.getRight().getData()<=k2)
			printbetweenk1k2(root.getRight(), k1, k2);
		
	}
	/**
	 * Description - A method to find out largest sorted sequence present in Binary Tree.
	 * @param root
	 * @return
	 */
	public static int largestSortedSeq(Node root){
		CustIndex ci= new CustIndex(0, 0);
		largestSortedSeqUtil(root,ci);
		return ci.max>ci.index?ci.max:ci.index;
	}
	private static void largestSortedSeqUtil(Node root, CustIndex ci) {
		if(root==null){
			return;
		}
		largestSortedSeqUtil(root.getLeft(), ci);
		
		if(ci.ptr==null){
			ci.ptr=root;
			ci.index++;
		}else if(ci.ptr.getData()>root.getData()){
			ci.max=ci.max>ci.index?ci.max:ci.index;
			ci.index=1;
			ci.ptr=root;
		}else{
			ci.index++;
			ci.ptr=root;
		}
		
		largestSortedSeqUtil(root.getRight(), ci);
	}
	
	/**
	 * Description - Find the largest BST subtree in a given Binary Tree. O(n) Solution
	 * 
	 * @param root
	 * @param ci
	 * @return
	 */
	public static CustIndex largestSubBST(Node root, CustIndex ci){
		if(root == null)
			return ci;
		
		CustIndex lst = largestSubBST(root.getLeft(), new CustIndex(root.getData(), root.getData())); // Check if left subtree is BST
		CustIndex rst = largestSubBST(root.getRight(), new CustIndex(root.getData(), root.getData())); // Check if right subtree is BST
		if(lst.bst && rst.bst && lst.max<=root.getData() && rst.min>=root.getData()){ // left and right subtree both are BST and max of left should less than roo and min of right should greater than root.
			ci.bst=true;
			ci.index=lst.index+rst.index+1;
			ci.max=rst.max;
			ci.min=lst.min;
		}else{
			ci.bst=false;
			ci.index=lst.index>rst.index?lst.index:rst.index;
		}
		
		return ci;
	}
	
	public static Node mergeTwoBalancedBST(Node root1, Node root2, int n){
		// Step 1 - Create DLL for first tree (root1) in-place
		// Step 2 - Create DLL for second tree (root2) in-place
		// Step 3 - Merge these two sorted DLL
		// Step 4 - Create Balanced BST from sorted DLL in step 3.
		CIndex curr1=new CIndex();
		createDLL4mBST(root1, curr1);
		
		CIndex curr2=new CIndex();
		createDLL4mBST(root2, curr2);
		
		Node combinedDLLsn=mergeDLLs(curr1.ptr, curr2.ptr);
		
		CIndex curr=new CIndex();
		curr.ptr=combinedDLLsn;
		Node finalRoot = buildBST4mDLL(curr, n);
		return finalRoot;
	}
	
	private static void createDLL4mBST(Node root, CIndex curr){
		if(root == null)
			return;
		createDLL4mBST(root.getRight(), curr);
		if(curr.ptr == null){
			curr.ptr=root;
			curr.end=root;
		}else{
			curr.ptr.setLeft(root);
			root.setRight(curr.ptr);
			curr.ptr=root;
		}
		createDLL4mBST(root.getLeft(), curr);
	}
	private static Node mergeDLLs(Node root1, Node root2){
		Node temp1=(root1.getData()>root2.getData()?root2:root1);	// Smaller Root
		Node temp2=(root1.getData()<root2.getData()?root2:root1);	// Greater Root
		Node ret=temp1;// Result start node reference
		while(temp1!=null && temp1.getRight()!=null && temp2!=null){
			Node temp1_next=temp1.getRight();
			Node temp2_next=temp2.getRight();
			
			if(temp2.getData()<temp1_next.getData()){
				temp1.setRight(temp2);
				temp2.setLeft(temp1);
				temp2.setRight(temp1_next);
				temp1_next.setLeft(temp2);
				
				temp1=temp1_next.getLeft(); // temp1 should be next after modification
				temp2=temp2_next;
			}else{
				temp1=temp1_next;
			}
		}
		
		if(temp2!=null){
			temp1.setLeft(temp2);
			temp2.setLeft(temp1);
		}
		return ret;
	}
	/**
	 * This is similar to how to get BST from sorted array.
	 * 
	 * @param curr
	 * @param n
	 * @return
	 */
	private static Node buildBST4mDLL(CIndex curr, int n){
		if(n<=0){
			return null;
		}
		
		Node left= buildBST4mDLL(curr, n/2);
		
		Node root=curr.ptr;
		curr.ptr=curr.ptr.getRight();
		
		root.setLeft(left);
		root.setRight(buildBST4mDLL(curr, n-n/2-1));
		
		return root;
	}
	
	public static void correctBST(Node root) {
		CIndex first=new CIndex(),middle=new CIndex(), last=new CIndex(), prev=new CIndex();
		correctBSTUtil(root, first, middle, last, prev);
		
		if(last.ptr!=null) {
			swapNodesKey(first.ptr, last.ptr);
		}else {
			swapNodesKey(first.ptr, middle.ptr);
		}
	}
	private static void swapNodesKey(Node ptr, Node ptr2) {
		int temp=ptr2.getData();
		ptr2.setData(ptr.getData());
		ptr.setData(temp);
	}
	private static void correctBSTUtil(Node root, CIndex first, CIndex middle, CIndex last, CIndex prev) {
		if(root==null || (middle.ptr!=null && last.ptr!=null))
			return;
		correctBSTUtil(root.getLeft(), first, middle, last, prev);
		
		if(prev.ptr==null)
			prev.ptr=root;
		else if(prev.ptr.getData()>root.getData()){
			if(first.ptr==null) {
				first.ptr=prev.ptr;
				middle.ptr=root;
			}else
				last.ptr=root;
		}
		
		prev.ptr=root;
		
		correctBSTUtil(root.getRight(), first, middle, last, prev);
	}
	
	public static int ceil(Node root, int keyValue) {
		if(root==null)
			return -1;
		
		if(root.getData()==keyValue)
			return root.getData();
		
		if(root.getData()<keyValue)
			return ceil(root.getRight(), keyValue);
		
		int ceilValue=ceil(root.getLeft(), keyValue);
		
		return ceilValue>=keyValue?ceilValue:root.getData();		
	}
	
	public static int floor(Node root, int keyValue) {
		if(root==null)
			return -1;
		
		if(root.getData()==keyValue)
			return root.getData();
		
		if(root.getData()>keyValue)
			return floor(root.getLeft(), keyValue);
		
		int floorValue=floor(root.getRight(), keyValue);
		
		return floorValue==-1?root.getData():floorValue;		
	}
	/**
	 * Description - This method will process each node once from both the tree so time complexity - O(m+n). There are nested while loops but still it will process one node atmost once
	 *               Also, in stack it will be pushed once and popped once.
	 * 
	 * @param root1
	 * @param root2
	 */
	public static void mergeAndPrint(Node root1, Node root2){
		
		Stack<Node> stackOne= new Stack<Node>();
		Stack<Node> stackTwo= new Stack<Node>();
		
		while(root1!=null){
			stackOne.push(root1);
			root1=root1.getLeft();
		}
		
		while(root2!=null){
			stackTwo.push(root2);
			root2=root2.getLeft();
		}
		
		while(!stackOne.isEmpty() && !stackTwo.isEmpty()){
			Node top = null;
			Node recNode = null;
			
			if(stackOne.peek().getData() < stackTwo.peek().getData()){
				top=stackOne.pop();
				
				if(top.getRight()!=null){
					stackOne.push(top.getRight());
					
					recNode = stackOne.peek().getLeft();
					while(recNode!=null){
						stackOne.push(recNode);
						recNode=recNode.getLeft();
					}
				}
			}else{
				top=stackTwo.pop();
				
				if(top.getRight()!=null){
					stackTwo.push(top.getRight());
					
					recNode = stackTwo.peek().getLeft();
					while(recNode!=null){
						stackTwo.push(recNode);
						recNode=recNode.getLeft();
					}	
				}
			}
			System.out.print(top.getData()+ " ");
		}
		
		while(!stackOne.isEmpty()){
			System.out.print(stackOne.peek().getData()+" ");
			inorder(stackOne.pop().getRight());
		}
		
		while(!stackTwo.isEmpty()){
			System.out.print(stackTwo.peek().getData()+" ");
			inorder(stackTwo.pop().getRight());
		}		
	}
	private static void inorder(Node right) {
		if(right == null)
			return;
		
		inorder(right.getLeft());
		System.out.println(right.getData()+" ");
		inorder(right.getRight());	
	}
	/**
	 * Description - Find if there is a triplet in a Balanced BST that adds to zero. Expected time complexity is O(n^2) and only O(Logn) extra space can be used. You can modify given Binary Search Tree. Note that height of a Balanced BST is always O(Logn).
	 * 
	 * @param root
	 */
	public static void printTripletOfZero(Node root){
		// Step 1 - convert in-order of BST to DLL in O(logn) space complexity
		
		CIndex curr=new CIndex();
		createDLL4mBST(root, curr);
		
		// Step 2 - find triplet in O(n^2) time complexity
		
		Node currSt=curr.ptr, currEnd=curr.end.getLeft().getLeft(), tempEnd=curr.end;
		
		while(currSt!=null && currSt!=currEnd){
			int currData=currSt.getData();
			
			Node currNext=currSt.getRight();
			while(currNext.getData()<tempEnd.getData()){
				if(currNext.getData()+tempEnd.getData()+currData == 0){
					System.out.println(currData+" : "+currNext.getData()+" : "+tempEnd.getData());
					return;
				}else if(currNext.getData()+tempEnd.getData()<currData*-1){
					currNext=currNext.getRight();
				}else{
					tempEnd=tempEnd.getLeft();
				}
			}
			
			currSt=currSt.getRight();
		}
		System.err.println("No tripplet that sums to zero !!");
	}
	/**
	 * Description - Find a pair with given sum in a Balanced BST. Expected time complexity is O(n) and only O(Logn) extra space can be used. Any modification to Binary Search Tree is not allowed.
	 * @param root
	 * @param k
	 */
	public static void findPair4SumK(Node root, int k){
		Stack<Node> stkOne = new Stack<Node>();
		Stack<Node> stkTwo = new Stack<Node>();
		
		boolean first=false, second=false;
		Node inOrder=root, revInOrder=root;
		int leftVal=0, rightVal=0;
		while(true){
			
			while(!first){
				while(inOrder!=null){
					stkOne.push(inOrder);
					inOrder=inOrder.getLeft();
				}
				if(stkOne.isEmpty())
					first=true;
				else{
					Node leftTemp=stkOne.pop();
					leftVal=leftTemp.getData();
					inOrder=leftTemp.getRight();
					first=true;
				}
			}
			
			while(!second){
				while(revInOrder!=null){
					stkTwo.push(revInOrder);
					revInOrder=revInOrder.getRight();
				}
				
				if(stkTwo.isEmpty())
					second=true;
				else{
					Node rightTemp=stkTwo.pop();
					rightVal=rightTemp.getData();
					revInOrder=rightTemp.getLeft();
					second=true;
				}
			}
			
			if(leftVal+rightVal == k){
				System.out.println(leftVal+" : "+rightVal);
				return;
			}else if(leftVal+rightVal > k){
				second=false;
			}else{
				first=false;
			}
			
			if(leftVal >= rightVal){
				System.err.println("No pair found");
				return;
			}

		}
		
		// Step 1 - Do an iterative in-order traversal using stack.
		// Step 2 - Do another iterative reverse-in-order traversal using stack.
		// Step 3 - Check if sum of step-1 node key + step-2 node key equals to k
		//          if yes = then print and return;
		//			else if(sum>k) find next node in reverse-in-order and check for sum
		//          else find next node in in-order and check for sum
	}
	/**
	 * Description - Remove BST keys outside the given range.
	 * 
	 * @param root - BST root.
	 * @param min - min value of the range.
	 * @param max - max value of the range.
	 * @return
	 */
	public static Node inRange(Node root, int min, int max){
		if(root==null)
			return null;
		
		root.setLeft(root.getData()==min?null:inRange(root.getLeft(), min, max));
		root.setRight(root.getData()==max?null:inRange(root.getRight(), min, max));
		
		if(root.getData()<min){
			return root.getRight();
		}else if(root.getData()>max){
			return root.getLeft();
		}/*else{
			root.setLeft(root.getData()==min?null:inRange(root.getLeft(), min, max));
			root.setRight(root.getData()==max?null:inRange(root.getRight(), min, max));
		}*/
		return root;
	}
	public static void main(String[] args) {
		/*Node root=new Node(20);
		root.setLeft(new Node(8));
		root.setRight(new Node(22));
		root.getLeft().setLeft(new Node(4));
		root.getLeft().setRight(new Node(12));
		
		System.out.println(getMinFromBST(root).getData());
		
		System.out.println("Total no of BST if n=3 : "+catalon(3));
		
		int[] arr={4,2,5,1,3};
		inorderFromLevelOrder(arr);
		
		System.out.println(inorderSuccessor(root, root.getLeft().getRight()).getData());
		printbetweenk1k2(root, 5, 21);*/
		
		/*Node root = new Node(50);
		root.left = new Node(10);
		root.right = new Node(60);
		root.left.left = new Node(5);
		root.left.right = new Node(20);
		root.right.left = new Node(55);
		root.right.left.left = new Node(45);
		root.right.right = new Node(70);
		root.right.right.left = new Node(65);
		root.right.right.right = new Node(80);
		
		System.out.println(largestSubBST(root, new CustIndex(root.getData(), root.getData())).index);*/
		
		/*Node root = new Node(100);
		root.left = new Node(50);
		root.right = new Node(300);
		root.left.left = new Node(20);
		root.left.right = new Node(70);

		Node root1 = new Node(80);  
		root1.left = new Node(40);
		root1.right = new Node(120);

		//System.out.println(mergeTwoBalancedBST(root, root1, 8));
		
		Node iRoot = new Node(100);
		iRoot.left = new Node(20); //new Node(50);
		iRoot.right = new Node(300);
		iRoot.left.left = new Node(50); //new Node(20);
		iRoot.left.right = new Node(70);

		correctBST(iRoot);
		
		System.out.println(iRoot.getData());*/
		
		/*Node root = new Node(8);
		root.left = new Node(4);
		root.right = new Node(12);
		root.left.left = new Node(2);
		root.left.right = new Node(6);
		root.right.left = new Node(10);
		root.right.right = new Node(14);
		
		for(int i=0; i<=15; i++) {
			System.out.println("Ceiling of "+i+" :: "+ceil(root, i));
		}
		for(int i=0; i<=15; i++) {
			System.err.println("Floor of "+i+" :: "+floor(root, i));
		}*/
		
		/*Node root = new Node(100);
		root.left = new Node(50);
		root.right = new Node(300);
		root.left.left = new Node(20);
		root.left.right = new Node(70);

		Node root1 = new Node(80);  
		root1.left = new Node(40);
		root1.right = new Node(120);

		//System.out.println(mergeTwoBalancedBST(root, root1, 8));
		mergeAndPrint(root, root1);*/
		
		Node root = new Node(6);
		root.left = new Node(-13);
		root.right = new Node(14);
		root.left.right = new Node(-12);
		
		root.right.left=new Node(13);
		root.right.right=new Node(15);
		root.right.left.left=new Node(7);
		
		//printTripletOfZero(root);
		//findPair4SumK(root, -21);
		Node rangeRoot=inRange(root, -10, 8);
		System.out.println(rangeRoot.getData());
	}

}

class CustIndex{
	Node ptr=null;
	int index=0,max=0,min=0;
	boolean bst=true;
	public CustIndex(int max, int min) {
		super();
		this.max = max;
		this.min = min;
	}

}