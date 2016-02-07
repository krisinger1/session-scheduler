package org.intervarsity;

import junit.framework.TestCase;

public class TreeTest extends TestCase {
	private Tree t1,t11,t12,t13,t111,t112,t113,t121,t122;
	
	private void setup(){
		Session s1=new Session(11);
		Session s2=new Session(12);
		Session s3=new Session(13);
		Session s4=new Session(111);
		Session s5=new Session(112);
		Session s6=new Session(121);
		Session s7=new Session(122);
		Session s9=new Session(113);
		Session s8=new Session(0);
		t1=new Tree(null, s8);


		 t11=new Tree(t1, s1);
		 t12=new Tree(t1, s2);
		 t13=new Tree(t1, s3);
		 t111=new Tree(t11, s4);
		 t112=new Tree(t11, s5);
		 t121=new Tree(t11, s6);
		 t122=new Tree(t11, s7);
		 t113=new Tree(t11, s9);
		
		t1.addLeaf(t11); //						t1
		t1.addLeaf(t12); //		    t11			t12			t13
		t1.addLeaf(t13);  //	t111   t112		t121 t122
		t11.addLeaf(t111);
		t11.addLeaf(t112);
		t11.addLeaf(t113);
		t12.addLeaf(t121);
		t12.addLeaf(t122);

		t111.isDead=false;
		t112.isDead=false;
		t113.isDead=true;
		t122.isDead=true;
		t121.isDead=true;
		t13.isEnd=true;
		
		Tree.printTree(t1, 0);

	}

	public void testTree() {
		Tree t1=new Tree(null, null);
	}

	public void testHasLeaves() {
		setup();
		assertTrue(t1.hasLeaves());
		assertFalse(t13.hasLeaves());
	}

	public void testAddLeaf() {
		
	}

	public void testRemoveLeaf() {
		setup();
		//t1.print();
		t11.removeLeaf(t111);
		t11.removeLeaf(t112);
		t11.removeLeaf(t113);
		assertFalse("t11 still has leaves",t11.hasLeaves());
		//t1.print();
		
		setup();
		t1.removeLeaf(t13);
		//t1.print();
		
		setup();
		t1.removeLeaf(t11);
		t1.removeLeaf(t12);
		t1.removeLeaf(t13);
		assertFalse("t1 still has leaves",t1.hasLeaves());
		//t1.print();
		
	}

	public void testPruneTree() {
		setup();
		t1.pruneTree();
		System.out.println("after pruning");
		Tree.printTree(t1,0);
	}

	public void testPrintLeaves() {
		fail("Not yet implemented");
	}

	public void testPrint() {
		fail("Not yet implemented");
	}

}
