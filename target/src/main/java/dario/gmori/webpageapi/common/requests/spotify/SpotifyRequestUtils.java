package dario.gmori.webpageapi.common.requests.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import dario.gmori.webpageapi.common.requests.HttpRestService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:spotify.properties")
public class SpotifyRequestUtils {
    private final HttpRestService httpRestService;
    private final ResourceLoader resourceLoader;
    @Value("${spotify.token}")
    private String token;
    @Value("${spotify.basicUser}")
    private String basicUser;
    @Value("${spotify.basicPass}")
    private String basicPass;
    @Value("${spotify.refreshToken}")
    private String refreshToken;
    public JsonNode getSpotifyInformation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        try{
            return httpRestService.makeGetRequest("https://api.spotify.com/v1/me", headers);
        } catch (HttpClientErrorException e){
            if(e.getStatusCode().value() == 401){
                refreshToken();
                headers.setBearerAuth(token);
                return httpRestService.makeGetRequest("https://api.spotify.com/v1/me", headers);
            }
            throw new RuntimeException("Error getting Spotify information");
        }
    }


    public JsonNode getTopSongs() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        try{
            return httpRestService.makeGetRequest("https://api.spotify.com/v1/me/top/tracks?limit=5&time_range=short_term", headers);
        } catch (HttpClientErrorException e){
            if(e.getStatusCode().value() == 401){
                refreshToken();
                headers.setBearerAuth(token);
                return httpRestService.makeGetRequest("https://api.spotify.com/v1/me/top/tracks?limit=5&time_range=short_term", headers);
            }
            throw new RuntimeException("Error getting Spotify songs");
        }
    }

    public JsonNode getTopArtists() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        try{
            return httpRestService.makeGetRequest("https://api.spotify.com/v1/me/top/artists?limit=5&time_range=short_term", headers);
        } catch (HttpClientErrorException e){
            if(e.getStatusCode().value() == 401){
                refreshToken();
                headers.setBearerAuth(token);
                return httpRestService.makeGetRequest("https://api.spotify.com/v1/me/top/artists?limit=5&time_range=short_term", headers);
            }
            throw new RuntimeException("Error getting Spotify artists");
        }
    }

    public void refreshToken() {
        //Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(basicUser, basicPass);
        // Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token",refreshToken);
        body.add("grant_type","refresh_token");

        JsonNode responseBody = httpRestService.makePostRequest("https://accounts.spotify.com/api/token", headers, body);
        if(responseBody != null){
            // Get the new token from the response body
            token = responseBody.findValue("access_token").textValue();
        } else{
            throw new RuntimeException("Error refreshing token");
        }
        // Save the new token
        saveProperty("spotify.properties","spotify.token", token);

    }

    private void saveProperty(String file, String key, String value){
        try {
            // Load existing properties
            Resource resource = resourceLoader.getResource("classpath:" + file);
            Properties credentials = new Properties();
            try (InputStream in = resource.getInputStream()) {
                credentials.load(in);
            }

            // Update the specific key-value pair
            credentials.setProperty(key, value);

            // Store the updated properties back to the file
            try (OutputStream out = new FileOutputStream(resource.getFile())) {
                credentials.store(out, key + " updated");
            }
        } catch (IOException e) {
            System.out.println("Error updating property: " + e.getMessage());
        }
    }

}
