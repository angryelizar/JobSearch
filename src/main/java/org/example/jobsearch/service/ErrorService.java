package org.example.jobsearch.service;

import org.example.jobsearch.exceptions.ErrorResponseBody;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
@Service
public interface ErrorService {
    ErrorResponseBody makeResponse(Exception exception);

    ErrorResponseBody makeResponse(BindingResult exception);
}
