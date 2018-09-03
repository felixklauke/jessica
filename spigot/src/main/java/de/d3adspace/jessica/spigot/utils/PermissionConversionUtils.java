package de.d3adspace.jessica.spigot.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class used to convert between the string and pair notation of a permission.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class PermissionConversionUtils {

    public static List<String> convertToStringNotation(Map<String, Boolean> permissions) {

        ArrayList<String> result = Lists.newArrayList();

        permissions.forEach((permission, value) -> {
            if (value) {
                result.add(permission);
                return;
            }
            result.add("-" + permission);
        });

        return result;
    }

    public static Map<String, Boolean> convertToPairNotation(List<String> permissions) {

        Map<String, Boolean> result = Maps.newHashMap();

        permissions.forEach(permission -> {
            if (permission.startsWith("-")) {

                result.put(permission.substring(1), false);
                return;
            }

            result.put(permission, true);
        });

        return result;
    }
}
