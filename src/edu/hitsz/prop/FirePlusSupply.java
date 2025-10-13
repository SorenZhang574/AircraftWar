package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RingShootStrategy;

public class FirePlusSupply extends AbstractProp{
    public FirePlusSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FirePlusSupply active!");
        hero.setShootStrategy(new RingShootStrategy());
        this.vanish();
    }
}
