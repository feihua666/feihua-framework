package com.feihua.framework.scheduler.job.user;

import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.scheduler.BaseQuartzJob;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.session.ShiroJedisSessionDAO;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.framework.statistic.api.ApiStatisticUserCountHistoryPoService;
import com.feihua.framework.statistic.api.ApiStatisticUserCountPoService;
import com.feihua.framework.statistic.po.StatisticUserCountHistoryPo;
import com.feihua.framework.statistic.po.StatisticUserCountPo;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.RandomUtils;
import org.apache.shiro.session.Session;
import org.quartz.JobExecutionContext;

import java.util.Collection;
import java.util.UUID;

import static com.feihua.framework.scheduler.QuartzSchedulerUtils.getBatchNo;

/**
 * 用户基本数量统计
 * Created by yangwei
 * Created at 2019/4/17 15:46
 */
public class UserCountJob extends BaseQuartzJob {

    private ApiBaseUserPoService apiBaseUserPoService;
    private ApiStatisticUserCountPoService apiStatisticUserCountPoService;
    private ApiStatisticUserCountHistoryPoService apiStatisticUserCountHistoryPoService;
    private ShiroJedisSessionDAO shiroJedisSessionDAO;

    @Override
    public void doExecute(JobExecutionContext jobExecutionContext) {
        this.initBean();
        // 用户统计
        String batchNo = getBatchNo(jobExecutionContext,true);

        BaseUserPo baseUserPoConditon = null;

        baseUserPoConditon = new BaseUserPo();
        baseUserPoConditon.setDelFlag(BasePo.YesNo.N.name());
        int allNum = apiBaseUserPoService.count(baseUserPoConditon);

        baseUserPoConditon = new BaseUserPo();
        baseUserPoConditon.setDelFlag(BasePo.YesNo.N.name());
        baseUserPoConditon.setGender(DictEnum.Gender.male.name());
        int maleNum = apiBaseUserPoService.count(baseUserPoConditon);

        baseUserPoConditon = new BaseUserPo();
        baseUserPoConditon.setDelFlag(BasePo.YesNo.N.name());
        baseUserPoConditon.setGender(DictEnum.Gender.female.name());
        int femaleNum = apiBaseUserPoService.count(baseUserPoConditon);

        int genderOtherNum = allNum - femaleNum - maleNum;

        Collection<Session> sessions =  shiroJedisSessionDAO.getActiveSessions();
        int onlineAllNum = 0;
        int onlineMaleNum = 0;
        int onlineFemaleNum = 0;
        int onlineGenderOtherNum = 0;
        if (sessions != null) {
            onlineAllNum = sessions.size();
            for (Session session : sessions) {
                ShiroUser su = ShiroUtils.getShiroUser(session);
                if (su == null) {
                    continue;
                }
                if(DictEnum.Gender.female.name().equals(su.getGender())){
                    onlineFemaleNum ++;
                } else if(DictEnum.Gender.male.name().equals(su.getGender())){
                    onlineMaleNum ++;
                }
            }

            onlineGenderOtherNum = onlineAllNum - onlineMaleNum - onlineFemaleNum;
        }


        // 添加历史记录
        StatisticUserCountHistoryPo statisticUserCountHistoryPo = new StatisticUserCountHistoryPo();
        statisticUserCountHistoryPo.setBatchNo(batchNo);
        statisticUserCountHistoryPo.setAllNum(allNum);
        statisticUserCountHistoryPo.setMaleNum(maleNum);
        statisticUserCountHistoryPo.setFemaleNum(femaleNum);
        statisticUserCountHistoryPo.setGenderOtherNum(genderOtherNum);

        statisticUserCountHistoryPo.setOnlineAllNum(onlineAllNum);
        statisticUserCountHistoryPo.setOnlineFemaleNum(onlineFemaleNum);
        statisticUserCountHistoryPo.setOnlineMaleNum(onlineMaleNum);
        statisticUserCountHistoryPo.setOnlineGenderOtherNum(onlineGenderOtherNum);
        statisticUserCountHistoryPo = apiStatisticUserCountHistoryPoService.preInsert(statisticUserCountHistoryPo, BasePo.DEFAULT_USER_ID);
        apiStatisticUserCountHistoryPoService.insert(statisticUserCountHistoryPo);

        // 添加最新记录
        StatisticUserCountPo statisticUserCountPo = new StatisticUserCountPo();
        statisticUserCountPo.setBatchNo(batchNo);
        statisticUserCountPo.setAllNum(allNum);
        statisticUserCountPo.setMaleNum(maleNum);
        statisticUserCountPo.setFemaleNum(femaleNum);
        statisticUserCountPo.setGenderOtherNum(genderOtherNum);

        statisticUserCountPo.setOnlineAllNum(onlineAllNum);
        statisticUserCountPo.setOnlineFemaleNum(onlineFemaleNum);
        statisticUserCountPo.setOnlineMaleNum(onlineMaleNum);
        statisticUserCountPo.setOnlineGenderOtherNum(onlineGenderOtherNum);
        // 全部删除，只保留最新的一份
        apiStatisticUserCountPoService.deleteSelective(new StatisticUserCountPo());
        statisticUserCountPo = apiStatisticUserCountPoService.preInsert(statisticUserCountPo, BasePo.DEFAULT_USER_ID);
        apiStatisticUserCountPoService.insert(statisticUserCountPo);

    }

    private void initBean(){
        if (apiBaseUserPoService == null) {
            apiBaseUserPoService = SpringContextHolder.getBean(ApiBaseUserPoService.class);
        }
        if (apiStatisticUserCountPoService == null) {
            apiStatisticUserCountPoService = SpringContextHolder.getBean(ApiStatisticUserCountPoService.class);
        }
        if (shiroJedisSessionDAO == null) {
            shiroJedisSessionDAO = SpringContextHolder.getBean(ShiroJedisSessionDAO.class);
        }
        if (apiStatisticUserCountHistoryPoService == null) {
            apiStatisticUserCountHistoryPoService = SpringContextHolder.getBean(ApiStatisticUserCountHistoryPoService.class);
        }
    }
}
