package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.HpSupply;

/**
 * 具体工厂：血量补给道具工厂
 */
public class HpSupplyFactory implements PropFactory {
    public int speedY = 3;
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        return new HpSupply(locationX, locationY, speedY);
    }
}
