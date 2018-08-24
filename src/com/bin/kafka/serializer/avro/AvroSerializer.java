package com.bin.kafka.serializer.avro;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.util.Map;


/**
 * 序列化对象准备
 * java -jar $HIVE_HOME/lib/avro-tools-1.7.7.jar  compile schema <schema file> <destination>
 */
public class AvroSerializer implements Serializer<User> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String s, User user) {
        DatumWriter<User> datumWriter = new SpecificDatumWriter<>(User.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataFileWriter<User> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(user.getSchema(), baos);
            dataFileWriter.append(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    public void close() {
    }
}
