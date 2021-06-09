package com.abtest.service;

import com.abtest.model.Group;
import com.abtest.model.Lab;
import com.abtest.util.SplitFlowUtil;
import com.abtest.zk.CreateNodeSync;
import com.abtest.zk.DeleteNodeSync;
import com.abtest.zk.GetChildrenSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junlin_huang
 * @create 2021-06-09 5:02 PM
 **/

@Service
public class LabService {

    Logger logger = LoggerFactory.getLogger(LabService.class);

    public boolean create(Lab lab) {
        try {
            SplitFlowUtil.assignRangeByRatio(lab);
            CreateNodeSync.createNode(lab);
        } catch (Exception e) {
            logger.error("error", e);
        }
        return true;
    }

    public boolean delete(Lab lab) {
        try {
            DeleteNodeSync.deleteNodeSync(lab.getProjectKey(),lab.getKey());
        } catch (Exception e) {
            logger.error("error",e);
        }
        return true;
    }

    public List<Group> query(String projectKey, String labKey) {
        List<Group> groupList = new ArrayList<>();
        try {
           groupList = GetChildrenSync.getChildren(projectKey, labKey);
       } catch (Exception e) {
           logger.error("error",e);
       }
       return groupList;
    }

    public Group partition(String projectKey, String labKey,String identify) {
        List<Group> groupList = query(projectKey,labKey);
        Lab lab = new Lab();
        lab.setProjectKey(projectKey);
        lab.setKey(labKey);
        lab.setGroupList(groupList);
        return SplitFlowUtil.partition(identify,lab);
    }

    public boolean update(Lab lab) {
        try {
            DeleteNodeSync.deleteNodeSync(lab.getProjectKey(), lab.getOldKey());
            create(lab);
        } catch (Exception e) {
            logger.error("error",e);
        }
        return true;
    }
}