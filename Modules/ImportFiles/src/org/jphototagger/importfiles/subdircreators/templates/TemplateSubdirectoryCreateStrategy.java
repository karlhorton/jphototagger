package org.jphototagger.importfiles.subdircreators.templates;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import org.jphototagger.api.file.SubdirectoryCreateStrategy;
import org.jphototagger.importfiles.NameUtil;
import org.jphototagger.lib.util.StringUtil;

/**
 * Strategy for creating a subdirectory based on templates.
 *
 * @author Elmar Baumann
 */
public final class TemplateSubdirectoryCreateStrategy implements SubdirectoryCreateStrategy {

    /**
     * String which will be repaced through the file date's year (EXIF, if
     * present, else the file's last modification time).
     */
    public static final String FILE_DATE_YEAR = "{YYYY}";
    /**
     * String which will be repaced through the file date's month (EXIF, if
     * present, else the file's last modification time).
     */
    public static final String FILE_DATE_MONTH = "{MM}";
    /**
     * String which will be repaced through the file date's day (EXIF, if
     * present, else the file's last modification time).
     */
    public static final String FILE_DATE_DAY = "{DD}";

    private final SubdirectoryTemplate template;

    public TemplateSubdirectoryCreateStrategy(SubdirectoryTemplate template) {
        this.template = Objects.requireNonNull(template, "template == null");
    }

    @Override
    public String suggestSubdirectoryName(File file) {
        String templateString = StringUtil.emptyStringIfNull(template.getTemplate());

        if (!StringUtil.hasContent(templateString)) {
            return "";
        }

        String dateString = NameUtil.getDateString(file); // string in the format "YYYY-MM-dd"
        String[] dateToken = dateString.split("-");

        return replaceDate(templateString, dateToken);
    }

    /**
     * @param templateString
     * @param dateToken      string in the format "YYYY-MM-dd"
     *
     * @return
     */
    private static String replaceDate(String templateString, String[] dateToken) {
        String name = templateString.replace(FILE_DATE_YEAR, dateToken[0]);
        name = name.replace(FILE_DATE_MONTH, dateToken[1]);
        name = name.replace(FILE_DATE_DAY, dateToken[2]);
        return name;
    }

    @Override
    public String getDisplayName() {
        return template.getDisplayName();
    }

    @Override
    public int getPosition() {
        return template.getPosition();
    }

    @Override
    public boolean isUserDefined() {
        return true;
    }

    public static String getExample(String templateString) {
        Objects.requireNonNull(templateString, "template == null");

        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        Date today = new Date();
        String[] dateToken = df.format(today).split("-");

        return replaceDate(templateString, dateToken);
    }
}
