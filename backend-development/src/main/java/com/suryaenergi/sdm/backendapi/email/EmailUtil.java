package com.suryaenergi.sdm.backendapi.email;

import java.awt.*;

public class EmailUtil {
    public static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private EmailUtil() {
        throw new IllegalStateException("Utility class");
    }
}
