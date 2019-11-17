package com.pulu.dividend.core;

import com.pulu.dividend.model.Stock;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private final Logger LOGGER = Logger.getLogger("Worker");

    public void process() {
        List<Stock> stocks = new ArrayList<>();

        String[] reits = loadDataFile();

        for (String paper : reits) {
            acquireExternalData(stocks, paper);
        }

        writeFile(stocks);
    }

    private String[] loadDataFile() {
        LOGGER.log(Level.INFO, "Reading Reits data file");

        Path path = Paths.get("reits.csv");
        String[] reits = new String[150];
        try {
            reits = Files.readAllLines(path).get(0).split(",");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while reading data file.", e);
        }
        return reits;
    }

    private void acquireExternalData(List<Stock> stocks, String paper) {
        String dividendURL = "https://api.nasdaq.com/api/quote/" + paper + "/dividends?assetclass=stocks";
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(dividendURL)).build();
        HttpResponse<String> response = null;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject dividendJson = new JSONObject(response.body());

            Document document = Jsoup.connect("https://finance.yahoo.com/quote/" + paper).get();
            String latestPrice = document.select("#quote-header-info > div:eq(2) > div > div > span:eq(0)").text();

            LOGGER.log(Level.INFO, "Acquired external data for " + paper);

            extractData(stocks, paper, dividendJson, new BigDecimal(latestPrice));

        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error while obtaining external data.", e);
        }
    }

    private void extractData(List<Stock> stocks, String paper, JSONObject dividendJson, BigDecimal latestPrice) {
        try {
            JSONObject nextDividend = dividendJson.getJSONObject("data").getJSONObject("dividends").getJSONArray("rows").getJSONObject(0);
            BigDecimal dividend = new BigDecimal(nextDividend.getString("amount").replace("$", ""));

            LocalDate exDate = LocalDate.now().plus(1, ChronoUnit.YEARS);
            String exOrEffDate = nextDividend.getString("exOrEffDate");
            if (!exOrEffDate.equalsIgnoreCase("N/A"))
                exDate = LocalDate.parse(exOrEffDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            LocalDate paymentDate = LocalDate.now().plus(1, ChronoUnit.YEARS);
            String paymentDate1 = nextDividend.getString("paymentDate");
            if (!paymentDate1.equalsIgnoreCase("N/A"))
                paymentDate = LocalDate.parse(paymentDate1, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            stocks.add(new Stock(paper, latestPrice, dividend, exDate, paymentDate));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while processing data for paper " + paper, e);
        }
    }

    private void writeFile(List<Stock> stocks) {
        LOGGER.log(Level.INFO, "Writing stocks data to file.");

        Path savePath = Paths.get("dividends_" + LocalDate.now() + ".csv");

        Comparator<Stock> comparator = Comparator.comparing((Stock s) -> s.getExDate().isAfter(LocalDate.now()))
                .thenComparing((s1, s2) -> s2.getExDate().compareTo(s1.getExDate()))
                .thenComparing(Stock::getValue).reversed();
        stocks.sort(comparator);

        writeHeaderToFile(savePath);
        writeDataToFile(stocks, savePath);

        LOGGER.log(Level.INFO, "Printing the current list of stocks...");
        for (Stock stock : stocks) {
            System.out.println(stock);
        }
    }

    private void writeHeaderToFile(Path savePath) {
        try {
            Files.writeString(savePath, "Paper, Dividend, Ex Date, Stock Price, Payment Date, Value, " + LocalDate.now() + System.lineSeparator(), StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create file.", e);
        }
    }

    private void writeDataToFile(List<Stock> stocks, Path savePath) {
        stocks.forEach(stock -> {
            try {
                Files.writeString(savePath, stock.toString() + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error while writing stocks file.", e);
            }
        });
    }
}