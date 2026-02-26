package core.security;

public class SecretManager {

    public static String getSecret(String key) {

        String value = System.getenv(key);

        if (value == null) {
            throw new RuntimeException(
                    "Missing required secret: " + key
            );
        }

        return value;
    }
}