package no.jan.android.spaceinvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Maintains a virtual 6x4 array of images/sprites to create a degradable bunker. 
 * The sprites themselves controls the collision detection and visibility. BunkerSpriteGroup just delegates.
 * @author jan.arne.sandnes
 *
 */
public class BunkerSpriteGroup {
	private Bitmap mBunkerUpperLeft1Img;
	private Bitmap mBunkerUpperLeft2Img;
	private Bitmap mBunkerUpperRight1Img;
	private Bitmap mBunkerUpperRight2Img;
	private Bitmap mBunkerLowerLeftImg;
	private Bitmap mBunkerLowerRightImg;
	private Bitmap mBunkerFillImg;
	private int mBunkerXPos;
	private int mBunkerYPos;
	private int mBitmapSize;
	private int mBunkerHalfWidth;
	private int mBunkerHalfHeight;
	private final int SPRITE_GRID_SIZE = 24;
	private final int SPRITE_GROUP_SIZE = 22;
	private List<RegularSprite> mSpriteList;
	private RegularSprite[] mActiveSpriteArray = new RegularSprite[SPRITE_GROUP_SIZE];
	
	public BunkerSpriteGroup(Bitmap upperLeft1Img, Bitmap upperLeft2Img, Bitmap upperRight1Img,
			Bitmap upperRight2Img, Bitmap lowerLeftImg, Bitmap lowerRightImg, Bitmap fillImg,
			Point pos) {
		this.mBunkerUpperLeft1Img = upperLeft1Img;
		this.mBunkerUpperLeft2Img = upperLeft2Img;
		this.mBunkerUpperRight1Img = upperRight1Img;
		this.mBunkerUpperRight2Img = upperRight2Img;
		this.mBunkerLowerLeftImg = lowerLeftImg;
		this.mBunkerLowerRightImg = lowerRightImg;
		this.mBunkerFillImg = fillImg;
		this.mBunkerXPos = pos.x;
		this.mBunkerYPos = pos.y;
		//
		// Check a couple of images that they are square and same size. Will assume this size anyway..
		if (!(mBunkerUpperLeft1Img.getHeight() == mBunkerFillImg.getHeight() 
				&& mBunkerUpperLeft1Img.getWidth() == mBunkerFillImg.getWidth()
				&& mBunkerUpperLeft1Img.getHeight() == mBunkerFillImg.getWidth())) {
			throw new RuntimeException("Similar size bitmaps for the BunkerSpriteGroup please...");
		}
		mBitmapSize = mBunkerFillImg.getWidth();
		mBunkerHalfWidth = mBitmapSize * 3;
		mBunkerHalfHeight = mBitmapSize * 2;
		initSpriteGroup();
		resetBunker();
	}
	
	private void initSpriteGroup() {
		int leftX = mBunkerXPos - mBunkerHalfWidth;
		int upperY = mBunkerYPos - mBunkerHalfHeight;
		mSpriteList = new ArrayList<RegularSprite>(SPRITE_GROUP_SIZE);
		for (int i=0; i < SPRITE_GRID_SIZE; i++) {
			Point pos = new Point(
					leftX + (i % 6) * mBitmapSize + mBitmapSize/2,
					upperY + (i / 6) * mBitmapSize + mBitmapSize/2);
			switch (i) {
			case 0:
				mSpriteList.add(new RegularSprite(mBunkerUpperLeft1Img, pos));
				break;
			case 1:
				mSpriteList.add(new RegularSprite(mBunkerUpperLeft2Img, pos));
				break;
			case 4:
				mSpriteList.add(new RegularSprite(mBunkerUpperRight2Img, pos));
				break;
			case 5:
				mSpriteList.add(new RegularSprite(mBunkerUpperRight1Img, pos));
				break;
			case 19:
				mSpriteList.add(new RegularSprite(mBunkerLowerLeftImg, pos));
				break;
			case 22:
				mSpriteList.add(new RegularSprite(mBunkerLowerRightImg, pos));
				break;
			case 20:
			case 21:
				// No sprites at position 20 and 21
				break;
			default:
				mSpriteList.add(new RegularSprite(mBunkerFillImg, pos));
				break;
			}
		}
	}
	
	public void resetBunker() {
		for (int i=0; i<SPRITE_GROUP_SIZE; i++) {
			mActiveSpriteArray[i] = mSpriteList.get(i);
		}
	}
	
	public boolean update() {
		// No need to update static sprites
		return true;
	}
	
	public boolean draw(Canvas canvas) {
		for (int i=0; i<SPRITE_GROUP_SIZE; i++) {
			if (mActiveSpriteArray[i] != null) mActiveSpriteArray[i].draw(canvas);
		}
		return true;
	}
	
	public boolean collide(Rect otherRect) {
		boolean collisionDetected = false;
		for (int i=0; i<SPRITE_GROUP_SIZE; i++) {
			if (mActiveSpriteArray[i] != null && mActiveSpriteArray[i].collide(otherRect)) {
				collisionDetected = true;
				mActiveSpriteArray[i] = null;
			}
		}
		return collisionDetected;
	}
	
}
