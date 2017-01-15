package org.intervarsity;

import junit.framework.TestCase;

public class SimpleScheduleTest extends TestCase {

	SimpleSchedule s1;
	SimpleSchedule s2;

	private void setup(){
		s1=new SimpleSchedule("one","",12);
		s1.schedule[0]=1;
		s1.schedule[1]=0;
		s1.schedule[2]=0;
		s1.schedule[3]=1;
		s1.schedule[4]=0;
		s1.schedule[5]=0;
		s1.schedule[6]=0;
		s1.schedule[7]=1;
		s1.schedule[8]=0;
		s1.schedule[9]=1;
		s1.schedule[10]=0;
		s1.schedule[11]=0;
		s1.determineRank(2);

		s2=new SimpleSchedule("two","",12);
		s2.schedule[0]=0;
		s2.schedule[1]=0;
		s2.schedule[2]=0;
		s2.schedule[3]=0;
		s2.schedule[4]=0;
		s2.schedule[5]=0;
		s2.schedule[6]=0;
		s2.schedule[7]=1;
		s2.schedule[8]=0;
		s2.schedule[9]=1;
		s2.schedule[10]=0;
		s2.schedule[11]=1;
		s2.determineRank(2);

	}

	public void testSimpleSchedule() {

		SimpleSchedule s3=new SimpleSchedule("three","",12);
		assertTrue(s3!=null);


	}

	public void testFillSchedule() {
		SimpleSchedule s4=new SimpleSchedule("four","",12);
		SimpleSchedule s5=new SimpleSchedule("five","",12);
		s4.fillSchedule();
		s5.fillSchedule();
		s4.printSchedule();
		s5.printSchedule();
		boolean different=false;
		for (int i=0;i<12 && !different;i++){
			if (s4.schedule[i]!=s5.schedule[i]) different=true;
		}
		assertTrue(different);
	}

	public void testFindOpenBlock() {
		setup();
		int index=0;
		//System.out.println("xx"+index);
		index=s1.findOpenBlock(index,2);
		//System.out.println("xx"+index);
		assertEquals(1,index);
		index++;
		index=s1.findOpenBlock(index,2);
		//System.out.println("xx"+index);
		assertEquals(4,index);
		index++;
		index=s1.findOpenBlock(index,2);
		//System.out.println("xx"+index);
		assertEquals(5,index);
		index++;
		index=s1.findOpenBlock(index,2);
		//System.out.println("xx"+index);
		assertEquals(10,index);
	}

	public void testCountOpenBlocks() {
		setup();
		int count=s1.countOpenBlocks(2);
		assertEquals(4,count);
	}

	public void testIsBlockOpen(){
		setup();
		boolean open = s2.isBlockOpen(1,2);
		assertTrue(open);
		open=s2.isBlockOpen(11,2);
		assertFalse(open);
	}

	public void testGetRank(){
		setup();
		assertEquals(6,s2.getRank());
		assertEquals(4,s1.getRank());
	}

	public void testToString(){
		setup();
		assertEquals("1\n0\n0\n1\n0\n0\n0\n1\n0\n1\n0\n0",s1.toString());
	}
}

