package evandgeorge.chip8.debug;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class FieldWatchDisplay extends JTable {

	private static final int WATCH_NAME_COLUMN = 0;
	private static final int WATCH_VALUE_COLUMN = 1;

	private final VariableWatchTableModel model = new VariableWatchTableModel();
	private FieldWatch[] fieldWatches = new FieldWatch[0];
	private Boolean[] recentlyChanged = new Boolean[0];
	private Set<FieldWatch> fieldBreakpoints = new HashSet<>();

	private static final long changeIndicatorTimeLength = 250;

	public void update(FieldWatch[] fieldWatches, Boolean[] fieldRecentlyChanged, Set<FieldWatch> fieldBreakpoints) {
		this.fieldWatches = fieldWatches;
		this.recentlyChanged = fieldRecentlyChanged;
		this.fieldBreakpoints = fieldBreakpoints;
		model.fireTableDataChanged();
	}

	public FieldWatchDisplay() {
		this.setModel(model);
		this.setDefaultRenderer(Object.class, new VariableWatchDisplayCellRenderer(this));
		this.addMouseListener(new TableMouseAdapter());
		this.setShowGrid(false);
		this.setIntercellSpacing(new Dimension(0, 0));
	}

	private class VariableWatchTableModel extends DefaultTableModel {

		@Override
		public String getColumnName(int column) {
			if(column == WATCH_NAME_COLUMN) {
				return "Name";
			} else if(column == WATCH_VALUE_COLUMN) {
				return "Value";
			} else {
				return "??";
			}
		}

		@Override
		public int getRowCount() {
			return fieldWatches != null ? fieldWatches.length : 0;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public Object getValueAt(int row, int column) {
			FieldWatch fieldWatch = watchAtRow(row);

			String name = fieldWatch.getName();
			Object value = fieldWatch.getValue();

			if(column == WATCH_NAME_COLUMN) {
				return name;
			} else if(column == WATCH_VALUE_COLUMN) {
				return value;
			} else {
				return "??";
			}
		}
	}

	private class VariableWatchDisplayCellRenderer extends DefaultTableCellRenderer {
		JTable table;

		public VariableWatchDisplayCellRenderer(JTable table) {
			this.table = table;
			this.setHorizontalAlignment(SwingConstants.LEFT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel renderComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if(fieldBreakpoints.contains(watchAtRow(row)) && column == WATCH_VALUE_COLUMN)
				renderComponent.setBackground(Color.PINK);
			if(rowChangedRecently(row))
				renderComponent.setBackground(Color.BLUE.brighter().brighter());
			else if(row % 2 == 0)
				renderComponent.setBackground(Color.WHITE);
			else
				renderComponent.setBackground(table.getBackground());

			renderComponent.setForeground(table.getForeground());

			return renderComponent;
		}
	}

	private class TableMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int row = rowAtPoint(e.getPoint());
			int col = columnAtPoint(e.getPoint());

			//set or remove breakpoint on right click
			if (e.getButton() == MouseEvent.BUTTON1 && row != -1 && col == WATCH_NAME_COLUMN) {
				if(fieldBreakpoints.contains(watchAtRow(row))) {
					fieldBreakpoints.remove(watchAtRow(row));
				} else {
					fieldBreakpoints.add(watchAtRow(row));
				}

				SwingUtilities.invokeLater(() -> model.fireTableRowsUpdated(row, row));
			}
		}
	}

	private FieldWatch watchAtRow(int row) {
		if(row < fieldWatches.length) {
			return (fieldWatches[row]);
		} else {
			return null;
		}
	}


	private boolean rowChangedRecently(int row) {
		return recentlyChanged[row];
	}
}
