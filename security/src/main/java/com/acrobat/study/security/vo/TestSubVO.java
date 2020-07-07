package com.acrobat.study.security.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xutao
 * @date 2020-07-07 15:21
 */
@Data
@NoArgsConstructor
public class TestSubVO implements Serializable {

    @NotNull(message = "subId不能为空")
    private Long subId;
}
