package org.jphototagger.lib.thirdparty;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jphototagger.lib.swing.DialogExt;
import org.jphototagger.lib.swing.FrameExt;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.UiFactory;

/**
 * Code geringfügig modifiziert Elmar Baumann:
 * - Konstruktoren private
 * - Buttons für Monatswechsel mit Icons
 * - Icons für die Titelleiste können wahlweise gesetzt werden
 * - Strings aus Bundle
 * - main() entfernt
 *
 * URL: http://groups.google.de/group/comp.lang.java.gui/browse_thread/thread/d69c86b8831c787f/1674081f7286a171?hl=de&lnk=st&q=#1674081f7286a171
 *
 *  JDateChooser
 *
 *  Written by: Knute Johnson
 *
 *  Date        Version     Modification
 *  ---------   -------
 * ---------------------------------------------------
 *  04 jun 05   01.00       incept
 *
 *  Constructor Summary
 *      JDateChooser()
 *          Creates a new JDateChooser with today's date displayed
 *      JDateChooser(GregorianCalendar gc)
 *          Creates a new JDateChooser with the specified date displayed
 *
 *  Method Summary
 *      GregorianCalendar getCalendar()
 *          Gets the selected date
 *      void setCalendar(GregorianCalendar gc)
 *          Sets and display's the specified date
 *      static GregorianCalendar showDialog(Component comp)
 *          Displays a JDateChooser in a modal JDialog and returns the
 *           selected date or null if dismissed.
 *      static GregorianCalendar showDialog(Component comp,GregorianCalendar gc)
 *          Displays a JDateChooser in a modal JDialog with the specified date
 *           and returns the selected date or null if dismissed.
 */
/**
 * Date Chooser DialogExt.
 *
 * @author Knute Johnson, Elmar Baumann
 */
public final class DateChooserDialog extends JComponent {

    private static final long serialVersionUID = 1L;
    private final String[] dayStr;
    private final String[] monthStr;
    private final JButton previousButton, nextButton;
    private final JLabel[] dayOfWeekLabels = new JLabel[7];
    private final JLabel[] dayOfMonthLabels = new JLabel[42];
    private final JLabel monthYearLabel;
    private final Locale locale;
    private GregorianCalendar gc;
    private int thisYear, thisMonth, today;
    private int selectedDay;
    private static DialogExt dialog;
    private static GregorianCalendar retcod;

    private DateChooserDialog() {
        this(new GregorianCalendar(), Locale.getDefault());
    }

    private DateChooserDialog(GregorianCalendar calendar, Locale locale) {
        gc = calendar;
        thisYear = gc.get(Calendar.YEAR);
        thisMonth = gc.get(Calendar.MONTH);
        today = selectedDay = gc.get(Calendar.DAY_OF_MONTH);
        this.locale = locale;

        DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);

        monthStr = dfs.getMonths();
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = c.gridy = 0;
        c.insets = UiFactory.insets(2, 2, 2, 2);
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        previousButton = UiFactory.button();
        previousButton.setIcon(org.jphototagger.resources.Icons.getIcon("icon_datechooser_prev.png"));
        previousButton.setBorder(BorderFactory.createEmptyBorder());
        previousButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                gc.add(Calendar.MONTH, -1);

                if (selectedDay > gc.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    selectedDay = 1;
                }

                drawCalendar();
            }
        });
        add(previousButton, c);
        ++c.gridx;
        c.anchor = GridBagConstraints.CENTER;
        monthYearLabel = UiFactory.label("           ", JLabel.CENTER);
        add(monthYearLabel, c);
        ++c.gridx;
        c.anchor = GridBagConstraints.EAST;
        nextButton = UiFactory.button();
        nextButton.setIcon(org.jphototagger.resources.Icons.getIcon("icon_datechooser_next.png"));
        nextButton.setBorder(BorderFactory.createEmptyBorder());
        nextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                gc.add(Calendar.MONTH, 1);

                if (selectedDay > gc.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    selectedDay = 1;
                }

                drawCalendar();
            }
        });
        add(nextButton, c);

        MouseListener ml = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                JLabel dayLabel = (JLabel) me.getSource();
                String str = dayLabel.getText();

                try {
                    int num = Integer.parseInt(str);

                    gc.set(Calendar.DAY_OF_MONTH, num);
                    selectedDay = num;
                    drawCalendar();
                } catch (Throwable t) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, t);
                }
            }
        };

        c.gridx = 0;
        ++c.gridy;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.CENTER;

        JPanel panel = UiFactory.panel(new GridLayout(7, 7, UiFactory.scale(1), UiFactory.scale(1)));

        dayStr = dfs.getShortWeekdays();

        int firstDay = gc.getFirstDayOfWeek();

        for (int i = 0; i < 7; i++) {
            int x = firstDay + i;

            if (i == 6) {
                x = (firstDay == Calendar.MONDAY)
                        ? Calendar.SUNDAY
                        : Calendar.SATURDAY;
            }

            dayOfWeekLabels[i] = UiFactory.label(dayStr[x].toUpperCase(), JLabel.CENTER);
            panel.add(dayOfWeekLabels[i]);
        }

        for (int i = 0; i < dayOfMonthLabels.length; i++) {
            dayOfMonthLabels[i] = UiFactory.label("  ", JLabel.CENTER);
            dayOfMonthLabels[i].setOpaque(true);
            dayOfMonthLabels[i].addMouseListener(ml);
            panel.add(dayOfMonthLabels[i]);
        }

        drawCalendar();
        add(panel, c);
    }

    private void drawCalendar() {
        int month = gc.get(Calendar.MONTH);
        int year = gc.get(Calendar.YEAR);

        monthYearLabel.setText(monthStr[month].toUpperCase() + " " + Integer.toString(year));
        gc.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfMonth = gc.get(Calendar.DAY_OF_WEEK);

        if (gc.getFirstDayOfWeek() == Calendar.MONDAY) {
            --firstDayOfMonth;
        }

        gc.set(Calendar.DAY_OF_MONTH, selectedDay);

        int day = 1;

        for (int i = 0; i < 42; i++) {
            if ((i >= (firstDayOfMonth - 1))
                    && (i < (gc.getActualMaximum(Calendar.DAY_OF_MONTH) + firstDayOfMonth - 1))) {
                dayOfMonthLabels[i].setText(Integer.toString(day));

                if ((day == today) && (month == thisMonth) && (year == thisYear)) {
                    dayOfMonthLabels[i].setForeground(Color.RED);
                } else {
                    dayOfMonthLabels[i].setForeground(Color.BLACK);
                }

                if (day == selectedDay) {
                    dayOfMonthLabels[i].setBackground(new Color(0xa0a0a0));
                } else {
                    dayOfMonthLabels[i].setBackground(Color.WHITE);
                }

                ++day;
            } else {
                dayOfMonthLabels[i].setText("  ");
                dayOfMonthLabels[i].setBackground(new Color(0xe0e0e0));
            }
        }
    }

    public GregorianCalendar getCalendar() {
        return new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), selectedDay);
    }

    public void setCalendar(GregorianCalendar calendar) {
        if (calendar == null) {
            throw new NullPointerException("calendar == null");
        }

        gc = calendar;
        drawCalendar();
    }

    /**
     * Zeigt den Datumsauswahldialog an, benutzt die Default-Locale.
     *
     * @param comp  Komponente, die den DialogExt nutzt
     * @param icons Icons oder null, wenn keine gesetzt werden sollen
     * @return      Kalender mit ausgewähltem Datum oder null, wenn keines
     *              ausgewählt wurde
     */
    public static GregorianCalendar showDialog(Component comp, java.util.List<? extends Image> icons) {
        return showDialog(comp, new GregorianCalendar(), Locale.getDefault(), icons);
    }

    /**
     * Zeigt den Datumsauswahldialog an.
     *
     * @param comp     Komponente, die den DialogExt nutzt
     * @param calendar Kalender mit Startdatum
     * @param locale   Locale
     * @param icons    Icons oder null, wenn keine gesetzt werden sollen
     * @return         Kalender mit ausgewähltem Datum oder null, wenn keines
     *                 ausgewählt wurde
     */
    public static GregorianCalendar showDialog(Component comp, GregorianCalendar calendar, Locale locale,
            java.util.List<? extends Image> icons) {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.insets = UiFactory.insets(2, 2, 2, 2);
        c.gridwidth = 2;

        FrameExt f = new FrameExt();

        dialog = new DialogExt(f, Bundle.getString(DateChooserDialog.class, "DateChooserDialog.title"), true);
        dialog.setPreferencesKey("DateChooserDialog");

        if (icons != null) {
            dialog.setIconImages(icons);
        }

        dialog.setLayout(new GridBagLayout());
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                retcod = null;
            }
        });

        final DateChooserDialog dc = new DateChooserDialog(calendar, locale);

        dialog.add(dc, c);
        ++c.gridy;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;

        JButton okButton = UiFactory.button(Bundle.getString(DateChooserDialog.class, "DateChooserDialog.okButton"));

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                retcod = dc.getCalendar();
                dialog.dispose();
            }
        });
        dialog.add(okButton, c);
        ++c.gridx;
        c.anchor = GridBagConstraints.EAST;

        JButton cancelButton = UiFactory.button(Bundle.getString(DateChooserDialog.class, "DateChooserDialog.cancelButton"));

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                retcod = null;
                dialog.dispose();
            }
        });
        dialog.add(cancelButton, c);
        dialog.pack();
        dialog.setLocationRelativeTo(comp);
        dialog.setVisible(true);

        return retcod;
    }

    @Override
    public void setFont(Font font) {
        if (font == null) {
            throw new NullPointerException("font == null");
        }

        previousButton.setFont(font);
        nextButton.setFont(font);
        for (JLabel dayOfWeekLabel : dayOfWeekLabels) {
            dayOfWeekLabel.setFont(font);
        }
        for (JLabel dayOfMonthLabel : dayOfMonthLabels) {
            dayOfMonthLabel.setFont(font);
        }

        monthYearLabel.setFont(font);
    }
}
