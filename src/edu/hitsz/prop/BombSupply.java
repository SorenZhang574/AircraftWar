package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

import java.util.LinkedList;
import java.util.List;

public class BombSupply extends AbstractProp {
    private static final List<BombObserver> observers = new LinkedList<>();
    /**
     * 静态方法，供外部对象观察
     */
    public static void registerObserver(BombObserver s) {
        if (!observers.contains(s)) {
            observers.add(s);
        }
    }
    /**
     * 静态方法，供外部对象取消观察
     */
    public static void removeObserve(BombObserver s) {
        observers.remove(s);
    }
    /**
     * 通知所有订阅者
     * 这个方法不是静态的，只有当一个具体的炸弹道具被激活时才调用
     */
    private void notifyObservers() {
        System.out.println("BombProp is notifying " + observers.size() + " subscribers!");
        for (int i = observers.size() - 1; i >= 0; i--) {
            observers.get(i).update();
        }
    }

    public BombSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        notifyObservers();
        this.vanish();
    }
}
