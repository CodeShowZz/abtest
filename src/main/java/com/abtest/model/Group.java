package com.abtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author junlin_huang
 * @create 2021-06-08 10:54 PM
 **/

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Group {

    private String key;


    /**
     * 百分比
     **/
    private BigDecimal ratio;

    private String name;

    /**
     * 分组的开始位置
     */
    private int start;

    /**
     * 分组的结束位置
     */
    private int end;

}