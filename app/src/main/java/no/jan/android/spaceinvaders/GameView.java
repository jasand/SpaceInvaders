package no.jan.android.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private final String GAME_VIEW_LOG_TAG = "GAME_VIEW";
	private GameActivity mGameActivity;
	public GameThread mGameThread;
	
	// Gameplay variables
	private int mLevel;
	private int mScore;
	private int mLives;
	int mInvaderCount;
	int mHighScoreMax;
	int mHighScoreMin;
	private boolean mGameOver;
	private boolean mTurnInvaders;
	private boolean mNewHighScore = false;
	private int mStatusbarHeight;
	private Rect mGameBorder;
	private Rect mGameBorderPadded;
	private final int INITIAL_INVADER_COUNT = 10*5;
	private final int NUM_BUNKERS = 4;
	private final int MAX_PLAYER_SHOTS = 8;
	private final int MAX_ZIGZAG_SHOTS = 8;
	private int mSpeedUpAtInvaderCount;
	private final int INVADER_1_SCORE = 30;
	private final int INVADER_2_SCORE = 20; 
	private final int INVADER_3_SCORE = 10; 
	private long mLastPlayerMissile = 0;
	private final long PLAYER_MISSILE_INTERVAL = 500l; 
	private long mLastZigZagMissile = 0;
	private final long ZIGZAG_MISSILE_INTERVAL = 10000l;
	private long mNextMysteryPass;
	private final long MYSTERY_PASS_INTERVAL = 60000l;
	
	// Arrays for gameplay
	private InvaderSprite[] mInvaderArray = new InvaderSprite[INITIAL_INVADER_COUNT];
	private BunkerSpriteGroup[] mBunkerArray = new BunkerSpriteGroup[NUM_BUNKERS];
	private RegularSprite[] mPlayerShotsArray;
	private ZigZagShotSprite[] mZigZagShotsArray;
	// Object holders for gameplay
	private RegularSprite mMystery;
	private RegularSprite mMysteryExplosion;
	
	// Graphics helper classes
	private GraphicsHelper mGraphicsHelper;
	private BitmapHelper mBitmapHelper;
	
	// Buttons
	private ButtonSprite mFirebutton;
	private ButtonSprite mLeftButton;
	private ButtonSprite mRightButton;
	private ButtonSprite mStopBtn;
	private ButtonSprite mPausePlayBtn;
	private ButtonSprite mSoundCtrl;
	
	// Sprites - reused between levels and different games
	//           Only created after onCreate and initialization.
	private RegularSprite mCannon;
	private BunkerSpriteGroup mBunkerSpriteGroup;
	private RegularSprite mMysterySprite;
	private RegularSprite mMysteryExplosion100;
	private RegularSprite mMysteryExplosion200;
	private RegularSprite mMysteryExplosion300;
	private InvaderSprite mInvaderSprite;
	private RegularSprite mInvaderExplosionSprite;
	private List<InvaderSprite> mInvaderList;
	private List<RegularSprite> mPlayerShotList;
	private List<ZigZagShotSprite> mZigZagShotList;
	private List<BunkerSpriteGroup> mBunkerList;
	private Paint myPaint = new Paint(Color.YELLOW);
	private MotionEventTracker mFirebuttonTracker = new MotionEventTracker();
	private MotionEventTracker mLeftButtonTracker = new MotionEventTracker();
	private MotionEventTracker mRightButtonTracker = new MotionEventTracker();
	private AudioManager mAudioManager;
	private SoundPool mSoundPool;
	private SoundIdStore mSounds;
	
	// Drawing resources
	private Paint mGameAreaPaint = new Paint();
	private Paint mGameOverPaint = new Paint();
	private Paint mGameOverTextPaint = new Paint();
	private Paint mGameOverInfoTextPaint = new Paint();	
	private int mBackgroundColor = 0xff355280;
	private int mGameOverOverlayColor = 0xa0111111;

	public GameView(GameActivity gameActivity) {
		super(gameActivity);
		mGameActivity = gameActivity;
		setFocusable(true);
		mScore = 0;
		mLevel = 1;
		mLives = 3;
		mGameOver = false;
		mGameAreaPaint.setColor(Color.BLACK);
		mGameOverPaint.setColor(Color.BLUE);
		
		mBitmapHelper = new BitmapHelper(gameActivity, this);
		
		mAudioManager = (AudioManager) gameActivity.getSystemService(Context.AUDIO_SERVICE);
		Log.i(GAME_VIEW_LOG_TAG, "MaxVolume: " + mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
				+ "  CurrentVolume: " + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		
		mSounds = new SoundIdStore();
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		int soundId = mSoundPool.load(gameActivity, R.raw.laser2, 1);
		mSounds.setPlayerMissileSoundId(soundId);
		soundId = mSoundPool.load(gameActivity, R.raw.invader_sound_1, 1);
		mSounds.addInvaderSound(soundId);
		soundId = mSoundPool.load(gameActivity, R.raw.invader_sound_2, 1);
		mSounds.addInvaderSound(soundId);
		soundId = mSoundPool.load(gameActivity, R.raw.invader_sound_3, 1);
		mSounds.addInvaderSound(soundId);
		soundId = mSoundPool.load(gameActivity, R.raw.invader_sound_4, 1);
		mSounds.addInvaderSound(soundId);
		
		mGameThread = new GameThread(getHolder(), this);
		mGameThread.setName("GameThread");
		getHolder().addCallback(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		mGameThread.feedMotionEvent(event);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(GAME_VIEW_LOG_TAG, "KeyCode: " + keyCode + ", keyCodeToString: " + KeyEvent.keyCodeToString(keyCode));
		if(keyCode == KeyEvent.KEYCODE_BACK){
	        Log.d(GAME_VIEW_LOG_TAG, "back key captured");
	        mGameThread.setGameState(GameThread.STOP);
	    }
		if(keyCode == KeyEvent.KEYCODE_HOME){
	        Log.d(GAME_VIEW_LOG_TAG, "home key captured");
	        mGameThread.setGameState(GameThread.STOP);
	    }
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		initializeGame();
		mGameThread.setGameState(GameThread.RUNNING);
		mGameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	
	// Called on startup to load resources, not for new levels or games in same gameplaysession
	private void initializeGame() {
		mGraphicsHelper = new GraphicsHelper(getWidth(), getHeight());
		mBitmapHelper.initialize(mGraphicsHelper);
		mStatusbarHeight = getStatusBarHeight();
		mGameBorder = mGraphicsHelper.getGameAreaBorderRect();
		mGameBorderPadded = mGraphicsHelper.getGameAreaPaddedBorderRect();
		Log.d(GAME_VIEW_LOG_TAG, "Statusbar: " + mStatusbarHeight);
		
		// Create and place buttons
		mCannon = mBitmapHelper.getCannonSprite();
		mFirebutton = mBitmapHelper.getFirebuttonSprite();
		mLeftButton = mBitmapHelper.getBlueArcadeButtonSprite(Direction.LEFT);
		mRightButton = mBitmapHelper.getBlueArcadeButtonSprite(Direction.RIGHT);
		mStopBtn = mBitmapHelper.getStopButtonSprite();
		mPausePlayBtn = mBitmapHelper.getPlayPauseButtonSprite();
		mSoundCtrl = mBitmapHelper.getSoundControlSprite();
		
		mPlayerShotList = new ArrayList<RegularSprite>();
		mZigZagShotList = new ArrayList<ZigZagShotSprite>();
		mInvaderList = new ArrayList<InvaderSprite>();		
		mBunkerList = new ArrayList<BunkerSpriteGroup>();
		
		mMysterySprite = mBitmapHelper.getMysterySprite();
		mMysteryExplosion100 = mBitmapHelper.getMysteryExplosionSprite100();
		mMysteryExplosion200 = mBitmapHelper.getMysteryExplosionSprite200();
		mMysteryExplosion300 = mBitmapHelper.getMysteryExplosionSprite300();
		
		for (int i=0; i<MAX_PLAYER_SHOTS; i++) {
			mPlayerShotList.add(mBitmapHelper.getLaserShotSprite(0, 0));
		}
		
		for (int i=0; i<MAX_ZIGZAG_SHOTS; i++) {
			mZigZagShotList.add(mBitmapHelper.getZigZagShotSprite(0, 0, 0, 0));
		}
		
		for (int i=1; i<=NUM_BUNKERS; i++) {
			mBunkerSpriteGroup = mBitmapHelper.getBunkerSpriteGroup(i, NUM_BUNKERS);
			mBunkerList.add(mBunkerSpriteGroup);
		}
		
		// Initialize invader sprites and assign start position
		int stepSize = mGraphicsHelper.getStepSize();
		int stepInterval = (int)mGameThread.FPS - mLevel * 2;
		mSpeedUpAtInvaderCount = INITIAL_INVADER_COUNT - (INITIAL_INVADER_COUNT/10);
		int spacingX = mGraphicsHelper.getInvaderCenterSpacingX();
		int spacingY = mGraphicsHelper.getInvaderCenterSpacingY();
		Point initialPos = mGraphicsHelper.getFirstInvaderInitialPosition();
		int x = initialPos.x;
		int y = initialPos.y;
		// Create and place by columns.
		for (int i=0; i<10; i++) {
			// Entering left		
			mInvaderSprite = mBitmapHelper.getInvader1Sprite(x + (i*spacingX), y, -stepSize, stepInterval);
			mInvaderSprite.setScore(INVADER_1_SCORE);
			mInvaderList.add(mInvaderSprite);
			mInvaderSprite = mBitmapHelper.getInvader2Sprite(x + (i*spacingX), y + spacingY, -stepSize, stepInterval);
			mInvaderSprite.setScore(INVADER_2_SCORE);
			mInvaderList.add(mInvaderSprite);
			mInvaderSprite = mBitmapHelper.getInvader2Sprite(x + (i*spacingX), y + 2*spacingY, -stepSize, stepInterval);
			mInvaderSprite.setScore(INVADER_2_SCORE);
			mInvaderList.add(mInvaderSprite);
			mInvaderSprite = mBitmapHelper.getInvader3Sprite(x + (i*spacingX), y + 3*spacingY, -stepSize, stepInterval);
			mInvaderSprite.setScore(INVADER_3_SCORE);
			mInvaderList.add(mInvaderSprite);
			mInvaderSprite = mBitmapHelper.getInvader3Sprite(x + (i*spacingX), y + 4*spacingY, -stepSize, stepInterval);
			mInvaderSprite.setScore(INVADER_3_SCORE);
			mInvaderList.add(mInvaderSprite);
		}
		
		initializeNewGame();
	}
	
	// Called when initializing new game. Initializes new game.
	private void initializeNewGame() {
		mGameOver = false;
		mScore = 0;
		mLevel = 1;
		mLives = 3;
		for (int i=0; i<NUM_BUNKERS; i++) {
			mBunkerArray[i] = mBunkerList.get(i);
			mBunkerArray[i].resetBunker();
		}
		
		initializeNewBoard();
	}

	// Called when initializing new level.
	private void initializeNewBoard() {
		mInvaderCount = INITIAL_INVADER_COUNT;
		mPlayerShotsArray  = new RegularSprite[MAX_PLAYER_SHOTS];
		mZigZagShotsArray = new ZigZagShotSprite[MAX_ZIGZAG_SHOTS];
		mNextMysteryPass = 0;
		mMystery = null;
		mMysteryExplosion = null;
		for (int i=0; i<INITIAL_INVADER_COUNT; i++) {
			mInvaderArray[i] = mInvaderList.get(i);
			mInvaderArray[i].resetInvaderSprite();
		}
	}
	
	private void playSound(int soundId) {
		if (!mSoundCtrl.isPressed()) {
			mSoundPool.play(soundId,1.0f,1.0f,10,0,1f);
		}
	}
	
	private void spawnPlayerShot() {
		Log.d("GAME_VIEW", "Spawning player shot");
		for (int i=0; i<MAX_PLAYER_SHOTS; i++) {
			if (mPlayerShotsArray[i] == null) {
				mPlayerShotsArray[i] = mPlayerShotList.get(i);
				mPlayerShotsArray[i].setX(mCannon.getX());
				mPlayerShotsArray[i].setY(mCannon.getY() - mBitmapHelper.getCannonHalfHeight());
				break;
			}
		}
		playSound(mSounds.getPlayerMissileSoundId());
	}
	
	private void spawnZigZagShot() {
		// Hva bør være reglene her???
		// Foreløpig en spawn per andre sekund.
		long currentTime = System.currentTimeMillis();
		if (currentTime - mLastZigZagMissile > ZIGZAG_MISSILE_INTERVAL) {
			mLastZigZagMissile = currentTime;
			// TODO: Spawn from screen center until refactored
			for (int i=0; i<MAX_ZIGZAG_SHOTS; i++) {
				if (mZigZagShotsArray[i] == null) {
					mZigZagShotsArray[i] = mZigZagShotList.get(i);
					mZigZagShotsArray[i].setX(mGameBorder.left + (mGameBorder.right - mGameBorder.left)/2);
					mZigZagShotsArray[i].setY(mGameBorder.top + (mGameBorder.bottom-mGameBorder.top)/2);
					break;
				}
			}
		}
	}
	
	private boolean spawnMysteryIfDue() {
		long now = System.currentTimeMillis();
		if (mNextMysteryPass == 0) {
			mNextMysteryPass = now + MYSTERY_PASS_INTERVAL/mLevel;
		}
		if (mMystery == null 
				&& now > mNextMysteryPass) {
			mNextMysteryPass = now + MYSTERY_PASS_INTERVAL/mLevel;
			mMystery = mMysterySprite;
			// TODO: Setup mystery
			mBitmapHelper.setupMysterySprite(mMystery);
			return true;
		}
		return false;
	}
	
	public void update() {
		mTurnInvaders = false;
		boolean speedup = false;
		boolean playInvaderSound = false;
		
		// TODO: New algorithm on speedup. List no good.
		if (mInvaderCount <= mSpeedUpAtInvaderCount) {
			mSpeedUpAtInvaderCount = mSpeedUpAtInvaderCount - (INITIAL_INVADER_COUNT/10);
			speedup = true;
		}

		// Update mystery
		if (mMystery == null) {
			spawnMysteryIfDue();
		} else {
			if (!mMystery.update()) {
				mMystery = null;
			} else {
				if (mMystery.getX() > mGraphicsHelper.getGameAreaPaddedBorderRect().right - mMystery.getSpriteWidth()/2) {
					mMystery = null;
				}
			}
		}
		
		// Update mystery explosion
		if (mMysteryExplosion != null) {
			if(!mMysteryExplosion.update()) {
				mMysteryExplosion = null;
			}
		}

		
		// Update invaders
		for (int i=0; i<INITIAL_INVADER_COUNT; i++) {
			if (mInvaderArray[i] != null) {
				if (speedup) {
					mInvaderArray[i].decrementStepInterval();
				}
				if(mInvaderArray[i].update()) {
					// One or more invader sprites signalled to be out of game area.
					// Signal to compensate and move down.
					mTurnInvaders = true;
				}
				if (mInvaderArray[i].getStepCounter() == 0) {
					playInvaderSound = true;
				}
			}
		}
		
		// Update missiles and remove missile sprites above screen.
		for (int i=0; i<MAX_PLAYER_SHOTS; i++) {
			if (mPlayerShotsArray[i] != null) {
				mPlayerShotsArray[i].update();
				if (mPlayerShotsArray[i].getY() < mGameBorder.top) {
					mPlayerShotsArray[i] = null;
				}
			}
		}
		
		// Update cannon
		mCannon.update();
		if (mFirebutton.isPressed()) {
			long now = System.currentTimeMillis();
			if (now >= mLastPlayerMissile + PLAYER_MISSILE_INTERVAL) {
				spawnPlayerShot();
				mLastPlayerMissile = now;
			}
		}
		
		// Update explosion
		if (mInvaderExplosionSprite != null) {
			if(!mInvaderExplosionSprite.update()) {
				mInvaderExplosionSprite = null;
			}
		}
		
		// Spawn zig-zag shots if appropriate...
		spawnZigZagShot();
		// Update and see if they hit anything
		for (int i=0; i<MAX_ZIGZAG_SHOTS; i++) {
			if (mZigZagShotsArray[i] != null) {
				mZigZagShotsArray[i].update();
				if (mZigZagShotsArray[i].getY() > mGameBorder.bottom) {
					mZigZagShotsArray[i] = null;
				}
				if (mZigZagShotsArray[i] != null && mZigZagShotsArray[i].collide(mCannon.getRect())) {
					// TODO:
					// Play explosion sound
					// Freeze input buttons
					// Show cannon wreck sprite
					mLives--;
					if (mLives <= 0) {
						mGameOver = true;
					}
					mZigZagShotsArray[i] = null;
				}
				for (int j=0; j<NUM_BUNKERS; j++) {
					if (mZigZagShotsArray[i] != null && mBunkerArray[j].collide(mZigZagShotsArray[i].getRect())) {
						mZigZagShotsArray[i] = null;
						// TODO: Play sound, maybe a small explosion too
					}
				}
			}
		}

		// Check if invaded.... (And turn invaders if due)
		for (int i=0; i<INITIAL_INVADER_COUNT; i++) {
			if (mInvaderArray[i] != null) {
				if (mTurnInvaders) {
					mInvaderArray[i].moveDownAndTurn();
				}
				if (mInvaderArray[i].collide(mCannon.getRect())) {
					// This is game over...
					mGameOver = true;
				}
				// But invaders also eat bunkers...
				for (int j=0; j<NUM_BUNKERS; j++) {
					mBunkerArray[j].collide(mInvaderArray[i].getRect());  // Bunker will remove parts
				}
			}
		}
		
		// Check collisions missiles-invaders and missiles-bunkers and missile-mystery
		for (int i=0; i<MAX_PLAYER_SHOTS; i++) {
			if (mPlayerShotsArray[i] != null) {
				for (int j=0; j<INITIAL_INVADER_COUNT; j++) {
					if (mInvaderArray[j] != null) {
						if (mPlayerShotsArray[i].collide(mInvaderArray[j].getRect())) {
							mScore += mInvaderArray[j].getScore();
							mInvaderExplosionSprite = mBitmapHelper.getInvaderExplosionSprite(
									mInvaderArray[j].getX(), mInvaderArray[j].getY(), 0, 0, mInvaderArray[j].getStepInterval());
							mPlayerShotsArray[i] = null;
							mInvaderArray[j] = null;
							mInvaderCount--;
							break;
						}
					}
				}
				for (int j=0; j<NUM_BUNKERS; j++) {
					if (mPlayerShotsArray[i] != null && mBunkerArray[j].collide(mPlayerShotsArray[i].getRect())) {
						mPlayerShotsArray[i] = null;
						break;
						// TODO: Play sound, maybe a small explosion too
					}
				}
				if(mPlayerShotsArray[i] != null && mMystery != null) {
					if (mPlayerShotsArray[i].collide(mMystery.getRect())) {
						mPlayerShotsArray[i] = null;
						int random = (int)Math.floor((Math.random()*3));
						switch (random) {
						case 0:
							mMysteryExplosion100.setX(mMystery.getX());
							mMysteryExplosion100.setY(mMystery.getY());
							mMysteryExplosion100.setLifespan(40);
							mMysteryExplosion = mMysteryExplosion100;
							mScore+=100;
							break;
						case 1:
							mMysteryExplosion200.setX(mMystery.getX());
							mMysteryExplosion200.setY(mMystery.getY());
							mMysteryExplosion200.setLifespan(40);
							mMysteryExplosion = mMysteryExplosion200;
							mScore+=200;
							break;
						default:
							mMysteryExplosion300.setX(mMystery.getX());
							mMysteryExplosion300.setY(mMystery.getY());
							mMysteryExplosion300.setLifespan(40);
							mMysteryExplosion = mMysteryExplosion300;
							mScore+=300;
						}
						mMystery = null;
					}
				}
			}
		}

		if (playInvaderSound) {
			playSound(mSounds.getNextInvaderSound());
		}

		if (mInvaderCount == 0) {
			mLevel++;
			//mGameThread.setGameState(GameThread.STOP);
			initializeNewBoard();
		}
		
		if (mGameOver) {
			// TODO: Implement Game Over.
			mGameThread.setGameState(GameThread.PAUSE);
		}
	}
	
	
	public void draw(Canvas canvas) {
//		canvas.drawBitmap(mGameBackground, 0,  0, null);
		canvas.drawColor(mBackgroundColor);
		Rect gameBorders = mGraphicsHelper.getGameAreaPaddedBorderRect();
		
		canvas.drawRect(gameBorders, mGameAreaPaint);
		Bitmap cannonImg = mBitmapHelper.getCannonBitmap();
		
		myPaint.setColor(Color.WHITE);
		myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		myPaint.setStrokeWidth(0);
		int bottomLineY = mGraphicsHelper.placeBottomLine(cannonImg);
		canvas.drawLine(gameBorders.left, bottomLineY, gameBorders.right, bottomLineY, myPaint);
		myPaint.setStrokeWidth(2);
		myPaint.setTextSize(mGraphicsHelper.getCurrentScoreTextSize());
		canvas.drawText("Score: " + mScore, gameBorders.left + 4, gameBorders.top + myPaint.getTextSize() + 2, myPaint);
		String levelText = "Level: " + mLevel;
		canvas.drawText(levelText, 
				gameBorders.right - mGraphicsHelper.getPixelWidthForText(levelText, myPaint),
				gameBorders.top + myPaint.getTextSize() + 2, myPaint);
		// Draw lives
		int livesY = bottomLineY + (gameBorders.bottom-bottomLineY)/2 - cannonImg.getHeight()/2;
		for (int i = 1; i<mLives; i++) {
			canvas.drawBitmap(cannonImg, (float) (gameBorders.left + cannonImg.getWidth()*1.5*i), (float)livesY, null);
		}
		mFirebutton.draw(canvas);
		mLeftButton.draw(canvas);
		mRightButton.draw(canvas);
		mCannon.draw(canvas);
		for (int i=0; i<NUM_BUNKERS; i++) {
			mBunkerArray[i].draw(canvas);
		}
		for (int i=0; i<INITIAL_INVADER_COUNT; i++) {
			if (mInvaderArray[i] != null) {
				mInvaderArray[i].draw(canvas);
			}
		}
		for (int i=0; i<MAX_PLAYER_SHOTS; i++) {
			if (mPlayerShotsArray[i] != null) {
				mPlayerShotsArray[i].draw(canvas);
			}
		}
		for (int i=0; i<MAX_ZIGZAG_SHOTS; i++) {
			if (mZigZagShotsArray[i] != null) {
				mZigZagShotsArray[i].draw(canvas);
			}
		}
		
		if (mInvaderExplosionSprite != null) {
			mInvaderExplosionSprite.draw(canvas);
		}
		if (mMystery != null) {
			mMystery.draw(canvas);
		}
		if (mMysteryExplosion != null) {
			mMysteryExplosion.draw(canvas);
		}
		
		// Draw stop/pause/play buttons
		mStopBtn.draw(canvas);
		mPausePlayBtn.draw(canvas);
		mSoundCtrl.draw(canvas);
		
		if (mGameOver) {
			// TODO:
			// - Halvtransparent overlay
			// - Opaque boks med melding og knapper (OK / CANCEL)
			//   - Lagre high score om aktuelt
			//   - Spill på nytt (Hvis ikke tilbake til meny)
			// Alternativt gå rett til lagre high score, cancel-mulighet der.
			canvas.drawColor(mGameOverOverlayColor);
			RectF gameOverRect = mGraphicsHelper.getGameOverRectF();
			canvas.drawRoundRect(gameOverRect, 20.0f, 20.0f, mGameOverPaint);
			
			canvas.drawText("Exit", 
					mLeftButton.getPos().x - mGraphicsHelper.getPixelWidthForText("Exit", myPaint)/2,
					mLeftButton.getPos().y + mLeftButton.getYDimension()/2 + myPaint.getTextSize()*2, 
					myPaint);
			canvas.drawText("Play", 
					mRightButton.getPos().x - mGraphicsHelper.getPixelWidthForText("Play", myPaint)/2,
					mRightButton.getPos().y + mRightButton.getYDimension()/2 + myPaint.getTextSize()*2, 
					myPaint);
			if (mNewHighScore) {
				canvas.drawText("Highscore", 
						mFirebutton.getPos().x - mGraphicsHelper.getPixelWidthForText("Highscore", myPaint)/2,
						mFirebutton.getPos().y + mFirebutton.getYDimension()/2 + myPaint.getTextSize()*2, 
						myPaint);
			}
			
			int textSize = mGraphicsHelper.getGameOverTextSize();
			mGameOverTextPaint.setColor(Color.YELLOW);
			mGameOverTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mGameOverTextPaint.setStrokeWidth(4);
			mGameOverTextPaint.setTextSize(textSize);
			mGameOverInfoTextPaint.setColor(Color.YELLOW);
			mGameOverInfoTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mGameOverInfoTextPaint.setStrokeWidth(2);
			mGameOverInfoTextPaint.setTextSize(textSize/2);
			int centerX = canvas.getWidth()/2;
			float highScoreY = gameOverRect.top + (gameOverRect.bottom - gameOverRect.top)/3;
			float line1Y = gameOverRect.top + (gameOverRect.bottom - gameOverRect.top)*4/6;
			float line2Y = gameOverRect.top + (gameOverRect.bottom - gameOverRect.top)*5/6;
			
			canvas.drawText("GAME OVER", 
					centerX - mGraphicsHelper.getPixelWidthForText("GAME OVER", mGameOverTextPaint)/2,
					highScoreY, mGameOverTextPaint);
			if (mNewHighScore) {
				String text = "New High Score!!!";
				canvas.drawText(text, 
						centerX - mGraphicsHelper.getPixelWidthForText(text,mGameOverInfoTextPaint)/2,
						line1Y, mGameOverInfoTextPaint);
				text = "Register HighScore, exit or Play?";
				canvas.drawText(text, 
						centerX - mGraphicsHelper.getPixelWidthForText(text,mGameOverInfoTextPaint)/2,
						line2Y, mGameOverInfoTextPaint);
			} else {
				String text = "Exit or Play again?";
				canvas.drawText(text, 
						centerX - mGraphicsHelper.getPixelWidthForText(text,mGameOverInfoTextPaint)/2,
						line1Y, mGameOverInfoTextPaint);
			}
		}
	}
	
	public void pauseHandler() {
		if (mGameOver) {
			if (mScore > mHighScoreMin) {
				mNewHighScore = true;
			}
			if (mLeftButton.isPressed()) {
				// Exit
				mGameThread.setGameState(GameThread.STOP);
				mGameActivity.finish();
			} else if (mRightButton.isPressed()) {
				// Play
				initializeNewGame();
				mStopBtn.setPressed(false);
				mGameThread.setGameState(GameThread.RUNNING);
			} else if (mFirebutton.isPressed() && mNewHighScore) {
				// Go to reg highscore
				Intent regHighScoreIntent = new Intent(mGameActivity, RegisterHighScoreActivity.class);
				regHighScoreIntent.putExtra(HighScoreEntry.SCORE_KEY, mScore);
				mGameActivity.startActivity(regHighScoreIntent);
				mGameActivity.finish();
			}
		}
	}
	
	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	} 
	
	public void processMotionEvent(MotionEvent event) {
		boolean firebuttonPressed = false;
		boolean leftButtonPressed = false;
		boolean rightButtonPressed = false;
		int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_DOWN: {
	        final float x = event.getX();
	        final float y = event.getY() - mStatusbarHeight;
	        if (mFirebutton.isInsideButton((int)x, (int)y)) {
				firebuttonPressed = true;
				mFirebuttonTracker.setTracking(true);
				mFirebuttonTracker.setMotionPointerId(event.getPointerId(0)); // Alltid bare en på ACTION_DOWN
			}
	        if (mLeftButton.isInsideButton((int)x, (int)y)) {
				leftButtonPressed = true;
				mLeftButtonTracker.setTracking(true);
				mLeftButtonTracker.setMotionPointerId(event.getPointerId(0)); // Alltid bare en på ACTION_DOWN
			}
	        if (mRightButton.isInsideButton((int)x, (int)y)) {
				rightButtonPressed = true;
				mRightButtonTracker.setTracking(true);
				mRightButtonTracker.setMotionPointerId(event.getPointerId(0)); // Alltid bare en på ACTION_DOWN
			}

			if (mSoundCtrl.isInsideButton((int)x, (int)y)) {
				if (mSoundCtrl.isPressed()) {
					mSoundCtrl.setPressed(false);
				} else {
					mSoundCtrl.setPressed(true);
				}
			}
			if (mPausePlayBtn.isInsideButton((int)x, (int)y)) {
				if (mPausePlayBtn.isPressed()) {
					mPausePlayBtn.setPressed(false);
					mGameThread.setGameState(GameThread.RUNNING);
				} else {
					mPausePlayBtn.setPressed(true);
					mGameThread.setGameState(GameThread.PAUSE);
				}
			}
			if (mStopBtn.isInsideButton((int)x, (int)y)) {
				if (mStopBtn.isPressed()) {
					mStopBtn.setPressed(false);
				} else {
					mStopBtn.setPressed(true);
					mGameOver = true;
				}
			}
	        break;
	    }
	    case MotionEvent.ACTION_POINTER_DOWN: {
	    	int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	    	final int pointerId = event.getPointerId(pointerIndex);
	    	final float x = event.getX(pointerIndex);
	        final float y = event.getY(pointerIndex) - mStatusbarHeight;
	        if (mFirebutton.isInsideButton((int)x, (int)y) && !mFirebuttonTracker.isTracking()) {
				firebuttonPressed = true;
				mFirebuttonTracker.setTracking(true);
				mFirebuttonTracker.setMotionPointerId(pointerId); 
			} else if (mFirebuttonTracker.isTracking()) {
				firebuttonPressed = true;
			}
	        if (mLeftButton.isInsideButton((int)x, (int)y) && !mFirebuttonTracker.isTracking()) {
				leftButtonPressed = true;
				mLeftButtonTracker.setTracking(true);
				mLeftButtonTracker.setMotionPointerId(pointerId);
			} else if (mLeftButtonTracker.isTracking()) {
				leftButtonPressed = true;
			}
	        if (mRightButton.isInsideButton((int)x, (int)y) && !mFirebuttonTracker.isTracking()) {
	        	rightButtonPressed = true;
				mRightButtonTracker.setTracking(true);
				mRightButtonTracker.setMotionPointerId(pointerId);
			} else if (mLeftButtonTracker.isTracking()) {
				rightButtonPressed = true;
			}

			if (mSoundCtrl.isInsideButton((int)x, (int)y)) {
				if (mSoundCtrl.isPressed()) {
					mSoundCtrl.setPressed(false);
				} else {
					mSoundCtrl.setPressed(true);
				}
			}
			if (mPausePlayBtn.isInsideButton((int)x, (int)y)) {
				if (mPausePlayBtn.isPressed()) {
					mPausePlayBtn.setPressed(false);
					mGameThread.setGameState(GameThread.RUNNING);
				} else {
					mPausePlayBtn.setPressed(true);
					mGameThread.setGameState(GameThread.PAUSE);
				}
			}
			if (mStopBtn.isInsideButton((int)x, (int)y)) {
				if (mStopBtn.isPressed()) {
					mStopBtn.setPressed(false);
				} else {
					mStopBtn.setPressed(true);
				}
			}
	    	break;
	    }
	    case MotionEvent.ACTION_MOVE: {
	    	int pointerCount = event.getPointerCount();
	    	for (int p = 0; p < pointerCount; p++) {
	    		int pointerId = event.getPointerId(p);
	    		int x = (int)event.getX(p);
				int y = (int)event.getY(p) - mStatusbarHeight;
	    		if (pointerId == mFirebuttonTracker.getMotionPointerId()) {
	    			// Check if moved out
	    			if (!mFirebutton.isInsideButton(x,y)) {
	    				firebuttonPressed = false;
	    				mFirebuttonTracker.setTracking(false);
	    				mFirebuttonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
	    			} else {
	    				firebuttonPressed = true;
	    			}
	    		} else if (pointerId == mLeftButtonTracker.getMotionPointerId()) {
	    			// Check if moved out
	    			if (!mLeftButton.isInsideButton(x,y)) {
	    				leftButtonPressed = false;
	    				mLeftButtonTracker.setTracking(false);
	    				mLeftButtonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
	    			} else {
	    				leftButtonPressed = true;
	    			}
	    		} else if (pointerId == mRightButtonTracker.getMotionPointerId()) {
	    			// Check if moved out
	    			if (!mRightButton.isInsideButton(x,y)) {
	    				rightButtonPressed = false;
	    				mRightButtonTracker.setTracking(false);
	    				mRightButtonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
	    			} else {
	    				rightButtonPressed = true;
	    			}
	    		} else  { // Check if reentering
	    			if (!mFirebuttonTracker.isTracking()) {
		    			// Check if stray pointer moved in to firebutton
		    			if (mFirebutton.isInsideButton(x,y)) {
		    				firebuttonPressed = true;
		    				mFirebuttonTracker.setTracking(true);
		    				mFirebuttonTracker.setMotionPointerId(pointerId);
		    			}
		    		}
	    			if (!mLeftButtonTracker.isTracking()) {
		    			// Check if stray pointer moved in to left button
		    			if (mLeftButton.isInsideButton(x,y)) {
		    				leftButtonPressed = true;
		    				mLeftButtonTracker.setTracking(true);
		    				mLeftButtonTracker.setMotionPointerId(pointerId);
		    			}
		    		}
	    			if (!mRightButtonTracker.isTracking()) {
		    			// Check if stray pointer moved in to right button
		    			if (mRightButton.isInsideButton(x,y)) {
		    				rightButtonPressed = true;
		    				mRightButtonTracker.setTracking(true);
		    				mRightButtonTracker.setMotionPointerId(pointerId);
		    			}
		    		}
	    		}
			}
	    	break;
	    }
	    case MotionEvent.ACTION_UP: {
	    	firebuttonPressed = false;
	    	mFirebuttonTracker.setTracking(false);
			mFirebuttonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
			leftButtonPressed = false;
	    	mLeftButtonTracker.setTracking(false);
	    	mLeftButtonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
			rightButtonPressed = false;
	    	mRightButtonTracker.setTracking(false);
	    	mRightButtonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
	    	break;
	    }   
	    case MotionEvent.ACTION_POINTER_UP: {
	    	int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	    	final int pointerId = event.getPointerId(pointerIndex);
	    	if (pointerId == mFirebuttonTracker.getMotionPointerId()) {
	    		firebuttonPressed = false;
	    		mFirebuttonTracker.setTracking(false);
				mFirebuttonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
	    	} else if (pointerId == mLeftButtonTracker.getMotionPointerId()) {
	    		leftButtonPressed = false;
	    		mLeftButtonTracker.setTracking(false);
	    		mLeftButtonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
	    	} else if (pointerId == mRightButtonTracker.getMotionPointerId()) {
	    		rightButtonPressed = false;
	    		mRightButtonTracker.setTracking(false);
	    		mRightButtonTracker.setMotionPointerId(MotionEventTracker.INVALID_POINTER_ID);
	    	}
	    	
	    	if (mFirebuttonTracker.isTracking()) {
	    		firebuttonPressed = true;
	    	}
	    	if (mLeftButtonTracker.isTracking()) {
	    		leftButtonPressed = true;
	    	}
	    	if (mRightButtonTracker.isTracking()) {
	    		rightButtonPressed = true;
	    	}
	    }
	    }


		// firebutton state first
		if (firebuttonPressed) {
			if (!mFirebutton.isPressed()) {
				mFirebutton.setPressed(true);
			}
		} else {
			if(mFirebutton.isPressed()) {
				mFirebutton.setPressed(false);
			}
		}
		
		mCannon.setDeltaX(0);
		if (leftButtonPressed) {
			if (!mLeftButton.isPressed()) {
				mLeftButton.setPressed(true);
			}
			mCannon.setDeltaX(-mGraphicsHelper.getCannonSpeed());
		} else {
			if(mLeftButton.isPressed()) {
				mLeftButton.setPressed(false);
			}
		}
		if (rightButtonPressed) {
			if (!mRightButton.isPressed()) {
				mRightButton.setPressed(true);
			}
			mCannon.setDeltaX(mGraphicsHelper.getCannonSpeed());
		} else {
			if(mRightButton.isPressed()) {
				mRightButton.setPressed(false);
			}
		}
	}
	
	public class MotionEventTracker {
		public static final int INVALID_POINTER_ID = -1;
		private boolean tracking;
		private int motionPointerId;
		
		public MotionEventTracker() {
			tracking = false;
			motionPointerId = INVALID_POINTER_ID;
		}
		
		public boolean isTracking() {
			return tracking;
		}
		public void setTracking(boolean tracking) {
			this.tracking = tracking;
		}
		public int getMotionPointerId() {
			return motionPointerId;
		}
		public void setMotionPointerId(int motionPointerId) {
			this.motionPointerId = motionPointerId;
		}
	}
	
	public enum Direction {
		LEFT, RIGHT;
	}

}
