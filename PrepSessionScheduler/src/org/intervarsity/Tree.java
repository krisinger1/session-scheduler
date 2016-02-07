package org.intervarsity;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

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
	
	public boolean hasLeaves(){
		if (leaves.size()>0) return true;
		else return false;
	}
	
	public void addLeaf(Tree leaf){
		leaves.add(leaf);
	}
	
	public void removeLeaf(Tree leaf){
		// TODO make sure leaf exists for this parent
		
		
		while (leaf.hasLeaves() ){
			leaf.removeLeaf(leaf.leaves.get(0));
		}
		leaves.remove(leaf);

		//System.out.println("********");
		//printTree(this, 0);
	}
	
	private boolean allLeavesDead(){
		for (Tree leaf:leaves){
			if (!leaf.isDead) return false;
		}
		return true;
	}
	
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
	
	public void printLeaves(){
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
	}
	
	public void print(){
		System.out.println("The Tree");
		if (session!=null) {System.out.print("node");session.print();}
		printLeaves();
	}
	
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
	
	private static String printRankedSolution(Tree t, String solution){
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
	}
}
