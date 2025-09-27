package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class FireSupply extends AbstractProp {
    public FireSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FireSupply active!");
        this.vanish();
    }
}
