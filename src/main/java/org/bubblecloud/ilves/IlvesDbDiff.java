package org.bubblecloud.ilves;

import org.apache.log4j.xml.DOMConfigurator;
import org.bubblecloud.ilves.util.PersistenceUtil;
import org.bubblecloud.ilves.util.PropertiesUtil;

/**
 * Created by tlaukkan on 12/13/2014.
 */
public class IlvesDbDiff {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        // Configure logging.
        DOMConfigurator.configure("log4j.xml");

        PropertiesUtil.setCategoryRedirection("site", IlvesMain.PROPERTIES_FILE_PREFIX);
        final String diff = PersistenceUtil.diff(IlvesMain.PERSISTENCE_UNIT, IlvesMain.PROPERTIES_FILE_PREFIX);
        System.out.println(diff);
    }
}
