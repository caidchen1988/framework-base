package com.zwt.framework.utils.util.dsstore;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Author: zwt
 * @Description: Class for a basic DataBlock inside of the DS_Store format.
 * @Name: DataBlock
 * @Date: 2019/7/27 3:11 PM
 * @Version: 1.0
 */
public class DataBlock {
    private byte[] data;
    private int pos;
    private boolean debug;

    /**
     * Returns an byte array of length from data at the given offset or pos.
     * If offset==0 (no offset is given) , pos will be increased by length.
     * Throws Exception if offset+length > this.data.length
     * @Params: [length, offset]
     * @Return: byte[]
     */
    public byte[] offsetRead(int length,int offset){
        int offsetPosition;
        if(offset==0){
            offsetPosition = this.pos;
        }else{
            offsetPosition = offset;
        }
        if(this.data.length < offsetPosition +length){
            throw new RuntimeException("Offset+Length > this.data.length");
        }
        if(offset==0){
            this.pos+=length;
        }
        byte[] value =new byte[length];
        System.arraycopy(this.data,offsetPosition,value,0,length);
        if(debug){
            System.out.println(String.format("Reading: %s-%s => %s",offsetPosition, offsetPosition+length, value));
        }
        return value;
    }

    /**
     * Increases pos by length without reading data!
     * @Params: [length]
     * @Return: void
     */
    public void skip(int length){
        this.pos+=length;
    }

    /**
     * Extracts a file name from the current position.
     * @Params: []
     * @Return: java.lang.String
     */
    public String readFileName(){
        //The length of the file name in bytes.
        int length = ByteBuffer.wrap(offsetRead(4,0)).getInt();
        //The file name in UTF-16, which is two bytes per character.
        String fileName = new String(offsetRead(2 * length,0), StandardCharsets.UTF_16BE);
        //A structure ID that I haven't found any use of.
        int structureId = ByteBuffer.wrap(offsetRead(4,0)).getInt();
        //Now read the structure type as a string of four characters and decode it to ascii.
        String structureType = new String(offsetRead(4,0), StandardCharsets.US_ASCII);
        if(debug){
            System.out.println("Structure type "+ structureType);
        }
        //If we don't find a match, skip stays < 0 and we will do some magic to find the right skip due to somehow broken .DS_Store files..
        int skip = -1;
        //Source: http://search.cpan.org/~wiml/Mac-Finder-DSStore/DSStoreFormat.pod
        while (skip < 0){
            if(structureType.equals("bool")){
                skip = 1;
            }else if(structureType.equals("type") || structureType.equals("long")  || structureType.equals("shor")
            || structureType.equals("fwsw") || structureType.equals("fwvh") || structureType.equals("icvt")
            || structureType.equals("lsvt") || structureType.equals("vSrn") || structureType.equals("vstl")){
                skip = 4;
            }else if(structureType.equals("comp") || structureType.equals("dutc") || structureType.equals("icgo")
            || structureType.equals("icsp") || structureType.equals("logS") || structureType.equals("lg1S")
            || structureType.equals("lssp") || structureType.equals("modD") || structureType.equals("moDD")
            || structureType.equals("phyS") || structureType.equals("ph1S")){
                skip = 8;
            }else if(structureType.equals("blob")){
                skip = ByteBuffer.wrap(offsetRead(4,0)).getInt();
            }else if(structureType.equals("ustr") || structureType.equals("cmmt") || structureType.equals("extn")
            || structureType.equals("GRP0")){
                skip = 2 * ByteBuffer.wrap(offsetRead(4,0)).getInt();
            }else if(structureType.equals("BKGD")){
                skip = 12;
            }else if(structureType.equals("ICVO") || structureType.equals("LSVO") || structureType.equals("dscl")){
                skip = 1;
            }else if(structureType.equals("Iloc") || structureType.equals("fwi0")){
                skip = 16;
            }else if(structureType.equals("dilc")){
                skip = 32;
            }else if(structureType.equals("lsvo")){
                skip = 76;
            }else if(structureType.equals("icvo")){

            }else if(structureType.equals("info")){

            }else {

            }
            if(skip <= 0){
                //We somehow didn't find a matching type. Maybe this file name's length value is broken. Try to fix it!
                //This is a bit voodoo and probably not the nicest way. Beware, there by dragons!
                if(debug){
                    System.out.println("Re-reading!");
                }
                // Rewind 8 bytes, so that we can re-read structure_id and structure_type
                skip(-1 * 2 * 0x4);
                fileName = new String(offsetRead(0x2,0), StandardCharsets.UTF_16BE);
                //re-read structure_id and structure_type
                structureId = ByteBuffer.wrap(offsetRead(4,0)).getInt();
                structureType = new String(offsetRead(4,0), StandardCharsets.US_ASCII);
                //Look-ahead and check if we have  structure_type==Iloc followed by blob.
                //If so, we're interested in blob, not Iloc. Otherwise continue!
                String futureStructureType = new String(offsetRead(4,this.pos), StandardCharsets.US_ASCII);
                if(debug){
                    System.out.println(String.format("Re-read structure_id %s / structure_type %s",structureId, structureType));
                }
                if ((!structureType.equals("blob")) && (!futureStructureType.equals("blob"))){
                    structureType = "";
                    if(debug){
                        System.out.println("Forcing another round!");
                    }
                }
            }
        }
        // Skip bytes until the next (file name) block
        skip(skip);
        if(debug){
            System.out.println(String.format("Filename %s",fileName));
        }
        return fileName;
    }

    public DataBlock() {
    }

    public DataBlock(byte[] data, int pos, boolean debug) {
        this.data = data;
        this.pos = pos;
        this.debug = debug;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
