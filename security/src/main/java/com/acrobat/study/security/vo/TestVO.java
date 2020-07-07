package com.acrobat.study.security.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xutao
 * @date 2020-07-07 15:15
 */
@Data
@NoArgsConstructor
public class TestVO implements Serializable {

    @NotNull(message = "id不能为空")
    private Long id;
    @NotBlank(message = "名称不能为空")
    private String name;

    private TestSubVO subVO;
}
