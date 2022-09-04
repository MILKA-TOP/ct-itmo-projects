package info.kgeorgiy.ja.milenin.i18n;

import java.util.ListResourceBundle;

public class UsageResourceBundle_ru extends ListResourceBundle {
    private static final Object[][] CONTENTS = {
            {"file", "Анализируемый файл"},
            {"another", "различных"},
            {"sum_stat", "Сводная статистика"},
            {"count_sentence", "Число предложений"},
            {"count_word", "Число слов"},
            {"count_numb", "Число чисел"},
            {"count_curr", "Число сумм"},
            {"count_date", "Число дат"},

            {"sentence_stat", "Статистика по предложениям"},
            {"min_sentence", "Минимальное предложение"},
            {"max_sentence", "Максимальное предложение"},
            {"min_sentence_size", "Минимальная длина предложение"},
            {"max_sentence_size", "Максимальная длина предложение"},
            {"average_sentence_size", "Средняя длина предложение"},

            {"word_stat", "Статистика по словам"},
            {"min_word", "Минимальное слово"},
            {"max_word", "Максимальное слово"},
            {"min_word_size", "Минимальная длина слова"},
            {"max_word_size", "Максимальная длина слова"},
            {"average_word_size", "Средняя длина слова"},

            {"numb_stat", "Статистика по числам"},
            {"min_numb", "Минимальное число"},
            {"max_numb", "Максимальное число"},
            {"average_numb", "Среднее число"},

            {"curr_stat", "Статистика по суммам денег"},
            {"min_curr", "Минимальная сумма"},
            {"max_curr", "Максимальная сумма"},
            {"average_curr", "Средняя сумма"},

            {"date_stat", "Статистика по датам"},
            {"min_date", "Минимальная дата"},
            {"max_date", "Максимальная дата"},
            {"average_date", "Средняя дата"}

    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}
