package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BombSupply extends AbstractProp {
    public BombSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("BombSupply active!");
        this.vanish();
    }
}
