package info.kgeorgiy.ja.milenin.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;


/**
 * By this class you can get result of functions with using multithreading;
 * <p>
 * Implementing {@link ParallelMapper}
 *
 * @author MILKA
 */

public class ParallelMapperImpl implements ParallelMapper {

    private final Thread[] threadArray;
    private final Deque<Runnable> deque = new ArrayDeque<>();


    /**
     * Constructor of {@link info.kgeorgiy.ja.milenin.concurrent.ParallelMapperImpl};
     *
     * @param threads create this count of {@link Thread threads};
     */
    public ParallelMapperImpl(final int threads) {
        threadArray = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            threadArray[i] = new Thread(() -> {
                while (!Thread.interrupted()) {
                    Runnable nowFunc;
                    try {
                        synchronized (deque) {
                            while (deque.isEmpty()) {
                                deque.wait();
                            }
                            nowFunc = deque.poll();
                        }
                        nowFunc.run();
                    } catch (final InterruptedException e) {
                        break;
                    }
                }
            });
            threadArray[i].start();
        }
    }

    /**
     * Calculate result of {@link Function function} with using multithreading;
     *
     * @param args arguments, which used by the {@link Function f};
     * @param f    {@link Function}, which calculate result;
     * @param <T>  input param of {@link Function f};
     * @param <R>  output param of {@link Function f};
     * @return {@link List} of {@link Function f} results;
     * @throws InterruptedException if will some problems on the threads;
     */
    @Override
    public <T, R> List<R> map(final Function<? super T, ? extends R> f, final List<? extends T> args) throws InterruptedException {
        info.kgeorgiy.ja.milenin.concurrent.ParallelMapperImpl.ApplyList<R> results = new info.kgeorgiy.ja.milenin.concurrent.ParallelMapperImpl.ApplyList<>(args.size());
        for (int i = 0; i < args.size(); i++) {
            final int finalI = i;
            synchronized (deque) {
                deque.add(() -> results.setter(finalI, f.apply(args.get(finalI))));
                deque.notify();
            }
        }
        return results.getter();
    }


    /**
     * Close all running threads;
     */
    @Override
    public void close() {
        // :NOTE: join
        for (Thread thread : threadArray) {
            thread.interrupt();
        }
        for (Thread thread : threadArray) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }


    private static class ApplyList<R> {
        private final List<R> outputList;
        private int resultCounter = 0;

        ApplyList(final int listSize) {
            outputList = new ArrayList<>(Collections.nCopies(listSize, null));
        }

        public void setter(final int index, final R value) {
            outputList.set(index, value);
            synchronized (this) {
                resultCounter++;
                notify();
            }
        }

        public synchronized List<R> getter() throws InterruptedException {
            while (resultCounter < outputList.size()) {
                wait();
            }
            return outputList;
        }
    }

}
