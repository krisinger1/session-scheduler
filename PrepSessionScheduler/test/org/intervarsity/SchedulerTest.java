package org.intervarsity;

import junit.framework.TestCase;

public class SchedulerTest extends TestCase {
	SimpleSchedule s1,s2,s3,s4,s5,s6,s7;
	Tree solutionTree;
	
	private void setup(){
		s1=new SimpleSchedule("one",12);
		//s1.fillSchedule();
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

		s2=new SimpleSchedule("two",12);
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
		s2.determineRank(2);
		
		s3=new SimpleSchedule("three",12);
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
		s3.determineRank(2);
		
		s4=new SimpleSchedule("four",12);
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
		s4.determineRank(2);
		
		s5 =new SimpleSchedule("five",12);
		s5.fillSchedule();
		s5.determineRank(2);
		
		s6 =new SimpleSchedule("six",12);
		s6.fillSchedule();
		s6.determineRank(2);

		s7 =new SimpleSchedule("seven",12);
		s7.fillSchedule();
		s7.determineRank(2);
		
	}

	public void testCreateTree() {
		fail("Not yet implemented");
	}

	public void testMain() {
		fail("Not yet implemented");
	}

}
