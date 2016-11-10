package cn.itcast.googleplay09.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolManager {

	private MyThreadPoolManager() {
	}

	private static MyThreadPoolManager instance;

	public synchronized static MyThreadPoolManager getInstance() {
		if (instance == null) {
			instance = new MyThreadPoolManager();
		}
		return instance;
	}

	// 线程池的对象
	private ThreadPoolExecutor executor;

	// 使用线程池，线程池中线程的创建完全是由线程池自己来维护的，我们不需要创建任何的线程
	// 我们所需要做的事情就是往这个池子里面丢一个又一个的任务
	public void execute(Runnable r) {
		if (executor == null) {
			/**
			 * corePoolSize:正常情况下，池子里面线程的数量
			 * maximumPoolSize：非正常的情况下(workqueue满了的情况)，池子里面的线程数量
			 * keepAliveTime：空闲时间
			 * unit：keepAliveTime的单位
			 * workQueue：等待队列，存储还未执行的任务
			 * threadFactory：线程创建的工厂
			 * handler：异常处理机制
			 * 
			 */
			executor = new ThreadPoolExecutor(3, 5,
					0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
			
			//根据cpu的数量动态的配置核心线程数和最大线程数
			int cpuCounts = Runtime.getRuntime().availableProcessors();
			//corePoolSize = cpuCounts*2 + 1;
		}

		executor.execute(r);// 把一个任务丢到了线程池中

	}
	
	public  void cancel(Runnable r) {
		if(r != null) {
			executor.getQueue().remove(r);//把任务移除等待队列
		}
	}
}
