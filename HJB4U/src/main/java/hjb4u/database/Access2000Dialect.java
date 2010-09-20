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
// Originally written by Toby McM for Hibernate 2
// Extended to Hibernate 3 by Russell Bride S4M GmbH

//package com.tobych.mcmanalyst;

/*
 * JDK docs on some of this:
 * http://java.sun.com/j2se/1.5.0/docs/api/java/sql/Types.html
 *
 * Microsoft'schema documentation on Microsoft Jet SQL for Access 2000:
 *
 * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dnacc2k/html/acfundsql.asp (Fundamental)
 * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dnacc2k/html/acintsql.asp (Intermediate)
 * Intermediate has the basics on the data types
 * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dnacc2k/html/acadvsql.asp (Advanced)
 */

/*
  Are we making assumptions here? That we're using the Sun JDBC/ODBC bridge
  Also perhaps the ODBC driver, and how it'schema set up: connection pooling is not allowed
 */

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;

import java.sql.Types;

/**
 * An SQL dialect for Microsoft Access
 * @author Toby Champion
 */
public class Access2000Dialect extends Dialect {

    static final String NO_BATCH = "0"; // this should be just protected in Dialect, I think

	public Access2000Dialect() {
		super();

		// From intermediate:
		//  Following is a table that lists the basic Jet
		//  NUMERIC data types, various synonyms, and the
		//  number of bytes allocated for each.
		//  Jet Data Type 	Synonyms 	Storage Size
		//  TINYINT 	INTEGER1, BYTE 	1 byte
		//  SMALLINT 	SHORT, INTEGER2 	2 bytes
		//  INTEGER 	LONG, INT, INTEGER4 	4 bytes
		//  REAL 	SINGLE, FLOAT4, IEEESINGLE 	4 bytes
		//  FLOAT 	DOUBLE, FLOAT8, IEEEDOUBLE, NUMBER 	8 bytes
		//  DECIMAL 	NUMERIC, DEC 	17 bytes

		// Java integers: http://java.sun.com/docs/books/jls/second_edition/html/typesValues.doc.html#9151

		//  The values of the integral types are integers in the following ranges:

		//      * For byte, from -128 to 127, inclusive
		//      * For short, from -32768 to 32767, inclusive
		//      * For int, from -2147483648 to 2147483647, inclusive
		//      * For long, from -9223372036854775808 to 9223372036854775807, inclusive
		//      * For char, from '\u0000' to '\uffff' inclusive, that is, from 0 to 65535

		registerColumnType( Types.BIT, "bit" ); // This is a synonym for BOOLEAN
		// registerColumnType( Types.BIGINT, "bigint" ); // I don't think Access has these
		registerColumnType( Types.SMALLINT, "smallint" );
		registerColumnType( Types.TINYINT,  "tinyint" );
		registerColumnType( Types.INTEGER,  "integer" );
        // BIGINT (64bit) is not available, use an integer
		registerColumnType( Types.BIGINT,   "long" );

		registerColumnType( Types.CHAR,     "char(1)" );
		registerColumnType( Types.VARCHAR,  255, "varchar($l)" );
		registerColumnType( Types.VARCHAR,  "memo" );

		// Java: The floating-point types are float and
		// double, which are conceptually associated with the
		// single-precision 32-bit and double-precision 64-bit
		// format IEEE 754 values and operations as specified
		// in IEEE Standard for Binary Floating-Point
		// Arithmetic, ANSI/IEEE Standard 754-1985 (IEEE, New
		// York).

		// so in Java, FLOAT is 32-bits (4 bytes); DOUBLE is 64-bits (8 bytes)
		// and in Access, REAL is 4 bytes; FLOAT is 8 bytes. ouch.

		registerColumnType( Types.FLOAT, "real" );
		registerColumnType( Types.DOUBLE, "float" );

		// Access: The DATETIME data type is used to store
		// date, time, and combination date/time values for
		// the years ranging from 100 to 9999. It uses 8 bytes
		// of memory for storage, and its synonyms are DATE,
		// TIME, DATETIME, and TIMESTAMP.

		// koko Access can only store absolute date/time; the HH example wants
		// to store a *duration* in there and it can't work

		registerColumnType( Types.DATE, "date" ); // synonym for DATETIME
		registerColumnType( Types.TIME, "time" ); // synonym for DATETIME
		registerColumnType( Types.TIMESTAMP, "timestamp" ); // synonym for DATETIME

		registerColumnType( Types.VARBINARY, "varbinary($l)" ); // synonym for BINARY
		// registerColumnType( Types.NUMERIC, "numeric(19, $l)" ); // NUMERIC in Access takes no parameters

		// Access: MEMO is 65,535 characters; 2.14 GB if not binary data

		registerColumnType( Types.BLOB, "memo" ); // so that'schema 65,535 characters in Access
		registerColumnType( Types.CLOB, "memo" ); // so that'schema 2.14 GB in Access

		//  http://www.schemamania.org/jkl/booksonline/SQLBOL70/html/2_005_77.htm

		//  Migrating String Functions from Access to SQL Server

		//  This table shows the Microsoft SQL Server� equivalent function for each Microsoft Access function.
		//  Access 	SQL Server
		//  chr$(x) 	char(x)
		//  asc(x) 	ascii(x)
		//  str$(x) 	str(x)
		//  space$( x) 	space(x)
		//  lcase$(x) 	lower(x)
		//  ucase$( x) 	upper(x)
		//  len(x) 	datalength(x)
		//  ltrim$( x) 	ltrim(x)
		//  rtrim$(x) 	rtrim(x)
		//  right$(x,y) 	right(x,y)
		//  mid$(x,y,z) 	substring(x,y,z)

		// See http://www.stanford.edu/dept/itss/docs/oracle/9i/win.920/a97262/ch3.htm for some more stuff

		registerFunction( "asc",        new StandardSQLFunction("asc",      Hibernate.INTEGER   ) ); // SQL Server: ascii(x)
		registerFunction( "char",       new StandardSQLFunction("char",     Hibernate.CHARACTER ) ); // SQL Server: chr(x)
		registerFunction( "len",        new StandardSQLFunction("len",      Hibernate.LONG      ) ); // SQL Server: datalength(x)
		registerFunction( "lcase$",     new StandardSQLFunction("lcase$"                        ) ); // SQL Server: lower(x)
		registerFunction( "ucase$",     new StandardSQLFunction("ucase$"                        ) ); // SQL Server: upper(x)
		registerFunction( "str$",       new StandardSQLFunction("str$",     Hibernate.STRING    ) ); // SQL Server: str(x)
		registerFunction( "ltrim$",     new StandardSQLFunction("ltrim$"                        ) ); // SQL Server: ltrim(x)
		registerFunction( "rtrim$",     new StandardSQLFunction("rtrim$"                        ) ); // SQL Server: rtrim(x)
		registerFunction( "right$",     new StandardSQLFunction("right$"                        ) ); // SQL Server: right(x,y)
		registerFunction( "mid$",       new StandardSQLFunction("mid$"                          ) ); // SQL Server: substring(x,y,z)

		registerFunction( "getdate",    new NoArgSQLFunction("getdate",     Hibernate.TIMESTAMP ) );
		registerFunction( "getutcdate", new NoArgSQLFunction("getutcdate",  Hibernate.TIMESTAMP ) );
		registerFunction( "day",        new StandardSQLFunction("day",      Hibernate.INTEGER   ) );
		registerFunction( "month",      new StandardSQLFunction("month",    Hibernate.INTEGER   ) );
		registerFunction( "year",       new StandardSQLFunction("year",     Hibernate.INTEGER   ) );
		registerFunction( "datename",   new StandardSQLFunction("datename", Hibernate.STRING    ) );

		registerFunction( "abs",        new StandardSQLFunction("abs"                           ) );
		registerFunction( "sign",       new StandardSQLFunction("sign",     Hibernate.INTEGER   ) );

		registerFunction( "acos",       new StandardSQLFunction("acos",     Hibernate.DOUBLE    ) );
		registerFunction( "asin",       new StandardSQLFunction("asin",     Hibernate.DOUBLE    ) );
		registerFunction( "atan",       new StandardSQLFunction("atan",     Hibernate.DOUBLE    ) );
		registerFunction( "cos",        new StandardSQLFunction("cos",      Hibernate.DOUBLE    ) );
		registerFunction( "cot",        new StandardSQLFunction("cot",      Hibernate.DOUBLE    ) );
		registerFunction( "exp",        new StandardSQLFunction("exp",      Hibernate.DOUBLE    ) );
		registerFunction( "log",        new StandardSQLFunction("log",      Hibernate.DOUBLE    ) );
		registerFunction( "log10",      new StandardSQLFunction("log10",    Hibernate.DOUBLE    ) );
		registerFunction( "sin",        new StandardSQLFunction("sin",      Hibernate.DOUBLE    ) );
		registerFunction( "sqrt",       new StandardSQLFunction("sqrt",     Hibernate.DOUBLE    ) );
		registerFunction( "tan",        new StandardSQLFunction("tan",      Hibernate.DOUBLE    ) );
		registerFunction( "pi",         new NoArgSQLFunction("pi",          Hibernate.DOUBLE    ) );
		registerFunction( "square",     new StandardSQLFunction("square"                        ) );
		registerFunction( "rand",       new StandardSQLFunction("rand",     Hibernate.FLOAT     ) );

		registerFunction("radians",     new StandardSQLFunction("radians",  Hibernate.DOUBLE    ) );
		registerFunction("degrees",     new StandardSQLFunction("degrees",  Hibernate.DOUBLE    ) );

		registerFunction( "round",      new StandardSQLFunction("round"                         ) );
		registerFunction( "ceiling",    new StandardSQLFunction("ceiling"                       ) );
		registerFunction( "floor",      new StandardSQLFunction("floor"                         ) );

		// I don't know what this means, but it looks good to me and is used in
		getDefaultProperties().setProperty(Environment.STATEMENT_BATCH_SIZE, NO_BATCH);
        getDefaultProperties().setProperty(Environment.USE_GET_GENERATED_KEYS, "false");
	}

	public boolean supportsForUpdate() {
		return false;
	}

	public boolean supportsIdentityColumns() {
		return true;
	}
	public String getIdentitySelectString() {
		return "select @@identity";
	}
    public boolean supportsInsertSelectIdentity() {
		return false;
	}
    public boolean supportsParametersInInsertSelect() {
		return false;
	}

    //  appendIdentitySelectToInsert() -- can't do this as can only have one statement at a time, I think
	public String getIdentityColumnString() {
		return "counter";
	}
	public boolean hasDataTypeInIdentityColumn() {
		return false;
	}

	public boolean supportsIfExistsBeforeTableName() {
		return false;
	}
	public String getLowercaseFunction() {
		return "lcase$";
	}

    public boolean hasAlterTable() {
		return true;
	}
    public boolean dropConstraints() {
		return false;
	}
    public String getAddPrimaryKeyConstraintString(String constraintName) {
		return " constraint " + constraintName + " primary key ";
    }
	public String getAddForeignKeyConstraintString(
			String constraintName,
			String[] foreignKey,
			String referencedTable,
			String[] primaryKey,
			boolean referencesPrimaryKey
	) {
        return super.getAddForeignKeyConstraintString(
            constraintName,foreignKey,referencedTable,primaryKey,true);
    }

	public boolean supportsCascadeDelete() {
		return false;
	}

//	public JoinFragment createOuterJoinFragment() {
//		return new AccessJoinFragment();
//	}
	public String transformSelectString(String select) {
        // Access supports joins as per ANSI but without the "outer" keyword
        int matches = StringUtils.countMatches(select, " outer join ")
                    + StringUtils.countMatches(select, " inner join ");
        if (matches > 0) {
            select = StringUtils.replace(select, " left outer join ", ") left join ");
            select = StringUtils.replace(select, " right outer join ", ") right join ");
            select = StringUtils.replace(select, " inner join ", ") inner join ");
            select = StringUtils.replace(select, "from ", "from " + StringUtils.repeat("(",matches));
        }
        return select;
	}
}

