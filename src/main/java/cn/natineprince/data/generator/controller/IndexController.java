package cn.natineprince.data.generator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("module", "main");
		mav.addObject("showLeftPanel", true);
		return mav;
	}

	@RequestMapping(value = "/{module}", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav, @PathVariable String module) {
		mav.setViewName("index");
		mav.addObject("module", module);
		mav.addObject("showLeftPanel", false);
		return mav;
	}

}
