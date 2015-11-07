package no.jan.android.spaceinvaders;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	private SurfaceHolder surfaceHolder;
    private GameView mGameView;
    public final long FPS = 30;
    private long gameCycleMillisec = 1000/FPS;
    private int gameState;
    public static final int STOP = 0;
    public static final int PAUSE = 1;
    public static final int RUNNING = 2;
    private ArrayBlockingQueue<MotionEvent> motionEventQueue = new ArrayBlockingQueue<MotionEvent>(20);
    private Object motionEventQueueMutex = new Object();
    
    public GameThread(SurfaceHolder surfaceHolder, GameView mGameView) {
    	super();
        this.surfaceHolder = surfaceHolder;
        this.mGameView = mGameView;
    }
    
    public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}
	
	public void feedMotionEvent(MotionEvent motionEvent) {
   	 synchronized (motionEventQueueMutex) {
   		 try {
   			 motionEventQueue.put(motionEvent);
//   			Log.d("GAME_THREAD", "fed motion event");
   		 } catch (InterruptedException iEx) {
   		 }
		}
    }
    
    public void processMotionEvent() {
   	 synchronized (motionEventQueueMutex) {
   		 try {
   			 while (motionEventQueue.size() > 0) {
   				 this.mGameView.processMotionEvent(motionEventQueue.take());
//   				Log.d("GAME_THREAD", "sendt motion event");
   			 }
   		 } catch (InterruptedException iEx) {
   		 }
		}
    }

	@Override
    public void run() {
   	 Log.d("GAME_THREAD", "Starting thread");
   	 // Fetch current HighScores  when starting thread.
   	 // Don't want to do this when game loop has started.
   	 // UI thread enough stressed during setup.
   	 HighScoreFileHandler fh = new HighScoreFileHandler(mGameView.getContext());
   	 try {
   		 List<HighScoreEntry> entries = fh.loadHighScore();
   		HighScoreEntry max = entries.get(0);
   		HighScoreEntry min = entries.get(entries.size()-1);
   		mGameView.mHighScoreMax = max.getScore();
   		mGameView.mHighScoreMin = min.getScore();
   	 } catch (Exception e) {
   	 }
   	 
   	 Canvas canvas;
   	 long previousTimestamp = System.currentTimeMillis();
   	 while (gameState == RUNNING || gameState == PAUSE) {
   		 long currentTimestamp = System.currentTimeMillis();
   		 long sleepTime = previousTimestamp + gameCycleMillisec - currentTimestamp;
   		 previousTimestamp = currentTimestamp;
   		 if (sleepTime < 0) Log.d("GAME_THREAD", "SleepTime: " + sleepTime);
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
//                	Log.d("GAME_THREAD", "Pre processMotionEvent: " + System.currentTimeMillis());
                	processMotionEvent();
                	if (gameState == RUNNING) {
//                		Log.d("GAME_THREAD", "Pre update: " + System.currentTimeMillis());
                		this.mGameView.update();
                	} else if (gameState == PAUSE) {
                		this.mGameView.pauseHandler();
                	}
//                	Log.d("GAME_THREAD", "Pre draw: " + System.currentTimeMillis());
                    this.mGameView.draw(canvas);
//                    Log.d("GAME_THREAD", "Post draw: " + System.currentTimeMillis());
                }
   		 } finally {
   			 if (canvas != null) {
   	                surfaceHolder.unlockCanvasAndPost(canvas);
   			 }
   		 }
   	 }
   	 Log.d("GAME_THREAD", "Loop ended");
   	// this.mSplashView.;
   	 Log.d("GAME_THREAD", "Returning");
   	 return;
    }

}
