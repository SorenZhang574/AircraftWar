package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class HpSupply extends AbstractProp {
    private final int hpIncrease = 30;

    public HpSupply(int x, int y, int speedY) {
        super(x, y, 0, speedY);
    }

    @Override
    public void activate(HeroAircraft hero) {
        hero.increaseHp(hpIncrease);
        this.vanish();
    }
}
