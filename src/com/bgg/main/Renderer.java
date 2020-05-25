package com.bgg.main;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class Renderer {

    public static void drawCenteredString(Graphics g, String text, int drawX, int drawY, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = drawX - (metrics.stringWidth(text) / 2);
        int y = (drawY - (metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    public static Rectangle getStringBounds(Graphics g, Font font, String str,
                                      float x, float y)
    {
        if(g == null) return new Rectangle(0, 0, 0, 0);

        FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }
}
