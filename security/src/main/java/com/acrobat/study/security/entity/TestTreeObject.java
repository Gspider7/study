package com.acrobat.study.security.entity;

import com.acrobat.study.security.utils.JacksonUtil;
import com.acrobat.study.security.utils.Tree;
import com.acrobat.study.security.utils.TreeObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author xutao
 * @date 2020-08-07 11:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestTreeObject implements TreeObject {

    private Long id;

    private String code;

    private String parentCode;

    private String content;


    @Override
    public Object getNodeId() {
        return code;
    }

    @Override
    public Object getParentNodeId() {
        return parentCode;
    }

    @Override
    public String getNodeText() {
        return content;
    }

    public static void main(String[] args) throws JsonProcessingException {
        TestTreeObject to1 = new TestTreeObject(1L, "a", null, ".");
        TestTreeObject to2 = new TestTreeObject(2L, "a1", "a", "..");
        TestTreeObject to3 = new TestTreeObject(3L, "a2", "a", "..");

        List<TestTreeObject> list = Arrays.asList(to1, to2, to3);
        System.out.println(JacksonUtil.writeValueAsString(list));

        List<Tree<TestTreeObject>> treeList = Tree.buildTree(list);
        System.out.println(JacksonUtil.writeValueAsString(treeList));
    }
}
