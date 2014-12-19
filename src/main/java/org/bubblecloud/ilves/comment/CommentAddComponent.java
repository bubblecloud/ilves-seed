/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
package org.bubblecloud.ilves.comment;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.bubblecloud.ilves.model.Company;
import org.bubblecloud.ilves.model.User;
import org.bubblecloud.ilves.site.DefaultSiteUI;
import org.bubblecloud.ilves.site.Site;

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
