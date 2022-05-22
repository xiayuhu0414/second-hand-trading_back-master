package com.second.hand.trading.server.Handler;

import com.second.hand.trading.server.Exception.ParamException;
import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.vo.ResultVo;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * controller的方法参数错误
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVo MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> collect = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResultVo.fail(ErrorMsg.PARAM_ERROR,collect);
    }

    /**
     * 缺少request body错误
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultVo HttpMessageNotReadableExceptionHandler() {
        return ResultVo.fail(ErrorMsg.MISSING_PARAMETER, "requestBody错误!");
    }

    /**
     * url中缺少Query Params
     * @param e e.getMessage()返回首个缺少的参数名
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultVo MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return ResultVo.fail(ErrorMsg.MISSING_PARAMETER, "缺少参数"+e.getParameterName());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVo ConstraintViolationExceptionHandler(ConstraintViolationException e) {

        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        Map<String, String> map = new HashMap<>();
        if (set.size() > 0) {
            for (ConstraintViolation<?> cv : set) {
                String[] param = cv.getPropertyPath().toString().split("\\.");
                String message = cv.getMessage();
                map.put(param[param.length - 1], message);
            }
        }

        return ResultVo.fail(ErrorMsg.PARAM_ERROR, map);
    }

    @ExceptionHandler(ParamException.class)
    public ResultVo ParamExceptionHandler(ParamException e) {
        return ResultVo.fail(ErrorMsg.PARAM_ERROR, e.getMap());
    }


    /**
     * 拦截cookie缺失异常
     * @return
     */
    @ExceptionHandler(MissingRequestCookieException.class)
    public ResultVo MissingRequestCookieExceptionHandler(){
        return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
    }
}
