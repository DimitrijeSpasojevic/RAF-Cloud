package rs.raf.rafcloud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Moj razlog bacanja izuzetka")
public class MyException extends RuntimeException {
}
