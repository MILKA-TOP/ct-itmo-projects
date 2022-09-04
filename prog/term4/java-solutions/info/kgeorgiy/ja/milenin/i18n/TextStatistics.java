package info.kgeorgiy.ja.milenin.i18n;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.*;
import java.util.*;

/**
 * Analyzes the text from the file and makes statistics on sentences, numbers, words, currencies and dates.
 *
 * @author Milenin Ivan
 */
public class TextStatistics {


    /**
     * This main class start making statistics on sentences, numbers, words, currencies and dates by input_file, write
     * to output_file and analyze input file with input_locale and write to output_file with output_locale (RU/EN).
     *
     * @param args [Locale input text][Locale output text (ru/en)][input path][output path];
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args[0] == null || args[1] == null || args[2] == null || args[3] == null) {
            System.err.println("Please, input [Locale input text][Locale output text (ru/en)][input path][output path]");
            return;
        }
        Locale inputLocale = Locale.forLanguageTag(args[0]);
        Locale outputLocale = Locale.forLanguageTag(args[1]);
        ResourceBundle outputRb;
        if (outputLocale.equals(Locale.ENGLISH)) {
            outputRb = new UsageResourceBundle_en();
        } else if (outputLocale.equals(new Locale("ru"))) {
            outputRb = new UsageResourceBundle_ru();
        } else {
            System.err.println("Unknown output locale[ru/en]: " + args[1]);
            return;
        }

        new TextStatistics().makeStatistic(args[2], args[3], inputLocale, outputRb);
    }

    /**
     * Start making statistics on sentences, numbers, words, currencies and dates
     *
     * @param inputFile   Input file;
     * @param outputFile  Output file;
     * @param inputLocale {@link Locale} of inputFile;
     * @param rb          {@link ResourceBundle} with {@link UsageResourceBundle_en en} or
     *                    {@link UsageResourceBundle_ru ru } text;
     * @throws IOException if can't read or write to inputFile/outputFile;
     */
    public void makeStatistic(String inputFile, String outputFile, Locale inputLocale, ResourceBundle rb) throws IOException {
        String text = Files.readString(Paths.get(inputFile));
        BreakIterator boundary = BreakIterator.getSentenceInstance(inputLocale);
        boundary.setText(text);
        String userOutput = formatOutputText(inputLocale, sentenceBreaking(inputLocale, text), rb, inputFile);
        System.out.println(userOutput);
        Files.writeString(Paths.get(outputFile), userOutput);
    }

    private List<StatisticInformation<?>> sentenceBreaking(Locale inputLocale, String text) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(inputLocale);
        NumberFormat currFormat = NumberFormat.getCurrencyInstance(inputLocale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, inputLocale);
        StatisticInformation<String> siWords = new StatisticInformation<>();
        StatisticInformation<String> siSentences = new StatisticInformation<>();
        StatisticInformation<Double> siNumber = new StatisticInformation<>();
        StatisticInformation<Number> siCurrency = new StatisticInformation<>();
        StatisticInformation<Date> siDate = new StatisticInformation<>();
        BreakIterator boundarySentences = BreakIterator.getSentenceInstance(inputLocale);
        boundarySentences.setText(text);
        int startSentence = boundarySentences.first();
        for (int endSentence = boundarySentences.next();
             endSentence != BreakIterator.DONE;
             startSentence = endSentence, endSentence = boundarySentences.next()) {
            String nowSentence = text.substring(startSentence, endSentence);
            siSentences.addInfo(nowSentence);
            BreakIterator boundaryWords = BreakIterator.getWordInstance(inputLocale);
            boundaryWords.setText(nowSentence);
            int startWords = boundaryWords.first();
            for (int endWords = boundaryWords.next();
                 endWords != BreakIterator.DONE;
                 startWords = endWords, endWords = boundaryWords.next()) {
                String nowWord = text.substring(startWords, endWords).replaceAll("\\s", "");
                if (nowWord.isEmpty()) {
                    continue;
                }
                siWords.addInfo(nowWord);
                try {
                    siNumber.addInfo(numberFormat.parse(nowWord).doubleValue());
                } catch (ParseException ignored) {
                }
                try {
                    siCurrency.addInfo(currFormat.parse(nowWord));
                } catch (ParseException ignored) {
                }
                try {
                    siDate.addInfo(dateFormat.parse(nowWord));
                } catch (ParseException ignored) {
                }

            }
        }
        siSentences.completeStatistic(inputLocale, Comparator.comparingInt(String::length));
        siWords.completeStatistic(inputLocale, Comparator.comparingInt(String::length));
        siNumber.completeStatistic(inputLocale, null);
        siCurrency.completeStatistic(inputLocale, null);
        siDate.completeStatistic(inputLocale, null);
        return List.of(siSentences, siWords, siNumber, siCurrency, siDate);
    }

    @SuppressWarnings("unchecked")
    private String formatOutputText(Locale inputLocale, List<StatisticInformation<?>> siList,
                                    ResourceBundle rb,
                                    String inputFile) {
        StatisticInformation<String> siSentence = (StatisticInformation<String>) siList.get(0);
        StatisticInformation<String> siWords = (StatisticInformation<String>) siList.get(1);
        StatisticInformation<Double> siNumber = (StatisticInformation<Double>) siList.get(2);
        StatisticInformation<Number> siCurrency = (StatisticInformation<Number>) siList.get(3);
        StatisticInformation<Date> siDate = (StatisticInformation<Date>) siList.get(4);

        NumberFormat nf = DecimalFormat.getNumberInstance(inputLocale);
        nf.setMaximumFractionDigits(2);
        NumberFormat cf = NumberFormat.getCurrencyInstance(inputLocale);
        cf.setMaximumFractionDigits(2);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, inputLocale);

        String outputText = "%s %s%n" +
                "%s%n" +
                "\t%s: %d.%n" +
                "\t%s: %d.%n" +
                "\t%s: %d.%n" +
                "\t%s: %d.%n" +
                "\t%s: %d.%n" +
                "%s%n" +
                "\t%s: %d (%d %s).%n" +
                "\t%s: \"%s\".%n" +
                "\t%s: \"%s\".%n" +
                "\t%s: %d (\"%s\").%n" +
                "\t%s: %d (\"%s\").%n" +
                "\t%s: %s.%n" +
                "%s%n" +
                "\t%s: %d (%d %s).%n" +
                "\t%s: \"%s\".%n" +
                "\t%s: \"%s\".%n" +
                "\t%s: %d (\"%s\").%n" +
                "\t%s: %d (\"%s\").%n" +
                "\t%s: %s.%n" +
                "%s%n" +
                "\t%s: %d (%d %s).%n" +
                "\t%s: %s.%n" +
                "\t%s: %s.%n" +
                "\t%s: %s.%n" +
                "%s%n" +
                "\t%s: %d (%d %s).%n" +
                "\t%s: %s.%n" +
                "\t%s: %s.%n" +
                "\t%s: %s.%n" +
                "%s%n" +
                "\t%s: %d (%d %s).%n" +
                "\t%s: %s.%n" +
                "\t%s: %s.%n" +
                "\t%s: %s.%n";
        return String.format(outputText,
                rb.getString("file"),
                inputFile,
                rb.getString("sum_stat"),
                rb.getString("count_sentence"),
                siSentence.allSize(),
                rb.getString("count_word"),
                siWords.allSize(),
                rb.getString("count_numb"),
                siNumber.allSize(),
                rb.getString("count_curr"),
                siCurrency.allSize(),
                rb.getString("count_date"),
                siDate.allSize(),

                rb.getString("sentence_stat"),
                rb.getString("count_sentence"),
                siSentence.allSize(),
                siSentence.sizeAnother(),
                rb.getString("another"),
                rb.getString("min_sentence"),
                (siSentence.isEmpty()) ? "_" : siSentence.minimalElement(),
                rb.getString("max_sentence"),
                (siSentence.isEmpty()) ? "_" : siSentence.maximumElement(),
                rb.getString("min_sentence_size"),
                siSentence.minimalLength(),
                (siSentence.isEmpty()) ? "_" : siSentence.minimalLengthElement(),
                rb.getString("max_sentence_size"),
                siSentence.maximumLength(),
                (siSentence.isEmpty()) ? "_" : siSentence.maximumLengthElement(),
                rb.getString("average_sentence_size"),
                nf.format(siSentence.getAverage(s -> (double) s.length())),

                rb.getString("word_stat"),
                rb.getString("count_word"),
                siWords.allSize(),
                siWords.sizeAnother(),
                rb.getString("another"),
                rb.getString("min_word"),
                (siWords.isEmpty()) ? "_" : siWords.minimalElement(),
                rb.getString("max_word"),
                (siWords.isEmpty()) ? "_" : siWords.maximumElement(),
                rb.getString("min_word_size"),
                siWords.minimalLength(),
                (siWords.isEmpty()) ? "_" : siWords.minimalLengthElement(),
                rb.getString("max_word_size"),
                siWords.maximumLength(),
                (siWords.isEmpty()) ? "_" : siWords.maximumLengthElement(),
                rb.getString("average_word_size"),
                nf.format(siWords.getAverage(s -> (double) s.length())),

                rb.getString("numb_stat"),
                rb.getString("count_numb"),
                siNumber.allSize(),
                siNumber.sizeAnother(),
                rb.getString("another"),
                rb.getString("min_numb"),
                (siNumber.isEmpty()) ? "_" : nf.format(siNumber.minimalElement()),
                (rb.getString("max_numb")),
                (siNumber.isEmpty()) ? "_" : nf.format(siNumber.maximumElement()),
                rb.getString("average_numb"),
                nf.format(siNumber.getAverage(d -> d)),

                rb.getString("curr_stat"),
                rb.getString("count_curr"),
                siCurrency.allSize(),
                siCurrency.sizeAnother(),
                rb.getString("another"),
                rb.getString("min_curr"),
                (siCurrency.isEmpty()) ? "_" : cf.format(siCurrency.minimalElement()),
                rb.getString("max_curr"),
                (siCurrency.isEmpty()) ? "_" : cf.format(siCurrency.maximumElement()),
                rb.getString("average_curr"),
                cf.format(siCurrency.getAverage(Number::doubleValue)),

                rb.getString("date_stat"),
                rb.getString("count_date"),
                siDate.allSize(),
                siDate.sizeAnother(),
                rb.getString("another"),
                rb.getString("min_curr"),
                (siDate.isEmpty()) ? "_" : df.format(siDate.minimalElement()),
                rb.getString("max_curr"),
                (siDate.isEmpty()) ? "_" : df.format(siDate.maximumElement()),
                rb.getString("average_curr"),
                (siDate.isEmpty()) ? "_" : df.format(siDate.getAverage(d -> (double) d.getTime()))
        );
    }

}
