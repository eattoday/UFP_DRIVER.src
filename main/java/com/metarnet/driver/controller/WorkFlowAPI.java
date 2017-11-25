package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.adapter.WorkflowAdapter_bps;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.AdapterException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.core.common.workflow.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ght on 2017-11-17 20:12:02
 */

public class WorkFlowAPI  {

    static String URL="http://10.225.222.203:8083/UFP_DRIVER/";

    public static void main(String[] args) {
        JSONObject jsonObject = WorkFlowAPI.startProcess("root", "['root']",
                "demo-1", null,
                null, "default",
                "11");
         System.out.println(jsonObject.toJSONString());
    }

    /**
     *  1.启动流程
     * @param accountId     必须
     * @param participants  必须
     * @param processModelID    必须
     * @param processModelParams
     * @param bizModleParam
     * @param tenantId  必须
     * @param formDataId    必须
     * @return
     */
    public static JSONObject startProcess(String accountId, String participants,
                                          String processModelID, String processModelParams,
                                          String bizModleParam, String tenantId,
                                          String formDataId) {
        RestTemplate restTemplate = new RestTemplate();
        if(processModelParams==null)
            processModelParams="";
        if(bizModleParam==null)
            bizModleParam="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                        "workFlowController.do?method=startProcess" +
                                "&accountId=" +accountId+
                                "&processModelID=" +processModelID+
                                "&participants={participants}" +
                                "&tenantId=" +tenantId+
                                "&formId=" +"1"+
                                "&formDataId=" +formDataId+
                                "&formType="+"1"+
                                "&processModelParams={processModelParams}"+
                                "&bizModleParam={bizModleParam}"
                                ,
                        null, JSONObject.class,
                        participants,
                        processModelParams,
                        bizModleParam
                        );
        JSONObject body = forEntity1.getBody();
        return body;
    }


    /**
     * 2.查询待办
     * @param accountId 必须
     * @param startRecord
     * @param pageSize
     * @param processInstID
     * @param tenantId
     * @return
     */
    public static JSONObject getMyWaitingTasks(String accountId, String startRecord, String pageSize,
                                               String processInstID, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(startRecord==null)
           startRecord="";
        if(pageSize==null)
           pageSize="";
        if(processInstID==null)
           processInstID="";
        if(tenantId==null)
           tenantId="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                        "workFlowController.do?method=getMyWaitingTasks" +
                                "&accountId=" +accountId+
                                "&startRecord=" +startRecord+
                                "&pageSize=" +pageSize+
                                "&processModelID=" +processInstID+
                                "&tenantId=" +tenantId
                                ,
                        null, JSONObject.class
                        );
        JSONObject body = forEntity1.getBody();
        return body;
    }

    /**
     * 4.查询已办
     * @param accountId
     * @param startRecord
     * @param pageSize
     * @param processInstID
     * @param tenantId
     * @return
     */
    public static JSONObject getMyCompletedTasks(String accountId, String startRecord, String pageSize, String processInstID,
                                                 String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
           accountId="";
        if(startRecord==null)
           startRecord="";
        if(pageSize==null)
           pageSize="";
        if(processInstID==null)
           processInstID="";
        if(tenantId==null)
           tenantId="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                        "workFlowController.do?method=getMyCompletedTasks" +
                                "&accountId=" +accountId+
                                "&startRecord=" +startRecord+
                                "&pageSize=" +pageSize+
                                "&processModelID=" +processInstID+
                                "&tenantId=" +tenantId
                                ,
                        null, JSONObject.class
                        );
        JSONObject body = forEntity1.getBody();
        return body;
    }
    /**
     * 5.查询已办
     * 基于同一用户的合并
     * @param accountId     必须
     * @param startRecord
     * @param pageSize
     * @param tenantId
     * @return
     */
    public static JSONObject getMyCompletedTasksDistinctJobId(String startRecord, String pageSize,
                                                              String accountId, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(startRecord==null)
           startRecord="";
        if(pageSize==null)
           pageSize="";
        if(tenantId==null)
           tenantId="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                        "workFlowController.do?method=getMyCompletedTasksDistinctJobId" +
                                "&accountId=" +accountId+
                                "&startRecord=" +startRecord+
                                "&pageSize=" +pageSize+
                                "&tenantId=" +tenantId
                                ,
                        null, JSONObject.class
                        );
        JSONObject body = forEntity1.getBody();
        return body;
    }




    /**
     * 7.根据活动实例ID获取任务实例ID
     *
     * @param accountId      用户ID    非必须
     * @param activityInstID 活动实例ID  必须
     * @param tenantId       租户ID    非必须
     * @return
     */
    public static JSONObject getTaskInstancesByActivityID(String accountId, String activityInstID, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
           tenantId="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                        "workFlowController.do?method=getTaskInstancesByActivityID" +
                                "&accountId=" +accountId+
                                "&activityInstID=" +activityInstID+
                                "&tenantId=" +tenantId
                                ,
                        null, JSONObject.class
                        );
        JSONObject body = forEntity1.getBody();
        return body;
    }


    /**
     * 8.提交待办
     *
     * @param taskInstanceID 任务实例ID  必须
     * @param participants   候选人列表   必须  ["aa","bb"]
     * @param accountId      用户ID    必须
     * @param tenantId       租户ID    必须
     * @param formDataId     表单数据ID  必须
     */
    public static JSONObject submitTask(String taskInstanceID, String participants,
                                        String accountId, String tenantId,
                                         String formDataId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=submitTask" +
                                "&accountId=" +accountId+
                                "&taskInstanceID=" +taskInstanceID+
                                "&participants={participants}" +
                                "&tenantId=" +tenantId+
                                "&formId=" +"1"+
                                "&formDataId=" +formDataId+
                                "&formType="+"1"
                        ,
                        null, JSONObject.class,
                        participants
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }


    /**
     *  9.设置相关数据
     *
     * @param processInstID 流程实例ID  必须
     * @param relaData      相关数据    必须  {"aa":"bb","cc":"dd","list":["ee","ff"]}
     * @param accountId     用户ID    非必须
     * @param tenantId      租户ID    非必须
     */
    public static JSONObject setRelativeData(String processInstID, String relaData,
                                             String accountId, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=setRelativeData" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&processInstID=" +processInstID+
                                "&relaData={relaData}"
                        ,
                        null, JSONObject.class,
                        relaData
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }
    /**
     * 10.获取相关数据
     *
     * @param processInstID 流程实例ID  必须
     * @param keys          查找的数据的key键   必须   ["aa","bb"]
     * @param accountId     用户ID    非必须
     * @param tenantId      租户ID    非必须
     */
    public static JSONObject getRelativeData(String processInstID, String keys,
                                             String accountId, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=getRelativeData" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&processInstID=" +processInstID+
                                "&keys={keys}"
                        ,
                        null, JSONObject.class,
                        keys
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }



    /**
     * 12.获取流程实例流转过的活动
     * 若流程未结束,则数据集合的最后一个元素是当前待办
     *
     * @param accountId     用户ID    非必须
     * @param processInstID 流程实例ID  必须
     * @param tenantId      租户ID    非必须
     */
    public static JSONObject getActivityInstances(String processInstID,
                                             String accountId, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=getActivityInstances" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&processInstID=" +processInstID
                        ,
                        null, JSONObject.class
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }

    /**
     * 13.转办
     * 14.协办
     *
     * @param accountId      用户ID   必须
     * @param taskInstanceId 任务实例ID 必须
     * @param participantID  转办后执行人ID   必须
     */
    public static JSONObject forwardTask(String accountId, String taskInstanceId, String participantID) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=forwardTask" +
                                "&accountId=" +accountId+
                                "&taskInstanceId=" +taskInstanceId+
                                "&participantID=" +participantID
                        ,
                        null, JSONObject.class
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }
    /**
     * 19.根据流程实例ID获取流程对象
     *
     * @param accountId     用户ID    非必须
     * @param processInstID 流程实例ID  必须
     * @param tenantId      租户ID    非必须
     */
    public static JSONObject getProcessInstance(String accountId, String processInstID, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";

        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=getProcessInstance" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&processInstID=" +processInstID
                        ,
                        null, JSONObject.class
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }




    /**
     * 21.获取流程实例的子流程
     *
     * @param accountId     用户ID    非必须
     * @param processInstID 流程实例ID  必须
     * @param tenantId      租户ID    非必须
     */
    public static JSONObject getSubProcessInstance(String accountId, String processInstID, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";

        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=getSubProcessInstance" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&processInstID=" +processInstID
                        ,
                        null, JSONObject.class
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }
    /**
     * 44.获取根流程实例id
     *
     * @param accountId         用户ID    非必须
     * @param processInstanceId 流程实例ID  必须
     * @param tenantId          租户ID    非必须
     */
    public static JSONObject getRootProcessInstance(String accountId, String processInstanceId, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";

        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=getRootProcessInstance" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&processInstanceId=" +processInstanceId
                        ,
                        null, JSONObject.class
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }
    /**
     * 51.	根据业务主键jobID获取当前待办参数
     *
     * @param accountId 用户ID    非必须
     * @param jobId     业务主键ID  必须
     * @param tenantId  租户ID    非必须
     */
    public static JSONObject findDoingActivitysByJobID( String accountId, String jobId, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";

        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=findDoingActivitysByJobID" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&jobId=" +jobId
                        ,
                        null, JSONObject.class
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }
    /**
     * 22.根据任务实例ID获取任务实例对象
     *
     * @param accountId
     * @param taskInstId
     * @param tenantId
     */
    public static JSONObject getTaskInstanceObject( String accountId, String taskInstId, String tenantId) {
        RestTemplate restTemplate = new RestTemplate();
        if(accountId==null)
            accountId="";
        if(tenantId==null)
            tenantId="";

        ResponseEntity<JSONObject> forEntity1 =
                restTemplate.postForEntity(URL+
                                "workFlowController.do?method=getTaskInstanceObject" +
                                "&accountId=" +accountId+
                                "&tenantId=" +tenantId+
                                "&taskInstId=" +taskInstId
                        ,
                        null, JSONObject.class
                );
        JSONObject body = forEntity1.getBody();
        return body;
    }


}
