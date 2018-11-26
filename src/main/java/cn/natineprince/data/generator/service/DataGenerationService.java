package cn.natineprince.data.generator.service;

import java.math.BigDecimal;

import cn.natineprince.data.generator.pojo.GenerationConfig;

public interface DataGenerationService {
	Long submitTask(GenerationConfig gc);
	BigDecimal queryProgress(Long ticketId);
	void reportProgress(Long ticketId, Long count);
	void clearProgress(Long ticketId);
}
