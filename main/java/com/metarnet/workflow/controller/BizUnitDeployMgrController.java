package com.metarnet.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.eos.workflow.api.BPSServiceClientFactory;
import com.eos.workflow.data.WFDeployeeOption;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.driver.service.IFlowNodeSettingService;
import com.metarnet.workflow.service.IBizFlowMgrService;
import com.metarnet.workflow.service.IBizUnitDeployMgrService;
import com.metarnet.workflow.utils.WorkFlowUtils;
import com.primeton.bps.component.manager.api.BPSMgrServiceClientFactory;
import com.primeton.bps.component.manager.api.IBPSMgrServiceClient;
import com.primeton.bps.data.WFBizProcessDef;
import com.primeton.workflow.api.WFServiceException;
import com.primeton.workflow.bizresource.manager.api.IWFBizProcessManager;
import com.primeton.workflow.bizresource.manager.api.IWFBizUnitDeployManager;
import com.primeton.workflow.bizresource.manager.model.WFBizResponse;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/18/0018.
 */
@Controller
public class BizUnitDeployMgrController extends BaseController {

    @Resource
    IBizUnitDeployMgrService bizUnitDeployMgrService;

    @Resource
    IBizFlowMgrService bizFlowMgrService;

    @RequestMapping(value = "/bizProcessSubmit.do")
    @ResponseBody
    public void bizProcessSubmit(HttpServletRequest request, HttpServletResponse resp , String submitType, String deploy, String versionDesc, String selectVersion, WFBizProcessDef processDef) throws UIException , WFServiceException {

        JSONObject jsonObject = new JSONObject();

        UserEntity userEntity = getUserEntity(request);

        try {
            jsonObject.put("success" , bizUnitDeployMgrService.deploy(submitType , deploy , versionDesc , selectVersion , processDef , userEntity));
        } catch (ServiceException e) {
            jsonObject.put("success", false);
        }
        endHandle(request, resp, jsonObject.toJSONString(), "");
    }

    @RequestMapping(value = "/bizProcessSubmitByTime.do")
    @ResponseBody
    public void bizProcessSubmitByTime(HttpServletRequest request, HttpServletResponse resp , String submitType, String deploy, String versionDesc, String selectVersion, WFBizProcessDef processDef) throws UIException , WFServiceException {

        JSONObject jsonObject = new JSONObject();

        UserEntity userEntity = getUserEntity(request);

        String activeTime = processDef.getExtend3();
        if(activeTime != null && !"".equals(activeTime)){

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Timer timer = new Timer();
            DeployerTask task = new DeployerTask(submitType , deploy, versionDesc, selectVersion, processDef, userEntity , bizUnitDeployMgrService);
            try {
                timer.schedule(task , sdf.parse(activeTime));

                WorkFlowUtils.deployTimerMap.put(processDef.getProcessDefID() , timer);

                //设置extend4为1，表示等待启用
                WFBizProcessDef wfBizProcessDef = new WFBizProcessDef();
                wfBizProcessDef.setProcessDefID(processDef.getProcessDefID());
                wfBizProcessDef.setExtend4("1");
                try {
                    bizFlowMgrService.updateProcessDefTmp(wfBizProcessDef);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }

            } catch (ParseException e) {
                jsonObject.put("success", false);
            }

            jsonObject.put("success" , true);
        } else {
            jsonObject.put("success" , false);
        }

        endHandle(request, resp, jsonObject.toJSONString(), "");
    }


}

class DeployerTask extends TimerTask {

    private IBizUnitDeployMgrService bizUnitDeployMgrService;
    private String submitType;
    private String deploy;
    private String versionDesc;
    private String selectVersion;
    private WFBizProcessDef processDef;
    private UserEntity userEntity;

    public DeployerTask(String submitType, String deploy, String versionDesc, String selectVersion, WFBizProcessDef processDef, UserEntity userEntity , IBizUnitDeployMgrService bizUnitDeployMgrService){
        this.bizUnitDeployMgrService = bizUnitDeployMgrService;
        this.submitType = submitType;
        this.deploy = deploy;
        this.versionDesc = versionDesc;
        this.selectVersion = selectVersion;
        this.processDef = processDef;
        this.userEntity = userEntity;
    }

    @Override
    public void run() {
        try {
            bizUnitDeployMgrService.deploy(submitType , deploy, versionDesc, selectVersion, processDef, userEntity);
            WorkFlowUtils.deployTimerMap.remove(processDef.getProcessDefID());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
