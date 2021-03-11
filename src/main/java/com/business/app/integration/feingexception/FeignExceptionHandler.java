package com.business.app.integration.feingexception;

import com.business.app.exception.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignExceptionHandler implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String msg, Response response) {

        if (HttpStatus.NOT_FOUND.value() == response.status()) {
            return new CustomException(msg, HttpStatus.NOT_FOUND);
        }

        return defaultErrorDecoder.decode(msg, response);
    }
}