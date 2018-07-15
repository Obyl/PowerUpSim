package ca.team4152.powerupsim.utils;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
This is the GUI that opens after the simulation completes, asking you where you want to save your game report.
 */
public class SaveGUI {

    public String getFileDir(){
        JFrame frame = new JFrame();

        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String value = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));

        int option = chooser.showSaveDialog(frame);
        if(option == JFileChooser.APPROVE_OPTION){
            value = chooser.getCurrentDirectory().getAbsolutePath() + "\\" +
                    chooser.getSelectedFile().getName();
        }
        if(!value.isEmpty() && !value.contains(".txt")){
            value += ".txt";
        }

        frame.dispose();
        return value;
    }

}