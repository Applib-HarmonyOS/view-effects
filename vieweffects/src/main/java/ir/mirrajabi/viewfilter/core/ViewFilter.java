package ir.mirrajabi.viewfilter.core;

import ohos.agp.components.Component;
import ohos.agp.components.element.PixelMapElement;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;

/**
 * ViewFilter.
 *
 * Getting background imageView and applying filter.
 */

public class ViewFilter {
    private static ViewFilter instance = null;
    private Renderer renderer;

    /**
     * get ViewFiter instance.
     */
    public static ViewFilter getInstance() {
        if (instance == null) {
            instance = new ViewFilter();
        }
        return instance;
    }

    public ViewFilter setRenderer(Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    /**
     * apply filter and updating the view.
     *
     * @param view view
     */
    public void applyFilterOnView(final Component view) {
        EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
        handler.postTask(() -> {
                PixelMapElement drawable = new PixelMapElement(loadBitmapFromView(view));
                view.setBackground(drawable);
            }

        );
    }

    /**
     * Sending to render the background image.
     *
     * @param view view
     */
    public PixelMap loadBitmapFromView(Component view) {
        PixelMapElement backgroundElement = (PixelMapElement) view.getBackgroundElement();
        PixelMap cropperBitMap = backgroundElement.getPixelMap();
        return renderer.render(cropperBitMap);
    }
}


