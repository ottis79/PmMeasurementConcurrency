package it.pm.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CallableExample {
	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		List<Future<Integer>> resultList = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			Integer number = random.nextInt(10);
			FactorialCalculator calculator = new FactorialCalculator(number);
			final Future<Integer> result = executor.submit(calculator);
			resultList.add(result);
		}
		long startTime = System.currentTimeMillis();
		while(!isDone(resultList)&&(System.currentTimeMillis()-startTime)<1000){
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(isDone(resultList)){
			System.out.println("Done "+ isDone(resultList));
			System.out.println("Time remaining "+ (System.currentTimeMillis()-startTime));
		}
		else System.out.println("timeOut");
		
		for (Future<Integer> future : resultList) {
			try {
				if(!future.isDone()) throw new TimeoutException();
				System.out.println(
						"Future result is - " + " - " + future.get() + "; And Task done is " + future.isDone());
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				System.out.println(e.getMessage());
				future.cancel(true);
			}
		}
		System.out.println("Dopo i risultati");
		// shut down the executor service now
		executor.shutdown();
	}
	
	public static boolean isDone(List<Future<Integer>> resultList){
		for (Future<Integer> future : resultList) {
			if(!future.isDone())
				return false;
		}
		return true;	
	}
}