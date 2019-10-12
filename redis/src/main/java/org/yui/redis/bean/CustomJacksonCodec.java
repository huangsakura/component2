package org.yui.redis.bean;

import org.yui.base.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author huangjinlong
 * @time 2019-03-15 14:54
 * @description
 */
public final class CustomJacksonCodec extends BaseCodec {

    private final Decoder<Object> decoder;

    private final Encoder encoder;

    public static final CustomJacksonCodec INSTANCE = new CustomJacksonCodec();

    /**
     *
     */
    private CustomJacksonCodec() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        JsonUtil.userDefineObjectMapper(objectMapper,false);

        decoder = (buf,state) -> objectMapper.readValue((InputStream) new ByteBufInputStream(buf), Object.class);

        encoder = ((in) -> {
            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
            try {
                ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(byteBuf);
                objectMapper.writeValue((OutputStream)byteBufOutputStream, in);
                return byteBufOutputStream.buffer();
            } catch (IOException e) {
                byteBuf.release();
                throw e;
            } catch (Exception e) {
                byteBuf.release();
                throw new IOException(e);
            }
        });
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }
}
