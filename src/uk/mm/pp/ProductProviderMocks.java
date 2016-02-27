package uk.mm.pp;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Moco;
import com.github.dreamhead.moco.Runner;

import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.header;
import static com.github.dreamhead.moco.Moco.latency;
import static com.github.dreamhead.moco.Moco.text;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Moco.with;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class ProductProviderMocks {
    private final static String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private final static String OFFER_URL = "http://www.google.com";
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

    public static void main(String[] args) {
        startProductServer(2016, 1.5);
        startProductServer(2017, 2.5);
        startProductServer(2018, 7.5);

        startProductServer(2019, 0.05);
        startProductServer(2020, 0.15);
        startProductServer(2021, 3.5);

        startProductServer(2022, 2.8);
        startProductServer(2023, 4.5);
        startProductServer(2024, 5.3);
    }

    private static void startProductServer(int port, double delay) {
        HttpServer server = Moco.httpServer(port);
        server.get(by(uri("/3rd/products")))
                .response(
                        header(CONTENT_TYPE, APPLICATION_JSON.toString()),
                        with(text(productRecords())),
                        with(latency(toInt(delay), MILLISECONDS))
                );
        Runner.runner(server).start();
    }

    private static String productRecords() {
        int count = nextInt(0, 5);
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
        String ccName = "CCard " + randomAlphabetic(4);
        return replace("{ 'name':'" + ccName + "',\n" +
                "  'OFFER_URL':'" + OFFER_URL + "', \n" +
                "  'desc':'" + LOREM + "', \n" +
                "  'imgUrl':'" + randomUrl() + "'\n" +
                "}", "'", "\"");
    }

    private static int toInt(double delay) {
        return ((Double) (delay * 1000)).intValue();
    }

    private static String randomUrl() {
        return IMAGE_URLS[nextInt(0, IMAGE_URLS.length)];
    }
}
