package modell;

import javafx.scene.media.AudioClip;

@SuppressWarnings("serial")
public class KachelleerException extends UBootRuntimeException{
	AudioClip clip;
	public void play() {
		//clip = new AudioClip(getClass().getResource("../resourcesPicturesAndSoundsVidoes/YouGetNothing.wav").toString());
		//clip.play(1.0);
	}
}
