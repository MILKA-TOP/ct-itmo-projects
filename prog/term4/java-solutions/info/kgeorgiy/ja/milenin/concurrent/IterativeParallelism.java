package info.kgeorgiy.ja.milenin.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;


/**
 * This class can return maximum/minimum of list of values or that `all/any` of these elements
 * correspond to {@link Predicate predicate} with using multithreading.
 * <p>
 * Implementing {@link ScalarIP}
 *
 * @author MILKA
 */
public class IterativeParallelism implements ScalarIP {


    private final ParallelMapper parallelMapper;

    /**
     * Constructor, where will {@link ParallelMapper parallelMapper} is not null
     */
    public IterativeParallelism(ParallelMapper mapper) {
        parallelMapper = mapper;
    }


    /**
     * Empty constructor, where will {@link ParallelMapper parallelMapper} is null
     */
    public IterativeParallelism() {
        parallelMapper = null;
    }

    /**
     * Return maximum of {@link List values}
     *
     * @param threads    number of threads to use;
     * @param values     {@link List} of values, where will be find maximum;
     * @param comparator {@link Comparator} for comparison;
     * @param <T>        {@link Class} of {@link List values} elements;
     * @return maximum of {@link List values} by {@link Comparator comparator};
     * @throws InterruptedException if you will get some problems with threads;
     */
    @Override
    public <T> T maximum(final int threads,
                         final List<? extends T> values,
                         final Comparator<? super T> comparator) throws InterruptedException {
        return minimum(threads, values, comparator.reversed());
    }

    /**
     * Return minimum of {@link List values}
     *
     * @param threads    number of threads to use;
     * @param values     {@link List} of values, where will be find minimum;
     * @param comparator {@link Comparator} for comparison;
     * @param <T>        {@link Class} of {@link List values} elements;
     * @return minimum of {@link List values} by {@link Comparator comparator};
     * @throws InterruptedException if you will get some problems with threads;
     */
    @Override
    public <T> T minimum(final int threads,
                         final List<? extends T> values,
                         final Comparator<? super T> comparator) throws InterruptedException {
        return functionThread(threads, values, ts -> ts.stream().min(comparator).orElseThrow())
                .stream()
                .min(comparator)
                .orElseThrow();
    }

    /**
     * Return, that all {@link List values} correspond to {@link Predicate predicate}
     *
     * @param threads   number of threads to use;
     * @param values    {@link List} of values, that is analyzed for the presence of all elements corresponding
     *                  to {@link Predicate predicate};
     * @param predicate {@link Predicate} for comparison;
     * @param <T>       {@link Class} of {@link List values} elements;
     * @return {@link Boolean}, that all {@link List values} correspond to {@link Predicate predicate};
     * @throws InterruptedException if you will get some problems with threads;
     */
    @Override
    public <T> boolean all(final int threads,
                           final List<? extends T> values,
                           final Predicate<? super T> predicate) throws InterruptedException {
        // :NOTE: Похоже на any
        return !any(threads, values, predicate.negate());
    }

    /**
     * Return, that any {@link List values} correspond to {@link Predicate predicate}
     *
     * @param threads   number of threads to use;
     * @param values    {@link List} of values, that is analyzed for the presence of any elements corresponding
     *                  to {@link Predicate predicate};
     * @param predicate {@link Predicate} for comparison;
     * @param <T>       {@link Class} of {@link List values} elements;
     * @return {@link Boolean}, that any {@link List values} correspond to {@link Predicate predicate};
     * @throws InterruptedException if you will get some problems with threads;
     */
    @Override
    public <T> boolean any(final int threads,
                           final List<? extends T> values,
                           final Predicate<? super T> predicate) throws InterruptedException {
        return comparisonByFunctions(threads,
                values,
                objects -> objects.stream().anyMatch(predicate),
                object -> object.stream().anyMatch(Boolean::booleanValue));
    }

    private <T> boolean comparisonByFunctions(final int threads,
                                              final List<? extends T> values,
                                              Function<List<? extends T>, Boolean> valueListFunc,
                                              Function<List<Boolean>, Boolean> booleanListFunc) throws InterruptedException {
        return booleanListFunc.apply(functionThread(threads, values, valueListFunc));
    }


    private <T, R> List<R> functionThread(int threadsCount,
                                          List<? extends T> values,
                                          Function<List<? extends T>, R> completeFunc) throws InterruptedException {
        if (threadsCount < 1) {
            throw new IllegalArgumentException("Please, input correct count of threads");
        }


        int valuesCount = values.size();
        threadsCount = Math.min(threadsCount, valuesCount);
        int deltaCount = valuesCount / threadsCount;
        int bigSubListCount = valuesCount % threadsCount;

        // :NOTE: ArrayList в объявлении
        List<List<? extends T>> subMemory = new ArrayList<>(Collections.nCopies(threadsCount, null));
        int leftRange = 0, rightRange;
        for (int i = 0; i < threadsCount; i++) {
            rightRange = leftRange + deltaCount + ((i < bigSubListCount) ? 1 : 0);
            subMemory.set(i, values.subList(leftRange, rightRange));

            leftRange = rightRange;
        }


        if (parallelMapper == null) {
            List<R> results = new ArrayList<>(Collections.nCopies(threadsCount, null));
            List<Thread> threads = new ArrayList<>(Collections.nCopies(threadsCount, null));
            IntStream.range(0, threadsCount).forEach(i -> {
                threads.set(i, new Thread(() -> results.set(i, completeFunc.apply(subMemory.get(i)))));
                threads.get(i).start();
            });

            for (Thread thread : threads) {
                thread.join();
            }
            return results;
        }
        return parallelMapper.map(completeFunc, subMemory);
    }

}