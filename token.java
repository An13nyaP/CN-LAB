import java.util.Scanner;

public class TokenBucketGPT {
    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        int tokensInBucket = 0; // total tokens in the bucket
        int tokenSent;
        System.out.println("Enter the bucket capacity");
        int bucketCapacity = in.nextInt();
        System.out.println("Enter the Token generation rate (Rate at which tokens are sent to the bucket)");
        int tokenRate = in.nextInt();
        System.out.println("Enter the number of requests");
        int n = in.nextInt();
        int[] requests = new int[n];
        System.out.println("Enter the number of packets sent per request: ");
        for (int i = 0; i < n; i++) {
            System.out.printf("Request %d: ", i + 1);
            requests[i] = in.nextInt();
        }
        // Output header
        System.out.printf("%-10s%-15s%-15s%-30s\n", "Time", "Tokens Sent", "Packets/Req", "Tokens Remaining in Bucket");
        for (int i = 0; i < n; i++) {
            // Store previous tokens in the bucket to calculate tokens sent
            int prev = tokensInBucket;
            // Add tokens to the bucket but don't exceed the bucket capacity
            tokensInBucket = Math.min(tokensInBucket + tokenRate, bucketCapacity);
            // Calculate how many tokens have been added
            tokenSent = tokensInBucket - prev;
            // Check if we have enough tokens to process the request
            if (tokensInBucket >= requests[i]) {
                tokensInBucket -= requests[i]; // Deduct the tokens for the request
                System.out.printf("%-10d%-15d%-15d%-30d\n", i + 1, tokenSent, requests[i], tokensInBucket);
            } else {
                // If there aren't enough tokens, display the message and wait for more tokens to be generated
                System.out.printf("%-10d%-15d%-15d%-30d\n", i + 1, tokenSent, requests[i], tokensInBucket);
                System.out.println("More tokens requested than present! Waiting for the bucket to replenish...");
                // Wait until the bucket has enough tokens (this will simulate the delay until enough tokens are available)
                while (tokensInBucket < requests[i]) {
                    // Regenerate tokens
                    tokensInBucket = Math.min(tokensInBucket + tokenRate, bucketCapacity);
                    System.out.printf("Waiting for more tokens... Tokens remaining: %d\n", tokensInBucket);
                    // Exit condition: If we are unable to generate enough tokens, we should break out of the loop
                    if (tokensInBucket == bucketCapacity) {
                        break;
                    }
                }
                // If we have enough tokens, process the request after waiting
                if (tokensInBucket >= requests[i]) {
                    tokensInBucket -= requests[i]; // Deduct the tokens for the request
                    System.out.printf("%-10d%-15d%-15d%-30d\n", i + 1, tokenSent, requests[i], tokensInBucket);
                }
            }
        }
        // Close scanner after use
        in.close();
    }
}
