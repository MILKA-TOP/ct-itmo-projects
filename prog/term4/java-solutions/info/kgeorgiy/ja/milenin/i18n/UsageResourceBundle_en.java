package info.kgeorgiy.ja.milenin.i18n;

import java.util.ListResourceBundle;

public class UsageResourceBundle_en extends ListResourceBundle {
    private static final Object[][] CONTENTS = {
            {"file", "Analyzed file"},
            {"another", "different"},
            {"sum_stat", "Summary statistics"},
            {"count_sentence", "Number of sentences"},
            {"count_word", "Number of words"},
            {"count_numb", "Number of numbers"},
            {"count_curr", "Number of currencies"},
            {"count_date", "Number of dates"},

            {"sentence_stat", "Offer statistics"},
            {"min_sentence", "Minimum offer"},
            {"max_sentence", "Maximum offer"},
            {"min_sentence_size", "Minimum sentence length"},
            {"max_sentence_size", "Maximum sentence length"},
            {"average_sentence_size", "Average sentence length"},

            {"word_stat", "Word statistics"},
            {"min_word", "Minimum word"},
            {"max_word", "Maximum word"},
            {"min_word_size", "Minimum word length"},
            {"max_word_size", "Maximum word length"},
            {"average_word_size", "Average word length"},

            {"numb_stat", "Statistics by numbers"},
            {"min_numb", "Minimum number"},
            {"max_numb", "Maximum number"},
            {"average_numb", "Average number"},

            {"curr_stat", "Statistics on amounts of money"},
            {"min_curr", "Minimum amount"},
            {"max_curr", "Maximum amount"},
            {"average_curr", "Average amount"},

            {"date_stat", "Date statistics"},
            {"min_date", "Minimum date"},
            {"max_date", "Maximum date"},
            {"average_date", "Average date"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}
