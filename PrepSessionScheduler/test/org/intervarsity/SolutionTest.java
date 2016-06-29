package org.intervarsity;

import java.util.ArrayList;
import junit.framework.TestCase;

public class SolutionTest extends TestCase {
	
	Session s1,s2,s3,s4,s5;
	ArrayList<Session> sessions=new ArrayList<Session>(); 
	int[] sched1,sched2,sched3;
	ArrayList<Schedule> schedules=new ArrayList<Schedule>();
	
	private void setup(){
		s1=new Session(3,true);
		s2=new Session(2,false);
		s3=new Session(9,false);
		s4=new Session(7,true);
		s5=new Session(5,true);
		sessions.add(s1);
		sessions.add(s2);
		sessions.add(s3);
		sessions.add(s4);
		sessions.add(s5);
		sched1= new int[]{1,1,1,1,1,0,0,0,0,0};
		sched2= new int[]{0,0,0,0,0,0,0,0,0,0};
		sched3= new int[]{0,0,0,0,0,1,1,1,1,1};
		Schedule schedule1 =new SimpleSchedule("Sched1","", 10);
		Schedule schedule2 =new SimpleSchedule("Sched2","", 10);
		Schedule schedule3 =new SimpleSchedule("Sched3","", 10);
		schedule1.setSchedule(sched1);
		schedule2.setSchedule(sched2);
		schedule3.setSchedule(sched3);
		schedules.add(schedule1);
		schedules.add(schedule2);
		schedules.add(schedule3);


}
	
	public void testSolution() {
		Solution sol1=new Solution();
		assertFalse(sol1.getSessions()==null);
		assertTrue(sol1.getRank()==-1);
	}

	public void testSetSessions() {
		setup();
		Solution sol1=new Solution();
		sol1.setSessions(sessions);
		assertEquals(3, sol1.getSessions().get(0).time);
	}

	public void testGetSessions() {
		// tested in testSetSessions
	}

	public void testGetRank() {
		setup();
		Solution sol1=new Solution();
		sol1.setSessions(sessions);
		sol1.calculateRank(1, 0, 0, 0);
		assertEquals(5.0, sol1.getRank());
	}

	public void testCalculateRank() {
		setup();
		Solution sol1=new Solution();
		sol1.setSessions(sessions);
		sol1.calculateRank(1, 1, 1, 1);
		assertFalse(sol1.getRank()==-1);
	}

	public void testFindAllMembers() {
		setup();
		Solution sol1=new Solution();
		sol1.setSessions(sessions);
		sol1.findAllMembers(schedules, 3);
		assertEquals(1,s1.members.size());
		assertEquals(2,s2.members.size());
		assertEquals(0,s3.members.size());
		assertEquals(2,s4.members.size());
		assertEquals(2,s5.members.size());

	}

	public void testCompareTo() {
		setup();
		Solution sol1=new Solution();
		sol1.setSessions(sessions);
		Solution sol2 =new Solution();
		ArrayList<Session> sessions2=new ArrayList<Session>();
		sessions2.add(s1);
		sessions2.add(s2);
		sol2.setSessions(sessions2);
		sol1.calculateRank(1, 0, 0, 0);
		sol2.calculateRank(1, 0, 0, 0);
		assertTrue(sol1.compareTo(sol2)>0);
	}

}
