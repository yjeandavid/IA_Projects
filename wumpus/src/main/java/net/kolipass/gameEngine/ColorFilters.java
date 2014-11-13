package net.kolipass.gameEngine;


import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;


/**
 * ColorFilters.java
 * By Stephen Lindberg
 * Last modified: 10/11/2011
 * A class of static methods used to apply color filters to images.
 */


public class ColorFilters {
    public static Image setTransparentColor(Image srcImg, final Color tColor) // method accepts a transparent color.
    // It'll transform all pixels of the transparent color to transparent.
    {
        ImageFilter filter = new RGBImageFilter() // overriding part of the RGBImageFilterClass to produce a specialized filter.
        {
            public int testColor = tColor.getRGB() | 0xFF000000; // establish the transparent color as a hexidecimal value for bit-wise filtering.

            public int filterRGB(int x, int y, int rgb) // overriden method
            {
                if ((rgb | 0xFF000000) == testColor) // if transparent color matches the color being tested, make it transparent.
                {
                    return rgb & 0x00FFFFFF; // alpha bits set to 0 yields transparency.
                } else // otherwise leave it alone.
                    return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(srcImg.getSource(), filter);
        Image result = Toolkit.getDefaultToolkit().createImage(ip);

        return result;
    }


    public static Image setSemiTransparency(Image srcImg, double semiTrans) // method accepts a transparent color.
    // It'll transform all pixels of the transparent color to transparent.
    {
        if (semiTrans > 1.0)
            semiTrans = 1.0;
        if (semiTrans < 0.0)
            semiTrans = 0.0;
        final int alpha = (int) (255 * (1.0 - semiTrans));

        ImageFilter filter = new RGBImageFilter() // overriding part of the RGBImageFilterClass to produce a specialized filter.
        {
            public int filterRGB(int x, int y, int rgb) // overriden method
            {
                if ((rgb & 0xFF000000) != 0)
                    return (rgb & 0x00FFFFFF) | (alpha << 24); // alpha bits set to 0 yields transparency.
                else
                    return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(srcImg.getSource(), filter);
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.sync();
        Image result = tk.createImage(ip);


        return result;
    }
}