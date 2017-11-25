package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSONObject;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.exception.AdapterException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.service.IWorkflowBaseService;
import com.metarnet.core.common.workflow.Participant;
import com.metarnet.core.common.workflow.ProcessModelParams;
import com.metarnet.core.common.workflow.TaskFilter;
import com.metarnet.core.common.workflow.TaskInstance;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import net.sf.json.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.unicom.ucloud.workflow.objects.ProcessModelParams;

//import com.unicom.ucloud.workflow.objects.Participant;
//import com.unicom.ucloud.workflow.objects.TaskInstance;
//import com.unicom.ucloud.workflow.filters.TaskFilter;

/**
 * Created by Administrator on 2016/11/10/0010.
 */
@Controller
public class WorkflowTestController extends CommonController {

    Logger logger = LogManager.getLogger("ComponentForm");

    @Resource()
    private IWorkflowBaseService workflowBaseService;


    @RequestMapping(value = "/test/start.do")
    @ResponseBody
    public void start(HttpServletRequest request, HttpServletResponse response) throws UIException {

        UserEntity userEntity = getUserEntity(request);
        userEntity.setUserName("ght");
        logger.info("current user username=" + userEntity.getUserName());
        System.out.println("current user username=" + userEntity.getUserName());

        Participant participant = new Participant();
        participant.setParticipantID(userEntity.getUserName());

        Map<String, Object> bizModleParams = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        bizModleParams.put("jobID" , new Date().getTime() + "");
        bizModleParams.put("jobCode" , sdf.format(new Date()));
        bizModleParams.put("jobTitle" , sdf.format(new Date()) + "-" + userEntity.getTrueName());

        ProcessModelParams processModelParams = new ProcessModelParams();

        String processInstID = null;
        try {
            processInstID = WorkflowAdapter.startProcess(userEntity.getUserName(), "demo-1", participant, bizModleParams, processModelParams);
        } catch (AdapterException e) {
            e.printStackTrace();
        }
        logger.info("startProcess SUCCESS,processInstID=" + processInstID);
        System.out.println("startProcess SUCCESS,processInstID=" + processInstID);

        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setProcessInstID(processInstID);

        TaskInstance taskInstance = null;
        try {
            taskInstance = workflowBaseService.getTaskInstance(processInstID, userEntity.getUserName());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        logger.info("getTaskInstance SUCCESS,TaskInstID=" + taskInstance.getTaskInstID());
        if(taskInstance.getTaskInstID() == null){
            List<TaskInstance> list = null;
            try {
                list = workflowBaseService.getMyWaitingTasks(taskFilter , userEntity.getUserName());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            if(list != null && list.size() > 0){
                taskInstance = list.get(0);
            }
        }
        logger.info("getTaskInstance SUCCESS second time,TaskInstID=" + taskInstance.getTaskInstID());

        //设置相关数据区参数
        Map<String, Object> relaDatas = new HashMap<String, Object>();
//        JSONObject PARTICIPANT_RULE = new JSONObject();
//        PARTICIPANT_RULE.put("orgcode" , userEntity.getOrgID());
//        PARTICIPANT_RULE.put("majorcode" , "ALL");
//        relaDatas.put("PARTICIPANT_RULE", PARTICIPANT_RULE.toJSONString());
        relaDatas.put("orgcode", userEntity.getOrgID());
        relaDatas.put("majorcode", "ALL");
        relaDatas.put("nextStep", "");
        try {
            WorkflowAdapter.setRelativeData(processInstID, relaDatas, userEntity.getUserName());
        } catch (AdapterException e) {
            e.printStackTrace();
        }

        List<Participant> participants = new ArrayList<Participant>();
        Participant participant1 = new Participant();
        participant1.setParticipantID("root");
        participants.add(participant1);

        try {
            WorkflowAdapter.submitTask(userEntity.getUserName(), taskInstance, participants);
        } catch (AdapterException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/test/submit.do")
    @ResponseBody
    public void submit(HttpServletRequest request, HttpServletResponse response) throws UIException {

    }

}
