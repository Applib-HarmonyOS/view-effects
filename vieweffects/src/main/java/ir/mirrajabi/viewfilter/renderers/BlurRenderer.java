package ir.mirrajabi.viewfilter.renderers;

import ohos.media.image.PixelMap;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ir.mirrajabi.viewfilter.core.Renderer;

/**
 * BlurRenderer algorithm to add blurr to view.
 */

public class BlurRenderer implements Renderer {
    int rsumCal;
    int gsumCal;
    int bsumCal;
    int xaxisCal;
    int yaxisCal;
    int ivarCal;
    int pvarCal;
    int yp;
    int yi;
    int yw;
    int goutSum;
    int boutSum;
    int rinSum;
    int ginSum;
    int binSum;
    int stackPointer;
    int stackStart;
    int[] sir;
    int rbs;
    private int blurRadius;
    private int height;
    private int[] pix;
    private int width;
    private int wm;
    private int hm;
    private int div;
    private int[] rarrCal;
    private int[] garrCal;
    private int[] barrCal;
    private int r1;
    private int[][] stack;
    private int[] vminCal;
    private int[] dv;
    private int routSum;

    public BlurRenderer(int blurRadius) {
        this.blurRadius = blurRadius;
    }


    @Override
    public PixelMap render(PixelMap sentBitMap) {
        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.editable = true;
        options.size = new Size(
            sentBitMap.getImageInfo().size.width, sentBitMap.getImageInfo().size.height);
        PixelMap bitmap = PixelMap.create(sentBitMap, options);

        if (blurRadius < 1) {
            return null;
        }
        height = bitmap.getImageInfo().size.height;
        width = bitmap.getImageInfo().size.width;
        pix = new int[width * height];
        Rect region = new Rect();
        region.height = height;
        region.width = width;
        region.minX = 0;
        region.minY = 0;
        bitmap.readPixels(pix, 0, width, region);
        wm = width - 1;
        hm = height - 1;
        int wh = width * height;
        div = blurRadius + blurRadius + 1;
        rarrCal = new int[wh];
        garrCal = new int[wh];
        barrCal = new int[wh];

        vminCal = new int[Math.max(width, height)];
        int divSum = (div + 1) >> 1;
        divSum *= divSum;
        dv = new int[256 * divSum];
        for (ivarCal = 0; ivarCal < 256 * divSum; ivarCal++) {
            dv[ivarCal] = (ivarCal / divSum);
        }
        yw = yi = 0;
        stack = new int[div][3];
        r1 = blurRadius + 1;
        yaxisIdentify();
        xaxisIdentify();
        bitmap.writePixels(pix, 0, width, region);
        return (bitmap);
    }


    private void xaxisIdentify() {
        for (xaxisCal = 0; xaxisCal < width; xaxisCal++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rsumCal = gsumCal = bsumCal = 0;
            yp = -blurRadius * width;
            for (ivarCal = -blurRadius; ivarCal <= blurRadius; ivarCal++) {
                yi = Math.max(0, yp) + xaxisCal;
                sir = stack[ivarCal + blurRadius];
                sir[0] = rarrCal[yi];
                sir[1] = garrCal[yi];
                sir[2] = barrCal[yi];
                rbs = r1 - Math.abs(ivarCal);
                rsumCal += rarrCal[yi] * rbs;
                gsumCal += garrCal[yi] * rbs;
                bsumCal += barrCal[yi] * rbs;
                if (ivarCal > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }
                if (ivarCal < hm) {
                    yp += width;
                }
            }
            yi = xaxisCal;
            stackPointer = blurRadius;
            for (yaxisCal = 0; yaxisCal < height; yaxisCal++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsumCal] << 16) | (dv[gsumCal] << 8) | dv[bsumCal];
                rsumCal -= routSum;
                gsumCal -= goutSum;
                bsumCal -= boutSum;
                stackStart = stackPointer - blurRadius + div;
                sir = stack[stackStart % div];
                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];
                if (xaxisCal == 0) {
                    vminCal[yaxisCal] = Math.min(yaxisCal + r1, hm) * width;
                }
                pvarCal = xaxisCal + vminCal[yaxisCal];
                sir[0] = rarrCal[pvarCal];
                sir[1] = garrCal[pvarCal];
                sir[2] = barrCal[pvarCal];
                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];
                rsumCal += rinSum;
                gsumCal += ginSum;
                bsumCal += binSum;
                stackPointer = (stackPointer + 1) % div;
                sir = stack[stackPointer];
                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];
                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];
                yi += width;
            }
        }
    }

    private void yaxisIdentify() {
        for (yaxisCal = 0; yaxisCal < height; yaxisCal++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rsumCal = gsumCal = bsumCal = 0;
            for (ivarCal = -blurRadius; ivarCal <= blurRadius; ivarCal++) {
                pvarCal = pix[yi + Math.min(wm, Math.max(ivarCal, 0))];
                sir = stack[ivarCal + blurRadius];
                sir[0] = (pvarCal & 0xff0000) >> 16;
                sir[1] = (pvarCal & 0x00ff00) >> 8;
                sir[2] = (pvarCal & 0x0000ff);
                rbs = r1 - Math.abs(ivarCal);
                rsumCal += sir[0] * rbs;
                gsumCal += sir[1] * rbs;
                bsumCal += sir[2] * rbs;
                if (ivarCal > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }
            }
            stackPointer = blurRadius;
            for (xaxisCal = 0; xaxisCal < width; xaxisCal++) {
                rarrCal[yi] = dv[rsumCal];
                garrCal[yi] = dv[gsumCal];
                barrCal[yi] = dv[bsumCal];
                rsumCal -= routSum;
                gsumCal -= goutSum;
                bsumCal -= boutSum;
                stackStart = stackPointer - blurRadius + div;
                sir = stack[stackStart % div];
                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];
                if (yaxisCal == 0) {
                    vminCal[xaxisCal] = Math.min(xaxisCal + blurRadius + 1, wm);
                }
                pvarCal = pix[yw + vminCal[xaxisCal]];
                sir[0] = (pvarCal & 0xff0000) >> 16;
                sir[1] = (pvarCal & 0x00ff00) >> 8;
                sir[2] = (pvarCal & 0x0000ff);
                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];
                rsumCal += rinSum;
                gsumCal += ginSum;
                bsumCal += binSum;
                stackPointer = (stackPointer + 1) % div;
                sir = stack[(stackPointer) % div];
                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];
                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];
                yi++;
            }
            yw += width;
        }
    }
}
