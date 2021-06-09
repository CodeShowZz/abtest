package com.abtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author junlin_huang
 * @create 2021-06-08 10:51 PM
 **/

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Lab {

    /**
     * 分流需要的参数,由调用方传入,调用方决定分流所使用的标识
     */
    private String identify;

    /**
     * 某个项目的标识,在ZK中是第一级目录,以来区分各个项目的实验
     */
    private String projectKey;

    /**
     * 在进行更新时,需要传入变更前的实验分组key,以便于删除原来的实验
     */
    private String oldKey;

    private String key;

    private String name;

    private List<Group> groupList;

}