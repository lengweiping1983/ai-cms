package com.ai.env.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimpleErrorController {
    private final Logger logger = LoggerFactory.getLogger(SimpleErrorController.class);

    @RequestMapping(value = "/401")
    public String handle401() {
        logger.info("handle 401 : ");

        return "errors/401";
    }

    @RequestMapping(value = "/403")
    public String handle403() {
        logger.info("handle 403 : ");

        return "errors/403";
    }

    @RequestMapping(value = "/404")
    public String handle404() {
        logger.info("handle 404 : ");

        return "errors/404";
    }

    @RequestMapping(value = "/500")
    public String handle500() {
        logger.info("handle 500 : ");

        return "errors/500";
    }
}