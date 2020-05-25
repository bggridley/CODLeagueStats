package com.bgg.main.gui;

import com.bgg.main.Main;

import java.awt.*;
import java.util.HashMap;

import static com.bgg.main.Main.mouseX;
import static com.bgg.main.Main.mouseY;

public abstract class GuiComponent {


    public boolean visible = true;
    public int x;
    public int y;
    int width = 125;
    int height = 25;

    public int listOffset = 0;

    public Gui parent;

    public boolean dynamic = false;

    public boolean takeLast = false; // IF SET TO TRUE, WHEN THIS COMPONENT'S POSITION IS CALCULATED IN A LIST, IT WILL TAKE THE PREVIOUS Y.

   public abstract void render(Graphics2D g);
   public abstract void update();
   public abstract void click();
   public abstract Rectangle getBounds();

   public HashMap<String, Object> tags = new HashMap<>();

   public boolean mouseInBounds() {
       Rectangle bounds = getBounds();
       return  (mouseX > bounds.getX() && mouseX < bounds.getX() + bounds.getWidth() && mouseY > bounds.getY() && mouseY < bounds.getY() + bounds.getHeight());
   }

   public void setVisible(boolean visible) {
       this.visible = visible;
   }

   public boolean isDynamic() {
       return dynamic;
   }

   public void updatePosition(Main main) {

   } // this is called when the window is resized. is meant to be overriden. can use main to getWidth and getHeight etc.
}
