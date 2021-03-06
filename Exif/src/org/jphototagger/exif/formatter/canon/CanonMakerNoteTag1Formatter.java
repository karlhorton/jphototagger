package org.jphototagger.exif.formatter.canon;

import java.text.DecimalFormat;
import org.jphototagger.exif.ExifTag;
import org.jphototagger.exif.ExifTags;

//References:
//* http://www.burren.cx/david/canon.html
//* http://www.ozhiker.com/electronics/pjmt/jpeg_info/canon_mn.html
//* http://gvsoft.homedns.org/exif/canon_explain/Canon-PS-S50-111_1115-explain.html

/**
 * @author Elmar Baumann
 */
final class CanonMakerNoteTag1Formatter {
    private static final int CANON_TAG = 1;

    static void add(short[] values, ExifTags exifTags) {
        addFocusMode(values, exifTags);
        addLens(values, exifTags);
    }

    private static void addLens(short[] values, ExifTags exifTags) {
        final int maxValueIndex = 24;

        if (maxValueIndex >= values.length) {
            return;    // No lens information available
        }

        double mmFactor = values[24];

        assert mmFactor > 0;

        if (mmFactor <= 0) {
            return;
        }

        double minF = (double) values[23] / mmFactor;
        double maxF = (double) values[22] / mmFactor;
        boolean fixFocalLength = Math.abs(minF - maxF) < .0000001;
        DecimalFormat df = new DecimalFormat("#.#");
        String lens = fixFocalLength
                      ? df.format(minF) + " mm"
                      : df.format(minF) + "-" + df.format(maxF) + " mm";

        CanonMakerNotes.addTag(exifTags, ExifTag.Properties.MAKER_NOTE_LENS.getTagId(), "Lens", lens);
    }

    private static void addFocusMode(short[] values, ExifTags exifTags) {
        final int offset = 8;
        final int valueIndex = offset - 1;

        if (valueIndex >= values.length) {
            return;    // No focus mode information available
        }

        short value = values[valueIndex];
        String mode = null;

        switch (value) {
        case 0 :
            mode = "One-Shot";

            break;

        case 1 :
            mode = "AI Servo";

            break;

        case 2 :
            mode = "AI Focus";

            break;

        case 3 :
            mode = "MF";

            break;

        case 4 :
            mode = "Single AF";

            break;

        case 5 :
            mode = "Continuous AF";

            break;

        case 6 :
            mode = "MF";

            break;

        default :
            return;
        }

        CanonMakerNotes.addTag(exifTags, CanonMakerNotes.tagId(CANON_TAG, offset), "FocusMode", mode);
    }

    private CanonMakerNoteTag1Formatter() {}
}
