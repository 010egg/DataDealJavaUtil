// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic;

import org.slf4j.LoggerFactory;
import ch.qos.logback.core.status.StatusManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import ch.qos.logback.core.status.ViewStatusMessagesServletBase;

public class ViewStatusMessagesServlet extends ViewStatusMessagesServletBase
{
    private static final long serialVersionUID = 443878494348593337L;
    
    @Override
    protected StatusManager getStatusManager(final HttpServletRequest req, final HttpServletResponse resp) {
        final LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        return lc.getStatusManager();
    }
    
    @Override
    protected String getPageTitle(final HttpServletRequest req, final HttpServletResponse resp) {
        final LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        return "<h2>Status messages for LoggerContext named [" + lc.getName() + "]</h2>\r\n";
    }
}
