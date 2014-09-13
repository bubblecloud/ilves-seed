package org.bubblecloud.ilves.comment;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addons.sitekit.model.Company;
import org.vaadin.addons.sitekit.model.User;
import org.vaadin.addons.sitekit.site.DefaultSiteUI;
import org.vaadin.addons.sitekit.site.Site;

import javax.persistence.EntityManager;

/**
 * Example comment component implementation.
 */
public class CommentAddComponent extends CustomComponent {

    private CommentListComponent commentListComponent;

    /**
     * The default constructor which instantiates Vaadin
     * component hierarchy.
     */
    public CommentAddComponent() {

        final User user = DefaultSiteUI.getSecurityProvider().getUserFromSession();

        final String contextPath = VaadinService.getCurrentRequest().getContextPath();
        final Site site = Site.getCurrent();

        final Company company = site.getSiteContext().getObject(Company.class);
        final EntityManager entityManager = site.getSiteContext().getObject(EntityManager.class);

        final Panel panel = new Panel(site.localize("panel-add-comment"));
        setCompositionRoot(panel);

        final VerticalLayout mainLayout = new VerticalLayout();
        panel.setContent(mainLayout);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        final TextArea commentMessageField = new TextArea(site.localize("field-comment-message"));
        mainLayout.addComponent(commentMessageField);
        commentMessageField.setWidth(100, Unit.PERCENTAGE);
        commentMessageField.setRows(3);
        commentMessageField.setMaxLength(255);

        final Button addCommentButton = new Button(site.localize("button-add-comment"));
        mainLayout.addComponent(addCommentButton);
        if (user == null) {
            commentMessageField.setEnabled(false);
            commentMessageField.setInputPrompt(site.localize("message-please-login-to-comment"));
            addCommentButton.setEnabled(false);
        }

        addCommentButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final String commentMessage = commentMessageField.getValue();
                if (StringUtils.isEmpty(commentMessage)) {
                    return;
                }
                final Comment comment = new Comment(company, user, contextPath, commentMessage);
                entityManager.getTransaction().begin();
                try {
                    entityManager.persist(comment);
                    entityManager.getTransaction().commit();
                    commentMessageField.setValue("");
                    if (commentListComponent != null) {
                        commentListComponent.refresh();
                    }
                } finally {
                    if (entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                }
            }
        });

    }

    /**
     * @return the comment list component
     */
    public CommentListComponent getCommentListComponent() {
        return commentListComponent;
    }

    /**
     * @param commentListComponent the comment list component to set
     */
    public void setCommentListComponent(final CommentListComponent commentListComponent) {
        this.commentListComponent = commentListComponent;
    }
}
