package no.jan.android.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class InvaderSprite {
	private Bitmap[] bitmaps = new Bitmap[2];
	private int x;
	private int y;
	private int initialX;
	private int initialY;
	private int score = 0;
	private int deltaX;
	private int deltaY;
	int currentBitmap;
	private int stepSize;
	private int initialStepSize;
	private int stepInterval;
	private int inititalStepInterval;
	private int stepCounter;
	private int maxLeftPos;
	private int maxRightPos;
	private boolean animationBoundHorizontal;
	private Rect spriteRect = new Rect();
	
	// Use for Invader sprite Splash screen
	public InvaderSprite(Bitmap bitmap1, Bitmap bitmap2, int x, int y, int stepSize, int stepInterval) {
		bitmaps[0] = bitmap1;
		bitmaps[1] = bitmap2;
		this.x = x;
		this.y = y;
		this.initialX = x;
		this.initialY = y;
		this.stepSize = stepSize;
		this.initialStepSize = stepSize;
		this.stepInterval = stepInterval;
		this.inititalStepInterval = stepInterval;
		currentBitmap = 0;
		stepCounter = 0;
		animationBoundHorizontal = false;
	}
	
	// Use for invader sprite in game
	public InvaderSprite(Bitmap bitmap1, Bitmap bitmap2, int x, int y, int stepSize, int stepInterval, Rect gameArea) {
		bitmaps[0] = bitmap1;
		bitmaps[1] = bitmap2;
		this.x = x;
		this.y = y;
		this.initialX = x;
		this.initialY = y;
		this.stepSize = stepSize;
		this.initialStepSize = stepSize;
		this.stepInterval = stepInterval;
		this.inititalStepInterval = stepInterval;
		maxRightPos = gameArea.right;
		maxLeftPos = gameArea.left;
		currentBitmap = 0;
		stepCounter = 0;
		animationBoundHorizontal = true;
//		Log.d("INVADER_SPRITE", "Created sprite at pos x=" + x + ",y=" + y);
	}
	
	public boolean collide(Rect otherRect) {
		return getRect().intersect(otherRect);
	}
	
	public boolean update() {
		stepCounter++;
		if (stepCounter > stepInterval && !animationBoundHorizontal) {
			stepCounter = 0;
			this.x += this.stepSize;	
			if (currentBitmap == 0) {
				currentBitmap = 1;
			} else {
				currentBitmap = 0;
			}
			return true;
		} else if (stepCounter > stepInterval && animationBoundHorizontal) {
			boolean trueIfOutOfBounds = false;
			stepCounter = 0;
			x += stepSize;
			if (x < maxLeftPos || x > maxRightPos) { // If outside bounds 
				trueIfOutOfBounds = true; // Signal to turn all invaders
			} 
			if (currentBitmap == 0) {
				currentBitmap = 1;
			} else {
				currentBitmap = 0;
			}
			return trueIfOutOfBounds;
		}
		return false;
	}
	
	public void moveDownAndTurn() {
		x -= stepSize;            // step back
		stepSize = -stepSize;     // turn direcion of movement
		y += Math.abs(stepSize);  // step down instead
	}
	
	public void draw(Canvas canvas) {
		int offsetX = bitmaps[currentBitmap].getWidth()/2;
		int offsetY = bitmaps[currentBitmap].getHeight()/2;
		canvas.drawBitmap(bitmaps[currentBitmap], x - offsetX , y - offsetY, null);
	}
	
	public void resetInvaderSprite() {
		x = initialX;
		y = initialY;
		stepInterval = inititalStepInterval;
		stepSize = initialStepSize;
		stepCounter = 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getInitialX() {
		return initialX;
	}

	public void setInitialX(int initialX) {
		this.initialX = initialX;
	}

	public int getInitialY() {
		return initialY;
	}

	public void setInitialY(int initialY) {
		this.initialY = initialY;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(int deltaY) {
		this.deltaY = deltaY;
	}

	public int getStepSize() {
		return stepSize;
	}

	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	public int getStepInterval() {
		return stepInterval;
	}

	public void setStepInterval(int stepInterval) {
		this.stepInterval = stepInterval;
	}
	
	public int getInititalStepInterval() {
		return inititalStepInterval;
	}

	public void setInititalStepInterval(int inititalStepInterval) {
		this.inititalStepInterval = inititalStepInterval;
	}

	public void decrementStepInterval() {
		this.stepInterval--;
	}

	public int getStepCounter() {
		return stepCounter;
	}

	public void setStepCounter(int stepCounter) {
		this.stepCounter = stepCounter;
	}
	
	public Rect getRect() {
		spriteRect.set(x-bitmaps[0].getWidth()/2,
				y-bitmaps[0].getHeight()/2, 
				x+bitmaps[0].getWidth()/2, 
				y+bitmaps[0].getHeight()/2);
		return spriteRect;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


}
