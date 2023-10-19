// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.resource;

import org.eclipse.jetty.util.log.Log;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.util.Iterator;
import java.util.List;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.util.ArrayList;
import java.nio.file.attribute.FileTime;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.io.InputStream;
import java.nio.file.Files;
import org.eclipse.jetty.util.StringUtil;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.io.File;
import java.nio.file.LinkOption;
import java.net.URI;
import java.nio.file.Path;
import org.eclipse.jetty.util.log.Logger;

public class PathResource extends Resource
{
    private static final Logger LOG;
    private final Path path;
    private final URI uri;
    private LinkOption[] linkOptions;
    
    public PathResource(final File file) {
        this(file.toPath());
    }
    
    public PathResource(final Path path) {
        this.linkOptions = new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
        this.assertValidPath(this.path = path);
        this.uri = this.path.toUri();
    }
    
    public PathResource(final URI uri) throws IOException {
        this.linkOptions = new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("not an absolute uri");
        }
        if (!uri.getScheme().equalsIgnoreCase("file")) {
            throw new IllegalArgumentException("not file: scheme");
        }
        Path path;
        try {
            path = new File(uri).toPath();
        }
        catch (InvalidPathException e) {
            throw e;
        }
        catch (IllegalArgumentException e2) {
            throw e2;
        }
        catch (Exception e3) {
            PathResource.LOG.ignore(e3);
            throw new IOException("Unable to build Path from: " + uri, e3);
        }
        this.path = path;
        this.uri = path.toUri();
    }
    
    public PathResource(final URL url) throws IOException, URISyntaxException {
        this(url.toURI());
    }
    
    @Override
    public Resource addPath(final String apath) throws IOException, MalformedURLException {
        return new PathResource(this.path.getFileSystem().getPath(this.path.toString(), apath));
    }
    
    private void assertValidPath(final Path path) {
        final String str = path.toString();
        final int idx = StringUtil.indexOfControlChars(str);
        if (idx >= 0) {
            throw new InvalidPathException(str, "Invalid Character at index " + idx);
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public boolean delete() throws SecurityException {
        try {
            return Files.deleteIfExists(this.path);
        }
        catch (IOException e) {
            PathResource.LOG.ignore(e);
            return false;
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final PathResource other = (PathResource)obj;
        if (this.path == null) {
            if (other.path != null) {
                return false;
            }
        }
        else if (!this.path.equals(other.path)) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean exists() {
        return Files.exists(this.path, this.linkOptions);
    }
    
    @Override
    public File getFile() throws IOException {
        return this.path.toFile();
    }
    
    public boolean getFollowLinks() {
        return this.linkOptions != null && this.linkOptions.length > 0 && this.linkOptions[0] == LinkOption.NOFOLLOW_LINKS;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(this.path, StandardOpenOption.READ);
    }
    
    @Override
    public String getName() {
        return this.path.toAbsolutePath().toString();
    }
    
    @Override
    public ReadableByteChannel getReadableByteChannel() throws IOException {
        return FileChannel.open(this.path, StandardOpenOption.READ);
    }
    
    @Override
    public URI getURI() {
        return this.uri;
    }
    
    @Override
    public URL getURL() {
        try {
            return this.path.toUri().toURL();
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.path == null) ? 0 : this.path.hashCode());
        return result;
    }
    
    @Override
    public boolean isContainedIn(final Resource r) throws MalformedURLException {
        return false;
    }
    
    @Override
    public boolean isDirectory() {
        return Files.isDirectory(this.path, this.linkOptions);
    }
    
    @Override
    public long lastModified() {
        try {
            final FileTime ft = Files.getLastModifiedTime(this.path, this.linkOptions);
            return ft.toMillis();
        }
        catch (IOException e) {
            PathResource.LOG.ignore(e);
            return 0L;
        }
    }
    
    @Override
    public long length() {
        try {
            return Files.size(this.path);
        }
        catch (IOException e) {
            return 0L;
        }
    }
    
    @Override
    public URI getAlias() {
        if (Files.isSymbolicLink(this.path)) {
            try {
                return this.path.toRealPath(new LinkOption[0]).toUri();
            }
            catch (IOException e) {
                PathResource.LOG.debug(e);
                return null;
            }
        }
        return null;
    }
    
    @Override
    public String[] list() {
        try (final DirectoryStream<Path> dir = Files.newDirectoryStream(this.path)) {
            final List<String> entries = new ArrayList<String>();
            for (final Path entry : dir) {
                String name = entry.getFileName().toString();
                if (Files.isDirectory(entry, new LinkOption[0])) {
                    name += "/";
                }
                entries.add(name);
            }
            final int size = entries.size();
            return entries.toArray(new String[size]);
        }
        catch (DirectoryIteratorException e) {
            PathResource.LOG.debug(e);
        }
        catch (IOException e2) {
            PathResource.LOG.debug(e2);
        }
        return null;
    }
    
    @Override
    public boolean renameTo(final Resource dest) throws SecurityException {
        if (dest instanceof PathResource) {
            final PathResource destRes = (PathResource)dest;
            try {
                final Path result = Files.move(this.path, destRes.path, StandardCopyOption.ATOMIC_MOVE);
                return Files.exists(result, this.linkOptions);
            }
            catch (IOException e) {
                PathResource.LOG.ignore(e);
                return false;
            }
        }
        return false;
    }
    
    public void setFollowLinks(final boolean followLinks) {
        if (followLinks) {
            this.linkOptions = new LinkOption[0];
        }
        else {
            this.linkOptions = new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
        }
    }
    
    static {
        LOG = Log.getLogger(PathResource.class);
    }
}
