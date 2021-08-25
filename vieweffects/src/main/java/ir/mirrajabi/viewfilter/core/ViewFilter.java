package ir.mirrajabi.viewfilter.core;

import ohos.agp.components.Component;
import ohos.agp.components.element.PixelMapElement;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;

/**
 * ViewFilter.
 *
 * @since 2021-08-06
 */

public class ViewFilter {
    private static ViewFilter instance = null;
    private IRenderer renderer;

    public static ViewFilter getInstance() {
        if (instance == null) {
            instance = new ViewFilter();
        }
        return instance;
    }

    public ViewFilter setRenderer(IRenderer renderer) {
        this.renderer = renderer;
        return this;
    }

    public void applyFilterOnView(final Component view) {
        EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
        handler.postTask(() -> {
                PixelMapElement drawable = new PixelMapElement(loadBitmapFromView(view));
                view.setBackground(drawable);
            }

        );
    }

    public PixelMap loadBitmapFromView(Component view) {
        PixelMapElement backgroundElement = (PixelMapElement) view.getBackgroundElement();
        PixelMap cropperBitMap = backgroundElement.getPixelMap();
        return renderer.render(cropperBitMap);
    }
}


