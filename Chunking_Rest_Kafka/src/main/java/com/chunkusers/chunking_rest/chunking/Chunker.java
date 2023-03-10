package com.chunkusers.chunking_rest.chunking;

import java.nio.ByteBuffer;

/**
 * A Chunker instance splits up a ByteBuffer into multiple chunks.
 *
 * The Chunker is initialized with an ID. For each message to be chunked,
 * a new Chunker instance is required.
 */
public class Chunker {

    private final long id;
    private final ByteBuffer buf;
    private final int chunkDataSize;
    private int chunkId;

    /**
     * Create a Chunker instance.
     *
     * @param id An identifier for the message. Must be between 0 and 2**32-1.
     * @param buf The ByteBuffer containing the data that should be chunked.
     * @param chunkSize The chunk size *including* header data.
     * @throws IllegalArgumentException if message id is negative
     * @throws IllegalArgumentException if chunk size is less than 1
     * @throws IllegalArgumentException if buffer is empty
     */
    public Chunker(long id, ByteBuffer buf, int chunkSize) {
        if (id < 0) {
            throw new IllegalArgumentException("Message id may not be negative");
        }
        if (chunkSize < (Common.HEADER_LENGTH + 1)) {
            throw new IllegalArgumentException("Chunk size must be at least " + (Common.HEADER_LENGTH + 1));
        }
        if (!buf.hasRemaining()) {
            throw new IllegalArgumentException("Buffer may not be empty");
        }
        this.id = id;
        this.buf = buf;
        this.chunkDataSize = chunkSize - Common.HEADER_LENGTH;
        this.chunkId = 0;
    }

    /**
     * Whether there are more chunks available.
     */
    public boolean hasNext() {
        return this.buf.hasRemaining();
    }

    /**
     * Return the next chunk, or `null` if there are no chunks remaining.
     */
    public ByteBuffer next() {
        if (!this.hasNext()) {
            return null;
        }

        // Allocate chunk buffer
        final int remaining = this.buf.remaining();
        final int chunkBytes = remaining < this.chunkDataSize ? remaining : this.chunkDataSize;
        final ByteBuffer chunk = ByteBuffer.allocate(chunkBytes + Common.HEADER_LENGTH);

        // Create header
        final byte options = remaining > chunkBytes ? (byte) 0 : (byte) 1;
        final int id = UnsignedHelper.getUnsignedInt(this.id);
        final int serial = UnsignedHelper.getUnsignedInt(this.nextSerial());

        // Write to chunk buffer
        chunk.put(options);
        chunk.putInt(id);
        chunk.putInt(serial);
        for (int i = 0; i < chunkBytes; i++) {
            chunk.put(this.buf.get());
        }
        return (ByteBuffer) chunk.flip();
    }

    /**
     * Return and post-increment the id of the next block
     */
    private int nextSerial() {
        return this.chunkId++;
    }

}
