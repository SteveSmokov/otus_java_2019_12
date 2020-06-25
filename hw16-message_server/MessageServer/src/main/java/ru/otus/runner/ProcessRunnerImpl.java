package ru.otus.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class ProcessRunnerImpl implements ProcessRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProcessRunnerImpl.class);
    private final StringBuffer stringBuffer = new StringBuffer();
    private Process process;

    @Override
    public void start(String command) {
        try {
            process = runProcess(command);
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("Process status: {}", process.isAlive());
    }

    @Override
    public void stop() {
        process.destroy();
    }

    @Override
    public String getOutput() {
        return stringBuffer.toString();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        Process localProcess = processBuilder.start();

        StreamListener processThread = new StreamListener(process.getInputStream(), "OUTPUT");
        processThread.start();
        return localProcess;
    }

    private class StreamListener extends Thread {
        private final InputStream inputStream;
        private final String type;

        public StreamListener(InputStream inputStream, String type) {
            this.inputStream = inputStream;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)){
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(type).append(">").append(line).append("\n");
                    logger.info("> {}", line);
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}


