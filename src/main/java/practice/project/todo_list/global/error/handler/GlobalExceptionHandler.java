package practice.project.todo_list.global.error.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import practice.project.todo_list.global.error.code.CommonErrorCode;
import practice.project.todo_list.global.error.code.ErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.global.response.ErrorResponseDto;

import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException e) {
        return createResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return createResponseEntity(ex, CommonErrorCode.NOT_VALID_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return createResponseEntity(CommonErrorCode.NOT_VALID_ERROR, convertMessage(ex.getMessage()));
    }

    // PathVariable 변환 실패시 나오는 에러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return createResponseEntity(ex, CommonErrorCode.NOT_VALID_ERROR);
    }

    private String convertMessage(String message) {
        return message.split("\"")[1] + " is wrong format";
    }

    // URL은 존재하지만 HTTP 메서드가 잘못된 경우
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Worng method : " + ex.getMethod());
        return createResponseEntity(CommonErrorCode.NOT_EXIST_METHOD);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("djskaldjksal");
        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
    }

    // Content-Type이 잘못된 경우
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info(ex.getContentType().getType());
        return createResponseEntity(CommonErrorCode.NOT_SUPPORT_MEDIATYPE);
    }

    // URL 잘못 적은 경우 나오는 것
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Wrong url : " + ex.getResourcePath());
        return createResponseEntity(CommonErrorCode.NOT_FOUND_URL);
    }

    // RequestParameter를 뺴먹은 경우
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Missing request : " + ex.getParameterName());
        return createResponseEntity(CommonErrorCode.MISSING_REQUEST_PARAMETER);
    }

    // Valid에서 유효성 검사를 실패한 경우
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return createResponseEntity(ex, CommonErrorCode.NOT_VALID_ERROR);
    }

    // url 파라미터에 유효성 검사 실패한 경우
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Validated");
        return createResponseEntity(CommonErrorCode.NOT_VALID_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("timeOut");
        return super.handleAsyncRequestTimeoutException(ex, headers, status, request);
    }

    private ResponseEntity<Object> createResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponseDto(errorCode));
    }

    private ResponseEntity<Object> createResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponseDto(errorCode, message));
    }

    private ResponseEntity<Object> createResponseEntity(MethodArgumentNotValidException ex, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponseDto(errorCode, convertValidateError(ex)));
    }

    private List<ErrorResponseDto.ValidationError> convertValidateError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponseDto.ValidationError::of)
                .toList();
    }

    private ResponseEntity<Object> createResponseEntity(ConstraintViolationException ex, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponseDto(errorCode, convertValidateError(ex)));
    }

    private ResponseEntity<Object> createResponseEntity(MethodArgumentTypeMismatchException ex, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponseDto(errorCode, convertValidateError(ex)));
    }

    private List<ErrorResponseDto.ValidationError> convertValidateError(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(ErrorResponseDto.ValidationError::of)
                .toList();
    }

    private List<ErrorResponseDto.ValidationError> convertValidateError(MethodArgumentTypeMismatchException ex) {
        return List.of(ErrorResponseDto.ValidationError.of(ex));
    }
}
