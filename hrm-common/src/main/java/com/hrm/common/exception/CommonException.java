package com.hrm.common.exception;

import com.hrm.common.entity.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author LZL
 * @date 2022/3/10-6:30
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommonException extends Exception {
    private ResultCode resultCode;
}
