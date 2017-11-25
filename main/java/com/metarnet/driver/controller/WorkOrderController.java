package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.metarnet.core.common.adapter.AAAAAdapter;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.GeneralInfoModel;
import com.metarnet.core.common.service.IWorkflowBaseService;
import com.metarnet.core.common.utils.SpringContextUtils;
import com.metarnet.core.common.workflow.Participant;
import com.metarnet.core.common.workflow.ProcessModelParams;
import com.metarnet.core.common.workflow.TaskFilter;
import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.driver.model.FlowNodeSettingEntity;
import com.metarnet.driver.model.GeneralProcessEntity;
import com.metarnet.driver.service.IFlowNodeSettingService;
import com.metarnet.driver.service.IGeneralProcessService;
import com.metarnet.driver.service.IProcessService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
//import com.unicom.ucloud.workflow.filters.TaskFilter;
//import com.unicom.ucloud.workflow.objects.Participant;
//import com.unicom.ucloud.workflow.objects.ProcessModelParams;
 import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/10/0010.
 */
@Controller
public class WorkOrderController extends BaseController{

    Logger logger = LogManager.getLogger("WorkOrderController");

    @Resource
    private IWorkflowBaseService workflowBaseService;

    @Resource
    private IFlowNodeSettingService flowNodeSettingService;

    @RequestMapping(value = "/workOrderController.do" , params = "method=launch")
    @ResponseBody
    public void launch(HttpServletRequest request, HttpServletResponse response , String formData , String formId , String tenantId , String processModelName) throws UIException {

        JSONObject jsonObject = new JSONObject();
        try {

            JSONObject formContainer = (JSONObject) JSON.parse(formData);
            JSONObject formDataMap = (JSONObject) formContainer.get("map");

            String formDataId = formDataMap.getString("formDataId");
            String workOrderNum = formDataMap.getString("work_order_num");
            String OPER_DESC = formDataMap.getString("OPER_DESC");

            UserEntity userEntity = getUserEntity(request);
            logger.info("current user username=" + userEntity.getUserName());

            Participant participant = new Participant();
            participant.setParticipantID(userEntity.getUserName());

            Map<String, Object> bizModleParams = new HashMap<String, Object>();
            bizModleParams.put("jobID" , new Date().getTime() + "");

            if(workOrderNum == null || "".equals(workOrderNum)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
                workOrderNum = sdf.format(new Date());
            }
            bizModleParams.put("jobCode" , workOrderNum);

            ProcessModelParams processModelParams = new ProcessModelParams();

            String processInstID = WorkflowAdapter.startProcess(userEntity.getUserName() + "," + tenantId, processModelName, null, bizModleParams, processModelParams);
            logger.info("startProcess SUCCESS,processInstID=" + processInstID);

            TaskFilter taskFilter = new TaskFilter();
            taskFilter.setProcessInstID(processInstID);

            TaskInstance taskInstance = workflowBaseService.getTaskInstance(processInstID, userEntity.getUserName() + "," + tenantId);
            logger.info("getTaskInstance SUCCESS,TaskInstID=" + taskInstance.getTaskInstID());
            if(taskInstance.getTaskInstID() == null){
                List<TaskInstance> list = workflowBaseService.getMyWaitingTasks(taskFilter , userEntity.getUserName());
                if(list != null && list.size() > 0){
                    taskInstance = list.get(0);
                }
            }
            logger.info("getTaskInstance SUCCESS second time,TaskInstID=" + taskInstance.getTaskInstID());

            //设置相关数据区参数
            Map<String, Object> relaDatas = new HashMap<String, Object>();
            JSONObject PARTICIPANT_RULE = new JSONObject();
            PARTICIPANT_RULE.put("orgcode" , userEntity.getOrgID());
            PARTICIPANT_RULE.put("majorcode" , "ALL");
            relaDatas.put("PARTICIPANT_RULE", PARTICIPANT_RULE.toJSONString());
            WorkflowAdapter.setRelativeData(processInstID, relaDatas, userEntity.getUserName() + "," + tenantId);

            WorkflowAdapter.submitTask(userEntity.getUserName() + "," + tenantId, taskInstance, null);
            logger.info("submitTask SUCCESS,TaskInstID=" + taskInstance.getTaskInstID());

            //记录通用处理信息
            GeneralInfoModel generalInfoModel = new GeneralInfoModel();
            generalInfoModel.setFormDataId(formDataId);
            generalInfoModel.setFormId(formId);
            generalInfoModel.setTenantId(tenantId);
            generalInfoModel.setOperDesc(OPER_DESC);
            generalInfoModel.setFormType("1");

            workflowBaseService.saveGeneralInfo(generalInfoModel , taskInstance , userEntity);

            jsonObject.put("success" , true);
        } catch (Exception e) {
            jsonObject.put("success" , false);
            e.printStackTrace();
        }
        endHandle(request, response, jsonObject, "");

    }

    @RequestMapping(value = "/workOrderController.do" , params = "method=submit")
    @ResponseBody
    public void submit(HttpServletRequest request, HttpServletResponse response , String formData , String formId , String tenantId , TaskInstance taskInstance) throws UIException {

        JSONObject jsonObject = new JSONObject();
        try {

            JSONObject formContainer = (JSONObject) JSON.parse(formData);
            JSONObject formDataMap = (JSONObject) formContainer.get("map");

            String formDataId = formDataMap.getString("formDataId");
            String OPER_TYPE = formDataMap.getString("OPER_TYPE");
            String OPER_DESC = formDataMap.getString("OPER_DESC");

            UserEntity userEntity = getUserEntity(request);

            if(OPER_TYPE != null && !"".equals(OPER_TYPE)){
                try{
                    IProcessService processService = (IProcessService) SpringContextUtils.getBean(OPER_TYPE);
                    if(processService != null){
                        formDataMap.put("tenantId" , tenantId);
                        processService.process(formDataMap , taskInstance , userEntity);
                    } else {
                        WorkflowAdapter.submitTask(userEntity.getUserName() + "," + tenantId, taskInstance, null);
                    }
                }catch (Exception e){
                    logger.info("跟据OPER_TYPE获取bean失败");
                    WorkflowAdapter.submitTask(userEntity.getUserName() + "," + tenantId, taskInstance, null);
                }

            } else {
                WorkflowAdapter.submitTask(userEntity.getUserName() + "," + tenantId, taskInstance, null);
            }

            //记录通用处理信息
            GeneralInfoModel generalInfoModel = new GeneralInfoModel();
            generalInfoModel.setFormDataId(formDataId);
            generalInfoModel.setFormId(formId);
            generalInfoModel.setTenantId(tenantId);
            generalInfoModel.setOperDesc(OPER_DESC);
            generalInfoModel.setFormType("1");

            workflowBaseService.saveGeneralInfo(generalInfoModel , taskInstance , userEntity);
            jsonObject.put("success" , true);
        } catch (Exception e) {
            jsonObject.put("success" , false);
            e.printStackTrace();
        }
        endHandle(request, response, jsonObject, "");

    }

    @RequestMapping(value = "/addLog.do")
    @ResponseBody
    public void addLog(HttpServletRequest request, HttpServletResponse response , GeneralInfoModel generalInfoModel , String taskInstanceStr , int userId) throws UIException {

        JSONObject jsonObject = new JSONObject();
        try {

            UserEntity userEntity = AAAAAdapter.findUserbyUserID(userId);

            //记录通用处理信息
//            GeneralInfoModel generalInfoModel = new GeneralInfoModel();
//            generalInfoModel.setFormDataId(formDataId);
//            generalInfoModel.setFormId(formId);
//            generalInfoModel.setTenantId(tenantId);
//            generalInfoModel.setOperDesc(OPER_DESC);

            if(!"".equals(taskInstanceStr) && taskInstanceStr.startsWith("%")){
                taskInstanceStr = java.net.URLDecoder.decode(taskInstanceStr , "UTF-8");
            }
            TaskInstance taskInstance = JSON.parseObject(taskInstanceStr , TaskInstance.class);

            String activityInstName = taskInstance.getActivityInstName();
            if(activityInstName != null && activityInstName.startsWith("%")){
                taskInstance.setActivityInstName(java.net.URLDecoder.decode(activityInstName , "UTF-8"));
            }
            String jobTitle = taskInstance.getJobTitle();
            if(jobTitle != null && jobTitle.startsWith("%")){
                taskInstance.setJobTitle(java.net.URLDecoder.decode(jobTitle , "UTF-8"));
            }
            String operDesc = generalInfoModel.getOperDesc();
            if(operDesc != null && operDesc.startsWith("%")){
                generalInfoModel.setOperDesc(java.net.URLDecoder.decode(operDesc, "UTF-8"));
            }

            FlowNodeSettingEntity flowNodeSettingEntity = new FlowNodeSettingEntity();
            flowNodeSettingEntity.setProcessModelName(taskInstance.getProcessModelName());
            flowNodeSettingEntity.setActivityDefID(taskInstance.getActivityDefID());
            flowNodeSettingEntity.setProcessModelId(taskInstance.getProcessModelId());

            flowNodeSettingEntity = (FlowNodeSettingEntity) flowNodeSettingService.getSetting(flowNodeSettingEntity);

            String areaName = flowNodeSettingEntity.getAreaName();
            if(areaName != null && !"".equals(areaName)){
                generalInfoModel.setAreaName(areaName);
            }

            workflowBaseService.saveGeneralInfo(generalInfoModel , taskInstance , userEntity);
            jsonObject.put("success" , true);
        } catch (Exception e) {
            jsonObject.put("success" , false);
            e.printStackTrace();
        }
        endHandle(request, response, jsonObject, "");

    }
}
