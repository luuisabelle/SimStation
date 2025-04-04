package simstation;

import mvc.Utilities;

import java.io.Serializable;

public abstract class Agent implements Runnable, Serializable {
    private int xc;
    private int yc;
    private boolean paused = false;
    private boolean stopped = false;
    private String agentName;
    transient protected Thread myThread;

    public int getXc() {
        return xc;
    }

    public int getYc() {
        return yc;
    }

    public void setXc(int xc) {
        this.xc = xc;
    }

    public void setYc(int yc) {
        this.yc = yc;
    }

    public void start() {
        myThread.start();
    }

    public void stop() {
        stopped = true;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public abstract void update();

    // in progress
    @Override
    public void run() {
        myThread = Thread.currentThread();
        onStart();
        while (!isStopped()) {
            try {
                update();
                Thread.sleep(1000);
                checkPaused();
            } catch(InterruptedException e) {
                onInterrupted();
            }
        }
        onExit();
    }

    void onExit() {
    }

    void onStart() {
    }

    void onInterrupted() {
    }

    private boolean isStopped() {
        return stopped;
    }

    private synchronized void checkPaused() {
        try {
            while(!stopped && paused) {
                wait();
                paused = false;
            }
        } catch (InterruptedException e) {
            Utilities.error(e.getMessage());
        }
    }
}
