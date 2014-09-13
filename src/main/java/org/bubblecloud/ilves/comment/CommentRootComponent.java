package org.bubblecloud.ilves.comment;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

/**
 * Example comment root component implementation.
 */
public class CommentRootComponent extends CustomComponent {

    /**
     * Default constructor which creates comment main layout
     * with list comments and add comment panels.
     */
    public CommentRootComponent() {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        setCompositionRoot(mainLayout);

        final CommentListComponent commentListComponent = new CommentListComponent();

        final CommentAddComponent commentAddComponent = new CommentAddComponent();
        commentAddComponent.setCommentListComponent(commentListComponent);

        mainLayout.addComponent(commentListComponent);
        mainLayout.addComponent(commentAddComponent);
    }
}
