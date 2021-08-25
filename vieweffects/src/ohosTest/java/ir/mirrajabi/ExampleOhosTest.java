package ir.mirrajabi;

import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ExampleOhosTest.
 *
 * @since 2021-08-06
 */

public class ExampleOhosTest {
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.huawei.blurvieweffect", actualBundleName);
    }
}