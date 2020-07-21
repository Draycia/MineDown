package de.themoep.minedown;

/*
 * Copyright (c) 2017 Max Lee (https://github.com/Phoenix616)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Util {

    private static final Pattern WRAP_PATTERN = Pattern.compile(" ", Pattern.LITERAL);

    /**
     * Utility method to throw an IllegalArgumentException if the value is false
     * @param value   The value to validate
     * @param message The message for the exception
     * @throws IllegalArgumentException Thrown if the value is false
     */
    public static void validate(boolean value, String message) throws IllegalArgumentException {
        if (!value) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check whether or not a character at a certain index of a string repeats itself
     * @param string The string to check
     * @param index  The index at which to check the character
     * @return Whether or not the character at that index repeated itself
     */
    public static boolean isDouble(String string, int index) {
        return index + 1 < string.length() && string.charAt(index) == string.charAt(index + 1);
    }

    /**
     * Get the index of the first occurrences of a not escaped character
     * @param string The string to search
     * @param chars  The characters to search for
     * @return The first unescaped index or -1 if not found
     */
    public static int indexOfNotEscaped(String string, String chars) {
        return indexOfNotEscaped(string, chars, 0);
    }

    /**
     * Get the index of the first occurrences of a not escaped character
     * @param string    The string to search
     * @param chars     The characters to search for
     * @param fromIndex Start searching from that index
     * @return The first unescaped index or {@code -1} if not found
     */
    public static int indexOfNotEscaped(String string, String chars, int fromIndex) {
        for (int i = fromIndex; i < string.length(); i++) {
            int index = string.indexOf(chars, i);
            if (index == -1) {
                return -1;
            }
            if (!isEscaped(string, index)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Check if a character at a certain index is escaped
     * @param string The string to check
     * @param index  The index of the character in the string to check
     * @return Whether or not the character is escaped (uneven number of backslashes in front of char mains it is escaped)
     * @throws IndexOutOfBoundsException if the {@code index} argument is not less than the length of this string.
     */
    public static boolean isEscaped(String string, int index) {
        if (index - 1 > string.length()) {
            return false;
        }
        int e = 0;
        while (index > e && string.charAt(index - e - 1) == '\\') {
            e++;
        }
        return e % 2 != 0;
    }

    /**
     * Wrap a string if it is longer than the line length and contains no new line.
     * Will try to wrap at spaces between words.
     * @param string        The string to wrap
     * @param lineLength    The max length of a line
     * @return The wrapped string
     */
    public static String wrap(String string, int lineLength) {
        if (string.length() <= lineLength || string.contains("\n")) {
            return string;
        }

        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        for (String s : WRAP_PATTERN.split(string)) {
            if (currentLine.length() + s.length() + 1 > lineLength) {
                int rest = lineLength - currentLine.length() - 1;
                if (rest > lineLength / 4 && s.length() > Math.min(rest * 2, lineLength / 4)) {
                    currentLine.append(" ").append(s, 0, rest);
                } else {
                    rest = 0;
                }
                lines.add(currentLine.toString());
                String restString = s.substring(rest);
                while (restString.length() >= lineLength) {
                    lines.add(restString.substring(0, lineLength));
                    restString = restString.substring(lineLength);
                }
                currentLine = new StringBuilder(restString);
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(s);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return String.join("\n", lines);
    }

    /**
     * Get the distance between two colors
     * @param c1 Color A
     * @param c2 Color B
     * @return The distance or 0 if they are equal
     */
    public static double distance(Color c1, Color c2) {
        if (c1.getRGB() == c2.getRGB()) {
            return 0;
        }
        return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2));
    }
}
