package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;

public class FireSupply extends AbstractProp {
    public FireSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FireSupply active!");
        hero.setShootStrategy(new ScatterShootStrategy());
        this.vanish();
    }
}
