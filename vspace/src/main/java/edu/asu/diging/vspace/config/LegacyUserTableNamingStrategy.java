package edu.asu.diging.vspace.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * simple-users 1.x renamed its user entity from {@code User} to
 * {@code SimpleUser}. Since the entity does not declare an explicit table
 * name, Hibernate would otherwise look for new tables ({@code SimpleUser},
 * {@code SimpleUser_roles}) and existing accounts would no longer be found.
 * This strategy maps those names back to the existing {@code User},
 * {@code User_roles} and {@code User_username} identifiers so no database
 * migration is required. All other names are left unchanged.
 */
public class LegacyUserTableNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    private static final long serialVersionUID = 1L;

    private static final String NEW_PREFIX = "SimpleUser";
    private static final String OLD_PREFIX = "User";

    @Override
    public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment context) {
        return mapLegacyName(super.toPhysicalTableName(logicalName, context));
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment context) {
        return mapLegacyName(super.toPhysicalColumnName(logicalName, context));
    }

    private Identifier mapLegacyName(Identifier name) {
        if (name == null) {
            return null;
        }
        String text = name.getText();
        if (text.equals(NEW_PREFIX) || text.startsWith(NEW_PREFIX + "_")) {
            return Identifier.toIdentifier(OLD_PREFIX + text.substring(NEW_PREFIX.length()), name.isQuoted());
        }
        return name;
    }
}
