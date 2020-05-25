package com.bgg.main.gui;

import com.bgg.main.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class GuiList extends GuiComponent {

    private LinkedList<GuiComponent> componentList;
    private ArrayList<GuiComponent> toRemove;

    public String group = ""; // if there's a group, they are connected to eachother in a vertical fashion. in order that they are added

    private Main main;
    private boolean open = false;
    private GuiList parentList;
    private Gui parent;
    boolean fixed = false;

    private int lastY = 0;

    public LinkedList<GuiComponent> getComponents() {
        return componentList;
    }

    public GuiList(Main main, Gui parent, GuiList parentList) {
        componentList = new LinkedList<>();
        this.main = main;
        this.parent = parent;
        this.parentList = parentList;

        setOpen(open);
        setVisible(visible);

    }

    public void clear() {
        componentList.clear();
    }

    public void add (GuiComponent comp) {

        componentList.add(comp);
        parent.add(comp); // add them both u know
    }

    public void updatePosition(Main main) {
        for(GuiComponent component : componentList) {
            component.updatePosition(main);
        }

//        for(GuiComponent c : componentList) {
//            comp.y +=yOffset;
//            yOffset+= comp.getBounds().height;
//


//        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void update(){ // call update to fix the list

    }

    public void setY(int y) {
        int yOffset = y;
        lastY = y;

        this.y = y;

        for(GuiComponent component : componentList) {
            if(component instanceof GuiList) {
                ((GuiList) component).setOpen(((GuiList) component).open);
                ((GuiList) component).setY(yOffset);

                if(!component.takeLast)
                yOffset += ((GuiList) component).getOpenHeight() + 5;

            } else {
                component.y = yOffset + component.listOffset;

                if(!component.takeLast)
                yOffset+= component.getBounds().getHeight() + 5;
            }
        }
    }

    public Rectangle getBounds() {



       // System.out.println("bounds:" + start.toString());

        if(componentList.isEmpty()) {
            return new Rectangle(0, 0, 0, 0);
        } else {
            return componentList.get(0).getBounds();
        }
    }

    public int getOpenHeight() {

            int yOffset = (int)componentList.get(0).getBounds().getHeight() + 5;

            if(open) {
                yOffset = 0;
                for (GuiComponent component : componentList) {
                    if(!component.takeLast)
                    yOffset += component.getBounds().getHeight() + 5;
                }
            }

            return yOffset;


            // TODO STOP MAKING AN EW RECTNALGE ON EVERY getBounds() function!  PLEASE.  STOP DOING IT .  ITS FUCKIGN STUJPID,.  YOU ARE FUCKIGN DUMB
    }


public void setOpen(boolean open) {
        this.open = open;
      for(int i = 0; i < getComponents().size(); i++) {
        GuiComponent g = getComponents().get(i);
        if(i != 0)
        g.setVisible(open);
    }


}

    public void click() {
        if(mouseInBounds() && visible) {

            if(!fixed) {
                setOpen(!open);
                updatePosition(main);

                System.out.println("set to:" + open);


                if (parentList != null)
                    parentList.setY(parentList.lastY);
            }
        }
    }

    public void render (Graphics2D g) {
        if(!visible) return;

        for(GuiComponent component : componentList) {
            component.render(g);
        }
    }

}
