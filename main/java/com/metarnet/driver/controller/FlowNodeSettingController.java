package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.AdapterException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.driver.model.FlowNodeSettingEntity;
import com.metarnet.driver.model.FlowNodeSettingTmpEntity;
import com.metarnet.driver.service.IFlowNodeSettingService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
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

/**
 * Created by Administrator on 2016/8/31/0031.
 */
@Controller
public class FlowNodeSettingController extends BaseController {

    Logger logger = LogManager.getLogger("FlowNodeSettingController");

    @Resource
    private IFlowNodeSettingService flowNodeSettingService;

    @RequestMapping(value = "/flowNodeSettingController.do", params = "method=init")
    @ResponseBody
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response , FlowNodeSettingTmpEntity flowNodeSettingEntity) throws UIException {
        FlowNodeSettingTmpEntity setting = new FlowNodeSettingTmpEntity();
        try {
            setting = (FlowNodeSettingTmpEntity) flowNodeSettingService.getSetting(flowNodeSettingEntity);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView(new InternalResourceView("/base/page/flowNodeSettings.jsp")).addObject("setting" , setting);
    }

    @RequestMapping(value = "/getNodeSetting.do")
    @ResponseBody
    public void getSetting(HttpServletRequest request, HttpServletResponse response , FlowNodeSettingEntity flowNodeSettingEntity) throws UIException {
        FlowNodeSettingEntity setting = new FlowNodeSettingEntity();
        try {
            setting = (FlowNodeSettingEntity) flowNodeSettingService.getSetting(flowNodeSettingEntity);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        endHandle(request, response, JSON.toJSONString(setting) , "" , true);
    }

    /**
     * 根据流程id,流程名字和环节定义id获取动态表单属性
     */
    @RequestMapping(value = "/flowNodeSettingController.do",params = "method=queryNodeSetting")
    @ResponseBody
    public  void  queryNodeSetting(HttpServletRequest request, HttpServletResponse response,
                                   String processModelId,String processModelName,String activityDefID) throws AdapterException, UIException {
        String json="";
        try {
            FlowNodeSettingEntity setting = new FlowNodeSettingEntity();
            setting.setActivityDefID(activityDefID);
            setting.setProcessModelId(processModelId);
            setting.setProcessModelName(processModelName);
            FlowNodeSettingEntity queryNodeSettings=(FlowNodeSettingEntity) flowNodeSettingService.getSetting(setting);
            json=JSON.toJSONString(queryNodeSettings);
        }catch (Exception e){
            e.printStackTrace();
        }

        endHandle4activiti(request,response,json,"");

    }

    @RequestMapping(value = "/flowNodeSettingController.do", params = "method=save")
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response , FlowNodeSettingTmpEntity flowNodeSettingEntity) throws UIException {
        JSONObject jsonObject = new JSONObject();
        try {
            UserEntity userEntity = getUserEntity(request);
            flowNodeSettingService.saveSetting(flowNodeSettingEntity , userEntity);
            jsonObject.put("settingID", flowNodeSettingEntity.getSettingID());
            jsonObject.put("activityDefID", flowNodeSettingEntity.getActivityDefID());
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success" , false);
            e.printStackTrace();
        }
        endHandle(request, response, jsonObject, "");
    }

    @RequestMapping(value = "/flowNodeSettingController.do", params = "method=checkSettings")
    @ResponseBody
    public void checkSettings(HttpServletRequest request, HttpServletResponse response , String processModelName , String nodeSettings) throws UIException {
        logger.info("processModelName=" + processModelName + ",nodeSettings=" + nodeSettings);
        JSONObject jsonObject = new JSONObject();
        try {
            UserEntity userEntity = getUserEntity(request);
            flowNodeSettingService.saveCheckSetting(processModelName, nodeSettings , userEntity);
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success" , false);
            e.printStackTrace();
        }
        endHandle(request, response, jsonObject, "");
    }

    @RequestMapping(value = "/flowNodeSettingController.do", params = "method=publish")
    @ResponseBody
    public void publish(HttpServletRequest request, HttpServletResponse response , String processModelName , String version , String processModelId) throws UIException {
        logger.info("processModelName=" + processModelName + ",version=" + version + ",processModelId=" + processModelId);
        JSONObject jsonObject = new JSONObject();
        try {
            UserEntity userEntity = getUserEntity(request);
            flowNodeSettingService.savePublishSetting(processModelName, version, userEntity , processModelId);
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success" , false);
            e.printStackTrace();
        }
        endHandle(request, response, jsonObject, "");
    }

    @RequestMapping(value = "/flowNodeSettingSyncController.do")
    @ResponseBody
    public void sync(HttpServletRequest request, HttpServletResponse response , String processModelName) throws UIException {
        logger.info("processModelName=" + processModelName);
        JSONObject jsonObject = new JSONObject();

        if(processModelName != null && !"".equals(processModelName)){
            try {
                flowNodeSettingService.saveSync(processModelName);
                jsonObject.put("success" , true);
            } catch (ServiceException e) {
                jsonObject.put("success" , false);
                e.printStackTrace();
            }
        } else {
            jsonObject.put("success" , false);
            jsonObject.put("msg" , "processModelName can't be null");
        }

        endHandle(request, response, jsonObject, "");
    }

}
