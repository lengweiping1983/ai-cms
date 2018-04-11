package com.ai.env.handler;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.ai.common.bean.MediaTypes;
import com.ai.common.beanvalidator.BeanValidators;
import com.ai.common.exception.RestException;
import com.ai.common.exception.ServiceException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@ControllerAdvice
public class CommonExceptionHandler {
    /**
     * Log category to use when no mapped handler is found for a request.
     *
     * @see #pageNotFoundLogger
     */
    public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
    /**
     * Additional logger to use when no mapped handler is found for a request.
     *
     * @see #PAGE_NOT_FOUND_LOG_CATEGORY
     */
    protected static final Log pageNotFoundLogger = LogFactory.getLog(PAGE_NOT_FOUND_LOG_CATEGORY);
    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    /**
     * Provides handling for standard Spring MVC exceptions.
     * 
     * @param ex
     *            the target exception
     * @param request
     *            the current request
     */
    @ExceptionHandler(value = {NoSuchRequestHandlingMethodException.class, HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class, MissingServletRequestParameterException.class,
            ServletRequestBindingException.class, ConversionNotSupportedException.class, TypeMismatchException.class, HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class, MethodArgumentNotValidException.class, MissingServletRequestPartException.class, BindException.class})
    public final ResponseEntity<Object> handleException(ServletException ex, WebRequest request) {
        logger.error("ServletException:" + ex.getMessage());
        ex.getMessage();

        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof NoSuchRequestHandlingMethodException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleNoSuchRequestHandlingMethod((NoSuchRequestHandlingMethodException) ex, headers, status, request);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
            return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, headers, status, request);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, headers, status, request);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
            return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, headers, status, request);
        } else if (ex instanceof MissingServletRequestParameterException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, headers, status, request);
        } else if (ex instanceof ServletRequestBindingException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleServletRequestBindingException((ServletRequestBindingException) ex, headers, status, request);
        } else if (ex instanceof MissingServletRequestPartException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMissingServletRequestPart((MissingServletRequestPartException) ex, headers, status, request);
        } else {
            logger.warn("Unknown exception type: " + ex.getClass().getName());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    /**
     * A single place to customize the response body of all Exception types.
     * This method returns {@code null} by default.
     * 
     * @param ex
     *            the exception
     * @param body
     *            the body to use for the response
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     */
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<Object>(body, headers, status);
    }

    /**
     * Customize the response for NoSuchRequestHandlingMethodException. This
     * method logs a warning and delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        pageNotFoundLogger.warn(ex.getMessage());

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpRequestMethodNotSupportedException. This
     * method logs a warning, sets the "Allow" header, and delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        pageNotFoundLogger.warn(ex.getMessage());

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!supportedMethods.isEmpty()) {
            headers.setAllow(supportedMethods);
        }

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpMediaTypeNotSupportedException. This
     * method sets the "Accept" header and delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpMediaTypeNotAcceptableException. This
     * method delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for MissingServletRequestParameterException. This
     * method delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for ServletRequestBindingException. This method
     * delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for ConversionNotSupportedException. This method
     * delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for TypeMismatchException. This method delegates
     * to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpMessageNotReadableException. This method
     * delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpMessageNotWritableException. This method
     * delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for MethodArgumentNotValidException. This method
     * delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for MissingServletRequestPartException. This
     * method delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for BindException. This method delegates to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for NoHandlerFoundException. This method delegates
     * to
     * {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatus, WebRequest)}
     * .
     * 
     * @param ex
     *            the exception
     * @param headers
     *            the headers to be written to the response
     * @param status
     *            the selected response status
     * @param request
     *            the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    @ExceptionHandler(value = {RestException.class})
    public final ResponseEntity<?> handleException(RestException ex, WebRequest request) {
        logger.error("RestException:" + ex.getMessage());
        ex.printStackTrace();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        return handleExceptionInternal(ex, ex.getMessage(), headers, ex.status, request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public final ResponseEntity<?> handleException(ConstraintViolationException ex, WebRequest request) {
        logger.error("ConstraintViolationException:" + ex.getMessage());
        ex.printStackTrace();
        Map<String, String> errors = BeanValidators.extractPropertyAndMessage(ex.getConstraintViolations());
        // String body = jsonMapper.toJson(errors);
        StringBuilder sb = new StringBuilder();
        for (String key : errors.keySet()) {
            sb.append(key).append(":").append(errors.get(key)).append(errors.size() > 1 ? "<br/>" : "");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        return handleExceptionInternal(ex, "数据验证失败：<br/>" + sb.toString(), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public final ResponseEntity<?> handleException(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("DataIntegrityViolationException:" + ex.getMessage());
        ex.printStackTrace();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        return handleExceptionInternal(ex, "操作失败！", headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {MySQLIntegrityConstraintViolationException.class})
    public final ResponseEntity<?> handleException(MySQLIntegrityConstraintViolationException ex, WebRequest request) {
        logger.error("MySQLIntegrityConstraintViolationException:" + ex.getMessage());
        ex.printStackTrace();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        return handleExceptionInternal(ex, "操作失败！", headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {ServiceException.class})
    public final ResponseEntity<?> handleServiceException(ServiceException ex, WebRequest request) {
        logger.error("ServiceException:" + ex.getMessage());
        ex.printStackTrace();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        return handleExceptionInternal(ex, "操作失败：<br/>" + ex.getMessage(), headers, HttpStatus.BAD_REQUEST, request);
    }

}
