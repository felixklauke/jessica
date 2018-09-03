package de.d3adspace.jessica.core.permission.status;

/**
 * Gives a permission a specific state. Whether it's not set at all or has a boolean value.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public enum PermissionStatus {

    /**
     * Permission is granted.
     */
    TRUE,

    /**
     * Permission is prohibited explicitly.
     */
    FALSE,

    /**
     * Permission is not set at all.
     */
    NOT_SET
}
