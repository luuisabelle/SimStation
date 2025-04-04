package mvc;

import java.util.ArrayList;

public class Publisher {
    public ArrayList<Subscriber> subscriberList = new ArrayList<>();

    public void notifySubscribers() {
        for (Subscriber s : subscriberList) {
            s.update();
        }
    }

    public void subscribe(Subscriber subscriber) {
        subscriberList.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscriberList.remove(subscriber);
    }
}
