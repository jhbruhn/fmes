package modell;

import javafx.scene.media.AudioClip;

@SuppressWarnings("serial")
public class FelsenDaException extends UBootRuntimeException {
	AudioClip clip;

	public void play() {
		//clip = new AudioClip(getClass().getResource("../resourcesPicturesAndSoundsVidoes/lost.wav").toString());
		//clip.play(1.0);
	}
}
