package ru.otus.runner;

public interface ProcessRunner {
    void start(String command);
    void stop();
    String getOutput();
}
