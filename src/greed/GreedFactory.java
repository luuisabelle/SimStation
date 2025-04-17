package greed;

import mvc.Model;
import mvc.View;
import simstation.WorldFactory;

class GreedFactory extends WorldFactory {

    @Override
    public Model makeModel() {

//        Meadow m = new Meadow();
//        m.
        return new Meadow(); }
    @Override
    public String getTitle() { return "Greed";}
    @Override
    public View makeView(Model m) {
        return new GreedView((Meadow) m);
    }
}
