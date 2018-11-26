package cn.natineprince.data.generator.controller;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.natineprince.data.generator.exception.CoreException;
import cn.natineprince.data.generator.pojo.GenerationConfig;
import cn.natineprince.data.generator.service.DataGenerationService;

@RestController
@RequestMapping(value = "/data/generation")
public class DataGenerationController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private DataGenerationService service;

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public Long submitGenerationTask(@RequestBody GenerationConfig gc) {
		if (gc.getBasicConfig().getTotal() < 1 || gc.getBasicConfig().getTotal() > 1000000) {
			throw new CoreException(
					messageSource.getMessage("message.error.total.range", new Object[] {}, Locale.CHINA));
		}
		if (gc.getFieldConfigs() == null || gc.getFieldConfigs().isEmpty()) {
			throw new CoreException(
					messageSource.getMessage("message.error.field.config.empty", new Object[] {}, Locale.CHINA));
		}
		return this.service.submitTask(gc);
	}

	@RequestMapping(value = "/progress/{ticketId}", method = RequestMethod.GET)
	public BigDecimal getProgress(@PathVariable Long ticketId) {
		return this.service.queryProgress(ticketId);
	}

	@RequestMapping(value = "/clear/progress/{ticketId}", method = RequestMethod.DELETE)
	public void clearProgress(@PathVariable Long ticketId) {
		this.service.clearProgress(ticketId);
	}

}
