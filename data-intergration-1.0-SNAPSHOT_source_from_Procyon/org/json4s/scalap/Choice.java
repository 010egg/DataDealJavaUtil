// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Function0;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u00014q!\u0001\u0002\u0011\u0002\u0007\u0005\u0011B\u0001\u0004DQ>L7-\u001a\u0006\u0003\u0007\u0011\taa]2bY\u0006\u0004(BA\u0003\u0007\u0003\u0019Q7o\u001c85g*\tq!A\u0002pe\u001e\u001c\u0001!F\u0003\u000b/\u0005\"seE\u0002\u0001\u0017E\u0001\"\u0001D\b\u000e\u00035Q\u0011AD\u0001\u0006g\u000e\fG.Y\u0005\u0003!5\u0011a!\u00118z%\u00164\u0007C\u0002\n\u0014+\u0001\u001ac%D\u0001\u0003\u0013\t!\"A\u0001\u0003Sk2,\u0007C\u0001\f\u0018\u0019\u0001!a\u0001\u0007\u0001\t\u0006\u0004I\"AA%o#\tQR\u0004\u0005\u0002\r7%\u0011A$\u0004\u0002\b\u001d>$\b.\u001b8h!\taa$\u0003\u0002 \u001b\t\u0019\u0011I\\=\u0011\u0005Y\tCA\u0002\u0012\u0001\t\u000b\u0007\u0011DA\u0002PkR\u0004\"A\u0006\u0013\u0005\r\u0015\u0002AQ1\u0001\u001a\u0005\u0005\t\u0005C\u0001\f(\t\u0019A\u0003\u0001\"b\u00013\t\t\u0001\fC\u0003+\u0001\u0011\u00051&\u0001\u0004%S:LG\u000f\n\u000b\u0002YA\u0011A\"L\u0005\u0003]5\u0011A!\u00168ji\")\u0001\u0007\u0001D\u0001c\u000591\r[8jG\u0016\u001cX#\u0001\u001a\u0011\u0007MZ\u0014C\u0004\u00025s9\u0011Q\u0007O\u0007\u0002m)\u0011q\u0007C\u0001\u0007yI|w\u000e\u001e \n\u00039I!AO\u0007\u0002\u000fA\f7m[1hK&\u0011A(\u0010\u0002\u0005\u0019&\u001cHO\u0003\u0002;\u001b!)q\b\u0001C\u0001\u0001\u0006)\u0011\r\u001d9msR\u0011\u0011\t\u0012\t\u0006%\t\u00033EJ\u0005\u0003\u0007\n\u0011aAU3tk2$\b\"B#?\u0001\u0004)\u0012AA5o\u0011\u00159\u0005\u0001\"\u0011I\u0003\u0019y'/\u00127tKV)\u0011\n\u0014)U1R\u0011!j\u0017\t\u0007%MYujU,\u0011\u0005YaE!B'G\u0005\u0004q%aA%oeE\u0011!$\u0006\t\u0003-A#Q!\u0015$C\u0002I\u0013AaT;ueE\u0011\u0001%\b\t\u0003-Q#Q!\u0016$C\u0002Y\u0013!!\u0011\u001a\u0012\u0005\rj\u0002C\u0001\fY\t\u0015IfI1\u0001[\u0005\tA&'\u0005\u0002';!1AL\u0012CA\u0002u\u000bQa\u001c;iKJ\u00042\u0001\u00040K\u0013\tyVB\u0001\u0005=Eft\u0017-\\3?\u0001")
public interface Choice<In, Out, A, X> extends Rule<In, Out, A, X>
{
    List<Rule<In, Out, A, X>> choices();
    
    Result<Out, A, X> apply(final In p0);
    
     <In2 extends In, Out2, A2, X2> Rule<In2, Out2, A2, X2> orElse(final Function0<Rule<In2, Out2, A2, X2>> p0);
}
