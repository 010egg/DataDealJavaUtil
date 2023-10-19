// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.time.format.DateTimeParseException;
import java.time.Duration;
import com.alibaba.fastjson2.util.DateUtils;
import java.net.URISyntaxException;
import java.net.URI;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.TypeReference;
import java.util.LinkedHashSet;
import java.util.Collection;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.function.Predicate;
import java.util.Set;
import java.util.regex.Pattern;

public final class StringSchema extends JSONSchema
{
    static final Pattern EMAIL_PATTERN;
    static final Pattern IP_DOMAIN_PATTERN;
    static final Pattern USER_PATTERN;
    final int maxLength;
    final int minLength;
    final String format;
    final String patternFormat;
    final Pattern pattern;
    final boolean typed;
    final AnyOf anyOf;
    final OneOf oneOf;
    final String constValue;
    final Set<String> enumValues;
    final Predicate<String> formatValidator;
    
    StringSchema(final JSONObject input) {
        super(input);
        this.typed = "string".equalsIgnoreCase(input.getString("type"));
        this.minLength = input.getIntValue("minLength", -1);
        this.maxLength = input.getIntValue("maxLength", -1);
        this.patternFormat = input.getString("pattern");
        this.pattern = ((this.patternFormat == null) ? null : Pattern.compile(this.patternFormat));
        this.format = input.getString("format");
        final Object anyOf = input.get("anyOf");
        if (anyOf instanceof JSONArray) {
            this.anyOf = JSONSchema.anyOf((JSONArray)anyOf, String.class);
        }
        else {
            this.anyOf = null;
        }
        final Object oneOf = input.get("oneOf");
        if (oneOf instanceof JSONArray) {
            this.oneOf = JSONSchema.oneOf((JSONArray)oneOf, String.class);
        }
        else {
            this.oneOf = null;
        }
        this.constValue = input.getString("const");
        Set<String> enumValues = null;
        final Object property = input.get("enum");
        if (property instanceof Collection) {
            final Collection enums = (Collection)property;
            enumValues = new LinkedHashSet<String>(enums.size());
            enumValues.addAll(enums);
        }
        else if (property instanceof Object[]) {
            enumValues = input.getObject("enum", TypeReference.collectionType(LinkedHashSet.class, String.class), new JSONReader.Feature[0]);
        }
        this.enumValues = enumValues;
        if (this.format == null) {
            this.formatValidator = null;
        }
        else {
            final String format = this.format;
            switch (format) {
                case "email": {
                    this.formatValidator = StringSchema::isEmail;
                    break;
                }
                case "ipv4": {
                    this.formatValidator = TypeUtils::validateIPv4;
                    break;
                }
                case "ipv6": {
                    this.formatValidator = TypeUtils::validateIPv6;
                    break;
                }
                case "uri": {
                    this.formatValidator = (url -> {
                        if (url == null || url.isEmpty()) {
                            return false;
                        }
                        else {
                            try {
                                new URI(url);
                                return true;
                            }
                            catch (URISyntaxException ignored) {
                                return false;
                            }
                        }
                    });
                    break;
                }
                case "date-time": {
                    this.formatValidator = DateUtils::isDate;
                    break;
                }
                case "date": {
                    this.formatValidator = DateUtils::isLocalDate;
                    break;
                }
                case "time": {
                    this.formatValidator = DateUtils::isLocalTime;
                    break;
                }
                case "duration": {
                    this.formatValidator = (str -> {
                        if (str == null || str.isEmpty()) {
                            return false;
                        }
                        else {
                            try {
                                Duration.parse(str);
                                return true;
                            }
                            catch (DateTimeParseException ignored2) {
                                return false;
                            }
                        }
                    });
                    break;
                }
                case "uuid": {
                    this.formatValidator = TypeUtils::isUUID;
                    break;
                }
                default: {
                    this.formatValidator = null;
                    break;
                }
            }
        }
    }
    
    @Override
    public Type getType() {
        return Type.String;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (value == null) {
            if (this.typed) {
                return StringSchema.REQUIRED_NOT_MATCH;
            }
            return StringSchema.SUCCESS;
        }
        else if (value instanceof String) {
            final String str = (String)value;
            if (this.minLength >= 0 || this.maxLength >= 0) {
                final int count = str.codePointCount(0, str.length());
                if (this.minLength >= 0 && count < this.minLength) {
                    return new ValidateResult(false, "minLength not match, expect >= %s, but %s", new Object[] { this.minLength, str.length() });
                }
                if (this.maxLength >= 0 && count > this.maxLength) {
                    return new ValidateResult(false, "maxLength not match, expect <= %s, but %s", new Object[] { this.maxLength, str.length() });
                }
            }
            if (this.pattern != null && !this.pattern.matcher(str).find()) {
                return new ValidateResult(false, "pattern not match, expect %s, but %s", new Object[] { this.patternFormat, str });
            }
            if (this.formatValidator != null && !this.formatValidator.test(str)) {
                return new ValidateResult(false, "format not match, expect %s, but %s", new Object[] { this.format, str });
            }
            if (this.anyOf != null) {
                final ValidateResult result = this.anyOf.validate(str);
                if (!result.isSuccess()) {
                    return result;
                }
            }
            if (this.oneOf != null) {
                final ValidateResult result = this.oneOf.validate(str);
                if (!result.isSuccess()) {
                    return result;
                }
            }
            if (this.constValue != null && !this.constValue.equals(str)) {
                return new ValidateResult(false, "must be const %s, but %s", new Object[] { this.constValue, str });
            }
            if (this.enumValues != null && !this.enumValues.contains(str)) {
                return new ValidateResult(false, "not in enum values, %s", new Object[] { str });
            }
            return StringSchema.SUCCESS;
        }
        else {
            if (!this.typed) {
                return StringSchema.SUCCESS;
            }
            return new ValidateResult(false, "expect type %s, but %s", new Object[] { Type.String, value.getClass() });
        }
    }
    
    public static boolean isEmail(final String email) {
        if (email == null) {
            return false;
        }
        if (email.endsWith(".")) {
            return false;
        }
        final Matcher emailMatcher = StringSchema.EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            return false;
        }
        final String user = emailMatcher.group(1);
        if (user.length() > 64) {
            return false;
        }
        if (!StringSchema.USER_PATTERN.matcher(user).matches()) {
            return false;
        }
        final String domain = emailMatcher.group(2);
        final Matcher ipDomainMatcher = StringSchema.IP_DOMAIN_PATTERN.matcher(domain);
        boolean validDomain;
        if (ipDomainMatcher.matches()) {
            final String inetAddress = ipDomainMatcher.group(1);
            validDomain = (TypeUtils.validateIPv4(inetAddress) || TypeUtils.validateIPv6(inetAddress));
        }
        else {
            validDomain = (DomainValidator.isValid(domain) || DomainValidator.isValidTld(domain));
        }
        return validDomain;
    }
    
    @Override
    public JSONObject toJSONObject() {
        final JSONObject object = new JSONObject();
        ((HashMap<String, String>)object).put("type", "string");
        if (this.minLength != -1) {
            ((HashMap<String, Integer>)object).put("minLength", this.minLength);
        }
        if (this.format != null) {
            ((HashMap<String, String>)object).put("format", this.format);
        }
        if (this.patternFormat != null) {
            ((HashMap<String, Pattern>)object).put("pattern", this.pattern);
        }
        if (this.anyOf != null) {
            ((HashMap<String, AnyOf>)object).put("anyOf", this.anyOf);
        }
        if (this.oneOf != null) {
            ((HashMap<String, OneOf>)object).put("oneOf", this.oneOf);
        }
        if (this.constValue != null) {
            ((HashMap<String, String>)object).put("const", this.constValue);
        }
        if (this.enumValues != null && !this.enumValues.isEmpty()) {
            ((HashMap<String, Set<String>>)object).put("enum", this.enumValues);
        }
        return object;
    }
    
    static {
        EMAIL_PATTERN = Pattern.compile("^\\s*?(.+)@(.+?)\\s*$");
        IP_DOMAIN_PATTERN = Pattern.compile("^\\[(.*)\\]$");
        USER_PATTERN = Pattern.compile("^\\s*(((\\\\.)|[^\\s\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\"))(\\.(((\\\\.)|[^\\s\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]]|')+|(\"[^\"]*\")))*$");
    }
}
