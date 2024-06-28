package com.example.projecttrackingserver.auth;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Utility class for generating API keys.
 */
public class ApiKeyGenerator {

    /**
     * Generates a random API key.
     *
     * @return a randomly generated API key in hexadecimal format
     */
	public static String generateApiKey() {
		SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 Bit
        random.nextBytes(bytes);
        return new BigInteger(1, bytes).toString(16); // Hexadecimal
	}
}
