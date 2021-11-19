package com.photos;

import com.perfree.commons.Constants;
import com.perfree.permission.AdminGroup;
import com.perfree.permission.AdminGroups;
import com.perfree.plugin.BasePlugin;
import org.pf4j.PluginWrapper;

/**
 * @description 插件主类
 * @author Perfree
 * @date 2021/11/15 9:15
 */
@AdminGroups(groups = {
        @AdminGroup(name = "内容管理", groupId = Constants.ADMIN_MENU_GROUP_CONTENT)
})
public class PluginMain extends BasePlugin {
    public PluginMain(PluginWrapper wrapper) {
        super(wrapper);
    }
}
