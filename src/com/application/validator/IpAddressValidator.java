package com.application.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAddressValidator {
    public static String validateIpv4Address(String ipAddress) {
        String IPRegex =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(IPRegex);
        if (ipAddress == null) return null;
        Matcher matcher = pattern.matcher(ipAddress);
        if (matcher.matches()) return ipAddress;
        return null;
    }
}
