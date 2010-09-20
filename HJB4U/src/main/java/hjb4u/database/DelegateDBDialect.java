/*
 * HJB4U is toolchain for creating a HyperJAXB front end for database users.
 * Copyright (C) 2010  NigelB
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package hjb4u.database;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLMyISAMDialect;
import org.hibernate.dialect.lock.LockingStrategy;
import org.hibernate.exception.SQLExceptionConverter;
import org.hibernate.exception.ViolatedConstraintNameExtracter;
import org.hibernate.persister.entity.Lockable;
import org.hibernate.sql.CaseFragment;
import org.hibernate.sql.JoinFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Set;

/**
 * <code>DeligateDBDialect</code>
 * Date: 16/06/2010
 * Time: 6:39:48 AM
 *
 * @author Nigel Bajema
 */
public class DelegateDBDialect extends Dialect {
    private static final Logger logger = Logger.getLogger(DelegateDBDialect.class.getName());
    private Dialect delegate;

//    public Dialect getDialect() throws HibernateException {
//        return delegate.getDialect();
//    }

//    public Dialect getDialect(Properties props) throws HibernateException {
//        return delegate.getDialect(props);
//    }

//    public Properties getDefaultProperties() {
//        return delegate.getDefaultProperties();
//    }


    public DelegateDBDialect(Dialect delegate) {
        super();
        this.delegate = delegate;
    }

    public String toString() {
        return DelegateDBDialect.class.getName();
    }

    public String getTypeName(int code) throws HibernateException {
        return delegate.getTypeName(code);
    }

    public String getTypeName(int code, int length, int precision, int scale) throws HibernateException {
        return delegate.getTypeName(code, length, precision, scale);
    }

    public String getCastTypeName(int code) {
        return delegate.getCastTypeName(code);
    }

    public void registerColumnType(int code, int capacity, String name) {
        callProtected("registerColumnType", new Class[]{int.class, int.class, String.class},
                new Object[]{code, capacity, name});
    }

    public void registerColumnType(int code, String name) {
        callProtected("registerColumnType", new Class[]{int.class, String.class},
                new Object[]{code, name});
    }

    private Object callProtected(String name, Class[] sig, Object[] args) {

        Class d = delegate.getClass();
        Method[] methods;
        Method m;
        while (d != null) {
            System.out.println(d);
            try {
                methods = d.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals(name)) {
                        m = d.getDeclaredMethod(name, sig);
                        m.setAccessible(true);
                        return m.invoke(delegate, args);
                    }
                }
                d = d.getSuperclass();
            } catch (NoSuchMethodException e) {
                logger.trace(e, e);
            } catch (InvocationTargetException e) {
                logger.trace(e, e);
            } catch (IllegalAccessException e) {
                logger.trace(e, e);
            }
        }
        return null;
    }

    public String getHibernateTypeName(int code) throws HibernateException {
        return delegate.getHibernateTypeName(code);
    }

    public String getHibernateTypeName(int code, int length, int precision, int scale) throws HibernateException {
        return delegate.getHibernateTypeName(code, length, precision, scale);
    }

//    public void registerHibernateType(int code, int capacity, String name) {
//        delegate.registerHibernateType(code, capacity, name);
//    }
//
//    public void registerHibernateType(int code, String name) {
//        delegate.registerHibernateType(code, name);
//    }
//
//    public void registerFunction(String name, SQLFunction function) {
//        delegate.registerFunction(name, function);
//    }

//    public Map getFunctions() {
//        return delegate.getFunctions();
//    }

//    public void registerKeyword(String word) {
//        delegate.registerKeyword(word);
//    }

    public Set getKeywords() {
        return delegate.getKeywords();
    }

    public Class getNativeIdentifierGeneratorClass() {
        return delegate.getNativeIdentifierGeneratorClass();
    }

    public boolean supportsIdentityColumns() {
        return delegate.supportsIdentityColumns();
    }

    public boolean supportsInsertSelectIdentity() {
        return delegate.supportsInsertSelectIdentity();
    }

    public boolean hasDataTypeInIdentityColumn() {
        return delegate.hasDataTypeInIdentityColumn();
    }

    public String appendIdentitySelectToInsert(String insertString) {
        return delegate.appendIdentitySelectToInsert(insertString);
    }

    public String getIdentitySelectString(String table, String column, int type) throws MappingException {
        return delegate.getIdentitySelectString(table, column, type);
    }

//    public String getIdentitySelectString() throws MappingException {
//        return delegate.getIdentitySelectString();
//    }

    public String getIdentityColumnString(int type) throws MappingException {
        return delegate.getIdentityColumnString(type);
    }

//    public String getIdentityColumnString() throws MappingException {
//        return delegate.getIdentityColumnString();
//    }

    public String getIdentityInsertString() {
        return delegate.getIdentityInsertString();
    }

    public boolean supportsSequences() {
        return delegate.supportsSequences();
    }

    public boolean supportsPooledSequences() {
        return delegate.supportsPooledSequences();
    }

    public String getSequenceNextValString(String sequenceName) throws MappingException {
        return delegate.getSequenceNextValString(sequenceName);
    }

    public String getSelectSequenceNextValString(String sequenceName) throws MappingException {
        return delegate.getSelectSequenceNextValString(sequenceName);
    }

    public String[] getCreateSequenceStrings(String sequenceName) throws MappingException {
        return delegate.getCreateSequenceStrings(sequenceName);
    }

    public String[] getCreateSequenceStrings(String sequenceName, int initialValue, int incrementSize) throws MappingException {
        return delegate.getCreateSequenceStrings(sequenceName, initialValue, incrementSize);
    }

//    public String getCreateSequenceString(String sequenceName) throws MappingException {
//        return delegate.getCreateSequenceString(sequenceName);
//    }

//    public String getCreateSequenceString(String sequenceName, int initialValue, int incrementSize) throws MappingException {
//        return delegate.getCreateSequenceString(sequenceName, initialValue, incrementSize);
//    }

    public String[] getDropSequenceStrings(String sequenceName) throws MappingException {
        return delegate.getDropSequenceStrings(sequenceName);
    }

//    public String getDropSequenceString(String sequenceName) throws MappingException {
//        return delegate.getDropSequenceString(sequenceName);
//    }

    public String getQuerySequencesString() {
        return delegate.getQuerySequencesString();
    }

    public String getSelectGUIDString() {
        return delegate.getSelectGUIDString();
    }

    public boolean supportsLimit() {
        return delegate.supportsLimit();
    }

    public boolean supportsLimitOffset() {
        return delegate.supportsLimitOffset();
    }

    public boolean supportsVariableLimit() {
        return delegate.supportsVariableLimit();
    }

    public boolean bindLimitParametersInReverseOrder() {
        return delegate.bindLimitParametersInReverseOrder();
    }

    public boolean bindLimitParametersFirst() {
        return delegate.bindLimitParametersFirst();
    }

    public boolean useMaxForLimit() {
        return delegate.useMaxForLimit();
    }

    public String getLimitString(String query, int offset, int limit) {
        return delegate.getLimitString(query, offset, limit);
    }

//    public String getLimitString(String query, boolean hasOffset) {
//        return delegate.getLimitString(query, hasOffset);
//    }

    public LockingStrategy getLockingStrategy(Lockable lockable, LockMode lockMode) {
        return delegate.getLockingStrategy(lockable, lockMode);
    }

    public String getForUpdateString(LockMode lockMode) {
        return delegate.getForUpdateString(lockMode);
    }

    public String getForUpdateString() {
        return delegate.getForUpdateString();
    }

    public boolean forUpdateOfColumns() {
        return delegate.forUpdateOfColumns();
    }

    public boolean supportsOuterJoinForUpdate() {
        return delegate.supportsOuterJoinForUpdate();
    }

    public String getForUpdateString(String aliases) {
        return delegate.getForUpdateString(aliases);
    }

    public String getForUpdateNowaitString() {
        return delegate.getForUpdateNowaitString();
    }

    public String getForUpdateNowaitString(String aliases) {
        return delegate.getForUpdateNowaitString(aliases);
    }

    public String appendLockHint(LockMode mode, String tableName) {
        return delegate.appendLockHint(mode, tableName);
    }

    public String applyLocksToSql(String sql, Map aliasedLockModes, Map keyColumnNames) {
        return delegate.applyLocksToSql(sql, aliasedLockModes, keyColumnNames);
    }

    public String getCreateTableString() {
        return delegate.getCreateTableString();
    }

    public String getCreateMultisetTableString() {
        return delegate.getCreateMultisetTableString();
    }

    public boolean supportsTemporaryTables() {
        return delegate.supportsTemporaryTables();
    }

    public String generateTemporaryTableName(String baseTableName) {
        return delegate.generateTemporaryTableName(baseTableName);
    }

    public String getCreateTemporaryTableString() {
        return delegate.getCreateTemporaryTableString();
    }

    public String getCreateTemporaryTablePostfix() {
        return delegate.getCreateTemporaryTablePostfix();
    }

    public Boolean performTemporaryTableDDLInIsolation() {
        return delegate.performTemporaryTableDDLInIsolation();
    }

    public boolean dropTemporaryTableAfterUse() {
        return delegate.dropTemporaryTableAfterUse();
    }

    public int registerResultSetOutParameter(CallableStatement statement, int position) throws SQLException {
        return delegate.registerResultSetOutParameter(statement, position);
    }

    public ResultSet getResultSet(CallableStatement statement) throws SQLException {
        return delegate.getResultSet(statement);
    }

    public boolean supportsCurrentTimestampSelection() {
        return delegate.supportsCurrentTimestampSelection();
    }

    public boolean isCurrentTimestampSelectStringCallable() {
        return delegate.isCurrentTimestampSelectStringCallable();
    }

    public String getCurrentTimestampSelectString() {
        return delegate.getCurrentTimestampSelectString();
    }

    public String getCurrentTimestampSQLFunctionName() {
        return delegate.getCurrentTimestampSQLFunctionName();
    }

    public SQLExceptionConverter buildSQLExceptionConverter() {
        return delegate.buildSQLExceptionConverter();
    }

    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
        return delegate.getViolatedConstraintNameExtracter();
    }

    public String getSelectClauseNullString(int sqlType) {
        return delegate.getSelectClauseNullString(sqlType);
    }

    public boolean supportsUnionAll() {
        return delegate.supportsUnionAll();
    }

    public JoinFragment createOuterJoinFragment() {
        return delegate.createOuterJoinFragment();
    }

    public CaseFragment createCaseFragment() {
        return delegate.createCaseFragment();
    }

    public String getNoColumnsInsertString() {
        return delegate.getNoColumnsInsertString();
    }

    public String getLowercaseFunction() {
        return delegate.getLowercaseFunction();
    }

    public String transformSelectString(String select) {
        return delegate.transformSelectString(select);
    }

    public int getMaxAliasLength() {
        return delegate.getMaxAliasLength();
    }

    public String toBooleanValueString(boolean bool) {
        return delegate.toBooleanValueString(bool);
    }

    public char openQuote() {
        return delegate.openQuote();
    }

    public char closeQuote() {
        return delegate.closeQuote();
    }

//    public String quote(String column) {
//        return delegate.quote(column);
//    }

    public boolean hasAlterTable() {
        return delegate.hasAlterTable();
    }

    public boolean dropConstraints() {
        return delegate.dropConstraints();
    }

    public boolean qualifyIndexName() {
        return delegate.qualifyIndexName();
    }

    public boolean supportsUnique() {
        return delegate.supportsUnique();
    }

    public boolean supportsUniqueConstraintInCreateAlterTable() {
        return delegate.supportsUniqueConstraintInCreateAlterTable();
    }

    public String getAddColumnString() {
        return delegate.getAddColumnString();
    }

    public String getDropForeignKeyString() {
        return delegate.getDropForeignKeyString();
    }

    public String getTableTypeString() {
        return delegate.getTableTypeString();
    }

    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
        return delegate.getAddForeignKeyConstraintString(constraintName, foreignKey, referencedTable, primaryKey, referencesPrimaryKey);
    }

    public String getAddPrimaryKeyConstraintString(String constraintName) {
        return delegate.getAddPrimaryKeyConstraintString(constraintName);
    }

    public boolean hasSelfReferentialForeignKeyBug() {
        return delegate.hasSelfReferentialForeignKeyBug();
    }

    public String getNullColumnString() {
        return delegate.getNullColumnString();
    }

    public boolean supportsCommentOn() {
        return delegate.supportsCommentOn();
    }

    public String getTableComment(String comment) {
        return delegate.getTableComment(comment);
    }

    public String getColumnComment(String comment) {
        return delegate.getColumnComment(comment);
    }

    public boolean supportsIfExistsBeforeTableName() {
        return delegate.supportsIfExistsBeforeTableName();
    }

    public boolean supportsIfExistsAfterTableName() {
        return delegate.supportsIfExistsAfterTableName();
    }

    public boolean supportsColumnCheck() {
        return delegate.supportsColumnCheck();
    }

    public boolean supportsTableCheck() {
        return delegate.supportsTableCheck();
    }

    public boolean supportsCascadeDelete() {
        return delegate.supportsCascadeDelete();
    }

    public boolean supportsNotNullUnique() {
        return delegate.supportsNotNullUnique();
    }

    public String getCascadeConstraintsString() {
        return delegate.getCascadeConstraintsString();
    }

    public boolean supportsEmptyInList() {
        return delegate.supportsEmptyInList();
    }

    public boolean areStringComparisonsCaseInsensitive() {
        return delegate.areStringComparisonsCaseInsensitive();
    }

    public boolean supportsRowValueConstructorSyntax() {
        return delegate.supportsRowValueConstructorSyntax();
    }

    public boolean supportsRowValueConstructorSyntaxInInList() {
        return delegate.supportsRowValueConstructorSyntaxInInList();
    }

    public boolean useInputStreamToInsertBlob() {
        return delegate.useInputStreamToInsertBlob();
    }

    public boolean supportsParametersInInsertSelect() {
        return delegate.supportsParametersInInsertSelect();
    }

    public boolean supportsResultSetPositionQueryMethodsOnForwardOnlyCursor() {
        return delegate.supportsResultSetPositionQueryMethodsOnForwardOnlyCursor();
    }

    public boolean supportsCircularCascadeDeleteConstraints() {
        return delegate.supportsCircularCascadeDeleteConstraints();
    }

    public boolean supportsSubselectAsInPredicateLHS() {
        return delegate.supportsSubselectAsInPredicateLHS();
    }

    public boolean supportsExpectedLobUsagePattern() {
        return delegate.supportsExpectedLobUsagePattern();
    }

    public boolean supportsLobValueChangePropogation() {
        return delegate.supportsLobValueChangePropogation();
    }

    public boolean supportsUnboundedLobLocatorMaterialization() {
        return delegate.supportsUnboundedLobLocatorMaterialization();
    }

    public boolean supportsSubqueryOnMutatingTable() {
        return delegate.supportsSubqueryOnMutatingTable();
    }

    public boolean supportsExistsInSelect() {
        return delegate.supportsExistsInSelect();
    }

    public boolean doesReadCommittedCauseWritersToBlockReaders() {
        return delegate.doesReadCommittedCauseWritersToBlockReaders();
    }

    public boolean doesRepeatableReadCauseReadersToBlockWriters() {
        return delegate.doesRepeatableReadCauseReadersToBlockWriters();
    }

    public boolean supportsBindAsCallableArgument() {
        return delegate.supportsBindAsCallableArgument();
    }

    public static void main(String[] args) {
        DelegateDBDialect db = new DelegateDBDialect(new MySQLMyISAMDialect());
        db.registerColumnType(Types.BIT, Hibernate.INTEGER.getName());
    }
}
