package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

/**
 * The type Zoom api service.
 */
@Service
public class ZoomApiService {

    @Value("${zoom.bot.client.id}")
    private String clientId;

    @Value("${zoom.bot.client.secret}")
    private String clientSecret;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    private String accessToken;
    private Instant tokenExpiryTime;

    /**
     * Instantiates a new Zoom api service.
     */
    public ZoomApiService() {
        this.okHttpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Gets access token.
     *
     * @return the access token
     * @throws IOException the io exception
     */
    public String getAccessToken() throws IOException {
        if(accessToken != null && Instant.now().isBefore(tokenExpiryTime.minusSeconds(60))) {
            return accessToken;
        }

        String url = "https://zoom.us/oauth/token?grant_type=account_credentials&account_id=" + clientId;
        String credentials = okhttp3.Credentials.basic(clientId, clientSecret);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", credentials)
                .post(okhttp3.RequestBody.create(new byte[0]))
                .build();

        try(Response response = okHttpClient.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode jsonNode = objectMapper.readTree(response.body().string());
            accessToken = jsonNode.get("access_token").asText();
            int expiresIn = jsonNode.get("expires_in").asInt();
            tokenExpiryTime = Instant.now().plusSeconds(expiresIn);

            System.out.println("Access token is occupied: " + accessToken);
            return accessToken;
        }
    }

    /**
     * Download recording string.
     *
     * @param downloadUrl the download url
     * @param meetingId   the meeting id
     * @return the string
     * @throws IOException the io exception
     */
    public String downloadRecording(String downloadUrl, String meetingId) throws IOException {
        String token = getAccessToken();

        Request request = new Request.Builder()
                .url(downloadUrl)
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();

        String filename = "zoom_" + meetingId + "_" + System.currentTimeMillis() + ".mp3";
        String filePath = "audio_storage/" + filename;

        new File("audio_storage").mkdirs();

        try(Response response = okHttpClient.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Failed to download recording: " + response);
            }

            try(InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                int total = 0;

                while((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    total += bytesRead;
                }
                System.out.println("Downloaded " + (total /1024 * 1024) + "MB");
            }
        }
        return filePath;
    }
}
