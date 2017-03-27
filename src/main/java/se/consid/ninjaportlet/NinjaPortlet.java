package se.consid.ninjaportlet;

import senselogic.sitevision.api.Utils;
import senselogic.sitevision.api.render.velocity.VelocityContext;
import senselogic.sitevision.api.render.velocity.VelocityException;
import senselogic.sitevision.api.render.velocity.VelocityRenderer;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class NinjaPortlet extends NinjaPortletDispatcher {

    private String template;
    private String subTemplate; //used to validate the string?
    private final String mode_config = "edit";
    private final String mode_view = "view";

    public void init() throws PortletException {

        template = "<h1>This is Velocity World BITCHEEEES!!!</h1>";

        subTemplate =
                " #if ($count)                                                                   " +
                        "    count exists and is: $count                                         " +
                        " #else                                                                  " +
                        "    The 'count' reference is missing in the context I'm rendering in... " +
                        " #end                                                                   ";
    }

    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        renderUsingVelocity(request, response, mode_view);
    }

    protected void doConfig(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        renderUsingVelocity(request, response, mode_config);
    }

    private void renderUsingVelocity(RenderRequest request, RenderResponse response, String mode) throws IOException {

        PrintWriter writer = response.getWriter();

        Utils utils = (Utils) request.getAttribute("sitevision.utils");
        VelocityRenderer renderer = utils.getVelocityRenderer();
        VelocityContext context = renderer.getVelocityContext(request, writer);
        context.put("evaluateThis", subTemplate);

        try {
            renderer.render(context, template + mode.toString());
            writer.println();

        } catch (VelocityException ex) {
            utils.getLogUtil().debug("Exception occured while rendering 'Hello Portlet'", ex);
        }
    }

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        if(hasWritePermission(request)) {
            response.setPortletMode(PortletMode.EDIT);
        } else {
            response.setPortletMode(PortletMode.VIEW);
        }
    }

    public void destroy() {
        template = null;
        subTemplate = null;
        super.destroy();
    }
}
