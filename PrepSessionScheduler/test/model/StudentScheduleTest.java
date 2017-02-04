package model;

import org.intervarsity.SimpleSchedule;

import junit.framework.TestCase;
import unused.StudentSchedule;

public class StudentScheduleTest extends TestCase {
	StudentSchedule s1,s2;

	public void setUp(){
		s1=new StudentSchedule(12);
		s1.timeSlots[0]=1;
		s1.timeSlots[1]=0;
		s1.timeSlots[2]=0;
		s1.timeSlots[3]=1;
		s1.timeSlots[4]=0;
		s1.timeSlots[5]=0;
		s1.timeSlots[6]=0;
		s1.timeSlots[7]=1;
		s1.timeSlots[8]=0;
		s1.timeSlots[9]=1;
		s1.timeSlots[10]=0;
		s1.timeSlots[11]=0;
		s1.determineRank(2);

		s2=new StudentSchedule(12);
		s2.timeSlots[0]=0;
		s2.timeSlots[1]=0;
		s2.timeSlots[2]=0;
		s2.timeSlots[3]=0;
		s2.timeSlots[4]=0;
		s2.timeSlots[5]=0;
		s2.timeSlots[6]=0;
		s2.timeSlots[7]=1;
		s2.timeSlots[8]=0;
		s2.timeSlots[9]=1;
		s2.timeSlots[10]=0;
		s2.timeSlots[11]=1;
		s2.determineRank(2);
	}

	public void testStudentSchedule(){
		StudentSchedule s3=new StudentSchedule(12);
		assertTrue(s3!=null);
		s3 = new StudentSchedule(s2.timeSlots);
		assertEquals(s2.getTimeSlot(1), s3.getTimeSlot(1));
		assertEquals(s2.getScheduleArray()[11], s3.getScheduleArray()[11]);
		assertEquals(s2.getScheduleArray()[5], s3.getScheduleArray()[5]);

	}


	public void testSetAll(){
		setUp();
		s2.setAll(3);
		assertEquals(3, s2.getScheduleArray()[0]);
		assertEquals(3, s2.getScheduleArray()[3]);
		assertEquals(3, s2.getScheduleArray()[6]);
		assertEquals(3, s2.getScheduleArray()[11]);

	}

	public void testFindOpenBlock(){
		setUp();
		int i = s2.findOpenBlock(0, 3);
		assertEquals(0, i);
		i= s2.findOpenBlock(9, 3);
		assertEquals(-1, i);
		i=s1.findOpenBlock(0, 3);
		assertEquals(4, i);

	}

	public void testIsBlockOpen(){
		setUp();
		assertTrue(s2.isBlockOpen(0, 3));
		assertTrue(s1.isBlockOpen(4, 3));
		assertFalse(s2.isBlockOpen(5, 3));
		assertFalse(s2.isBlockOpen(11, 3));
		assertTrue(s1.isBlockOpen(11, 1));
	}

	public void testCountOpenBlocks(){
		setUp();
		assertEquals(1,s1.countOpenBlocks(3));
		assertEquals(4,s1.countOpenBlocks(2));

	}

	public void testDetermineRank(){
		setUp();
		s1.determineRank(2);
		assertEquals(4, s1.getRank());
	}


}
