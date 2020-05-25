package com.bgg.main.gui;

import com.bgg.main.Main;
import com.bgg.main.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Gui {

    protected LinkedList<GuiComponent> components;
    protected Main main;
    protected String title;

    protected ArrayList<GuiComponent> remove;

    public Gui (Main main) {
        this.main = main;
        components = new LinkedList<>();
        remove = new ArrayList<>();
    }

    public void updatePosition() {
        for(GuiComponent component : getComponents()) {
            component.updatePosition(main);
        }
    }

    public void  swap() { // called when this GUI is set as active

    }

    public void render(Graphics2D g) {
        for(GuiComponent component : components) {
            component.render(g);
        }


        if(title == null) return;
        g.setColor(Color.WHITE);
        g.setFont(Main.trbLarge);
        Rectangle bounds = Renderer.getStringBounds(g, Main.trbLarge, title, 0, 0);


        g.drawString(title, main.getWidth() - (int) bounds.getWidth() - 20, 40);
    }
    public void update() {

    }
  //  public abstract void

public void remove(GuiComponent component) {
        remove.add(component);
}

    public void add(GuiComponent component) {
        component.parent = this;
        components.add(component);
    }

    public LinkedList<GuiComponent> getComponents() {
        return components;
    }
}
