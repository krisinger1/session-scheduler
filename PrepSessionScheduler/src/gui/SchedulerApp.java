package gui;

import java.awt.EventQueue;

public class SchedulerApp {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame("InterVarsity Prep Session Scheduler");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
