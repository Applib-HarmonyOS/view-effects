package ir.mirrajabi.viewfilter.core;

import ohos.media.image.PixelMap;

/**
 * IRenderer.
 *
 * @since 2021-08-06
 */

public interface Renderer {
    PixelMap render(PixelMap sentBitMap);
}