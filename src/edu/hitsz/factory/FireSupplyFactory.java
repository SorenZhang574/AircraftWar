package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.FireSupply;

/**
 * 具体工厂：火力补给道具工厂 (Concrete Creator for FireSupply)
 */
public class FireSupplyFactory implements PropFactory {
    public int speedY = 3;
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        return new FireSupply(locationX, locationY, speedY);
    }
}
