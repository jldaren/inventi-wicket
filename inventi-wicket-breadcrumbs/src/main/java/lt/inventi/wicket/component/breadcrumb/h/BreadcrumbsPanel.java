package lt.inventi.wicket.component.breadcrumb.h;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;


public class BreadcrumbsPanel extends GenericPanel<List<Breadcrumb>> {

    private final ListView<Breadcrumb> breadcrumbs;

    public BreadcrumbsPanel(String id) {
        super(id);

        add(breadcrumbs = new ListView<Breadcrumb>("crumbs", new LoadableDetachableModel<List<Breadcrumb>>() {
            @Override
            public List<Breadcrumb> load() {
                return getBreadcrumbs();
            }
        }) {
            @Override
            protected void populateItem(ListItem<Breadcrumb> item) {
                BreadcrumbsPanel.this.populateBreadcrumb(item);
            }
        });
    }

    // PRIVATE API
    List<Breadcrumb> getBreadcrumbs() {
        return BreadcrumbTrailHistory.getTrail(Breadcrumb.constructIdFrom(getPage()));
    }

    protected void populateBreadcrumb(ListItem<Breadcrumb> item) {
        Component title = new Label("title", item.getModelObject().getTitleModel());
        AbstractLink link = customize(createBreadcrumbLink("link", item));
        if (shouldBeDisabled(item.getIndex(), item.getModel())) {
            markAsDisabled(link);
        }
        link.add(title);
        item.add(link);
    }

    protected AbstractLink customize(AbstractLink link) {
        return link.setBeforeDisabledLink("").setAfterDisabledLink("");
    }

    protected AbstractLink createBreadcrumbLink(String linkId, ListItem<Breadcrumb> item) {
        return new BreadcrumbLink(linkId, item.getModel());
    }

    protected void markAsDisabled(WebMarkupContainer link) {
        link.setEnabled(false);
        link.add(new AttributeAppender("class", " inactive"));
    }

    protected boolean shouldBeDisabled(int index, IModel<Breadcrumb> crumb) {
        return index == getNumberOfBreadcrumbs() - 1;
    }

    protected final int getNumberOfBreadcrumbs() {
        return breadcrumbs.getViewSize();
    }

}