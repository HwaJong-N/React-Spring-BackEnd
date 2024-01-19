package ghkwhd.apiServer.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {

    /**
     * 없는 내용을 조회하는 경우
     * 해당 예외를 처리하지 않으면 사용자는 500 에러를 받게 되는데 이것을 NOT_FOUND 로 변경해서 return
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> notExist(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", e.getMessage()));
    }

    /**
     * page 에 숫자가 아닌 다른 자료형이 들어온 경우 400 에러가 발생하게 된다
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notMathParam(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg", e.getMessage()));
    }
}
