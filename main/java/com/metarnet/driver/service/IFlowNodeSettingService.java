package com.metarnet.driver.service;

import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.driver.model.FlowNodeSettingBase;
import com.metarnet.driver.model.FlowNodeSettingEntity;
import com.metarnet.driver.model.FlowNodeSettingTmpEntity;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
public interface IFlowNodeSettingService {

    /**
     * 保存设置
     * @param flowNodeSettingEntity
     * @throws ServiceException
     */
    public abstract void saveSetting(FlowNodeSettingTmpEntity flowNodeSettingEntity , UserEntity userEntity) throws ServiceException;

    /**
     * 检查所有的设置项，更新活动定义ID，删除已删除节点的配置
     * @param processModelName
     * @param nodeSettings
     * @param userEntity
     * @throws ServiceException
     */
    public abstract void saveCheckSetting(String processModelName , String nodeSettings , UserEntity userEntity) throws ServiceException;

    /**
     * 发布配置
     * 1、当前配置进入历史表
     * 2、临时配置更新状态为SETTING_STATUS_PRO
     * 3、删除临时配置
     * 4、设置配置bpsVersion
     * @param processModelName
     * @param bpsVersion
     * @param userEntity
     * @throws ServiceException
     */
    public abstract void savePublishSetting(String processModelName , String bpsVersion , UserEntity userEntity , String processModelId) throws ServiceException;

    /**
     * 获取当前节点对应的设置
     * @param flowNodeSettingEntity
     * @return
     * @throws ServiceException
     */
    public abstract FlowNodeSettingBase getSetting(FlowNodeSettingBase flowNodeSettingEntity) throws ServiceException;

    /**
     * 获取设置列表
     * @param flowNodeSettingEntity
     * @return
     * @throws ServiceException
     */
    public abstract List<FlowNodeSettingBase> findSettingsByExample(FlowNodeSettingBase flowNodeSettingEntity) throws ServiceException;

    /**
     * 删除生产配置
     * @param processModelName
     * @throws ServiceException
     */
    public abstract void deleteProSettings(String processModelName , String processModelId) throws ServiceException;

    /**
     * 更新配置状态
     * @param processModelName
     * @param from
     * @param to
     * @throws ServiceException
     */
    public abstract void updateSettingStatus(String processModelName , String from , String to , String bpsVersion) throws ServiceException;


    /**
     * 保存历史配置
     * @param list
     * @param userEntity
     * @param settingEntity
     * @throws ServiceException
     */
    public abstract void saveSettingList(List<FlowNodeSettingBase> list , UserEntity userEntity , Class settingEntity) throws ServiceException;
    public abstract void saveSettingList(List<FlowNodeSettingBase> list , UserEntity userEntity , Class settingEntity , String bpsVersion , String processModelId) throws ServiceException;


    /**
     * 将指定流程相关的配置从测试区同步到生产区
     * @param processModelName
     * @throws ServiceException
     */
    public abstract void saveSync(String processModelName) throws ServiceException;

}
