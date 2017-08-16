/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kishor.lzw;



import java.io.*;
import kishor.model.Data;
/**
 *
 * @author Kishor
 */
public class Encoder {
	
	static final int BITS 			= 	12;
	static final int HASHING_SHIFT = 	4;
	static final int MAX_VALUE 	= 	(1 << BITS ) - 1;//1*2^12-1=4095
	static final int MAX_CODE 		= 	MAX_VALUE - 1;
	static final int TABLE_SIZE 	= 	5021;
	static final int EOF			= 	-1;
	
	static BufferedInputStream  inputFile  = null;
	static BufferedOutputStream outputFile = null;
	
	private static int output_bit_count  = 0;
	private static int output_bit_buffer = 0;
	
	static short[] code_value  		= new short[ TABLE_SIZE ];
	static short[] prefix_code 		= new short[ TABLE_SIZE ];
	static short[] append_character 	= new short[ TABLE_SIZE ];


	

	
	public static  void compress()
	{inputFile=Data.in;
         outputFile=Data.out;
		short next_code   = 0;
		short character   = 0;
		short string_code ;
		short index 	  = 0;
		
		next_code = 256;              			  //Next code is the next available string code
		
		for ( short i = 0; i < TABLE_SIZE; i++ )  //Clear out the string table before starting
			code_value[ i ] = -1;
	
		try
		{
			string_code = ( short ) inputFile.read(); // Get the first character. Assuming it to be 0 - 255
			 									 
		
			
			/*This is the main loop runs until all of the input has been read. It stops adding codes to the table 
			after all of the possible codes have been defined.*/
			while ( ( character = ( short ) inputFile.read() ) != EOF )
			{
				index = find_match ( string_code, character );	// See if the string is in 
				
				if ( code_value[ index ] != -1 )
			    {            									// the table.  If it is,   
					string_code = code_value[ index ];          // get the code value.  If 
			    }
			    else                                    		// the string is not in the
			    {                                       		// table, try to add it.   
			    	if ( next_code <= MAX_CODE )
			    	{
					    code_value		[ index ] = next_code++;
					    prefix_code		[ index ] = string_code;
					    append_character[ index ] = character;
			    	}
			    	
			        output_code( string_code );  				// When a string is found  
			    	string_code = character;           			// that is not in the table 
			    }												// It outputs the last string 
			}                                     				// after adding the new one 
			
			
			output_code( string_code ); 						// Output the last code               
			output_code( ( short ) MAX_VALUE );   				// Output the end of buffer code      
			output_code( ( short ) 0 );           				// This code flushes the output buffer
			
			//Close the files
			
			outputFile.close();
			inputFile.close();
		}
		catch ( IOException ioe )
		{
			System.out.println( "IOException in compress()" );
			System.exit( 1 );
		}
	}
	
	/* This is the hashing routine. It tries to find a match for the prefix + char string in the string table.
		If it finds it, the index is returned. If the string is not found, the first available index in the
		string table is returned instead.*/
	
	private static short find_match ( short hash_prefix, short hash_character )
	{
		int index  = 0;
		int offset = 0;
	
		index = ( hash_character << HASHING_SHIFT ) ^ hash_prefix; /// 
		  
		if ( index == 0 )
			offset = 1;
		else
			offset = TABLE_SIZE - index;
		
		while ( true )
		{
			if ( code_value[ index ] == -1 )
				return ( short ) index;
			if ( prefix_code[ index ] == hash_prefix && append_character[ index ] == hash_character )
				return ( short ) index;
		      
			index -= offset;
		    
			if ( index < 0 )
				index += TABLE_SIZE;
		}
	}
	
	private static void output_code( short code )
	{
		output_bit_buffer |= code << ( 32 - BITS - output_bit_count );
		output_bit_count += BITS;
		
		while ( output_bit_count >= 8 )
		{
			try
			{
				outputFile.write( output_bit_buffer >> 24 );
			}
			catch( IOException io )
			{
				System.out.println( "IOException in output_code()" );
				System.exit( 1 );
			}
			output_bit_buffer <<= 8;
			output_bit_count -= 8;
		}
	}
	
}
