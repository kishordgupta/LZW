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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kishor.lzw.Decoder;

/**
 *
 * @author Kishor
 */
public class SetFileDecode {
  
    
    
    public static boolean setinputfile(File file)
    {
        try {
        
            
            Data.in = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
            Data.filename = file.getAbsolutePath();
              Data.filesize=file.getName();
            return setoutputfile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetFileDecode.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
      
    }
     public static boolean setoutputfile()
    {
        try {
           
            String outputFile = Data.filename.replace(".gupta","");
            outputFile = outputFile.replace(".","_decoded.");
             Data.out = new BufferedOutputStream(new FileOutputStream(outputFile));
             if(Data.out==null)
                 return false;
            try {
                Decoder de = new Decoder();
                Decoder.decompress();
                    Data.history= Data.history+"\n"+ Data.filesize + "Decoded";
            } catch (IOException ex) {
                Logger.getLogger(SetFileDecode.class.getName()).log(Level.SEVERE, null, ex);
                 return false;
            }
            return true;
          
               
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SetFileDecode.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
