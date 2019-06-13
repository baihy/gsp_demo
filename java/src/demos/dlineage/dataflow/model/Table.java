
package demos.dlineage.dataflow.model;

import demos.dlineage.util.Pair;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.nodes.TTable;

import java.util.ArrayList;
import java.util.List;

public class Table {


    private int id;
    private String name;
    private String fullName;
    private String alias;
    private String parent;
    private Pair<Long, Long> startPosition;
    private Pair<Long, Long> endPosition;
    private List<TableColumn> columns = new ArrayList<TableColumn>();
    private boolean subquery = false;

    private TTable tableObject;

    public Table(TTable table) {
        if (table == null) {
            throw new IllegalArgumentException("Table arguments can't be null.");
        }

        id = ++ModelBindingManager.get().TABLE_COLUMN_ID;

        this.tableObject = table;

        TSourceToken startToken = table.getStartToken();
        TSourceToken endToken = table.getEndToken();
        this.startPosition = new Pair<Long, Long>(startToken.lineNo,
                startToken.columnNo);
        this.endPosition = new Pair<Long, Long>(endToken.lineNo,
                endToken.columnNo + endToken.astext.length());

        if (table.getLinkTable() != null) {
            this.fullName = table.getLinkTable().getFullName();
            this.name = table.getLinkTable().getName();
            this.alias = table.getName();
        } else {
            this.fullName = table.getFullName();
            this.name = table.getName();
            this.alias = table.getAliasName();
        }

        if (table.getSubquery() != null) {
            subquery = true;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pair<Long, Long> getStartPosition() {
        return startPosition;
    }

    public Pair<Long, Long> getEndPosition() {
        return endPosition;
    }

    public List<TableColumn> getColumns() {
        return columns;
    }

    public void addColumn(TableColumn column) {
        if (column != null && !this.columns.contains(column)) {
            this.columns.add(column);
        }
    }

    public TTable getTableObject() {
        return tableObject;
    }

    public String getAlias() {
        return alias;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean hasSubquery() {
        return subquery;
    }

    public String getDisplayName() {
        return getFullName() + getAlias() != null ? " [" + getAlias() + "]"
                : "";
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

}
