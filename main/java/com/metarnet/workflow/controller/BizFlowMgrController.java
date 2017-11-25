package com.metarnet.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.core.common.utils.PagerPropertyUtils;
import com.metarnet.workflow.service.IBizFlowMgrService;
import com.metarnet.workflow.service.IWorkflowService;
import com.primeton.bps.data.WFBizProcessDef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
@Controller
public class BizFlowMgrController extends BaseController {

    Logger logger = LogManager.getLogger("BizFlowMgrController");

    @Resource
    private IBizFlowMgrService bizFlowMgrService;

    @RequestMapping(value = "/updateProcessDefTmp.do")
    @ResponseBody
    public void updateProcessDefTmp(HttpServletRequest request, HttpServletResponse response , WFBizProcessDef processDef) throws UIException {
        JSONObject jsonObject = new JSONObject();

        try {
            bizFlowMgrService.updateProcessDefTmp(processDef);
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success", false);
        }
        endHandle(request, response, jsonObject.toJSONString(), "");
    }

    @RequestMapping(value = "/updateProcessDef.do")
    @ResponseBody
    public void updateProcessDef(HttpServletRequest request, HttpServletResponse response , WFBizProcessDef processDef) throws UIException {
        JSONObject jsonObject = new JSONObject();

        try {
            bizFlowMgrService.updateProcessDef(processDef);
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success", false);
        }
        endHandle(request, response, jsonObject.toJSONString(), "");
    }

    @RequestMapping(value = "/getProcessDefById.do")
    @ResponseBody
    public void getProcessDefById(HttpServletRequest request, HttpServletResponse response , String processDefId) throws UIException {
        Map map = new HashMap();
        try {
            map = bizFlowMgrService.getProcessDefById(processDefId);
        } catch (ServiceException e) {
            logger.info("getProcessDefById encounter Exception:\n");
            logger.info(e.getMessage());
        }

        endHandle(request, response, JSON.toJSONString(map), "");
    }
}
