package com.application.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator {
    public static String isValidName(String name) {
        String nameRegex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        Pattern pattern = Pattern.compile(nameRegex);
        if (name == null || name.trim().isEmpty()) return null;
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) return name;
        return null;
    }
}
