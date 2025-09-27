package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;

public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 道具生效逻辑，由具体子类实现
     */
    public abstract void activate(HeroAircraft hero);

    /**
     * 道具下落（默认使用 forward，下落到屏幕外就消失）
     */
    @Override
    public void forward() {
        super.forward();
        if (locationY >= edu.hitsz.application.Main.WINDOW_HEIGHT) {
            vanish();
        }
    }
}
