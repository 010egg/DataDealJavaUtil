// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

import java.util.Vector;
import java.util.Iterator;
import ch.qos.logback.core.joran.event.EndEvent;
import ch.qos.logback.core.joran.event.BodyEvent;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.event.StartEvent;
import java.util.Map;
import ch.qos.logback.core.Context;
import java.util.Stack;
import org.xml.sax.Locator;
import ch.qos.logback.core.joran.action.ImplicitAction;
import java.util.ArrayList;
import ch.qos.logback.core.joran.action.Action;
import java.util.List;

public class Interpreter
{
    private static List<Action> EMPTY_LIST;
    private final RuleStore ruleStore;
    private final InterpretationContext interpretationContext;
    private final ArrayList<ImplicitAction> implicitActions;
    private final CAI_WithLocatorSupport cai;
    private ElementPath elementPath;
    Locator locator;
    EventPlayer eventPlayer;
    Stack<List<Action>> actionListStack;
    ElementPath skip;
    
    public Interpreter(final Context context, final RuleStore rs, final ElementPath initialElementPath) {
        this.skip = null;
        this.cai = new CAI_WithLocatorSupport(context, this);
        this.ruleStore = rs;
        this.interpretationContext = new InterpretationContext(context, this);
        this.implicitActions = new ArrayList<ImplicitAction>(3);
        this.elementPath = initialElementPath;
        this.actionListStack = new Stack<List<Action>>();
        this.eventPlayer = new EventPlayer(this);
    }
    
    public EventPlayer getEventPlayer() {
        return this.eventPlayer;
    }
    
    public void setInterpretationContextPropertiesMap(final Map<String, String> propertiesMap) {
        this.interpretationContext.setPropertiesMap(propertiesMap);
    }
    
    @Deprecated
    public InterpretationContext getExecutionContext() {
        return this.getInterpretationContext();
    }
    
    public InterpretationContext getInterpretationContext() {
        return this.interpretationContext;
    }
    
    public void startDocument() {
    }
    
    public void startElement(final StartEvent se) {
        this.setDocumentLocator(se.getLocator());
        this.startElement(se.namespaceURI, se.localName, se.qName, se.attributes);
    }
    
    private void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts) {
        final String tagName = this.getTagName(localName, qName);
        this.elementPath.push(tagName);
        if (this.skip != null) {
            this.pushEmptyActionList();
            return;
        }
        final List<Action> applicableActionList = this.getApplicableActionList(this.elementPath, atts);
        if (applicableActionList != null) {
            this.actionListStack.add(applicableActionList);
            this.callBeginAction(applicableActionList, tagName, atts);
        }
        else {
            this.pushEmptyActionList();
            final String errMsg = "no applicable action for [" + tagName + "], current ElementPath  is [" + this.elementPath + "]";
            this.cai.addError(errMsg);
        }
    }
    
    private void pushEmptyActionList() {
        this.actionListStack.add(Interpreter.EMPTY_LIST);
    }
    
    public void characters(final BodyEvent be) {
        this.setDocumentLocator(be.locator);
        String body = be.getText();
        final List<Action> applicableActionList = this.actionListStack.peek();
        if (body != null) {
            body = body.trim();
            if (body.length() > 0) {
                this.callBodyAction(applicableActionList, body);
            }
        }
    }
    
    public void endElement(final EndEvent endEvent) {
        this.setDocumentLocator(endEvent.locator);
        this.endElement(endEvent.namespaceURI, endEvent.localName, endEvent.qName);
    }
    
    private void endElement(final String namespaceURI, final String localName, final String qName) {
        final List<Action> applicableActionList = this.actionListStack.pop();
        if (this.skip != null) {
            if (this.skip.equals(this.elementPath)) {
                this.skip = null;
            }
        }
        else if (applicableActionList != Interpreter.EMPTY_LIST) {
            this.callEndAction(applicableActionList, this.getTagName(localName, qName));
        }
        this.elementPath.pop();
    }
    
    public Locator getLocator() {
        return this.locator;
    }
    
    public void setDocumentLocator(final Locator l) {
        this.locator = l;
    }
    
    String getTagName(final String localName, final String qName) {
        String tagName = localName;
        if (tagName == null || tagName.length() < 1) {
            tagName = qName;
        }
        return tagName;
    }
    
    public void addImplicitAction(final ImplicitAction ia) {
        this.implicitActions.add(ia);
    }
    
    List<Action> lookupImplicitAction(final ElementPath elementPath, final Attributes attributes, final InterpretationContext ec) {
        for (int len = this.implicitActions.size(), i = 0; i < len; ++i) {
            final ImplicitAction ia = this.implicitActions.get(i);
            if (ia.isApplicable(elementPath, attributes, ec)) {
                final List<Action> actionList = new ArrayList<Action>(1);
                actionList.add(ia);
                return actionList;
            }
        }
        return null;
    }
    
    List<Action> getApplicableActionList(final ElementPath elementPath, final Attributes attributes) {
        List<Action> applicableActionList = this.ruleStore.matchActions(elementPath);
        if (applicableActionList == null) {
            applicableActionList = this.lookupImplicitAction(elementPath, attributes, this.interpretationContext);
        }
        return applicableActionList;
    }
    
    void callBeginAction(final List<Action> applicableActionList, final String tagName, final Attributes atts) {
        if (applicableActionList == null) {
            return;
        }
        for (final Action action : applicableActionList) {
            try {
                action.begin(this.interpretationContext, tagName, atts);
            }
            catch (ActionException e) {
                this.skip = this.elementPath.duplicate();
                this.cai.addError("ActionException in Action for tag [" + tagName + "]", e);
            }
            catch (RuntimeException e2) {
                this.skip = this.elementPath.duplicate();
                this.cai.addError("RuntimeException in Action for tag [" + tagName + "]", e2);
            }
        }
    }
    
    private void callBodyAction(final List<Action> applicableActionList, final String body) {
        if (applicableActionList == null) {
            return;
        }
        for (final Action action : applicableActionList) {
            try {
                action.body(this.interpretationContext, body);
            }
            catch (ActionException ae) {
                this.cai.addError("Exception in end() methd for action [" + action + "]", ae);
            }
        }
    }
    
    private void callEndAction(final List<Action> applicableActionList, final String tagName) {
        if (applicableActionList == null) {
            return;
        }
        for (final Action action : applicableActionList) {
            try {
                action.end(this.interpretationContext, tagName);
            }
            catch (ActionException ae) {
                this.cai.addError("ActionException in Action for tag [" + tagName + "]", ae);
            }
            catch (RuntimeException e) {
                this.cai.addError("RuntimeException in Action for tag [" + tagName + "]", e);
            }
        }
    }
    
    public RuleStore getRuleStore() {
        return this.ruleStore;
    }
    
    static {
        Interpreter.EMPTY_LIST = new Vector<Action>(0);
    }
}
