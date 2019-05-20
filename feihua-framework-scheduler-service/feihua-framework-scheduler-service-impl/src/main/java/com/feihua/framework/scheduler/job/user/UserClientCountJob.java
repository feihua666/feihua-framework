package com.feihua.framework.scheduler.job.user;

import com.feihua.framework.base.modules.loginclient.api.ApiBaseLoginClientPoService;
import com.feihua.framework.base.modules.loginclient.po.BaseLoginClientPo;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.scheduler.BaseQuartzJob;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.session.ShiroJedisSessionDAO;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.framework.statistic.api.ApiStatisticUserClientCountHistoryPoService;
import com.feihua.framework.statistic.api.ApiStatisticUserClientCountPoService;
import com.feihua.framework.statistic.po.StatisticUserClientCountHistoryPo;
import com.feihua.framework.statistic.po.StatisticUserClientCountPo;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.quartz.JobExecutionContext;

import java.util.*;

import static com.feihua.framework.scheduler.QuartzSchedulerUtils.getBatchNo;

/**
 * 用户客户端数据统计
 * Created by yangwei
 * Created at 2019/4/17 15:46
 */
public class UserClientCountJob extends BaseQuartzJob {


    private ApiBaseUserPoService apiBaseUserPoService;
    private ApiStatisticUserClientCountPoService apiStatisticUserClientCountPoService;
    private ApiBaseLoginClientPoService apiBaseClientPoService;
    private ShiroJedisSessionDAO shiroJedisSessionDAO;
    private ApiStatisticUserClientCountHistoryPoService apiStatisticUserClientCountHistoryPoService;

    private static String onlineAllNumKey = "onlineAllNum";
    private static String onlineMaleNumKey = "onlineMaleNum";
    private static String onlineFemaleNumKey = "onlineFemaleNum";
    private static String onlineGenderOtherNumKey = "onlineGenderOtherNum";

    @Override
    public void doExecute(JobExecutionContext jobExecutionContext) {
        this.initBean();
        String batchNo = getBatchNo(jobExecutionContext,true);

        Map<String,Integer> map = new HashMap<>();
        this.onlineNum(map);

        // 查询所有客户端
        List<BaseLoginClientPo> clientPos = apiBaseClientPoService.selectByIsVirtual(BasePo.YesNo.N);

        // 历史待插入集合
        List<StatisticUserClientCountHistoryPo> historyToBeInsert = new ArrayList<>(clientPos.size());
        // 最新待插入集合
        List<StatisticUserClientCountPo> toBeInsert = new ArrayList<>(clientPos.size());
        StatisticUserClientCountHistoryPo countHistoryPo = null;
        StatisticUserClientCountPo countPo = null;

        BaseUserPo baseUserPoConditon = null;
        for (BaseLoginClientPo clientPo : clientPos) {
            int allNum = 0;
            int maleNum = 0;
            int femaleNum = 0;
            int genderOtherNum = 0;
            int onlineAllNum = 0;
            if(map.get(clientPo.getId() + onlineAllNumKey) != null) {
                onlineAllNum = map.get(clientPo.getId() + onlineAllNumKey);
            }
            int onlineMaleNum = 0;
            if(map.get(clientPo.getId() + onlineMaleNumKey) != null) {
                onlineMaleNum = map.get(clientPo.getId() + onlineMaleNumKey);
            }
            int onlineFemaleNum = 0;
            if(map.get(clientPo.getId() + onlineFemaleNumKey) != null) {
                onlineFemaleNum = map.get(clientPo.getId() + onlineFemaleNumKey);
            }
            int onlineGenderOtherNum = onlineAllNum - onlineMaleNum - onlineFemaleNum;

            baseUserPoConditon = new BaseUserPo();
            baseUserPoConditon.setDelFlag(BasePo.YesNo.N.name());
            baseUserPoConditon.setFromClientId(clientPo.getId());
            allNum = apiBaseUserPoService.count(baseUserPoConditon);

            baseUserPoConditon = new BaseUserPo();
            baseUserPoConditon.setDelFlag(BasePo.YesNo.N.name());
            baseUserPoConditon.setFromClientId(clientPo.getId());
            baseUserPoConditon.setGender(DictEnum.Gender.male.name());
            maleNum = apiBaseUserPoService.count(baseUserPoConditon);

            baseUserPoConditon = new BaseUserPo();
            baseUserPoConditon.setDelFlag(BasePo.YesNo.N.name());
            baseUserPoConditon.setFromClientId(clientPo.getId());
            baseUserPoConditon.setGender(DictEnum.Gender.female.name());
            femaleNum = apiBaseUserPoService.count(baseUserPoConditon);

            genderOtherNum = allNum - femaleNum - maleNum;

            // 历史记录
            countHistoryPo = new StatisticUserClientCountHistoryPo();
            countHistoryPo.setBatchNo(batchNo);
            countHistoryPo.setAllNum(allNum);
            countHistoryPo.setMaleNum(maleNum);
            countHistoryPo.setFemaleNum(femaleNum);
            countHistoryPo.setGenderOtherNum(genderOtherNum);
            countHistoryPo.setOnlineAllNum(onlineAllNum);
            countHistoryPo.setOnlineFemaleNum(onlineMaleNum);
            countHistoryPo.setOnlineMaleNum(onlineFemaleNum);
            countHistoryPo.setOnlineGenderOtherNum(onlineGenderOtherNum);
            countHistoryPo.setClientId(clientPo.getId());
            countHistoryPo.setClientName(clientPo.getName());
            countHistoryPo.setClientCode(clientPo.getCode());
            countHistoryPo = apiStatisticUserClientCountHistoryPoService.preInsert(countHistoryPo, BasePo.DEFAULT_USER_ID);
            historyToBeInsert.add(countHistoryPo);

            // 最新记录
            countPo = new StatisticUserClientCountPo();
            countPo.setBatchNo(batchNo);
            countPo.setAllNum(allNum);
            countPo.setMaleNum(maleNum);
            countPo.setFemaleNum(femaleNum);
            countPo.setGenderOtherNum(genderOtherNum);
            countPo.setOnlineAllNum(onlineAllNum);
            countPo.setOnlineFemaleNum(onlineMaleNum);
            countPo.setOnlineMaleNum(onlineFemaleNum);
            countPo.setOnlineGenderOtherNum(onlineGenderOtherNum);
            countPo.setClientId(clientPo.getId());
            countPo.setClientName(clientPo.getName());
            countPo.setClientCode(clientPo.getCode());
            countPo = apiStatisticUserClientCountPoService.preInsert(countPo, BasePo.DEFAULT_USER_ID);

            toBeInsert.add(countPo);
        }
        apiStatisticUserClientCountHistoryPoService.insertBatch(historyToBeInsert);
        // 先删除再添加，保留最新的一份
        apiStatisticUserClientCountPoService.deleteSelective(new StatisticUserClientCountPo());
        apiStatisticUserClientCountPoService.insertBatch(toBeInsert);
    }

    private void initBean(){
        if (apiBaseUserPoService == null) {
            apiBaseUserPoService = SpringContextHolder.getBean(ApiBaseUserPoService.class);
        }
        if (apiStatisticUserClientCountPoService == null) {
            apiStatisticUserClientCountPoService = SpringContextHolder.getBean(ApiStatisticUserClientCountPoService.class);
        }
        if (apiBaseClientPoService == null) {
            apiBaseClientPoService = SpringContextHolder.getBean(ApiBaseLoginClientPoService.class);
        }
        if (shiroJedisSessionDAO == null) {
            shiroJedisSessionDAO = SpringContextHolder.getBean(ShiroJedisSessionDAO.class);
        }
        if (apiStatisticUserClientCountHistoryPoService == null) {
            apiStatisticUserClientCountHistoryPoService = SpringContextHolder.getBean(ApiStatisticUserClientCountHistoryPoService.class);
        }
    }
    private void putIncreaseMapNum(String clientCode, String suffix, Map<String,Integer> map){
        Integer num = map.get(clientCode + suffix);
        if (num == null) {
            num = 0;
        }
        // 在线男数
        map.put(clientCode + suffix, num + 1);
    }
    private void onlineNum(Map<String,Integer> map) {
        Collection<Session> sessions =  shiroJedisSessionDAO.getActiveSessions();
        if (sessions != null) {
            ShiroUser su = null;
            String loginClientId = null;
            for (Session session : sessions) {
                su = ShiroUtils.getShiroUser(session);
                if (su == null) {
                    continue;
                }
                loginClientId = su.getLoginClientId();
                if (StringUtils.isNotEmpty(loginClientId)){
                    putIncreaseMapNum(loginClientId,onlineAllNumKey,map);
                    if(DictEnum.Gender.female.name().equals(su.getGender())){
                        putIncreaseMapNum(loginClientId,onlineFemaleNumKey,map);
                    } else if(DictEnum.Gender.male.name().equals(su.getGender())){
                        putIncreaseMapNum(loginClientId,onlineMaleNumKey,map);
                    }
                }
            }

        }
    }
}
