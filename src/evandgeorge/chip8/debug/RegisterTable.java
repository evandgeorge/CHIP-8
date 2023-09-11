package evandgeorge.chip8.debug;

import evandgeorge.chip8.vm.state.Processor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class RegisterTable extends JTable {
	static final int REGISTER_NAME_COLUMN = 0;
	static final int REGISTER_HEX_COLUMN = 1;
	static final int REGISTER_BIN_COLUMN = 2;

	private static final int DELAY_TIMER_ROW = 1 + 16 + 1;
	private static final int SOUND_TIMER_ROW = 2 + 16 + 1;
	private static final int STACK_POINTER_ROW = 3 + 16 + 1;
	private static final int INDEX_REGISTER_ROW = 4 + 16 + 1;
	private static final int PROGRAM_COUNTER_ROW = 5 + 16 + 1;

	private final RegisterTableModel model = new RegisterTableModel();

	public RegisterTable() {
		this.setModel(model);
		styleSetup();
	}

	public void update(Processor processor) {
		model.processor = processor;
		SwingUtilities.invokeLater(model::fireTableDataChanged);
	}

	private void styleSetup() {
		this.setGridColor(Color.LIGHT_GRAY);
		this.setBackground(new JPanel().getBackground());
		this.getTableHeader().setBackground(Color.LIGHT_GRAY);

		/* Make table columns just wide enough to show all content */
		FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
		FontMetrics nameColumnFontMetrics = this.getFontMetrics(this.getFont().deriveFont(Font.BOLD));

		/* Set preferred width of columns to width of sample column entries */
		this.getColumnModel().getColumn(REGISTER_NAME_COLUMN).setMinWidth(nameColumnFontMetrics.stringWidth("Program Counter."));
		this.getColumnModel().getColumn(REGISTER_HEX_COLUMN).setMinWidth(fontMetrics.stringWidth("0000_"));
		this.getColumnModel().getColumn(REGISTER_BIN_COLUMN).setMinWidth(fontMetrics.stringWidth("0000000000000000_"));


		/* Create custom renderers for the table */
		registerNameColumnCellRenderer = new RegisterNameColumnCellRenderer(this);

		centeredCellRenderer = new DefaultTableCellRenderer() {
			{
				this.setHorizontalAlignment(SwingConstants.CENTER);
			}
		};

		rightAlignedCellRenderer = new DefaultTableCellRenderer() {
			{
				this.setHorizontalAlignment(SwingConstants.RIGHT);
			}
		};
	}

	TableCellRenderer registerNameColumnCellRenderer;
	TableCellRenderer centeredCellRenderer;
	TableCellRenderer rightAlignedCellRenderer;

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if(row == 0) {
			return centeredCellRenderer;
		} else if(column == REGISTER_NAME_COLUMN || row == 17) {
			return registerNameColumnCellRenderer;
		} else {
			return rightAlignedCellRenderer;
		}
	}

	private static class RegisterTableModel extends AbstractTableModel {

		private Processor processor;

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return processor != null && columnIndex != REGISTER_NAME_COLUMN && rowIndex != 0 && rowIndex != 17;
		}

		@Override
		public int getRowCount() {
			return 16 + 5 + 1 + 1;
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(processor == null && rowIndex != 0 && columnIndex != REGISTER_NAME_COLUMN)
				return "";

			if(rowIndex == 0) {
				//Header row
				switch(columnIndex) {
					case REGISTER_NAME_COLUMN:
						return "";
					case REGISTER_HEX_COLUMN:
						return "Hex";
					case REGISTER_BIN_COLUMN:
					default:
						return "Bin";
				}
			} else if(rowIndex < 16 + 1) {
				//Vx register rows
				switch(columnIndex) {
					case REGISTER_NAME_COLUMN:
						return "Register V" + Integer.toHexString(rowIndex - 1).toUpperCase();
					case REGISTER_HEX_COLUMN:
						return processor.getMainRegister(rowIndex - 1).toHexString();
					case REGISTER_BIN_COLUMN:
						return processor.getMainRegister(rowIndex - 1).toBinaryString();
					default:
						return "??";
				}
			} else {
				//Special register rows
				switch(columnIndex) {
					case REGISTER_NAME_COLUMN:
						switch(rowIndex) {
							case INDEX_REGISTER_ROW:
								return "Index Register";
							case PROGRAM_COUNTER_ROW:
								return "Program Counter";
							case DELAY_TIMER_ROW:
								return "Delay Timer";
							case SOUND_TIMER_ROW:
								return "Sound Timer";
							case STACK_POINTER_ROW:
								return "Stack Pointer";
						}
					case REGISTER_HEX_COLUMN:
						switch(rowIndex) {
							case INDEX_REGISTER_ROW:
								return processor.getIndexRegister().toHexString();
							case PROGRAM_COUNTER_ROW:
								return processor.getProgramCounter().toHexString();
							case DELAY_TIMER_ROW:
								return processor.getDelayTimer().toHexString();
							case SOUND_TIMER_ROW:
								return processor.getSoundTimer().toHexString();
							case STACK_POINTER_ROW:
								return processor.getStackPointer().toHexString();
						}
					case REGISTER_BIN_COLUMN:
						switch(rowIndex) {
							case INDEX_REGISTER_ROW:
								return processor.getIndexRegister().toBinaryString();
							case PROGRAM_COUNTER_ROW:
								return processor.getProgramCounter().toBinaryString();
							case DELAY_TIMER_ROW:
								return processor.getDelayTimer().toBinaryString();
							case SOUND_TIMER_ROW:
								return processor.getSoundTimer().toBinaryString();
							case STACK_POINTER_ROW:
								return processor.getStackPointer().toBinaryString();
						}
					default:
						return "  ";
				}
			}
		}
	}

	private static class RegisterNameColumnCellRenderer extends DefaultTableCellRenderer {
		JTable table;

		public RegisterNameColumnCellRenderer(JTable table) {
			this.table = table;
			this.setHorizontalAlignment(SwingConstants.LEFT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel renderComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			//renderComponent.setFont(renderComponent.getFont().deriveFont(Font.BOLD));
			renderComponent.setBorder(null);
			return renderComponent;
		}

		@Override
		public Color getBackground() {
			return table != null ? table.getBackground() : Color.WHITE;
		}

		@Override
		public Color getForeground() {
			return table != null ? table.getForeground() : Color.BLACK;
		}
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension minimumSize = new Dimension(super.getMinimumSize());
		minimumSize.width = 10;

		for(int i = 0; i < getColumnCount(); i++) {
			minimumSize.width += this.columnModel.getColumn(i).getMinWidth();
		}

		return minimumSize;
	}
}
