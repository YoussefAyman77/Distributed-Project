package client.ui;

import java.awt.Color;
import java.awt.Font;

/** Warm harvest boutique palette for the grocery client. */
public final class UiTheme {
    /* Brand */
    public static final Color PRIMARY_DEEP = new Color(124, 45, 18);
    public static final Color PRIMARY = new Color(194, 65, 12);
    public static final Color PRIMARY_SOFT = new Color(234, 88, 12);
    public static final Color ACCENT = new Color(249, 115, 22);
    public static final Color ACCENT_GLOW = new Color(253, 186, 116);
    public static final Color ROSE = new Color(190, 24, 93);
    public static final Color ROSE_SOFT = new Color(251, 207, 232);

    /* Surfaces */
    public static final Color BG_TOP = new Color(255, 247, 237);
    public static final Color BG_BOTTOM = new Color(255, 241, 242);
    public static final Color CARD = Color.WHITE;
    public static final Color SURFACE = new Color(255, 250, 245);
    public static final Color SURFACE_WARM = new Color(255, 237, 213);
    public static final Color HIGHLIGHT = new Color(254, 243, 199);

    /* Borders & shadows */
    public static final Color BORDER = new Color(254, 215, 170);
    public static final Color BORDER_SOFT = new Color(255, 228, 196);
    public static final Color SHADOW = new Color(124, 45, 18, 42);
    public static final Color SHADOW_STRONG = new Color(124, 45, 18, 80);

    /* Text */
    public static final Color TEXT_DARK = new Color(28, 25, 23);
    public static final Color TEXT_BODY = new Color(68, 64, 60);
    public static final Color TEXT_MUTED = new Color(120, 113, 108);
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;
    public static final Color TEXT_CREAM = new Color(255, 237, 213);

    /* Status */
    public static final Color SUCCESS = new Color(21, 128, 61);
    public static final Color SUCCESS_SOFT = new Color(220, 252, 231);
    public static final Color ERROR = new Color(220, 38, 38);
    public static final Color WARNING = new Color(180, 83, 9);

    /* Legacy aliases used across UI kit */
    public static final Color FOREST = PRIMARY_DEEP;
    public static final Color FOREST_LIGHT = PRIMARY;
    public static final Color GREEN = ACCENT;
    public static final Color GREEN_BRIGHT = ACCENT_GLOW;
    public static final Color MINT = TEXT_CREAM;
    public static final Color MINT_SOFT = SURFACE_WARM;
    public static final Color CREAM = BG_TOP;

    public static final Font FONT_DISPLAY = new Font("Segoe UI", Font.BOLD, 34);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEAD = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_EMOJI_LARGE = new Font("Segoe UI Emoji", Font.PLAIN, 48);
    public static final Font FONT_EMOJI_HERO = new Font("Segoe UI Emoji", Font.PLAIN, 64);
    public static final Font FONT_PRICE = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_TOTAL = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_RECEIPT_TOTAL = new Font("Segoe UI", Font.BOLD, 32);

    private UiTheme() {
    }
}
