package com.metarnet.driver.model;


import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2016/3/8.
 */
@Entity
@Table(name = "flow_node_setting_tmp")
@Where(clause = "DELETED_FLAG=0")
public class FlowNodeSettingTmpEntity extends FlowNodeSettingBase {


}
