package com.rupesh.customeventlog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupesh.customeventlog.configuration.ApplicationData;
import com.rupesh.customeventlog.manager.LogAuditorManager;
import com.rupesh.customeventlog.model.Context;
import com.rupesh.customeventlog.validator.LogAuditorValidator;

@Service
public class LogAuditorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAuditorService.class);

    @Autowired
    private LogAuditorValidator validator;

    @Autowired
    private LogAuditorManager manager;

    public void execute(String... args) {
    	LOGGER.info("Inside execute()..");
        Context context = Context.getInstance();
        validator.inputValidator(context, args);
        manager.readAndPreserveEvent(context);
        LOGGER.info("Completed execute()..");
    }

}
