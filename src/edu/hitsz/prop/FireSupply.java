package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class FireSupply extends AbstractProp {
    private static final long DURATION = 8000;

    public FireSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FireSupply active!");
        hero.activateFire(1, DURATION);
        this.vanish();
    }
}
