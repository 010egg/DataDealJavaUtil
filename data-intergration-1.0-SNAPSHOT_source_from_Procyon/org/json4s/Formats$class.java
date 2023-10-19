// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.PartialFunction;
import scala.Some;
import scala.Tuple2;
import scala.None$;
import scala.math.Ordering;
import scala.package$;
import scala.Option;
import scala.Predef;
import scala.Function1;
import scala.Function2;
import scala.collection.Iterable;
import org.json4s.prefs.EmptyValueStrategy$;
import org.json4s.prefs.EmptyValueStrategy;
import org.json4s.reflect.package;
import scala.collection.Seq;
import java.lang.reflect.Type;
import scala.Predef$;
import scala.collection.immutable.Set;
import scala.collection.immutable.Nil$;
import scala.collection.immutable.List;

public abstract class Formats$class
{
    public static TypeHints typeHints(final Formats $this) {
        return NoTypeHints$.MODULE$;
    }
    
    public static List customSerializers(final Formats $this) {
        return (List)Nil$.MODULE$;
    }
    
    public static List customKeySerializers(final Formats $this) {
        return (List)Nil$.MODULE$;
    }
    
    public static List fieldSerializers(final Formats $this) {
        return (List)Nil$.MODULE$;
    }
    
    public static boolean wantsBigInt(final Formats $this) {
        return true;
    }
    
    public static boolean wantsBigDecimal(final Formats $this) {
        return false;
    }
    
    public static Set primitives(final Formats $this) {
        return (Set)Predef$.MODULE$.Set().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Type[] { JsonAST.JValue.class, JsonAST.JObject.class, JsonAST.JArray.class }));
    }
    
    public static List companions(final Formats $this) {
        return (List)Nil$.MODULE$;
    }
    
    public static boolean allowNull(final Formats $this) {
        return true;
    }
    
    public static boolean strictOptionParsing(final Formats $this) {
        return false;
    }
    
    public static boolean strictArrayExtraction(final Formats $this) {
        return false;
    }
    
    public static boolean alwaysEscapeUnicode(final Formats $this) {
        return false;
    }
    
    public static String typeHintFieldName(final Formats $this) {
        return "jsonClass";
    }
    
    public static package.ParameterNameReader parameterNameReader(final Formats $this) {
        return package.ParanamerReader$.MODULE$;
    }
    
    public static EmptyValueStrategy emptyValueStrategy(final Formats $this) {
        return EmptyValueStrategy$.MODULE$.default();
    }
    
    private static Formats copy(final Formats $this, final DateFormat wDateFormat, final String wTypeHintFieldName, final package.ParameterNameReader wParameterNameReader, final TypeHints wTypeHints, final List wCustomSerializers, final List wCustomKeySerializers, final List wFieldSerializers, final boolean wWantsBigInt, final boolean wWantsBigDecimal, final Set withPrimitives, final List wCompanions, final boolean wAllowNull, final boolean wStrictOptionParsing, final boolean wStrictArrayExtraction, final boolean wAlwaysEscapeUnicode, final EmptyValueStrategy wEmptyValueStrategy) {
        return (Formats)new Formats$$anon.Formats$$anon$3($this, wDateFormat, wTypeHintFieldName, wParameterNameReader, wTypeHints, wCustomSerializers, wCustomKeySerializers, wFieldSerializers, wWantsBigInt, wWantsBigDecimal, withPrimitives, wCompanions, wAllowNull, wStrictOptionParsing, wStrictArrayExtraction, wAlwaysEscapeUnicode, wEmptyValueStrategy);
    }
    
    private static DateFormat copy$default$1(final Formats $this) {
        return $this.dateFormat();
    }
    
    private static String copy$default$2(final Formats $this) {
        return $this.typeHintFieldName();
    }
    
    private static package.ParameterNameReader copy$default$3(final Formats $this) {
        return $this.parameterNameReader();
    }
    
    private static TypeHints copy$default$4(final Formats $this) {
        return $this.typeHints();
    }
    
    private static List copy$default$5(final Formats $this) {
        return $this.customSerializers();
    }
    
    private static List copy$default$6(final Formats $this) {
        return $this.customKeySerializers();
    }
    
    private static List copy$default$7(final Formats $this) {
        return $this.fieldSerializers();
    }
    
    private static boolean copy$default$8(final Formats $this) {
        return $this.wantsBigInt();
    }
    
    private static boolean copy$default$9(final Formats $this) {
        return $this.wantsBigDecimal();
    }
    
    private static Set copy$default$10(final Formats $this) {
        return $this.primitives();
    }
    
    private static List copy$default$11(final Formats $this) {
        return $this.companions();
    }
    
    private static boolean copy$default$12(final Formats $this) {
        return $this.allowNull();
    }
    
    private static boolean copy$default$13(final Formats $this) {
        return $this.strictOptionParsing();
    }
    
    private static boolean copy$default$14(final Formats $this) {
        return $this.strictArrayExtraction();
    }
    
    private static boolean copy$default$15(final Formats $this) {
        return $this.alwaysEscapeUnicode();
    }
    
    private static EmptyValueStrategy copy$default$16(final Formats $this) {
        return $this.emptyValueStrategy();
    }
    
    public static Formats withBigInt(final Formats $this) {
        final boolean x$25 = true;
        final DateFormat x$26 = copy$default$1($this);
        final String x$27 = copy$default$2($this);
        final package.ParameterNameReader x$28 = copy$default$3($this);
        final TypeHints x$29 = copy$default$4($this);
        final List x$30 = copy$default$5($this);
        final List x$31 = copy$default$6($this);
        final List x$32 = copy$default$7($this);
        final boolean x$33 = copy$default$9($this);
        final Set x$34 = copy$default$10($this);
        final List x$35 = copy$default$11($this);
        final boolean x$36 = copy$default$12($this);
        final boolean x$37 = copy$default$13($this);
        final boolean x$38 = copy$default$14($this);
        final boolean x$39 = copy$default$15($this);
        final EmptyValueStrategy x$40 = copy$default$16($this);
        return copy($this, x$26, x$27, x$28, x$29, x$30, x$31, x$32, x$25, x$33, x$34, x$35, x$36, x$37, x$38, x$39, x$40);
    }
    
    public static Formats withLong(final Formats $this) {
        final boolean x$41 = false;
        final DateFormat x$42 = copy$default$1($this);
        final String x$43 = copy$default$2($this);
        final package.ParameterNameReader x$44 = copy$default$3($this);
        final TypeHints x$45 = copy$default$4($this);
        final List x$46 = copy$default$5($this);
        final List x$47 = copy$default$6($this);
        final List x$48 = copy$default$7($this);
        final boolean x$49 = copy$default$9($this);
        final Set x$50 = copy$default$10($this);
        final List x$51 = copy$default$11($this);
        final boolean x$52 = copy$default$12($this);
        final boolean x$53 = copy$default$13($this);
        final boolean x$54 = copy$default$14($this);
        final boolean x$55 = copy$default$15($this);
        final EmptyValueStrategy x$56 = copy$default$16($this);
        return copy($this, x$42, x$43, x$44, x$45, x$46, x$47, x$48, x$41, x$49, x$50, x$51, x$52, x$53, x$54, x$55, x$56);
    }
    
    public static Formats withBigDecimal(final Formats $this) {
        final boolean x$57 = true;
        final DateFormat x$58 = copy$default$1($this);
        final String x$59 = copy$default$2($this);
        final package.ParameterNameReader x$60 = copy$default$3($this);
        final TypeHints x$61 = copy$default$4($this);
        final List x$62 = copy$default$5($this);
        final List x$63 = copy$default$6($this);
        final List x$64 = copy$default$7($this);
        final boolean x$65 = copy$default$8($this);
        final Set x$66 = copy$default$10($this);
        final List x$67 = copy$default$11($this);
        final boolean x$68 = copy$default$12($this);
        final boolean x$69 = copy$default$13($this);
        final boolean x$70 = copy$default$14($this);
        final boolean x$71 = copy$default$15($this);
        final EmptyValueStrategy x$72 = copy$default$16($this);
        return copy($this, x$58, x$59, x$60, x$61, x$62, x$63, x$64, x$65, x$57, x$66, x$67, x$68, x$69, x$70, x$71, x$72);
    }
    
    public static Formats withDouble(final Formats $this) {
        final boolean x$73 = false;
        final DateFormat x$74 = copy$default$1($this);
        final String x$75 = copy$default$2($this);
        final package.ParameterNameReader x$76 = copy$default$3($this);
        final TypeHints x$77 = copy$default$4($this);
        final List x$78 = copy$default$5($this);
        final List x$79 = copy$default$6($this);
        final List x$80 = copy$default$7($this);
        final boolean x$81 = copy$default$8($this);
        final Set x$82 = copy$default$10($this);
        final List x$83 = copy$default$11($this);
        final boolean x$84 = copy$default$12($this);
        final boolean x$85 = copy$default$13($this);
        final boolean x$86 = copy$default$14($this);
        final boolean x$87 = copy$default$15($this);
        final EmptyValueStrategy x$88 = copy$default$16($this);
        return copy($this, x$74, x$75, x$76, x$77, x$78, x$79, x$80, x$81, x$73, x$82, x$83, x$84, x$85, x$86, x$87, x$88);
    }
    
    public static Formats withCompanions(final Formats $this, final Seq comps) {
        final List x$89 = $this.companions().$colon$colon$colon(comps.toList());
        final DateFormat x$90 = copy$default$1($this);
        final String x$91 = copy$default$2($this);
        final package.ParameterNameReader x$92 = copy$default$3($this);
        final TypeHints x$93 = copy$default$4($this);
        final List x$94 = copy$default$5($this);
        final List x$95 = copy$default$6($this);
        final List x$96 = copy$default$7($this);
        final boolean x$97 = copy$default$8($this);
        final boolean x$98 = copy$default$9($this);
        final Set x$99 = copy$default$10($this);
        final boolean x$100 = copy$default$12($this);
        final boolean x$101 = copy$default$13($this);
        final boolean x$102 = copy$default$14($this);
        final boolean x$103 = copy$default$15($this);
        final EmptyValueStrategy x$104 = copy$default$16($this);
        return copy($this, x$90, x$91, x$92, x$93, x$94, x$95, x$96, x$97, x$98, x$99, x$89, x$100, x$101, x$102, x$103, x$104);
    }
    
    public static Formats preservingEmptyValues(final Formats $this) {
        return $this.withEmptyValueStrategy(EmptyValueStrategy$.MODULE$.preserve());
    }
    
    public static Formats skippingEmptyValues(final Formats $this) {
        return $this.withEmptyValueStrategy(EmptyValueStrategy$.MODULE$.skip());
    }
    
    public static Formats withEmptyValueStrategy(final Formats $this, final EmptyValueStrategy strategy) {
        final EmptyValueStrategy x$105 = strategy;
        final DateFormat x$106 = copy$default$1($this);
        final String x$107 = copy$default$2($this);
        final package.ParameterNameReader x$108 = copy$default$3($this);
        final TypeHints x$109 = copy$default$4($this);
        final List x$110 = copy$default$5($this);
        final List x$111 = copy$default$6($this);
        final List x$112 = copy$default$7($this);
        final boolean x$113 = copy$default$8($this);
        final boolean x$114 = copy$default$9($this);
        final Set x$115 = copy$default$10($this);
        final List x$116 = copy$default$11($this);
        final boolean x$117 = copy$default$12($this);
        final boolean x$118 = copy$default$13($this);
        final boolean x$119 = copy$default$14($this);
        final boolean x$120 = copy$default$15($this);
        return copy($this, x$106, x$107, x$108, x$109, x$110, x$111, x$112, x$113, x$114, x$115, x$116, x$117, x$118, x$119, x$120, x$105);
    }
    
    public static Formats withTypeHintFieldName(final Formats $this, final String name) {
        final String x$121 = name;
        final DateFormat x$122 = copy$default$1($this);
        final package.ParameterNameReader x$123 = copy$default$3($this);
        final TypeHints x$124 = copy$default$4($this);
        final List x$125 = copy$default$5($this);
        final List x$126 = copy$default$6($this);
        final List x$127 = copy$default$7($this);
        final boolean x$128 = copy$default$8($this);
        final boolean x$129 = copy$default$9($this);
        final Set x$130 = copy$default$10($this);
        final List x$131 = copy$default$11($this);
        final boolean x$132 = copy$default$12($this);
        final boolean x$133 = copy$default$13($this);
        final boolean x$134 = copy$default$14($this);
        final boolean x$135 = copy$default$15($this);
        final EmptyValueStrategy x$136 = copy$default$16($this);
        return copy($this, x$122, x$121, x$123, x$124, x$125, x$126, x$127, x$128, x$129, x$130, x$131, x$132, x$133, x$134, x$135, x$136);
    }
    
    public static Formats withEscapeUnicode(final Formats $this) {
        final boolean x$137 = true;
        final DateFormat x$138 = copy$default$1($this);
        final String x$139 = copy$default$2($this);
        final package.ParameterNameReader x$140 = copy$default$3($this);
        final TypeHints x$141 = copy$default$4($this);
        final List x$142 = copy$default$5($this);
        final List x$143 = copy$default$6($this);
        final List x$144 = copy$default$7($this);
        final boolean x$145 = copy$default$8($this);
        final boolean x$146 = copy$default$9($this);
        final Set x$147 = copy$default$10($this);
        final List x$148 = copy$default$11($this);
        final boolean x$149 = copy$default$12($this);
        final boolean x$150 = copy$default$13($this);
        final boolean x$151 = copy$default$14($this);
        final EmptyValueStrategy x$152 = copy$default$16($this);
        return copy($this, x$138, x$139, x$140, x$141, x$142, x$143, x$144, x$145, x$146, x$147, x$148, x$149, x$150, x$151, x$137, x$152);
    }
    
    public static Formats withStrictOptionParsing(final Formats $this) {
        final boolean x$153 = true;
        final DateFormat x$154 = copy$default$1($this);
        final String x$155 = copy$default$2($this);
        final package.ParameterNameReader x$156 = copy$default$3($this);
        final TypeHints x$157 = copy$default$4($this);
        final List x$158 = copy$default$5($this);
        final List x$159 = copy$default$6($this);
        final List x$160 = copy$default$7($this);
        final boolean x$161 = copy$default$8($this);
        final boolean x$162 = copy$default$9($this);
        final Set x$163 = copy$default$10($this);
        final List x$164 = copy$default$11($this);
        final boolean x$165 = copy$default$12($this);
        final boolean x$166 = copy$default$14($this);
        final boolean x$167 = copy$default$15($this);
        final EmptyValueStrategy x$168 = copy$default$16($this);
        return copy($this, x$154, x$155, x$156, x$157, x$158, x$159, x$160, x$161, x$162, x$163, x$164, x$165, x$153, x$166, x$167, x$168);
    }
    
    public static Formats withStrictArrayExtraction(final Formats $this) {
        final boolean x$169 = true;
        final DateFormat x$170 = copy$default$1($this);
        final String x$171 = copy$default$2($this);
        final package.ParameterNameReader x$172 = copy$default$3($this);
        final TypeHints x$173 = copy$default$4($this);
        final List x$174 = copy$default$5($this);
        final List x$175 = copy$default$6($this);
        final List x$176 = copy$default$7($this);
        final boolean x$177 = copy$default$8($this);
        final boolean x$178 = copy$default$9($this);
        final Set x$179 = copy$default$10($this);
        final List x$180 = copy$default$11($this);
        final boolean x$181 = copy$default$12($this);
        final boolean x$182 = copy$default$13($this);
        final boolean x$183 = copy$default$15($this);
        final EmptyValueStrategy x$184 = copy$default$16($this);
        return copy($this, x$170, x$171, x$172, x$173, x$174, x$175, x$176, x$177, x$178, x$179, x$180, x$181, x$182, x$169, x$183, x$184);
    }
    
    public static Formats strict(final Formats $this) {
        final boolean x$185 = true;
        final boolean x$186 = true;
        final DateFormat x$187 = copy$default$1($this);
        final String x$188 = copy$default$2($this);
        final package.ParameterNameReader x$189 = copy$default$3($this);
        final TypeHints x$190 = copy$default$4($this);
        final List x$191 = copy$default$5($this);
        final List x$192 = copy$default$6($this);
        final List x$193 = copy$default$7($this);
        final boolean x$194 = copy$default$8($this);
        final boolean x$195 = copy$default$9($this);
        final Set x$196 = copy$default$10($this);
        final List x$197 = copy$default$11($this);
        final boolean x$198 = copy$default$12($this);
        final boolean x$199 = copy$default$15($this);
        final EmptyValueStrategy x$200 = copy$default$16($this);
        return copy($this, x$187, x$188, x$189, x$190, x$191, x$192, x$193, x$194, x$195, x$196, x$197, x$198, x$185, x$186, x$199, x$200);
    }
    
    public static Formats nonStrict(final Formats $this) {
        final boolean x$201 = false;
        final boolean x$202 = false;
        final DateFormat x$203 = copy$default$1($this);
        final String x$204 = copy$default$2($this);
        final package.ParameterNameReader x$205 = copy$default$3($this);
        final TypeHints x$206 = copy$default$4($this);
        final List x$207 = copy$default$5($this);
        final List x$208 = copy$default$6($this);
        final List x$209 = copy$default$7($this);
        final boolean x$210 = copy$default$8($this);
        final boolean x$211 = copy$default$9($this);
        final Set x$212 = copy$default$10($this);
        final List x$213 = copy$default$11($this);
        final boolean x$214 = copy$default$12($this);
        final boolean x$215 = copy$default$15($this);
        final EmptyValueStrategy x$216 = copy$default$16($this);
        return copy($this, x$203, x$204, x$205, x$206, x$207, x$208, x$209, x$210, x$211, x$212, x$213, x$214, x$201, x$202, x$215, x$216);
    }
    
    public static Formats disallowNull(final Formats $this) {
        final boolean x$217 = false;
        final DateFormat x$218 = copy$default$1($this);
        final String x$219 = copy$default$2($this);
        final package.ParameterNameReader x$220 = copy$default$3($this);
        final TypeHints x$221 = copy$default$4($this);
        final List x$222 = copy$default$5($this);
        final List x$223 = copy$default$6($this);
        final List x$224 = copy$default$7($this);
        final boolean x$225 = copy$default$8($this);
        final boolean x$226 = copy$default$9($this);
        final Set x$227 = copy$default$10($this);
        final List x$228 = copy$default$11($this);
        final boolean x$229 = copy$default$13($this);
        final boolean x$230 = copy$default$14($this);
        final boolean x$231 = copy$default$15($this);
        final EmptyValueStrategy x$232 = copy$default$16($this);
        return copy($this, x$218, x$219, x$220, x$221, x$222, x$223, x$224, x$225, x$226, x$227, x$228, x$217, x$229, x$230, x$231, x$232);
    }
    
    public static Formats $plus(final Formats $this, final TypeHints extraHints) {
        final TypeHints x$233 = $this.typeHints().$plus(extraHints);
        final DateFormat x$234 = copy$default$1($this);
        final String x$235 = copy$default$2($this);
        final package.ParameterNameReader x$236 = copy$default$3($this);
        final List x$237 = copy$default$5($this);
        final List x$238 = copy$default$6($this);
        final List x$239 = copy$default$7($this);
        final boolean x$240 = copy$default$8($this);
        final boolean x$241 = copy$default$9($this);
        final Set x$242 = copy$default$10($this);
        final List x$243 = copy$default$11($this);
        final boolean x$244 = copy$default$12($this);
        final boolean x$245 = copy$default$13($this);
        final boolean x$246 = copy$default$14($this);
        final boolean x$247 = copy$default$15($this);
        final EmptyValueStrategy x$248 = copy$default$16($this);
        return copy($this, x$234, x$235, x$236, x$233, x$237, x$238, x$239, x$240, x$241, x$242, x$243, x$244, x$245, x$246, x$247, x$248);
    }
    
    public static Formats $plus(final Formats $this, final Serializer newSerializer) {
        final List x$249 = $this.customSerializers().$colon$colon((Object)newSerializer);
        final DateFormat x$250 = copy$default$1($this);
        final String x$251 = copy$default$2($this);
        final package.ParameterNameReader x$252 = copy$default$3($this);
        final TypeHints x$253 = copy$default$4($this);
        final List x$254 = copy$default$6($this);
        final List x$255 = copy$default$7($this);
        final boolean x$256 = copy$default$8($this);
        final boolean x$257 = copy$default$9($this);
        final Set x$258 = copy$default$10($this);
        final List x$259 = copy$default$11($this);
        final boolean x$260 = copy$default$12($this);
        final boolean x$261 = copy$default$13($this);
        final boolean x$262 = copy$default$14($this);
        final boolean x$263 = copy$default$15($this);
        final EmptyValueStrategy x$264 = copy$default$16($this);
        return copy($this, x$250, x$251, x$252, x$253, x$249, x$254, x$255, x$256, x$257, x$258, x$259, x$260, x$261, x$262, x$263, x$264);
    }
    
    public static Formats $plus(final Formats $this, final KeySerializer newSerializer) {
        final List x$265 = $this.customKeySerializers().$colon$colon((Object)newSerializer);
        final DateFormat x$266 = copy$default$1($this);
        final String x$267 = copy$default$2($this);
        final package.ParameterNameReader x$268 = copy$default$3($this);
        final TypeHints x$269 = copy$default$4($this);
        final List x$270 = copy$default$5($this);
        final List x$271 = copy$default$7($this);
        final boolean x$272 = copy$default$8($this);
        final boolean x$273 = copy$default$9($this);
        final Set x$274 = copy$default$10($this);
        final List x$275 = copy$default$11($this);
        final boolean x$276 = copy$default$12($this);
        final boolean x$277 = copy$default$13($this);
        final boolean x$278 = copy$default$14($this);
        final boolean x$279 = copy$default$15($this);
        final EmptyValueStrategy x$280 = copy$default$16($this);
        return copy($this, x$266, x$267, x$268, x$269, x$270, x$265, x$271, x$272, x$273, x$274, x$275, x$276, x$277, x$278, x$279, x$280);
    }
    
    public static Formats $plus$plus(final Formats $this, final Iterable newSerializers) {
        final List x$281 = (List)newSerializers.foldRight((Object)$this.customSerializers(), (Function2)new Formats$$anonfun.Formats$$anonfun$1($this));
        final DateFormat x$282 = copy$default$1($this);
        final String x$283 = copy$default$2($this);
        final package.ParameterNameReader x$284 = copy$default$3($this);
        final TypeHints x$285 = copy$default$4($this);
        final List x$286 = copy$default$6($this);
        final List x$287 = copy$default$7($this);
        final boolean x$288 = copy$default$8($this);
        final boolean x$289 = copy$default$9($this);
        final Set x$290 = copy$default$10($this);
        final List x$291 = copy$default$11($this);
        final boolean x$292 = copy$default$12($this);
        final boolean x$293 = copy$default$13($this);
        final boolean x$294 = copy$default$14($this);
        final boolean x$295 = copy$default$15($this);
        final EmptyValueStrategy x$296 = copy$default$16($this);
        return copy($this, x$282, x$283, x$284, x$285, x$281, x$286, x$287, x$288, x$289, x$290, x$291, x$292, x$293, x$294, x$295, x$296);
    }
    
    public static Formats $minus(final Formats $this, final Serializer serializer) {
        final List x$297 = (List)$this.customSerializers().filterNot((Function1)new Formats$$anonfun.Formats$$anonfun$2($this, serializer));
        final DateFormat x$298 = copy$default$1($this);
        final String x$299 = copy$default$2($this);
        final package.ParameterNameReader x$300 = copy$default$3($this);
        final TypeHints x$301 = copy$default$4($this);
        final List x$302 = copy$default$6($this);
        final List x$303 = copy$default$7($this);
        final boolean x$304 = copy$default$8($this);
        final boolean x$305 = copy$default$9($this);
        final Set x$306 = copy$default$10($this);
        final List x$307 = copy$default$11($this);
        final boolean x$308 = copy$default$12($this);
        final boolean x$309 = copy$default$13($this);
        final boolean x$310 = copy$default$14($this);
        final boolean x$311 = copy$default$15($this);
        final EmptyValueStrategy x$312 = copy$default$16($this);
        return copy($this, x$298, x$299, x$300, x$301, x$297, x$302, x$303, x$304, x$305, x$306, x$307, x$308, x$309, x$310, x$311, x$312);
    }
    
    public static Formats addKeySerializers(final Formats $this, final Iterable newKeySerializers) {
        return (Formats)newKeySerializers.foldLeft((Object)$this, (Function2)new Formats$$anonfun$addKeySerializers.Formats$$anonfun$addKeySerializers$1($this));
    }
    
    public static Formats $plus(final Formats $this, final FieldSerializer newSerializer) {
        final List x$313 = $this.fieldSerializers().$colon$colon((Object)Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)newSerializer.mf().runtimeClass()), (Object)newSerializer));
        final DateFormat x$314 = copy$default$1($this);
        final String x$315 = copy$default$2($this);
        final package.ParameterNameReader x$316 = copy$default$3($this);
        final TypeHints x$317 = copy$default$4($this);
        final List x$318 = copy$default$5($this);
        final List x$319 = copy$default$6($this);
        final boolean x$320 = copy$default$8($this);
        final boolean x$321 = copy$default$9($this);
        final Set x$322 = copy$default$10($this);
        final List x$323 = copy$default$11($this);
        final boolean x$324 = copy$default$12($this);
        final boolean x$325 = copy$default$13($this);
        final boolean x$326 = copy$default$14($this);
        final boolean x$327 = copy$default$15($this);
        final EmptyValueStrategy x$328 = copy$default$16($this);
        return copy($this, x$314, x$315, x$316, x$317, x$318, x$319, x$313, x$320, x$321, x$322, x$323, x$324, x$325, x$326, x$327, x$328);
    }
    
    public static Option fieldSerializer(final Formats $this, final Class clazz) {
        final Ordering ord = package$.MODULE$.Ordering().apply((Ordering)Ordering.Int$.MODULE$).on((Function1)new Formats$$anonfun.Formats$$anonfun$3($this, clazz));
        final List obj = (List)$this.fieldSerializers().filter((Function1)new Formats$$anonfun.Formats$$anonfun$4($this, clazz));
        Object module$;
        if (Nil$.MODULE$.equals(obj)) {
            module$ = None$.MODULE$;
        }
        else {
            module$ = new Some(((Tuple2)obj.min(ord))._2());
        }
        return (Option)module$;
    }
    
    @Deprecated
    public static PartialFunction customSerializer(final Formats $this, final Formats format) {
        return (PartialFunction)$this.customSerializers().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new Formats$$anonfun$customSerializer.Formats$$anonfun$customSerializer$3($this, format));
    }
    
    @Deprecated
    public static PartialFunction customDeserializer(final Formats $this, final Formats format) {
        return (PartialFunction)$this.customSerializers().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new Formats$$anonfun$customDeserializer.Formats$$anonfun$customDeserializer$3($this, format));
    }
    
    @Deprecated
    public static PartialFunction customKeySerializer(final Formats $this, final Formats format) {
        return (PartialFunction)$this.customKeySerializers().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new Formats$$anonfun$customKeySerializer.Formats$$anonfun$customKeySerializer$3($this, format));
    }
    
    @Deprecated
    public static PartialFunction customKeyDeserializer(final Formats $this, final Formats format) {
        return (PartialFunction)$this.customKeySerializers().foldLeft((Object)Predef$.MODULE$.Map().apply((Seq)Nil$.MODULE$), (Function2)new Formats$$anonfun$customKeyDeserializer.Formats$$anonfun$customKeyDeserializer$3($this, format));
    }
    
    public static void $init$(final Formats $this) {
    }
}
