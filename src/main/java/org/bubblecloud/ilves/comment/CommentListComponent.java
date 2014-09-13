package org.bubblecloud.ilves.comment;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.sitekit.model.Company;
import org.vaadin.addons.sitekit.site.Site;
import org.vaadin.addons.sitekit.util.GravatarUtil;

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
        gridLayout.setWidth(100, Unit.PERCENTAGE);
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
