package practice.project.todo_list.global.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import practice.project.todo_list.global.error.code.CommonErrorCode;
import practice.project.todo_list.global.error.code.ErrorCode;
import practice.project.todo_list.global.error.exception.BusinessException;
import practice.project.todo_list.global.response.ErrorResponseDto;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(ErrorCode errorCode) {
        return createResponseEntity(errorCode);
    }

//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return createResponseEntity(CommonErrorCode.NOT_FOUND_URL);
//    }


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
}
