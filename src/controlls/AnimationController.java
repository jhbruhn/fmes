package controlls;

import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AnimationController {
	private int paneCount = 0;
	private RadioMenuItem[] menus;
	private ComboBox<ImageView> combo;
	private Label label;
	private ResourceBundle rb;
	boolean clicked = false;

	public AnimationController(RadioMenuItem[] m, ComboBox<ImageView> c , Label l, ResourceBundle rb) {
		combo = c;
		menus = m;
		label = l;
		this.rb = rb;
	}

	/*
	 * sorgt daf�r, dass die Pane am Ende wieder entfernt wird, wenn die
	 * Animation/en abgeschlossen sind. Sonst werden die onClick-Events des
	 * Spielfeldes in jeglicher Art nicht mehr "anklickbar". Somit wird dieses
	 * sichtbar zumindest kurz disabled und am ende wieder freigesetzt
	 */
	public void paneControll(double time, Pane p, Group root) {
		Pane pane = p;
		String text = rb.getString("paneClicked");
		pane.setOnMouseClicked(e-> {
			clicked = true;
			label.setTextFill(Color.RED);
			label.setText(text);	
		});
		paneCount = paneCount + 1;
		setDisable(true);
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(time + 50), ae -> {
			root.getChildren().remove(pane);
			paneCount = paneCount - 1;
			if (paneCount == 0) {
				setDisable(false);
				if(clicked){
					label.setTextFill(Color.WHITE);
					label.setText(rb.getString("labelOKAgain"));
				}
			}
		}));
		timeline.play();

	}

	private void setDisable(boolean b) {
		for (RadioMenuItem m : menus) {
			m.setDisable(b);
		}
		combo.setDisable(b);
	}
}
