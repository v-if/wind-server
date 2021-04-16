package com.github.tkpark.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        traceRequest(request, body);

        ClientHttpResponse response = execution.execute(request, body);

        traceResponse(request, body, response);

        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        log.debug("REQUEST {} {} {}", request.getMethod(), request.getURI(), new String(body, "UTF-8"));
    }

    private void traceResponse(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\t');
            line = bufferedReader.readLine();
        }

        log.debug("RESPONSE {} {} {} / REQUEST {} {} {}",
                response.getStatusCode(), response.getStatusText(), inputStringBuilder.toString(),
                request.getMethod(), request.getURI(), new String(body, "UTF-8"));
    }
}
