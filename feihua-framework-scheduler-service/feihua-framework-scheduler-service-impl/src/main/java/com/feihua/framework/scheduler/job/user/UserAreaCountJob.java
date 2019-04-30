package com.feihua.framework.scheduler.job.user;

import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.area.po.BaseAreaPo;
import com.feihua.framework.base.modules.user.api.ApiBaseUserPoService;
import com.feihua.framework.base.modules.user.po.BaseUserPo;
import com.feihua.framework.constants.DictEnum;
import com.feihua.framework.scheduler.BaseQuartzJob;
import com.feihua.framework.shiro.pojo.ShiroUser;
import com.feihua.framework.shiro.session.ShiroJedisSessionDAO;
import com.feihua.framework.shiro.utils.ShiroUtils;
import com.feihua.framework.statistic.api.ApiStatisticUserAreaCountHistoryPoService;
import com.feihua.framework.statistic.api.ApiStatisticUserAreaCountPoService;
import com.feihua.framework.statistic.po.StatisticUserAreaCountHistoryPo;
import com.feihua.framework.statistic.po.StatisticUserAreaCountPo;
import com.feihua.utils.spring.SpringContextHolder;
import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.pojo.PageAndOrderbyParamDto;
import feihua.jdbc.api.pojo.PageResultDto;
import feihua.jdbc.api.utils.PageUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.quartz.JobExecutionContext;

import java.util.*;

import static com.feihua.framework.scheduler.QuartzSchedulerUtils.getBatchNo;

/**
 * 用户区域数量统计任务
 * 用户区域归属根据用户表本身的区域归属区分
 * Created by yangwei
 * Created at 2019/4/17 15:46
 */
public class UserAreaCountJob extends BaseQuartzJob {


    private ApiBaseUserPoService apiBaseUserPoService;
    private ApiStatisticUserAreaCountPoService apiStatisticUserAreaCountPoService;
    private ApiStatisticUserAreaCountHistoryPoService apiStatisticUserAreaCountHistoryPoService;
    private ApiBaseAreaPoService apiBaseAreaPoService;
    private ShiroJedisSessionDAO shiroJedisSessionDAO;
    private String provinceType = "province";

    private static String allNumKey = "allNum";
    private static String maleNumKey = "maleNum";
    private static String femaleNumKey = "femaleNum";
    private static String onlineAllNumKey = "onlineAllNum";
    private static String onlineMaleNumKey = "onlineMaleNum";
    private static String onlineFemaleNumKey = "onlineFemaleNum";

    @Override
    public void doExecute(JobExecutionContext jobExecutionContext) {
        this.initBean();
        String batchNo = getBatchNo(jobExecutionContext,true);
        Map<String,Integer> map = new HashMap<>();

        // 获取各区域在线数据
        this.onlineNum(map);
        // 获取所有用户区域数据
        this.num(map);
        //按省查询所有省
        BaseAreaPo baseAreaPo = new BaseAreaPo();
        baseAreaPo.setType(provinceType);
        baseAreaPo.setDelFlag(BasePo.YesNo.N.name());
        List<BaseAreaPo> provinces = apiBaseAreaPoService.selectListSimple(baseAreaPo);
        // 最新待插入集合
        List<StatisticUserAreaCountHistoryPo> historyToBeInsert = new ArrayList<>(provinces.size());
        List<StatisticUserAreaCountPo> toBeInsert = new ArrayList<>(provinces.size());
        StatisticUserAreaCountHistoryPo countHistoryPo = null;
        StatisticUserAreaCountPo countPo = null;
        for (BaseAreaPo province : provinces) {

            Map<String,Integer> r = getAreaAndChildrenAllCountNum(province,map);
            int allNum = r.get("allNum");
            int maleNum = r.get("maleNum");
            int femaleNum = r.get("femaleNum");
            int genderOtherNum = r.get("genderOtherNum");
            int onlineAllNum = r.get("onlineAllNum");
            int onlineMaleNum = r.get("onlineMaleNum");
            int onlineFemaleNum = r.get("onlineFemaleNum");
            int onlineGenderOtherNum = r.get("onlineGenderOtherNum");

            // 历史数据
            countHistoryPo = new StatisticUserAreaCountHistoryPo();
            countHistoryPo.setBatchNo(batchNo);
            countHistoryPo.setAllNum(allNum);
            countHistoryPo.setMaleNum(maleNum);
            countHistoryPo.setFemaleNum(femaleNum);
            countHistoryPo.setGenderOtherNum(genderOtherNum);
            countHistoryPo.setOnlineAllNum(onlineAllNum);
            countHistoryPo.setOnlineMaleNum(onlineMaleNum);
            countHistoryPo.setOnlineFemaleNum(onlineFemaleNum);
            countHistoryPo.setOnlineGenderOtherNum(onlineGenderOtherNum);
            countHistoryPo.setAreaId(province.getId());
            countHistoryPo.setAreaName(province.getName());
            countHistoryPo.setLongitude(province.getLongitude());
            countHistoryPo.setLatitude(province.getLatitude());
            countHistoryPo = apiStatisticUserAreaCountHistoryPoService.preInsert(countHistoryPo, BasePo.DEFAULT_USER_ID);
            historyToBeInsert.add(countHistoryPo);


            // 最新数据
            countPo = new StatisticUserAreaCountPo();
            countPo.setBatchNo(batchNo);
            countPo.setAllNum(allNum);
            countPo.setMaleNum(maleNum);
            countPo.setFemaleNum(femaleNum);
            countPo.setGenderOtherNum(genderOtherNum);
            countPo.setOnlineAllNum(onlineAllNum);
            countPo.setOnlineMaleNum(onlineMaleNum);
            countPo.setOnlineFemaleNum(onlineFemaleNum);
            countPo.setOnlineGenderOtherNum(onlineGenderOtherNum);
            countPo.setAreaId(province.getId());
            countPo.setAreaName(province.getName());
            countPo.setLongitude(province.getLongitude());
            countPo.setLatitude(province.getLatitude());
            countPo = apiStatisticUserAreaCountPoService.preInsert(countPo, BasePo.DEFAULT_USER_ID);

            toBeInsert.add(countPo);
        }

        apiStatisticUserAreaCountHistoryPoService.insertBatch(historyToBeInsert);
        // 先删除再添加，保留最新
        apiStatisticUserAreaCountPoService.deleteSelective(new StatisticUserAreaCountPo());
        apiStatisticUserAreaCountPoService.insertBatch(toBeInsert);
    }

    private void initBean(){
        if (apiBaseUserPoService == null) {
            apiBaseUserPoService = SpringContextHolder.getBean(ApiBaseUserPoService.class);
        }
        if (apiStatisticUserAreaCountPoService == null) {
            apiStatisticUserAreaCountPoService = SpringContextHolder.getBean(ApiStatisticUserAreaCountPoService.class);
        }
        if (apiBaseAreaPoService == null) {
            apiBaseAreaPoService = SpringContextHolder.getBean(ApiBaseAreaPoService.class);
        }
        if (shiroJedisSessionDAO == null) {
            shiroJedisSessionDAO = SpringContextHolder.getBean(ShiroJedisSessionDAO.class);
        }
        if (apiStatisticUserAreaCountHistoryPoService == null) {
            apiStatisticUserAreaCountHistoryPoService = SpringContextHolder.getBean(ApiStatisticUserAreaCountHistoryPoService.class);
        }
    }

    /**
     * 区域用户数量
     * @param map
     */
    private void num(Map<String,Integer> map){

        Map<String,String> pageParam = new HashMap<>();
        pageParam.put("pageNo","1");
        pageParam.put("pageSize","50");
        pageParam.put("pageable","true");
        Page page = PageUtils.getPageFromMap(pageParam);
        PageAndOrderbyParamDto pageAndOrderbyParamDto = new PageAndOrderbyParamDto(page);

        PageResultDto<BaseUserPo> pageResultDto = apiBaseUserPoService.selectAllAndAreaNotNull(pageAndOrderbyParamDto);
        while (pageResultDto !=null && pageResultDto.getData() != null && !pageResultDto.getData().isEmpty()){
            List<BaseUserPo> userPos = pageResultDto.getData();
            String areaId = null;
            for (BaseUserPo userPo : userPos) {
                areaId = userPo.getDataAreaId();
                if (StringUtils.isNotEmpty(areaId)) {
                    putIncreaseMapNum(areaId,allNumKey,map);
                    if (DictEnum.Gender.female.name().equals(userPo.getGender())) {
                        putIncreaseMapNum(areaId,femaleNumKey,map);
                    } else if (DictEnum.Gender.male.name().equals(userPo.getGender())) {
                        putIncreaseMapNum(areaId,maleNumKey,map);
                    }
                }
            }
            pageAndOrderbyParamDto.getPage().setPageNo(pageAndOrderbyParamDto.getPage().getPageNo() + 1);
            pageResultDto = apiBaseUserPoService.selectAllSimple(false,pageAndOrderbyParamDto);
        }
    }

    private void putIncreaseMapNum(String areaId, String suffix, Map<String,Integer> map){
        Integer num = map.get(areaId + suffix);
        if (num == null) {
            num = 0;
        }
        // 在线男数
        map.put(areaId + suffix, num + 1);
    }
    /**
     * 在线区域用户数量
     * @param map
     */
    private void onlineNum(Map<String,Integer> map) {

        Collection<Session> sessions =  shiroJedisSessionDAO.getActiveSessions();
        if (sessions != null) {
            ShiroUser su = null;
            String areaId = null;
            for (Session session : sessions) {
                su = ShiroUtils.getShiroUser(session);
                if (su == null) {
                    continue;
                }
                BaseAreaDto areaDto = (BaseAreaDto) su.getArea();
                if (areaDto != null) {
                    areaId = areaDto.getId();
                }
                if (StringUtils.isNotEmpty(areaId)){
                    putIncreaseMapNum(areaId,onlineAllNumKey,map);
                    if(DictEnum.Gender.female.name().equals(su.getGender())){
                        putIncreaseMapNum(areaId,onlineFemaleNumKey,map);
                    } else if(DictEnum.Gender.male.name().equals(su.getGender())){
                        putIncreaseMapNum(areaId,onlineMaleNumKey,map);
                    }
                }
            }
        }
    }
    private Integer getNum(String areaId,String suffix,Map<String,Integer> map){
        Integer num = map.get(areaId + suffix);
        if (num == null) {
            num = 0;
        }
        return num;
    }
    private Map<String,Integer> getAreaAndChildrenAllCountNum(BaseAreaPo parent,Map<String,Integer> map){
        Map<String,Integer> r = new HashMap<>();
        List<BaseAreaPo> childerAll = apiBaseAreaPoService.getChildrenAll(parent.getId());
        int allNum = 0;
        int maleNum = 0;
        int femaleNum = 0;
        int genderOtherNum = 0;
        int onlineAllNum = 0;
        int onlineMaleNum = 0;
        int onlineFemaleNum = 0;
        int onlineGenderOtherNum = 0;
        List<BaseAreaPo> all = new ArrayList<>();
        all.add(parent);
        if (childerAll != null) {
            all.addAll(childerAll);
        }
        for (BaseAreaPo baseAreaPo : all) {
            allNum += getNum(baseAreaPo.getId(),allNumKey,map);
            maleNum += getNum(baseAreaPo.getId(),maleNumKey,map);
            femaleNum += getNum(baseAreaPo.getId(),femaleNumKey,map);
            onlineAllNum += getNum(baseAreaPo.getId(),onlineAllNumKey,map);
            onlineMaleNum += getNum(baseAreaPo.getId(),onlineMaleNumKey,map);
            onlineFemaleNum += getNum(baseAreaPo.getId(),onlineFemaleNumKey,map);
        }
        genderOtherNum = allNum - femaleNum - maleNum;
        onlineGenderOtherNum = onlineAllNum - onlineMaleNum - onlineFemaleNum;
        r.put("allNum",allNum);
        r.put("maleNum",maleNum);
        r.put("femaleNum",femaleNum);
        r.put("genderOtherNum",genderOtherNum);
        r.put("onlineAllNum",onlineAllNum);
        r.put("onlineMaleNum",onlineMaleNum);
        r.put("onlineFemaleNum",onlineFemaleNum);
        r.put("onlineGenderOtherNum",onlineGenderOtherNum);
        return r;
    }
}
