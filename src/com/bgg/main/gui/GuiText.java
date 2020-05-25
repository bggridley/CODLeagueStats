package com.bgg.main.gui;

import com.bgg.main.Renderer;

import java.awt.*;

public class GuiText extends GuiComponent {

    private String text;
    private Font font;
    private boolean centered = true;
    private Color color;

    private Graphics2D g;

    public GuiText(String string, int x, int y, boolean centered, Graphics2D g, Font font) {
        this.text = string;
        this.centered = centered;
        this.x = x;
        this.y = y;
        this.g = g;
        this.font = font;

        this.color = new Color(255, 255 , 255);
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void update() { // do nothing

    }

    public void click()  {

    } // do nothing

    public void render(Graphics2D g) {
        if(!visible) return;

        g.setColor(color);
        g.setFont(font);
        if(centered) {
            Rectangle bounds = getBounds();
            g.drawString(text,x - bounds.width /2, y - bounds.height / 2);
        } else {
            g.drawString(text, x, y);
        }
    }

    public void setCentered(boolean cen) {
        this.centered = cen;
    }


    public Rectangle getBounds() {
        return Renderer.getStringBounds(g, font, text, x, y);
    }

}
