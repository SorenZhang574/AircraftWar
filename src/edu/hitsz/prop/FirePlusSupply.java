package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class FirePlusSupply extends AbstractProp{
    private static final long DURATION = 12000;

    public FirePlusSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        System.out.println("FirePlusSupply active!");
        hero.activateFire(2, DURATION);
        this.vanish();
    }
}
