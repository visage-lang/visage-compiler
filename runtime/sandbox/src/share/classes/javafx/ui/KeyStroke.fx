/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */ 
package javafx.ui;


public class KeyStroke {
    public static attribute KEYBOARD = Keyboard{};
    attribute description: String;
    
    attribute id: Integer
        on replace {
            KEYBOARD.putKeyStroke(id, this);
            description = javax.swing.KeyStroke.getKeyStroke(id, 0).toString();
        
    };
    attribute keyChar: String;
    


/*
    public static attribute ALT_DOWN = KeyStroke {id: java.awt.event.InputEvent.ALT_DOWN_MASK};
*/
    public static attribute ENTER = KeyStroke {id: java.awt.event.KeyEvent.VK_ENTER };
    public static attribute BACK_SPACE = KeyStroke {id: java.awt.event.KeyEvent.VK_BACK_SPACE };
    public static attribute TAB = KeyStroke {id: java.awt.event.KeyEvent.VK_TAB };
    public static attribute CANCEL = KeyStroke {id: java.awt.event.KeyEvent.VK_CANCEL };
    public static attribute CLEAR = KeyStroke {id: java.awt.event.KeyEvent.VK_CLEAR };
    public static attribute SHIFT = KeyStroke {id: java.awt.event.KeyEvent.VK_SHIFT };
    public static attribute CONTROL = KeyStroke {id: java.awt.event.KeyEvent.VK_CONTROL };
    public static attribute ALT = KeyStroke {id: java.awt.event.KeyEvent.VK_ALT };
    public static attribute PAUSE = KeyStroke {id: java.awt.event.KeyEvent.VK_PAUSE };
    public static attribute CAPS_LOCK = KeyStroke {id: java.awt.event.KeyEvent.VK_CAPS_LOCK };
    public static attribute ESCAPE = KeyStroke {id: java.awt.event.KeyEvent.VK_ESCAPE };
    public static attribute SPACE = KeyStroke {id: java.awt.event.KeyEvent.VK_SPACE };
    public static attribute PAGE_DOWN = KeyStroke {id: java.awt.event.KeyEvent.VK_PAGE_DOWN };
    public static attribute PAGE_UP = KeyStroke {id: java.awt.event.KeyEvent.VK_PAGE_UP };
    public static attribute END = KeyStroke {id: java.awt.event.KeyEvent.VK_END };
    public static attribute HOME = KeyStroke {id: java.awt.event.KeyEvent.VK_HOME };

    /**
     * Constant for the non-numpad <b>left</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_KP_LEFT
     */
    public static attribute LEFT = KeyStroke {id: java.awt.event.KeyEvent.VK_LEFT };

    /**
     * Constant for the non-numpad <b>up</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_KP_UP
     */
    public static attribute UP = KeyStroke {id: java.awt.event.KeyEvent.VK_UP };

    /**
     * Constant for the non-numpad <b>right</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_KP_RIGHT
     */
    public static attribute RIGHT = KeyStroke {id: java.awt.event.KeyEvent.VK_RIGHT };

    /**
     * Constant for the non-numpad <b>down</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_KP_DOWN
     */
    public static attribute DOWN = KeyStroke {id: java.awt.event.KeyEvent.VK_DOWN };

    /**
     * Constant for the comma key, ","
     */
    public static attribute COMMA = KeyStroke {id: java.awt.event.KeyEvent.VK_COMMA };

    /**
     * Constant for the minus key, "-"
     * @since 1.2
     */
    public static attribute MINUS = KeyStroke {id: java.awt.event.KeyEvent.VK_MINUS };

    /**
     * Constant for the period key, "."
     */
    public static attribute PERIOD = KeyStroke {id: java.awt.event.KeyEvent.VK_PERIOD };

    /**
     * Constant for the forward slash key, "/"
     */
    public static attribute SLASH = KeyStroke {id: java.awt.event.KeyEvent.VK_SLASH };

    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */
    public static attribute _0 = KeyStroke {id: java.awt.event.KeyEvent.VK_0 };
    public static attribute _1 = KeyStroke {id: java.awt.event.KeyEvent.VK_1 };
    public static attribute _2 = KeyStroke {id: java.awt.event.KeyEvent.VK_2 };
    public static attribute _3 = KeyStroke {id: java.awt.event.KeyEvent.VK_3 };
    public static attribute _4 = KeyStroke {id: java.awt.event.KeyEvent.VK_4 };
    public static attribute _5 = KeyStroke {id: java.awt.event.KeyEvent.VK_5 };
    public static attribute _6 = KeyStroke {id: java.awt.event.KeyEvent.VK_6 };
    public static attribute _7 = KeyStroke {id: java.awt.event.KeyEvent.VK_7 };
    public static attribute _8 = KeyStroke {id: java.awt.event.KeyEvent.VK_8 };
    public static attribute _9 = KeyStroke {id: java.awt.event.KeyEvent.VK_9 };

    /**
     * Constant for the semicolon key, ";"
     */
    public static attribute SEMICOLON = KeyStroke {id: java.awt.event.KeyEvent.VK_SEMICOLON };

    /**
     * Constant for the equals key, "="
     */
    public static attribute EQUALS = KeyStroke {id: java.awt.event.KeyEvent.VK_EQUALS };

    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */
    public static attribute A = KeyStroke {id: java.awt.event.KeyEvent.VK_A, keyChar:"a" };
    public static attribute B = KeyStroke {id: java.awt.event.KeyEvent.VK_B, keyChar:"b"  };
    public static attribute C = KeyStroke {id: java.awt.event.KeyEvent.VK_C, keyChar:"c" };
    public static attribute D = KeyStroke {id: java.awt.event.KeyEvent.VK_D, keyChar:"d" };
    public static attribute E = KeyStroke {id: java.awt.event.KeyEvent.VK_E , keyChar:"e"};
    public static attribute F = KeyStroke {id: java.awt.event.KeyEvent.VK_F, keyChar:"f" };
    public static attribute G = KeyStroke {id: java.awt.event.KeyEvent.VK_G , keyChar:"g"};
    public static attribute H = KeyStroke {id: java.awt.event.KeyEvent.VK_H, keyChar:"h" };
    public static attribute I = KeyStroke {id: java.awt.event.KeyEvent.VK_I , keyChar:"i"};
    public static attribute J = KeyStroke {id: java.awt.event.KeyEvent.VK_J , keyChar:"j"};
    public static attribute K = KeyStroke {id: java.awt.event.KeyEvent.VK_K , keyChar:"k"};
    public static attribute L = KeyStroke {id: java.awt.event.KeyEvent.VK_L , keyChar:"l"};
    public static attribute M = KeyStroke {id: java.awt.event.KeyEvent.VK_M , keyChar:"m"};
    public static attribute N = KeyStroke {id: java.awt.event.KeyEvent.VK_N , keyChar:"n"};
    public static attribute O = KeyStroke {id: java.awt.event.KeyEvent.VK_O , keyChar:"o"};
    public static attribute P = KeyStroke {id: java.awt.event.KeyEvent.VK_P , keyChar:"p"};
    public static attribute Q = KeyStroke {id: java.awt.event.KeyEvent.VK_Q , keyChar:"q"};
    public static attribute R = KeyStroke {id: java.awt.event.KeyEvent.VK_R , keyChar:"r"};
    public static attribute S = KeyStroke {id: java.awt.event.KeyEvent.VK_S , keyChar:"s"};
    public static attribute T = KeyStroke {id: java.awt.event.KeyEvent.VK_T , keyChar:"t"};
    public static attribute U = KeyStroke {id: java.awt.event.KeyEvent.VK_U , keyChar:"u"};
    public static attribute V = KeyStroke {id: java.awt.event.KeyEvent.VK_V , keyChar:"v"};
    public static attribute W = KeyStroke {id: java.awt.event.KeyEvent.VK_W , keyChar:"w"};
    public static attribute X = KeyStroke {id: java.awt.event.KeyEvent.VK_X , keyChar:"x"};
    public static attribute Y = KeyStroke {id: java.awt.event.KeyEvent.VK_Y , keyChar:"y"};
    public static attribute Z = KeyStroke {id: java.awt.event.KeyEvent.VK_Z , keyChar:"z"};

    /**
     * Constant for the open bracket key, "["
     */
    public static attribute OPEN_BRACKET = KeyStroke {id: java.awt.event.KeyEvent.VK_OPEN_BRACKET };

    /**
     * Constant for the back slash key, "\"
     */
    public static attribute BACK_SLASH = KeyStroke {id: java.awt.event.KeyEvent.VK_BACK_SLASH };

    /**
     * Constant for the close bracket key, "]"
     */
    public static attribute CLOSE_BRACKET = KeyStroke {id: java.awt.event.KeyEvent.VK_CLOSE_BRACKET };

    public static attribute NUMPAD0 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD0 };
    public static attribute NUMPAD1 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD1 };
    public static attribute NUMPAD2 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD2 };
    public static attribute NUMPAD3 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD3 };
    public static attribute NUMPAD4 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD4 };
    public static attribute NUMPAD5 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD5 };
    public static attribute NUMPAD6 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD6 };
    public static attribute NUMPAD7 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD7 };
    public static attribute NUMPAD8 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD8 };
    public static attribute NUMPAD9 = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMPAD9 };
    public static attribute MULTIPLY = KeyStroke {id: java.awt.event.KeyEvent.VK_MULTIPLY };
    public static attribute ADD = KeyStroke {id: java.awt.event.KeyEvent.VK_ADD };

    /** 
     * This constant is obsolete, and is included only for backwards
     * compatibility.
     * @see java.awt.event.KeyEvent#VK_SEPARATOR
     */
    public static attribute SEPARATER = KeyStroke {id: java.awt.event.KeyEvent.VK_SEPARATER };

    /** 
     * Constant for the Numpad Separator key. 
     * @since 1.4
     */
    public static attribute SEPARATOR = KeyStroke {id: java.awt.event.KeyEvent.VK_SEPARATOR };

    public static attribute SUBTRACT = KeyStroke {id: java.awt.event.KeyEvent.VK_SUBTRACT };
    public static attribute DECIMAL = KeyStroke {id: java.awt.event.KeyEvent.VK_DECIMAL };
    public static attribute DIVIDE = KeyStroke {id: java.awt.event.KeyEvent.VK_DIVIDE };
    public static attribute DELETE = KeyStroke {id: java.awt.event.KeyEvent.VK_DELETE }; /* ASCII DEL */
    public static attribute NUM_LOCK = KeyStroke {id: java.awt.event.KeyEvent.VK_NUM_LOCK };
    public static attribute SCROLL_LOCK = KeyStroke {id: java.awt.event.KeyEvent.VK_SCROLL_LOCK };

    /** Constant for the F1 function key. */
    public static attribute F1 = KeyStroke {id: java.awt.event.KeyEvent.VK_F1 };

    /** Constant for the F2 function key. */
    public static attribute F2 = KeyStroke {id: java.awt.event.KeyEvent.VK_F2 };

    /** Constant for the F3 function key. */
    public static attribute F3 = KeyStroke {id: java.awt.event.KeyEvent.VK_F3 };

    /** Constant for the F4 function key. */
    public static attribute F4 = KeyStroke {id: java.awt.event.KeyEvent.VK_F4 };

    /** Constant for the F5 function key. */
    public static attribute F5 = KeyStroke {id: java.awt.event.KeyEvent.VK_F5 };

    /** Constant for the F6 function key. */
    public static attribute F6 = KeyStroke {id: java.awt.event.KeyEvent.VK_F6 };

    /** Constant for the F7 function key. */
    public static attribute F7 = KeyStroke {id: java.awt.event.KeyEvent.VK_F7 };

    /** Constant for the F8 function key. */
    public static attribute F8 = KeyStroke {id: java.awt.event.KeyEvent.VK_F8 };

    /** Constant for the F9 function key. */
    public static attribute F9 = KeyStroke {id: java.awt.event.KeyEvent.VK_F9};

    /** Constant for the F10 function key. */
    public static attribute F10 = KeyStroke {id: java.awt.event.KeyEvent.VK_F10 };

    /** Constant for the F11 function key. */
    public static attribute F11 = KeyStroke {id: java.awt.event.KeyEvent.VK_F11 };

    /** Constant for the F12 function key. */
    public static attribute F12 = KeyStroke {id: java.awt.event.KeyEvent.VK_F12 };

    /**
     * Constant for the F13 function key.
     * @since 1.2
     */
    /* F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static attribute F13 = KeyStroke {id: java.awt.event.KeyEvent.VK_F3 };
 
    /**
     * Constant for the F14 function key.
     * @since 1.2
     */
    public static attribute F14 = KeyStroke {id: java.awt.event.KeyEvent.VK_F14 };
 
    /**
     * Constant for the F15 function key.
     * @since 1.2
     */
    public static attribute F15 = KeyStroke {id: java.awt.event.KeyEvent.VK_F15 };
 
    /**
     * Constant for the F16 function key.
     * @since 1.2
     */
    public static attribute F16 = KeyStroke {id: java.awt.event.KeyEvent.VK_F16 };
 
    /**
     * Constant for the F17 function key.
     * @since 1.2
     */
    public static attribute F17 = KeyStroke {id: java.awt.event.KeyEvent.VK_F17 };
 
    /**
     * Constant for the F18 function key.
     * @since 1.2
     */
    public static attribute F18 = KeyStroke {id: java.awt.event.KeyEvent.VK_F18 };
 
    /**
     * Constant for the F19 function key.
     * @since 1.2
     */
    public static attribute F19 = KeyStroke {id: java.awt.event.KeyEvent.VK_F19 };
 
    /**
     * Constant for the F20 function key.
     * @since 1.2
     */
    public static attribute F20 = KeyStroke {id: java.awt.event.KeyEvent.VK_F20 };
 
    /**
     * Constant for the F21 function key.
     * @since 1.2
     */
    public static attribute F21 = KeyStroke {id: java.awt.event.KeyEvent.VK_F21 };
 
    /**
     * Constant for the F22 function key.
     * @since 1.2
     */
    public static attribute F22 = KeyStroke {id: java.awt.event.KeyEvent.VK_F22 };
 
    /**
     * Constant for the F23 function key.
     * @since 1.2
     */
    public static attribute F23 = KeyStroke {id: java.awt.event.KeyEvent.VK_F23 };
 
    /**
     * Constant for the F24 function key.
     * @since 1.2
     */
    public static attribute F24 = KeyStroke {id: java.awt.event.KeyEvent.VK_F24 };
 
    public static attribute PRINTSCREEN = KeyStroke {id: java.awt.event.KeyEvent.VK_PRINTSCREEN };
    public static attribute INSERT = KeyStroke {id: java.awt.event.KeyEvent.VK_INSERT };
    public static attribute HELP = KeyStroke {id: java.awt.event.KeyEvent.VK_HELP };
    public static attribute META = KeyStroke {id: java.awt.event.KeyEvent.VK_META };

    public static attribute BACK_QUOTE = KeyStroke {id: java.awt.event.KeyEvent.VK_BACK_QUOTE };
    public static attribute QUOTE = KeyStroke {id: java.awt.event.KeyEvent.VK_QUOTE };

    /**
     * Constant for the numeric keypad <b>up</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_UP
     * @since 1.2
     */
    public static attribute KP_UP = KeyStroke {id: java.awt.event.KeyEvent.VK_KP_UP };

    /**
     * Constant for the numeric keypad <b>down</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_DOWN
     * @since 1.2
     */
    public static attribute KP_DOWN = KeyStroke {id: java.awt.event.KeyEvent.VK_KP_DOWN };

    /**
     * Constant for the numeric keypad <b>left</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_LEFT
     * @since 1.2
     */
    public static attribute KP_LEFT = KeyStroke {id: java.awt.event.KeyEvent.VK_KP_LEFT };

    /**
     * Constant for the numeric keypad <b>right</b> arrow key.
     * @see java.awt.event.KeyEvent#VK_RIGHT
     * @since 1.2
     */
    public static attribute KP_RIGHT = KeyStroke {id: java.awt.event.KeyEvent.VK_KP_RIGHT };
    
    /** @since 1.2 */
    public static attribute AMPERSAND = KeyStroke {id: java.awt.event.KeyEvent.VK_AMPERSAND };
    /** @since 1.2 */
    public static attribute ASTERISK = KeyStroke {id: java.awt.event.KeyEvent.VK_ASTERISK };
    /** @since 1.2 */
    public static attribute QUOTEDBL = KeyStroke {id: java.awt.event.KeyEvent.VK_QUOTEDBL };
    /** @since 1.2 */
    public static attribute LESS = KeyStroke {id: java.awt.event.KeyEvent.VK_LESS };

    /** @since 1.2 */
    public static attribute GREATER = KeyStroke {id: java.awt.event.KeyEvent.VK_GREATER };
    /** @since 1.2 */
    public static attribute BRACELEFT = KeyStroke {id: java.awt.event.KeyEvent.VK_BRACELEFT };
    /** @since 1.2 */
    public static attribute BRACERIGHT = KeyStroke {id: java.awt.event.KeyEvent.VK_BRACERIGHT };

    /**
     * Constant for the "@" key.
     * @since 1.2
     */
    public static attribute AT = KeyStroke {id: java.awt.event.KeyEvent.VK_AT };
 
    /**
     * Constant for the ":" key.
     * @since 1.2
     */
    public static attribute COLON = KeyStroke {id: java.awt.event.KeyEvent.VK_COLON };
 
    /**
     * Constant for the "^" key.
     * @since 1.2
     */
    public static attribute CIRCUMFLEX = KeyStroke {id: java.awt.event.KeyEvent.VK_CIRCUMFLEX };
 
    /**
     * Constant for the "$" key.
     * @since 1.2
     */
    public static attribute DOLLAR = KeyStroke {id: java.awt.event.KeyEvent.VK_DOLLAR };
 
    /**
     * Constant for the Euro currency sign key.
     * @since 1.2
     */
    public static attribute EURO_SIGN = KeyStroke {id: java.awt.event.KeyEvent.VK_EURO_SIGN };
 
    /**
     * Constant for the "!" key.
     * @since 1.2
     */
    public static attribute EXCLAMATION_MARK = KeyStroke {id: java.awt.event.KeyEvent.VK_EXCLAMATION_MARK };
 
    /**
     * Constant for the inverted exclamation mark key.
     * @since 1.2
     */
    public static attribute INVERTED_EXCLAMATION_MARK = KeyStroke {id: java.awt.event.KeyEvent.VK_INVERTED_EXCLAMATION_MARK };
 
    /**
     * Constant for the "(" key.
     * @since 1.2
     */
    public static attribute LEFT_PARENTHESIS = KeyStroke {id: java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS };
 
    /**
     * Constant for the "#" key.
     * @since 1.2
     */
    public static attribute NUMBER_SIGN = KeyStroke {id: java.awt.event.KeyEvent.VK_NUMBER_SIGN };
 
    /**
     * Constant for the "+" key.
     * @since 1.2
     */
    public static attribute PLUS = KeyStroke {id: java.awt.event.KeyEvent.VK_PLUS };
 
    /**
     * Constant for the ")" key.
     * @since 1.2
     */
    public static attribute RIGHT_PARENTHESIS = KeyStroke {id: java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS };
 
    /**
     * Constant for the "_" key.
     * @since 1.2
     */
    public static attribute UNDERSCORE = KeyStroke {id: java.awt.event.KeyEvent.VK_UNDERSCORE };
 
    /**
     * Constant for the Microsoft Windows "Windows" key.
     * It is used for both the left and right version of the key.  
     * @see java.awt.event.KeyEvent#VK_WINDOWS
     * @since 1.5
     */
    public static attribute WINDOWS = KeyStroke {id: java.awt.event.KeyEvent.VK_WINDOWS };
 
    /**
     * Constant for the Microsoft Windows Context Menu key.
     * @since 1.5
     */
    public static attribute CONTEXT_MENU = KeyStroke {id: java.awt.event.KeyEvent.VK_CONTEXT_MENU };
 
    /* for input method support on Asian Keyboards */

    /* not clear what this means - listed in Microsoft Windows API */
    public static attribute FINAL = KeyStroke {id: java.awt.event.KeyEvent.VK_FINAL };
    
    /** Constant for the Convert function key. */
    /* Japanese PC 106 keyboard, Japanese Solaris keyboard: henkan */
    public static attribute CONVERT = KeyStroke {id: java.awt.event.KeyEvent.VK_CONVERT };

    /** Constant for the Don't Convert function key. */
    /* Japanese PC 106 keyboard: muhenkan */
    public static attribute NONCONVERT = KeyStroke {id: java.awt.event.KeyEvent.VK_NONCONVERT };
    
    /** Constant for the Accept or Commit function key. */
    /* Japanese Solaris keyboard: kakutei */
    public static attribute ACCEPT = KeyStroke {id: java.awt.event.KeyEvent.VK_ACCEPT };

    /* not clear what this means - listed in Microsoft Windows API */
    public static attribute MODECHANGE = KeyStroke {id: java.awt.event.KeyEvent.VK_MODECHANGE };

    /* replaced by VK_KANA_LOCK for Microsoft Windows and Solaris; 
       might still be used on other platforms */
    public static attribute KANA = KeyStroke {id: java.awt.event.KeyEvent.VK_KANA };

    /* replaced by VK_INPUT_METHOD_ON_OFF for Microsoft Windows and Solaris; 
       might still be used for other platforms */
    public static attribute KANJI = KeyStroke {id: java.awt.event.KeyEvent.VK_KANJI };

    /**
     * Constant for the Alphanumeric function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: eisuu */
    public static attribute ALPHANUMERIC = KeyStroke {id: java.awt.event.KeyEvent.VK_ALPHANUMERIC };
 
    /**
     * Constant for the Katakana function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: katakana */
    public static attribute KATAKANA = KeyStroke {id: java.awt.event.KeyEvent.VK_KATAKANA };
 
    /**
     * Constant for the Hiragana function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: hiragana */
    public static attribute HIRAGANA = KeyStroke {id: java.awt.event.KeyEvent.VK_HIRAGANA };
 
    /**
     * Constant for the Full-Width Characters function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: zenkaku */
    public static attribute FULL_WIDTH = KeyStroke {id: java.awt.event.KeyEvent.VK_FULL_WIDTH };
 
    /**
     * Constant for the Half-Width Characters function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: hankaku */
    public static attribute HALF_WIDTH = KeyStroke {id: java.awt.event.KeyEvent.VK_HALF_WIDTH };
 
    /**
     * Constant for the Roman Characters function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard: roumaji */
    public static attribute ROMAN_CHARACTERS = KeyStroke {id: java.awt.event.KeyEvent.VK_ROMAN_CHARACTERS };
 
    /**
     * Constant for the All Candidates function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - VK_CONVERT + ALT: zenkouho */
    public static attribute ALL_CANDIDATES = KeyStroke {id: java.awt.event.KeyEvent.VK_ALL_CANDIDATES };
 
    /**
     * Constant for the Previous Candidate function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - VK_CONVERT + SHIFT: maekouho */
    public static attribute PREVIOUS_CANDIDATE = KeyStroke {id: java.awt.event.KeyEvent.VK_PREVIOUS_CANDIDATE };
 
    /**
     * Constant for the Code Input function key.
     * @since 1.2
     */
    /* Japanese PC 106 keyboard - VK_ALPHANUMERIC + ALT: kanji bangou */
    public static attribute CODE_INPUT = KeyStroke {id: java.awt.event.KeyEvent.VK_CODE_INPUT };
 
    /**
     * Constant for the Japanese-Katakana function key.
     * This key switches to a Japanese input method and selects its Katakana input mode.
     * @since 1.2
     */
    /* Japanese Macintosh keyboard - VK_JAPANESE_HIRAGANA + SHIFT */
    public static attribute JAPANESE_KATAKANA = KeyStroke {id: java.awt.event.KeyEvent.VK_JAPANESE_KATAKANA };
 
    /**
     * Constant for the Japanese-Hiragana function key.
     * This key switches to a Japanese input method and selects its Hiragana input mode.
     * @since 1.2
     */
    /* Japanese Macintosh keyboard */
    public static attribute JAPANESE_HIRAGANA = KeyStroke {id: java.awt.event.KeyEvent.VK_JAPANESE_HIRAGANA };
 
    /**
     * Constant for the Japanese-Roman function key.
     * This key switches to a Japanese input method and selects its Roman-Direct input mode.
     * @since 1.2
     */
    /* Japanese Macintosh keyboard */
    public static attribute JAPANESE_ROMAN = KeyStroke {id: java.awt.event.KeyEvent.VK_JAPANESE_ROMAN };

    /**
     * Constant for the locking Kana function key.
     * This key locks the keyboard into a Kana layout.
     * @since 1.3
     */
    /* Japanese PC 106 keyboard with special Windows driver - eisuu + Control; Japanese Solaris keyboard: kana */
    public static attribute KANA_LOCK = KeyStroke {id: java.awt.event.KeyEvent.VK_KANA_LOCK };

    /**
     * Constant for the input method on/off key.
     * @since 1.3
     */
    /* Japanese PC 106 keyboard: kanji. Japanese Solaris keyboard: nihongo */
    public static attribute INPUT_METHOD_ON_OFF = KeyStroke {id: java.awt.event.KeyEvent.VK_INPUT_METHOD_ON_OFF };

    /* for Sun keyboards */
    /** @since 1.2 */
    public static attribute CUT = KeyStroke {id: java.awt.event.KeyEvent.VK_CUT };
    /** @since 1.2 */
    public static attribute COPY = KeyStroke {id: java.awt.event.KeyEvent.VK_COPY };
    /** @since 1.2 */
    public static attribute PASTE = KeyStroke {id: java.awt.event.KeyEvent.VK_PASTE };
    /** @since 1.2 */
    public static attribute UNDO = KeyStroke {id: java.awt.event.KeyEvent.VK_UNDO };
    /** @since 1.2 */
    public static attribute AGAIN = KeyStroke {id: java.awt.event.KeyEvent.VK_AGAIN };
    /** @since 1.2 */
    public static attribute FIND = KeyStroke {id: java.awt.event.KeyEvent.VK_FIND };
    /** @since 1.2 */
    public static attribute PROPS = KeyStroke {id: java.awt.event.KeyEvent.VK_PROPS };
    /** @since 1.2 */
    public static attribute STOP = KeyStroke {id: java.awt.event.KeyEvent.VK_STOP };
    
    /**
     * Constant for the Compose function key.
     * @since 1.2
     */
    public static attribute COMPOSE = KeyStroke {id: java.awt.event.KeyEvent.VK_COMPOSE };
 
    /**
     * Constant for the AltGraph function key.
     * @since 1.2
     */
    public static attribute ALT_GRAPH = KeyStroke {id: java.awt.event.KeyEvent.VK_ALT_GRAPH };

    /**
     * Constant for the Begin key.
     * @since 1.5
     */
    public static attribute BEGIN = KeyStroke {id: java.awt.event.KeyEvent.VK_BEGIN };

    /**
     * This value is used to indicate that the keyCode is unknown.
     * KEY_TYPED events do not have a keyCode value; this value 
     * is used instead.  
     */
    public static attribute UNDEFINED = KeyStroke {id: java.awt.event.KeyEvent.VK_UNDEFINED };
    
}
