/**
 * Copyright 2013 Tommi S.E. Laukkanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bubblecloud.ilves;

import org.bubblecloud.ilves.comment.CommentRootComponent;
import org.eclipse.jetty.server.Server;
import org.vaadin.addons.sitekit.jetty.DefaultJettyConfiguration;
import org.vaadin.addons.sitekit.site.*;

/**
 * Seed main class.
 *
 * @author Tommi S.E. Laukkanen
 */
public class Ilves {
    /** The persistence unit to be used. */
    public static final String PERSISTENCE_UNIT = "custom";
    /** The localization bundle. */
    public static final String LOCALIZATION_BUNDLE = "custom-localization";

    /**
     * Main method for tutorial site.
     *
     * @param args the commandline arguments
     * @throws Exception if exception occurs in jetty startup.
     */
    public static void main(final String[] args) throws Exception {
        // The default Jetty server configuration.
        final Server server = DefaultJettyConfiguration.configureServer(PERSISTENCE_UNIT, LOCALIZATION_BUNDLE);

        // Get default site descriptor.
        final SiteDescriptor siteDescriptor = DefaultSiteUI.getContentProvider().getSiteDescriptor();

        // Custom view name.
        final String customViewName = "custom";

        // Describe custom view.
        final ViewDescriptor commentView = new ViewDescriptor(customViewName, CustomView.class);
        siteDescriptor.getViewDescriptors().add(commentView);
        // Place example viewlet to content slot in the view.
        commentView.setViewletClass(Slot.CONTENT, CustomViewlet.class);
        // Place example Vaadin component to footer slot in the view.
        commentView.setComponentClass(Slot.FOOTER, CommentRootComponent.class);

        // Add custom view to navigation.
        final NavigationVersion navigationVersion = siteDescriptor.getNavigationVersion();
        navigationVersion.setDefaultPageName(customViewName);
        navigationVersion.addRootPage(0, customViewName);

        // Start server.
        server.start();

        // Join this main thread to the Jetty server thread.
        server.join();
    }
}