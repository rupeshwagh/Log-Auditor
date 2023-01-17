package com.rupesh.customeventlog.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.rupesh.customeventlog.model.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class LogAuditorValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAuditorValidator.class);

    public void inputValidator(Context context, String... args) {
        LOGGER.info("Inside inputValidator()...");
        validateArguments(args);
        validateFilePath(context, args[0]);
    }

    private void validateFilePath(Context context, String logFilePath) {
        LOGGER.info("Inside validateFilePath()...", logFilePath);
        context.setLogFilePath(logFilePath);

        try {
            File file = new ClassPathResource("testlogfile/" + logFilePath).getFile();
            if (!file.exists()) {
                file = new ClassPathResource(logFilePath).getFile();
                if (!file.exists()) {
                    file = new File(logFilePath);
                }
            }

            if (!file.exists())
                throw new FileNotFoundException("Unable to open the file " + logFilePath);
        } catch (IOException e) {
            LOGGER.error("!!! Unable to find the specified file '{}'", logFilePath);
        }
    }

    private void validateArguments(String[] args) {
        LOGGER.debug("Validating the program arguments...");
        if (args.length < 1) {
            throw new IllegalArgumentException("Please add file as an argument.");
        }
    }
}
