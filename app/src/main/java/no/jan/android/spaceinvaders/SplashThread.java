package no.jan.android.spaceinvaders;

import java.util.concurrent.ArrayBlockingQueue;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class SplashThread extends Thread {
	private SurfaceHolder surfaceHolder;
    private SplashView mSplashView;
    private long FPS = 30;
    private long gameCycleMillisec = 1000/FPS;
    private int splashState;
    public static final int STOP = 0;
    public static final int RUNNING = 1;
    private ArrayBlockingQueue<MotionEvent> motionEventQueue = new ArrayBlockingQueue<MotionEvent>(20);
    private Object motionEventQueueMutex = new Object();
    
    public SplashThread(SurfaceHolder surfaceHolder, SplashView mSplashView) {
    	super();
        this.surfaceHolder = surfaceHolder;
        this.mSplashView = mSplashView;
    }
    
    public void setSplashState(int splashState) {
        this.splashState = splashState;
     }
     public int getSplashState(){
        return splashState;
     }
     
     public void feedMotionEvent(MotionEvent motionEvent) {
    	 synchronized (motionEventQueueMutex) {
    		 try {
    			 motionEventQueue.put(motionEvent);
    		 } catch (InterruptedException iEx) {
    		 }
		}
     }
     
     public void processMotionEvent() {
    	 synchronized (motionEventQueueMutex) {
    		 try {
    			 while (motionEventQueue.size() > 0) {
    				 this.mSplashView.processMotionEvent(motionEventQueue.take());
    			 }
    		 } catch (InterruptedException iEx) {
    		 }
		}
     }
     
     @Override
     public void run() {
    	 Log.d("SPLASH_THREAD", "Starting thread");
    	 Canvas canvas;
    	 long previousTimestamp = System.currentTimeMillis();
    	 while (splashState == RUNNING) {
    		 long currentTimestamp = System.currentTimeMillis();
    		 long sleepTime = previousTimestamp + gameCycleMillisec - currentTimestamp;
    		 if (sleepTime > 0) {
    			 try {
    				 Thread.sleep(sleepTime);
    			 } catch (Exception e) {
    			 }
    		 }  // else no time to sleep, running late...
    		 canvas = null;
    		 try {
    			 canvas = this.surfaceHolder.lockCanvas();
                 synchronized (surfaceHolder) {
                	 this.mSplashView.update();
                	 processMotionEvent();
                     this.mSplashView.draw(canvas);
                 }
    		 } finally {
    			 if (canvas != null) {
    	                surfaceHolder.unlockCanvasAndPost(canvas);
    			 }
    		 }
    	 }
    	 Log.d("SPLASH_THREAD", "Loop ended");
    	 this.mSplashView.moveToMenu();
    	 Log.d("SPLASH_THREAD", "Returning");
    	 return;
     }
}
