package com.metarnet.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.utils.BeanUtils;
import com.metarnet.core.common.utils.HttpClientUtil;
import com.metarnet.driver.dao.IProDao;
import com.metarnet.driver.model.FlowNodeSettingBase;
import com.metarnet.driver.model.FlowNodeSettingEntity;
import com.metarnet.driver.model.FlowNodeSettingHisEntity;
import com.metarnet.driver.model.FlowNodeSettingTmpEntity;
import com.metarnet.driver.service.IFlowNodeSettingService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
@Service
public class FlowNodeSettingServiceImpl implements IFlowNodeSettingService {

    Logger logger = LogManager.getLogger();

    @Resource
    private IBaseDAO baseDAO;

    @Resource
    private IProDao proDao;

    @Override
    public void saveSetting(FlowNodeSettingTmpEntity flowNodeSettingEntity, UserEntity userEntity) throws ServiceException {
        try {
            FlowNodeSettingTmpEntity currentSetting = new FlowNodeSettingTmpEntity();
            currentSetting.setSettingID(flowNodeSettingEntity.getSettingID());
            List<FlowNodeSettingBase> list = findSettingsByExample(currentSetting);
            if (list.size() > 0) {
                FlowNodeSettingTmpEntity flowNodeSetting = (FlowNodeSettingTmpEntity) list.get(0);
                flowNodeSetting.setActivityDefID(flowNodeSettingEntity.getActivityDefID());
                flowNodeSetting.setFormType(flowNodeSettingEntity.getFormType());
                flowNodeSetting.setFormID(flowNodeSettingEntity.getFormID());
                flowNodeSetting.setFormName(flowNodeSettingEntity.getFormName());
                flowNodeSetting.setComponentID(flowNodeSettingEntity.getComponentID());
                flowNodeSetting.setComponentName(flowNodeSettingEntity.getComponentName());
                flowNodeSetting.setCustomURL(flowNodeSettingEntity.getCustomURL());
                flowNodeSetting.setExtand(flowNodeSettingEntity.getExtand());
                flowNodeSetting.setTenantId(flowNodeSettingEntity.getTenantId());
                flowNodeSetting.setAreaName(flowNodeSettingEntity.getAreaName());
                flowNodeSetting.setComponent(flowNodeSettingEntity.getComponent());
                flowNodeSetting.setEditLinks(flowNodeSettingEntity.getEditLinks());
                flowNodeSetting.setShowLinks(flowNodeSettingEntity.getShowLinks());
                flowNodeSetting.setBtnIDs(flowNodeSettingEntity.getBtnIDs());
                flowNodeSetting.setBtnNames(flowNodeSettingEntity.getBtnNames());
                baseDAO.saveOrUpdate(flowNodeSetting, userEntity);
            } else {
                flowNodeSettingEntity.setId(baseDAO.getSequenceNextValue(FlowNodeSettingTmpEntity.class));
                baseDAO.save(flowNodeSettingEntity, userEntity);
            }

        } catch (DAOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void saveCheckSetting(String processModelName, String nodeSettings, UserEntity userEntity) throws ServiceException {

        //转换需要check的配置列表
        List<FlowNodeSettingTmpEntity> nowList = JSON.parseArray(nodeSettings, FlowNodeSettingTmpEntity.class);

        //跟进流程定义ID查询当前保存的配置列表
        FlowNodeSettingTmpEntity flowNodeSettingEntity = new FlowNodeSettingTmpEntity();
        flowNodeSettingEntity.setProcessModelName(processModelName);
        List<FlowNodeSettingBase> currentList = findSettingsByExample(flowNodeSettingEntity);

        //定义需要更新的配置列表
        List<FlowNodeSettingTmpEntity> updateList = new ArrayList<FlowNodeSettingTmpEntity>();

        Iterator<FlowNodeSettingTmpEntity> nowIt = nowList.iterator();

        while (nowIt.hasNext()) {
            FlowNodeSettingTmpEntity nowSetting = nowIt.next();
            Iterator<FlowNodeSettingBase> currentIt = currentList.iterator();
            while (currentIt.hasNext()) {
                FlowNodeSettingTmpEntity currentSetting = (FlowNodeSettingTmpEntity) currentIt.next();
                if (nowSetting.getSettingID().equals(currentSetting.getSettingID())) {

                    if (!currentSetting.getActivityDefID().equals(nowSetting.getActivityDefID())) {
                        currentSetting.setActivityDefID(nowSetting.getActivityDefID());
                        updateList.add(currentSetting);
                    }

                    nowIt.remove();
                    currentIt.remove();
                }
            }
        }

        //删除当前保存的多余的配置
        if (currentList.size() > 0) {
            for (int i = 0; i < currentList.size(); i++) {
                try {
                    baseDAO.delete(currentList.get(i));
                } catch (DAOException e) {
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }
            }

        }

        //对需要更新的配置进行更新
        if (updateList.size() > 0) {
            try {
                baseDAO.saveOrUpdateAll(updateList, userEntity);
            } catch (DAOException e) {
                logger.info(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void savePublishSetting(String processModelName, String bpsVersion, UserEntity userEntity, String processModelId) throws ServiceException {
        FlowNodeSettingEntity flowNodeSettingEntity = new FlowNodeSettingEntity();
        flowNodeSettingEntity.setProcessModelName(processModelName);
        List<FlowNodeSettingBase> proList = findSettingsByExample(flowNodeSettingEntity);
        logger.info("proList size = " + proList.size());
        if (proList.size() > 0) {
            saveSettingList(proList, userEntity, FlowNodeSettingHisEntity.class);
        }

        deleteProSettings(processModelName, processModelId);

        FlowNodeSettingTmpEntity flowNodeSettingTmpEntity = new FlowNodeSettingTmpEntity();
        flowNodeSettingTmpEntity.setProcessModelName(processModelName);
        List<FlowNodeSettingBase> tmpList = findSettingsByExample(flowNodeSettingTmpEntity);
        logger.info("tmpList size = " + tmpList.size());
        if (tmpList.size() > 0) {
            saveSettingList(tmpList, userEntity, FlowNodeSettingEntity.class, bpsVersion, processModelId);
        }

    }

    @Override
    public FlowNodeSettingBase getSetting(FlowNodeSettingBase flowNodeSettingEntity) throws ServiceException {
        FlowNodeSettingBase flowNodeSetting = null;
        if (flowNodeSettingEntity.getSettingID() != null) {
            try {
                FlowNodeSettingBase currentSetting = (FlowNodeSettingBase) Class.forName(flowNodeSettingEntity.getClass().getName()).newInstance();

                currentSetting.setSettingID(flowNodeSettingEntity.getSettingID());


                List<FlowNodeSettingBase> list = findSettingsByExample(currentSetting);
                if (list.size() > 0) {
                    flowNodeSetting = list.get(0);
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            List<FlowNodeSettingBase> list = findSettingsByExample(flowNodeSettingEntity);
            if (list.size() > 0) {
                flowNodeSetting = list.get(0);
            }
        }
        return flowNodeSetting;
    }

    @Override
    public List<FlowNodeSettingBase> findSettingsByExample(FlowNodeSettingBase flowNodeSettingEntity) throws ServiceException {
        List<FlowNodeSettingBase> list = new ArrayList<FlowNodeSettingBase>();
        try {
            list = baseDAO.findByExample(flowNodeSettingEntity);
        } catch (DAOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void deleteProSettings(String processModelName, String processModelId) throws ServiceException {
        String sql = "delete from flow_node_setting where PROCESS_MODEL_NAME = '" + processModelName + "' and PROCESS_MODEL_ID='" + processModelId + "'";
        try {
            baseDAO.executeSql(sql);
        } catch (DAOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateSettingStatus(String processModelName, String from, String to, String bpsVersion) throws ServiceException {
        String sql = "update flow_node_setting set SETTING_STATUS = '" + to + "' , BPS_VERSION = '" + bpsVersion + "' where PROCESS_MODEL_NAME = '" + processModelName + "' and SETTING_STATUS = '" + from + "'";
        try {
            baseDAO.executeSql(sql);
        } catch (DAOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void saveSettingList(List<FlowNodeSettingBase> list, UserEntity userEntity, Class settingEntity) throws ServiceException {
        saveSettingList(list, userEntity, settingEntity, null, null);
    }

    @Override
    public void saveSettingList(List<FlowNodeSettingBase> list, UserEntity userEntity, Class settingEntity, String bpsVersion, String processModelId) throws ServiceException {
        List newList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            try {
                FlowNodeSettingBase setting = (FlowNodeSettingBase) settingEntity.newInstance();
                BeanUtils.copyProperties(list.get(i), setting);
                try {
                    setting.setId(baseDAO.getSequenceNextValue(settingEntity));
                } catch (DAOException e) {
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }

                if (bpsVersion != null) {
                    setting.setBpsVersion(bpsVersion);
                }

                if (processModelId != null) {
                    setting.setProcessModelId(processModelId);
                }

                newList.add(setting);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        if (newList.size() > 0) {
            try {
                baseDAO.saveOrUpdateAll(newList);
            } catch (DAOException e) {
                logger.info(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveSync(String processModelName) throws ServiceException {

        FlowNodeSettingEntity flowNodeSettingEntity = new FlowNodeSettingEntity();
        flowNodeSettingEntity.setProcessModelName(processModelName);
        try {
            List<FlowNodeSettingEntity> list = baseDAO.findByExample(flowNodeSettingEntity);

            if (list != null && list.size() > 0) {
                List<FlowNodeSettingTmpEntity> newList = new ArrayList<FlowNodeSettingTmpEntity>();

                String maxProcessModelId = list.get(list.size() - 1).getProcessModelId();

                for (int i = list.size() - 1; i > -1; i--) {

                    String processModelId = list.get(i).getProcessModelId();
                    if (maxProcessModelId.equals(processModelId)) {
                        syncForm(list.get(i).getFormID(), list.get(i).getTenantId());

                        FlowNodeSettingTmpEntity tmpEntity = new FlowNodeSettingTmpEntity();
                        BeanUtils.copyProperties(list.get(i), tmpEntity);
                        newList.add(tmpEntity);
                    } else {
                        break;
                    }

                }

                proDao.executeSql("delete from flow_node_setting_tmp where PROCESS_MODEL_NAME = '" + processModelName + "'");

                proDao.saveOrUpdateAll(newList);
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
//      cform一键发布url
//      http://localhost:8080/cform/command/dispatcher/org.loushang.cform.form.cmd.FormDispatcherCmd/trialCformtoReal?formId=CeShi&tenantId=default

    }

    private void syncForm(String formId, String tenantId) {
        HttpClientUtil.sendGetRequest("http://10.245.2.221/cform_trial/command/dispatcher/org.loushang.cform.form.cmd.FormDispatcherCmd/trialCformtoReal?formId=" + formId + "&tenantId=" + tenantId, null);
    }
}
