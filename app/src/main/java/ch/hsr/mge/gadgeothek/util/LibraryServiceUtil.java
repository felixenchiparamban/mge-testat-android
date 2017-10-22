package ch.hsr.mge.gadgeothek.util;

public class LibraryServiceUtil {
    private final static String SERVER_URL_PATTERN = "http://mge%d.dev.ifs.hsr.ch/public";
    private final static int DEFAULT_SERVER = 2;

    public static String[] generateServerUrls() {
        String[] urls = new String[10];
        for (int i = 0; i < 10; i++) {
            urls[i] = String.format(SERVER_URL_PATTERN, i + 1);
        }
        return urls;
    }

    public static int getDefaultServerValue() {
        return DEFAULT_SERVER;
    }

    public static String getServerAddress() {
        return getServerAddress(DEFAULT_SERVER);
    }

    public static String getServerAddress(int value) {
        return String.format(SERVER_URL_PATTERN, value);
    }
}
