package org.intervarsity;

import junit.framework.TestCase;

public class DayTest extends TestCase {

	Day d1,d2,d3;
	
	private  void setup(){
		d1=new Day(30, 7, new Time(9, 0), "thursday");
		d2=new Day(30, new Time(9, 0), new Time(12, 0), "friday");
		d3= new Day(30, 17, new Time(9, 0), "Thursday");

	}
	
	public void testDay() {
		Day d1=new Day(30, 7, new Time(9, 0), "thursday");
		assertEquals("thursday", d1.toString());
		d1=new Day(30, new Time(9, 0), new Time(12, 0), "friday");
		assertEquals("friday", d1.toString());
	}

	public void testGetSlotNumber(){
		setup();
		int n1=d1.getSlotNumber();
		int n2=d2.getSlotNumber();
		assertEquals(7, n1);
		assertEquals(6, n2);
	}
	
	public void testGetStartTime(){
		setup();
		assertEquals("9:00 am", d1.getStartTime().toString());
	}
	
	public void testGetSlotIncrement(){
		setup();
		assertEquals(30, d1.getSlotIncrement());
	}
	
	public void testGetName(){
		setup();
		assertEquals("thursday", d1.getName());
	}
	
	public void testGetEndTime(){
		setup();
		assertEquals("12:30 pm", d1.getEndTime().toString());
		assertEquals("12:00 pm", d2.getEndTime().toString());
		assertEquals("5:30 pm", d3.getEndTime().toString());
	}
	
}
