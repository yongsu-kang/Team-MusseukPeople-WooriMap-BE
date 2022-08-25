package com.musseukpeople.woorimap.couple.exception;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class AlreadyBrokenCoupleException extends CoupleException{

    public AlreadyBrokenCoupleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
