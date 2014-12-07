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
package org.bubblecloud.ilves;

import org.apache.log4j.xml.DOMConfigurator;
import org.bubblecloud.ilves.comment.CommentingComponent;
import org.eclipse.jetty.server.Server;
import org.vaadin.addons.sitekit.jetty.DefaultJettyConfiguration;
import org.vaadin.addons.sitekit.site.*;
import org.vaadin.addons.sitekit.site.view.DefaultValoView;

/**
 * Ilves seed project main class.
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
        // Configure logging.
        DOMConfigurator.configure("log4j.xml");

        // The default Jetty server configuration.
        final Server server = DefaultJettyConfiguration.configureServer(PERSISTENCE_UNIT, LOCALIZATION_BUNDLE);

        // Get default site descriptor.
        final SiteDescriptor siteDescriptor = DefaultSiteUI.getContentProvider().getSiteDescriptor();

        // Custom view name.
        final String customViewName = "custom";

        // Describe custom view.
        final ViewDescriptor commentView = new ViewDescriptor(customViewName, DefaultValoView.class);
        siteDescriptor.getViewDescriptors().add(commentView);

        // Place example Vaadin component to content slot in the view.
        commentView.setComponentClass(Slot.CONTENT, WelcomeComponent.class);
        // Place example Vaadin component to footer slot in the view.
        commentView.setComponentClass(Slot.FOOTER, CommentingComponent.class);

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