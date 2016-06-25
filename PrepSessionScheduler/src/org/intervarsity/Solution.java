package org.intervarsity;

import java.util.ArrayList;

/**
 * represents a single solution of sessions that allow all students to attend at east one session
 * solutions have a rank based on how well they meet certain criteria -
 * number of sessions, if sessions are in preferred time,
 * good balance of students who must attend and can attend
 * @author Kristin
 *
 */
public class Solution implements Comparable<Solution>{
	private ArrayList<Session> sessions;
	private double rank;
	
	/**
	 * constructor
	 * initially an empty list of sessions with rank -1
	 */
	public Solution(){
		sessions=new ArrayList<Session>();
		rank=-1;
	}
	
	/**
	 * assigns a list of viable sessions to a solution
	 * @param sessions ArrayList of sessions that solve the schedule problem
	 */
	public void setSessions(ArrayList<Session> sessions){
		this.sessions=sessions;
	}
	
	/**
	 * returns the list of sessions for this solution
	 * @return the ArrayList of sessions for this solution
	 */
	public ArrayList<Session> getSessions(){
		return sessions;
	}
	
	public double getRank(){
		return rank;
	}
	
	/**
	 * counts how many of the sessions in this solution are in preferred time slots
	 * to be used in determining rank of solution
	 * @return int number of sessions
	 */
	private int numSessionsInPrefTime(){
		int count=0;
		for (Session s:sessions){
			if (s.preferred) count++;
		}
		return count;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumSessionsInPrefTime(){
		return numSessionsInPrefTime();
	}
	
	/**
	 * how many sessions make up this solution
	 * @return the number of sessions
	 */
	public int getNumSessions(){
		return sessions.size();
	}
	
	/**
	 * calculates how good this solution is - how well it meets criteria
	 * @param m1 weight of numSessions criteria
	 * @param m2 weight of preferred time criteria
	 * @param m3 weight of balance of people who _can_ attend sessions criteria
	 * @param m4 weight of balance of people who _must_ attend sessions criteria
	 */
	// Looks like this code is never used
	/*public void calculateRank(double m1,double m2,double m3, double m4){
		rank= (m1*getNumSessions()+
				m2*(getNumSessions()-numSessionsInPrefTime())+
				m3*checkCanBalance()+
				m4*checkMustBalance());
	}*/
	
	/**
	 * calculates how good this solution is - how well it meets criteria
	 * rank is normalized to 1
	 * @param m1 weight of numSessions criteria
	 * @param m2 weight of preferred time criteria
	 * @param m3 weight of balance of people who _can_ attend sessions criteria
	 * @param m4 weight of balance of people who _must_ attend sessions criteria
	 * @param solutions all possible solutions for use in normalizing rank
	 */
	public void calculateNormalizedRank(double m1,double m2,double m3, double m4,ArrayList<Solution> solutions){
		int maxNumSessions=0;
		int maxDifference=0;
		double maxMustBalance=0;
		double maxCanBalance=0;
		for (Solution sol:solutions){
			int numSess=sol.getNumSessions();
			if (numSess>maxNumSessions) maxNumSessions=numSess;
			int prefSess=sol.getNumSessionsInPrefTime();
			if (numSess-prefSess>maxDifference) maxDifference=numSess-prefSess;
			double must=sol.getMustBalance();
			if (must>maxMustBalance) maxMustBalance=must;
			double can=sol.getCanBalance();
			if (can>maxCanBalance) maxCanBalance=can;

		}
		System.out.println(maxDifference+"");
		if (maxNumSessions==0)rank=m1;
		else rank= m1*getNumSessions()/maxNumSessions;
		if (maxDifference==0) rank+=m2;
		else rank+=	m2*(getNumSessions()-numSessionsInPrefTime())/maxDifference;
		if (maxCanBalance==0) rank+=m3;
		else rank+=m3*checkCanBalance()/maxCanBalance;
		if (maxMustBalance==0) rank+=m4;
		else rank+=m4*checkMustBalance()/maxMustBalance;
		rank /=(m1+m2+m3+m4);
	}
	
	/**
	 * sets the value of the rank for this solution if it hasn't been set yet
	 * @param rank rank of this solution calculated
	 * @see calculateNormalizedRank
	 */
	public void setRank(double rank){
		if (rank==-1){
			this.rank=rank;
		}
		else System.out.println("Rank has already been calculated for this solution.");
		
	}
	
	private double checkMustBalance(){
		double diff=0;
		double total = 0;
		double average=0;
		for (Session s:sessions){
			total+=(double)numMustCome(s);
		}
		average=total/getNumSessions();
		for (Session s:sessions){
			double thisDiff=Math.abs((double)numMustCome(s)-average);
			if (thisDiff>diff)diff=thisDiff;
		}
		return diff;
	}
	
	public double getMustBalance(){
		return checkMustBalance();
	}
	
	private double checkCanBalance(){
		double diff=0;
		double total = 0;
		double average=0;
		for (Session s:sessions){
			total+=(double)numCanCome(s);
		}
		average=total/getNumSessions();
		for (Session s:sessions){
			double thisDiff=Math.abs((double)numCanCome(s)-average);
			if (thisDiff>diff)diff=thisDiff;
		}
		return diff;
	}
	
	public double getCanBalance(){
		return checkCanBalance();
	}
	
	private int numCanCome(Session thisSession){
		return thisSession.members.size();
	}
	
	private int numMustCome(Session thisSession){
		return thisSession.membersMustAttend.size();
	}
	
	/**
	 * find schedules of all students who can or must attend each session in solution
	 * @param schedules list of all schedules of students
	 * @param blockSize number of blocks in schedule that need to be open for a session to fit
	 */
	public void findAllMembers(ArrayList<Schedule> schedules, int blockSize){
		
		for (Session session:sessions){ //clear out any schedules that were added while determining possible solutions
			session.members.clear();
			session.membersMustAttend.clear();
		}
		for (Schedule schedule:schedules){
			int numFound=0; //count how many sessions fit this schedule
			Session sessionFound=null;
			for (Session session:sessions){
				if (schedule.isBlockOpen(session.time, blockSize)) {
					session.add(schedule);
					sessionFound=session;
					numFound++;
				}
			}
			//if only one session fits, also add to mustAttend list
			if (numFound==1) sessionFound.addMustAttend(schedule);
			//TODO error handling for this case
			if (numFound==0) System.out.println("ERROR: no session found for schedule: "+schedule.getName());
		}
	}
	
	public void print(){
		System.out.format("%.2f",rank);
		for (Session s:sessions){
			System.out.println("\t"+s.time);
			s.print();
		}
		System.out.println();
	}
	
	/**
	 * compare 2 solutions by rank
	 */
	public int compareTo(Solution s){
		if (rank<s.getRank()) return -1;
		else if (rank>s.getRank()) return 1;
		else return 0;
	}
	
}
