package com.zwt.framework.utils.util.dsstore;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zwt
 * @Description: Represents the .DS_Store file from the given binary data.
 * @Name: DS_Store
 * @Date: 2019/7/27 4:14 PM
 * @Version: 1.0
 */
public class DS_Store extends DataBlock{
    private byte[] data;
    private boolean debug;

    private DataBlock root;
    private List<Integer> offsets;
    private Map<String,Integer> toc;
    private Map<Integer,List<Integer>> freeList;

    /**
     * Constructor of DS_Store
     * @Params: [data, debug]
     * @Return:
     */
    public DS_Store(byte[] data, boolean debug) {
        super(data,0,debug);
        this.data = data;
        this.debug = debug;
        this.data = data;
        this.debug = debug;
        this.root = readHeader();
        this.offsets = readOffsets();
        this.toc = readTOC();
        this.freeList = readFreelist();
    }

    /**
     * Checks if self.data is actually a .DS_Store file by checking the magic bytes.
     * It returns the file's root block.
     * @Params: []
     * @Return: com.zwt.framework.utils.util.dsstore.DataBlock
     */
    private DataBlock readHeader(){

        // We read at least 32+4 bytes for the header!
        if (this.data.length < 36){
            throw new RuntimeException("Length of data is too short!");
        }
        // Check the magic bytes for .DS_Store
        int magic1 = ByteBuffer.wrap(this.offsetRead(4,0)).getInt();
        int magic2 = ByteBuffer.wrap(this.offsetRead(4,0)).getInt();
        if(magic1 != 0x1 && magic2 != 0x42756431){
            throw new RuntimeException("Magic byte 1 does not match!");
        }
        // After the magic bytes, the offset follows two times with block's size in between.
        // Both offsets have to match and are the starting point of the root block
        int offset = ByteBuffer.wrap(this.offsetRead(4,0)).getInt();
        int size = ByteBuffer.wrap(this.offsetRead(4,0)).getInt();
        int offset2 = ByteBuffer.wrap(this.offsetRead(4,0)).getInt();
        if(debug){
            System.out.println(String.format("Offset 1: %s",offset));
            System.out.println(String.format("Size: %s",size));
            System.out.println(String.format("Offset 2: %s",offset2));
        }
        if(offset!=offset2){
            throw new RuntimeException("Offsets do not match!");
        }
        //Skip 16 bytes of unknown data...
        skip(4*4);

        return new DataBlock(this.offsetRead(size, offset+4),0, this.debug);
    }

    /**
     * Reads the offsets which follow the header
     * @Params: []
     * @Return: java.util.List<java.lang.Integer>
     */
    private List<Integer> readOffsets(){

        int startPos = this.root.getPos();
        // First get the number of offsets in this file.
        int count = ByteBuffer.wrap(this.root.offsetRead(4,0)).getInt();
        if(debug){
            System.out.println(String.format("Offset count: %s",count));
        }
        // Always appears to be zero!
        this.root.skip(4);

        // Iterate over the offsets and get the offset addresses.
        List<Integer> offsets = new ArrayList<>();
        for(int i=0;i<count;i++){
            // Address of the offset.
            int address = ByteBuffer.wrap(this.root.offsetRead(4,0)).getInt();
            if(debug){
                System.out.println(String.format("Offset %s is %s",i, address));
            }
            if (address == 0){
                // We're only interested in non-zero values
                continue;
            }
            offsets.add(address);
        }

        // Calculate the end of the address space (filled with zeroes) instead of dumbly reading zero values...
        int sectionEnd = startPos + (count / 256 + 1) * 256 * 4 - count*4;

        // Skip to the end of the section
        this.root.skip(sectionEnd);
        if(debug){
            System.out.println(String.format("Skipped %s to %s",(this.root.getPos() + sectionEnd),this.root.getPos()));
            System.out.println(String.format("Offsets: %s",offsets));
        }
        return offsets;
    }

    /**
     * Reads the table of contents (TOCs) from the file.
     * @Params: []
     * @Return: java.util.Map<java.lang.String,java.lang.Integer>
     */
    private Map<String,Integer> readTOC(){
        if(debug){
            System.out.println(String.format("POS %s",this.root.getPos()));
        }
        // First get the number of ToC entries.
        int count = ByteBuffer.wrap(this.root.offsetRead(4,0)).getInt();
        if(debug){
            System.out.println(String.format("Toc count: %s",count));
        }

        Map<String,Integer> toc = new HashMap<>();
        // Iterate over all ToCs
        for(int i=0;i<count;i++){
            // Get the length of a ToC's name
            int tocLen = this.root.offsetRead(1,0)[0];
            // Read the ToC's name
            String tocName = new String(this.root.offsetRead(tocLen,0), StandardCharsets.UTF_8);
            // Read the address (block id) in the data section
            int blockId = ByteBuffer.wrap(this.root.offsetRead(4,0)).getInt();
            // Add all values to the dictionary
            toc.put(tocName,blockId);
        }
        if(debug){
            System.out.println(String.format("Toc %s",toc));
        }
        return toc;
    }

    /**
     * Read the free list from the header.
     * The free list has n=0..31 buckets with the index 2^n
     * @Params: []
     * @Return: java.util.Map<java.lang.Integer,java.util.List<java.lang.Integer>>
     */
    private Map<Integer,List<Integer>> readFreelist(){
        Map<Integer,List<Integer>> freelist = new HashMap<>();
        for(int i=0;i<32;i++){
            freelist.put(1<<i,new ArrayList<>());
            // Read the amount of blocks in the specific free list.
            int blkcount = ByteBuffer.wrap(this.root.offsetRead(4,0)).getInt();
            for(int j=0;j<blkcount;j++){
                // Read blkcount block offsets.
                int freeOffset = ByteBuffer.wrap(this.root.offsetRead(4,0)).getInt();
                freelist.get(1<<i).add(freeOffset);
            }
        }
        if(debug){
            System.out.println(String.format("Freelist: %s",freelist));
        }
        return freelist;
    }


    /**
     * Create a DataBlock from a given block ID (e.g. from the ToC)
     * @Params: [blockId]
     * @Return: com.zwt.framework.utils.util.dsstore.DataBlock
     */
    public DataBlock blockById(int blockId){
        // First check if the block_id is within the offsets range
        if(this.offsets.size() < blockId){
            throw new RuntimeException("BlockID out of range!");
        }

        // Get the address of the block
        int addr = this.offsets.get(blockId);

        // Do some necessary bit operations to extract the offset and the size of the block.
        // The address without the last 5 bits is the offset in the file
        int offset = addr >> 0x5 << 0x5;
        //The address' last five bits are the block's size.
        int size = 1 << (addr & 0x1f);
        if(debug){
            System.out.println(String.format("New block: addr %s offset %s size %s",addr, offset + 0x4, size));
        }
        // Return the new block
        return new DataBlock(this.offsetRead(size, offset + 0x4),0, this.debug);
    }

    /**
     * Traverses a block identified by the given block_id and extracts the file names.
     * @Params: [blockId]
     * @Return: java.util.List<java.lang.String>
     */
    public List<String> traverse(int blockId){
        // Get the responsible block by it's ID
        DataBlock node = this.blockById(blockId);
        // Extract the pointer to the next block
        int nextPointer =  ByteBuffer.wrap(node.offsetRead(4,0)).getInt();
        // Get the number of next blocks or records
        int count =  ByteBuffer.wrap(node.offsetRead(4,0)).getInt();
        if(debug){
            System.out.println(String.format("Next Ptr %s with %s ",nextPointer,count));
        }
        List<String> filenames =new ArrayList<>();

        // If a next_pointer exists (>0), iterate through the next blocks recursively
        // If not, we extract all file names from the current block
        if(nextPointer > 0){
            for(int i=0;i<count;i++){
                // Get the block_id for the next block
                int nextId =  ByteBuffer.wrap(node.offsetRead(4,0)).getInt();
                if(debug){
                    System.out.println(String.format("Child: %s",nextId));
                }
                // Traverse it recursively
                List<String>  files = this.traverse(nextId);
                filenames.addAll(files);
                // Also get the filename for the current block.
                String filename = node.readFileName();
                if(debug){
                    System.out.println(String.format("Filename: %s", filename));
                }
                filenames.add(filename);
            }
            // Now that we traversed all childs of the next_pointer, traverse the pointer itself.
            // TODO: Check if that is really necessary as the last child should be the current node... (or so?)
            List<String> files = this.traverse(nextPointer);
            filenames.addAll(files);
        }else{
            // We're probably in a leaf node, so extract the file names.
            for(int i=0;i<count;i++){
                String f = node.readFileName();
                filenames.add(f);
            }
        }
        return filenames;
    }


    /**
     * Traverse from the root block and extract all file names.
     * @Params: []
     * @Return: java.util.List<java.lang.String>
     */
    public List<String> traverseRoot(){
        // Get the root block from the ToC 'DSDB'
        DataBlock root = this.blockById(this.toc.get("DSDB"));
        // Read the following root block's ID, so that we can traverse it.
        int rootId =  ByteBuffer.wrap(root.offsetRead(4,0)).getInt();
        if(debug){
            System.out.println(String.format("Root-ID %s", rootId));
        }
        // Read other values that we might be useful, but we're not interested in... (at least right now)
        int internalBlockCount =  ByteBuffer.wrap(root.offsetRead(4,0)).getInt();
        int recordCount =  ByteBuffer.wrap(root.offsetRead(4,0)).getInt();
        int blockCount =  ByteBuffer.wrap(root.offsetRead(4,0)).getInt();
        int unknown =  ByteBuffer.wrap(root.offsetRead(4,0)).getInt();
        // traverse from the extracted root block id.
        return this.traverse(rootId);
    }


    public DS_Store() {
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
