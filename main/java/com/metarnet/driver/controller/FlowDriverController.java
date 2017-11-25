package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.metarnet.core.common.adapter.AAAAAdapter;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.client.WFServiceClient;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.GeneralInfoModel;
import com.metarnet.core.common.workflow.PageCondition;
import com.metarnet.core.common.workflow.ProcessInstance;
import com.metarnet.core.common.workflow.TaskFilter;
import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.driver.model.ComponentFormEntity;
import com.metarnet.driver.model.FlowNodeSettingEntity;
import com.metarnet.driver.service.IComponentFormService;
import com.metarnet.driver.service.IFlowNodeSettingService;
import com.metarnet.driver.service.IGeneralProcessService;
//import com.unicom.ucloud.workflow.filters.TaskFilter;
//import com.unicom.ucloud.workflow.objects.PageCondition;
//import com.unicom.ucloud.workflow.objects.ProcessInstance;
//import com.unicom.ucloud.workflow.objects.TaskInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import com.metarnet.core.common.workflow.TaskInstance;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
@Controller
public class FlowDriverController extends BaseController{

    Logger logger = LogManager.getLogger("FlowDriverController");

    @Resource
    private IFlowNodeSettingService flowNodeSettingService;

    @Resource
    private IGeneralProcessService generalProcessService;

    @Resource
    private IComponentFormService componentFormService;

    @RequestMapping(value = "/driverController.do")
    @ResponseBody
    public ModelAndView driver(HttpServletRequest request, HttpServletResponse response , TaskInstance taskInstance , String type , String globalUniqueID) throws UIException {

        JSONObject __buildJSON = new JSONObject();

        try{


//            WFServiceClient.getInstance().addLog(taskInstance , "11" , "测试日志" , "90" , 1059L);

            if(globalUniqueID != null && !"".equals(globalUniqueID)){
                request.getSession().setAttribute("globalUniqueID", globalUniqueID);
                request.getSession().setAttribute("globalUniqueUser", AAAAAdapter.getInstence().findUserBySessionID(globalUniqueID));
            }
//            UserEntity userEntity = getUserEntity(request);

            TaskFilter taskFilter = new TaskFilter();
            taskFilter.setProcessInstID(taskInstance.getProcessInstID());
            PageCondition page = new PageCondition();
            page.setBegin(0);
            page.setLength(1);
            page.setIsCount(true);
            taskFilter.setPageCondition(page);
            List<TaskInstance> tasks = null;
            if(taskInstance.getTaskInstID().startsWith("-")){
                if("waiting".equals(type)) {
                    tasks  = WorkflowAdapter.getMyWaitingTasks(taskFilter, getUserEntity(request).getUserName());
                } else {
                    tasks =  WorkflowAdapter.getMyCompletedTasks(taskFilter , getUserEntity(request).getUserName());
                }
                if(tasks != null && tasks.size() > 0){
                    taskInstance = tasks.get(0);
                }
            } else {
                String activityInstName = taskInstance.getActivityInstName();
                String jobTitle = taskInstance.getJobTitle();
                if(activityInstName != null && !"".equals(activityInstName)){
                    taskInstance.setActivityInstName(java.net.URLDecoder.decode(activityInstName , "UTF-8"));
                }
                if(jobTitle != null && !"".equals(jobTitle)){
                    taskInstance.setJobTitle(java.net.URLDecoder.decode(jobTitle , "UTF-8"));
                }
            }

            FlowNodeSettingEntity flowNodeSettingEntity = null;

//            if("waiting".equals(type)){
                flowNodeSettingEntity = new FlowNodeSettingEntity();
                flowNodeSettingEntity.setProcessModelName(taskInstance.getProcessModelName());
                flowNodeSettingEntity.setActivityDefID(taskInstance.getActivityDefID());
                flowNodeSettingEntity.setProcessModelId(taskInstance.getProcessModelId());

                logger.info("ProcessModelName=" + flowNodeSettingEntity.getProcessModelName() + ",ActivityDefID=" + flowNodeSettingEntity.getActivityDefID() + ",ProcessModelId=" + flowNodeSettingEntity.getProcessModelId());

                flowNodeSettingEntity = (FlowNodeSettingEntity) flowNodeSettingService.getSetting(flowNodeSettingEntity);
//            }



            String tenantId = "default";

            __buildJSON.put("areaName" , "工单详情");

            JSONArray componentModels = new JSONArray();

            __buildJSON.put("componentModels" , componentModels);

            if(taskInstance.getProcessInstID() != null){
                List<GeneralInfoModel> list = generalProcessService.findGeneralInfoByProcessInstIDUp(taskInstance.getProcessInstID());

                if(list == null || list.size() == 0){
                    ProcessInstance processInstance = WorkflowAdapter.getProcessInstance("root", taskInstance.getProcessInstID());
                    list = generalProcessService.findGeneralInfoByProcessInstIDUp(processInstance.getParentProcessInstID());
                }

                if(list != null && list.size() > 0){

                    Collections.sort(list, new Comparator<GeneralInfoModel>() {

                        /*
                         * int compare(Student o1, Student o2) 返回一个基本类型的整型，
                         * 返回负数表示：o1 小于o2，
                         * 返回0 表示：o1和o2相等，
                         * 返回正数表示：o1大于o2。
                         */
                        public int compare(GeneralInfoModel o1, GeneralInfoModel o2) {

                            //按照学生的年龄进行升序排列
                            if (o1.getObjectId() > o2.getObjectId()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });


                    for(int i = 0 ; i < list.size() ; i++){
                        GeneralInfoModel processEntity = list.get(i);

                        tenantId = processEntity.getTenantId();

//                        String formDetailURL = "/cform_trial/jsp/cform/tasklist/render/formrender.jsp?formId="+processEntity.getFormId()+"&tenantId="+processEntity.getTenantId()+"&formDataId="+processEntity.getFormDataId()+"&taskType=3";
                        String formDetailURL = "";

                        JSONObject componentModel = new JSONObject();
                        if("1".equals(processEntity.getFormType())){
                            formDetailURL = "/cform/jsp/cform/tasklist/render/formrender.jsp?formId="+processEntity.getFormId()+"&tenantId="+processEntity.getTenantId()+"&formDataId="+processEntity.getFormDataId()+"&taskType=3";
                        } else if("4".equals(processEntity.getFormType())){
                            ComponentFormEntity formEntity = new ComponentFormEntity();
                            formEntity.setId(Long.valueOf(processEntity.getFormId()));
                            try {
                                formEntity = (ComponentFormEntity) componentFormService.findById(formEntity);
                                formDetailURL = formEntity.getPcShowURL();
                            } catch (ServiceException e) {
                                logger.info("componentFormService.findById ERROR\n" + e.getLocalizedMessage());
                            }
                            componentModel.put("formDataId" , processEntity.getFormDataId());
                        }
                        componentModel.put("componentModelURL" , formDetailURL);
                        componentModel.put("model" , "show");
                        componentModel.put("operUser" , processEntity.getOperUserTrueName());
                        componentModel.put("operTime" , processEntity.getOperTime());
                        componentModel.put("areaName" , processEntity.getAreaName() == null ? "" : processEntity.getAreaName());
                        if(processEntity.getOperDesc() != null && !"".equals(processEntity.getOperDesc())){
                            componentModel.put("operDesc" , URLEncoder.encode(processEntity.getOperDesc() , "UTF-8"));
                        }
                        componentModel.put("activityInstName" , processEntity.getActivityInstName());

                        componentModels.add(componentModel);
                    }

                }
            }

            if(flowNodeSettingEntity != null){
                JSONObject componentModel = new JSONObject();
                componentModels.add(componentModel);

                componentModel.put("tenantId" , tenantId);
                componentModel.put("btnIDs" , flowNodeSettingEntity.getBtnIDs() == null ? "" : flowNodeSettingEntity.getBtnIDs());
                componentModel.put("btnNames" , flowNodeSettingEntity.getBtnNames() == null ? "" : flowNodeSettingEntity.getBtnNames());
                componentModel.put("areaName" , flowNodeSettingEntity.getAreaName() == null ? "" : flowNodeSettingEntity.getAreaName());

                if("waiting".equals(type)){
                    componentModel.put("model" , "edit");
                } else {
                    componentModel.put("model" , "show");
                }

                if(flowNodeSettingEntity.getFormType() == 1 && "waiting".equals(type)){
                    //云表单
                    String formId = flowNodeSettingEntity.getFormID();
                    componentModel.put("formId" , formId);
                    componentModel.put("activityInstName" , taskInstance.getActivityInstName());
                    componentModel.put("componentModelURL" , "/cform/jsp/cform/tasklist/list/add.jsp?disSaveBtn=0&formId="+formId+"&tenantId="+ tenantId +"&globalUniqueID=" + request.getSession().getAttribute("globalUniqueID"));
                } else if(flowNodeSettingEntity.getFormType() == 4 && "waiting".equals(type)){
                    componentModel.put("activityInstName" , taskInstance.getActivityInstName());
                    ComponentFormEntity formEntity = new ComponentFormEntity();
                    formEntity.setId(Long.valueOf(flowNodeSettingEntity.getComponentID()));
                    try {
                        formEntity = (ComponentFormEntity) componentFormService.findById(formEntity);
                        componentModel.put("formId" , formEntity.getId());
                        componentModel.put("componentModelURL" , formEntity.getPcEditURL());
                    } catch (ServiceException e) {
                        logger.info("componentFormService.findById ERROR\n" + e.getLocalizedMessage());
                    }
                } else if(flowNodeSettingEntity.getFormType() == 2){
                    //自定义URL
                    String customURL = flowNodeSettingEntity.getCustomURL();

                    if(customURL != null && !"".equals(customURL)){
                        StringBuffer customURLSB = new StringBuffer(customURL);
                        if(customURLSB.indexOf("?") > -1){
                            customURLSB.append("&");
                        } else {
                            customURLSB.append("?");
                        }
                        Map parameterMap = request.getParameterMap();
                        for(Object key : parameterMap.keySet()){
                            customURLSB.append(key);
                            customURLSB.append("=");
                            customURLSB.append(((String[])parameterMap.get(key))[0]);
                            customURLSB.append("&");
                        }

                        String customURLStr = customURLSB.toString();

                        if(!flowNodeSettingEntity.getExtand() || flowNodeSettingEntity.getExtand() == null){

                            logger.info("Before sendRedirect = " + customURLStr);
                            response.sendRedirect(customURLStr);
//                        response.sendRedirect("http://10.245.2.221/uflow/bpmapprove/backModifyView.do?tenantId=test3&appNo=89&activityDefID=apply&processModelName=publish_approve_process&processModelId=41&wfState=finish&workItemID=301&processInstId=281&");
                            return null;
//                        logger.info("After sendRedirect = " + customURLSB.toString());
                        }
                        componentModel.put("componentModelURL" , customURLStr);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        SerializeConfig ser = new SerializeConfig();
        ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
//        request.setAttribute("__taskInstance" , JSON.toJSONString(__buildJSON, ser, SerializerFeature.WriteNullListAsEmpty));

        logger.info("Dispatch base/page/frame.jsp");
        return new ModelAndView(new InternalResourceView("base/page/frame.jsp")).addObject("__buildJSON" , JSON.toJSONString(__buildJSON, ser, SerializerFeature.WriteNullListAsEmpty)).addObject("__taskInstance", JSON.toJSONString(taskInstance, ser, SerializerFeature.WriteNullListAsEmpty));
    }
}
