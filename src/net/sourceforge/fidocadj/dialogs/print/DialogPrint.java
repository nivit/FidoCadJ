package net.sourceforge.fidocadj.dialogs.print;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.event.*;

import java.io.*;
import java.awt.print.*;

import javax.imageio.*;

import net.sourceforge.fidocadj.globals.*;
import net.sourceforge.fidocadj.dialogs.mindimdialog.MinimumSizeDialog;
import net.sourceforge.fidocadj.dialogs.*;
import net.sourceforge.fidocadj.circuit.model.DrawingModel;

/** Choose file format, size and options of the graphic exporting.
    The class dialogPrint implements a modal dialog to select printing options.

<pre>
    This file is part of FidoCadJ.

    FidoCadJ is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FidoCadJ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FidoCadJ.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2007-2015 by Davide Bucci
</pre>

    @author Davide Bucci
*/

public class DialogPrint extends MinimumSizeDialog
{
    private final JCheckBox mirror_CB;
    private final JCheckBox fit_CB;
    private final JCheckBox bw_CB;
    private final JCheckBox landscape_CB;

    private final JTextField tTopMargin;
    private final JTextField tBottomMargin;
    private final JTextField tLeftMargin;
    private final JTextField tRightMargin;

    private double maxHorisontalMargin;
    private double maxVerticalMargin;

    private boolean marginsSet=false;
    private boolean oldLandscapeState=false;

    private DrawingModel drawingModel;

    private boolean print;     // Indicates that the print should be done
    /** Standard constructor: it needs the parent frame.
        @param parent the dialog's parent.
        @param dd the drawing model to be employed.
        @param pageDescription the description of the page to be printed.
    */
    public DialogPrint (JFrame parent, DrawingModel dd,
        PageFormat pageDescription)
    {
        super(400,350, parent,Globals.messages.getString("Print_dlg"), true);
        addComponentListener(this);
        print=false;
        drawingModel=dd;

        // Ensure that under MacOSX >= 10.5 Leopard, this dialog will appear
        // as a document modal sheet

        getRootPane().putClientProperty("apple.awt.documentModalSheet",
                Boolean.TRUE);

        GridBagLayout bgl=new GridBagLayout();
        GridBagConstraints constraints=new GridBagConstraints();
        Container contentPane=getContentPane();
        contentPane.setLayout(bgl);

        constraints.insets.right=30;

        JLabel empty=new JLabel("  ");
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(empty, constraints);            // Add "   " label

        JLabel empty1=new JLabel("  ");
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=4;
        constraints.gridy=0;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(empty1, constraints);           // Add "   " label

        PrintPreview prp=new PrintPreview(false, pageDescription, this);
        prp.add(Box.createVerticalStrut(256));
        prp.add(Box.createHorizontalStrut(256));

        prp.setDrawingModel(drawingModel);

        constraints.anchor=GridBagConstraints.EAST;
        constraints.fill=GridBagConstraints.BOTH;
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=1;
        constraints.gridy=0;
        constraints.gridwidth=1;
        constraints.gridheight=8;
        contentPane.add(prp, constraints);              // Print preview!

        JLabel lTopMargin=new JLabel(Globals.messages.getString("TopMargin"));
        constraints.anchor=GridBagConstraints.WEST;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=3;
        constraints.gridy=0;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(lTopMargin, constraints);           // Top margin label

        DocumentListener dl=new DocumentListener() {
            public void changedUpdate(DocumentEvent e)
            {
                marginsSet=true;
            }

            public void removeUpdate(DocumentEvent e)
            {
            }

            public void insertUpdate(DocumentEvent e)
            {
            }
        };

        tTopMargin=new JTextField(10);
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=2;
        constraints.gridy=0;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(tTopMargin, constraints);           // Top margin text
        tTopMargin.getDocument().addDocumentListener(dl);

        JLabel lBottomMargin=new JLabel(
            Globals.messages.getString("BottomMargin"));
        constraints.anchor=GridBagConstraints.WEST;
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=3;
        constraints.gridy=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(lBottomMargin, constraints);    // Bottom margin label

        tBottomMargin=new JTextField(10);
        Dimension ddmin=tBottomMargin.getMinimumSize();
        ddmin.width=100;
        tBottomMargin.setMinimumSize(ddmin);
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=2;
        constraints.gridy=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(tBottomMargin, constraints);    // Bottom margin text
        tBottomMargin.getDocument().addDocumentListener(dl);

        JLabel lLeftMargin=new JLabel(Globals.messages.getString("LeftMargin"));
        constraints.anchor=GridBagConstraints.WEST;
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=3;
        constraints.gridy=2;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(lLeftMargin, constraints);    // Left margin label

        tLeftMargin=new JTextField(10);
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=2;
        constraints.gridy=2;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(tLeftMargin, constraints);    // Left margin text
        tLeftMargin.getDocument().addDocumentListener(dl);

        JLabel lRightMargin=new JLabel(
            Globals.messages.getString("RightMargin"));
        constraints.anchor=GridBagConstraints.WEST;
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=3;
        constraints.gridy=3;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(lRightMargin, constraints);     // Right margin label

        tRightMargin=new JTextField(10);
        constraints.weightx=100;
        constraints.weighty=100;
        constraints.gridx=2;
        constraints.gridy=3;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        contentPane.add(tRightMargin, constraints);    // Right margin text
        tRightMargin.getDocument().addDocumentListener(dl);

        mirror_CB=new JCheckBox(Globals.messages.getString("Mirror"));
        constraints.gridx=2;
        constraints.gridy=4;
        constraints.gridwidth=2;
        constraints.gridheight=1;
        constraints.anchor=GridBagConstraints.WEST;
        contentPane.add(mirror_CB, constraints);        // Add Print Mirror cb

        fit_CB=new JCheckBox(Globals.messages.getString("FitPage"));
        constraints.gridx=2;
        constraints.gridy=5;
        constraints.gridwidth=2;
        constraints.gridheight=1;
        constraints.anchor=GridBagConstraints.WEST;
        contentPane.add(fit_CB, constraints);       // Add Fit to page cb

        bw_CB=new JCheckBox(Globals.messages.getString("B_W"));
        constraints.gridx=2;
        constraints.gridy=6;
        constraints.gridwidth=2;
        constraints.gridheight=1;
        constraints.anchor=GridBagConstraints.WEST;
        contentPane.add(bw_CB, constraints);        // Add BlackWhite cb

        landscape_CB=new JCheckBox(Globals.messages.getString("Landscape"));
        constraints.gridx=2;
        constraints.gridy=7;
        constraints.gridwidth=2;
        constraints.gridheight=1;
        constraints.anchor=GridBagConstraints.WEST;
        contentPane.add(landscape_CB, constraints);     // Add landscape cb

        landscape_CB.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent)
            {
                // If the page was rotated, the margins should be adapted
                // so they always correspond to the same place in the page.
                if(!marginsSet) {
                    if(!oldLandscapeState && landscape_CB.isSelected()) {
                        String d=tLeftMargin.getText();
                        tLeftMargin.setText(tTopMargin.getText());
                        tTopMargin.setText(tRightMargin.getText());
                        tRightMargin.setText(tBottomMargin.getText());
                        tBottomMargin.setText(d);
                    } else if(oldLandscapeState && !landscape_CB.isSelected()){
                        String d=tTopMargin.getText();
                        tTopMargin.setText(tLeftMargin.getText());
                        tLeftMargin.setText(tBottomMargin.getText());
                        tBottomMargin.setText(tRightMargin.getText());
                        tRightMargin.setText(d);
                    }
                }
                oldLandscapeState=landscape_CB.isSelected();
            }
        });

        // Put the OK and Cancel buttons and make them active.
        JButton ok=new JButton(Globals.messages.getString("Ok_btn"));
        JButton cancel=new JButton(Globals.messages.getString("Cancel_btn"));

        constraints.gridx=1;
        constraints.gridy=8;
        constraints.gridwidth=4;
        constraints.gridheight=1;
        constraints.anchor=GridBagConstraints.EAST;

        // Put the OK and Cancel buttons and make them active.
        Box b=Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        ok.setPreferredSize(cancel.getPreferredSize());

        if (Globals.okCancelWinOrder) {
            b.add(ok);
            b.add(Box.createHorizontalStrut(12));
            b.add(cancel);

        } else {
            b.add(cancel);
            b.add(Box.createHorizontalStrut(12));
            b.add(ok);
        }

        ok.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                if(validateInput()) {
                    print=true;
                    setVisible(false);
                }
            }
        });
        cancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                print=false;
                setVisible(false);
            }
        });
        // Here is an action in which the dialog is closed

        AbstractAction cancelAction = new AbstractAction ()
        {
            public void actionPerformed (ActionEvent e)
            {
                setVisible(false);
            }
        };
        contentPane.add(b, constraints);        // Add OK/cancel dialog

        DialogUtil.addCancelEscape (this, cancelAction);
        pack();
        DialogUtil.center(this);
        getRootPane().setDefaultButton(ok);
    }

    /** Check if the drawing should be mirrored.
        @return true wether the mirroring should be done.
    */
    public boolean getMirror()
    {
        return mirror_CB.isSelected();
    }

    /** Check if the drawing should be fit to the page
        @return true wether the fitting should be done.
    */
    public boolean getFit()
    {
        return fit_CB.isSelected();
    }
    /** Check if the page orientation should be landscape
        @return true wether the orientation is landscape.
    */
    public boolean getLandscape()
    {
        return landscape_CB.isSelected();
    }

    /** Check if the black and white checkbox is selected.
        @return true if the checkbox is active.
    */
    public boolean getBW()
    {
        return bw_CB.isSelected();
    }

    /** Set the mirror attribute
        @param m true if the printout should be done in mirroring mode.
    */
    public void setMirror(boolean m)
    {
        mirror_CB.setSelected(m);
    }

    /** Set the resize to fit option
        @param f true if the drawing should be stretched in order to fit the
            page.
    */
    public void setFit(boolean f)
    {
        fit_CB.setSelected(f);
    }

    /** Set the landscape mode
        @param l true if the output should be in landscape mode. It will be
            in portrait orientation otherwise.
    */
    public void setLandscape(boolean l)
    {
        landscape_CB.setSelected(l);
        oldLandscapeState=l;
    }

    /** Set the size of the margins, in centimeters. The orientation of those
        margins should correspond to the page in the portrait orientation.
        @param tm top margin.
        @param bm bottom margin.
        @param lm left margin.
        @param rm right margin.
    */
    public void setMargins(double tm, double bm, double lm, double rm)
    {
        tTopMargin.setText(Globals.roundTo(tm,3));
        tBottomMargin.setText(Globals.roundTo(bm,3));
        tLeftMargin.setText(Globals.roundTo(lm,3));
        tRightMargin.setText(Globals.roundTo(rm,3));
    }

    /** Check if the input is valid.
        @return true if the input is valid.
    */
    private boolean validateInput()
    {
        double tm;
        double bm;
        double lm;
        double rm;

        try {
            tm=Double.parseDouble(tTopMargin.getText());
            bm=Double.parseDouble(tBottomMargin.getText());
            lm=Double.parseDouble(tLeftMargin.getText());
            rm=Double.parseDouble(tRightMargin.getText());
        } catch (java.lang.NumberFormatException n) {
            JOptionPane.showMessageDialog(this,
                Globals.messages.getString("Format_invalid"), "",
                JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if(tm+bm>=maxVerticalMargin || lm+rm>=maxHorisontalMargin) {
            JOptionPane.showMessageDialog(this,
                Globals.messages.getString("Margins_too_large"), "",
                JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    /** Defines the limits for the sum of the horisontal and vertical margins.
        @param maxhor the limit of the left+right margins (in cm)
        @param maxvert the limit of the top+bottom margins (in cm)
    */
    public void setMaxMargins(double maxhor, double maxvert)
    {
        maxHorisontalMargin=maxhor;
        maxVerticalMargin=maxvert;
    }

    /** Get the size of the top margin, in centimeters.
        @return the margin size.
    */
    public double getTMargin()
    {
        return Double.parseDouble(tTopMargin.getText());
    }

    /** Get the size of the bottom margin, in centimeters.
        @return the margin size.
    */
    public double getBMargin()
    {
        return Double.parseDouble(tBottomMargin.getText());
    }

    /** Get the size of the left margin, in centimeters.
        @return the margin size.
    */
    public double getLMargin()
    {
        return Double.parseDouble(tLeftMargin.getText());
    }

    /** Get the size of the right margin, in centimeters.
        @return the margin size.
    */
    public double getRMargin()
    {
        return Double.parseDouble(tRightMargin.getText());
    }

    /** Print in black and white
        @param l if true, print in black and white, if false respect the colors
            associated to the layers.
    */
    public void setBW(boolean l)
    {
        bw_CB.setSelected(l);
    }

    /** Indicates that the printing should be done: the user selected the "ok"
        button
        @return a boolean value which indicates if the printing should be done
    */
    public boolean shouldPrint()
    {
        return print;
    }
}