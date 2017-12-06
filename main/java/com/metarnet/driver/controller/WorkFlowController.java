package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.metarnet.core.common.adapter.AAAAAdapter;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.adapter.WorkflowAdapter4Activiti;
import com.metarnet.core.common.adapter.WorkflowAdapter_bps;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.AdapterException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.GeneralInfoModel;
import com.metarnet.core.common.model.Pager;
import com.metarnet.core.common.service.IWorkflowBaseService;
import com.metarnet.core.common.utils.HttpClientUtil;
import com.metarnet.core.common.utils.HttpRequestUtil;
import com.metarnet.core.common.utils.PagerPropertyUtils;
import com.metarnet.core.common.workflow.*;
import com.metarnet.core.common.workflow.ActivityInstance;
import com.metarnet.core.common.workflow.PageCondition;
import com.metarnet.core.common.workflow.Participant;
import com.metarnet.core.common.workflow.ProcessInstance;
import com.metarnet.core.common.workflow.ProcessModelParams;
import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.driver.model.ComponentFormEntity;
import com.metarnet.driver.model.FlowNodeSettingEntity;
import com.metarnet.driver.model.FlowNodeSettingTmpEntity;
import com.metarnet.driver.service.IComponentFormService;
import com.metarnet.driver.service.IFlowNodeSettingService;
import com.metarnet.workflow.utils.SmsDuanxinServer;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import com.ucloud.paas.proxy.aaaa.util.PaasAAAAException;
import com.unicom.ucloud.workflow.objects.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by ght on 2017-10-27 08:58:21
 */
@Controller
public class WorkFlowController extends BaseController {

    @Resource
    private IFlowNodeSettingService flowNodeSettingService;
    @Resource
    private IWorkflowBaseService workflowBaseService;
    @Resource
    private IComponentFormService componentFormService;


    /**
     * 0.
     * 测试各种方法用的
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=test")
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response,
                     String user, String taskInstId
    ) throws AdapterException, UIException {
        WorkflowAdapter4Activiti.deleteAssignee(taskInstId, user);
    }


    /**
     * * 1.启动流程
     * 获取表单数据并提交第一步任务
     *
     * @param accountId          用户ID    必须
     * @param participants       候选人列表   必须    List<String>
     * @param processModelID     流程模板名称  必须
     * @param processModelParams 流程模板参数  非必须 Map<String, Object>
     * @param bizModleParam      业务参数    非必须 Map<String, Object>
     * @param tenantId           租户ID    非必须
     * @param nextStep           流程下一步  非必须
     *                           //     * @param formId             表单ID    非必须
     *                           //     * @param formDataId         表单数据ID  非必须
     *                           //     * @param formType           表单类型    非必须
     * @return processInstID    流程实例ID
     */

    @RequestMapping(value = "/workFlowController.do", params = "method=startProcess")
    @ResponseBody
    public void startProcess(HttpServletRequest request, HttpServletResponse response,
                             String accountId, String participants,
                             String processModelID, String processModelParams,
                             String bizModleParam, String tenantId,
                             String nextStep) throws AdapterException, UIException {

        String processInstID = "";

        try {
            //候选人,启动流程后的第一步由流程启动者完成
            //设置方法activiti:candidateUsers="${   candidateUsers}"，多人逗号隔开
            Participant participant = new Participant();
            participant.setParticipantID(accountId);

            // 设置业务参数

            Map<String, Object> bizModleParams = new HashMap<>();
            if (bizModleParam != null & !"".equals(bizModleParam)) {
                Map<String, Object> params = JSONObject.parseObject(bizModleParam);
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    bizModleParams.put(entry.getKey(), entry.getValue());
                }
            }
            //jobID等由系统自动生成
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
//            bizModleParams.put("jobID", new Date().getTime() + "");
//            bizModleParams.put("jobCode", sdf.format(new Date()));
//            bizModleParams.put("jobTitle", sdf.format(new Date()) + "-" + accountId);

            //设置流程模板参数
            //可以为空
            ProcessModelParams processModelParam = new ProcessModelParams();
            if (processModelParams != null & !"".equals(processModelParams)) {
                Map<String, Object> params = JSONObject.parseObject(processModelParams);
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    processModelParam.setParameter(entry.getKey(), entry.getValue());
                }
            }

            //启动流程,返回流程实例id
            processInstID = WorkflowAdapter.startProcess(accountId, processModelID, participant, bizModleParams, processModelParam);

            //通过流程实例id获取第一步任务的任务实例id
            List<TaskInstance> taskInstances = WorkflowAdapter.queryNextWorkItemsByProcessInstID(processInstID, accountId);
            TaskInstance taskInstance = taskInstances.get(0);

            //记录通用处理信息
            //通过用户Id查询userEntity
            UserEntity userEntity = new UserEntity();
            try {
                userEntity = AAAAAdapter.findUserByPortalAccountId(accountId);
            } catch (PaasAAAAException e) {
                e.printStackTrace();
            }
            GeneralInfoModel generalInfoModel = new GeneralInfoModel();
            workflowBaseService.saveGeneralInfo(generalInfoModel, taskInstance, userEntity);

            //通过任务实例id提交任务,存储表数据
            //下一步的候选人列表
            List<Participant> participantList = new ArrayList<>();
            List<String> participantStrings = JSONArray.parseArray(participants, String.class);
            for (String participantString : participantStrings) {
                Participant participant1 = new Participant();
                participant1.setParticipantID(participantString);
                participantList.add(participant1);
            }

            String s = JSON.toJSONString(participantList);


            //提交任务
            WorkflowAdapter.submitTask(accountId, taskInstance, participantList, nextStep, tenantId);

            //记录通用处理信息
            //保存刚刚提交的任务信息
            TaskInstance taskInstanceComplete = WorkflowAdapter4Activiti.getHistoryTaskInstanceObject(accountId, taskInstance.getTaskInstID());
            GeneralInfoModel generalInfoModelOld = new GeneralInfoModel();
            workflowBaseService.saveGeneralInfo(generalInfoModelOld, taskInstanceComplete, userEntity);

            //保存刚刚新获得的任务信息
            TaskFilter taskFilter = new TaskFilter();
            taskFilter.setProcessInstID(taskInstanceComplete.getProcessInstID());
            List<TaskInstance> myWaitingTasks = WorkflowAdapter.getMyWaitingTasks(taskFilter, accountId);
            if (myWaitingTasks.size() != 0) {
                GeneralInfoModel generalInfoModelNew = new GeneralInfoModel();
                workflowBaseService.saveGeneralInfo(generalInfoModelNew, myWaitingTasks.get(0), userEntity);
            }


        } catch (Exception e) {
            processInstID = "false";
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("processInstID", processInstID);
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 2.提交待办
     *
     * @param taskInstanceID 任务实例ID  必须
     * @param participants   候选人列表   必须  ["aa","bb"]
     * @param accountId      用户ID    必须
     * @param tenantId       租户ID    必须
     * @param nextStep       下一步流程    必须
     *                       //     * @param formId         表单ID    必须
     *                       //     * @param formDataId     表单数据ID  必须
     *                       //     * @param formType       表单类型    必须
     * @return result  反馈结果    true/false
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=submitTask")
    @ResponseBody
    public void submitTask(HttpServletRequest request, HttpServletResponse response,
                           String taskInstanceID, String participants,
                           String accountId, String tenantId,
                           String nextStep) throws AdapterException, UIException {

        String result = "true";

        try {
            //将候选人列表的参数放入候选人对象集合中
            List<Participant> participantList = new ArrayList<>();
            List<String> participantStrings = JSONArray.parseArray(participants, String.class);
            for (String participantString : participantStrings) {
                Participant participant = new Participant();
                participant.setParticipantID(participantString);
                participantList.add(participant);
            }

            //通过任务实例ID查询任务实例
//            TaskInstance taskInstance = WorkflowAdapter.getTaskInstanceObject(accountId, taskInstanceID);
            //通过用户Id查询userEntity
            UserEntity userEntity = new UserEntity();
            try {
                userEntity = AAAAAdapter.findUserByPortalAccountId(accountId);
            } catch (PaasAAAAException e) {
                e.printStackTrace();
            }
            TaskInstance taskInstance = new TaskInstance();
            taskInstance.setTaskInstID(taskInstanceID);

            //提交待办
//            WorkflowAdapter.submitTask(accountId, taskInstance, participantList);
            WorkflowAdapter.submitTask(accountId, taskInstance, participantList, nextStep, tenantId);

            //记录通用处理信息
            //保存刚刚提交的任务信息
            TaskInstance taskInstanceComplete = WorkflowAdapter4Activiti.getHistoryTaskInstanceObject(accountId, taskInstanceID);
            GeneralInfoModel generalInfoModel = new GeneralInfoModel();
            workflowBaseService.saveGeneralInfo(generalInfoModel, taskInstanceComplete, userEntity);

            //保存刚刚新获得的任务信息
            TaskFilter taskFilter = new TaskFilter();
            taskFilter.setProcessInstID(taskInstanceComplete.getProcessInstID());
            List<TaskInstance> myWaitingTasks = WorkflowAdapter.getMyWaitingTasks(taskFilter, accountId);
            if (myWaitingTasks.size() != 0) {
                GeneralInfoModel generalInfoModelNew = new GeneralInfoModel();
                workflowBaseService.saveGeneralInfo(generalInfoModelNew, myWaitingTasks.get(0), userEntity);
            }

        } catch (Exception e) {
            result = "false";
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("result", result);
        endHandle4activiti(request, response, json, "");
    }


    /**
     * 3.查询待办-旧版
     *
     * @param accountId     用户ID 必须    若传递值为空则查询所有待办任务
     * @param startRecord   分页参数,开始记录   非必须
     * @param pageSize      分页参数,每页条数   非必须
     * @param processInstID 流程实例ID 非必须
     * @param tenantId      租户ID 非必须
     * @return Pager pager  exhibitDatas:List<TaskInstance>
     * isSuccess:true
     * pageSize:10
     * startRecord:0
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getMyWaitingTasks")
    @ResponseBody
    public void getMyWaitingTasks(HttpServletRequest request, HttpServletResponse response,
                                  String accountId, String startRecord, String pageSize,
                                  String processInstID, String tenantId) throws AdapterException, UIException {

        String json = "";
        String dtGridPager = request.getParameter("dtGridPager");

        if (dtGridPager != null) {
            JSONObject gridPager = JSONObject.parseObject(dtGridPager);
            pageSize = gridPager.getString("pageSize");
            startRecord = gridPager.getString("startRecord");
        }

        UserEntity userEntity = getUserEntity(request);
        if (userEntity != null)
            accountId = userEntity.getUserName();


        try {
            TaskFilter taskFilter = new TaskFilter();
            //设置流程实例ID
            taskFilter.setProcessInstID(processInstID);
            //设置分页参数
            PageCondition pageCondition = new PageCondition();
            if (startRecord != null & !"".equals(startRecord)) {
                pageCondition.setBegin(Integer.parseInt(startRecord));
            } else {
                pageCondition.setBegin(0);
            }
            if (startRecord != null & !"".equals(pageSize)) {
                pageCondition.setLength(Integer.parseInt(pageSize));
            } else {
                pageCondition.setLength(10);
            }
            taskFilter.setPageCondition(pageCondition);

            //查询待办任务集合
            List<TaskInstance> list = WorkflowAdapter.getMyWaitingTasks(taskFilter, accountId);
            //
            List<TaskInstance> list2 = new ArrayList<>();
            if (taskFilter.getDatColumn1StartTime() != null && taskFilter.getDatColumn1EndTime() != null) {
                for (TaskInstance task : list) {
                    if ((task.getDatColumn1() != null)
                            && (task.getDatColumn1().getTime() <= taskFilter.getDatColumn1EndTime().getTime())
                            && (task.getDatColumn1().getTime() >= taskFilter.getDatColumn1StartTime().getTime())) {
                        list2.add(task);
                    }
                }
            } else {
                list2 = list;
            }

            Pager pager = new Pager();
            pager.setExhibitDatas(list2);
            //设置超时参数
//            for (TaskInstance taskInstance : list2) {
//                Calendar timeout = Calendar.getInstance();
//                Calendar comingTimeout = Calendar.getInstance();
//                comingTimeout.add(Calendar.HOUR_OF_DAY, 2);
//                Date require = taskInstance.getDatColumn1();
//                if (require != null) {
//                    if (require.before(timeout.getTime())) {
//                        //超时
//                        taskInstance.setNumColumn1(2);
//                    } else if (require.after(timeout.getTime()) && require.before(comingTimeout.getTime())) {
//                        //即将超时
//                        taskInstance.setNumColumn1(1);
//                    }
//                }
//            }
            //设置是否成功
            pager.setIsSuccess(true);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
//        endHandle(request, response, json, "");
    }

    /**
     * 4.新待办查询
     * 用来查询待办任务列表
     * 主要是可以进行搜索查询和总数显示
     *
     * @param request
     * @param response
     * @param accountId
     * @param startRecord
     * @param pageSize
     * @param processInstID
     * @throws AdapterException
     * @throws UIException
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getWaitingTaskList")
    @ResponseBody
    public void getWaitingTaskList(HttpServletRequest request, HttpServletResponse response,
                                   String accountId, String startRecord, String pageSize,
                                   String processInstID, String processModelName,
                                   String createdBefore, String createdAfter, String jobCode, String tenantId) throws AdapterException, UIException {
        String json = "";
        String dtGridPager = request.getParameter("dtGridPager");
        if (dtGridPager != null) {
            JSONObject gridPager = JSONObject.parseObject(dtGridPager);
            pageSize = gridPager.getString("pageSize");
            startRecord = gridPager.getString("startRecord");
            JSONObject highQueryParameters = gridPager.getJSONObject("highQueryParameters");
            if (highQueryParameters != null) {
                //流程模板名
                if (!"".equals(highQueryParameters.getString("lk_processModelName")))
                    processModelName = highQueryParameters.getString("lk_processModelName");
                //在时间之前
//                createdBefore = highQueryParameters.getString("ge_jobTitle");
//                //在时间之后
//                createdAfter = highQueryParameters.getString("le_jobTitle");
//                        //工单编号
//                        highQueryParameters.getString("lk_");
//                        //当前节点
//                        highQueryParameters.getString("lk_activityInstName");
//                        //发起时间
//                        highQueryParameters.getString("lk_createDate");
//                        //发起人
//                        highQueryParameters.getString("lk_strColumn5");
            }
            System.out.println("control新待办查询:" + dtGridPager);
        }
        UserEntity userEntity = getUserEntity(request);
        if (userEntity != null)
            accountId = userEntity.getUserName();

        try {
            TaskFilter taskFilter = new TaskFilter();
            //设置流程实例ID
            taskFilter.setProcessInstID(processInstID);
            //设置流程模板名称
            taskFilter.setProcessModelName(processModelName);
            //设置工单编号,目前无用
//            taskFilter.setJobCode(jobCode);
            //设置分页参数
            PageCondition pageCondition = new PageCondition();
            if (startRecord != null & !"".equals(startRecord)) {
                pageCondition.setBegin(Integer.parseInt(startRecord));
            } else {
                pageCondition.setBegin(0);
            }
            if (startRecord != null & !"".equals(pageSize)) {
                pageCondition.setLength(Integer.parseInt(pageSize));
            } else {
                pageCondition.setLength(10);
            }
            taskFilter.setPageCondition(pageCondition);

            //查询待办任务集合
            Pager pager = WorkflowAdapter4Activiti.getWaitingTaskList(taskFilter, accountId, createdBefore, createdAfter);

            //设置是否成功
            pager.setIsSuccess(true);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 5.待办详情页面
     * 这个是重头戏,用来展示待办详情页面的
     * 分为工单详情,待办工单和历史工单三个模块
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getWaitingDesc")
    @ResponseBody
    public ModelAndView getWaitingDesc(HttpServletRequest request, HttpServletResponse response,
                                       String processInstID, String taskInstanceId, String accountId
    ) throws AdapterException, UIException, IOException, ServletException, Exception {
        UserEntity userEntity = getUserEntity(request);
        if (userEntity != null)
            accountId = userEntity.getUserName();

        List<String> srcList = new ArrayList<>();
        List<String> hisActivityList = new ArrayList<>();

        //获取所有的用户表单
//        Pager pager4Form=new Pager();
//            pager4Form.setPageSize(1000);
//            pager4Form.setStartRecord(0);
//        Pager pager = componentFormService.queryByPager(new ComponentFormEntity(), pager4Form);
//        List<ComponentFormEntity> componentFormEntityList = pager.getExhibitDatas();

        //通过流程实例ID获取历史流程实例
        TaskFilter taskFilter = new TaskFilter();
        //设置分页参数
        PageCondition pageCondition = new PageCondition();
        pageCondition.setBegin(0);
        pageCondition.setLength(100);
        taskFilter.setPageCondition(pageCondition);
        taskFilter.setProcessInstID(processInstID);
        //获取任务实例集合(倒序).0为待办任务,最后为工单详情
        List<TaskInstance> list = WorkflowAdapter.getMyCompletedTasks(taskFilter, "");
        if (list != null && list.size() > 1) {
            for (int i = list.size() - 1; i >= 0; i--) {

                //i=list.size()-1时,则为工单详情
                if (i == list.size() - 1) {
                    String first = "";
                    //获取任务实例
                    TaskInstance t = list.get(i);
                    //设置查询环节信息所需要的属性
                    FlowNodeSettingTmpEntity setting = new FlowNodeSettingTmpEntity();
                    setting.setActivityDefID(t.getActivityDefID());
                    setting.setProcessModelName(t.getProcessModelName());
                    //查询环节信息
                    FlowNodeSettingTmpEntity queryNodeSettings = (FlowNodeSettingTmpEntity) flowNodeSettingService.getSetting(setting);
                    if (queryNodeSettings != null) {
                        //查询表单类型,如果为4则是自行开发表单
                        Integer formType = queryNodeSettings.getFormType();
                        if (formType == 4) {
                            //通过环节信息查询个人表单信息
                            String componentID = queryNodeSettings.getComponentID();
                            ComponentFormEntity componentFormEntitySql = new ComponentFormEntity();
                            componentFormEntitySql.setId(Long.parseLong(componentID));
                            ComponentFormEntity formEntity = (ComponentFormEntity) componentFormService.findById(componentFormEntitySql);
                            if (formEntity != null) {
                                //通过表单信息获取表单地址
                                //show为emos的工单详情
                                String pcShowURL = formEntity.getPcShowURL();
                                if (pcShowURL == null)
                                    pcShowURL = "";
                                //获取工单详情页面的地址
                                first = pcShowURL.replace("{userName}", accountId).replace("{processInstID}", processInstID);
                            }
                        }
                    }
                    if (first == null || "".equals(first))
                        first = "base/page/pageIsNull.jsp";
                    srcList.add(first);
                    continue;
                }

                //i=0时,为待办工单
                if (i == 0) {
                    String now = "";
                    //获取任务实例
                    TaskInstance t = list.get(i);
                    //设置查询环节信息所需要的属性
                    FlowNodeSettingTmpEntity setting = new FlowNodeSettingTmpEntity();
                    setting.setActivityDefID(t.getActivityDefID());
                    setting.setProcessModelName(t.getProcessModelName());
                    //查询环节信息
                    FlowNodeSettingTmpEntity queryNodeSettings = (FlowNodeSettingTmpEntity) flowNodeSettingService.getSetting(setting);
                    if (queryNodeSettings != null) {
                        //查询表单类型,如果为4则是自行开发表单
                        Integer formType = queryNodeSettings.getFormType();
                        if (formType == 4) {
                            //通过环节信息查询个人表单信息
                            String componentID = queryNodeSettings.getComponentID();
                            ComponentFormEntity componentFormEntitySql = new ComponentFormEntity();
                            componentFormEntitySql.setId(Long.parseLong(componentID));
                            ComponentFormEntity formEntity = (ComponentFormEntity) componentFormService.findById(componentFormEntitySql);
                            if (formEntity != null) {
                                //通过表单信息获取表单地址
                                //edit为待办和历史的地址
                                String editURL = formEntity.getPcEditURL();
                                if (editURL == null)
                                    editURL = "";
                                //获取待办工单的地址
                                now = editURL.replace("{userName}", accountId).replace("{processInstID}", processInstID).replace("{taskInstanceID}", t.getTaskInstID()).replace("{isCurrent}", "1");
                            }
                        }
                    }
                    if (now == null || "".equals(now))
                        now = "base/page/pageIsNull.jsp";
                    srcList.add(now);
                    continue;
                }

                //中间均为历史环节
                String history = "";
                //获取任务实例
                TaskInstance t = list.get(i);
                //设置查询环节信息所需要的属性
                FlowNodeSettingTmpEntity setting = new FlowNodeSettingTmpEntity();
                setting.setActivityDefID(t.getActivityDefID());
                setting.setProcessModelName(t.getProcessModelName());
                //查询环节信息
                FlowNodeSettingTmpEntity queryNodeSettings = (FlowNodeSettingTmpEntity) flowNodeSettingService.getSetting(setting);
                if (queryNodeSettings != null) {
                    //查询表单类型,如果为4则是自行开发表单
                    Integer formType = queryNodeSettings.getFormType();
                    if (formType == 4) {
                        String componentID = queryNodeSettings.getComponentID();

                        //通过环节信息查询个人表单信息
                        ComponentFormEntity componentFormEntitySql = new ComponentFormEntity();
                        componentFormEntitySql.setId(Long.parseLong(componentID));
                        ComponentFormEntity formEntity = (ComponentFormEntity) componentFormService.findById(componentFormEntitySql);
                        if (formEntity != null) {
                            //通过表单信息获取表单地址
                            //edit为待办和历史的地址
                            String editURL = formEntity.getPcEditURL();
                            if (editURL == null)
                                editURL = "";
                            //获取待办工单的地址
                            history = editURL.replace("{userName}", accountId).replace("{processInstID}", processInstID).replace("{taskInstanceID}", t.getTaskInstID()).replace("{isCurrent}", "0");
                        }
                    }
                }
                if (history == null || "".equals(history))
                    history = "base/page/pageIsNull.jsp";
                srcList.add(history);

                //添加环节名称
                hisActivityList.add(t.getActivityInstName());
            }
        }
        String src = JSON.toJSONString(srcList);
        String hisActivity = JSON.toJSONString(hisActivityList);
        //测试表单,用来在前期无表单数据时进行页面测试,也是留作纪念的
//        List<String> testList=new ArrayList<>();
//        testList.add("http://10.225.222.200/cform/jsp/cform/tasklist/render/formrender.jsp?formId=XianChangFuWuGuiDang&tenantId=default");
//        testList.add("http://10.225.222.200/cform/jsp/cform/tasklist/render/formrender.jsp?formId=XianChangFuWuGuiDang&tenantId=default");
//        testList.add("http://10.225.222.200/cform/jsp/cform/tasklist/render/formrender.jsp?formId=XianChangFuWuGuiDang&tenantId=default");
//        testList.add("http://10.225.222.200/cform/jsp/cform/tasklist/render/formrender.jsp?formId=XianChangFuWuGuiDang&tenantId=default");
//        String test=JSON.toJSONString(testList);

        return new ModelAndView(new InternalResourceView("base/page/demoTaskSubmit.jsp")).addObject("srcList", src).addObject("hisActivity", hisActivity).addObject("processInstID", processInstID);
    }

    /**
     * 6.查询已办
     * 返回所有已办任务
     * 如果不传入用户,只传入流程实例id的话,则会包括该流程的未完成的任务!!!!
     *
     * @param accountId     用户ID 非必须
     * @param startRecord   分页参数,开始记录   非必须
     * @param pageSize      分页参数,每页条数   非必须
     * @param processInstID 流程实例ID  非必须
     * @param tenantId      租户ID 非必须
     * @return Pager pager  exhibitDatas:List<TaskInstance>
     * isSuccess:true
     * pageSize:10
     * startRecord:0
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getMyCompletedTasks")
    @ResponseBody
    public void getMyCompletedTasks(HttpServletRequest request, HttpServletResponse response,
                                    String accountId, String startRecord, String pageSize, String processInstID,
                                    String tenantId) throws AdapterException, UIException {

        String json = "";

        try {
            //设置任务参数
            TaskFilter taskFilter = new TaskFilter();
            //设置流程实例ID
            taskFilter.setProcessInstID(processInstID);
            //设置分页参数
            PageCondition pageCondition = new PageCondition();
            if (startRecord != null & !"".equals(startRecord)) {
                pageCondition.setBegin(Integer.parseInt(startRecord));
            } else {
                pageCondition.setBegin(0);
            }
            if (startRecord != null & !"".equals(pageSize)) {
                pageCondition.setLength(Integer.parseInt(pageSize));
            } else {
                pageCondition.setLength(10);
            }
            taskFilter.setPageCondition(pageCondition);

            //查询已办任务
            List<TaskInstance> list = WorkflowAdapter.getMyCompletedTasks(taskFilter, accountId);

            Pager pager = new Pager();
            pager.setExhibitDatas(list);
            //设置是否成功
            pager.setIsSuccess(true);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "todo");
    }

    /**
     * 7.查询发起过的工单
     * 通过用户id获取发起过的工单
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getWorkOrderStart")
    @ResponseBody
    public void getWorkOrderStart(HttpServletRequest request, HttpServletResponse response,
                                  String accountId
    ) throws AdapterException, UIException, IOException, ServletException, ServiceException {

        String json = "";
        List<TaskInstance> taskInstanceList = new ArrayList<>();

        try {
            //通过用户Id查询userEntity
            UserEntity userEntity = getUserEntity(request);
            if (userEntity != null) {
                accountId = userEntity.getUserName();
            }
            try {
                userEntity = AAAAAdapter.findUserByPortalAccountId(accountId);
            } catch (PaasAAAAException e) {
                e.printStackTrace();
            }
            //查询所有当前用户的通用处理信息
            List<GeneralInfoModel> generalInfoByCreatedBy = workflowBaseService.getGeneralInfoByCreatedBy(userEntity.getUserId());
            //根据通用信息获取所有流程实例id
            Set<String> processInstIdSet = new LinkedHashSet<>();
            for (GeneralInfoModel generalInfoModel : generalInfoByCreatedBy) {
                if (generalInfoModel.getProcessInstId() != null)
                    processInstIdSet.add(generalInfoModel.getProcessInstId());
            }
            //根据流程实例id获取所有待办
            for (String processInstId : processInstIdSet) {
                TaskFilter taskFilter = new TaskFilter();
                taskFilter.setProcessInstID(processInstId);
                List<TaskInstance> myWaitingTasks = WorkflowAdapter.getMyWaitingTasks(taskFilter, accountId);
                if (myWaitingTasks.size() != 0) {
                    TaskInstance taskInstance = myWaitingTasks.get(0);
                    taskInstanceList.add(taskInstance);
                }
            }

            Pager pager = new Pager();
            pager.setExhibitDatas(taskInstanceList);
            pager.setRecordCount(taskInstanceList.size());
            pager.setIsSuccess(true);

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 7.1获取发起过的工单页面
     */


    /**
     * 8.获取动态表单属性
     * 根据流程id,流程名字和环节定义id获取动态表单属性
     * 在flowNodeSettingController中已有的方法,拿到这个里面也可以用
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=queryNodeSetting")
    @ResponseBody
    public void queryNodeSetting(HttpServletRequest request, HttpServletResponse response,
                                 String processModelId, String processModelName, String activityDefID) throws AdapterException, UIException {
        String json = "";
        try {
            FlowNodeSettingTmpEntity setting = new FlowNodeSettingTmpEntity();
            setting.setActivityDefID(activityDefID);
            setting.setProcessModelId(processModelId);
            setting.setProcessModelName(processModelName);
            FlowNodeSettingTmpEntity queryNodeSettings = (FlowNodeSettingTmpEntity) flowNodeSettingService.getSetting(setting);
            json = JSON.toJSONString(queryNodeSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 9.待办列表页面
     * 此方法只是用来给其他系统传递待办列表页面,并无太多逻辑
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getWaitingList")
    @ResponseBody
    public ModelAndView getWaitingList(HttpServletRequest request, HttpServletResponse response,
                                       String accountId
    ) throws AdapterException, UIException, IOException, ServletException {
        return new ModelAndView(new InternalResourceView("base/page/todo.jsp")).addObject("accountId", accountId);
    }

    /**
     * 10.获取流程模板列表
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getModeLists")
    @ResponseBody
    public void getProcessModeLists(HttpServletRequest request, HttpServletResponse response) throws AdapterException, UIException {
        net.sf.json.JSONObject root = WorkflowAdapter4Activiti.getProcessModeLists("root");
        String json = JSONObject.toJSONString(root);
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 10.获取流程实例对象
     * 根据流程实例ID获取流程对象
     *
     * @param accountId     用户ID    非必须
     * @param processInstID 流程实例ID  必须
     * @param tenantId      租户ID    非必须
     * @return ProcessInstance 流程实例对象
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getProcessInstance")
    @ResponseBody
    public void getProcessInstance(HttpServletRequest request, HttpServletResponse response,
                                   String accountId, String processInstID, String tenantId) throws AdapterException, UIException {

        String json = "";
        try {
            ProcessInstance processInstance = WorkflowAdapter.getProcessInstance(accountId, processInstID);

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(processInstance, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 11.获取任务实例对象
     * 根据任务实例ID
     * 仅限于未完成的任务实例
     *
     * @param accountId
     * @param taskInstId
     * @param tenantId
     * @return TaskInstance    任务实例对象
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getTaskInstanceObject")
    @ResponseBody
    public void getTaskInstanceObject(HttpServletRequest request, HttpServletResponse response,
                                      String accountId, String taskInstId, String tenantId) throws AdapterException, UIException {
        String json = "";
        try {
            TaskInstance taskInstanceObject = WorkflowAdapter.getTaskInstanceObject(accountId, taskInstId);

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(taskInstanceObject, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 12.设置相关数据
     *
     * @param processInstID 流程实例ID  必须
     * @param relaData      相关数据    必须  {"aa":"bb","cc":"dd","list":["ee","ff"]}
     * @param accountId     用户ID    非必须
     * @param tenantId      租户ID    非必须
     * @return result  反馈结果    true/false
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=setRelativeData")
    @ResponseBody
    public void setRelativeData(HttpServletRequest request, HttpServletResponse response,
                                String processInstID, String relaData,
                                String accountId, String tenantId) throws AdapterException, UIException {
        String result = "true";
        try {
            Map<String, Object> relaDatas = JSONObject.parseObject(relaData);
            WorkflowAdapter.setRelativeData(processInstID, relaDatas, accountId);
        } catch (Exception e) {
            result = "false";
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("result", result);
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 13.获取相关数据
     *
     * @param processInstID 流程实例ID  必须
     * @param keys          查找的数据的key键   必须   ["aa","bb"]
     * @param accountId     用户ID    非必须
     * @param tenantId      租户ID    非必须
     * @return 数据集合    {"aa":"bb","cc":"dd","list":["ee","ff"]}
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getRelativeData")
    @ResponseBody
    public void getRelativeData(HttpServletRequest request, HttpServletResponse response,
                                String processInstID, String keys,
                                String accountId, String tenantId) throws AdapterException, UIException {
        String json = "";
        try {
            List<String> keyList = JSONArray.parseArray(keys, String.class);
            Map<String, Object> relativeData = WorkflowAdapter.getRelativeData(processInstID, keyList, accountId);
            json = JSON.toJSONString(relativeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 14.记录通用处理信息
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=saveGeneralInfo")
    @ResponseBody
    public void saveGeneralInfo(HttpServletRequest request, HttpServletResponse response,
                                String accountId, String taskInstanceID,
                                String tenantId, String formId, String formDataId, String formType) throws AdapterException, UIException {

        try {
            //通过任务实例ID查询任务实例
            TaskInstance taskInstance = WorkflowAdapter.getTaskInstanceObject(accountId, taskInstanceID);
            //通过用户Id查询userEntity
            UserEntity userEntity = new UserEntity();
            try {
                userEntity = AAAAAdapter.findUserByPortalAccountId(accountId);
            } catch (PaasAAAAException e) {
                e.printStackTrace();
            }
            //记录通用处理信息
            GeneralInfoModel generalInfoModel = new GeneralInfoModel();
//            generalInfoModel.setFormDataId(formDataId);
//            generalInfoModel.setFormId(formId);
//            generalInfoModel.setTenantId(tenantId);
//            generalInfoModel.setFormType(formType);
            workflowBaseService.saveGeneralInfo(generalInfoModel, taskInstance, userEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 15.获取通用处理信息
     * 根据任务实例ID
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getGeneralInfo")
    @ResponseBody
    public void getGeneralInfo(HttpServletRequest request, HttpServletResponse response,
                               String taskInstanceID) throws AdapterException, UIException {
        String json = "";

        try {
            List<GeneralInfoModel> generaInfoList = workflowBaseService.getGeneraInfoList(taskInstanceID);
            Pager pager = new Pager();
            if (generaInfoList != null) {
//               generaInfoList.get(0);
                pager.setExhibitDatas(generaInfoList);
                pager.setRecordCount(generaInfoList.size());
            }
            pager.setIsSuccess(true);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 16.获取通用处理信息集合
     * 根据流程实例ID,获取所有过往记录
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getGeneralInfoList")
    @ResponseBody
    public void getGeneralInfoList(HttpServletRequest request, HttpServletResponse response,
                                   String processInstID) throws AdapterException, UIException {
        String json = "";

        try {

            //通过流程实例ID获取历史流程实例
            TaskFilter taskFilter = new TaskFilter();
            //设置分页参数
            PageCondition pageCondition = new PageCondition();
            pageCondition.setBegin(0);
            pageCondition.setLength(100);
            taskFilter.setPageCondition(pageCondition);
            taskFilter.setProcessInstID(processInstID);
            List<TaskInstance> taskInstanceList = WorkflowAdapter.getMyCompletedTasks(taskFilter, "");

            Pager pager = new Pager();
            List<GeneralInfoModel> generaInfoList = new ArrayList<>();
            //通过历史流程实例id获取通用处理信息集合
            for (int i = taskInstanceList.size() - 1; i >= 0; i--) {
                List<GeneralInfoModel> generaInfoListTemp = workflowBaseService.getGeneraInfoList(taskInstanceList.get(i).getTaskInstID());
                if (generaInfoListTemp != null && generaInfoListTemp.size() != 0) {
                    generaInfoList.add(generaInfoListTemp.get(0));
                }
            }

            pager.setExhibitDatas(generaInfoList);
            pager.setRecordCount(generaInfoList.size());
            pager.setIsSuccess(true);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 17.获取通用处理信息页面
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getGeneralInfoPage")
    @ResponseBody
    public ModelAndView getGeneralInfoPage(HttpServletRequest request, HttpServletResponse response,
                                           String processInstID) throws AdapterException, UIException {
        return new ModelAndView(new InternalResourceView("base/page/workOrderMonitor.jsp")).addObject("processInstID", processInstID);
//        return new ModelAndView(new InternalResourceView("base/page/workOrderStart.jsp")).addObject("processInstID",processInstID);
    }


    /**
     * 18.更改任务领取人,若为空则设置为空
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=deleteAssignee")
    @ResponseBody
    public void deleteAssignee(HttpServletRequest request, HttpServletResponse response,
                               String user, String taskInstId
    ) throws AdapterException, UIException {
        WorkflowAdapter4Activiti.deleteAssignee(taskInstId, user);
    }

    /**
     * 19.催办
     * 返回所有被催办人
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=sendMessage")
    @ResponseBody
    public ModelAndView sendMessage(HttpServletRequest request, HttpServletResponse response,
                                    String taskInstanceId) throws AdapterException, UIException {
        String json = "";

        try {
            Pager pager = new Pager();

            List<String> telephoneList = new ArrayList<>();

            //根据任务实例id获取所有候选人
            List<String> candidates = WorkflowAdapter4Activiti.getCandidates(taskInstanceId);
            TaskInstance taskInstance = WorkflowAdapter4Activiti.getTaskInstanceObject("", taskInstanceId);
            String jobCode = taskInstance.getJobCode();
            //根据所有候选人来获取电话,并发送短信
            for (String candidate : candidates) {
                try {
                    UserEntity userEntity = AAAAAdapter.findUserByPortalAccountId(candidate);
                    if (userEntity != null) {
                        String telephone = userEntity.getTelephone();
                        if (telephone != null && !"".equals(telephone)) {
                            String message = "尊敬的" + userEntity.getTrueName() + ":您好!工单编号:" + jobCode + ",的待办任务正在催办";
                            SmsDuanxinServer.duanxinByphone("18611805440", message);
                            telephoneList.add(userEntity.getUserName());
                        }
                    }
                } catch (PaasAAAAException e) {
                    e.printStackTrace();
                }
            }

            //返回所有已发送短信的用户名
            pager.setExhibitDatas(telephoneList);
            pager.setIsSuccess(true);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        endHandle4activiti(request, response, json, "");
        return new ModelAndView(new InternalResourceView("base/page/workOrderStart.jsp"));
    }

    /**
     * 20.增加候选人-转办-协办
     * 若传递accountId的值,则会删除accountId的候选资格
     * 可以传递List集合的json形式的候选人集合 进行批量增加
     *
     * @param accountId      用户ID
     * @param taskInstanceId 任务实例ID
     * @param participantID  转办后执行人ID
     * @return 反馈结果    true/false
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=forwardTask")
    @ResponseBody
    public void forwardTask(HttpServletRequest request, HttpServletResponse response,
                            String accountId, String taskInstanceId, String participantID) throws AdapterException, UIException {
        String result = "true";
        try {
            //将转办后执行人ID放入候选人对象集合
            List<String> participantList = JSONArray.parseArray(participantID, String.class);
            List<Participant> participants = new ArrayList<>();
            for (String p : participantList) {
                Participant participant = new Participant();
                participant.setParticipantID(p);
                participants.add(participant);
            }
            WorkflowAdapter.forwardTask(accountId, taskInstanceId, participants);
        } catch (Exception e) {
            result = "false";
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("result", result);
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 21.根据活动实例ID获取任务实例ID
     *
     * @param accountId      用户ID    非必须
     * @param activityInstID 活动实例ID  必须
     * @param tenantId       租户ID    非必须
     * @return Pager pager  exhibitDatas:List<TaskInstance>
     * isSuccess:true
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getTaskInstancesByActivityID")
    @ResponseBody
    public void getTaskInstancesByActivityID(HttpServletRequest request, HttpServletResponse response,
                                             String accountId, String activityInstID, String tenantId) throws AdapterException, UIException {
        String json = "";
        try {
            List<TaskInstance> taskInstancesByActivityID = WorkflowAdapter.getTaskInstancesByActivityID(accountId, activityInstID);

            Pager pager = new Pager();
            pager.setExhibitDatas(taskInstancesByActivityID);

            pager.setIsSuccess(true);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "todo");
    }

    /**
     * 22.获取流程实例流转过的活动
     * 若流程未结束,则数据集合的最后一个元素是当前待办
     *
     * @param accountId     用户ID    非必须
     * @param processInstID 流程实例ID  必须
     * @param tenantId      租户ID    非必须
     * @return Pager pager  exhibitDatas:List<ActivityInstance>
     * isSuccess:true
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getActivityInstances")
    @ResponseBody
    public void getActivityInstances(HttpServletRequest request, HttpServletResponse response,
                                     String accountId, String processInstID, String tenantId) throws AdapterException, UIException {

        String json = "";

        try {
            List<ActivityInstance> activityInstances = WorkflowAdapter.getActivityInstances(accountId, processInstID);

            Pager pager = new Pager();
            pager.setExhibitDatas(activityInstances);
            pager.setIsSuccess(true);

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 23.获取流程实例的子流程
     *
     * @param accountId     用户ID    非必须
     * @param processInstID 流程实例ID  必须
     * @param tenantId      租户ID    非必须
     * @return Pager pager  exhibitDatas:List<ProcessInstance>
     * isSuccess:true
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getSubProcessInstance")
    @ResponseBody
    public void getSubProcessInstance(HttpServletRequest request, HttpServletResponse response,
                                      String accountId, String processInstID, String tenantId) throws AdapterException, UIException {
        String json = "";
        try {
            List<ProcessInstance> subProcessInstance = WorkflowAdapter.getSubProcessInstance(accountId, processInstID);

            Pager pager = new Pager();
            pager.setExhibitDatas(subProcessInstance);
            pager.setIsSuccess(true);

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 24.获取根流程实例id
     *
     * @param accountId         用户ID    非必须
     * @param processInstanceId 流程实例ID  必须
     * @param tenantId          租户ID    非必须
     * @return ProcessInstance 流程实例对象  {
     * "processInstID": "18097",
     * "processModelID": "demo-1:7:16562",
     * "processModelName": "demo-1"
     * }
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=getRootProcessInstance")
    @ResponseBody
    public void getRootProcessInstance(HttpServletRequest request, HttpServletResponse response,
                                       String accountId, String processInstanceId, String tenantId) throws AdapterException, UIException {
        String json = "";
        try {
            ProcessInstance rootProcessInstance = WorkflowAdapter.getRootProcessInstance(accountId, processInstanceId);

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            json = JSON.toJSONString(rootProcessInstance, ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endHandle4activiti(request, response, json, "");
    }

    /**
     * 25.	根据业务主键jobID获取当前待办参数
     *
     * @param accountId 用户ID    非必须
     * @param jobId     业务主键ID  必须
     * @param tenantId  租户ID    非必须
     * @return [
     * {
     * "proinstid": "18108",   流程实例ID
     * "parproinstid": null,   父流程实例ID
     * "participants": "ght",  参与者集合(多人用逗号分隔)
     * "actinstname": "流程3",   活动节点名称
     * "creattime": 1510487552000, 创建时间
     * "actinstid": "18185"    任务实例ID
     * }
     * ]
     */
    @RequestMapping(value = "/workFlowController.do", params = "method=findDoingActivitysByJobID")
    @ResponseBody
    public void findDoingActivitysByJobID(HttpServletRequest request, HttpServletResponse response,
                                          String accountId, String jobId, String tenantId) throws AdapterException, UIException {
        String doingActivitysByJobID = "";
        try {
            doingActivitysByJobID = WorkflowAdapter.findDoingActivitysByJobID(accountId, jobId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        endHandle4activiti(request, response, doingActivitysByJobID, "");
    }


/**
 * 4.
 * 获取表单
 * todo 很简单的表单查询功能,基本无用,留个纪念
 */
//    @RequestMapping(value = "/workFlowController.do", params = "method=getTable")
//    @ResponseBody
//    public ModelAndView getTable(HttpServletRequest request, HttpServletResponse response,
//                                 String activityDefID, String processModelId
//    ) throws AdapterException, UIException, IOException, ServletException {
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<JSONObject> forEntity1 =
//                restTemplate.postForEntity("http://localhost:9087/UFP_DRIVER/flowNodeSettingController.do?method=queryNodeSetting&processModelId={?}&activityDefID={?}",
//                        null, JSONObject.class,
//                        processModelId, activityDefID);
//        JSONObject body = forEntity1.getBody();
//        Map<String, Object> relaDatas = JSONObject.parseObject(body.toJSONString());
//        String formID = relaDatas.get("formID").toString();
//        String tenantId = relaDatas.get("tenantId").toString();
//        return new ModelAndView(new InternalResourceView("/demoTaskSubmit.jsp")).addObject("formID", formID).addObject("tenantId", tenantId);
//    }

    /**
     * 3.通过流程实例ID获取待办任务对象
     * 通常用于刚起流程时查询待办
     *
     * @param accountId     用户ID    非必须
     * @param processInstID 流程实例ID  必须
     * @param tenantId      租户ID    非必须
     * @return Pager pager  exhibitDatas:List<TaskInstance> 获取第一个元素即所需要的任务实例对象
     * isSuccess:true
     * pageSize:10
     * startRecord:0
     */
    //todo 和上面的方法完全重复了,不用了
//    @RequestMapping(value = "/workFlowController.do", params = "method=queryNextWorkItemsByProcessInstID")
//    @ResponseBody
//    public void queryNextWorkItemsByProcessInstID(HttpServletRequest request, HttpServletResponse response,
//                                                  String accountId, String processInstID, String tenantId) throws AdapterException, UIException {
//
//        String json = "";
//
//        try {
//            //设置分页参数
//            Pager pager = new Pager();
//            pager.setStartRecord(0);
//            pager.setPageSize(10);
//
//            //根据流程实例ID查询下一步的任务实例集合
//            List<TaskInstance> taskInstances = WorkflowAdapter.queryNextWorkItemsByProcessInstID(processInstID, accountId);
//            pager.setExhibitDatas(taskInstances);
//
//            //设置是否成功
//            pager.setIsSuccess(true);
//            SerializeConfig ser = new SerializeConfig();
//            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
//
//            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        endHandle(request, response, json, "");
//    }

    /**
     * 4.查询已办
     * 基于同一用户的合并
     *  todo 我觉得并无大用,和上面的方法重复了,
     *  只不过是可以通过pager传递一个总数量,
     *  过段时间我可以把上面的方法也同样实现出来
     *
     * @param startRecord 分页参数,开始记录   非必须
     * @param pageSize    分页参数,每页条数   非必须
     * @param accountId   用户ID    必须  isNotNull
     * @param tenantId    租户ID    非必须
     * @return Pager pager  exhibitDatas:List<TaskInstance>
     * isSuccess:true
     * pageSize:10
     * startRecord:0
     * RecordCount:记录总数
     */
//    @RequestMapping(value = "/workFlowController.do", params = "method=getMyCompletedTasksDistinctJobId")
//    @ResponseBody
//    public void getMyCompletedTasksDistinctJobId(HttpServletRequest request, HttpServletResponse response,
//                                                 String startRecord, String pageSize,
//                                                 String accountId, String tenantId) throws AdapterException, UIException {
//
//        String json = "";
//
//        try {
//            //设置任务参数
//            TaskFilter taskFilter = new TaskFilter();
//            //设置分页参数
//            PageCondition pageCondition = new PageCondition();
//            if (startRecord != null & !"".equals(startRecord)) {
//                pageCondition.setBegin(Integer.parseInt(startRecord));
//            } else {
//                pageCondition.setBegin(0);
//            }
//            if (startRecord != null & !"".equals(pageSize)) {
//                pageCondition.setLength(Integer.parseInt(pageSize));
//            } else {
//                pageCondition.setLength(10);
//            }
//            taskFilter.setPageCondition(pageCondition);
//
//            Pager pager = WorkflowAdapter.getMyCompletedTasksDistinctJobId(taskFilter, accountId);
//
//            pager.setIsSuccess(true);
//            SerializeConfig ser = new SerializeConfig();
//            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
//            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
//
//            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        endHandle4activiti(request, response, json, "todo");
//    }

    /**
     *
     * 4.查询已办
     * --基于同于流程实例中相同处理人的已办合并
     *
     * @param request
     * @param response
     * @throws AdapterException
     * @Sthrows UIException
     */
    //todo 此方法还未实现!!
//    @RequestMapping(value = "/workFlowController.do", params = "method=getMyCompletedTasksDistinctProinstanceId")
//    @ResponseBody
//    public void getMyCompletedTasksDistinctProinstanceId(HttpServletRequest request, HttpServletResponse response,
//                                                         String accountId,TaskFilter taskFilter, String tenantId) throws AdapterException, UIException {
//
//        String json = "";
//
//        try {
//            Pager pager = PagerPropertyUtils.copy(request.getParameter("dtGridPager"));
//            if (pager == null)
//                pager = new Pager();
//            taskFilter = setPageCodition(taskFilter, request);
//
//            List<TaskInstance> myCompletedTasksDistinctProinstanceId =
//                    WorkflowAdapter.getMyCompletedTasksDistinctProinstanceId(taskFilter, accountId);
//            pager.setExhibitDatas(myCompletedTasksDistinctProinstanceId);
//
//            pager.setIsSuccess(true);
//            SerializeConfig ser = new SerializeConfig();
//            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
//            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
//
//            json = JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        endHandle(request, response, json, "todo);
//    }

    /**
     * 11.根据传入的processParameter 分解组装processParameter对象集合
     *
     * @param areacode    区域名称
     * @param orgcode     组织名称
     * @param majorcode   专业名称
     * @param productcode 产品名称
     * @return
     */
    //todo 属于其他类的工具,不需要写入
//    @RequestMapping(value = "/workFlowController.do", params = "method=resolveProcsParameter")
//    @ResponseBody
//    public void resolveProcsParameter(HttpServletRequest request, HttpServletResponse response,
//                                      String areacode, String orgcode, String majorcode,
//                                      String productcode, String tenantId) throws AdapterException, UIException {
//
//        String[] jsonArray = null;
//
//        try {
//            jsonArray = WorkflowAdapter.resolveProcsParameter(areacode, orgcode, majorcode, productcode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String json  = JSONArray.toJSONString(jsonArray);
//        endHandle(request, response, json, "");
//    }

    /**
     * 11.业务描述：撤回任务，在对方没有提交之前进行撤回
     *
     * @param currentActivityInstId 当前活动实例ID
     * @param targetActivityInstId  需要回退/取回的目标活动实例ID
     */
    //todo 这个方法底层是错误的,需要重写
//    @RequestMapping(value = "/workFlowController.do", params = "method=backActivity")
//    @ResponseBody
//    public void backActivity(HttpServletRequest request, HttpServletResponse response,
//                             String currentActivityInstId, String targetActivityInstId,
//                             String accountId,String tenantId) throws AdapterException, UIException {
//
//        String result = "true";
//
//        try {
//            WorkflowAdapter.backActivity(accountId, currentActivityInstId, targetActivityInstId);
//        } catch (Exception e) {
//            result = "false";
//            e.printStackTrace();
//        }
//        JSONObject json=new JSONObject();
//        json.put("result",result);
//        endHandle(request, response, json, "");
//    }

    /**
     * 11.回退到指定的步骤
     *
     * @param processId             当前流程id
     * @param currentActivityInstId 当前活动实例id
     * @param activityDefID         要回退环节的活动定义id
     * @throws AdapterException
     */
    //todo 这个方法底层是错误的,需要重写
//    @RequestMapping(value = "/workFlowController.do", params = "method=backTargetActivity")
//    @ResponseBody
//    public void backTargetActivity(HttpServletRequest request, HttpServletResponse response,
//                                   String processId, String currentActivityInstId,
//                                   String accountId,String activityDefID, String tenantId) throws AdapterException, UIException {
//        String result = "true";
//        try {
//            WorkflowAdapter.backTargetActivity(accountId, processId, currentActivityInstId, activityDefID);
//        } catch (Exception e) {
//            result = "false";
//            e.printStackTrace();
//        }
//        JSONObject json=new JSONObject();
//        json.put("result",result);
//        endHandle(request, response, json, "");
//    }

    /**
     * 13.根据业务主键获取活动
     *
     */
    //todo 这个方法有问题,底层方法应该是错的
//    @RequestMapping(value = "/workFlowController.do", params = "method=findHisDoingActivitysByJobID")
//    @ResponseBody
//    public void findHisDoingActivitysByJobID(HttpServletRequest request, HttpServletResponse response,
//                                             String accountId,String tenantId, String jobId) throws AdapterException, UIException {
//        String json = "";
//        try {
//            json = WorkflowAdapter.findHisDoingActivitysByJobID(accountId, jobId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        endHandle(request, response, json, "");
//    }

    /**
     * 15.获取下一个参与者
     *
     *
     * @param request
     * @param response
     * @param specialty
     * @param orgID
     * @param orgName
     * @param process
     * @param node
     * @param flag
     * @throws AdapterException
     * @throws UIException
     */
    //todo 这个方法无法验证,不知道组织id在哪获取
//    @RequestMapping(value = "/workFlowController.do", params = "method=findNextParticipant")
//    @ResponseBody
//    public void findNextParticipant(HttpServletRequest request, HttpServletResponse response,
//                                    String specialty, String orgID, String orgName,
//                                    String process, String node, String flag,
//                                    String accountId,String tenantId) throws AdapterException, UIException {
//        String json = "";
//        try {
//            if("".equals(flag)||flag==null)
//                flag="false";
//            if(orgName==null)
//                orgName="";
//            boolean flag1 = "true".equals(flag) ? true : false;
//            json = WorkflowAdapter.findNextParticipant(specialty, orgID, orgName, process, node, flag1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        endHandle(request, response, json, "");
//    }

    /**
     * 15.获取XXX参与者
     *
     * @param request
     * @param response
     * @param specialty
     * @param orgID
     * @param orgName
     * @param process
     * @param node
     * @throws AdapterException
     * @throws UIException
     */
    //todo 这个方法无法验证,不知道组织id在哪获取
//    @RequestMapping(value = "/workFlowController.do", params = "method=findNextParticipantToChildInstance")
//    @ResponseBody
//    public void findNextParticipantToChildInstance(HttpServletRequest request, HttpServletResponse response,
//                                                   String accountId,String specialty, String orgID, String orgName,
//                                                   String process, String node, String tenantId) throws AdapterException, UIException {
//
//        String json = "";
//
//        try {
//            if (orgName == null)
//                orgName = "";
//            List<String> nextParticipantToChildInstance =
//                    WorkflowAdapter.findNextParticipantToChildInstance(specialty, orgID, orgName, process, node);
//
//            json = JSON.toJSONString(nextParticipantToChildInstance);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        endHandle(request, response, json, "");
//    }

    /**
     * 获取分页信息
     * todo 这个方法是工具,目前没有用到,后面如果需要整合就用
     */
    private TaskFilter setPageCodition(TaskFilter taskFilter, HttpServletRequest request) {
        String dtGridPager = request.getParameter("dtGridPager");
        int start;// 开始行数
        int length;// 每页大小
        String jobCode = null;
        String jobTitle = null;
        String startCreateDate = null;
        String endCreateDate = null;
        try {
            JSONObject dtGridPagerJson = JSON.parseObject(dtGridPager);
            start = Integer.valueOf((String) dtGridPagerJson.get("startRecord"));
            length = Integer.valueOf((String) dtGridPagerJson.get("pageSize"));

            JSONObject fastQueryParameters = (JSONObject) dtGridPagerJson.get("fastQueryParameters");
            try {
                jobCode = fastQueryParameters.getString("lk_jobCode");
                jobTitle = fastQueryParameters.getString("lk_jobTitle");
                //到单时间查询
                startCreateDate = fastQueryParameters.getString("ge_completionDate");
                endCreateDate = fastQueryParameters.getString("le_completionDate");
            } catch (Exception e) {

            }
            if (jobCode != null && !"".equals(jobCode)) {
                taskFilter.setJobCode(jobCode);
            }
            if (jobTitle != null && !"".equals(jobTitle)) {
                taskFilter.setJobTitle(jobTitle);
            }

        } catch (Exception e) {
            start = 0;
            length = 10;
        }

        PageCondition pageCondition = taskFilter.getPageCondition();
        if (pageCondition == null) {
            pageCondition = new PageCondition();
            pageCondition.setBegin(start);
            pageCondition.setLength(length);
            taskFilter.setPageCondition(pageCondition);
        }

        return taskFilter;
    }


}
