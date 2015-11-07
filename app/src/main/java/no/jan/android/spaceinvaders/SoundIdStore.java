package no.jan.android.spaceinvaders;

public class SoundIdStore {
	private int playerMissileSoundId;
	private int playerBombedSoundId;
	private int mysterySoundId;
	private int invaderSoundindex;
	private int[] invaderSounds;
	
	public SoundIdStore() {
		playerMissileSoundId = -1;
		playerBombedSoundId = -1;
		mysterySoundId = -1;
		invaderSoundindex = 0;
		invaderSounds = new int[4];
	}

	public int getPlayerMissileSoundId() {
		return playerMissileSoundId;
	}

	public void setPlayerMissileSoundId(int playerMissileSoundId) {
		this.playerMissileSoundId = playerMissileSoundId;
	}

	public int getPlayerBombedSoundId() {
		return playerBombedSoundId;
	}

	public void setPlayerBombedSoundId(int playerBombedSoundId) {
		this.playerBombedSoundId = playerBombedSoundId;
	}

	public int getMysterySoundId() {
		return mysterySoundId;
	}

	public void setMysterySoundId(int mysterySoundId) {
		this.mysterySoundId = mysterySoundId;
	}

	public int getNextInvaderSound() {
		if (invaderSoundindex >= invaderSounds.length) {
			invaderSoundindex = 0;
		}
		return invaderSounds[invaderSoundindex++];
	}

	public void addInvaderSound(int invaderSoundId) {
		this.invaderSounds[invaderSoundindex] = invaderSoundId;
		invaderSoundindex++;
		if (invaderSoundindex >= invaderSounds.length) {
			invaderSoundindex = 0;
		}
	}
	
}
