package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.core.common.utils.PagerPropertyUtils;
import com.metarnet.driver.model.ComponentFormEntity;
import com.metarnet.driver.model.FlowButtonEntity;
import com.metarnet.driver.service.IComponentFormService;
import com.metarnet.driver.service.IFlowButtonService;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10/0010.
 */
@Controller
public class FlowButtonController extends CommonController {

    Logger logger = LogManager.getLogger("ComponentForm");

    @Resource
    private IFlowButtonService flowButtonService;

    @RequestMapping(value = "/flowButtonInit.do")
    @ResponseBody
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response , String tenantId) throws UIException {

        return new ModelAndView(new InternalResourceView("/base/page/publishLaunch.jsp"));

    }

    @RequestMapping(value = "/flowButtonSave.do")
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response , FlowButtonEntity flowButtonEntity) throws UIException {

        FlowButtonEntity tmpButton = new FlowButtonEntity();
        tmpButton.setButtonName(flowButtonEntity.getButtonName());

        super.saveNoExsit(request , response , flowButtonEntity ,  tmpButton);

    }

    @RequestMapping(value = "/flowButtonFindById.do")
    @ResponseBody
    public void findById(HttpServletRequest request, HttpServletResponse response , FlowButtonEntity flowButtonEntity) throws UIException {

        super.findById(request , response , flowButtonEntity);

    }

    @RequestMapping(value = "/flowButtonQuery.do")
    @ResponseBody
    public void query(HttpServletRequest request, HttpServletResponse response , FlowButtonEntity flowButtonEntity , String dtGridPager) throws UIException {

        super.query(request , response , flowButtonEntity , dtGridPager);

    }

    @RequestMapping(value = "/flowButtonDelete.do")
    @ResponseBody
    public void delete(HttpServletRequest request, HttpServletResponse response , FlowButtonEntity flowButtonEntity) throws UIException {

        super.delete(request , response , flowButtonEntity);

    }

}
