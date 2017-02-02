package it.pm.thread;

public class MyThread extends NotifyingThread {

	@Override
	public void doRun() {
		System.out.println("my Thread");
		
	}

}
