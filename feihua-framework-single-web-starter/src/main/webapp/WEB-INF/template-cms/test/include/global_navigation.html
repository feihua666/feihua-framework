<!-- NAVBAR
================================================== -->
<div class="navbar-wrapper">
    <div class="container">

        <nav class="navbar navbar-inverse navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">${site.name}</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li class="[#if !channel??]active[/#if]"><a href="${site.indexUrl}">首页</a></li>
                        [#-- 一级栏目 --]
                        [@cms_channel_list siteId=site.id channelType="channel";item,index]
                        [#if item??]
                            [#-- 二级栏目 --]
                            [@cms_channel_list siteId=site.id parentId=item.id channelType="subChannel" iteratorType="var"]
                                [#if channelList??]
                        <li class="dropdown [#if channel?? && channel.id == item.id]active[/#if] [#if channel?? && channel.parentId == item.id]active[/#if]">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${item.name}<span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                [#list channelList as cl]
                                <li><a href="${site.sitePath + cl.channelUrl}">${cl.name}</a></li>
                                [/#list]
                            </ul>
                        </li>
                                [#else]
                        <li class="[#if channel?? && channel.id == item.id]active[/#if]">
                            <a href="${site.sitePath}${item.channelUrl}">${item.name}</a>
                        </li>
                                [/#if]
                            [/@]
                        [/#if]
                        [/@]
                    </ul>
                </div>
            </div>
        </nav>

    </div>
</div>