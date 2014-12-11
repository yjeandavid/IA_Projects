package main.java.net.kolipass.wworld.agent;

import main.java.net.kolipass.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kolipass on 12.12.13.
 */
public class MemoAgentBlog extends AgentBlog {
    Log log = new Log();
    BlogFrame blogFrame;

    public MemoAgentBlog(AgentBlog agentBlog) {
        super(agentBlog);
        initFrame();
    }

    private void initFrame() {
        new Runnable() {

            @Override
            public void run() {
                blogFrame = new BlogFrame();
                blogFrame.pack();
                blogFrame.setVisible(true);
            }

        }.run();
    }

    @Override
    public void addNewNote(String post) {
        if (blogFrame != null) {
            blogFrame.append(post);
        }
    }


    class BlogFrame extends JFrame {
        public JTextArea textArea;

        public BlogFrame() {
            super("Agent blog");
            init();
        }

        void append(String post) {
            if (textArea != null) {
                if (textArea.getLineCount() != 0)
                    textArea.append("\n");
                textArea.append(post);
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        }

        private void init() {
            textArea = new JTextArea();
            textArea.setFont(new Font("Serif", Font.ITALIC, 12));
//            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);

            getContentPane().add(new JScrollPane(textArea));
            setPreferredSize(new Dimension(300, 600));
            pack();
            setLocationRelativeTo(null);
            setBounds(750, 50, 300, 600);
            setVisible(true);
        }
    }
}
