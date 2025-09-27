package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombSupply;

/**
 * 具体工厂：炸弹道具工厂 (Concrete Creator for BombSupply)
 */
public class BombSupplyFactory implements PropFactory {
    public int speedY = 3;
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        return new BombSupply(locationX, locationY, speedY);
    }
}
