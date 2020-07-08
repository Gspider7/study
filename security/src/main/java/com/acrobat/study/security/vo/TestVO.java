package com.acrobat.study.security.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 2, max = 10, message = "name 长度必须在 {min} - {max} 之间")
    private String name;

    private TestSubVO subVO;
}
