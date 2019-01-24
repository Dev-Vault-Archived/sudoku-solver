/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author askaeks
 */
final class DisabledGlassPane extends JComponent
     implements KeyListener
{
     private final static Color DEFAULT_BACKGROUND = new Color(210, 210, 210, 210);
     private final static Border MESSAGE_BORDER = new EmptyBorder(10, 10, 10, 10);

     private final JLabel message = new JLabel();

     public DisabledGlassPane()
     {
          initialize();
     }
     
     protected void initialize() {
         setOpaque( false );
          setBackground( DEFAULT_BACKGROUND );
          setLayout( new GridBagLayout() );
          add(message, new GridBagConstraints());

          message.setOpaque(true);
          message.setBorder(MESSAGE_BORDER);

          addMouseListener( new MouseAdapter() {} );
          addMouseMotionListener( new MouseMotionAdapter() {} );

          addKeyListener( this );

        setFocusTraversalKeys(
            KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET );
        setFocusTraversalKeys(
            KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET );
     }

     @Override
     protected void paintComponent(Graphics g)
     {
          g.setColor( getBackground() );
          g.fillRect(0, 0, getSize().width, getSize().height);
     }

     @Override
     public void setBackground(Color background)
     {
          super.setBackground( background );

          Color messageBackground =
               new Color(background.getRed(), background.getGreen(), background.getBlue());
          message.setBackground( messageBackground );
     }

     @Override
     public void keyPressed(KeyEvent e)
     {
          e.consume();
     }

     @Override
     public void keyTyped(KeyEvent e)
     {
          e.consume();
     }
     @Override
     public void keyReleased(KeyEvent e)
     {
          e.consume();
     }

     public void activate(String text)
     {
          if  (text != null && text.length() > 0)
          {
               message.setVisible( true );
               message.setText( text );
               message.setForeground( getForeground() );
          }
          else
               message.setVisible( false );

          setVisible( true );
          setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          requestFocusInWindow();
     }

     public void deactivate()
     {
          setCursor(null);
          setVisible( false );
     }
}