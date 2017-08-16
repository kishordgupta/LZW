/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kishor.lzw;


import java.io.*;
import java.util.*;
import kishor.model.Data;
import kishor.model.Element;
/**
 *
 * @author Kishor
 */
public class Decoder{
	

    final static int MAX_CODES = 4096;
    final static int BYTE_SIZE = 8;
    final static int EXCESS = 4;
    final static int Limit= 256;
    final static int MASK = 15;

    static int [] suffix;
    static int size;
    static Element [] gupta;// =new Element[MAX_CODES];
    static int leftOver;
    static boolean bitsLeftOver;
  public  static BufferedInputStream in;
   public static BufferedOutputStream out;

 

    private static void output(int code)throws IOException{
        size = -1;
        while(code>=Limit){
            suffix[++size]=gupta[code].suffix;
            code = gupta[code].prefix;
        }
        suffix[++size]=code;
        for(int i=size; i>=0; i--)
            out.write(suffix[i]);
    }

    private static int getCode() throws IOException{
        int c = in.read();
        if(c == -1)return -1;

        int code;
        if(bitsLeftOver)
            code = (leftOver<<BYTE_SIZE)+c;
        else{
            int d = in.read();
            code = (c<<EXCESS)+(d>>EXCESS);
            leftOver = d&MASK;
        }
        bitsLeftOver = !bitsLeftOver;
        return code;
    }

    public static void decompress() throws IOException{
       in=Data.in;
         out=Data.out;
        int codeUsed = Limit;
        suffix = new int[MAX_CODES];
        gupta = new Element[MAX_CODES];

        int pcode = getCode(), ccode;
        if(pcode>=0){
            suffix[0] = pcode;
            out.write(suffix[0]);
            size = 0;

            do{
                ccode = getCode();
                if(ccode<0)break;
                if(ccode<codeUsed){
                    output(ccode);
                    if(codeUsed<MAX_CODES)
                        gupta[codeUsed++] = new Element(pcode, suffix[size]);
                }
                else{
                    gupta[codeUsed++] = new Element(pcode, suffix[size]);
                    output(ccode);
                }
                pcode = ccode;
            }while(true);
        }
        out.close();
        in.close();
    }

 
}
