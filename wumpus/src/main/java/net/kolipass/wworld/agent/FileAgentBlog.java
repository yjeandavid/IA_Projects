package net.kolipass.wworld.agent;

import net.kolipass.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by kolipass on 12.12.13.
 */
public class FileAgentBlog extends AgentBlog {
    String fileName;
    Log log = new Log();

    public FileAgentBlog(AgentBlog agentBlog, String fileName) {
        super(agentBlog);
        this.fileName = fileName;
        initFolders();
    }

    private void initFolders() {
        File file = new File(fileName);
        String name = file.getName();
        String path = file.getPath();
        path = path.substring(0, path.indexOf(name));
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public void addNewNote(String post) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            out.println(post);
            out.close();
        } catch (Exception e) {
            log.printStackTrace(e);
        }
    }

    public String getFileName() {
        return fileName;
    }
}
