package evandgeorge.chip8.vm.debug.program;

import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.state.Memory;
import evandgeorge.chip8.vm.types.Program;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramDisplay extends JTable {

	private static final int ADDRESS_COLUMN = 0;
	private static final int INSTRUCTION_COLUMN = 1;
	private static final int DISASSEMBLY_COLUMN = 2;

	private boolean showInstructionDisassembly = true;
	private List<Integer> filteredInstructionIndexes;

	private final Color breakpointColor = Color.PINK;
	private final boolean[] breakpoints;
	private final Program program;

	private final ProgramTableModel model;
	private int currentInstructionRow = 0;

	private boolean filterEnabled = false;

	public void setFilter(String filterString) {
		filterString = filterString.toUpperCase();
		filteredInstructionIndexes = new ArrayList<>();
		int instructionIndex = 0;

		for(Instruction instruction : program.instructions) {
			if(instruction.getDescription().contains(filterString) || instruction.getDisassembly().contains(filterString))
				filteredInstructionIndexes.add(instructionIndex);

			instructionIndex++;
		}

		model.fireTableDataChanged();
	}

	public void setFilterEnabled(boolean filterEnabled) {
		this.filterEnabled = filterEnabled;
		model.fireTableDataChanged();
	}

	public void update(int currentInstructionRow, boolean blocked) {
		int lastInstructionRow = this.currentInstructionRow;
		this.currentInstructionRow = blocked ? currentInstructionRow : -1;

		if(blocked || lastInstructionRow != -1) {
			model.fireTableDataChanged();
		}

		//fixColumnSizes();
	}

	public ProgramDisplay(Program program) {
		this.program = program;
		this.breakpoints = new boolean[program.instructions.length];
		this.model = new ProgramTableModel();
		this.setModel(model);
		this.addMouseListener(new TableMouseAdapter());
		this.setDefaultRenderer(Object.class, new ProgramDisplayTableCellRenderer(this));
		this.setShowGrid(false);
		this.setIntercellSpacing(new Dimension(0, 0));
		this.setFilter("");

		fixColumnSizes();
	}

	private void fixColumnSizes() {
		FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
		this.getColumnModel().getColumn(ADDRESS_COLUMN).setMinWidth(fontMetrics.stringWidth("Addr"));
		this.getColumnModel().getColumn(INSTRUCTION_COLUMN).setMinWidth(fontMetrics.stringWidth("Instr__"));

		int maxDisassemblyStringWidth = fontMetrics.stringWidth("Disassembly");

		for(Instruction i : program.instructions) {
			int disassemblyStringWidth = fontMetrics.stringWidth(i.getDisassembly());
			maxDisassemblyStringWidth = Math.max(maxDisassemblyStringWidth, disassemblyStringWidth);
		}

		this.getColumnModel().getColumn(DISASSEMBLY_COLUMN).setMinWidth(maxDisassemblyStringWidth + 10);
	}

	public void ensureCurrentInstructionIsVisible(int row) {
		if (this.getParent() instanceof JViewport) {
			JViewport viewport = (JViewport) this.getParent();
			Rectangle rowCellRect = this.getCellRect(row, 0, true);
			rowCellRect.setLocation(rowCellRect.x - viewport.getViewPosition().x, rowCellRect.y - viewport.getViewPosition().y);
			viewport.scrollRectToVisible(rowCellRect);
		}
	}

	public boolean isBreakSet(int row) {
		return breakpoints[row];
	}

	@Override
	public boolean isRowSelected(int row) {
		return false;
	}

	@Override
	public String getToolTipText(MouseEvent e) {
		int row = rowAtPoint(e.getPoint());
		int col = columnAtPoint(e.getPoint());

		Instruction instruction = filterEnabled ? program.instructions[filteredInstructionIndexes.get(row)] : program.instructions[row];
		if(col != ADDRESS_COLUMN) {
			return instruction.getDescription();
		} else {
			return null;
		}
	}

	public void clearAllBreaks() {
		Arrays.fill(breakpoints, false);
		SwingUtilities.invokeLater(this.model::fireTableDataChanged);
	}

	private class ProgramTableModel extends DefaultTableModel {

		@Override
		public String getColumnName(int column) {
			switch(column) {
				case ADDRESS_COLUMN:
					return "Addr";
				case INSTRUCTION_COLUMN:
					return "Instr";
				case DISASSEMBLY_COLUMN:
					return "Disassembly";
				default:
					return "??";
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public int getRowCount() {
			return program == null ? 0 : filterEnabled ? filteredInstructionIndexes.size() : program.instructions.length;
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			int instructionIndex = (filterEnabled
					? filteredInstructionIndexes.get(rowIndex)
					: rowIndex);

			if(program == null)
				return "??";
			if(columnIndex == ADDRESS_COLUMN)
				return Integer.toHexString(Memory.ROM_BEGIN_OFFSET + 2 * instructionIndex).toUpperCase();
			else if(columnIndex == INSTRUCTION_COLUMN)
				return program.instructionCodes[instructionIndex].toHexString();
			else if(showInstructionDisassembly && columnIndex == DISASSEMBLY_COLUMN)
				return program.instructions[instructionIndex].getDisassembly();
			else
				return "??";
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}
	
	private class ProgramDisplayTableCellRenderer extends DefaultTableCellRenderer {
		JTable table;

		public ProgramDisplayTableCellRenderer(JTable table) {
			this.table = table;
			this.setHorizontalAlignment(SwingConstants.LEFT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel renderComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			int instructionIndex = filterEnabled ? filteredInstructionIndexes.get(row) : row;
			if(instructionIndex == currentInstructionRow) {
				renderComponent.setBackground(Color.BLUE.brighter().brighter());
				if(breakpoints[instructionIndex])
					renderComponent.setForeground(Color.PINK);
				else
					renderComponent.setForeground(Color.WHITE);
			} else {
				if (breakpoints[instructionIndex])
					renderComponent.setBackground(breakpointColor);
				else if(row % 2 == 0)
					renderComponent.setBackground(Color.WHITE);
				else
					renderComponent.setBackground(table.getBackground());

				renderComponent.setForeground(table.getForeground());
			}

			return renderComponent;
		}
	}

	private class TableMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int row = rowAtPoint(e.getPoint());
			int col = columnAtPoint(e.getPoint());

			//set or remove breakpoint on right click
			if (e.getButton() == MouseEvent.BUTTON1 && row != -1 && col == ADDRESS_COLUMN) {
				int instructionIndex = filterEnabled ? filteredInstructionIndexes.get(row) : row;
				breakpoints[instructionIndex] = !breakpoints[instructionIndex];
				SwingUtilities.invokeLater(() -> model.fireTableRowsUpdated(instructionIndex, instructionIndex));
			}
		}
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension minimumSize = new Dimension(super.getMinimumSize());
		minimumSize.width = 30;

		for(int i = 0; i < getColumnCount(); i++) {
			minimumSize.width += this.columnModel.getColumn(i).getMinWidth();
		}

		return minimumSize;
	}
}
