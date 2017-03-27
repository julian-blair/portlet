package se.consid.ninjaportlet;

import senselogic.sitevision.api.Utils;
import senselogic.sitevision.api.security.PermissionUtil;

import javax.portlet.*;
import java.io.IOException;


public abstract class NinjaPortletDispatcher extends GenericPortlet{

    protected static final String SITEVISION_CONFIG_PORTLET_MODE = "config";

    protected abstract void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException;

    protected abstract void doConfig(RenderRequest request, RenderResponse response) throws PortletException, IOException;

    protected void doDispatch(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        if(SITEVISION_CONFIG_PORTLET_MODE.equals(request.getPortletMode().toString())) {
            doConfig(request, response);
        } else {

            super.doDispatch(request, response);
        }
    }

    protected final boolean hasWritePermission(PortletRequest request) {

        Utils siteVisionUtils = (Utils) request.getAttribute("sitevision.utils");
        PermissionUtil permissionUtil = siteVisionUtils.getPermissionUtil();
        return permissionUtil.hasWritePermission();
    }
}
