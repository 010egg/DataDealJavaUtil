// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.regex.Pattern;
import java.util.Arrays;
import java.math.BigInteger;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.function.Function;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.Fnv;
import java.util.ArrayList;
import java.util.List;

class JSONPathParser
{
    final String path;
    final JSONReader jsonReader;
    boolean dollar;
    boolean lax;
    boolean strict;
    int segmentIndex;
    JSONPathSegment first;
    JSONPathSegment second;
    List<JSONPathSegment> segments;
    int filterNests;
    boolean negative;
    
    public JSONPathParser(final String str) {
        this.path = str;
        this.jsonReader = JSONReader.of(str, JSONPath.PARSE_CONTEXT);
        if (this.jsonReader.ch == 'l' && this.jsonReader.nextIfMatchIdent('l', 'a', 'x')) {
            this.lax = true;
        }
        else if (this.jsonReader.ch == 's' && this.jsonReader.nextIfMatchIdent('s', 't', 'r', 'i', 'c', 't')) {
            this.strict = true;
        }
        if (this.jsonReader.ch == '-') {
            this.jsonReader.next();
            this.negative = true;
        }
        if (this.jsonReader.ch == '$') {
            this.jsonReader.next();
            this.dollar = true;
        }
    }
    
    JSONPath parse(final JSONPath.Feature... features) {
        if (this.dollar && this.jsonReader.ch == '\u001a') {
            if (this.negative) {
                return new JSONPathSingle(JSONPathFunction.FUNC_NEGATIVE, this.path, new JSONPath.Feature[0]);
            }
            return JSONPath.RootPath.INSTANCE;
        }
        else if (this.jsonReader.ch == 'e' && this.jsonReader.nextIfMatchIdent('e', 'x', 'i', 's', 't', 's')) {
            if (!this.jsonReader.nextIfMatch('(')) {
                throw new JSONException("syntax error " + this.path);
            }
            if (this.jsonReader.ch == '@') {
                this.jsonReader.next();
                if (!this.jsonReader.nextIfMatch('.')) {
                    throw new JSONException("syntax error " + this.path);
                }
            }
            final char ch = this.jsonReader.ch;
            if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z') && ch != '_' && ch != '@') {
                throw new JSONException("syntax error " + this.path);
            }
            final JSONPathSegment segment = this.parseProperty();
            if (!this.jsonReader.nextIfMatch(')')) {
                throw new JSONException("syntax error " + this.path);
            }
            return new JSONPathTwoSegment(this.path, segment, JSONPathFunction.FUNC_EXISTS, new JSONPath.Feature[0]);
        }
        else {
            while (this.jsonReader.ch != '\u001a') {
                final char ch2 = this.jsonReader.ch;
                JSONPathSegment segment2;
                if (ch2 == '.') {
                    this.jsonReader.next();
                    segment2 = this.parseProperty();
                }
                else if (this.jsonReader.ch == '[') {
                    segment2 = this.parseArrayAccess();
                }
                else if ((ch2 >= 'a' && ch2 <= 'z') || (ch2 >= 'A' && ch2 <= 'Z') || ch2 == '_') {
                    segment2 = this.parseProperty();
                }
                else if (ch2 == '?') {
                    if (this.dollar && this.segmentIndex == 0) {
                        this.first = JSONPathSegment.RootSegment.INSTANCE;
                        ++this.segmentIndex;
                    }
                    this.jsonReader.next();
                    segment2 = this.parseFilter();
                }
                else {
                    if (ch2 != '@') {
                        throw new JSONException("not support " + ch2);
                    }
                    this.jsonReader.next();
                    segment2 = JSONPathSegment.SelfSegment.INSTANCE;
                }
                if (this.segmentIndex == 0) {
                    this.first = segment2;
                }
                else if (this.segmentIndex == 1) {
                    this.second = segment2;
                }
                else if (this.segmentIndex == 2) {
                    (this.segments = new ArrayList<JSONPathSegment>()).add(this.first);
                    this.segments.add(this.second);
                    this.segments.add(segment2);
                }
                else {
                    this.segments.add(segment2);
                }
                ++this.segmentIndex;
            }
            if (this.negative) {
                if (this.segmentIndex == 1) {
                    this.second = JSONPathFunction.FUNC_NEGATIVE;
                }
                else if (this.segmentIndex == 2) {
                    (this.segments = new ArrayList<JSONPathSegment>()).add(this.first);
                    this.segments.add(this.second);
                    this.segments.add(JSONPathFunction.FUNC_NEGATIVE);
                }
                else {
                    this.segments.add(JSONPathFunction.FUNC_NEGATIVE);
                }
                ++this.segmentIndex;
            }
            if (this.segmentIndex == 1) {
                if (this.first instanceof JSONPathSegmentName) {
                    return new JSONPathSingleName(this.path, (JSONPathSegmentName)this.first, features);
                }
                if (this.first instanceof JSONPathSegmentIndex) {
                    final JSONPathSegmentIndex firstIndex = (JSONPathSegmentIndex)this.first;
                    if (firstIndex.index >= 0) {
                        return new JSONPathSingleIndex(this.path, firstIndex, features);
                    }
                }
                return new JSONPathSingle(this.first, this.path, features);
            }
            else {
                if (this.segmentIndex == 2) {
                    return new JSONPathTwoSegment(this.path, this.first, this.second, features);
                }
                return new JSONPathMulti(this.path, this.segments, features);
            }
        }
    }
    
    private JSONPathSegment parseArrayAccess() {
        this.jsonReader.next();
        JSONPathSegment segment = null;
        Label_1374: {
            switch (this.jsonReader.ch) {
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    int index = this.jsonReader.readInt32Value();
                    boolean last = false;
                    if (this.jsonReader.ch == ':') {
                        this.jsonReader.next();
                        if (this.jsonReader.ch == ']') {
                            segment = new JSONPathSegment.RangeIndexSegment(index, (index >= 0) ? Integer.MAX_VALUE : 0);
                            break;
                        }
                        final int end = this.jsonReader.readInt32Value();
                        segment = new JSONPathSegment.RangeIndexSegment(index, end);
                        break;
                    }
                    else {
                        if (this.jsonReader.isNumber() || (last = this.jsonReader.nextIfMatchIdent('l', 'a', 's', 't'))) {
                            final List<Integer> list = new ArrayList<Integer>();
                            list.add(index);
                            if (last) {
                                list.add(-1);
                                this.jsonReader.nextIfComma();
                            }
                            while (true) {
                                if (this.jsonReader.isNumber()) {
                                    index = this.jsonReader.readInt32Value();
                                    list.add(index);
                                }
                                else {
                                    if (!this.jsonReader.nextIfMatchIdent('l', 'a', 's', 't')) {
                                        break;
                                    }
                                    list.add(-1);
                                    this.jsonReader.nextIfComma();
                                }
                            }
                            final int[] indics = new int[list.size()];
                            for (int i = 0; i < list.size(); ++i) {
                                indics[i] = list.get(i);
                            }
                            segment = new JSONPathSegment.MultiIndexSegment(indics);
                            break;
                        }
                        segment = JSONPathSegmentIndex.of(index);
                        break;
                    }
                    break;
                }
                case '*': {
                    this.jsonReader.next();
                    segment = JSONPathSegment.AllSegment.INSTANCE_ARRAY;
                    break;
                }
                case ':': {
                    this.jsonReader.next();
                    final int end2 = (this.jsonReader.ch == ']') ? 0 : this.jsonReader.readInt32Value();
                    if (end2 > 0) {
                        segment = new JSONPathSegment.RangeIndexSegment(0, end2);
                        break;
                    }
                    segment = new JSONPathSegment.RangeIndexSegment(Integer.MIN_VALUE, end2);
                    break;
                }
                case '\"':
                case '\'': {
                    final String name = this.jsonReader.readString();
                    if (this.jsonReader.current() == ']') {
                        segment = new JSONPathSegmentName(name, Fnv.hashCode64(name));
                        break;
                    }
                    if (this.jsonReader.isString()) {
                        final List<String> names = new ArrayList<String>();
                        names.add(name);
                        do {
                            names.add(this.jsonReader.readString());
                        } while (this.jsonReader.isString());
                        final String[] nameArray = new String[names.size()];
                        names.toArray(nameArray);
                        segment = new JSONPathSegment.MultiNameSegment(nameArray);
                        break;
                    }
                    throw new JSONException("TODO : " + this.jsonReader.current());
                }
                case '?': {
                    this.jsonReader.next();
                    segment = this.parseFilter();
                    break;
                }
                case 'r': {
                    final String fieldName = this.jsonReader.readFieldNameUnquote();
                    if ("randomIndex".equals(fieldName) && this.jsonReader.nextIfMatch('(') && this.jsonReader.nextIfMatch(')') && this.jsonReader.ch == ']') {
                        segment = JSONPathSegment.RandomIndexSegment.INSTANCE;
                        break;
                    }
                    throw new JSONException("not support : " + fieldName);
                }
                case 'l': {
                    final String fieldName = this.jsonReader.readFieldNameUnquote();
                    if ("last".equals(fieldName)) {
                        segment = JSONPathSegmentIndex.of(-1);
                        break;
                    }
                    throw new JSONException("not support : " + fieldName);
                }
                case '(': {
                    this.jsonReader.next();
                    if (!this.jsonReader.nextIfMatch('@') || !this.jsonReader.nextIfMatch('.')) {
                        throw new JSONException("not support : " + this.path);
                    }
                    final String fieldNameUnquote;
                    final String fieldName = fieldNameUnquote = this.jsonReader.readFieldNameUnquote();
                    switch (fieldNameUnquote) {
                        case "length":
                        case "size": {
                            final int index2 = this.jsonReader.readInt32Value();
                            if (!this.jsonReader.nextIfMatch(')')) {
                                throw new JSONException("not support : " + fieldName);
                            }
                            if (index2 > 0) {
                                throw new JSONException("not support : " + fieldName);
                            }
                            segment = JSONPathSegmentIndex.of(index2);
                            break Label_1374;
                        }
                        default: {
                            throw new JSONException("not support : " + this.path);
                        }
                    }
                    break;
                }
                default: {
                    throw new JSONException("TODO : " + this.jsonReader.current());
                }
            }
        }
        if (this.jsonReader.ch == '&' || this.jsonReader.ch == '|' || this.jsonReader.ch == 'a' || this.jsonReader.ch == 'o') {
            --this.filterNests;
            segment = this.parseFilterRest(segment);
        }
        while (this.filterNests > 0) {
            this.jsonReader.next();
            --this.filterNests;
        }
        if (!this.jsonReader.nextIfArrayEnd()) {
            throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
        }
        return segment;
    }
    
    private JSONPathSegment parseProperty() {
        JSONPathSegment segment;
        if (this.jsonReader.ch == '*') {
            this.jsonReader.next();
            segment = JSONPathSegment.AllSegment.INSTANCE;
        }
        else if (this.jsonReader.ch == '.') {
            this.jsonReader.next();
            if (this.jsonReader.ch == '*') {
                this.jsonReader.next();
                segment = new JSONPathSegment.CycleNameSegment("*", Fnv.hashCode64("*"));
            }
            else {
                final long hashCode = this.jsonReader.readFieldNameHashCodeUnquote();
                final String name = this.jsonReader.getFieldName();
                segment = new JSONPathSegment.CycleNameSegment(name, hashCode);
            }
        }
        else {
            boolean isNum = this.jsonReader.isNumber();
            final long hashCode2 = this.jsonReader.readFieldNameHashCodeUnquote();
            final String name2 = this.jsonReader.getFieldName();
            if (isNum) {
                if (name2.length() > 9) {
                    isNum = false;
                }
                else {
                    for (int i = 0; i < name2.length(); ++i) {
                        final char ch = name2.charAt(i);
                        if (ch < '0' || ch > '9') {
                            isNum = false;
                            break;
                        }
                    }
                }
            }
            if (this.jsonReader.ch == '(') {
                this.jsonReader.next();
                final String s = name2;
                switch (s) {
                    case "length":
                    case "size": {
                        segment = JSONPathSegment.LengthSegment.INSTANCE;
                        break;
                    }
                    case "keys": {
                        segment = JSONPathSegment.KeysSegment.INSTANCE;
                        break;
                    }
                    case "values": {
                        segment = JSONPathSegment.ValuesSegment.INSTANCE;
                        break;
                    }
                    case "entrySet": {
                        segment = JSONPathSegment.EntrySetSegment.INSTANCE;
                        break;
                    }
                    case "min": {
                        segment = JSONPathSegment.MinSegment.INSTANCE;
                        break;
                    }
                    case "max": {
                        segment = JSONPathSegment.MaxSegment.INSTANCE;
                        break;
                    }
                    case "sum": {
                        segment = JSONPathSegment.SumSegment.INSTANCE;
                        break;
                    }
                    case "type": {
                        segment = JSONPathFunction.FUNC_TYPE;
                        break;
                    }
                    case "floor": {
                        segment = JSONPathFunction.FUNC_FLOOR;
                        break;
                    }
                    case "ceil":
                    case "ceiling": {
                        segment = JSONPathFunction.FUNC_CEIL;
                        break;
                    }
                    case "double": {
                        segment = JSONPathFunction.FUNC_DOUBLE;
                        break;
                    }
                    case "abs": {
                        segment = JSONPathFunction.FUNC_ABS;
                        break;
                    }
                    case "lower": {
                        segment = JSONPathFunction.FUNC_LOWER;
                        break;
                    }
                    case "upper": {
                        segment = JSONPathFunction.FUNC_UPPER;
                        break;
                    }
                    case "trim": {
                        segment = JSONPathFunction.FUNC_TRIM;
                        break;
                    }
                    case "negative": {
                        segment = JSONPathFunction.FUNC_NEGATIVE;
                        break;
                    }
                    case "first": {
                        segment = JSONPathFunction.FUNC_FIRST;
                        break;
                    }
                    case "last": {
                        segment = JSONPathFunction.FUNC_LAST;
                        break;
                    }
                    case "index": {
                        if (this.jsonReader.isNumber()) {
                            Number number = this.jsonReader.readNumber();
                            if (number instanceof BigDecimal) {
                                BigDecimal decimal = (BigDecimal)number;
                                decimal = decimal.stripTrailingZeros();
                                if (decimal.scale() != 0) {
                                    segment = new JSONPathFunction(new JSONPathFunction.IndexDecimal(decimal));
                                    break;
                                }
                                final BigInteger unscaledValue = decimal.unscaledValue();
                                if (unscaledValue.compareTo(TypeUtils.BIGINT_INT64_MIN) >= 0 && unscaledValue.compareTo(TypeUtils.BIGINT_INT64_MAX) <= 0) {
                                    number = unscaledValue.longValue();
                                }
                                else {
                                    number = unscaledValue;
                                }
                            }
                            if (number instanceof Integer || number instanceof Long) {
                                final long longValue = number.longValue();
                                segment = new JSONPathFunction(new JSONPathFunction.IndexInt(longValue));
                                break;
                            }
                        }
                        else if (this.jsonReader.isString()) {
                            final String indexValue = this.jsonReader.readString();
                            segment = new JSONPathFunction(new JSONPathFunction.IndexString(indexValue));
                            break;
                        }
                        throw new JSONException("not support syntax, path : " + this.path);
                    }
                    default: {
                        throw new JSONException("not support syntax, path : " + this.path);
                    }
                }
                if (!this.jsonReader.nextIfMatch(')')) {
                    throw new JSONException("not support syntax, path : " + this.path);
                }
            }
            else {
                segment = new JSONPathSegmentName(name2, hashCode2);
            }
        }
        return segment;
    }
    
    JSONPathSegment parseFilterRest(final JSONPathSegment segment) {
        boolean and = false;
        switch (this.jsonReader.ch) {
            case '&': {
                this.jsonReader.next();
                if (!this.jsonReader.nextIfMatch('&')) {
                    throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                }
                and = true;
                break;
            }
            case '|': {
                this.jsonReader.next();
                if (!this.jsonReader.nextIfMatch('|')) {
                    throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                }
                and = false;
                break;
            }
            case 'A':
            case 'a': {
                final String fieldName = this.jsonReader.readFieldNameUnquote();
                if (!"and".equalsIgnoreCase(fieldName)) {
                    throw new JSONException("syntax error : " + fieldName);
                }
                and = true;
                break;
            }
            case 'O':
            case 'o': {
                final String fieldName = this.jsonReader.readFieldNameUnquote();
                if (!"or".equalsIgnoreCase(fieldName)) {
                    throw new JSONException("syntax error : " + fieldName);
                }
                and = false;
                break;
            }
            default: {
                throw new JSONException("TODO : " + this.jsonReader.ch);
            }
        }
        final JSONPathSegment right = this.parseFilter();
        if (segment instanceof JSONPathFilter.GroupFilter) {
            final JSONPathFilter.GroupFilter group = (JSONPathFilter.GroupFilter)segment;
            group.filters.add(((JSONPathFilter)right).setAnd(and));
            return group;
        }
        final List<JSONPathFilter> filters = new ArrayList<JSONPathFilter>();
        filters.add((JSONPathFilter)segment);
        if (right instanceof JSONPathFilter.GroupFilter) {
            final JSONPathFilter.GroupFilter group2 = (JSONPathFilter.GroupFilter)right;
            final List<JSONPathFilter> groupFilters = group2.filters;
            if (groupFilters != null && groupFilters.size() > 0) {
                for (int i = 0; i < groupFilters.size(); ++i) {
                    final JSONPathFilter filter = groupFilters.get(i);
                    if (i == 0) {
                        filter.setAnd(and);
                    }
                    filters.add(filter);
                }
            }
        }
        else {
            filters.add(((JSONPathFilter)right).setAnd(and));
        }
        return new JSONPathFilter.GroupFilter(filters);
    }
    
    JSONPathSegment parseFilter() {
        final boolean parentheses = this.jsonReader.nextIfMatch('(');
        if (parentheses && this.filterNests > 0) {
            ++this.filterNests;
        }
        final boolean at = this.jsonReader.ch == '@';
        if (at) {
            this.jsonReader.next();
        }
        else if (this.jsonReader.nextIfMatchIdent('e', 'x', 'i', 's', 't', 's')) {
            if (!this.jsonReader.nextIfMatch('(')) {
                throw new JSONException(this.jsonReader.info("exists"));
            }
            if (this.jsonReader.nextIfMatch('@') && this.jsonReader.nextIfMatch('.')) {
                final long hashCode = this.jsonReader.readFieldNameHashCodeUnquote();
                final String fieldName = this.jsonReader.getFieldName();
                if (this.jsonReader.nextIfMatch(')')) {
                    if (parentheses && !this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    return new JSONPathFilter.NameExistsFilter(fieldName, hashCode);
                }
            }
            throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
        }
        final boolean starts = this.jsonReader.nextIfMatchIdent('s', 't', 'a', 'r', 't', 's');
        final boolean ends = !starts && this.jsonReader.nextIfMatchIdent('e', 'n', 'd', 's');
        if ((at && (starts || ends)) || (this.jsonReader.ch != '.' && !JSONReader.isFirstIdentifier(this.jsonReader.ch))) {
            if (this.jsonReader.nextIfMatch('(')) {
                ++this.filterNests;
                ++this.filterNests;
                return this.parseFilter();
            }
            if (!at) {
                throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
            }
            JSONPathFilter.Operator operator;
            if (starts || ends) {
                this.jsonReader.readFieldNameHashCodeUnquote();
                final String fieldName2 = this.jsonReader.getFieldName();
                if (!"with".equalsIgnoreCase(fieldName2)) {
                    throw new JSONException("not support operator : " + fieldName2);
                }
                operator = (starts ? JSONPathFilter.Operator.STARTS_WITH : JSONPathFilter.Operator.ENDS_WITH);
            }
            else {
                operator = JSONPath.parseOperator(this.jsonReader);
            }
            JSONPathSegment segment = null;
            if (this.jsonReader.isNumber()) {
                final Number number = this.jsonReader.readNumber();
                if (number instanceof Integer || number instanceof Long) {
                    segment = new JSONPathFilter.NameIntOpSegment(null, 0L, null, null, null, operator, number.longValue());
                }
            }
            else if (this.jsonReader.isString()) {
                final String string = this.jsonReader.readString();
                switch (operator) {
                    case STARTS_WITH: {
                        segment = new JSONPathFilter.StartsWithSegment(null, 0L, string);
                        break;
                    }
                    case ENDS_WITH: {
                        segment = new JSONPathFilter.EndsWithSegment(null, 0L, string);
                        break;
                    }
                    default: {
                        throw new JSONException("syntax error, " + string);
                    }
                }
            }
            while (this.jsonReader.ch == '&' || this.jsonReader.ch == '|') {
                --this.filterNests;
                segment = this.parseFilterRest(segment);
            }
            if (segment == null) {
                throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
            }
            if (parentheses && !this.jsonReader.nextIfMatch(')')) {
                throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
            }
            return segment;
        }
        else {
            if (at) {
                this.jsonReader.next();
            }
            long hashCode2 = this.jsonReader.readFieldNameHashCodeUnquote();
            String fieldName3 = this.jsonReader.getFieldName();
            if (parentheses && this.jsonReader.nextIfMatch(')')) {
                if (this.filterNests > 0) {
                    --this.filterNests;
                }
                return new JSONPathFilter.NameExistsFilter(fieldName3, hashCode2);
            }
            String functionName = null;
            long[] hashCode3 = null;
            String[] fieldName4 = null;
            while (this.jsonReader.ch == '.') {
                this.jsonReader.next();
                final long hash = this.jsonReader.readFieldNameHashCodeUnquote();
                final String str = this.jsonReader.getFieldName();
                if (this.jsonReader.ch == '(') {
                    functionName = str;
                    break;
                }
                if (hashCode3 == null) {
                    hashCode3 = new long[] { hash };
                    fieldName4 = new String[] { str };
                }
                else {
                    hashCode3 = Arrays.copyOf(hashCode3, hashCode3.length + 1);
                    hashCode3[hashCode3.length - 1] = hash;
                    fieldName4 = Arrays.copyOf(fieldName4, fieldName4.length + 1);
                    fieldName4[fieldName4.length - 1] = str;
                }
            }
            JSONPathFilter.Operator operator2 = null;
            Function function = null;
            if (this.jsonReader.ch == '(') {
                if (functionName == null) {
                    functionName = fieldName3;
                    fieldName3 = null;
                }
                final String s = functionName;
                switch (s) {
                    case "type": {
                        hashCode2 = 0L;
                        function = JSONPathFunction.TypeFunction.INSTANCE;
                        break;
                    }
                    case "size": {
                        hashCode2 = 0L;
                        function = JSONPathFunction.SizeFunction.INSTANCE;
                        break;
                    }
                    case "contains": {
                        hashCode2 = 0L;
                        operator2 = JSONPathFilter.Operator.CONTAINS;
                        break;
                    }
                    default: {
                        throw new JSONException("syntax error, function not support " + fieldName3);
                    }
                }
                if (function != null) {
                    this.jsonReader.next();
                    if (!this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException("syntax error, function " + functionName);
                    }
                }
            }
            if (operator2 == null) {
                operator2 = JSONPath.parseOperator(this.jsonReader);
            }
            switch (operator2) {
                case REG_MATCH:
                case RLIKE:
                case NOT_RLIKE: {
                    String regex;
                    boolean ignoreCase;
                    if (this.jsonReader.isString()) {
                        regex = this.jsonReader.readString();
                        ignoreCase = false;
                    }
                    else {
                        regex = this.jsonReader.readPattern();
                        ignoreCase = this.jsonReader.nextIfMatch('i');
                    }
                    final Pattern pattern = ignoreCase ? Pattern.compile(regex, 2) : Pattern.compile(regex);
                    JSONPathSegment segment2 = new JSONPathFilter.NameRLikeSegment(fieldName3, hashCode2, pattern, operator2 == JSONPathFilter.Operator.NOT_RLIKE);
                    if (this.jsonReader.ch == '&' || this.jsonReader.ch == '|' || this.jsonReader.ch == 'a' || this.jsonReader.ch == 'o') {
                        --this.filterNests;
                        segment2 = this.parseFilterRest(segment2);
                    }
                    if (!this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    return segment2;
                }
                case IN:
                case NOT_IN: {
                    if (this.jsonReader.ch != '(') {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    this.jsonReader.next();
                    JSONPathSegment segment3;
                    if (this.jsonReader.isString()) {
                        final List<String> list = new ArrayList<String>();
                        while (this.jsonReader.isString()) {
                            list.add(this.jsonReader.readString());
                        }
                        final String[] strArray = new String[list.size()];
                        list.toArray(strArray);
                        segment3 = new JSONPathFilter.NameStringInSegment(fieldName3, hashCode2, strArray, operator2 == JSONPathFilter.Operator.NOT_IN);
                    }
                    else {
                        if (!this.jsonReader.isNumber()) {
                            throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                        }
                        final List<Number> list2 = new ArrayList<Number>();
                        while (this.jsonReader.isNumber()) {
                            list2.add(this.jsonReader.readNumber());
                        }
                        final long[] values = new long[list2.size()];
                        for (int i = 0; i < list2.size(); ++i) {
                            values[i] = list2.get(i).longValue();
                        }
                        segment3 = new JSONPathFilter.NameIntInSegment(fieldName3, hashCode2, fieldName4, hashCode3, function, values, operator2 == JSONPathFilter.Operator.NOT_IN);
                    }
                    if (!this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    if (this.jsonReader.ch == '&' || this.jsonReader.ch == '|' || this.jsonReader.ch == 'a' || this.jsonReader.ch == 'o') {
                        --this.filterNests;
                        segment3 = this.parseFilterRest(segment3);
                    }
                    if (!this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    return segment3;
                }
                case CONTAINS: {
                    if (this.jsonReader.ch != '(') {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    this.jsonReader.next();
                    JSONPathSegment segment3;
                    if (this.jsonReader.isString()) {
                        final List<String> list = new ArrayList<String>();
                        while (this.jsonReader.isString()) {
                            list.add(this.jsonReader.readString());
                        }
                        final String[] strArray = new String[list.size()];
                        list.toArray(strArray);
                        segment3 = new JSONPathFilter.NameStringContainsSegment(fieldName3, hashCode2, fieldName4, hashCode3, strArray, false);
                    }
                    else {
                        if (!this.jsonReader.isNumber()) {
                            throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                        }
                        final List<Number> list2 = new ArrayList<Number>();
                        while (this.jsonReader.isNumber()) {
                            list2.add(this.jsonReader.readNumber());
                        }
                        final long[] values = new long[list2.size()];
                        for (int i = 0; i < list2.size(); ++i) {
                            values[i] = list2.get(i).longValue();
                        }
                        segment3 = new JSONPathFilter.NameLongContainsSegment(fieldName3, hashCode2, fieldName4, hashCode3, values, false);
                    }
                    if (!this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    if (!this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    return segment3;
                }
                case BETWEEN:
                case NOT_BETWEEN: {
                    if (!this.jsonReader.isNumber()) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    final Number begin = this.jsonReader.readNumber();
                    final String and = this.jsonReader.readFieldNameUnquote();
                    if (!"and".equalsIgnoreCase(and)) {
                        throw new JSONException("syntax error, " + and);
                    }
                    final Number end = this.jsonReader.readNumber();
                    final JSONPathSegment segment3 = new JSONPathFilter.NameIntBetweenSegment(fieldName3, hashCode2, begin.longValue(), end.longValue(), operator2 == JSONPathFilter.Operator.NOT_BETWEEN);
                    if (parentheses && !this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    return segment3;
                }
                default: {
                    JSONPathSegment segment3 = null;
                    switch (this.jsonReader.ch) {
                        case '+':
                        case '-':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9': {
                            final Number number2 = this.jsonReader.readNumber();
                            if (number2 instanceof Integer || number2 instanceof Long) {
                                segment3 = new JSONPathFilter.NameIntOpSegment(fieldName3, hashCode2, fieldName4, hashCode3, function, operator2, number2.longValue());
                                break;
                            }
                            if (number2 instanceof BigDecimal) {
                                segment3 = new JSONPathFilter.NameDecimalOpSegment(fieldName3, hashCode2, operator2, (BigDecimal)number2);
                                break;
                            }
                            throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                        }
                        case '\"':
                        case '\'': {
                            final String strVal = this.jsonReader.readString();
                            final int p0 = strVal.indexOf(37);
                            if (p0 == -1) {
                                if (operator2 == JSONPathFilter.Operator.LIKE) {
                                    operator2 = JSONPathFilter.Operator.EQ;
                                }
                                else if (operator2 == JSONPathFilter.Operator.NOT_LIKE) {
                                    operator2 = JSONPathFilter.Operator.NE;
                                }
                            }
                            if (operator2 == JSONPathFilter.Operator.LIKE || operator2 == JSONPathFilter.Operator.NOT_LIKE) {
                                final String[] items = strVal.split("%");
                                String startsWithValue = null;
                                String endsWithValue = null;
                                String[] containsValues = null;
                                if (p0 == 0) {
                                    if (strVal.charAt(strVal.length() - 1) == '%') {
                                        containsValues = new String[items.length - 1];
                                        System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                    }
                                    else {
                                        endsWithValue = items[items.length - 1];
                                        if (items.length > 2) {
                                            containsValues = new String[items.length - 2];
                                            System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                        }
                                    }
                                }
                                else if (strVal.charAt(strVal.length() - 1) == '%') {
                                    if (items.length == 1) {
                                        startsWithValue = items[0];
                                    }
                                    else {
                                        containsValues = items;
                                    }
                                }
                                else if (items.length == 1) {
                                    startsWithValue = items[0];
                                }
                                else if (items.length == 2) {
                                    startsWithValue = items[0];
                                    endsWithValue = items[1];
                                }
                                else {
                                    startsWithValue = items[0];
                                    endsWithValue = items[items.length - 1];
                                    containsValues = new String[items.length - 2];
                                    System.arraycopy(items, 1, containsValues, 0, containsValues.length);
                                }
                                segment3 = new JSONPathFilter.NameMatchFilter(fieldName3, hashCode2, startsWithValue, endsWithValue, containsValues, operator2 == JSONPathFilter.Operator.NOT_LIKE);
                                break;
                            }
                            segment3 = new JSONPathFilter.NameStringOpSegment(fieldName3, hashCode2, fieldName4, hashCode3, function, operator2, strVal);
                            break;
                        }
                        case 't': {
                            final String ident = this.jsonReader.readFieldNameUnquote();
                            if ("true".equalsIgnoreCase(ident)) {
                                segment3 = new JSONPathFilter.NameIntOpSegment(fieldName3, hashCode2, fieldName4, hashCode3, function, operator2, 1L);
                                break;
                            }
                            break;
                        }
                        case 'f': {
                            final String ident = this.jsonReader.readFieldNameUnquote();
                            if ("false".equalsIgnoreCase(ident)) {
                                segment3 = new JSONPathFilter.NameIntOpSegment(fieldName3, hashCode2, fieldName4, hashCode3, function, operator2, 0L);
                                break;
                            }
                            break;
                        }
                        case '[': {
                            final JSONArray array = this.jsonReader.read(JSONArray.class);
                            segment3 = new JSONPathFilter.NameArrayOpSegment(fieldName3, hashCode2, fieldName4, hashCode3, function, operator2, array);
                            break;
                        }
                        case '{': {
                            final JSONObject object = this.jsonReader.read(JSONObject.class);
                            segment3 = new JSONPathFilter.NameObjectOpSegment(fieldName3, hashCode2, fieldName4, hashCode3, function, operator2, object);
                            break;
                        }
                        case 'n': {
                            final boolean nextNull = this.jsonReader.nextIfNull();
                            if (nextNull) {
                                segment3 = new JSONPathFilter.NameIsNull(fieldName3, hashCode2, fieldName4, hashCode3, function);
                                break;
                            }
                            throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                        }
                        default: {
                            throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                        }
                    }
                    if (this.jsonReader.ch == '&' || this.jsonReader.ch == '|' || this.jsonReader.ch == 'a' || this.jsonReader.ch == 'o') {
                        --this.filterNests;
                        segment3 = this.parseFilterRest(segment3);
                    }
                    if (parentheses && !this.jsonReader.nextIfMatch(')')) {
                        throw new JSONException(this.jsonReader.info("jsonpath syntax error"));
                    }
                    return segment3;
                }
            }
        }
    }
}
