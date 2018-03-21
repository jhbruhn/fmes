package modell;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ScAchse {
	private final DoubleProperty value = new SimpleDoubleProperty(this, "value", 0);

	public Double getValue() {
		return value.get();
	}

	public void setValue(Double val) {
		value.set(val);
	}

	public DoubleProperty valueProperty() {
		return value;
	}
}