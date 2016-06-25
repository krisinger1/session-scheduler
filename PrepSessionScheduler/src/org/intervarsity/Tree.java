package org.intervarsity;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
/**
 * A Tree of possible session options
 * @author Kristin
 *
 */
public class Tree {
	LinkedList<Tree> leaves;
	Tree parent=null;
	Session session;
	boolean isDead=false;
	boolean isEnd=false;
	
	public Tree(Tree parent, Session session){
		this.parent=parent;
		this.session=session;
		leaves=new LinkedList<Tree>();
	}
	
	/**
	 * Determines if this node has any leaves
	 * @return true if there is at least one leaf, false otherwise
	 */
	public boolean hasLeaves(){
		if (leaves.size()>0) return true;
		else return false;
	}
	
	/**
	 * Adds a leaf to this node
	 * @param leaf the leaf to add to the tree at this node
	 */
	public void addLeaf(Tree leaf){
		leaves.add(leaf);
	}
	
	/**
	 * removes leaf from this node and recursively removes all of leaf's leaves
	 * @param leaf the leaf to remove from tree
	 */
	public void removeLeaf(Tree leaf){
		if (this==leaf.parent){		// make sure this leaf exists for this parent
			while (leaf.hasLeaves() ){
				leaf.removeLeaf(leaf.leaves.get(0));
			}
			leaves.remove(leaf);

			//System.out.println("********");
			//printTree(this, 0);
		}
	}
	
	/**
	 * Determines if all leaves of this node are dead - 
	 * meaning they do not provide a viable solution
	 * @return true if all leaves are dead, otherwise false
	 */
	private boolean allLeavesDead(){
		for (Tree leaf:leaves){
			if (!leaf.isDead) return false;
		}
		return true;
	}
	
	/**
	 * Removes branches from tree that do not lead to usable solutions
	 * 
	 */
	public void pruneTree(){
		if (isEnd) return;
		int i=0;
		if (hasLeaves() && allLeavesDead()) {
			//if (!(session==null))session.print();
			//else System.out.println("null");
			//System.out.println("All leaves dead");
			//while(leaves.size()>0) removeLeaf(leaves.get(0));
			leaves.clear();
			isDead=true;
			return;
			}
		while (hasLeaves() && i<leaves.size()){
				Tree leaf=leaves.get(i);
				if (!leaf.isDead) {
					leaf.pruneTree();
					
					}
				if (leaf.isDead) removeLeaf(leaf);
				else i++;
		}
		if (!hasLeaves()) isDead=true;
		
	}
	
	/**
	 * Prints to console all descendants of this node with session info
	 */
	/*public void printLeaves(){
		for (Tree t:leaves){
			t.session.print();
			System.out.println("");
			if (t.leaves.size()>0)
			{	
				System.out.print("leaves of ");
				t.session.print();
				t.printLeaves();
			}
		}
	}*/
	/**
	 * Prints to console the entire tree of solutions with session info
	 */
	/*public void print(){
		System.out.println("The Tree");
		if (session!=null) {System.out.print("node");session.print();}
		printLeaves();
	}*/
	
	/**
	 * Prints to console entire tree in a readable way
	 * @param t the Tree object to print
	 * @param indent how far to indent next level of tree
	 */
	public static void printTree (Tree t, int indent) {
	    StringBuilder ind = new StringBuilder();
	    for (int i = 0; i < indent; i++)
	        ind.append("       ");
	    if (t==null)return;
	    // print the Node;
	    //
	    System.out.print( ind);
	    if (!(t.session==null)) {
	    	t.session.print();
	    	if (t.isEnd) System.out.println(" END");
	    	if (t.isDead)System.out.println(" DEAD");
	    }

	    // traverse Descendants.
	    //
	    for (Tree leaf : t.leaves) {
	        printTree( leaf, indent+1);
	    }
	}
	
	/**
	 * Prints to file entire tree in a readable way
	 * used by printSolutionToFile
	 * @param t the Tree object to print
	 * @param indent how far to indent next level of tree
	 * @param writer PrintWriter object
	 */
	private static void printTreeToFile (Tree t, int indent, PrintWriter writer) {
	    StringBuilder ind = new StringBuilder();
	    for (int i = 0; i < indent; i++)
	        ind.append("       ");
	    if (t==null)return;
	    // print the Node;
	    //
	    writer.print( ind);
	    if (!(t.session==null)) {
	    	writer.println(t.session.toString());
	    	if (t.isEnd) writer.println(" END");
	    	if (t.isDead)writer.println(" DEAD");
	    }

	    // traverse Descendants.
	    //
	    for (Tree leaf : t.leaves) {
	        printTreeToFile( leaf, indent+1,writer);
	    }
	}
	
	/**
	 * Prints solutions tree to file specified in filename
	 * @param t solution tree to print
	 * @param indent how far to indent next level of tree
	 * @param filename file to print to
	 */
	public static void printSolutionToFile(Tree t,int indent,String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			printTreeToFile(t, indent, writer);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO is this code even correct for anything? Looks like infinite recursion
	/*private static String printRankedSolution(Tree t, String solution){
		if(t.isEnd){
			solution+="\t"+t.session.time+"\n";
			return solution;
		}
		else {
			solution+="\t"+t.session.time;
			printRankedSolution(t, solution);
		}
		return solution;
	}
	
	public static void printRankedSolutionToFile(Tree t,String filename){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			String s=printRankedSolution(t, "");
			writer.print(s);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
