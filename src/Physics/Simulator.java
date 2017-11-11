package Physics;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * @author Siggy
 *         $
 */
public class Simulator implements ChangeListener, ActionListener
{
    private static final String K_TITLE = "Gravity";

    double rotation;
    double mass;

    public Simulator()
    {
        SwingUtilities.invokeLater(() ->
        {
            //init Frame
            JFrame frame = new JFrame(K_TITLE);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

            Content content = new Content();

            JSlider slider = new JSlider(SwingConstants.HORIZONTAL, -90, 90, 0);
            slider.setMinorTickSpacing(15);
            slider.setMajorTickSpacing(30);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setVisible(true);
            slider.addChangeListener(e ->
            {
                JSlider s = (JSlider) e.getSource();
                rotation = s.getValue();

                content.draw((int) rotation, mass);
            });

            NumberFormat number = NumberFormat.getNumberInstance();
            NumberFormatter formatter = new NumberFormatter(number);
            formatter.setValueClass(Integer.class);
            formatter.setMinimum(-90);
            formatter.setMaximum(90);

            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JFormattedTextField angleInput = new JFormattedTextField(formatter);
            angleInput.setPreferredSize(new Dimension(50, 20));
            angleInput.setHorizontalAlignment(JFormattedTextField.LEFT);
            angleInput.addPropertyChangeListener(e ->
            {
                String t = angleInput.getText();
                if(t.length() >= 1)
                {
                    int r = Integer.parseInt(t);
                    rotation = r;
                    content.draw((int) rotation, mass);
                    slider.setValue((int) rotation);

                }
            });
            JLabel angleLabel = new JLabel("Angle:", JLabel.LEFT);


            JButton reset = new JButton("Zero");
            reset.addActionListener(e ->
            {
                rotation = 0;
                content.draw((int) rotation, mass);
                slider.setValue((int) rotation);
            });
            reset.setAlignmentX(Component.CENTER_ALIGNMENT);

            JFormattedTextField massInput = new JFormattedTextField(formatter);
            massInput.setHorizontalAlignment(JFormattedTextField.LEFT);
            massInput.setPreferredSize(new Dimension(50, 20));

            JLabel massLabel = new JLabel("Mass:", JLabel.LEFT);
            massInput.addPropertyChangeListener(e ->
            {
                String t = massInput.getText();
                if(t.length() >= 1)
                {
                    int m = Integer.parseInt(t);
                    mass = m;
                    content.draw((int) rotation, mass);
                }
            });

            frame.getContentPane().add(content);
            frame.getContentPane().add(slider);
            frame.getContentPane().add(reset);
            wrapper.add(angleLabel);
            wrapper.add(angleInput);
            wrapper.add(massLabel);
            wrapper.add(massInput);
            frame.getContentPane().add(wrapper);

            frame.pack();
            frame.setVisible(true);
        });
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {


    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }
}
