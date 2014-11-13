package main.java.net.kolipass.gameEngine;

import main.java.net.kolipass.Log;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.io.FileNotFoundException;
import java.net.URL;

public class Sound {
    private Clip clip;
    private AudioInputStream stream;
    public DataLine.Info info;
    private Log log = new Log();

    public Sound(String name) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(name);
            loadFile(soundURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void loadFile(URL soundFile) {
        try {
            stream = AudioSystem.getAudioInputStream(soundFile); // obtains input stream from AudioSystem to read from the file.

            info = new DataLine.Info(Clip.class, stream.getFormat()); // obtains the sound file's line

            clip = (Clip) AudioSystem.getLine(info); // loads the line into the clip
            clip.open(stream); // opens the clip onto the stream

        } catch (FileNotFoundException e) {
            log.d("File not found.");
        } catch (Exception e) {
            log.d("Error loading sound " + soundFile);
            log.printStackTrace(e);
        }
    }

    public void play() {
        this.play(true);
    }

    public void play(boolean reset) {
        try {
            if (reset)
                clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            log.d("Sound error: Error playing sound");
            log.printStackTrace(e);
        }


    }

    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }

    public void pause() {
        clip.stop();
    }

    public void loop(int times) {
        clip.setLoopPoints(0, -1);
        if (times == 0) // loops infinitely if 0 is inputted
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
            clip.loop(times);
    }

    public int getPosition() {
        return clip.getFramePosition();
    }


    public boolean isRunning() {
        return clip.isRunning();
    }


}