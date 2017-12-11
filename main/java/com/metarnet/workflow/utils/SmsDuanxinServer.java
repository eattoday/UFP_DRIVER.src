package com.metarnet.workflow.utils;

import com.eoms2.sms.adapter.RmiAdapter;
import org.apache.log4j.Logger;

import java.rmi.Naming;

/**
 * Title: eoms3<br>
 * Description: <br>
 * Copyright: Copyright 2017 <br>
 * Create DateTime: Sep 19, 2017 10:31:06 AM <br>
 * CVS last modify person: gpp <br>
 * CVS last modify DateTime: <br>
 * CVS last version: Revision: 1.0<br>
 * 
 * 
 * @author gpp
 */

public class SmsDuanxinServer {


	// 新增短信通知功能指定手机号和通知内容
	public static void duanxinByphone(String phone, String returnXmlStr) {

		try {
			// 取得短信配置信息
			String smsHost = "10.147.180.12:1334";
			String smsSystem_id = "lizhidan";
			String smsPassword = "lizhidan123";
			if (1==1) {
				System.out.println("发送前");
				RmiAdapter rmi = (RmiAdapter) Naming.lookup("rmi://" + smsHost + "/SmsRmiAdapter");
				System.out.println("发送后"+rmi);
				String tmp = "<root><username>u###</username><password>p###</password><message><address>a###</address><content>b###</content></message></root>";
				String msg = returnXmlStr;
				if (1==1) {
					if (phone!=null&&!"".equals(phone)) {
						// 填入短信服务用户名
						tmp = tmp.replaceAll("u###", smsSystem_id);
						// 填入短信服务密码
						tmp = tmp.replaceAll("p###", smsPassword);
						// 手机号
						tmp = tmp.replaceAll("a###", phone);
						// 内容
						tmp = tmp.replaceAll("b###", msg);
						System.out.println("发送前:"+tmp);
						rmi.sendSms(tmp);
						System.out.println("发送后11111");
					}
					tmp = "<root><username>u###</username><password>p###</password><message><address>a###</address><content>b###</content></message></root>";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
