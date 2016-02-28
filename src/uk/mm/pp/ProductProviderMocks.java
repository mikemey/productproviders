package uk.mm.pp;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Moco;
import com.github.dreamhead.moco.Runner;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.header;
import static com.github.dreamhead.moco.Moco.latency;
import static com.github.dreamhead.moco.Moco.log;
import static com.github.dreamhead.moco.Moco.text;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Moco.with;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.CharUtils.isAsciiAlphaUpper;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class ProductProviderMocks {
    private static final int DESCRIPTION_LEN = 500;
    private final static String OFFER_URL = "http://www.google.com";
    private final static String DESCRIPTIONS = readRandomFile();

    private final static String[] IMAGE_URLS = {
            "http://www.credit-card-logos.com/images/multiple_credit-card-logos-2/credit_card_logos_29.gif",
            "http://www.credit-card-logos.com/images/visa_credit-card-logos/visa_infinite_chip2.gif",
            "http://www.credit-card-logos.com/images/visa_credit-card-logos/visa_electron.gif",
            "http://www.credit-card-logos.com/images/mastercard_credit-card-logos/mastercard_logo_6.gif",
            "http://www.credit-card-logos.com/images/visa_credit-card-logos/visa_travelmoney.gif",
            "http://www.credit-card-logos.com/images/visa_credit-card-logos/visa_corporate.gif",
            "http://www.credit-card-logos.com/images/american_express_credit-card-logos/american_express_logo_7.gif",
            "http://www.credit-card-logos.com/images/discover_credit-card-logos/discover_network1.jpg"
    };

    private static String readRandomFile() {
        try {
            List<String> lines = Files.readLines(new File("resources/random.txt"), defaultCharset());
            return Joiner.on(' ').join(lines);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        startProductServer(2016, 1.5);
        startProductServer(2017, 2.5);
        startProductServer(2018, 7.5);

        startProductServer(2019, 0.05);
        startProductServer(2020, 0.15);
        startProductServer(2021, 3.5);

        startProductServer(2022, 2.8);
        startProductServer(2023, 5.0);
        startProductServer(2024, 6.3);
    }

    private static void startProductServer(int port, double delay) {
        HttpServer server = Moco.httpServer(port, log());
        server.get(by(uri("/3rd/products")))
                .response(
                        header(CONTENT_TYPE, APPLICATION_JSON.toString()),
                        with(text(productRecords())),
                        with(latency(toInt(delay), MILLISECONDS))
                );
        Runner.runner(server).start();
        System.out.println("started server on port: " + port);
    }

    private static String productRecords() {
        int count = nextInt(0, 6);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(productRecord());
        }
        sb.append("]");
        return sb.toString();
    }

    private static String productRecord() {
        String ccName = "Card " + randomAlphabetic(4);
        return replace("{ 'name':'" + ccName + "',\n" +
                "  'offer_url':'" + OFFER_URL + "', \n" +
                "  'desc':'" + randomText() + "', \n" +
                "  'img_url':'" + randomUrl() + "'\n" +
                "}", "'", "\"");
    }

    private static int toInt(double delay) {
        return ((Double) (delay * 1000)).intValue();
    }

    private static String randomUrl() {
        return IMAGE_URLS[nextInt(0, IMAGE_URLS.length)];
    }

    private static String randomText() {
        int startIx = nextInt(0, DESCRIPTIONS.length() - DESCRIPTION_LEN);
        int endIx = startIx + DESCRIPTION_LEN;

        while (!isAsciiAlphaUpper(DESCRIPTIONS.charAt(startIx))) {
            startIx++;
            endIx++;
            if (endIx >= DESCRIPTIONS.length()) {
                startIx = 0;
                endIx = DESCRIPTION_LEN;
            }
        }
        return DESCRIPTIONS.substring(startIx, endIx);
    }
}
