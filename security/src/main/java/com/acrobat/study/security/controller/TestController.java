package com.acrobat.study.security.controller;

import com.acrobat.study.security.utils.JacksonUtil;
import com.acrobat.study.security.vo.TestVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xutao
 * @date 2020-07-07 15:25
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试参数校验的接口
     */
    @ResponseBody
    @RequestMapping("/validation")
    public String testValidation(@Valid @RequestBody TestVO testVO) throws JsonProcessingException {

        return JacksonUtil.writeValueAsString(testVO);
    }
}
