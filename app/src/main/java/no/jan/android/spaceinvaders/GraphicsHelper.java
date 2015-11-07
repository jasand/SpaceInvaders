package no.jan.android.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class GraphicsHelper {
	private final String TAG = "GRAPHICS_HELPER";
	private static int DEFAULT_WIDEST_INVADER = 48;
	private static int DEFAULT_HEIGHT_MYSTERY = 48;
	private static int DEFAULT_INVADER_HEIGHT = 32;
	private static int DEFAULT_CANVAS_TEXT_SIZE = 40;
	private static int DEFAULT_GAME_OVER_TEXT_SIZE = 80;
	
	// Widest invader is 48 pixels. Need to accommodate for 10 invaders
	// on a row. Spacing is half of widest invader.
	// Invaders should be able to move 3 x (widest invader + spacing)
	// Border outside game area should be 2 x 20 pixels (2 x 10 for smallest screens)
	// Also assume square game area with additional area for controls
	// Min game area + border for portrait width:
	// For 72px:  72 * 10 + 36 * 9 + (72 + 36) * 3 + 2 * 20 = 720 + 324 + 324 + 20 = 1388px
	// For 48px:  48 * 10 + 24 * 9 + (48 + 24) * 3 + 2 * 20 = 480 + 216 + 216 + 40 = 952px
	// For 33px:  33 * 10 + 16 * 9 + (33 + 16) * 3 + 2 * 20 = 330 + 144 + 147 + 40 = 661px
	// For 22px:  22 * 10 + 11 * 9 + (22 + 11) * 3 + 2 * 10 = 220 + 99 + 99 + 20   = 438px
	// For 16px:  16 * 10 + 8 * 9 + (16 + 8) * 3 + 2 * 10 = 160 + 72 + 72 + 20     = 324px
	//
	// => For screens between 661px and 952px scale factor 0.75 is used
	private static int MIN_WIDTH_3_2_SCALE = 1388;
	private static int MIN_WIDTH_1_1_SCALE = 952;
	private static int MIN_WIDTH_3_4_SCALE = 661;
	private static int MIN_WIDTH_1_2_SCALE = 438;
	private static int MIN_WIDTH_1_3_SCALE = 324;
	private static int GAME_AREA_3_2_SCALE = 1348;
	private static int GAME_AREA_1_1_SCALE = 912;
	private static int GAME_AREA_3_4_SCALE = 621;
	private static int GAME_AREA_1_2_SCALE = 418;
	private static int GAME_AREA_1_3_SCALE = 304;
	private static int MOVE_LEN_3_2_SCALE = 324;
	private static int MOVE_LEN_1_1_SCALE = 216;
	private static int MOVE_LEN_3_4_SCALE = 147;
	private static int MOVE_LEN_1_2_SCALE = 99;
	private static int MOVE_LEN_1_3_SCALE = 72;
	
	private int width;
	private int height;
	private double scale;
	private int gameAreaSizeX;
	private int gameAreaSizeY;
	private int invaderWidth;
	private int invaderHeight;
	private int invaderCenterSpacingX;
	private int invaderCenterSpacingY;
	private int invaderStartOffset;
	private int gameAreaPadding;
	
	public GraphicsHelper(int width, int height) {
		// Calculate from portrait
		if (width > height) {
			this.width = height;
			this.height = width;
		} else {
			this.width = width;
			this.height = height;
		}
		calculateValues();
		invaderWidth = (int)(DEFAULT_WIDEST_INVADER * scale);
		invaderHeight = (int)(DEFAULT_INVADER_HEIGHT * scale);
		invaderCenterSpacingX = (int)(invaderWidth * 1.5);
		invaderCenterSpacingY = (int)(invaderHeight * 1.5);
		gameAreaPadding = (width - gameAreaSizeX)/2;
		gameAreaSizeY = gameAreaSizeX;
	}
	
	public int getStepSize() {
		return (int) (DEFAULT_WIDEST_INVADER * scale / 3);
	}

	public double getScale() {
		return scale;
	}

	public int getInvaderWidth() {
		return invaderWidth;
	}

	public int getInvaderHeight() {
		return invaderHeight;
	}

	public int getInvaderCenterSpacingX() {
		return invaderCenterSpacingX;
	}

	public int getInvaderCenterSpacingY() {
		return invaderCenterSpacingY;
	}

	public int getInvaderMoveDistance() {
		return invaderCenterSpacingX * 3;
	}
	
	public int getMysteryY() {
		Point initialInvaderPos = getFirstInvaderInitialPosition();
		return initialInvaderPos.y - (int)(DEFAULT_HEIGHT_MYSTERY * scale); 
	}
	
	public int getCannonSpeed() {
		Log.i(TAG, "Cannon speed: " + (int)(3*scale));
		Log.i(TAG, "Scale: " + scale);
		return (int)(3*scale);
	}

	public Point getFirstInvaderInitialPosition() {
		int x = gameAreaPadding + invaderStartOffset - getStepSize();
		int y = gameAreaPadding + invaderHeight * 4;
		Log.d("GRAPHICS_HELPER", "StartingPoint: x=" + x + ",y=" + y);
		return new Point(x, y);
	}
	
	public Rect getGameAreaBorderRect() {
		int left = gameAreaPadding;
		int right = width - 1 - gameAreaPadding;
		int top = gameAreaPadding;
		int bottom = gameAreaPadding + gameAreaSizeY;
		return new Rect(left, top, right, bottom);
	}
	
	public Rect getGameAreaPaddedBorderRect() {
		int outsideBorder = gameAreaPadding - invaderWidth*3/4;
		int left = outsideBorder;
		int right = width - 1 - outsideBorder;
		int top = outsideBorder;
		int bottom = outsideBorder + gameAreaSizeY;
		return new Rect(left, top, right, bottom);
	}
	
	public Point placeFirebutton(Bitmap firebtn) {
		Rect gameArea = getGameAreaPaddedBorderRect();
		int x = width -1 - gameAreaPadding/2 - firebtn.getWidth()/2;
		int y = gameArea.bottom + gameAreaPadding + firebtn.getHeight()/2;
		Point pos = new Point(x,y);
		Log.d("GRAPHICS_HELPER", "Firebutton pos: " + pos);
		return pos;
	}
	
	public Point placeDirectionButton(Bitmap button, GameView.Direction direction) {
		Rect gameArea = getGameAreaPaddedBorderRect();
		int x;
		if (direction == GameView.Direction.LEFT) {
			x = gameAreaPadding/2 + button.getWidth()/2 - button.getWidth()/16;
		} else {
			x = gameAreaPadding/2 + button.getWidth()*3/2 - button.getWidth()*3/16;;
		}
		int y = gameArea.bottom + gameAreaPadding + button.getHeight()/2;
		Point pos = new Point(x,y);
		Log.d("GRAPHICS_HELPER", "Arcade blue pos: " + pos);
		return pos;
	}

	public Point placeStopButton(Bitmap buttonImg) {
		int x = width/2 - buttonImg.getWidth()*4/3;
		int y = height - buttonImg.getHeight()*2/3;
		return new Point(x,y);
	}
	
	public Point placePausePlayButton(Bitmap buttonImg) {
		int x = width/2 + buttonImg.getWidth()*4/3;
		int y = height - buttonImg.getHeight()*2/3;
		return new Point(x,y);
	}
	
	public Point placeSoundControl(Bitmap img) {
		int x = width/2;
		int y = height - img.getHeight()*2/3;
		return new Point(x,y);
	}
	
	public Point placeCannon(Bitmap cannon) {
		Rect gameArea = getGameAreaPaddedBorderRect();
		int x = width/2;
		int y = gameArea.bottom - cannon.getHeight()*2;
		return new Point(x,y);
	}
	
	public int placeBottomLine(Bitmap cannon) {
		Rect gameArea = getGameAreaPaddedBorderRect();
		return gameArea.bottom - cannon.getHeight()*4/3;
	}
	
	public int getCannonLeftBound(Bitmap cannon) {
		return getGameAreaPaddedBorderRect().left + cannon.getWidth();
	}
	
	public int getCannonRightBound(Bitmap cannon) {
		return getGameAreaPaddedBorderRect().right - cannon.getWidth();
	}

	public Point placeBunkerSpriteGroup(Bitmap bunkerElement, int bunkerNum, int numBunkers) {
		Rect gameArea = getGameAreaPaddedBorderRect();
		int bunkerHeight = bunkerElement.getHeight() * 4;
		int x = gameArea.left + (gameArea.right-gameArea.left)*bunkerNum/(numBunkers+1);
		int y = gameArea.bottom - bunkerHeight*2;
		return new Point(x,y);
	}
	
	public Point placeSkipSplashButton(Bitmap button) {
		int x = width/2;
		int y = height - button.getHeight();
		return new Point(x,y);
	}
	
	public Bitmap scaleBitmap(Bitmap bitmap) {
		if (scale == 1.0) {
			return bitmap;
		} else {
			return Bitmap.createScaledBitmap(bitmap,
					(int)(bitmap.getWidth() * scale), (int)(bitmap.getHeight() * scale), false);
		}
	}
	
	public Bitmap scaleControlsBitmap(Bitmap bitmap) {
		return Bitmap.createScaledBitmap(bitmap,
				(int)(bitmap.getWidth() * scale * 2.5), (int)(bitmap.getHeight() * scale * 2.5), false);
	}
	
	public int getPixelWidthForText(String text, Paint paint) {
		int strlen = text.length();
		float[] widths = new float[strlen];
		paint.getTextWidths(text, widths);
		float sum = 0.0f;
		for (int i=0; i<strlen; i++) {
			sum += widths[i];
		}
		return (int)sum;
	}
	
	public RectF getGameOverRectF() {
		Rect gameArea = getGameAreaPaddedBorderRect();
		RectF retVal = new RectF();
		retVal.left = (float) gameArea.left;
		retVal.top = (float) gameArea.top + gameAreaPadding/2;
		retVal.right = (float) gameArea.right;
		retVal.bottom = (float) gameArea.bottom - (gameArea.bottom-gameArea.top)/2 + gameAreaPadding;
		return retVal;
	}
	
	public int getCurrentScoreTextSize() {
		return (int)((double)DEFAULT_CANVAS_TEXT_SIZE * scale);
	}
	
	public int getGameOverTextSize() {
		return (int)((double)DEFAULT_GAME_OVER_TEXT_SIZE * scale);
	}
	
	private void calculateValues() {
		if (width >= MIN_WIDTH_3_2_SCALE) {
			gameAreaSizeX = GAME_AREA_3_2_SCALE;
			invaderStartOffset = MOVE_LEN_3_2_SCALE;
			scale = 1.5;
		} else if (width >= MIN_WIDTH_1_1_SCALE) {
			gameAreaSizeX = GAME_AREA_1_1_SCALE;
			invaderStartOffset = MOVE_LEN_1_1_SCALE;
			scale = 1;
		} else if (width >= MIN_WIDTH_3_4_SCALE) {
			gameAreaSizeX = GAME_AREA_3_4_SCALE;
			invaderStartOffset = MOVE_LEN_3_4_SCALE;
			scale = .75;
		} else if (width >= MIN_WIDTH_1_2_SCALE) {
			gameAreaSizeX = GAME_AREA_1_2_SCALE;
			invaderStartOffset = MOVE_LEN_1_2_SCALE;
			scale = .5;
		} else if (width >= MIN_WIDTH_1_3_SCALE) {
			gameAreaSizeX = GAME_AREA_1_3_SCALE;
			invaderStartOffset = MOVE_LEN_1_3_SCALE;
			scale = .3333;
		} else {
			throw new RuntimeException("Ridiculous screen size Exception");
		}
	}
}
