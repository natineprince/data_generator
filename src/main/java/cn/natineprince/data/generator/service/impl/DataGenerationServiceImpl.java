package cn.natineprince.data.generator.service.impl;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import cn.natineprince.data.generator.pojo.GenerationConfig;
import cn.natineprince.data.generator.service.DataGenerationService;
import cn.natineprince.data.generator.task.GenerationTask;
import cn.natineprince.data.generator.task.TaskProgress;

@Service
public class DataGenerationServiceImpl implements DataGenerationService {
	
	private static ConcurrentMap<Long, TaskProgress> progressMap = new ConcurrentHashMap<>();
	private AtomicLong ticketCount = new AtomicLong(1);

	@Override
	public Long submitTask(GenerationConfig gc) {
		Long ticketId = ticketCount.getAndIncrement();
		TaskProgress taskProgress = new TaskProgress(gc.getBasicConfig().getTotal(), new AtomicLong(0));
		progressMap.putIfAbsent(ticketId, taskProgress);
		ExecutorService es = Executors.newFixedThreadPool(4);
		for(long i=0; i<gc.getBasicConfig().getTotal(); i+=1000){
			es.submit(new GenerationTask(this, ticketId, gc, i));
		}
		es.shutdown();
		return ticketId;
	}

	@Override
	public BigDecimal queryProgress(Long ticketId) {
		return progressMap.get(ticketId).getProgress();
	}

	@Override
	public void reportProgress(Long ticketId, Long count) {
		progressMap.get(ticketId).reportProgress(count);
	}

	@Override
	public void clearProgress(Long ticketId) {
		progressMap.remove(ticketId);
	}

}
