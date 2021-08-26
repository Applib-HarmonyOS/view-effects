package ir.mirrajabi.viewfilter.renderers;


import ir.mirrajabi.viewfilter.core.IRenderer;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

/**
 * BlurRenderer.
 *
 * @since 2021-08-06
 */

public class BlurRenderer implements IRenderer {
    int rsumCal;
    int gSum;
    int bSum;
    int xaxisCal;
    int yAxis;
    int iVar;
    int pVar;
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
    private int[] rArr;
    private int[] gArr;
    private int[] bArr;
    private int r1;
    private int[][] stack;
    private int[] vMin;
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
        rArr = new int[wh];
        gArr = new int[wh];
        bArr = new int[wh];

        vMin = new int[Math.max(width, height)];
        int divSum = (div + 1) >> 1;
        divSum *= divSum;
        dv = new int[256 * divSum];
        for (iVar = 0; iVar < 256 * divSum; iVar++) {
            dv[iVar] = (iVar / divSum);
        }
        yw = yi = 0;
        stack = new int[div][3];
        r1 = blurRadius + 1;
        yAxis();
        xAxis();
        bitmap.writePixels(pix, 0, width, region);
        return (bitmap);
    }


    private void xAxis() {
        for (xaxisCal = 0; xaxisCal < width; xaxisCal++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rsumCal = gSum = bSum = 0;
            yp = -blurRadius * width;
            for (iVar = -blurRadius; iVar <= blurRadius; iVar++) {
                yi = Math.max(0, yp) + xaxisCal;
                sir = stack[iVar + blurRadius];
                sir[0] = rArr[yi];
                sir[1] = gArr[yi];
                sir[2] = bArr[yi];
                rbs = r1 - Math.abs(iVar);
                rsumCal += rArr[yi] * rbs;
                gSum += gArr[yi] * rbs;
                bSum += bArr[yi] * rbs;
                if (iVar > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }
                if (iVar < hm) {
                    yp += width;
                }
            }
            yi = xaxisCal;
            stackPointer = blurRadius;
            for (yAxis = 0; yAxis < height; yAxis++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsumCal] << 16) | (dv[gSum] << 8) | dv[bSum];
                rsumCal -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;
                stackStart = stackPointer - blurRadius + div;
                sir = stack[stackStart % div];
                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];
                if (xaxisCal == 0) {
                    vMin[yAxis] = Math.min(yAxis + r1, hm) * width;
                }
                pVar = xaxisCal + vMin[yAxis];
                sir[0] = rArr[pVar];
                sir[1] = gArr[pVar];
                sir[2] = bArr[pVar];
                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];
                rsumCal += rinSum;
                gSum += ginSum;
                bSum += binSum;
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

    private void yAxis() {
        for (yAxis = 0; yAxis < height; yAxis++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rsumCal = gSum = bSum = 0;
            for (iVar = -blurRadius; iVar <= blurRadius; iVar++) {
                pVar = pix[yi + Math.min(wm, Math.max(iVar, 0))];
                sir = stack[iVar + blurRadius];
                sir[0] = (pVar & 0xff0000) >> 16;
                sir[1] = (pVar & 0x00ff00) >> 8;
                sir[2] = (pVar & 0x0000ff);
                rbs = r1 - Math.abs(iVar);
                rsumCal += sir[0] * rbs;
                gSum += sir[1] * rbs;
                bSum += sir[2] * rbs;
                if (iVar > 0) {
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
                rArr[yi] = dv[rsumCal];
                gArr[yi] = dv[gSum];
                bArr[yi] = dv[bSum];
                rsumCal -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;
                stackStart = stackPointer - blurRadius + div;
                sir = stack[stackStart % div];
                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];
                if (yAxis == 0) {
                    vMin[xaxisCal] = Math.min(xaxisCal + blurRadius + 1, wm);
                }
                pVar = pix[yw + vMin[xaxisCal]];
                sir[0] = (pVar & 0xff0000) >> 16;
                sir[1] = (pVar & 0x00ff00) >> 8;
                sir[2] = (pVar & 0x0000ff);
                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];
                rsumCal += rinSum;
                gSum += ginSum;
                bSum += binSum;
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
