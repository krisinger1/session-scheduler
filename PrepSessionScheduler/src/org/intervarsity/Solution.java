package org.intervarsity;

import java.lang.management.MemoryManagerMXBean;
import java.util.ArrayList;

import javax.naming.ldap.SortControl;

import org.w3c.dom.css.ElementCSSInlineStyle;

public class Solution implements Comparable<Solution>{
	private ArrayList<Session> sessions;
	private double rank;
	
	public Solution(){
		sessions=new ArrayList<Session>();
		rank=-1;
	}
	
	public void setSessions(ArrayList<Session> sessions){
		this.sessions=sessions;
	}
	public ArrayList<Session> getSessions(){
		return sessions;
	}
	
	public double getRank(){
		return rank;
	}
	
	private int numSessionsInPrefTime(){
		int count=0;
		for (Session s:sessions){
			if (s.preferred) count++;
		}
		return count;
	}
	
	public int getNumSessionsInPrefTime(){
		return numSessionsInPrefTime();
	}
	
	public int getNumSessions(){
		return sessions.size();
	}
	
	public void calculateRank(double m1,double m2,double m3, double m4){
		rank= (m1*getNumSessions()+
				m2*(getNumSessions()-numSessionsInPrefTime())+
				m3*checkCanBalance()+
				m4*checkMustBalance());
	}
	
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
	
	public void findAllMembers(ArrayList<Schedule> schedules, int blockSize){
		//ArrayList<Session> sessionsCopy = (ArrayList<Session>)sessions.clone();
		//for (Session session:sessionsCopy){
		//	session.members.clear();
		//}
		for (Session session:sessions){
			session.members.clear();
			session.membersMustAttend.clear();
		}
		for (Schedule schedule:schedules){
			int numFound=0;
			Session sessionFound=null;
			for (Session session:sessions){
				if (schedule.isBlockOpen(session.time, blockSize)) {
					session.add(schedule);
					sessionFound=session;
					numFound++;
				}
			}
			if (numFound==1) sessionFound.addMustAttend(schedule);
			//TODO error handling for this case
			if (numFound==0) System.out.println("ERROR: no session found for schedule: "+schedule.getName());
		}

		/*boolean must=true;
		for (Schedule s:thisSession.members){
			for (Session session:sessions){
				if (session!=thisSession && session.members.contains(s)){
						must=false;
					}
				}
			if (must) thisSession.addMustAttend(s);
			}
		
		}*/

	}
	
	public void print(){
		System.out.format("%.2f",rank);
		for (Session s:sessions){
			System.out.println("\t"+s.time);
			s.print();
		}
		System.out.println();
	}
	
	public int compareTo(Solution s){
		if (rank<s.getRank()) return -1;
		else if (rank>s.getRank()) return 1;
		else return 0;
	}
	
}
