package org.yui.logger.plugin;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author huangjinlong
 * @time 2019-10-11 13:47
 * @description
 */
@Plugin(name = "SlowSqlPlugin", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class SlowSqlPlugin extends AbstractFilter {

    /**
     *
     */
    private static final List<String> SLOW_SQL_LIST = Collections.unmodifiableList(Arrays.asList("慢sql"));

    protected SlowSqlPlugin() {
        super();
    }

    protected SlowSqlPlugin(Result onMatch, Result onMismatch) {
        super(onMatch, onMismatch);
    }

    @Override
    public Result filter(LogEvent event) {

        for (String x : SLOW_SQL_LIST) {
            if (event.getMessage().getFormat().startsWith(x)) {
                return this.onMatch;
            }
        }
        return this.onMismatch;
    }

    /**
     *
     * @param match
     * @param mismatch
     * @return
     */
    @PluginFactory
    public static SlowSqlPlugin createFilter(
            @PluginAttribute("onMatch") final Result match,
            @PluginAttribute("onMismatch") final Result mismatch) {
        return new SlowSqlPlugin(match, mismatch);
    }
}
