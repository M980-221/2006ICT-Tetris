package com.pg2.tetris;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class PlayerStrategyFakeTest {
 @Test void fakeStrategyReceivesUpdate(){FakeStrategy fake=new FakeStrategy();fake.update(null,.25);assertEquals(1,fake.calls);}
 private static final class FakeStrategy implements PlayerStrategy {int calls;public void update(GameController controller,double elapsed){calls++;}}
}
