package com.feihua.framework.activity.rest.mvc;

import org.activiti.rest.editor.model.ModelSaveRestResource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yangwei
 * Created at 2018/4/10 17:24
 */
@RestController
public class ActivityModelSaveRestResource extends ModelSaveRestResource {
    @RequiresPermissions("activity:model:save")
    @RequestMapping(value = {"/activity/model/{modelId}/save"},method = {RequestMethod.PUT})
    @ResponseStatus(HttpStatus.OK)
    public void saveModel(@PathVariable String modelId, String name, String description, String json_xml,String svg_xml) {
        MultiValueMap<String,String> values = new LinkedMultiValueMap<>();
        values.set("name",name);
        values.set("description",description);
        values.set("json_xml",json_xml);
        values.set("svg_xml",svg_xml);
        super.saveModel(modelId, values);
    }
}
