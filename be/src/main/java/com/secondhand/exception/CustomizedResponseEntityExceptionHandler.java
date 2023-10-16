package com.secondhand.exception;

import com.secondhand.exception.ouath.GitHubRequestException;
import com.secondhand.exception.token.*;
import com.secondhand.exception.v2.DuplicatedException;
import com.secondhand.exception.v2.ErrorResponse;
import com.secondhand.exception.v2.NotFoundException;
import com.secondhand.exception.v2.UnAuthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
                        , e.getMessage(), false, request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImageUploadFailException.class)
    public final ResponseEntity<Object> handleAllRuntimeExceptions(ImageUploadFailException e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
                        , e.getMessage(), false, request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value()
                        , ex.getMessage(), false, request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingTokenException.class)
    public final ResponseEntity<Object> handleTokenExceptions(MissingTokenException e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public final ResponseEntity<Object> handleElementNotFoundExceptions(ElementNotFoundException e,
                                                                        WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TownNotFoundException.class)
    public final ResponseEntity<Object> handleTownNotFoundExceptions(TownNotFoundException e,
                                                                     WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public final ResponseEntity<Object> handleMemberNotFoundExceptions(MemberNotFoundException e,
                                                                       WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public final ResponseEntity<Object> handleGCategoryFoundExceptions(CategoryNotFoundException e,
                                                                       WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public final ResponseEntity<Object> handleStatusNotFoundExceptions(StatusNotFoundException e,
                                                                       WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotUserMineProductException.class)
    public final ResponseEntity<Object> handleNotUserMineProductExceptions(NotUserMineProductException e,
                                                                           WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GitHubRequestException.class)
    public final ResponseEntity<Object> handleAllGitHubRequestExceptions(GitHubRequestException e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JoinException.class)
    public final ResponseEntity<Object> handleAllJoinExceptionExceptions(JoinException e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value()
                        , e.getMessage(), false, request.getDescription(false));


        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TokenException.class)
    public final ResponseEntity<Object> handleAllAccessTokenTimeExceptions(TokenException e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND); //400
    }


    @ExceptionHandler(TokenTimeException.class)
    public final ResponseEntity<Object> handleAllAccessTokenTimeExceptions(TokenTimeException e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.UNAUTHORIZED.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED); //400
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public final ResponseEntity<Object> handleAllRefreshTokenTimeExceptions(TokenNotFoundException e, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value()
                        , e.getMessage(), false, request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, e.getMessage()));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(401, e.getMessage()));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedException(DuplicatedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(401, e.getMessage()));
    }
}
