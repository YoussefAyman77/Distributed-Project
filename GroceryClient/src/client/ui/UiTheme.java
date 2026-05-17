package client.ui;

import java.awt.Color;
import java.awt.Font;

/** Premium Fresh Market palette — deep emerald forest with warm golden accents. */
public final class UiTheme {

    /* ── Brand: Emerald Green ─────────────────────────────────────────── */
    public static final Color PRIMARY_DEEP  = new Color(21, 128, 61);   // green-700
    public static final Color PRIMARY       = new Color(34, 197, 94);   // green-500
    public static final Color PRIMARY_SOFT  = new Color(74, 222, 128);  // green-400

    /* ── Accent: Warm Amber / Gold ────────────────────────────────────── */
    public static final Color ACCENT        = new Color(245, 158, 11);  // amber-500
    public static final Color ACCENT_GLOW   = new Color(251, 191, 36);  // amber-400
    public static final Color ROSE          = new Color(251, 113, 133); // rose-400 (keep for blobs)
    public static final Color ROSE_SOFT     = new Color(255, 228, 230);

    /* ── Surfaces: Deep Forest ────────────────────────────────────────── */
    public static final Color BG_TOP        = new Color(10,  20, 14);   // near-black green
    public static final Color BG_BOTTOM     = new Color(18,  38, 24);   // deep forest
    public static final Color CARD          = new Color(22,  46, 30);   // card surface
    public static final Color SURFACE       = new Color(12,  28, 17);   // deeper surface
    public static final Color SURFACE_WARM  = new Color(30,  62, 40);   // warm surface
    public static final Color HIGHLIGHT     = new Color(18,  52, 26);   // subtle green tint

    /* ── Borders & Shadows ────────────────────────────────────────────── */
    public static final Color BORDER        = new Color(40,  84, 52);
    public static final Color BORDER_SOFT   = new Color(24,  58, 34);
    public static final Color SHADOW        = new Color(0, 0, 0, 140);
    public static final Color SHADOW_STRONG = new Color(0, 0, 0, 200);

    /* ── Text ─────────────────────────────────────────────────────────── */
    public static final Color TEXT_DARK     = new Color(240, 253, 244); // green-50 (near white)
    public static final Color TEXT_BODY     = new Color(203, 213, 225); // slate-300
    public static final Color TEXT_MUTED    = new Color(148, 163, 184); // slate-400
    public static final Color TEXT_ON_PRIMARY = new Color(255, 255, 255);
    public static final Color TEXT_CREAM    = new Color(220, 252, 231); // green-100

    /* ── Status ───────────────────────────────────────────────────────── */
    public static final Color SUCCESS       = new Color(34, 197, 94);
    public static final Color SUCCESS_SOFT  = new Color(20,  83, 45);
    public static final Color ERROR         = new Color(239,  68, 68);
    public static final Color WARNING       = new Color(245, 158, 11);

    /* ── Legacy aliases (keep other files compiling) ──────────────────── */
    public static final Color FOREST        = PRIMARY_DEEP;
    public static final Color FOREST_LIGHT  = PRIMARY;
    public static final Color GREEN         = PRIMARY;
    public static final Color GREEN_BRIGHT  = PRIMARY_SOFT;
    public static final Color MINT          = TEXT_CREAM;
    public static final Color MINT_SOFT     = SURFACE_WARM;
    public static final Color CREAM         = BG_TOP;

    /* ── Typography ───────────────────────────────────────────────────── */
    public static final Font FONT_DISPLAY       = new Font("Segoe UI", Font.BOLD, 34);
    public static final Font FONT_TITLE         = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_HEADING       = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEAD       = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY          = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD     = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_CAPTION       = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_EMOJI_LARGE   = new Font("Segoe UI Emoji", Font.PLAIN, 48);
    public static final Font FONT_EMOJI_HERO    = new Font("Segoe UI Emoji", Font.PLAIN, 64);
    public static final Font FONT_PRICE         = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_TOTAL         = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_RECEIPT_TOTAL = new Font("Segoe UI", Font.BOLD, 32);

    private UiTheme() {}
}
