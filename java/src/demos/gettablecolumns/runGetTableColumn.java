
package demos.gettablecolumns;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.IMetaDatabase;

class sampleMetaDB implements IMetaDatabase
{

//	String columns[][] = {
//			{
//					"server", "db", "schema", "promotion", "promo_desc"
//			}, {
//					"server", "db", "schema", "sales", "dollars"
//			},
//			{
//
//					"server", "db", "schema", "TBL_BBGPFOLIOMAP_REP", "M_TP_BUY"
//
//			},
//			{
//					"server", "db", "schema", "TBL_BBGPFOLIOMAP_REP", "M_ANDRES"
//			},
//			{
//					"server", "db", "schema", "TBL_BBGPOS_REP", "M_TP_BUY"
//			},
//			{
//					"server", "db", "schema", "TBL_BBGPOS_REP", "M_TP_PFOLIO"
//			}
//	};

	String columns[][] = {
			{
					"server", "db", "schema", "TBL_BBGPOS_REP", "M_TP_PFOLIO"
			}, {
			"server", "db", "schema", "A_FXPOSITIONDEL_REP", "M_FXDELTA"
			}, {
			"server", "db", "schema", "A_FXPOSITIONDEL_REP", "M_CLOSING_E"
			}, {
			"server", "db", "schema", "A_FXPOSITIONDEL_REP", "M_FXDELTA_Z"
			}
	};

	public boolean checkColumn( String server, String database, String schema,
			String table, String column )
	{
		boolean bServer, bDatabase, bSchema, bTable, bColumn, bRet = false;
		for ( int i = 0; i < columns.length; i++ )
		{
			if ( ( server == null ) || ( server.length( ) == 0 ) )
			{
				bServer = true;
			}
			else
			{
				bServer = columns[i][0].equalsIgnoreCase( server );
			}
			if ( !bServer )
				continue;

			if ( ( database == null ) || ( database.length( ) == 0 ) )
			{
				bDatabase = true;
			}
			else
			{
				bDatabase = columns[i][1].equalsIgnoreCase( database );
			}
			if ( !bDatabase )
				continue;

			if ( ( schema == null ) || ( schema.length( ) == 0 ) )
			{
				bSchema = true;
			}
			else
			{
				bSchema = columns[i][2].equalsIgnoreCase( schema );
			}

			if ( !bSchema )
				continue;

			bTable = columns[i][3].equalsIgnoreCase( table );
			if ( !bTable )
				continue;

			bColumn = columns[i][4].equalsIgnoreCase( column );
			if ( !bColumn )
				continue;

			bRet = true;
			break;

		}

		return bRet;
	}

}

public class runGetTableColumn
{

	public static void main( String args[] )
	{
		if ( args.length < 2 )
		{
			displayInitInformation( );
			return;
		}

		List<String> argList = Arrays.asList( args );

		File sqlFile = null;

		if ( argList.indexOf( "/f" ) != -1
				&& argList.size( ) > argList.indexOf( "/f" ) + 1 )
		{
			sqlFile = new File( args[argList.indexOf( "/f" ) + 1] );
			if ( !sqlFile.exists( ) || !sqlFile.isFile( ) )
			{
				System.out.println( sqlFile + " is not a valid file." );
				return;
			}
		}

		if ( sqlFile == null )
		{
			displayInitInformation( );
			return;
		}

		EDbVendor vendor = EDbVendor.dbvoracle;

		int index = argList.indexOf( "/t" );

		if ( index != -1 && args.length > index + 1 )
		{
			if ( args[index + 1].equalsIgnoreCase( "mssql" ) )
			{
				vendor = EDbVendor.dbvmssql;
			}
			else if ( args[index + 1].equalsIgnoreCase( "db2" ) )
			{
				vendor = EDbVendor.dbvdb2;
			}
			else if ( args[index + 1].equalsIgnoreCase( "mysql" ) )
			{
				vendor = EDbVendor.dbvmysql;
			}
			else if ( args[index + 1].equalsIgnoreCase( "netezza" ) )
			{
				vendor = EDbVendor.dbvnetezza;
			}
			else if ( args[index + 1].equalsIgnoreCase( "teradata" ) )
			{
				vendor = EDbVendor.dbvteradata;
			}
			else if ( args[index + 1].equalsIgnoreCase( "oracle" ) )
			{
				vendor = EDbVendor.dbvoracle;
			}
			else if ( args[index + 1].equalsIgnoreCase( "informix" ) )
			{
				vendor = EDbVendor.dbvinformix;
			}
			else if ( args[index + 1].equalsIgnoreCase( "sybase" ) )
			{
				vendor = EDbVendor.dbvsybase;
			}
			else if ( args[index + 1].equalsIgnoreCase( "postgresql" ) )
			{
				vendor = EDbVendor.dbvpostgresql;
			}
			else if ( args[index + 1].equalsIgnoreCase( "hive" ) )
			{
				vendor = EDbVendor.dbvhive;
			}
			else if ( args[index + 1].equalsIgnoreCase( "greenplum" ) )
			{
				vendor = EDbVendor.dbvgreenplum;
			}
			else if ( args[index + 1].equalsIgnoreCase( "redshift" ) )
			{
				vendor = EDbVendor.dbvredshift;
			}
			else if ( args[index + 1].equalsIgnoreCase( "bigquery" ) )
			{
				vendor = EDbVendor.dbvbigquery;
			}
			else if ( args[index + 1].equalsIgnoreCase( "couchbase" ) )
			{
				vendor = EDbVendor.dbvcouchbase;
			}
			else if ( args[index + 1].equalsIgnoreCase( "snowflake" ) )
			{
				vendor = EDbVendor.dbvsnowflake;
			}
		}

		TGetTableColumn getTableColumn = new TGetTableColumn( vendor );
		getTableColumn.showDetail = false;
		getTableColumn.showSummary = true;
		getTableColumn.showTreeStructure = false;
		getTableColumn.showBySQLClause = false;
		getTableColumn.showJoin = false;
		getTableColumn.showColumnLocation = true;
		getTableColumn.linkOrphanColumnToFirstTable = false;
		getTableColumn.showIndex = false;
		getTableColumn.showDatatype = true;
		getTableColumn.listStarColumn = true;
		getTableColumn.setMetaDatabase( new sampleMetaDB());

		if ( argList.indexOf( "/showDetail" ) != -1 )
		{
			getTableColumn.showSummary = false;
			getTableColumn.showDetail = true;
		}
		else if ( argList.indexOf( "/showTreeStructure" ) != -1 )
		{
			getTableColumn.showSummary = false;
			getTableColumn.showTreeStructure = true;
		}
		else if ( argList.indexOf( "/showBySQLClause" ) != -1 )
		{
			getTableColumn.showSummary = false;
			getTableColumn.showBySQLClause = true;
		}
		else if ( argList.indexOf( "/showJoin" ) != -1 )
		{
			getTableColumn.showSummary = false;
			getTableColumn.showJoin = true;
		}

		getTableColumn.runFile( sqlFile.getAbsolutePath( ) );
	}

	private static void displayInitInformation( )
	{
		System.out.println( "Usage: java runGetTableColumn [/f <path_to_sql_file>] [/t <database type>] [/<show option>]" );
		System.out.println( "/f: specify the sql file path to analyze." );
		System.out.println( "/t: option, set the database type. Support oracle, mysql, mssql, db2, netezza, teradata, informix, sybase, postgresql, hive, greenplum and redshift, the default type is oracle" );
		System.out.println( "/showSummary: default show option, display the summary information." );
		System.out.println( "/showDetail: show option, display the detail information." );
		System.out.println( "/showTreeStructure: show option, display the information as a tree structure." );
		System.out.println( "/showBySQLClause: show option, display the information group by sql clause type." );
		System.out.println( "/showJoin: show option, display the join table and column." );
	}
}
