package dario.gmori.webpageapi.common.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class HttpRestService {
    public JsonNode makeGetRequest(String uri, HttpHeaders headers){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(uri, HttpMethod.GET,entity,JsonNode.class).getBody();
    }

    public JsonNode makePostRequest(String uri, HttpHeaders headers, MultiValueMap<String, String> body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(uri, HttpMethod.POST, entity, JsonNode.class).getBody();
    }
}
