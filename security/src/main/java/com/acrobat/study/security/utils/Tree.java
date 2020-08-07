package com.acrobat.study.security.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用自动构建树结构，兼容旧的结构
 *
 * @author xutao
 * @date 2020-07-31 15:17
 */
@Data
public class Tree<T extends TreeObject> implements Serializable {

    /* 节点ID */
    private Object id;
    /* 父节点ID */
    private Object parentId;
    /* 显示节点文本 */
    private String text;
    /* 节点对象 */
    private T object;
    /* 节点属性 */
    private Map<String, Object> attributes;
    /* 节点是否被选中 true false */
    private boolean checked = false;
    /* 节点的子节点 */
    private List<Tree<T>> children = new ArrayList<>();

    // -------------------------------------------------------------------------------------

    public Tree() {
    }

    public Tree(T object) {
        this.object = object;

        this.id = object.getId();
        this.parentId = object.getParentId();
        this.text = object.getText();
    }

    // -------------------------------------------------------------------------------------

    /**
     * 通用方法：构建树结构
     */
    public static <T extends TreeObject> List<Tree<T>> buildTree(List<T> list) {
        /* key：id */
        Map<Object, Tree<T>> treeMap = new HashMap<>();
        // 加入map
        list.forEach(item -> {
            Object id = item.getId();

            if (!treeMap.containsKey(id)) {
                Tree<T> tree = new Tree<>(item);
                treeMap.put(id, tree);
            }
        });

        // 构建父子关系
        treeMap.forEach((id, tree) -> {
            Object parentId = tree.getParentId();
            if (parentId != null) {
                Tree<T> parent = treeMap.get(parentId);
                if (parent != null) {
                    parent.addChild(tree);
                } else {
                    tree.setParentId(null);                 // 要支持通过parentId判断上级是否存在，上级不存在时，必须设置parentId为null
                }
            }
        });

        // 返回
        return treeMap.values().stream().filter(tree -> tree.getParentId() == null).collect(Collectors.toList());
    }

    /**
     * 根据节点的text模糊匹配过滤节点，保留父子关系结构：即使父节点不满足过滤条件也要保留
     * @param treeList          树结构
     * @param textLike          匹配字符串
     */
    public static <T extends TreeObject> void filterByText(List<Tree<T>> treeList,
                                                           String textLike) {
        // 将树平铺
        Map<Object, Tree<T>> flatMap = new HashMap<>();

        List<Tree<T>> currentLevel = treeList;
        while (CollectionUtils.isNotEmpty(currentLevel)) {
            List<Tree<T>> nextLevel = new ArrayList<>();
            currentLevel.forEach(tree -> {
                nextLevel.addAll(tree.getChildren());
                flatMap.put(tree.getId(), tree);
            });
            currentLevel = nextLevel;
        }

        // 不断过滤，剔除不满足条件的节点
        while (true) {
            List<Tree<T>> deleteList = new ArrayList<>();
            flatMap.forEach((id, tree) -> {
                // 剔除条件：不匹配，且没有子节点
                if (tree.getChildren().size() == 0 && !tree.getText().contains(textLike)) {
                    deleteList.add(tree);

                    Tree<T> parent = flatMap.get(tree.getParentId());
                    if (parent != null) {
                        parent.getChildren().remove(tree);
                    }
                }
            });
            deleteList.forEach(tree -> flatMap.remove(tree.getId()));

            // 再没有可剔除的节点了
            if (deleteList.size() == 0) break;
        }

        treeList.clear();
        treeList.addAll(flatMap.values().stream().filter(item -> item.getParentId() == null).collect(Collectors.toList()));
    }

    // -------------------------------------------------------------------------------------

    private void addChild(Tree<T> child) {
        children.add(child);
    }
}
