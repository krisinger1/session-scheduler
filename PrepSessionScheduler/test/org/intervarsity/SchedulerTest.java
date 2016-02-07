package org.intervarsity;

import junit.framework.TestCase;

public class SchedulerTest extends TestCase {
	SimpleSchedule s1,s2,s3,s4,s5,s6,s7;
	Tree solutionTree;
	
	private void setup(){
		s1=new SimpleSchedule("one");
		//s1.fillSchedule();
		s1.schedule[0]=true;
		s1.schedule[1]=false;
		s1.schedule[2]=false;
		s1.schedule[3]=true;
		s1.schedule[4]=false;
		s1.schedule[5]=false;
		s1.schedule[6]=false;
		s1.schedule[7]=true;
		s1.schedule[8]=false;
		s1.schedule[9]=true;
		s1.schedule[10]=false;
		s1.schedule[11]=false;
		s1.determineRank();
		

		s2=new SimpleSchedule("two");
		s2.fillSchedule();
		/*s2.schedule[0]=false;
		s2.schedule[1]=false;
		s2.schedule[2]=false;
		s2.schedule[3]=false;
		s2.schedule[4]=false;
		s2.schedule[5]=false;
		s2.schedule[6]=false;
		s2.schedule[7]=true;
		s2.schedule[8]=false;
		s2.schedule[9]=true;
		s2.schedule[10]=false;
		s2.schedule[11]=true;*/
		s2.determineRank();
		
		s3=new SimpleSchedule("three");
		s3.fillSchedule();
		/*s3.schedule[0]=true;
		s3.schedule[1]=true;
		s3.schedule[2]=true;
		s3.schedule[3]=true;
		s3.schedule[4]=true;
		s3.schedule[5]=true;
		s3.schedule[6]=false;
		s3.schedule[7]=false;
		s3.schedule[8]=false;
		s3.schedule[9]=false;
		s3.schedule[10]=false;
		s3.schedule[11]=false;*/
		s3.determineRank();
		
		s4=new SimpleSchedule("four");
		s4.fillSchedule();
		/*s4.schedule[0]=true;
		s4.schedule[1]=true;
		s4.schedule[2]=true;
		s4.schedule[3]=true;
		s4.schedule[4]=true;
		s4.schedule[5]=true;
		s4.schedule[6]=true;
		s4.schedule[7]=false;
		s4.schedule[8]=false;
		s4.schedule[9]=false;
		s4.schedule[10]=true;
		s4.schedule[11]=true;*/
		s4.determineRank();
		
		s5 =new SimpleSchedule("five");
		s5.fillSchedule();
		s5.determineRank();
		
		s6 =new SimpleSchedule("six");
		s6.fillSchedule();
		s6.determineRank();

		s7 =new SimpleSchedule("seven");
		s7.fillSchedule();
		s7.determineRank();
		
	}

	public void testCreateTree() {
		fail("Not yet implemented");
	}

	public void testMain() {
		fail("Not yet implemented");
	}

}
