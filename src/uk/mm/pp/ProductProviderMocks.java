package uk.mm.pp;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Moco;
import com.github.dreamhead.moco.Runner;
import org.apache.commons.lang3.RandomStringUtils;

import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.header;
import static com.github.dreamhead.moco.Moco.latency;
import static com.github.dreamhead.moco.Moco.log;
import static com.github.dreamhead.moco.Moco.text;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Moco.with;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class ProductProviderMocks {
    private final static String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private final static String OFFER_URL = "http://www.google.com";
    private final static String IMAGE_URL = "http://www.credit-card-logos.com/images/multiple_credit-card-logos-2/credit_card_logos_29.gif";

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
        HttpServer server = Moco.httpServer(port, log());
        server.get(by(uri("/3rd/products")))
                .response(
                        header(CONTENT_TYPE, APPLICATION_JSON.toString()),
                        with(text(productRecord())),
                        with(latency(toInt(delay), MILLISECONDS))
                );
        Runner.runner(server).start();
    }

    private static int toInt(double delay) {
        return ((Double) (delay * 1000)).intValue();
    }

    private static String productRecord() {
        String ccName = "CCard " + RandomStringUtils.randomAlphabetic(4);
        return "{ 'name':'" + ccName + "',\n" +
                "  'OFFER_URL':'" + OFFER_URL + "', \n" +
                "  'desc':'" + LOREM + "', \n" +
                "  'imgUrl':'" + IMAGE_URL + "'\n" +
                "}";
    }
}
