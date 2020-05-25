package com.bgg.main.gui;

import com.bgg.main.Main;
import com.bgg.main.Renderer;

import java.awt.*;

public class ComboBox extends GuiComponent {
    boolean swap = false;


    String[] strings;
    int highlightedIndex = -1;
    int selectedIndex = 0;
    Font font;
    public boolean selected = false; //whether the combo box is "opened" or not

   // int oldPos = 0; // so that the list is not all jumbled up.

    public ComboBox(int x, int y, int width, int height, String[] elements, Font font) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y =y ;
        this.strings = elements;
        this.font = font;
    }

    public boolean swapCondition() {
        return true; // if returns false, it will be impossible to swap.
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }


    public void update() {
// updated on mouse move

        if(mouseInBounds()) {
            int newIndex = (Main.mouseY - y) / height;


           highlightedIndex = newIndex;
            // System.out.println(highlightedIndex);
        } else {
            highlightedIndex = -1;

            selected = false;
        }
    }

    public void render(Graphics2D g) {
        if(!visible) return;

        g.setFont(font);
            for(int i = 0; i < strings.length; i++) {
                if(i > 0 && !selected) break;

                String curString = strings[i];

                if(curString.length() > 14) {
                    curString = curString.substring(0, 14);
                    curString = curString + "...";
                }

                Rectangle bounds = Renderer.getStringBounds(g, font, curString, x, y);


           // g.drawRect(bounds.x, bounds.y, (int)bounds.getWidth(), (int)bounds.getHeight());

                g.setColor(highlightedIndex != i ? Color.DARK_GRAY : Color.GRAY);
                g.fillRect(x, y + i * height, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(x, y + i * height, width, height);
                g.setColor(Color.WHITE);

             g.setColor(Color.WHITE);

             g.drawString(curString, x + (x - bounds.x) + (width - ((int)bounds.getWidth())) / 2, y + (i * height) + (y - bounds.y) + (height - ((int)bounds.getHeight())) / 2);
            }
    }

    public void setSwap(boolean swap) {
        this.swap = swap;
    }

    public void click() {
        selected = !selected;

        int newIndex = (Main.mouseY - y) / height;
        click(strings[newIndex]);


        if(!swap) return;

        if(!swapCondition()) return;

        String firstSlot = strings[0];

        strings[0] = strings[newIndex];
        strings[newIndex] = firstSlot;

       // System.out.println("clicky lcick");
    }

    public void click(String selected) {

    }

    public void setSelected(String selected) {
        for(int i = 0; i < strings.length; i++) {
            if(strings[i].equals(selected)) {

                String firstSlot = strings[0];

                strings[0] = strings[i];
                System.out.println(firstSlot + "SELECTED" + selected + " NEW VAL" + strings[0] + selectedIndex);
                strings[i] = firstSlot;

            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, selected ? strings.length * height : height);
    }

    public String getCurrent() {
        return strings[selectedIndex];
    }


}
