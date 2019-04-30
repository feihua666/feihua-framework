package com.feihua.framework.base.test;


import com.feihua.framework.base.modules.area.api.ApiBaseAreaPoService;
import com.feihua.framework.base.modules.area.dto.BaseAreaDto;
import com.feihua.framework.base.modules.area.po.BaseAreaPo;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.json.JSONUtils;
import feihua.jdbc.api.utils.OrderbyUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feihua on 2016/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/test/resources/applicationContext.xml"
})
/**
 * 根据国家统计局页面http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201608/t20160809_1386477.html获取
 */
public class ImportAreaTest {
    @Autowired
    ApiBaseAreaPoService sysAreaService;
    @Test
    public void testEmpty() throws Exception {
        List<BaseAreaDto> list = sysAreaService.selectAll(true);
        List<String> updateSql = new ArrayList<>(list.size());
        for (BaseAreaDto areaDto : list) {

            String name = getName(areaDto);
            System.out.println(name);
            name = URLEncoder.encode(name,"GBK");
            System.out.println(name);
            String r = HttpClientUtils.httpGet("http://api.map.baidu.com/geocoder/v2/?address=" + name +"&output=json&ak=69a5a1e7260cd2cd298c33666e436530");

            Map mapr = JSONUtils.json2map(r);
            String lng = "";
            String lat = "";
            Map location = ((Map) ((Map) mapr.get("result")).get("location"));
            lng = location.get("lng").toString();
            lat = location.get("lat").toString();
            updateSql.add("update base_area set longitude='"+ lng +"',latitude='"+ lat +"' where id='" + areaDto.getId() +"' and name='" + areaDto.getName() +"';");
        }
        for (String s : updateSql) {
            System.out.println(s);
        }
    }
    private String getName(BaseAreaDto areaDto){
        Map order = new HashMap();
        order.put("orderby","level-asc");
        order.put("orderable","true");
        List<BaseAreaPo> p = sysAreaService.getParents(areaDto.getId(),OrderbyUtils.getOrderbyFromMap(order));
        String r = "";
        if (p != null) {
            for (BaseAreaPo baseAreaPo : p) {
                r += baseAreaPo.getName() + " ";
            }
        }
        r += areaDto.getName();
        return r;
    }
    //@Test
    public void test() throws ParserException {
        //String url = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201608/t20160809_1386477.html";
        String url = "C:\\Users\\Lenovo\\Desktop\\sfsdsdfsdf.html";
        Parser parser = new Parser(url);
        parser.setEncoding("utf-8");
        Html htmlNode = null;
       /* for (NodeIterator i = parser.elements (); i.hasMoreNodes(); ) {
            Node node = i.nextNode();
            if(node instanceof Html){
                htmlNode = (Html) node;
            }
        }*/
        List<String> result = new ArrayList<>();
        CssSelectorNodeFilter paraFilter =new CssSelectorNodeFilter(".center .xilan_con .TRS_PreAppend .MsoNormal span");
        Span span = new Span();
        span.setAttribute("style","font-family: 宋体");
        paraFilter.accept(span);
        NodeList nodeList = parser.extractAllNodesThatMatch(paraFilter);
        // NodeList nodeList = parser.extractAllNodesThatMatch(null);
        for (int i = 0; i < nodeList.size(); i++) {
            Node node =  nodeList.elementAt(i);
            if(!StringUtils.isEmpty(((Span)node).getAttribute("style"))){
                result.add(node.toPlainTextString());
            }
        }
        add(result,"fcdab3b9fab611e794174439c4325934");
        System.out.println();
    }
    //间隔，根据间隔判断级别
    String space = "　";
    //入口是添加省，直辖市
    private void add(List<String> result,String parentId) {
        BaseAreaPo provinceParent = null;
        BaseAreaPo cityParent = null;
        String parentType = "";
        for (int i = 0; i < result.size(); i++) {
            String name = StringUtils.strip(result.get(i));
            String space = result.get(i).replace(name,"");

            if(space.equals(this.space)){
                BaseAreaPo baseAreaPo = new BaseAreaPo();
                baseAreaPo.setName(name);
                BaseAreaPo entity = sysAreaService.selectOneSimple(baseAreaPo);
                //省或直辖市
                if(entity == null){
                    String type = null;
                    if(i+1<=result.size()-1&&StringUtils.strip(result.get(i+1)).equals("市辖区")){
                        type = ("municipality");
                        provinceParent = insert(name,parentId,"province");
                        cityParent = insert(name,provinceParent.getId(),"city");
                        parentType = cityParent.getType();
                    }else {
                        type = ("province");
                        provinceParent = insert(name,parentId,type);
                        parentType = provinceParent.getType();
                    }
                }else {
                    provinceParent = entity;
                    parentType = provinceParent.getType();
                }

            }else if(space.length()/this.space.length() == 2){
                //市，这里面又分地市或直辖市的区
                if(name.equals("市辖区") || name.equals("县")){
                    //直辖市的区，其中市辖区和县不要
                }else {
                    BaseAreaPo baseAreaPo = new BaseAreaPo();
                    baseAreaPo.setName(name);
                    BaseAreaPo entity = sysAreaService.selectOneSimple(baseAreaPo);
                    //地市
                    if(entity == null){
                        cityParent = insert(name,provinceParent.getId(),"city");
                    }else {
                        cityParent = entity;
                    }
                    parentType = cityParent.getType();
                }

            }else if(space.length()/this.space.length() == 3){
                BaseAreaPo baseAreaPo = new BaseAreaPo();
                baseAreaPo.setName(name);
                BaseAreaPo entity = sysAreaService.selectOneSimple(baseAreaPo);
                //区、县
                if(entity == null){
                    String pId = null;
                    if(parentType.equals("province")){
                        pId = provinceParent.getId();
                    }else if(parentType.equals("city")){
                        pId = cityParent.getId();
                    }
                    insert(name,pId,"district");
                }
            }
        }
    }

    private BaseAreaPo insert(String name,String parentId,String type){
        BaseAreaPo newentity = new BaseAreaPo();
        newentity.setSequence(10);
        newentity.setParentId(parentId);
        newentity.setName(name);
        newentity.setType(type);
        newentity = sysAreaService.preInsert(newentity,"0");
        sysAreaService.insert(newentity);
        return newentity;
    }
}
