package se.consid.ninjaportlet;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import senselogic.sitevision.api.Utils;
import senselogic.sitevision.api.render.velocity.VelocityContext;
import senselogic.sitevision.api.render.velocity.VelocityException;
import senselogic.sitevision.api.render.velocity.VelocityRenderer;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NinjaPortlet extends NinjaPortletDispatcher {

    private final String mode_config = "config";
    private final String mode_view = "view";
    private final String CONFIG_HEADER = "CONFIG HEADER";
    private final String VIEW_HEADER = "VIEW HEADER";

    private String template;
    private String subTemplate;
    private ArrayList<Item> items;

    @Override
    public void init(PortletConfig portletConfig) throws PortletException {
        super.init(portletConfig);

        this.items = new ArrayList();
        this.items.add(new Item("Strawberry Jam", 0, "A", 15.00));
        this.items.add(new Item("Blackberry Jam", 1, "A", 12.00));
        this.items.add(new Item("Vanilla Custard", 2, "B", 17.00));
        this.items.add(new Item("Luxury Double Cream", 3, "B", 10.00));
        this.items.add(new Item("Yoghurt Mayonnaise", 4, "B", 8.00));
        this.items.add(new Item("Holy Apple Tart", 5, "C", 25.00));
        this.items.add(new Item("Sugar", 6, "D", 4));

        template =
            "<h1> $header </h1>"                                                        +
            "<table>"                                                                       +
                "<tr>"                                                                      +
                    "<th>Name</th>"                                                         +
                    "<th>Price</th>"                                                        +
                "</tr>"                                                                     +
                "#foreach ($item in $items)"                                                +
                    "<tr>"                                                                  +
                        "<td>$item.getName()<td>"                                           +
                        "<td>$item.getPrice()<td>"                                          +
                    "</tr>"                                                                 +
                "#end"                                                                      +
            "</table>";

        subTemplate =
                "#if ($items)"                                      +
                    "'items' ref exists with size: $items.size() "  +
                "#else"                                             +
                    "the 'items' ref is missing from the context"   +
                "#end";
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response) {
        Utils utils = (Utils) request.getAttribute("sitevision.utils");

        try{
            renderUsingTemplateString(request, response, VIEW_HEADER);

        } catch(Exception ex){
            utils.getLogUtil().debug("Exception occurred while writing to VIEW", ex);
        }
    }

    @Override
    protected void doConfig(RenderRequest request, RenderResponse response) throws PortletException {

        Utils utils = (Utils) request.getAttribute("sitevision.utils");

        try {

            renderUsingTemplateString(request, response, CONFIG_HEADER);

        } catch(Exception ex) {

            utils.getLogUtil().debug("Exception occurred while writing", ex);
        }
    }

    private void renderUsingTemplateString(RenderRequest request, RenderResponse response, String header) throws Exception {

        Utils utils = (Utils) request.getAttribute("sitevision.utils");
        try {

            PrintWriter writer = response.getWriter();
            VelocityRenderer renderer = utils.getVelocityRenderer();
            VelocityContext context = renderer.getVelocityContext(writer);

            if(hasWritePermission(request)) {

                context.put("items", this.items);
                context.put("header", header);
                context.put("evaluateThis", subTemplate);

                try {

                    renderer.render(context, template);
                } catch(VelocityException ex) {

                    utils.getLogUtil().debug("Velocity Exception occurred while writing", ex);
                }
            } else {
                renderer.render(context,
                        "<h3><strong>You do not have permission to change config preferences</strong></h3>"
                );
            }
        } catch(IOException ex) {

            utils.getLogUtil().debug("IO Exception occurred while writing", ex);
        }
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        response.setPortletMode(PortletMode.VIEW);
    }
}
