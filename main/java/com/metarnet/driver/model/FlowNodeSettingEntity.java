package com.metarnet.driver.model;


import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/3/8.
 */
@Entity
@Table(name = "flow_node_setting")
@Where(clause = "DELETED_FLAG=0")
public class FlowNodeSettingEntity extends FlowNodeSettingBase {


}
