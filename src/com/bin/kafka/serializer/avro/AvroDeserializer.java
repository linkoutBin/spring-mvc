package com.bin.kafka.serializer.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class AvroDeserializer implements Deserializer<User> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public User deserialize(String s, byte[] bytes) {
        SeekableByteArrayInput arrayInput = new SeekableByteArrayInput(bytes);
        DatumReader<User> datumReader = new SpecificDatumReader<>(User.class);

        User user = null;
        try (DataFileReader<User> dataFileReader = new DataFileReader<>(arrayInput, datumReader)) {
            if (dataFileReader.hasNext()) {
                user = dataFileReader.next(user);
            } else
                System.out.println("未解析到数据！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void close() {

    }
}
