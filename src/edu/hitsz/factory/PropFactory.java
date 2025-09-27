package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;

/**
 * 抽象道具工厂 (Abstract Creator)
 * 定义了创建道具的抽象工厂方法 createProp()
 */
public interface PropFactory {
    /**
     * 创建道具的抽象方法（工厂方法）
     */
    public abstract AbstractProp createProp(int locationX, int locationY);
}

