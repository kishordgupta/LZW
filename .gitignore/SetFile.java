/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kishor.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import kishor.lzw.Encoder;

/**
 *
 * @author Kishor
 */
public class SetFile {
  
    
    
    public static boolean setinputfile(File file)
    {
        try {
        
            
            Data.in = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
            Data.filename = file.getAbsolutePath();
            double bytes = file.length();
	     Data.kilobytes = (bytes / 1024);
            Data.filesize=file.getName();
            return setoutputfile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
      
    }
     public static boolean setoutputfile()
    {
        try {
           
            String outputFile = Data.filename+".gupta";
             Data.out = new BufferedOutputStream(new FileOutputStream(outputFile));
             if(Data.out==null)
                 return false;
             //  Encoding.compress();
             Encoder e =new Encoder();
             Encoder.compress();
             File f= new File(outputFile);
             double bytes = f.length();
             double kilobytes = (bytes / 1024);
             double ratio =kilobytes/Data.kilobytes;
             Data.history= Data.history+"\n"+ Data.filesize+":  "+ Data.kilobytes +" - >  "+kilobytes + " :  Ratio :"+ratio;
            return true;
          
               
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
