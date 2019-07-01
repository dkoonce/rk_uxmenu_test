/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rkbots.tools.app.uxmenu;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author root
 */
public class Fonts {

    public static void loadFonts() {
        try {
            loadFont("/fonts/fontawesome-webfont.ttf");
            loadFont("/fonts/metro.ttf");
            loadFont("/fonts/typicons.ttf");
        } catch (IOException | FontFormatException exp) {
            exp.printStackTrace();
        }
    }

    private static void loadFont(final String fontName) throws IOException, FontFormatException {
        InputStream is = Resources.loadResourceAsStream(fontName);
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        System.out.println("Font loaded from jar: " + font.getFontName() + " " + font.getName()
            + " " + font.getFamily() + " " + font.getPSName());
        final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        localGraphicsEnvironment.registerFont(font);
        is.close();
    }
}
