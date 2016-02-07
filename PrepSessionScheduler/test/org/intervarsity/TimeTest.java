package org.intervarsity;

import junit.framework.TestCase;

public class TimeTest extends TestCase {
	public void testTime() {
		Time t1=new Time(10,30);
		assertEquals(10, t1.getHour());
		assertEquals(30, t1.getMinute());
		assertEquals("10:30 am", t1.toString());
		Time t2=new Time(10,61);
		assertEquals(11, t2.getHour());
		assertEquals(1, t2.getMinute());
		t1= new Time(-1, -1);
		assertEquals("12:00 pm", t1.toString());
		t1=new Time(13, 00);
		assertEquals("1:00 pm", t1.toString());
		t1=new Time(0, 00);
		assertEquals("12:00 am", t1.toString());
		t1=new Time(12, 0,"a");
		assertEquals("12:00 am", t1.toString());
		t1=new Time(12, 0,"p");
		assertEquals("12:00 pm", t1.toString());
		t1=new Time(13, 0,"a");
		assertEquals("1:00 am", t1.toString());
		t1=new Time(1, 15,"p");
		assertEquals("1:15 pm", t1.toString());
	}
	
	public void testGetHour(){
		Time t1=new Time(11, 15);
		int hour=t1.getHour();
		assertEquals(11, hour);
	}
		
	public void testGetMinute(){
		Time t1=new Time(11, 15);
		int minute=t1.getMinute();
		assertEquals(15, minute);
	}
	
	public void testTimeDifference(){
		Time t1=new Time(11, 15);
		Time t2=new Time(12, 00);
		int diff=t1.timeDifference(t2);
		assertEquals(45, diff);
		diff=t2.timeDifference(t1);
		assertEquals(45, diff);
	}
	
	public void testCompareTo(){
		Time t1=new Time(13, 15);
		Time t2=new Time(10, 00);
		Time t3=new Time(1,15,"p");
		assertTrue("failed: t1 should be greater than t2",t1.compareTo(t2)>0);
		assertTrue("failed: t2 should be less than t1",t2.compareTo(t1)<0);
		assertTrue("failed: t1 & t3 should be equal",t1.compareTo(t3)==0);
	}

	public void testNextTime() {
		Time t1=new Time(10,0);
		Time t2=t1.nextTime(15);
		assertEquals("10:15 am",t2.toString());
		assertEquals("10:00 am", t1.toString());
		assertEquals(10,t2.getHour());
		assertEquals(15,t2.getMinute());
		t2=t1.nextTime(120);
		assertEquals(12,t2.getHour());
		assertEquals(0,t2.getMinute());
		assertEquals("12:00 pm", t2.toString());
		assertEquals("10:00 am", t1.toString());
	}
	
	public void testAdvanceTime(){
		Time t1=new Time(11, 55);
		t1.advanceTime(7);
		assertEquals("12:02 pm", t1.toString());
		t1.advanceTime(7);
		assertEquals("12:09 pm", t1.toString());
	}

	public void testToString() {
		Time t1=new Time(10,0);
		assertEquals("10:00 am",t1.toString());
		t1=new Time(12,0);
		assertEquals("12:00 pm",t1.toString());
		t1=new Time(13,0);
		assertEquals("1:00 pm",t1.toString());
	}

}
