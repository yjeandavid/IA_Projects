package main.java.net.kolipass.wworld.agent;

/**
 * Created by kolipass on 12.12.13.
 */
public abstract class AgentBlog {
    AgentBlog agentBlog;

    protected AgentBlog(AgentBlog agentBlog) {
        this.agentBlog = agentBlog;
    }

    protected final void note(String post) {
        if (agentBlog != null) {
            agentBlog.note(post);
        }
        addNewNote(post);
    }

    protected abstract void addNewNote(String post);
}
