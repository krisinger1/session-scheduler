package model;

import java.awt.Color;

public class Parameters {
	public static String version = "1.0";

	public static Color schemeColor1=new Color(0,186,249);
	public static Color schemeColor2=new Color(0,168,242);

	public static int blockSize;

	public static String[] dayNames={"Wednesday","Thursday","Friday"};
	public static String[] timeSlotStrings ={"9:00","9:30","10:00","10:30","11:00","11:30","12:00",
							"12:30","1:00","1:30","2:00","2:30","3:00","3:30","4:00","4:30","5:00"};

	public static int numDays=dayNames.length;
	public static int numSlots = timeSlotStrings.length;

	//public static int[][] allowedTimesMask=new int[numDays][numSlots];
	//public static int[][] preferredTimesMask=new int[numDays][numSlots];

}


