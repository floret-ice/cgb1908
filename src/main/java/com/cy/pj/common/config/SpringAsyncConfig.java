package com.cy.pj.common.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "task.executor.pool")
public class SpringAsyncConfig implements AsyncConfigurer{
	//核心线程数
	private int corePoolSize=8;
	//最大线程数
	private int maximumPoolSize=128;
	//线程空闲时间
	private int keepAliveTime=60;
	//阻塞队列容量
	private int queueCapacity=128;
	//构建线程工厂
	private ThreadFactory threadFactory=new ThreadFactory() {
		//CAS 算法
		private AtomicLong at = new AtomicLong(1);
		@Override
		public Thread newThread(Runnable r) {		
			return new Thread(r,
					"DB-SERVICE-ASYNC-"+at.getAndIncrement());
		}
	};
	@Override
	public Executor getAsyncExecutor() {
		System.out.println("corePoolSize="+corePoolSize);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maximumPoolSize);
		executor.setKeepAliveSeconds(keepAliveTime);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadFactory(threadFactory);
		executor.setRejectedExecutionHandler((Runnable r,
				 ThreadPoolExecutor exe) -> {
			log.warn("当前任务线程池队列已满");
		});
		executor.initialize(); //ThreadPoolExecutor
		return executor;		
	}
	@Override
	public AsyncUncaughtExceptionHandler 
	   getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			
			@Override
			public void handleUncaughtException(Throwable ex, 
					Method method, Object... params) {
				log.error("线程池执行任务发生未知异常.",ex);
				
			}
		};
	}

}
