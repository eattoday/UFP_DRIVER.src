package com.metarnet.workflow.listener;

import com.eos.data.datacontext.DataContextManager;
import com.eos.runtime.resource.ContributionMetaData;
import com.primeton.common.connection.impl.datasource.DataSourceReference;

/**
 * Created by Administrator on 2017/3/18/0018.
 */
public class DataSourceHelper
{
    public static void prepare(String dsName)
    {
        String contributionName = "bps";
        String datasourceAlias = dsName;
        String datasourceRealName = dsName;
        DataSourceReference.putDataSourceReference(contributionName, datasourceAlias, datasourceRealName);

        ContributionMetaData cmd = new ContributionMetaData();
        cmd.setName(contributionName);
        DataContextManager.current().pushContributionMetaData(cmd);
    }
}