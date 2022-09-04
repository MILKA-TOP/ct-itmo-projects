package info.kgeorgiy.ja.milenin.i18n;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class TextStatisticsTest {

    static TextStatistics ts;
    private final String testDir = "java-solutions/info/kgeorgiy/ja/milenin/i18n/test_texts/";

    @BeforeClass
    public static void before() {
        ts = new TextStatistics();
    }

    @Test
    public void test_zn_only_text() throws IOException {
        String inputPath = testDir + "zn_input";
        String outputPath = testDir + "zn_output";
        String resultPath = testDir + "zn_output_result";
        checkTexts(inputPath, outputPath, resultPath, Locale.CHINESE, new UsageResourceBundle_en());
    }

    @Test
    public void test_en_BE_only_text() throws IOException {
        String inputPath = testDir + "en_BE_input";
        String outputPath = testDir + "en_BE_output";
        String resultPath = testDir + "en_BE_output_result";
        checkTexts(inputPath, outputPath, resultPath, new Locale("en_BE"), new UsageResourceBundle_en());
    }

    @Test
    public void test_ru_only_text() throws IOException {
        String inputPath = testDir + "ru1_input";
        String outputPath = testDir + "ru1_output";
        String resultPath = testDir + "ru1_output_result";
        checkTexts(inputPath, outputPath, resultPath, new Locale("ru_RU"), new UsageResourceBundle_ru());
    }

    @Test
    public void test_ru_with_numbers_text() throws IOException {
        String inputPath = testDir + "ru2_input";
        String outputPath = testDir + "ru2_output";
        String resultPath = testDir + "ru2_output_result";
        checkTexts(inputPath, outputPath, resultPath, new Locale("ru_RU"), new UsageResourceBundle_ru());
    }

    @Test
    public void test_ru_with_dates_text() throws IOException {
        String inputPath = testDir + "ru3_input";
        String outputPath = testDir + "ru3_output";
        String resultPath = testDir + "ru3_output_result";
        checkTexts(inputPath, outputPath, resultPath, new Locale("ru"), new UsageResourceBundle_ru());
    }

    private void checkTexts(String inputPath,
                            String outputPath,
                            String resultPath,
                            Locale inputLocale,
                            ResourceBundle rb) throws IOException {
        ts.makeStatistic(inputPath, outputPath, inputLocale, rb);
        Assert.assertEquals(Files.readString(Paths.get(outputPath)), Files.readString(Paths.get(resultPath)));
    }


}
