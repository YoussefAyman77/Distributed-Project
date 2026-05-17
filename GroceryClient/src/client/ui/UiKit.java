package client.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/** Reusable polished Swing primitives. */
public final class UiKit {

    private UiKit() {
    }

    public static JPanel gradientBackground(LayoutManager layout, Color start, Color end) {
        return new GradientPanel(layout, start, end, false);
    }

    public static JPanel gradientCard(LayoutManager layout, Color start, Color end) {
        return new GradientPanel(layout, start, end, true);
    }

    public static JPanel rounded(Color bg, int radius) {
        return new RoundedPanel(radius, bg, 0, false);
    }

    public static JPanel elevatedCard(Color bg, int radius) {
        return new RoundedPanel(radius, bg, 10, true);
    }

    public static JPanel transparent(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    public static JLabel label(String text, Font font, Color color, int align) {
        JLabel l = new JLabel(text, align);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JButton primaryButton(String text, Runnable action) {
        RoundedButton btn = new RoundedButton(text, true);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    public static JButton secondaryButton(String text, Runnable action) {
        RoundedButton btn = new RoundedButton(text, false);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    public static JScrollPane styledScroll(JComponent view) {
        JScrollPane scroll = new JScrollPane(view);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    public static JPanel decorativeBackdrop(LayoutManager layout) {
        return new DecorativePanel(layout);
    }

    public static QuantityStepper quantityStepper(int min, int max, IntConsumer onChange) {
        return new QuantityStepper(min, max, onChange);
    }

    public static void installHoverLift(JComponent component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                component.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    public static void shakeWindow(java.awt.Window window) {
        Point original = window.getLocation();
        for (int i = 0; i < 10; i++) {
            int offset = (i % 2 == 0) ? 10 : -10;
            window.setLocation(original.x + offset, original.y);
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        window.setLocation(original);
    }

    private static class DecorativePanel extends JPanel {
        private DecorativePanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, UiTheme.BG_TOP, getWidth(), getHeight(), UiTheme.BG_BOTTOM));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.14f));
            g2.setColor(UiTheme.ACCENT);
            g2.fillOval(-90, -70, 240, 240);
            g2.fillOval(getWidth() - 160, getHeight() - 200, 280, 280);
            g2.fillOval(getWidth() / 2 - 70, -110, 200, 200);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
            g2.setColor(UiTheme.ROSE);
            g2.fillOval(50, getHeight() - 130, 170, 170);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class GradientPanel extends JPanel {
        private final Color start;
        private final Color end;
        private final boolean rounded;

        private GradientPanel(LayoutManager layout, Color start, Color end, boolean rounded) {
            super(layout);
            this.start = start;
            this.end = end;
            this.rounded = rounded;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            if (rounded) {
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
            } else {
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color background;
        private final int shadowSize;
        private final boolean shadow;

        public RoundedPanel(int radius, Color background, int shadowSize, boolean shadow) {
            this.radius = radius;
            this.background = background;
            this.shadowSize = shadowSize;
            this.shadow = shadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (shadow && shadowSize > 0) {
                g2.setColor(UiTheme.SHADOW);
                g2.fillRoundRect(shadowSize, shadowSize + 2, getWidth() - shadowSize, getHeight() - shadowSize, radius, radius);
            }
            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth() - (shadow ? shadowSize : 0), getHeight() - (shadow ? shadowSize + 2 : 0), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class RoundedButton extends JButton {
        private final boolean primary;
        private boolean hovered;
        private boolean pressed;

        public RoundedButton(String text, boolean primary) {
            super(text);
            this.primary = primary;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setForeground(primary ? Color.WHITE : UiTheme.PRIMARY_DEEP);
            setFont(UiTheme.FONT_SUBHEAD);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    pressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(Math.max(200, fm.stringWidth(getText()) + 56), 52);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int offsetY = pressed ? 2 : 0;
            int w = getWidth();
            int h = getHeight() - offsetY;

            if (primary) {
                Color c1 = hovered ? UiTheme.ACCENT_GLOW : UiTheme.ACCENT;
                Color c2 = hovered ? UiTheme.PRIMARY : UiTheme.PRIMARY_DEEP;
                g2.setPaint(new GradientPaint(0, offsetY, c1, w, h, c2));
                g2.fillRoundRect(0, offsetY, w, h, 24, 24);
            } else {
                g2.setColor(hovered ? UiTheme.SURFACE_WARM : Color.WHITE);
                g2.fillRoundRect(0, offsetY, w, h, 24, 24);
                g2.setColor(UiTheme.BORDER);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, offsetY + 1, w - 2, h - 2, 24, 24);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class QuantityStepper extends JPanel {
        private final JLabel valueLabel = new JLabel("0", SwingConstants.CENTER);
        private int value;
        private final int min;
        private final int max;
        private final IntConsumer onChange;

        public QuantityStepper(int min, int max, IntConsumer onChange) {
            super(new FlowLayout(FlowLayout.CENTER, 0, 0));
            this.min = min;
            this.max = max;
            this.onChange = onChange;
            setOpaque(false);

            JButton minus = stepButton("\u2212");
            JButton plus = stepButton("+");

            valueLabel.setFont(UiTheme.FONT_SUBHEAD);
            valueLabel.setForeground(UiTheme.TEXT_DARK);
            valueLabel.setPreferredSize(new Dimension(44, 40));

            RoundedPanel shell = new RoundedPanel(20, UiTheme.SURFACE, 0, false);
            shell.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 6));
            shell.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UiTheme.BORDER_SOFT, 1),
                    new EmptyBorder(2, 6, 2, 6)));
            shell.add(minus);
            shell.add(valueLabel);
            shell.add(plus);

            minus.addActionListener(e -> setValue(value - 1));
            plus.addActionListener(e -> setValue(value + 1));

            add(shell);
        }

        private JButton stepButton(String symbol) {
            JButton btn = new JButton(symbol);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btn.setForeground(UiTheme.PRIMARY);
            btn.setPreferredSize(new Dimension(36, 36));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(UiTheme.SURFACE_WARM);
                    btn.setOpaque(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setOpaque(false);
                }
            });
            return btn;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int newValue) {
            int clamped = Math.max(min, Math.min(max, newValue));
            if (clamped != value) {
                value = clamped;
                valueLabel.setText(String.valueOf(value));
                onChange.accept(value);
            }
        }
    }

    public static JPanel fieldShell(String labelText, Component field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setOpaque(false);
        wrapper.add(label(labelText, UiTheme.FONT_BODY_BOLD, UiTheme.PRIMARY, SwingConstants.LEFT), BorderLayout.NORTH);

        RoundedPanel shell = new RoundedPanel(22, Color.WHITE, 6, true);
        shell.setLayout(new BorderLayout());
        shell.setBorder(new EmptyBorder(12, 16, 12, 16));
        shell.add(field, BorderLayout.CENTER);
        wrapper.add(shell, BorderLayout.CENTER);
        return wrapper;
    }

    public static void styleInput(javax.swing.JTextField field) {
        field.setFont(UiTheme.FONT_SUBHEAD);
        field.setForeground(UiTheme.TEXT_DARK);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        field.setCaretColor(UiTheme.PRIMARY);
        field.setOpaque(false);
    }

    public static void stylePassword(javax.swing.JPasswordField field) {
        styleInput(field);
    }
}
