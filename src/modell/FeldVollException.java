package modell;

import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class FeldVollException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridPane gridPane;
	private ResourceBundle rb;
	
	public FeldVollException(GridPane grid, ResourceBundle rb){
		setGridPane(grid);
		setRb(rb);
	}
	
	public void ausgabe(){
		Label l = new Label(rb.getString("wrongInput"));
		l.setTextFill(Color.RED);
		getGridPane().add(l, 1, 0);
	}

	public GridPane getGridPane() {
		return gridPane;
	}

	public void setGridPane(GridPane gridPane) {
		this.gridPane = gridPane;
	}

	public ResourceBundle getRb() {
		return rb;
	}

	public void setRb(ResourceBundle rb) {
		this.rb = rb;
	}
}
