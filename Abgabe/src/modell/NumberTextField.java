package modell;

import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NumberTextField extends TextField {
	
	private GridPane gridPane;
	private ResourceBundle rb;

	public NumberTextField(GridPane grid, ResourceBundle rb) {
		setGridPane(grid);
		setRb(rb);
	}

	@Override
	public void replaceText(int start, int end, String text) {
		try {
			if (text.matches("[0-9]") && getText().length() < 3 || text == "") {
				super.replaceText(start, end, text);
			} else
				throw new FeldVollException(getGridPane(), getRb());
		} catch (FeldVollException ex) {
			ex.ausgabe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void replaceSelection(String text) {
		try {
			if (text.matches("[0-9]") && getText().length() < 3 || text == "") {
				super.replaceSelection(text);
			} else
				throw new FeldVollException(getGridPane(), getRb());
		} catch (FeldVollException ex) {
			ex.ausgabe();
		} catch (Exception e) {
			e.printStackTrace();
		}
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