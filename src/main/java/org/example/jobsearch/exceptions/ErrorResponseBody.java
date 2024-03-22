package org.example.jobsearch.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ErrorResponseBody {
    private String title;
    private List<String> reasons;
}
