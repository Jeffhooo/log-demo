package jeff.demo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class MainFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        return (event.getMessage().contains("main"))? FilterReply.ACCEPT : FilterReply.DENY;
    }
}
