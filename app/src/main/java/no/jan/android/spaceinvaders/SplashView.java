package no.jan.android.spaceinvaders;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SplashView extends SurfaceView implements SurfaceHolder.Callback {
	private SplashThread mSplashThread;
	private Bitmap invader1a;
	private Bitmap invader1b;
	private Bitmap invader2a;
	private Bitmap invader2b;
	private Bitmap invader3a;
	private Bitmap invader3b;
	private Bitmap mFwdButton;
	private Bitmap mFwdButtonDwn;
	private Bitmap mBackground;
	InvaderSprite sprite;
	ButtonSprite mSkipButton;
	GraphicsHelper graphicsHelper;
	private Set<InvaderSprite> mInvaderSet;
	private Paint myPaint = new Paint(Color.YELLOW);
	private Context context;
	private final String SPLASH_VIEW_LOG_TAG = "SPLASH_VIEW";
//	private SoundPool soundPool;
//	private int alienBlip;

	public SplashView(Context context) {
		super(context);
		this.context = context;
		setFocusable(true);
		invader1a = BitmapFactory.decodeResource(getResources(), R.drawable.invader1a);
		invader1b = BitmapFactory.decodeResource(getResources(), R.drawable.invader1b);
		invader2a = BitmapFactory.decodeResource(getResources(), R.drawable.invader2a);
		invader2b = BitmapFactory.decodeResource(getResources(), R.drawable.invader2b);
		invader3a = BitmapFactory.decodeResource(getResources(), R.drawable.invader3a);
		invader3b = BitmapFactory.decodeResource(getResources(), R.drawable.invader3b);
		mFwdButton = BitmapFactory.decodeResource(getResources(), R.drawable.forward_button);
		mFwdButtonDwn = BitmapFactory.decodeResource(getResources(), R.drawable.forward_button_dwn);
		mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.earth_moon_dimmed);
		
//		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
//		alienBlip = soundPool.load(context, R.raw.alien_blip_2, 1);
		
		mSplashThread = new SplashThread(getHolder(), this);
		mSplashThread.setName("SplashThread");
		getHolder().addCallback(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		mSplashThread.feedMotionEvent(event);
		Log.d("SPLASH_VIEW", "onTouchEvent() thread name: " + Thread.currentThread().getName());
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(SPLASH_VIEW_LOG_TAG, "KeyCode: " + keyCode + ", keyCodeToString: " + KeyEvent.keyCodeToString(keyCode));
		if(keyCode == KeyEvent.KEYCODE_BACK){
	        Log.d(SPLASH_VIEW_LOG_TAG, "back key captured");
	        mSplashThread.setSplashState(SplashThread.STOP);
	    }
		if(keyCode == KeyEvent.KEYCODE_HOME){
	        Log.d(SPLASH_VIEW_LOG_TAG, "home key captured");
	        mSplashThread.setSplashState(SplashThread.STOP);
	    }
		return super.onKeyDown(keyCode, event);
	}
	
	public void processMotionEvent(MotionEvent event) {
		Log.d("SPLASH_VIEW", "processMotionEvent() thread name: " + Thread.currentThread().getName());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (mSkipButton.isInsideButton((int)event.getX(), (int)event.getY())) {
				mSkipButton.setPressed(true);
				Log.d("SPLASH_VIEW", "DOWN detected: " + (int)event.getX() + "/" + (int)event.getY());
			}
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (mSkipButton.isInsideButton((int)event.getX(), (int)event.getY())) {
				mSkipButton.setPressed(true);
				Log.d("SPLASH_VIEW", "MOVE detected: " + (int)event.getX() + "/" + (int)event.getY());
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("SPLASH_VIEW", "Width in surfaceCreated(): " + getWidth());
		initializeSplash();
		mSplashThread.setSplashState(SplashThread.RUNNING);
		mSplashThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
//		soundPool.release();
	}
	
	private void initializeSplash() {
		graphicsHelper = new GraphicsHelper(getWidth(), getHeight());
		invader1a = graphicsHelper.scaleBitmap(invader1a);
		invader1b = graphicsHelper.scaleBitmap(invader1b);
		invader2a = graphicsHelper.scaleBitmap(invader2a);
		invader2b = graphicsHelper.scaleBitmap(invader2b);
		invader3a = graphicsHelper.scaleBitmap(invader3a);
		invader3b = graphicsHelper.scaleBitmap(invader3b);
		mFwdButton = graphicsHelper.scaleBitmap(mFwdButton);
		mFwdButtonDwn = graphicsHelper.scaleBitmap(mFwdButtonDwn);
		mSkipButton = new ButtonSprite(mFwdButton,mFwdButtonDwn, graphicsHelper.placeSkipSplashButton(mFwdButton));
		mInvaderSet = new HashSet<InvaderSprite>();
		int y1 = getHeight()/2 - (int) (invader3a.getHeight() * 6);
		int y2 = getHeight()/2 - (int) (invader3a.getHeight() * 4.5);
		int y3 = getHeight()/2 - (int) (invader3a.getHeight() * 3);
		int y4 = getHeight()/2 - (int) (invader3a.getHeight() * 1.5);
		int y5 = getHeight()/2;
		int stepSize = invader3a.getWidth()/3;
		int stepInterval = 15;
		for (int i=1; i<=5; i++) {
			// Entering left
			int leftX = -((int)(i*invader3a.getWidth()*1.5) - invader3a.getWidth());
			int rightX = getWidth() + ((int)(i*invader3a.getWidth()*1.5)) - invader3a.getWidth();
			sprite = new InvaderSprite(invader1a, invader1b, leftX, y1, stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader2a, invader2b, leftX, y2, stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader2a, invader2b, leftX, y3, stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader3a, invader3b, leftX, y4, stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader3a, invader3b, leftX, y5, stepSize, stepInterval);
			mInvaderSet.add(sprite);
			// Entering right
			sprite = new InvaderSprite(invader1a, invader1b, rightX, y1, -stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader2a, invader2b, rightX, y2, -stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader2a, invader2b, rightX, y3, -stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader3a, invader3b, rightX, y4, -stepSize, stepInterval);
			mInvaderSet.add(sprite);
			sprite = new InvaderSprite(invader3a, invader3b, rightX, y5, -stepSize, stepInterval);
			mInvaderSet.add(sprite);
		}
			
	}
	
	public void draw(Canvas canvas) {
//		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(mBackground, 0, 0, null);
		for (InvaderSprite invader : mInvaderSet) {
			invader.draw(canvas);
		}
		if (!mInvaderSet.isEmpty()) {
			myPaint.setColor(Color.YELLOW);
			myPaint.setTextSize(50);
			myPaint.setStyle(Paint.Style.FILL);
			myPaint.setStrokeWidth(3);
			String text = "SPACE";
			canvas.drawText(text, getWidth()/2-graphicsHelper.getPixelWidthForText(text, myPaint)/2,
					getHeight()/9, myPaint);
			text = "INVADERS";
			canvas.drawText(text, getWidth()/2-graphicsHelper.getPixelWidthForText(text, myPaint)/2,
					getHeight()/6, myPaint);
		}
		mSkipButton.draw(canvas);
	}
	
	public void update() {
		boolean sound = false;
		for (InvaderSprite invader : mInvaderSet) {
			if (invader.update()) {
				sound = true;
			}
		}
//		if (sound) {
//			soundPool.play(alienBlip,1.0f,1.0f,10,0,1f);
//		}
		processInvaderSet();
		if (mSkipButton.isPressed()) {
			Log.d("INVADER_SPLASH", "Skip button pressed. Stopping splash thread.");
			mSplashThread.setSplashState(SplashThread.STOP);
		}
	}
	
	private void processInvaderSet() {
		int speedUpLimitRight = getWidth()/2 + invader3a.getWidth()/2; 
		int speedUpLimitLeft = getWidth()/2 - invader3a.getWidth()/2;
		boolean speedUp = false;
		for (Iterator<InvaderSprite> spriteIter = mInvaderSet.iterator(); spriteIter.hasNext();) {
			InvaderSprite sprite = spriteIter.next();
		    if (sprite.getStepSize() > 0) {
		    	if (sprite.getX() > getWidth()) {
		    		spriteIter.remove();
//		    		Log.d("INVADER_SPLASH", "Removing rightwalkers");
		    	} else if (sprite.getX() > speedUpLimitLeft) {
//		    		sprite.setStepInterval(sprite.getStepInterval()/2);
		    		speedUp = true;
		    	}
		    } else if (sprite.getStepSize() < 0) {
		    	if (sprite.getX() < 0) {
		    		spriteIter.remove();
//		    		Log.d("INVADER_SPLASH", "Removing leftwalkers");
		    	} else if (sprite.getX() < speedUpLimitRight) {
//		    		sprite.setStepInterval(sprite.getStepInterval()/2);
		    		speedUp = true;
		    	}
		    }
		}
		if (speedUp) {
			for (InvaderSprite invader : mInvaderSet) {
				invader.setStepInterval(sprite.getStepInterval()/2);
			}
		}
		if (mInvaderSet.isEmpty()) {
			Log.d("INVADER_SPLASH", "Stopping splash thread.");
			mSplashThread.setSplashState(SplashThread.STOP);			
		}
	}
	
	public void moveToMenu() {
		Log.d("INVADER_SPLASH", "Starting main menu.");
		context.startActivity(new Intent(context, MainMenuActivity.class));
		((Activity)context).finish();
	}
}
