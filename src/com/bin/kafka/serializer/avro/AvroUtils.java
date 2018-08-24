package com.bin.kafka.serializer.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class AvroUtils {
    public static void main(String[] args) {
        User user1 = new User();
        user1.setName("张三");
        user1.setAge(12);
        user1.setPhone("1245678");
        User user2 = new User("李斯", 45, "987654321");

        User user3 = User.newBuilder().setName("王二").setAge(57).setPhone("456893256").build();

        // 序列化user到文件中
        File file = new File("src/com/bin/kafka/users.avro");
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);

        try (DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter)) {
            dataFileWriter.create(user1.getSchema(), new File("src/com/bin/kafka/users.avro"));
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
            dataFileWriter.append(user3);
            dataFileWriter.flush();
        } catch (IOException e) {
        }

        // 从文件中反序列化对象
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> dataFileReader = null;
        try {
            dataFileReader = new DataFileReader<>(file, userDatumReader);
        } catch (IOException e) {
        }
        User user = null;
        try {
            while (dataFileReader.hasNext()) {
                user = dataFileReader.next(user);
                System.out.println(user);
            }
        } catch (IOException e) {
        }

    }
}
