package cn.natineprince.data.generator.task;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;

public class TaskProgress {
	
	private Long total;
	private AtomicLong count;
	@Getter
	private BigDecimal progress;
	
	public TaskProgress(Long total, AtomicLong count) {
		super();
		this.total = total;
		this.count = count;
		this.progress = BigDecimal.ZERO;
	}
	
	public void reportProgress(Long taskCount){
		Long current = this.count.addAndGet(taskCount);
		this.progress = BigDecimal.valueOf(current, 2).divide(BigDecimal.valueOf(this.total,2));
	}

}
