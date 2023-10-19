// 
// Decompiled by Procyon v0.5.36
// 

package javax.xml.transform;

import java.util.Properties;

public interface Templates
{
    Transformer newTransformer() throws TransformerConfigurationException;
    
    Properties getOutputProperties();
}
