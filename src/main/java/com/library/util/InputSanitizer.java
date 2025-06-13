package com.library.util;

/**
 * 输入内容过滤工具类：防止XSS、特殊字符、超长等攻击
 */
public class InputSanitizer {
    /**
     * 基础HTML转义，防止XSS
     */
    public static String sanitizeForHtml(String input) {
        if (input == null) return null;
        return input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#39;");
    }

    /**
     * 只保留字母、数字、下划线、@、.，适用于用户名、邮箱等
     */
    public static String sanitizeIdentifier(String input, int maxLen) {
        if (input == null) return "";
        String safe = input.replaceAll("[^a-zA-Z0-9_@.]", "");
        if (safe.length() > maxLen) safe = safe.substring(0, maxLen);
        return safe;
    }

    /**
     * 手机号过滤，只保留数字并限制长度
     */
    public static String sanitizeMobile(String input) {
        if (input == null) return "";
        String safe = input.replaceAll("[^0-9]", "");
        if (safe.length() > 11) safe = safe.substring(0, 11);
        return safe;
    }

    /**
     * 一般文本过滤，去除危险字符和超长
     */
    public static String sanitizeText(String input, int maxLen) {
        if (input == null) return "";
        String safe = input.replaceAll("[<>\"'%;()&+]", "");
        if (safe.length() > maxLen) safe = safe.substring(0, maxLen);
        return safe;
    }
}