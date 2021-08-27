/*
 * Copyright(C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on as "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or impiles.
 * See the License for the specific language governing permissions and
 * Limitations under the License.
 */

package com.huawei.blurvieweffect;

import ir.mirrajabi.viewfilter.core.ViewFilter;
import ir.mirrajabi.viewfilter.renderers.BlurRenderer;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To execute the UnitTestcases.
 */

public class ExampleOhosTest {
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.huawei.blurvieweffect", actualBundleName);
    }

    @Test
    public void testBlurRender() {
        assertEquals("class ir.mirrajabi.core.ViewFilter", ViewFilter.getInstance().setRenderer(
            new BlurRenderer(30)).getClass().toString());
    }
}