// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.entity.mime;

import java.io.IOException;
import java.util.Iterator;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

class HttpStrictMultipart extends AbstractMultipartForm
{
    private final List<FormBodyPart> parts;
    
    public HttpStrictMultipart(final String subType, final Charset charset, final String boundary, final List<FormBodyPart> parts) {
        super(subType, charset, boundary);
        this.parts = parts;
    }
    
    @Override
    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }
    
    @Override
    protected void formatMultipartHeader(final FormBodyPart part, final OutputStream out) throws IOException {
        final Header header = part.getHeader();
        for (final MinimalField field : header) {
            AbstractMultipartForm.writeField(field, out);
        }
    }
}
