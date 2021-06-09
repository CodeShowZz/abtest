package com.abtest.controller;

import com.abtest.model.Group;
import com.abtest.model.Lab;
import com.abtest.service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author junlin_huang
 * @create 2021-06-09 4:37 PM
 **/

@RestController
@RequestMapping("/lab")
public class LabController {

    @Autowired
    private LabService labService;

    /**
     * 创建实验
     * @param lab
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public boolean create(@RequestBody Lab lab) {
        return labService.create(lab);
    }

    /**
     * 根据projectKey和labKey删除实验
     * @param lab
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public boolean delete(@RequestBody Lab lab) {
        return labService.delete(lab);
    }

    /**
     * 根据projectKey和labKey查询实验下的分组
     * @param projectKey
     * @param labKey
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public List<Group> query(@RequestParam String projectKey, @RequestParam String labKey) {
        return labService.query(projectKey,labKey);
    }

    /**
     * 根据projectKey和labKey还有identify来进行分流 得到某个分组
     * @param projectKey
     * @param labKey
     * @param identify
     * @return
     */
    @RequestMapping(value = "/partition", method = RequestMethod.GET)
    public Group partition(@RequestParam String projectKey, @RequestParam String labKey,@RequestParam String identify) {
        return labService.partition(projectKey,labKey,identify);
    }

    /**
     * 更新实验
     * @param lab
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public boolean update(@RequestBody Lab lab) {
        return labService.update(lab);
    }
}