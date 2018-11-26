package cn.natineprince.data.generator.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/message", method = RequestMethod.GET)
public class MessageController {

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/get/{code:.+}", method = RequestMethod.GET)
	public String getMessage(@PathVariable String code, HttpServletRequest request) {
		return this.messageSource.getMessage(code, request.getParameterValues("params"), request.getLocale());
	}
}
