package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.FirePlusSupply;

public class FirePlusSupplyFactory implements PropFactory {
    public int speedY = 3;
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        return new FirePlusSupply(locationX, locationY, speedY);
    }
}
