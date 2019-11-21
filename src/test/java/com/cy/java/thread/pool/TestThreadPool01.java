package com.cy.java.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
/**
 * ThreadPoolExecutor 执行任务
 * 第一步:先检测池中线程数是否已达到核心
 * @author Administrator
 *
 */
public class TestThreadPool01 {
	static ThreadPoolExecutor pExecutor;
	//阻塞式队列
	static BlockingQueue<Runnable> workQueue=
	new ArrayBlockingQueue<>(1);
	//线程工厂
	static ThreadFactory threadFactory = new ThreadFactory() {	
	private AtomicLong at = new AtomicLong(1);
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r,"DB-SERIVCE-ASYNC-"+at.getAndIncrement());
		}
	};
	static {
	    pExecutor=new ThreadPoolExecutor(
						2,//corePoolSize核心线程数
						3,//maximumPoolSize 最大线程数
						60,//keepAliveTime 最大空闲时间
						TimeUnit.SECONDS,
						workQueue,
						threadFactory);
	}
	public static void main(String[] args) {
		pExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String tName=Thread.currentThread().getName();
				System.out.println(tName+"-->task01");
				try{Thread.sleep(5000);}
				catch (Exception e) {}
			}
		});
		pExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String tName=Thread.currentThread().getName();
				System.out.println(tName+"-->task02");
				try{Thread.sleep(5000);}
				catch (Exception e) {}
			}
		});
		pExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String tName=Thread.currentThread().getName();
				System.out.println(tName+"-->task03");
				try{Thread.sleep(3000);}
				catch (Exception e) {}
			}
		});
		pExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String tName=Thread.currentThread().getName();
				System.out.println(tName+"-->task04");
				try{Thread.sleep(5000);}
				catch (Exception e) {}
			}
		});
		pExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String tName=Thread.currentThread().getName();
				System.out.println(tName+"-->task05");
				try{Thread.sleep(5000);}
				catch (Exception e) {}
			}
		});
	}

}









