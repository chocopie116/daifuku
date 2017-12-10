package com.amazonaws.kinesisvideo.producer;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * KinesisVideo stream interface
 */

public interface KinesisVideoProducerStream extends StreamCallbacks {
    /**
     * Ready timeout value in milliseconds for the sync API
     */
    public static final long READY_TIMEOUT_IN_MILLISECONDS = 15000;

    /**
     * Stopped timeout value in milliseconds for the sync API
     */
    public static final long STOPPED_TIMEOUT_IN_MILLISECONDS = 15000;

    /**
     * Returns the input stream to retrieve the data from.
     *
     * NOTE: The returned stream should ideally be buffered
     * like {@link java.io.BufferedInputStream}
     *
     * IMPORTANT: During the streaming token rotation or when the underlying stream
     * gets closed the stream returned by this function will also close.
     * The caller then should re-acquire a new stream by calling this API again.
     *
     * @return {@link InputStream} for retrieving the data
     * @throws ProducerException
     */
    @Nonnull
    InputStream getDataStream() throws ProducerException;

    /**
     * Get stream data from the buffer.
     *
     * @param fillBuffer
     *         The buffer to fill
     * @param offset
     *         The start of the buffer
     * @param length
     *         The number of bytes to fill
     * @return The number of bytes filled
     * @throws ProducerException
     */
    int getStreamData(final @Nonnull byte[] fillBuffer, int offset, int length)
            throws ProducerException;

    /**
     * Puts a frame into the stream.
     */
    void putFrame(final @Nonnull KinesisVideoFrame kinesisVideoFrame) throws ProducerException;

    /**
     * Reports an ACK for a fragment.
     */
    void fragmentAck(final @Nonnull KinesisVideoFragmentAck kinesisVideoFragmentAck) throws ProducerException;

    /**
     * Parses and processes a response which can contain partial/multiple fragment ACK.
     * @param kinesisVideoFragmentAck ACK string returned from the service.
     * @throws ProducerException
     */
    void parseFragmentAck(final @Nonnull String kinesisVideoFragmentAck) throws ProducerException;

    /**
     * Indicates that the stream format has changed.
     *
     * NOTE: CPD is @Nullable - specifying a null will remove the CPD.
     * NOTE: currently, only Codec Private Data is supported while not streaming.
     */
    void streamFormatChanged(final @Nullable byte[] codecPrivateData) throws ProducerException;

    /**
     * Returns the underlying native stream handle
     * @return
     */
    long getStreamHandle();

    /**
     * Returns the stream name
     */
    @Nonnull
    String getStreamName();

    /**
     * Stops the Kinesis Video stream.
     */
    void stopStream() throws ProducerException;

    /**
     * Reports an abnormal stream termination
     * @param statusCode - Status code of the termination.
     * @throws ProducerException
     */
    void streamTerminated(int statusCode) throws ProducerException;

    /**
     * Returns stream specific metrics.
     * @return Stream metrics
     * @throws ProducerException
     */
    @Nonnull
    KinesisVideoStreamMetrics getMetrics() throws ProducerException;
}