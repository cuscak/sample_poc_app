package andrej.cuscak.dms.advice;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiError(
        HttpStatus status,
        String field,
        List<String> errors
) {
}
