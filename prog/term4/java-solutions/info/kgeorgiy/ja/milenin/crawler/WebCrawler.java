package info.kgeorgiy.ja.milenin.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * By this class you can download all links from input url (with using multithreading).
 *
 * @author Milenin_Ivan
 */
public class WebCrawler implements Crawler {

    private final Downloader downloader;
    private final ExecutorService downloaderExecutor;
    private final ExecutorService extractorExecutor;

    /**
     * Constructor of WebCrawler, which create {@link Downloader downloader}, {@link ExecutorService downloaderExecutor}
     * and {@link ExecutorService extractorExecutor}.
     *
     * @param downloader  {@link Downloader}, which download pages and extract links from them;;
     * @param downloaders maximum number of simultaneously loaded pages;
     * @param extractors  the maximum number of pages from which links are extracted at the same time;
     * @param perHost     the maximum number of pages loaded simultaneously from one host (unused, because the version
     *                    of this program is light)
     */
    @SuppressWarnings("unused")
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        downloaderExecutor = Executors.newFixedThreadPool(downloaders);
        extractorExecutor = Executors.newFixedThreadPool(extractors);
    }


    /**
     * Main method, which start downloading all links from input url;
     *
     * @param args looks like [url [depth [downloads [extractors [perHost]]]]]
     */
    public static void main(String[] args) {
        if (args == null || args.length < 5) {
            System.err.println("Please, input arguments like: WebCrawler url [depth [downloads [extractors [perHost]]]]");
            return;
        }
        String inputUrl = args[0];
        int depth, downloaders, extractors, perHost;
        try {
            depth = Integer.parseInt(args[1]);
            downloaders = Integer.parseInt(args[2]);
            extractors = Integer.parseInt(args[3]);
            perHost = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            System.err.println("Please, input downloaders/extractors/perHost like numbers");
            return;
        }

        try {
            new info.kgeorgiy.ja.milenin.crawler.WebCrawler(new CachingDownloader(), downloaders, extractors, perHost).download(inputUrl, depth);
        } catch (IOException e) {
            System.err.println("CachingDownloader IOException error");
        }


    }

    /**
     * Download all links from {@link String url} with this {@link Integer depth}.
     *
     * @param url   start link, where will be taken;
     * @param depth сщгте of cycles for downloading links;
     * @return {@link Result} , where contains list of successfully downloaded pages and  pages downloaded with errors;
     */
    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public Result download(String url, int depth) {
        Set<String> checkedUrlsSet = new ConcurrentSkipListSet<>();
        Queue<String> urlQueue = new ArrayDeque<>();
        List<String> correctUrls = new CopyOnWriteArrayList<>();
        Map<String, IOException> errorUrls = new ConcurrentHashMap<>();

        urlQueue.add(url);
        for (int i = 0; i < depth; i++) {
            Queue<Future<List<String>>> extractedList = new ConcurrentLinkedQueue<>();
            final int queueSize = urlQueue.size();
            AtomicInteger downloadedCounter = new AtomicInteger();
            while (!urlQueue.isEmpty()) {
                String nowUrl = urlQueue.poll();
                Future<Document> downloadedUrls = downloaderExecutor.submit(() -> {
                    Document nowDoc = null;
                    if (checkedUrlsSet.add(nowUrl)) {
                        try {
                            nowDoc = downloader.download(nowUrl);
                            correctUrls.add(nowUrl);
                        } catch (final IOException e) {
                            errorUrls.put(nowUrl, e);
                        }
                    }
                    downloadedCounter.incrementAndGet();
                    return nowDoc;
                });

                if (i != depth - 1) {
                    Future<List<String>> extractedUrl = extractorExecutor.submit(() -> {
                        try {
                            Document exDoc = downloadedUrls.get();
                            if (exDoc != null) {
                                return exDoc.extractLinks();
                            }
                        } catch (final InterruptedException | ExecutionException ignored) {
                            return Collections.emptyList();
                        }
                        return Collections.emptyList();
                    });
                    extractedList.add(extractedUrl);
                }
            }
            if (i == depth - 1) {
                while (downloadedCounter.get() < queueSize) {
                }
                break;
            }

            while (!extractedList.isEmpty()) {
                Future<List<String>> exFuture = extractedList.poll();
                try {
                    urlQueue.addAll(exFuture.get());
                } catch (final InterruptedException | ExecutionException ignored) {
                }
            }
        }


        return new Result(correctUrls, errorUrls);
    }


    /**
     * Close {@link ExecutorService extractorExecutor} and {@link ExecutorService downloaderExecutor};
     */
    @Override
    public void close() {
        downloaderExecutor.shutdown();
        extractorExecutor.shutdown();
        // while (!downloaderExecutor.isShutdown()) ...
    }
}
