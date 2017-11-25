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
import com.metarnet.driver.model.CommonEntity;
import com.metarnet.driver.model.ComponentFormEntity;
import com.metarnet.driver.service.ICommonService;
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
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10/0010.
 */
public class CommonController extends BaseController{

    Logger logger = LogManager.getLogger("CommonController");

    @Resource(name="commonServiceImpl")
    private ICommonService commonService;

//    @RequestMapping(value = "/commonInit.do")
//    @ResponseBody
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response , String tenantId) throws UIException {

        return new ModelAndView(new InternalResourceView("/base/page/publishLaunch.jsp"));
    }

    public void saveNoExsit(HttpServletRequest request, HttpServletResponse response , CommonEntity object , CommonEntity tmpObj) throws UIException {
        JSONObject jsonObject = new JSONObject();

        List<CommonEntity> list = null;

        Boolean isExsit = false;

        try {
            list = commonService.query(tmpObj);
            if(list != null && list.size() > 0){
                if(!list.get(0).getId().equals(object.getId())){
                    isExsit = true;
                }
            }
        } catch (ServiceException e) {
            logger.info("检查重复实体报错\n" + e.getLocalizedMessage());
        }

        if(!isExsit){
            UserEntity userEntity = getUserEntity(request);

            try {
                commonService.save(object, userEntity);
                jsonObject.put("success" , true);
            } catch (ServiceException e) {
                jsonObject.put("success", false);
            }
        } else {
            jsonObject.put("success", false);
            jsonObject.put("msg" , "名称已存在");
        }

        endHandle(request , response , jsonObject , null);
    }

//    @RequestMapping(value = "/commonSave.do")
//    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response , CommonEntity object) throws UIException {
        JSONObject jsonObject = new JSONObject();

        UserEntity userEntity = getUserEntity(request);

        try {
            commonService.save(object, userEntity);
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success", false);
        }

        endHandle(request , response , jsonObject , null);
    }

//    @RequestMapping(value = "/commonFindById.do")
//    @ResponseBody
    public void findById(HttpServletRequest request, HttpServletResponse response , Serializable object) throws UIException {
        logger.info("into findById...");
        CommonEntity commonEntity = new CommonEntity();
        try {
            commonEntity = (CommonEntity) commonService.findById(object);
        } catch (ServiceException e) {
            logger.info("commonService.findById ERROR\n" + e.getLocalizedMessage());
        }

        SerializeConfig ser = new SerializeConfig();
        ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

        endHandle(request , response , JSON.toJSONString(commonEntity, ser, SerializerFeature.WriteNullListAsEmpty) , null);
    }

//    @RequestMapping(value = "/commonQuery.do")
//    @ResponseBody
    public void query(HttpServletRequest request, HttpServletResponse response , CommonEntity object , String dtGridPager) throws UIException {

        Pager pager = null;
        try {
            pager = PagerPropertyUtils.copy(dtGridPager);
            pager = commonService.queryByPager(object, pager);
        } catch (ServiceException e) {
            logger.info("commonService.queryByPager ERROR\n" + e.getLocalizedMessage());
        } catch (Exception e) {
            logger.info("PagerPropertyUtils.copy ERROR\n" + e.getLocalizedMessage());
        }

        SerializeConfig ser = new SerializeConfig();
        ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        System.out.println(pager.toString());
        endHandle(request , response , JSON.toJSONString(pager, ser, SerializerFeature.WriteNullListAsEmpty) , null);
    }



//    @RequestMapping(value = "/commonDelete.do")
//    @ResponseBody
    public void delete(HttpServletRequest request, HttpServletResponse response , CommonEntity object) throws UIException {

        JSONObject jsonObject = new JSONObject();

        try {
            commonService.delete(object);
            jsonObject.put("success" , true);
        } catch (ServiceException e) {
            jsonObject.put("success", false);
        }

        endHandle(request , response , jsonObject , null);
    }

}
