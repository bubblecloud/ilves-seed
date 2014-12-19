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
import com.vaadin.ui.themes.ValoTheme;
import org.bubblecloud.ilves.model.Company;
import org.bubblecloud.ilves.site.Site;
import org.bubblecloud.ilves.util.GravatarUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Example comment list component implementation.
 */
public class CommentListComponent extends CustomComponent {

    public CommentListComponent() {
    }

    @Override
    public void attach() {
        super.attach();

        refresh();
    }

    public void refresh() {

        final String contextPath = VaadinService.getCurrentRequest().getContextPath();
        final Site site = Site.getCurrent();

        final Company company = site.getSiteContext().getObject(Company.class);
        final EntityManager entityManager = site.getSiteContext().getObject(EntityManager.class);

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Comment> commentCriteriaQuery = builder.createQuery(Comment.class);
        final Root<Comment> commentRoot = commentCriteriaQuery.from(Comment.class);
        commentCriteriaQuery.where(builder.and(
                builder.equal(commentRoot.get("owner"), company),
                builder.equal(commentRoot.get("dataId"), contextPath)
        ));
        commentCriteriaQuery.orderBy(builder.asc(commentRoot.get("created")));

        final TypedQuery<Comment> commentTypedQuery = entityManager.createQuery(commentCriteriaQuery);
        final List<Comment> commentList = commentTypedQuery.getResultList();

        final Panel panel = new Panel(site.localize("panel-comments"));
        setCompositionRoot(panel);

        final GridLayout gridLayout = new GridLayout(3, commentList.size() + 1);
        panel.setContent(gridLayout);
        gridLayout.setSpacing(true);
        gridLayout.setMargin(true);
        gridLayout.setColumnExpandRatio(0, 0.0f);
        gridLayout.setColumnExpandRatio(1, 0.1f);
        gridLayout.setColumnExpandRatio(2, 0.9f);

        final Label authorHeaderLabel = new Label();
        authorHeaderLabel.setStyleName(ValoTheme.LABEL_BOLD);
        authorHeaderLabel.setValue(site.localize("column-header-author"));
        gridLayout.addComponent(authorHeaderLabel, 0, 0, 1, 0);

        final Label commentHeaderLabel = new Label();
        commentHeaderLabel.setStyleName(ValoTheme.LABEL_BOLD);
        commentHeaderLabel.setValue(site.localize("column-header-comment"));
        gridLayout.addComponent(commentHeaderLabel, 2, 0);

        for (int i = 0; i < commentList.size(); i++) {
            final Comment comment = commentList.get(i);

            final Link authorImageLink = GravatarUtil.getGravatarImageLink(comment.getAuthor().getEmailAddress());
            gridLayout.addComponent(authorImageLink, 0, i + 1);

            final Label authorLabel = new Label();
            final String authorName = comment.getAuthor().getFirstName();
            authorLabel.setValue(authorName);
            gridLayout.addComponent(authorLabel, 1, i + 1);

            final Label messageLabel = new Label();
            messageLabel.setValue(comment.getMessage());
            gridLayout.addComponent(messageLabel, 2, i + 1);
        }

    }
}
