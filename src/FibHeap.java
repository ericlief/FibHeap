import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Normal Fibonacci Heap class
 * 
 * @author liefe
 *
 */
public class FibHeap {

    private Node min;		// ptr to min node (in root)
    private int n;		// total # nodes in heap
    private int steps;		// count of steps for extractMin

    /**
     * Helper class to build heap-ordered tree
     * 
     * @author liefe
     *
     */
    private class Node {
	int key;		// key of node (int or generic here)
	int degree = 0;		// degree of node
	boolean mark = false;	// indicates whether a child has been deleted
	Node parent; 		// ptr to parent of node (necessary?)
	Node child;		// ptr to any child
	Node prev, next;	// siblings
	int id;
    }

    /**
     * Public method to insert a node along with its key and id (or data field)
     * 
     * @param key
     */
    public void insert(Node tree, int key, int id) {
	//	System.out.println("inserting tree with " + key);
	tree.key = key;			// set key of node
	tree.id = id;			// set id of node
	insert(tree);
	n++;	// increment size of nodes in heap    }
    }

    /**
     * Helper method for inserting a node. Create new singleton tree and add to
     * root list (i.e. heap at head) This lazy version of FibHeap will not do
     * the merge operation after each insert, only after a delMin has been
     * called.
     * 
     * @param tree
     */
    private void insert(Node tree) {

	// System.out.println("inserting tree w key" + tree.key);

	// Insert tree to at end of a DL list headed by min ptr
	if (min == null) {
	    tree.prev = tree;
	    tree.next = tree;
	    min = tree;
	} else {

	    min.prev.next = tree;
	    tree.next = min;
	    tree.prev = min.prev;
	    min.prev = tree;
	    if (tree.key < min.key)
		min = tree;
	}

	// System.out.println("roots -> min" + min.key);

	//	n++;	// increment size of nodes in heap
    }

    /**
     * Remove a node from a doubly linked (child) list and add to root list.
     * Return the modified list/head pointer.
     * 
     * @param tree
     * @param head
     * @return head of list (Node)
     */
    private Node remove(Node head, Node tree) {

	// Remove tree from root list of the heap

	// System.out.println("removing " + tree.key + " from list at " + head.key);

	tree.prev.next = tree.next;
	tree.next.prev = tree.prev;

	// Deleted last tree in heap, min is now null 
	if (tree == tree.next)
	    return null;
	// Redirect min ptr to next element; will need to find a new min now
	else if (head == tree)
	    return head.next;	// changed from min.next
	// Return new modified node list
	else
	    return head;

    }

    /**
     * Extract min from heap and add children to root list and then consolidate
     * Heap so that no more than one of each degree remain.
     * 
     * @return min key
     */
    public Integer extractMin() {
	// System.out.println("Extracting min " + min.key + "-> next " + min.next.key);

	// If no min, then we're done
	if (n == 0)
	    throw new NoSuchElementException("Heap is empty!");

	Node minNode = min;
	int minVal = min.key;

	// Remove min from root list
	min = remove(min, min);
	n--;

	//	if (min != null)
	//	    System.out.println("cur min=" + min.key);

	// Insert children under min node into root list
	int nchildren = 0;
	Node child = minNode.child;
	if (child != null) {
	    nchildren = minNode.degree; // number of children cut
	    Node next = child;
	    int i = 0;
	    do {
		// System.out.println("i=" + i + " out of nchildren = " + nchildren);
		child = next;
		next = child.next;
		//		System.out.println("inserting child to root " + child.key);
		//		System.out.println(child.key + "-> " + next.key);

		child.parent = null;	// reset pointer to parent

		insert(child);
		// System.out.println(isRoot(min, child));
		assert isRoot(min, child) : "child not in root";
		//		child = child.next;
		steps++;	// increment steps for children
		i++;		// increment counter
	    } while (i < nchildren);

	}

	// printRoots(min);

	if (min == null) {
	    System.out.println("tree now empty, min=null");
	} else {
	    // Consolidate root list until no more than one of each degrees remain
	    //	    min = min.next;
	    consolidate();
	    //System.out.println("done consolidating");
	    // printRoots(min);
	}

	return minVal;
    }

    /**
     * Consolidate heap and make sure that not more than one degree is left
     */
    public void consolidate() {

	int maxDegree = (int) Math.floor(Math.log10(n) / Math.log10((1 + Math.sqrt(5)) / 2));
	// System.out.println("maxdeg=" + maxDegree);
	Node[] tableDegrees = new Node[maxDegree + 1];	// array of tree degree used for iteration below
	Node cur;		// ptr to current pos
	Node next = min;	// ptr to next pos

	int nRoots = 0;

	cur = min;
	do {
	    cur = cur.next;
	    nRoots++;

	} while (cur != min);

	// System.out.println("Number of roots = " + nRoots);
	if (nRoots == 1)
	    return;

	int i = 0;
	do {
	    i++;
	    cur = next;
	    next = cur.next;

	    //	    System.out.println("cur " + cur.key);
	    //	    System.out.println("next" + next.key);

	    // Iterate through the array of degree until reaching the end of heap
	    int d = cur.degree;
	    // System.out.println(d);
	    while (tableDegrees[d] != null) {
		//		System.out.println("other " + tableDegrees[d].key);
		//		System.out.println("degree=" + d);

		// Join/link nodes of same rank, with the min element as new root
		// If cur key is larger swap trees 
		if (cur.key <= tableDegrees[d].key) {
		    cur = link(cur, tableDegrees[d]);
		    // System.out.println("linked other below cur" + tableDegrees[d].key + ", cur=" + cur.key);
		} else {
		    // System.out.println("swapped other w current, other=" + tableDegrees[d].key + ", cur=" + cur.key);
		    cur = link(tableDegrees[d], cur);	// link trees with other as child of cur
		}

		tableDegrees[d] = null;
		d++;
	    }

	    // Save current node if not in list of degrees
	    tableDegrees[d] = cur;

	} while (i < nRoots);

	min = null;
	int ntrees = tableDegrees.length;
	for (i = 0; i < ntrees; i++) {
	    cur = tableDegrees[i];
	    if (cur == null)
		continue;
	    if (min == null || cur.key < min.key) {
		min = cur;
	    }
	}
	//	printRoots(min);
    }

    /**
     * Helper method to print roots of a list with sentinel
     * 
     * @param head
     */
    public void printRoots(Node head) {

	System.out.println("Printing roots");
	if (head == null) {
	    System.out.println("empty list, aborting");
	    return;
	}

	Node cur = head;
	do {
	    System.out.print(cur.key + " ");
	    cur = cur.next;

	} while (cur != head);

	System.out.println();
    }

    /**
     * Helper method to determine whether a child of a node
     * 
     * @param parent
     * @param child
     * @return true/false
     */
    public boolean childOf(Node parent, Node child) {
	return child.parent == parent;
    }

    /**
     * Helper method to combine two lists
     * 
     * @param head1
     * @param head2
     * @return
     */
    public Node meld(Node head1, Node head2) {

	if (head1 == null) {
	    return head2;
	} else if (head2 == null) {
	    return head1;
	}

	// Meld two linked lists together 

	head1.next.prev = head2.prev;
	head2.prev.next = head1.next;
	head1.next = head2;
	head2.prev = head1;
	return head1;
    }

    /**
     * Remove a node from a doubly linked (child) list and add to root list.
     * Return the modified list/head pointer.
     * 
     * @param child
     * @param head
     * @return head of list (Node)
     */
    private void cut(Node child, Node parent) {

	// Remove child from root list of its parent 

	//	System.out.println("cutting " + child.key + " from list at " + parent.child.key);
	//	System.out.println("parent=" + parent.key);
	//	System.out.println("child next=" + child.next.key);

	parent.child = remove(parent.child, child);
	assert !isRoot(parent.child, child) : "child not removed";	// check if gone
	parent.degree--;	// decrement degree
	//	System.out.println("children: ");
	//	printRoots(parent.child);
	//	System.out.println("parent degree" + parent.degree);

	// Insert child into root list
	//	System.out.println("inserting child in root list @" + min.key);
	insert(child);
	assert isRoot(min, child) : "child not in root list";
	//	System.out.println("is in root? " + isRoot(min, child));
	//	printRoots(min);

	child.parent = null;	// remove parent
	child.mark = false;	// remove any mark
    }

    /**
     * The recursive method from Cormen. Here implemented iteratively upto the
     * root list.
     * 
     * @param child
     */
    private void cascadingCut(Node child) {
	Node parent = child.parent;

	while (parent != null) {

	    //	    System.out.println("parent=" + parent.key + " of " + child.key);

	    // Mark and do nothing else
	    if (child.mark == false) {
		child.mark = true;
		break;
	    }

	    // Else cut and cascade if possible
	    //	    System.out.println("cascading");

	    cut(child, parent);		// cut and add to root list
	    child.parent = null;	// remove parent pointer
	    child.mark = false;		// remove any mark

	    // Repeat until reaching root list level
	    child = parent;
	    parent = child.parent;
	}
    }

    /**
     * Helper method to find a root in root list.
     * 
     * @param head
     * @param root
     * @return true or not
     */
    public boolean isRoot(Node head, Node root) {
	System.out.println("isRoot @ head " + head.key);
	Node cur = head;
	do {
	    //System.out.println("cur" + cur.key + "->head" + head.key);
	    if (cur == root)
		return true;
	    cur = cur.next;

	} while (cur != head);

	return false;

    }

    /**
     * Link two trees (nodes), the lesser key above the greater.
     * 
     * @param tree1
     * @param tree2
     * @return new tree/node
     */
    private Node link(Node tree1, Node tree2) {
	// Augment counter
	steps++;

	// Remove tree2 from root list of the heap
	min = remove(min, tree2); 	// new heap root list, need to reset min
	//	System.out.println("Removed root " + tree2.key + ", " + !isRoot(min, tree2));

	// Update parent
	tree2.parent = tree1;

	// Make child, updating pointers 
	if (tree1.child == null) {
	    tree1.child = tree2;
	    tree2.next = tree2;
	    tree2.prev = tree2;
	} else {
	    tree2.prev = tree1.child;
	    tree2.next = tree1.child.next;
	    tree1.child.next = tree2;
	    tree2.next.prev = tree2;
	}
	//	System.out.println("tree1 children");
	//	printRoots(tree1.child);
	//	System.out.println("tree2 children");
	//	printRoots(tree2.child);

	// Update counts
	tree1.degree++;
	tree2.mark = false;	// update mark 

	return tree1;
    }

    /**
     * Decrease a nodes key and maintain heap property.
     * 
     * @param child
     * @param key
     */
    public void decreaseKey(Node child, int key) {
	if (key > child.key) {
	    System.out.println("Key must be less than current value");
	    return;
	}

	child.key = key;		// decrease current key

	Node parent = child.parent;	// current parent
	//System.out.println("old min = " + min.key);

	// If parent then remove child from list of children of parent
	if (parent != null && key < parent.key) {
	    //	    System.out.println("roots before cutting child " + child.key + " from parent " + parent.key);
	    //	    printRoots(parent.child);

	    // Cut child from child list and insert in root list

	    cut(child, parent);

	    //	    System.out.println("lost child node");
	    //	    printRoots(parent.child);
	    //	    System.out.println("child root list");
	    //	    printRoots(child.child);
	    //	    System.out.println("new root list");
	    //	    printRoots(min);

	    // Cascade from parent up to root if necessary to maintain FibHeap property
	    cascadingCut(parent);
	    //	    System.out.println("new root list");
	    //	    printRoots(min);
	}

	// Update min if necessary
	if (child.key < min.key) {

	    //	    System.out.println("old min" + min.key);
	    //	    printRoots(min);

	    min = child;
	    //	    System.out.println("new min from dec child" + child.key);
	    //	    System.out.println("old parent key " + parent);
	    //	    if (parent != null)
	    //		System.out.println(parent.key);
	}
    }

    public boolean isEmpty() {
	return n == 0;
    }

    public static void main(String[] args) throws IOException {

	//File in = new File("~/fh/test.txt");
	//Path in = Paths.get(System.getProperty("user.home")).resolve("fh/test.txt");
	Scanner sc = new Scanner(System.in);
	//Scanner sc = new Scanner(in);

	FibHeap heap = null;
	// final long startTime = System.currentTimeMillis();
	int n = 0;
	String fout = "proper-normal.csv";
	//String fout = "test.csv";

	//File fname = new File("proper-random.csv");
	Path pathOut = Paths.get(System.getProperty("user.home")).resolve("code/ds/FibHeap/output/" + fout);
	try (BufferedWriter out = Files.newBufferedWriter(pathOut, StandardOpenOption.WRITE,
		StandardOpenOption.CREATE)) {
	    //	try (BufferedWriter out = new BufferedWriter(new FileWriter(fout))) {
	    System.out.println();
	    //	    String[] tableID;
	    FibHeap.Node[] tableID;
	    int extractMinCount;
	    while (sc.hasNext()) {
		if (sc.hasNext("#")) {
		    sc.next();
		    heap = new FibHeap();
		    extractMinCount = 0;
		    if (sc.hasNextInt())
			n = sc.nextInt();	// number of elements to be inserted

		    System.out.println("Building an ID table of size " + n);
		    tableID = new FibHeap.Node[n];
		    sc.nextLine();

		    while (sc.hasNext("INS") || sc.hasNext("DEL") || sc.hasNext("DEC")) {
			if (sc.hasNext("INS")) {
			    sc.next();
			    if (sc.hasNextInt()) {
				int id = sc.nextInt();
				if (sc.hasNextInt()) {
				    int key = sc.nextInt();
				    FibHeap.Node x = heap.new Node();
				    tableID[id] = x;	// store id in table
				    heap.insert(x, key, id);
				    // System.out.println("inserted " + key + "id=" + id);
				}
			    }
			} else if (sc.hasNext("DEL")) {		// extract min
			    sc.next();
			    Node min = heap.min; // current min
			    //			    System.out.println("deleting old min " + min.key + " id=" + min.id);
			    tableID[min.id] = null;
			    heap.extractMin();
			    //			    System.out.println("new min" + heap.min.key);
			    //			    System.out.println("deleted min " + tableID[min.id]);
			    extractMinCount++;
			} else if (sc.hasNext("DEC")) {		// dec
			    sc.next();
			    if (sc.hasNextInt()) {
				int id = sc.nextInt();
				if (sc.hasNextInt()) {
				    int key = sc.nextInt();
				    FibHeap.Node x = tableID[id];

				    if (x != null) {
					// System.out.println("decrementing key " + x.key + "/id=" + id + "-->" + key);

					heap.decreaseKey(x, key);
					// System.out.println("new key " + tableID[id].key);
				    }
				}
			    }
			}
		    }
		    int totalSteps = heap.steps;
		    double aveSteps = (double) totalSteps / extractMinCount;
		    System.out.println("total steps " + totalSteps);
		    System.out.println("Average steps for extractMin for " + n + ": " + aveSteps);
		    out.write(n + "," + aveSteps + "\n");
		    //final long time = System.currentTimeMillis() - startTime;
		    //System.out.println("Time: " + time);
		}
	    }
	    //final long time = System.currentTimeMillis() - startTime;
	    //System.out.println("Total time: " + time);
	    out.close();
	} catch (IOException e) {
	    System.err.println("Error writing output");
	    e.printStackTrace();
	    sc.close();
	}
    }
}
