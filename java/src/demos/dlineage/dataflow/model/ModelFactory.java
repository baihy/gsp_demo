
package demos.dlineage.dataflow.model;

import demos.dlineage.util.Pair;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.nodes.*;
import gudusoft.gsqlparser.stmt.TCursorDeclStmt;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.TStoredProcedureSqlStatement;

public class ModelFactory {

    private ModelBindingManager modelManager;

    public ModelFactory(ModelBindingManager modelManager) {
        this.modelManager = modelManager;
    }

    public SelectResultSet createResultSet(TSelectSqlStatement select,
                                           boolean isTarget) {
        if (modelManager.getModel(select.getResultColumnList()) instanceof ResultSet) {
            return (SelectResultSet) modelManager.getModel(select.getResultColumnList());
        }
        SelectResultSet resultSet = new SelectResultSet(select, isTarget);
        modelManager.bindModel(select.getResultColumnList(), resultSet);
        return resultSet;
    }

    public ResultSet createResultSet(TParseTreeNode gspObject,
                                     boolean isTarget) {
        if (modelManager.getModel(gspObject) instanceof ResultSet) {
            return (ResultSet) modelManager.getModel(gspObject);
        }
        ResultSet resultSet = new ResultSet(gspObject, isTarget);
        modelManager.bindModel(gspObject, resultSet);
        return resultSet;
    }

    public ResultColumn createResultColumn(ResultSet resultSet,
                                           TResultColumn resultColumn) {
        if (modelManager.getModel(resultColumn) instanceof ResultColumn) {
            return (ResultColumn) modelManager.getModel(resultColumn);
        }
        ResultColumn column = new ResultColumn(resultSet, resultColumn);
        modelManager.bindModel(resultColumn, column);
        return column;
    }

    public ResultColumn createSelectSetResultColumn(
            ResultSet resultSet, ResultColumn resultColumn, int index) {
        if (modelManager.getModel(resultColumn) instanceof ResultColumn) {
            return (ResultColumn) modelManager.getModel(resultColumn);
        }
        ResultColumn column = new SelectSetResultColumn(resultSet,
                resultColumn, index);
        modelManager.bindModel(resultColumn, column);
        return column;
    }

    public ResultColumn createSelectSetResultColumn(
            ResultSet resultSet, TResultColumn resultColumn, int index) {
        SelectSetResultColumn column = new SelectSetResultColumn(resultSet,
                resultColumn, index);
        return column;
    }

    public ResultColumn createResultColumn(ResultSet resultSet,
                                           TObjectName resultColumn) {
        if (modelManager.getModel(resultColumn) instanceof ResultColumn) {
            return (ResultColumn) modelManager.getModel(resultColumn);
        }
        ResultColumn column = new ResultColumn(resultSet, resultColumn);
        modelManager.bindModel(resultColumn, column);
        return column;
    }

    public ResultColumn createMergeResultColumn(ResultSet resultSet,
                                                TObjectName resultColumn) {
        if (modelManager.getMergeModel(resultColumn) instanceof ResultColumn) {
            return (ResultColumn) modelManager.getMergeModel(resultColumn);
        }
        ResultColumn column = new ResultColumn(resultSet, resultColumn);
        modelManager.bindMergeModel(resultColumn, column);
        return column;
    }

    public ResultColumn createUpdateResultColumn(ResultSet resultSet,
                                                 TObjectName resultColumn) {
        if (modelManager.getUpdateModel(resultColumn) instanceof ResultColumn) {
            return (ResultColumn) modelManager.getUpdateModel(resultColumn);
        }
        ResultColumn column = new ResultColumn(resultSet, resultColumn);
        modelManager.bindUpdateModel(resultColumn, column);
        return column;
    }

    public ResultColumn createResultColumn(QueryTable queryTableModel,
                                           TResultColumn resultColumn) {
        if (modelManager.getModel(resultColumn) instanceof ResultColumn) {
            return (ResultColumn) modelManager.getModel(resultColumn);
        }
        ResultColumn column = new ResultColumn(queryTableModel, resultColumn);
        modelManager.bindModel(resultColumn, column);
        return column;
    }

    public Table createTableFromCreateDML(TTable table) {
        if (modelManager.getCreateModel(table) instanceof Table) {
            return (Table) modelManager.getCreateModel(table);
        }
        Table tableModel = new Table(table);
        modelManager.bindCreateModel(table, tableModel);
        return tableModel;
    }

    public Table createTable(TTable table) {
        if (modelManager.getModel(table) instanceof Table) {
            return (Table) modelManager.getModel(table);
        }
        Table tableModel = new Table(table);
        modelManager.bindModel(table, tableModel);
        return tableModel;
    }

    public QueryTable createQueryTable(TTable table) {
        QueryTable tableModel = null;

        if (table.getCTE() != null) {
            if (modelManager.getModel(table.getCTE()) instanceof QueryTable) {
                return (QueryTable) modelManager.getModel(table.getCTE());
            }

            tableModel = new QueryTable(table);

            modelManager.bindModel(table.getCTE(), tableModel);
        } else if (table.getSubquery() != null
                && table.getSubquery().getResultColumnList() != null) {
            if (modelManager.getModel(table.getSubquery()
                    .getResultColumnList()) instanceof QueryTable) {
                return (QueryTable) modelManager.getModel(table.getSubquery()
                        .getResultColumnList());
            }

            tableModel = new QueryTable(table);
            modelManager.bindModel(table.getSubquery()
                    .getResultColumnList(), tableModel);
        } else if (table.getAliasClause() != null
                && table.getAliasClause().getColumns() != null) {
            if (modelManager.getModel(table.getAliasClause()
                    .getColumns()) instanceof QueryTable) {
                return (QueryTable) modelManager
                        .getModel(table.getAliasClause().getColumns());
            }

            tableModel = new QueryTable(table);
            TObjectNameList columns = table.getAliasClause().getColumns();
            modelManager.bindModel(columns, tableModel);
            for (int i = 0; i < columns.size(); i++) {
                createResultColumn(tableModel,
                        columns.getObjectName(i));
            }
            modelManager.bindModel(table, tableModel);
        }else {
            if (modelManager.getModel(table) instanceof QueryTable) {
                return (QueryTable) modelManager.getModel(table);
            }
            tableModel = new QueryTable(table);
            modelManager.bindModel(table, tableModel);
        }
        return tableModel;
    }

    public TableColumn createTableColumn(Table table, TObjectName column) {
        if (modelManager.getModel(new Pair<Table, TObjectName>(table,
                column)) instanceof TableColumn) {
            return (TableColumn) modelManager.getModel(new Pair<Table, TObjectName>(table,
                    column));
        }
        TableColumn columnModel = new TableColumn(table, column);
        modelManager.bindModel(new Pair<Table, TObjectName>(table,
                column), columnModel);
        return columnModel;
    }

    public DataFlowRelation createDataFlowRelation() {
        DataFlowRelation relation = new DataFlowRelation();
        modelManager.addRelation(relation);
        return relation;
    }

    public TableColumn createTableColumn(Table table,
                                         TResultColumn column) {
        if (column.getAliasClause() != null
                && column.getAliasClause().getAliasName() != null) {
            TableColumn columnModel = new TableColumn(table,
                    column.getAliasClause().getAliasName());
            modelManager.bindModel(column, columnModel);
            return columnModel;
        }
        return null;
    }

    public RecordSetRelation createRecordSetRelation() {
        RecordSetRelation relation = new RecordSetRelation();
        modelManager.addRelation(relation);
        return relation;
    }

    public ImpactRelation createImpactRelation() {
        ImpactRelation relation = new ImpactRelation();
        modelManager.addRelation(relation);
        return relation;
    }

    public IndirectImpactRelation createIndirectImpactRelation() {
        IndirectImpactRelation relation = new IndirectImpactRelation();
        modelManager.addRelation(relation);
        return relation;
    }

    public JoinRelation createJoinRelation() {
        JoinRelation relation = new JoinRelation();
        modelManager.addRelation(relation);
        return relation;
    }

    public View createView(TCustomSqlStatement viewStmt, TObjectName viewName) {
        if (modelManager.getViewModel(viewStmt) instanceof View) {
            return (View) modelManager.getViewModel(viewStmt);
        }
        View viewModel = new View(viewStmt, viewName);
        modelManager.bindViewModel(viewStmt, viewModel);
        return viewModel;
    }

    public ViewColumn createViewColumn(View viewModel,
                                       TObjectName column, int index) {
        Pair<View, TObjectName> bindingModel = new Pair<View, TObjectName>(
                viewModel, column);
        if (modelManager.getViewModel(bindingModel) instanceof ViewColumn) {
            return (ViewColumn) modelManager.getViewModel(bindingModel);
        }
        ViewColumn columnModel = new ViewColumn(viewModel, column, index);
        modelManager.bindViewModel(bindingModel, columnModel);
        return columnModel;
    }

    public TableColumn createInsertTableColumn(Table tableModel,
                                               TObjectName column) {
        Pair<Table, TObjectName> bindingModel = new Pair<Table, TObjectName>(
                tableModel, column);
        if (modelManager
                .getInsertModel(bindingModel) instanceof TableColumn) {
            return (TableColumn) modelManager
                    .getInsertModel(bindingModel);
        }
        TableColumn columnModel = new TableColumn(tableModel, column);
        modelManager.bindInsertModel(bindingModel, columnModel);
        return columnModel;
    }

    public TableColumn createInsertTableColumn(Table tableModel,
                                               TConstant column, int columnIndex) {
        Pair<Table, TConstant> bindingModel = new Pair<Table, TConstant>(
                tableModel, column);

        if (modelManager
                .getInsertModel(bindingModel) instanceof TableColumn) {
            return (TableColumn) modelManager
                    .getInsertModel(bindingModel);
        }
        TableColumn columnModel = new TableColumn(tableModel,
                column,
                columnIndex);
        modelManager.bindInsertModel(bindingModel, columnModel);
        return columnModel;
    }

    public SelectSetResultSet createSelectSetResultSet(
            TSelectSqlStatement stmt) {
        if (modelManager.getModel(stmt) instanceof SelectSetResultSet) {
            return (SelectSetResultSet) modelManager.getModel(stmt);
        }
        SelectSetResultSet resultSet = new SelectSetResultSet(stmt);
        modelManager.bindModel(stmt, resultSet);
        return resultSet;
    }

    public ResultColumn createStarResultColumn(
            SelectResultSet resultSet,
            Pair<TResultColumn, TObjectName> starColumnPair) {
        if (modelManager.getModel(starColumnPair) instanceof ResultColumn) {
            return (ResultColumn) modelManager.getModel(starColumnPair);
        }
        ResultColumn column = new ResultColumn(resultSet, starColumnPair);
        modelManager.bindModel(starColumnPair, column);
        return column;
    }

    public CursorResultSet createCursorResultSet(TCursorDeclStmt stmt) {
        if (modelManager.getModel(stmt) instanceof SelectSetResultSet) {
            return (CursorResultSet) modelManager.getModel(stmt);
        }
        CursorResultSet resultSet = new CursorResultSet(stmt);
        modelManager.bindModel(stmt, resultSet);
        return resultSet;
    }
    
	public Procedure createProcedure(TStoredProcedureSqlStatement stmt) {
		if (this.modelManager.getModel(stmt) instanceof Procedure) {
			return (Procedure) this.modelManager.getModel(stmt);
		} else {
			Procedure procedure = new Procedure(stmt);
			this.modelManager.bindModel(stmt, procedure);
			return procedure;
		}
	}

	public Argument createProcedureArgument(Procedure procedure, TParameterDeclaration parameter) {
		if (this.modelManager.getModel(parameter) instanceof Argument) {
			return (Argument) this.modelManager.getModel(parameter);
		} else {
			Argument argumentModel = new Argument(procedure, parameter);
			this.modelManager.bindModel(parameter, argumentModel);
			return argumentModel;
		}
	}

}
