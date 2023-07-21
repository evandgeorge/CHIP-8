package evandgeorge.chip8.vm.debug;

import evandgeorge.chip8.vm.state.Memory;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemoryDisplay extends JTable {

	private static final int ADDRESS_COLUMN = 0;
	private static final int HEX_COLUMN = 1;
	private static final int BIN_COLUMN = 2;
	private static final int SPRITE_COLUMN = 3;

	private boolean highlightSprite = false;
	private int highlightedSpriteAddress = -1;
	private int highlightedSpriteHeight = 0;

	private Memory memory;

	public void update(Memory memory) {
		this.memory = memory;
		SwingUtilities.invokeLater(this::repaint);
	}

	public void setHighlightedSprite(int spriteAddress, int spriteHeight) {
		highlightSprite = true;
		highlightedSpriteAddress = spriteAddress;
		highlightedSpriteHeight = spriteHeight;
	}

	public void clearSpriteHighlight() {
		highlightSprite = false;
	}


	public MemoryDisplay() {
		this.setModel(new MemoryTableModel());
		this.setShowGrid(false);
		this.setIntercellSpacing(new Dimension(0, 0));
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if(column == SPRITE_COLUMN) {
					if((row % 10) < 5) {
						rendererComponent.setBackground(Color.BLACK);
					} else {
						rendererComponent.setBackground(new Color(25, 25, 25));
					}

					if(highlightSprite && row >= highlightedSpriteAddress && row <= highlightedSpriteAddress + highlightedSpriteHeight - 1) {
						rendererComponent.setForeground(Color.PINK);
					} else {
						rendererComponent.setForeground(Color.WHITE);
					}

					((JLabel) rendererComponent).setHorizontalAlignment(SwingConstants.CENTER);
				} else {
					if(row % 2 == 0)
						rendererComponent.setBackground(Color.WHITE);
					else
						rendererComponent.setBackground(table.getBackground());

					rendererComponent.setForeground(table.getForeground());
				}

				return rendererComponent;
			}
		});


		FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
		this.getColumnModel().getColumn(ADDRESS_COLUMN).setMinWidth(fontMetrics.stringWidth("Addr" + 10));
		this.getColumnModel().getColumn(HEX_COLUMN).setMinWidth(fontMetrics.stringWidth("Hex" + 10));
		this.getColumnModel().getColumn(BIN_COLUMN).setMinWidth(fontMetrics.stringWidth("00000000" + 10));
		this.getColumnModel().getColumn(SPRITE_COLUMN).setMinWidth(fontMetrics.stringWidth("████████" + 10));
	}

	private class MemoryTableModel extends DefaultTableModel {

		@Override
		public String getColumnName(int column) {
			switch(column) {
				case ADDRESS_COLUMN:
					return "Addr";
				case HEX_COLUMN:
					return "Hex";
				case BIN_COLUMN:
					return "Bin";
				case SPRITE_COLUMN:
					return "Sprite";
				default:
					return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public int getRowCount() {
			return memory == null ? 0 : Memory.SIZE;
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(memory == null)
				return "??";

			Unsigned8Bit rowByte = memory.getByte(new Unsigned16Bit(rowIndex));

			switch(columnIndex) {
				case ADDRESS_COLUMN:
					return "0x" + Integer.toHexString(rowIndex).toUpperCase();
				case HEX_COLUMN:
					return rowByte.toHexString();
				case BIN_COLUMN:
					return rowByte.toBinaryString();
				case SPRITE_COLUMN:
					return spriteRowString(rowByte);
				default:
					return "??";
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}

	private static String spriteRowString(Unsigned8Bit spriteRow) {
		StringBuilder spriteRowStringBuilder = new StringBuilder();

		for(int i = 0; i < 8; i++)
			spriteRowStringBuilder.append(spriteRow.getBit(i) ? "█" : "   ");

		return spriteRowStringBuilder.toString();
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
