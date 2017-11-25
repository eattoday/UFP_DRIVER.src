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
import com.metarnet.core.common.utils.BeanUtils;
import com.metarnet.core.common.utils.PagerPropertyUtils;
import com.metarnet.driver.model.ComponentFormEntity;
import com.metarnet.driver.service.IComponentFormService;
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
public class ComponentFormController extends BaseController{

    Logger logger = LogManager.getLogger("ComponentForm");

    @Resource
    private IComponentFormService componentFormService;

    @RequestMapping(value = "/componentFormInit.do")
    @ResponseBody
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response , String tenantId) throws UIException {

        return new ModelAndView(new InternalResourceView("/base/page/publishLaunch.jsp"));
    }

    @RequestMapping(value = "/componentFormSave.do")
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response , ComponentFormEntity componentFormEntity) throws UIException {
        JSONObject jsonObject = new JSONObject();

        List<ComponentFormEntity> list = null;

        Boolean isExsit = false;

        ComponentFormEntity tmpForm = new ComponentFormEntity();
        tmpForm.setComponentName(componentFormEntity.getComponentName());
        try {
            list = componentFormService.query(tmpForm);
            if(list != null && list.size() > 0){
                if(!list.get(0).getId().equals(componentFormEntity.getId())){
                    isExsit = true;
                } else {
                    tmpForm = list.get(0);
                    BeanUtils.copyProperties(componentFormEntity , tmpForm);
                    componentFormEntity = tmpForm;
                }
            }
        } catch (ServiceException e) {
            logger.info("检查重复表单名称报错\n" + e.getLocalizedMessage());
        }

        if(!isExsit){
            UserEntity userEntity = getUserEntity(request);

            try {
                componentFormService.save(componentFormEntity, userEntity);
                jsonObject.put("success" , true);
            } catch (ServiceException e) {
                jsonObject.put("success", false);
            }
        } else {
            jsonObject.put("success", false);
            jsonObject.put("msg" , "表单名称已存在");
        }

        endHandle(request , response , jsonObject , null);
    }

    @RequestMapping(value = "/componentFormFindById.do")
    @ResponseBody
    public void findById(HttpServletRequest request, HttpServletResponse response , ComponentFormEntity componentFormEntity) throws UIException {
        logger.info("into findById...");
        ComponentFormEntity formEntity = new ComponentFormEntity();
        try {
            formEntity = (ComponentFormEntity) componentFormService.findById(componentFormEntity);
        } catch (ServiceException e) {
            logger.info("componentFormService.findById ERROR\n" + e.getLocalizedMessage());
        }

        SerializeConfig ser = new SerializeConfig();
        ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

        endHandle(request , response , JSON.toJSONString(formEntity, ser, SerializerFeature.WriteNullListAsEmpty) , null);
    }

    @RequestMapping(value = "/componentFormQuery.do")
    @ResponseBody
    public void query(HttpServletRequest request, HttpServletResponse response , ComponentFormEntity componentFormEntity , String dtGridPager) throws UIException {

        Pager pager = null;
        try {
            pager = PagerPropertyUtils.copy(dtGridPager);
            pager = componentFormService.queryByPager(componentFormEntity, pager);
        } catch (ServiceException e) {
            logger.info("componentFormService.queryByPager ERROR\n" + e.getLocalizedMessage());
        } catch (Exception e) {
            logger.info("PagerPropertyUtils.copy ERROR\n" + e.getLocalizedMessage());
        }

        SerializeConfig ser = new SerializeConfig();
        ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

        endHandle(request , response , JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty) , null);
    }

    @RequestMapping(value = "/componentFormDelete.do")
    @ResponseBody
    public void delete(HttpServletRequest request, HttpServletResponse response , ComponentFormEntity componentFormEntity) throws UIException {

        JSONObject jsonObject = new JSONObject();

        try {
            componentFormService.delete(componentFormEntity);
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success", false);
        }

        endHandle(request , response , jsonObject , null);
    }

}
