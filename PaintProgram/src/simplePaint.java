/* Author: 814930756
 * Last Modified: 10/31/14
 * This is a simple paint program that can draw 3 shapes in four colors. Rectangle, Oval, and Line. Red, Green, Blue and Black
 * The User can also decide if the Shape is to be filled with the color or not. 
 * The user may undo the action or clear the entire paint. As well as move the recently drawn shapes.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;

public class simplePaint{
	public static void main(String args[]){
		// Gui components
		final JLabel mouseInfo;
		JButton undo;
		JButton clear;
		// Creates the JFrame
		JFrame paintProgram = new JFrame("Paint Program");
		// Creates the container window
		Container window = paintProgram.getContentPane();
		// Sets the new layout
		window.setLayout(new BorderLayout());
		// Creates the paint object
		final Paint paint  = new Paint();
		// adds paint object to center of container
		window.add(paint, BorderLayout.CENTER);
		// Panels for buttons and label
		JPanel top = new JPanel(); 
		JPanel bottom = new JPanel();
		// Color names
		String[] colors = {"Red", "Black", "Green", "Blue"};
		// New color dropdown list
		JComboBox colorList = new JComboBox(colors);
		// Sets the selected value to null, in other words, nothing is selected upon running the program
		colorList.setSelectedItem(null);
		// Adds the actionListener to the comboBox
		colorList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
				// Grabs selected item
				String color = (String)cb.getSelectedItem();
				// Calls and passes the selected color
				paint.setColor(color);
			}
		});
		// Shape names
		String[] shapes = {"Line", "Ellipse", "Rectangle"};
		// New shape dropdown list
		JComboBox shapeList = new JComboBox(shapes);
		// Sets the selected value to null, in other words, nothing is selected upon running the program
		shapeList.setSelectedItem(null);
		// Adds the actionLisetener to the shapeList combobox
		shapeList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
				// Grabs selected item and stores into String shape
				String shape = (String)cb.getSelectedItem();
				// Calls and passes the selected shape
				paint.setShape(shape);
			}
		});
		// Creates JCheckBox
		final JCheckBox fill = new JCheckBox("Filled");	
		// Sets the selected value to false, or nothing
		fill.setSelected(false);
		// Adds the actionListener to the checkbox
		fill.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Grabs the selected value (T or F) and stores into boolean selected
				boolean selected = fill.isSelected();
				// Calls and passes the value of the selection
				paint.setCheck(selected);
				
			}
		});
		// Sets bottom panel to this size
		bottom.setPreferredSize(new Dimension(500,25));
		// New JButton
		undo = new JButton("Undo");
		// Adds the actionListener to the undo button
		undo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// calls undo
				paint.undo();	
			}
		});	
		// New JButton
		clear = new JButton("Clear");
		// Adds the actionListener to the clear button
		clear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// calls clear in the paint class
				paint.clear();	
			}
		});		
		// New JLabel for mouse Coords
		mouseInfo = new JLabel("");
		// Aligns the label to the left
        FlowLayout infoLayout = new FlowLayout();
        infoLayout.setAlignment(FlowLayout.LEFT);
        bottom.setLayout(infoLayout);
        // Adds the mouseMotionListener to paint object
        paint.addMouseMotionListener(new MouseMotionAdapter(){
        	public void mouseMoved(MouseEvent e){
        		// Coords of the mouse
	            int currentX = e.getX();
	            int currentY = e.getY();
	            // Displays the current coords 
	            mouseInfo.setText(String.format("Mouse Position: %s,%s ",currentX,currentY));
        	}
        });
   
        
        // Adding GUI Components     
		top.add(undo);
		top.add(clear);
		top.add(colorList);
		top.add(shapeList);
		top.add(fill);
		bottom.add(mouseInfo);
		window.add(top, BorderLayout.NORTH);
		window.add(bottom, BorderLayout.SOUTH);
		// Displaying and Sizing GUI
		paintProgram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		paintProgram.setSize(800,800);
		paintProgram.setVisible(true);
		paintProgram.setLocationRelativeTo(null);
		paintProgram.setResizable(false);
	}
}


class Paint extends JComponent{
	// Mouse Coords
	int currentX, currentY, oldX, oldY;
	// Image for painting
	Image image;
	// Graphics object
	Graphics2D graphics2D;
	// Boolean variables to determine what is to be drawn
	boolean line, rect, oval;
	boolean fillRect, fillOval;
	// Paint Constructor
	public Paint(){
		setDoubleBuffered(false);
		// Adds mouseListener - mousePressed
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				// Stores the old coords 
				oldX = e.getX();
				oldY = e.getY();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				// Determine how to move shape
			}
		});	
		// Adds the mouseListener - mouseReleased
		addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e){
				// Stores the new coords
				currentX = e.getX();
				currentY = e.getY();
				
				/* The following code uses boolean varibales to determine what is to be drawn
				 * based on user's selection. i.e Red Oval not Filled
				 * 
				 */
				if(graphics2D != null)
					if(line){
						graphics2D.drawLine(oldX, oldY, currentX, currentY);
					}
					if(rect){
						if(fillRect)
							graphics2D.fillRect(oldX, oldY, currentX, currentY);
						else
							graphics2D.drawRect(oldX, oldY, currentX, currentY);
					}
					if(oval){
						if(fillOval)
							graphics2D.fillOval(oldX, oldY, currentX, currentY);
						else
							graphics2D.drawOval(oldX, oldY, currentX, currentY);
					}
					// Repaints the Image
					repaint();
					// Stores Mouse Coords
					oldX = currentX;
					oldY = currentY;	
			}
		});
	}
	
	public void undo() {
		// shape.remove(size - 1)? Couldn't implement 
	}
	
	/* The follwing method sets the Shape, Color, and if the selection is T or F
	 * Using the boolean variables from above, this will determine what is to be drawn
	 * based on user selections
	 */

	public void setShape(String shape) {
		if(shape.equals("Line")){
			line = true;
			oval = false;
			rect = false;
		}
		if(shape.equals("Ellipse")){
			oval = true;
			line = false;
			rect = false;
		}
		if(shape.equals("Rectangle")){
			rect = true;
			line = false;
			oval = false;
		}	
	}
	public void setColor(String color) {
		if(color.equals("Green")){
			graphics2D.setPaint(Color.green);
			repaint();	
		}
		if(color.equals("Red")){
			graphics2D.setPaint(Color.red);
			repaint();	
		}
		if(color.equals("Black")){
			graphics2D.setPaint(Color.black);
			repaint();	
		}
		if(color.equals("Blue")){
			graphics2D.setPaint(Color.blue);
			repaint();	
		}	
	}
	public void setCheck(boolean selected){
		if(selected){
			fillRect = true;
			fillOval = true;
		}
		if(selected == false){
			fillRect = false;
			fillOval = false;
		}
		
	}
	// Paint component to draw the shapes 
	public void paintComponent(Graphics g){
		if(image == null){
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D)image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();

		}
		// draws new shape
		g.drawImage(image, 0, 0, null);
	}
	// Clear method
	public void clear(){
		graphics2D.setPaint(Color.white);
		// resets entire window to default
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		// sets the paint color to black after clearing
		graphics2D.setPaint(Color.black);
		// calls repaint
		repaint();
	}
	


}
