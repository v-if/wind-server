package com.github.tkpark.wind;

import com.github.tkpark.interceptor.LoggingRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

@Service
@Slf4j
public class CommonService {

    // 트랜잭션 타임아웃 (초단위)
    public static final int TRANSACTION_TIMEOUT = 5;

    // 요청 : 연결 타임아웃 (밀리초단위)
    public static final int REQUEST_CONNECT_TIMEOUT = 3000;

    // 요청 : 조회 타임아웃 (밀리초단위)
    public static final int REQUEST_READ_TIMEOUT = 5000;

    public <T> ResponseEntity<T> request(URI url, HttpMethod method, HttpEntity<?> reqEntity, ParameterizedTypeReference<T> responseType) throws ResourceAccessException, HttpServerErrorException {
        RestTemplate restTemplate = getRestTemplate();
        try {
            return restTemplate.exchange(url, method, reqEntity, responseType);
        } catch (HttpStatusCodeException e) {
            return (ResponseEntity<T>) ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
        }
    }

    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Arrays.asList(new LoggingRequestInterceptor()));
        restTemplate.setRequestFactory(restTemplateRequestFactory(REQUEST_CONNECT_TIMEOUT, REQUEST_READ_TIMEOUT));

        return restTemplate;
    }

    public ClientHttpRequestFactory restTemplateRequestFactory(int connectTimeout, int readTimeout) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        return new BufferingClientHttpRequestFactory(factory);
    }
}
