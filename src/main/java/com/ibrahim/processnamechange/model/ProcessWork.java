package com.ibrahim.processnamechange.model;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class ProcessWork {

    private Process process;
    private String randomUUId;
    private String botName;

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotName() {
        return botName;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setRandomUUId(String randomUUId) {
        this.randomUUId = randomUUId;
    }

    public Process getProcess() {
        return process;
    }

    public String getRandomUUId() {
        return randomUUId;
    }

}
