package info.kgeorgiy.ja.milenin.i18n;

import java.text.Collator;
import java.util.*;
import java.util.function.Function;

/**
 * This class make statistic of input data.
 *
 * @param <T> class of data (for example {@link String}, {@link Double}, {@link Number} and etc.);
 * @author Milenin Ivan
 */
class StatisticInformation<T> {
    private final List<T> informationList = new ArrayList<>();
    private Set<T> informationSet;
    private List<T> sortedByLocale;
    private List<T> sortedByLength;

    /**
     * Add element to list with all data.
     *
     * @param element - added element;
     */
    public void addInfo(T element) {
        informationList.add(element);
    }

    /**
     * Making sort by {@link Locale inputLocale} (if he makes it). Also make sorting by {@link Comparator infoComp} and
     * makes set with unique elements.
     *
     * @param inputLocale input {@link Locale};
     * @param infoComp    {@link Comparator} for sorting;
     */
    public void completeStatistic(Locale inputLocale, Comparator<T> infoComp) {
        Collator localeSort = Collator.getInstance(inputLocale);
        sortedByLocale = new ArrayList<>(informationList);
        sortedByLength = new ArrayList<>(informationList);
        try {
            sortedByLocale.sort(localeSort);
        } catch (Exception ignored) {
        }
        sortedByLength.sort(infoComp);
        informationSet = new HashSet<>(informationList);
    }

    /**
     * @return counts of all elements;
     */
    public int allSize() {
        return informationList.size();
    }

    /**
     * @return that doesn't have elements;
     */
    public boolean isEmpty() {
        return informationList.isEmpty();
    }

    /**
     * @return counts of unique elements;
     */
    public int sizeAnother() {
        return informationSet.size();
    }

    /**
     * @return minimal sorted elements (or {@code null} if doesn't have elements;
     */
    public T minimalElement() {
        return (sortedByLocale.isEmpty()) ? null : sortedByLocale.get(0);
    }

    /**
     * @return maximum sorted elements (or {@code null} if doesn't have elements;
     */
    public T maximumElement() {
        return (sortedByLocale.isEmpty()) ? null : sortedByLocale.get(sortedByLocale.size() - 1);
    }

    /**
     * @return length of minimal by length element (or {@code -1} if it doesn't have elements;
     */
    public int minimalLength() {
        return (sortedByLength.isEmpty()) ? -1 : ((String) sortedByLength.get(0)).length();
    }

    /**
     * @return minimal by length element (or {@code null} if it doesn't have elements;
     */
    public T minimalLengthElement() {
        return (sortedByLength.isEmpty()) ? null : sortedByLength.get(0);
    }

    /**
     * @return length of maximum by length element (or {@code -1} if it doesn't have elements;
     */
    public int maximumLength() {
        return (sortedByLength.isEmpty()) ? -1 : ((String) sortedByLength.get(sortedByLength.size() - 1)).length();
    }

    /**
     * @return maximum by length element (or {@code null} if it doesn't have elements;
     */
    public T maximumLengthElement() {
        return (sortedByLength.isEmpty()) ? null : sortedByLength.get(sortedByLength.size() - 1);
    }

    /**
     * @param plusFunc function, which transform element from list to number for calculating average value;
     * @return calculating average value;
     */
    public double getAverage(Function<T, Double> plusFunc) {
        double allSize = 0.0;
        for (T element : informationList) {
            allSize += plusFunc.apply(element);
        }
        return (informationList.isEmpty()) ? 0.0 : allSize / informationList.size();
    }
}
