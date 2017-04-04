package se.consid.ninjaportlet;

import senselogic.sitevision.api.Utils;
import senselogic.sitevision.api.security.PermissionUtil;
import javax.portlet.*;


public abstract class NinjaPortletDispatcher extends GenericPortlet{

    protected static final String SITEVISION_CONFIG_PORTLET_MODE = "config";

    protected abstract void doView(RenderRequest request, RenderResponse response);

    protected abstract void doConfig(RenderRequest request, RenderResponse response) throws PortletException;

    protected void doDispatch(RenderRequest request, RenderResponse response) throws PortletException{

        Utils utils = (Utils) request.getAttribute("sitevision.utils");

        if(SITEVISION_CONFIG_PORTLET_MODE.equals(request.getPortletMode().toString())) {

            doConfig(request, response);
        } else {
            try{
                super.doDispatch(request, response);
            } catch (Exception ex){
                utils.getLogUtil().debug("Exception occurred calling super.Dispatch()", ex);
            }
        }
    }

    protected final boolean hasWritePermission(PortletRequest request) {

        Utils siteVisionUtils = (Utils) request.getAttribute("sitevision.utils");
        PermissionUtil permissionUtil = siteVisionUtils.getPermissionUtil();
        return permissionUtil.hasWritePermission();
    }
}
