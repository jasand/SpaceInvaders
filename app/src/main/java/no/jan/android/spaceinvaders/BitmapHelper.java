package no.jan.android.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class BitmapHelper {
	private Context context;
	private boolean bitmapHelperInitialized = false;
	private Bitmap mInvader1aImg;
	private Bitmap mInvader1bImg;
	private Bitmap mInvader2aImg;
	private Bitmap mInvader2bImg;
	private Bitmap mInvader3aImg;
	private Bitmap mInvader3bImg;
	private Bitmap mInvaderExplosionImg;
	private Bitmap mCannonImg;
	private Bitmap mCannonWreckImg;
	private Bitmap mBunkerUpperLeft1Img;
	private Bitmap mBunkerUpperLeft2Img;
	private Bitmap mBunkerUpperRight1Img;
	private Bitmap mBunkerUpperRight2Img;
	private Bitmap mBunkerLowerLeftImg;
	private Bitmap mBunkerLowerRightImg;
	private Bitmap mBunkerFillImg;
	private Bitmap mLaserShotImg;
	private Bitmap mZigZagShot1Img;
	private Bitmap mZigZagShot2Img;
	private Bitmap mArcadeRedBtnImg;
	private Bitmap mArcadeBlueBtnImg;
	private Bitmap mFirebtnNeutralImg;
	private Bitmap mFirebtnPressedImg;
	private Bitmap mDirectionBtnNeutralImg;
	private Bitmap mDirectionBtnPressedImg;
	private Bitmap mIconSetImg;
	private Bitmap mStopBtnImg;
	private Bitmap mStopBtnPressedImg;
	private Bitmap mPauseBtnImg;
	private Bitmap mPlayBtnImg;
	private Bitmap mSoundOffImg;
	private Bitmap mSoundOnImg;
	private Bitmap mMysteryImg;
	private Bitmap mMysteryExp100Img;
	private Bitmap mMysteryExp200Img;
	private Bitmap mMysteryExp300Img;
	private Bitmap mGameBackgroundImg;
	private GraphicsHelper mGraphicsHelper;
	private GameView mGameView;
	
	public BitmapHelper(Context context, GameView gameView) {
		this.context = context;
		mGameView = gameView;
		mInvader1aImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader1a);
		mInvader1bImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader1b);
		mInvader2aImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader2a);
		mInvader2bImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader2b);
		mInvader3aImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader3a);
		mInvader3bImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader3b);
		mMysteryImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.ufo_red);
		mMysteryExp100Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.mystery_explosion_100);
		mMysteryExp200Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.mystery_explosion_200);
		mMysteryExp300Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.mystery_explosion_300);
		mInvaderExplosionImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_explosion);
		mCannonImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cannon);
		mCannonWreckImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cannon_wreck);
		mBunkerUpperLeft1Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.bunker_upper_left_1);
		mBunkerUpperLeft2Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.bunker_upper_left_2);
		mBunkerUpperRight1Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.bunker_upper_right_1);
		mBunkerUpperRight2Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.bunker_upper_right_2);
		mBunkerLowerLeftImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bunker_lower_left);
		mBunkerLowerRightImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bunker_lower_right);
		mBunkerFillImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bunker_fill);
		mLaserShotImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.lasershot);
		mZigZagShot1Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.zigzag1);
		mZigZagShot2Img = BitmapFactory.decodeResource(context.getResources(), R.drawable.zigzag2);
		mArcadeRedBtnImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.arcade_red);
		mFirebtnNeutralImg = Bitmap.createBitmap(
				mArcadeRedBtnImg, 0, 0, mArcadeRedBtnImg.getWidth()/2, mArcadeRedBtnImg.getHeight());
		mFirebtnPressedImg = Bitmap.createBitmap(
				mArcadeRedBtnImg, mArcadeRedBtnImg.getWidth()/2, 0, mArcadeRedBtnImg.getWidth()/2, mArcadeRedBtnImg.getHeight());
		mArcadeBlueBtnImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.arcade_blue);
		mDirectionBtnNeutralImg = Bitmap.createBitmap(
				mArcadeBlueBtnImg, 0, 0, mArcadeBlueBtnImg.getWidth()/2, mArcadeBlueBtnImg.getHeight());
		mDirectionBtnPressedImg = Bitmap.createBitmap(
				mArcadeBlueBtnImg, mArcadeBlueBtnImg.getWidth()/2, 0, mArcadeBlueBtnImg.getWidth()/2, mArcadeBlueBtnImg.getHeight());
		
		mIconSetImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_set_blue);
		mStopBtnImg = Bitmap.createBitmap(mIconSetImg, 256, 64, 64, 64);
		mStopBtnPressedImg = Bitmap.createBitmap(mIconSetImg, 192, 128, 64, 64);
		mPauseBtnImg = Bitmap.createBitmap(mIconSetImg, 192, 64, 64, 64);
		mPlayBtnImg = Bitmap.createBitmap(mIconSetImg, 128, 64, 64, 64);
		mSoundOffImg = Bitmap.createBitmap(mIconSetImg, 384, 128, 64, 64);
		mSoundOnImg = Bitmap.createBitmap(mIconSetImg, 320, 128, 64, 64);
		mGameBackgroundImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_background);
		// Release for GC, as these two not needed anymore
		mArcadeRedBtnImg = null;
		mArcadeBlueBtnImg = null;
	}
	
	public void initialize(GraphicsHelper graphicsHelper) {
		mGraphicsHelper = graphicsHelper;	
		mInvader1aImg = mGraphicsHelper.scaleBitmap(mInvader1aImg);
		mInvader1bImg = mGraphicsHelper.scaleBitmap(mInvader1bImg);
		mInvader2aImg = mGraphicsHelper.scaleBitmap(mInvader2aImg);
		mInvader2bImg = mGraphicsHelper.scaleBitmap(mInvader2bImg);
		mInvader3aImg = mGraphicsHelper.scaleBitmap(mInvader3aImg);
		mInvader3bImg = mGraphicsHelper.scaleBitmap(mInvader3bImg);
		mMysteryImg = mGraphicsHelper.scaleBitmap(mMysteryImg);
		mMysteryExp100Img = mGraphicsHelper.scaleBitmap(mMysteryExp100Img);
		mMysteryExp200Img = mGraphicsHelper.scaleBitmap(mMysteryExp200Img);
		mMysteryExp300Img = mGraphicsHelper.scaleBitmap(mMysteryExp300Img);
		mZigZagShot1Img = mGraphicsHelper.scaleBitmap(mZigZagShot1Img);
		mZigZagShot2Img = mGraphicsHelper.scaleBitmap(mZigZagShot2Img);
		mInvaderExplosionImg = mGraphicsHelper.scaleBitmap(mInvaderExplosionImg);
		mCannonImg = mGraphicsHelper.scaleBitmap(mCannonImg);
		mBunkerUpperLeft1Img = mGraphicsHelper.scaleBitmap(mBunkerUpperLeft1Img);
		mBunkerUpperLeft2Img = mGraphicsHelper.scaleBitmap(mBunkerUpperLeft2Img);
		mBunkerUpperRight1Img = mGraphicsHelper.scaleBitmap(mBunkerUpperRight1Img);
		mBunkerUpperRight2Img = mGraphicsHelper.scaleBitmap(mBunkerUpperRight2Img);
		mBunkerLowerLeftImg = mGraphicsHelper.scaleBitmap(mBunkerLowerLeftImg);
		mBunkerLowerRightImg = mGraphicsHelper.scaleBitmap(mBunkerLowerRightImg);
		mBunkerFillImg = mGraphicsHelper.scaleBitmap(mBunkerFillImg);
		mFirebtnNeutralImg = mGraphicsHelper.scaleBitmap(mFirebtnNeutralImg);
		mFirebtnPressedImg = mGraphicsHelper.scaleBitmap(mFirebtnPressedImg);
		mDirectionBtnNeutralImg = mGraphicsHelper.scaleBitmap(mDirectionBtnNeutralImg);
		mDirectionBtnPressedImg = mGraphicsHelper.scaleBitmap(mDirectionBtnPressedImg);
		mStopBtnImg = mGraphicsHelper.scaleControlsBitmap(mStopBtnImg);
		mStopBtnPressedImg = mGraphicsHelper.scaleControlsBitmap(mStopBtnPressedImg);
		mPauseBtnImg = mGraphicsHelper.scaleControlsBitmap(mPauseBtnImg);
		mPlayBtnImg = mGraphicsHelper.scaleControlsBitmap(mPlayBtnImg);
		mSoundOffImg = mGraphicsHelper.scaleControlsBitmap(mSoundOffImg);
		mSoundOnImg = mGraphicsHelper.scaleControlsBitmap(mSoundOnImg);
		mLaserShotImg = mGraphicsHelper.scaleBitmap(mLaserShotImg);
		mGameBackgroundImg = Bitmap.createScaledBitmap(mGameBackgroundImg, mGameView.getWidth(), mGameView.getHeight(), false);
	}
	
	public RegularSprite getCannonSprite() {
		return new RegularSprite(mCannonImg,mGraphicsHelper.placeCannon(mCannonImg),
				0, 0, mGraphicsHelper.getCannonLeftBound(mCannonImg),
				mGraphicsHelper.getCannonRightBound(mCannonImg));
	}
	
	public void setupMysterySprite(RegularSprite mystery) {
		mystery.setX(mGraphicsHelper.getGameAreaPaddedBorderRect().left + mMysteryImg.getWidth()/2);
		mystery.setY(mGraphicsHelper.getMysteryY());
		mystery.setDeltaX(2);
		mystery.setDeltaY(0);
	}
	
	public RegularSprite getMysterySprite() {
		return new RegularSprite(mMysteryImg, new Point(0,0), 0, 0);
	}
	
	public RegularSprite getMysteryExplosionSprite100() {
		return new RegularSprite(mMysteryExp100Img, new Point(0,0), 0, 0, 0);
	}
	
	public RegularSprite getMysteryExplosionSprite200() {
		return new RegularSprite(mMysteryExp200Img, new Point(0,0), 0, 0, 0);
	}
	
	public RegularSprite getMysteryExplosionSprite300() {
		return new RegularSprite(mMysteryExp300Img, new Point(0,0), 0, 0, 0);
	}
	
	public ButtonSprite getFirebuttonSprite() {
		return new ButtonSprite(
				mFirebtnNeutralImg, mFirebtnPressedImg, mGraphicsHelper.placeFirebutton(mFirebtnNeutralImg));
	}
	
	public ButtonSprite getBlueArcadeButtonSprite(GameView.Direction direction) {
		return new ButtonSprite(
				mDirectionBtnNeutralImg, mDirectionBtnPressedImg, mGraphicsHelper.placeDirectionButton(mDirectionBtnNeutralImg, direction));
	}
	
	public ButtonSprite getStopButtonSprite() {
		return new ButtonSprite(mStopBtnImg, mStopBtnPressedImg, mGraphicsHelper.placeStopButton(mStopBtnImg));
	}
	
	public ButtonSprite getPlayPauseButtonSprite() {
		return new ButtonSprite(mPauseBtnImg, mPlayBtnImg, mGraphicsHelper.placePausePlayButton(mPauseBtnImg));
	}
	
	public ButtonSprite getSoundControlSprite() {
		return new ButtonSprite(mSoundOnImg, mSoundOffImg, mGraphicsHelper.placeSoundControl(mSoundOffImg));
	}
		
	public BunkerSpriteGroup getBunkerSpriteGroup(int bunkerNbr, int numBunkers) {
		//return new RegularSprite(mBunkerImg, mGraphicsHelper.placeBunker(mBunkerImg, pos), 0, 0);
		return new BunkerSpriteGroup(
				mBunkerUpperLeft1Img,
				mBunkerUpperLeft2Img,
				mBunkerUpperRight1Img,
				mBunkerUpperRight2Img,
				mBunkerLowerLeftImg,
				mBunkerLowerRightImg,
				mBunkerFillImg,
				mGraphicsHelper.placeBunkerSpriteGroup(mBunkerFillImg, bunkerNbr, numBunkers)
				);
	}
	
	public RegularSprite getLaserShotSprite(int xPos, int yPos) {
		return new RegularSprite(mLaserShotImg, 
				new Point(xPos, yPos - mCannonImg.getHeight()/2), 0, -4);
	}
	
	public int getCannonHalfHeight() {
		return mCannonImg.getHeight()/2;
	}
	
	public InvaderSprite getInvader1Sprite(int x, int y, int stepSize, int stepInterval) {
		return new InvaderSprite(
				mInvader1aImg, mInvader1bImg, x, y, stepSize, stepInterval, mGraphicsHelper.getGameAreaBorderRect());
	}
	
	public InvaderSprite getInvader2Sprite(int x, int y, int stepSize, int stepInterval) {
		return new InvaderSprite(
				mInvader2aImg, mInvader2bImg, x, y, stepSize, stepInterval, mGraphicsHelper.getGameAreaBorderRect());
	}
	
	public InvaderSprite getInvader3Sprite(int x, int y, int stepSize, int stepInterval) {
		return new InvaderSprite(
				mInvader3aImg, mInvader3bImg, x, y, stepSize, stepInterval, mGraphicsHelper.getGameAreaBorderRect());
	}
	
	public RegularSprite getInvaderExplosionSprite(int x, int y, int deltaX, int deltaY, int lifetimeInTicks) {
		return new RegularSprite(mInvaderExplosionImg, new Point(x,y), deltaX, deltaY, lifetimeInTicks);
	}
	
	public ZigZagShotSprite getZigZagShotSprite(int x, int y, int deltaX, int deltaY) {
		return new ZigZagShotSprite(mZigZagShot1Img, mZigZagShot2Img, x, y, 9, 7);
	}
	
	public RegularSprite getCannonWreckSprite(int x, int y) {
		return new RegularSprite(mCannonWreckImg, new Point(x,y), 0, 0, 100);
	}
	
	public Bitmap getCannonBitmap() {
		return mCannonImg;
	}

}
