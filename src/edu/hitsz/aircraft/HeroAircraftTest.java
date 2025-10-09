package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {
    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        System.out.println("setUp: Resetting HeroAircraft instance for a new test.");
        heroAircraft = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        heroAircraft = null;
        System.out.println("tearDown: Test finished.");
    }

    /**
     * 测试 decreaseHp 方法的两种主要情况：
     * 1. 扣血后生命值仍大于0。
     * 2. 扣血后生命值小于或等于0，导致飞机消失。
     */
    @Test
    @DisplayName("HA1: decreaseHp test")
    void decreaseHp() {
        // case1
        System.out.println("Testing decreaseHp branch: HP > 0 after decrease.");
        heroAircraft.decreaseHp(30);
        assertEquals(70, heroAircraft.getHp(), "Hp: 70.");
        assertFalse(heroAircraft.notValid(), "Hp over 0, valid.");
        // case2
        System.out.println("Testing decreaseHp branch: HP <= 0 after decrease.");
        heroAircraft.decreaseHp(70);
        assertEquals(0, heroAircraft.getHp(), "Hp: 0.");
        assertTrue(heroAircraft.notValid(), "Hp is less than or equals 0, not valid.");
    }

    /**
     * 测试 getInstance 方法是否遵循单例模式。
     * 同时验证单例的初始状态是否正确。
     */
    @Test
    @DisplayName("HA2: getInstance test")
    void getInstance() {
        // 单例
        System.out.println("Testing getInstance for singleton property.");
        HeroAircraft anotherInstance = HeroAircraft.getInstance();
        assertSame(heroAircraft, anotherInstance, "Return the same.");
        // 初始状态
        System.out.println("Testing initial state of the singleton instance.");
        assertEquals(100, heroAircraft.getHp(), "Initial Hp: 100.");
        assertEquals(100, heroAircraft.maxHp, "Max Hp: 100.");
        assertFalse(heroAircraft.notValid(), "Initial state: valid.");
    }

    /**
     * 测试 shoot 方法能否正确生成子弹。
     * 检查子弹的数量、速度方向和位置。
     */
    @Test
    @DisplayName("HA3: shoot test")
    void shoot() {
        System.out.println("Testing shoot method.");
        List<BaseBullet> bullets = heroAircraft.shoot();
        // 子弹列表
        assertNotNull(bullets, "Bullets shouldn't be null.");
        assertEquals(1, bullets.size(), "Hero Aircraft should have 1 shot bullet.");
        // 子弹属性
        BaseBullet bullet = bullets.get(0);
        assertTrue(bullet.getSpeedY() < 0, "SpeedY should less than 0.");
        assertEquals(heroAircraft.getLocationX(), bullet.getLocationX(), "LocationX of the bullet should equal x.");
    }
}