package modell;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ImageCombo implements Callback<ListView<ImageView>, ListCell<ImageView>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.util.Callback#call(java.lang.Object) neuschreiben der
	 * ComboBox, wie sie sich mit imageViews verhalten soll, damit immer die
	 * richtigen ImageViews angezeigt werden und keine Leerfelder entstehen
	 */
	@Override
	public ListCell<ImageView> call(ListView<ImageView> p) {
		return new ListCell<ImageView>() {
			private final ImageView imgView;
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				this.imgView = new ImageView();
			}

			@Override
			protected void updateItem(ImageView item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setGraphic(null);
				} else {
					this.imgView.setImage(item.getImage());
					this.setGraphic(this.imgView);
				}
			}
		};
	}

}
