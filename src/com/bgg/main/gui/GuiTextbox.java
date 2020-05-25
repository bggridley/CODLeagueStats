package com.bgg.main.gui;

import com.bgg.main.Main;
import com.bgg.main.Renderer;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GuiTextbox extends GuiComponent {

    GuiTextbox nextBox;
    public String currentText = "";
    boolean highlighted = false;
    public boolean clickedOn = false;
    private int maxChars = 0;
    private Main main;
    public boolean numeric = false;
    public boolean alphanumeric = true;

    Color offwhite = new Color(200, 200, 150);

    public GuiTextbox(Main main, int x, int y, int width, int height, int maxChars) {
        this.main = main;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxChars = maxChars;
    }

    public void setAlphanumeric(boolean f) {
        this.alphanumeric = f;
    }

    public void setNumeric(boolean f) {
        this.numeric = f;
        this.setAlphanumeric(!f);
    }

    public void setNextBox(GuiTextbox box) {
        this.nextBox = box;
    }

    public void click() {
        if(mouseInBounds()) {
            clickedOn = true;

            main.selectedBox = this;

            if(currentText.equals("0") && numeric) currentText = "";
        }
    }

    public void handleInput(int keycode, char keyChar) {
        if(keycode == KeyEvent.VK_BACK_SPACE) {
            if(currentText.length() != 0) {
                currentText = currentText.substring(0, currentText.length() - 1);
            }
        } else if(keycode == KeyEvent.VK_TAB) {
            if(nextBox != null) {
                if(main.selectedBox.currentText.equals("") && numeric) main.selectedBox.currentText = "0"; // set to 0

                main.selectedBox = nextBox;


                if(nextBox.currentText.equals("0") && numeric) nextBox.currentText = "";
               main.repaint();
                return;
            }

        } else if (currentText.length() != maxChars && Character.isLetterOrDigit(keyChar)){
            if((numeric) && Character.isDigit(keyChar)) {
                currentText += keyChar;
            } else if(alphanumeric && Character.isLetterOrDigit(keyChar)) {
                currentText += keyChar;
            }
        }
        main.repaint();

        typed();
    }

    public void typed() {

    }

    public void update() {
    highlighted = mouseInBounds();

    }

    public String getText() {
        return currentText;
    }

    public void render(Graphics2D g) {
        if(!visible) return;


        g.setFont(Main.trb);

        Rectangle bounds = Renderer.getStringBounds(g, Main.trb, currentText, x, y);


        // g.drawRect(bounds.x, bounds.y, (int)bounds.getWidth(), (int)bounds.getHeight());

        g.setColor(!highlighted ? Color.DARK_GRAY : Color.GRAY);

        if(main.selectedBox == this) {
            g.setColor(offwhite);
        }
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setColor(Color.WHITE);

        g.setColor(Color.WHITE);

        g.drawString(currentText, x + (x - bounds.x) + (width - ((int)bounds.getWidth())) / 2, y + (y - bounds.y) + (height - ((int)bounds.getHeight())) / 2);

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
