package mvc;

import javax.swing.*;

public class View extends JPanel implements Subscriber {
    protected Model model;

    public View(Model model) {
        this.model = model;
        model.subscribe(this);
    }

    public void setModel(Model newModel) {
      model.unsubscribe(this);
      model = newModel;
      model.subscribe(this);
      repaint();
    }

    @Override
    public void update() {

    }
}
