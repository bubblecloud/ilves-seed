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

import com.vaadin.sass.SassCompiler;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.nio.file.*;

/**
 * This class wraps Vaadin SASS compiler to provide on demand SASS compilation.
 *
 * @author Tommi S.E. Laukkanen
 */
public class IlvessSassCompiler {
    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(IlvessSassCompiler.class);

    public static void main(final String args[]) {
        // Configure logging.
        DOMConfigurator.configure("log4j.xml");

        final String sourceFile;
        final String targetFile;
        final String watchDirectory;

        if (args.length == 3) {
            sourceFile = args[0];
            targetFile = args[1];
            watchDirectory = args[2];
        } else {
            sourceFile = Paths.get("src/main/resources/VAADIN/themes/ilves/styles.scss").toString();
            targetFile = Paths.get("target/classes/VAADIN/themes/ilves/styles.css").toString();
            watchDirectory = Paths.get("src/main/resources/VAADIN/themes/ilves").toString();
        }

        LOGGER.info("SASS compilation source file: " + sourceFile);
        LOGGER.info("SASS compilation target file: " + targetFile);

        final Runnable runnable = new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    final long startTimeMillis = System.currentTimeMillis();
                    SassCompiler.main(new String[]{sourceFile, targetFile});
                    LOGGER.info("SASS compilation done in " + (System.currentTimeMillis() - startTimeMillis) + "ms.");
                } catch (Exception e) {
                    LOGGER.error("Error in SASS compilation: " + e);
                }
            }
        };

        try {
            watch(runnable, ".scss", watchDirectory);
        } catch (Exception e) {
            LOGGER.error("Error starting sass file watcher: " + e);
        }
    }

    /**
     * Create directory watch for
     * @param runnable the runnable to execute on file change
     * @param fileExtension the file extension.
     * @param directories the directories
     * @throws Exception
     */
    public static void watch(final Runnable runnable, final String fileExtension, final String... directories) throws Exception {
        final WatchService watcher = FileSystems.getDefault().newWatchService();
        for (final String directory : directories) {
            Path path = Paths.get(directory);
            path.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            LOGGER.info("SASS source file watch registered for dir: " + path.toString());
        }

        while (true) {
            final WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                final WatchEvent.Kind<?> kind = event.kind();

                @SuppressWarnings("unchecked")
                final WatchEvent<Path> ev = (WatchEvent<Path>) event;
                final Path fileName = ev.context();

                if (kind == StandardWatchEventKinds.ENTRY_MODIFY && fileName.toString().endsWith(fileExtension)) {
                    LOGGER.info("SASS compilation started after source file change...");
                    runnable.run();
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}
