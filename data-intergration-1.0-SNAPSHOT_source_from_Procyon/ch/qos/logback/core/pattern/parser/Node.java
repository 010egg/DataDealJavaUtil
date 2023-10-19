// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern.parser;

public class Node
{
    static final int LITERAL = 0;
    static final int SIMPLE_KEYWORD = 1;
    static final int COMPOSITE_KEYWORD = 2;
    final int type;
    final Object value;
    Node next;
    
    Node(final int type) {
        this(type, null);
    }
    
    Node(final int type, final Object value) {
        this.type = type;
        this.value = value;
    }
    
    public int getType() {
        return this.type;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public Node getNext() {
        return this.next;
    }
    
    public void setNext(final Node next) {
        this.next = next;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        final Node r = (Node)o;
        if (this.type == r.type) {
            if (this.value != null) {
                if (!this.value.equals(r.value)) {
                    return false;
                }
            }
            else if (r.value != null) {
                return false;
            }
            if ((this.next == null) ? (r.next == null) : this.next.equals(r.next)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.type;
        result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
        return result;
    }
    
    String printNext() {
        if (this.next != null) {
            return " -> " + this.next;
        }
        return "";
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        switch (this.type) {
            case 0: {
                buf.append("LITERAL(" + this.value + ")");
                break;
            }
            default: {
                buf.append(super.toString());
                break;
            }
        }
        buf.append(this.printNext());
        return buf.toString();
    }
}
