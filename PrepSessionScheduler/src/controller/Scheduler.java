package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.intervarsity.Schedule;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import model.Session;
import model.Student;
import model.TimeSlot;
import model.Tree;

public class Scheduler {
	public static void createTree(ArrayList<Student> studentList,Tree parent, int[][] mask, int blockSize, int maxStudents){
		System.out.println("in createTree");
		if (studentList.size()==0) { //if no students left in list, then this solution branch complete
			parent.isEnd=true;
			System.out.println("Scheduler: END of branch");

			return;
		}
		else {
			int maxDay = mask.length;
			int maxTime = mask[0].length;
			TimeSlot timeSlot;
			int dayIndex=0;
			int timeIndex=0;
			timeSlot=new TimeSlot(dayIndex, timeIndex);
			boolean foundOne=false;

			// start with worst schedule
			Student worst=studentList.get(0);
			System.out.println("Scheduler: worst schedule: "+worst.toString());

			//new code
			// while there are still students unassigned and there are still timeslots to try...
			while (studentList.size()>0 && dayIndex<maxDay && timeIndex<maxTime-blockSize){
				timeSlot=new TimeSlot(dayIndex, timeIndex);
				System.out.println("Scheduler: timeslot in while "+timeSlot.toString());
				// find first open block in worst schedule
				timeSlot= worst.findOpenBlock(timeSlot, blockSize);


				if (timeSlot!=null){  // if worst schedule has a timeslot available, check if it works...
					System.out.println("Scheduler: timeslot of worst "+timeSlot.toString());

					// get day and time of open block
					dayIndex=timeSlot.getDay();
					timeIndex=timeSlot.getTime();

					// See if mask allows this timeslot as a session
					boolean maskOpen=true; // assume mask has slot open
					for (int i=0;i<blockSize;i++){
						if (mask[dayIndex][timeIndex]==1) maskOpen=false; //if any slot has a 1 time slot is not open in mask
					}

					// if mask allows this timeslot, see who else can come...
					if (maskOpen){
						ArrayList<Student> smallerStudentList, tempList;
						smallerStudentList = new ArrayList<Student>();
						smallerStudentList=(ArrayList<Student>) studentList.clone();
						tempList=new ArrayList<Student>();

						//see which people can come up to max allowed
						for (Student stu:studentList){
							if (stu.hasBlockOpen(timeSlot, blockSize) && tempList.size()<maxStudents){
								tempList.add(stu);
							}
						}
						System.out.println("Scheduler: templist size "+tempList.size());

						// don't check for minSessionSize here. Some students who can come may have already been taken out of list
						// check later when creating solutions

						foundOne=true; // we found a legit session
						System.out.println("Scheduler: found one: "+foundOne);
						Session session = new Session(timeSlot); //create session
						System.out.println("Scheduler: session: "+session.toString());

						Tree solutionLeaf = new Tree(parent, session); // create new leaf for tree
						parent.addLeaf(solutionLeaf); //add leaf to tree

						//block out this section in mask copy to send into next level of tree - prevent overlapping sessions
						int[][] newMask = new int[3][17];
						for (int i=0; i<3;i++){
							for (int j=0;j<17;j++){
								newMask[i][j]=mask[i][j];
							}
						}
						//newMask=mask.clone();
						for (int j=0;j<blockSize;j++) newMask[dayIndex][timeIndex+j]=1; //block out a blocksize section at this index so no overlapping sessions
						//all all students who can come to the members of this session
						session.members.addAll(tempList);
						//create list of just the remaining unassigned students
						smallerStudentList.removeAll(tempList);
						// make tree from this new leaf/session just created
						createTree(smallerStudentList,solutionLeaf,newMask,blockSize,maxStudents);
						System.out.println("Scheduler: out of create tree ");

					}  // end if mask open

					else{
						// else if mask is not open
						// increment timeslot and try again?
						if (timeIndex==maxTime) {  //if at end of day...
							if (dayIndex<maxDay){  //if not already on last day go to beginning of next day
								dayIndex++;
								timeIndex=0;
							} // else dayIndex=maxday and timeindex=maxtime and should exit while loop
						}
						else timeIndex++; //else just move ahead one timeslot on same day
					} // end else mask not open

				} // end if timeslot!=null


				// else if timeslot==null - ran out of options to try
				// if haven't found any legit sessions, this parent node has no leaves and is dead
				else {  //timeslot==null
					if (!foundOne) parent.isDead=true;
					//else parent.isEnd=true;
					return;
				}
				if (timeIndex==maxTime) {  //if at end of day...
					if (dayIndex<maxDay){  //if not already on last day go to beginning of next day
						dayIndex++;
						timeIndex=0;
					} // else dayIndex=maxday and timeindex=maxtime and should exit while loop
				}
				else timeIndex++; //else just move ahead one timeslot on same day

			} // end while
			if (!foundOne) parent.isDead=true;
		}
	}
}


