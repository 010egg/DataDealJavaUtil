// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import org.postgresql.core.Query;

interface V3Query extends Query
{
    SimpleQuery[] getSubqueries();
}
