package com.ai.common.exception;

import org.springframework.validation.BindingResult;

import com.ai.common.bean.ResultCode;

/**
 * Validator校验异常.
 *
 */
public class ParamValidationException extends ServiceException {
    private static final long serialVersionUID = 1L;

    private BindingResult bindingResult;

    public ParamValidationException(final BindingResult bindingResult) {

        super(ResultCode.ILLEGAL_ARGUMENT.value(),
                "["
                        + bindingResult.getFieldError().getField()
                        + "]"
                        + (bindingResult.getFieldError().isBindingFailure() ? ResultCode.ILLEGAL_ARGUMENT.message() : bindingResult.getFieldError()
                                .getDefaultMessage()));
        this.bindingResult = bindingResult;
    }

    public ParamValidationException(String msg) {
        super(ResultCode.ILLEGAL_ARGUMENT.value(), msg);
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

}
