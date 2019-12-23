package com.ibrahim.processnamechange.controller;

import com.ibrahim.processnamechange.model.ProcessWork;
import com.ibrahim.processnamechange.ui.HomeFrame;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class HomeController {

    private HomeFrame homeFrame = null;
//    private List<ProcessWork> processeList = new LinkedList<ProcessWork>();
private Map<String,ProcessWork> processMap = new ConcurrentHashMap<String, ProcessWork>();
    public HomeController(HomeFrame homeFrame) {
        this.homeFrame = homeFrame;

    }

    public void openNewBot() {

        UUID uuid = UUID.randomUUID();
        String randomFolderName = uuid.toString();
        displayLog("Opening new bot: poster_" + (processMap.size() + 1) + ".exe");
        try {
            crateDir("res/" + randomFolderName);
            copyFolder("res/main_file", "res/" + randomFolderName);
            renameFile("res/" + randomFolderName + "/poster.exe", "res/" + randomFolderName + "/poster_" + (processMap.size() + 1) + ".exe");
            renameFile("res/" + randomFolderName + "/poster.lap", "res/" + randomFolderName + "/poster_" + (processMap.size() + 1) + ".lap");

            ProcessWork processWork = new ProcessWork();
            processWork.setRandomUUId(randomFolderName);
            processWork.setBotName("poster_" + (processMap.size() + 1) + ".exe");
            openApplicaiton("res/" + randomFolderName + "/poster_" + (processMap.size() + 1) + ".exe", processWork);

            displayLog("poster_" + (processMap.size()) + ".exe BOT Opened");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteAllBot() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                if (processMap == null || processMap.isEmpty()) {
                    displayLog("There has no BOT to delete!");
                    return;
                }
                Map<String,ProcessWork> tempList = new ConcurrentHashMap<String, ProcessWork>();
                tempList = processMap;
                
                for (String key : tempList.keySet()) {
                    final String index = key;
                    Thread processThread = new Thread(new Runnable() {
                        public void run() {

                            ProcessWork processWork = processMap.get(index);
                            displayLog(processWork.getBotName() + " is deleting.");
                            if (processWork.getProcess().isAlive()) {
                                processWork.getProcess().destroy();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                                }
//                        displayLog(processWork.getBotName() + "is in use plaease close this file first.");

                            }
                            try {
                                deleteFolder("res/" + processWork.getRandomUUId());
                                displayLog(processWork.getBotName() + "is deleted.");
                                processMap.remove(processWork.getRandomUUId());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                displayLog(processWork.getBotName() + "is in use plaease close this file first.");
                                System.out.println("File in use plaease close this file first...");
                            }

                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    });
                    processThread.start();
                }
                System.out.println("tempList.size();"+tempList.size());
            }
        });

        thread.start();

    }

    private void openApplicaiton(String path, ProcessWork processWork) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("The file " + path + " does not exist");
        }
        Process p = Runtime.getRuntime().exec(file.getAbsolutePath());
        processWork.setProcess(p);
        processMap.put(processWork.getRandomUUId(),processWork);
    }

    private void crateDir(String folderPath) throws IOException {
        File folderFile = new File(folderPath);
        FileUtils.forceMkdir(folderFile);
    }

    private void copyFolder(String srcFolder, String destinationFolder) throws IOException {
        File srcDir = new File(srcFolder);
        File destDir = new File(destinationFolder);
        FileUtils.copyDirectory(srcDir, destDir);
    }

    private void renameFile(String srcFilePath, String renamedName) throws IOException {
        File srcFile = new File(srcFilePath);
        File renamedFile = new File(renamedName);
        FileUtils.moveFile(srcFile, renamedFile);
    }

    private void deleteFolder(String folderPath) throws IOException {
        File deleteFolder = new File(folderPath);
//        FileUtils.forceDelete(deleteFolder);
        FileUtils.deleteDirectory(deleteFolder);
    }

    public void displayLog(final String displayableText) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                homeFrame.displayText.setText(homeFrame.displayText.getText() + "\n->" + displayableText);
            }
        });
    }

}
