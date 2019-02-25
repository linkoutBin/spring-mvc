package com.bin.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author: xingshulin
 * @Date: 2018/12/21 上午10:13
 * @Description: TODO
 * @Version: 1.0
 **/
public class IndexController {
    public static void main(String[] args) throws IOException {
        //index();

        query();

        //delete();

    }

    private static void delete() throws IOException {
        File indexFile = new File("/Users/xingshulin/Tools/lucene/index");
        FSDirectory fsDirectory = FSDirectory.open(indexFile.toPath());
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig();
        try (IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig)) {
            indexWriter.deleteAll();
        }
    }

    private static void index() throws IOException {
        //索引存储目录
        File indexDir = new File("/Users/xingshulin/Tools/lucene/index");
        //待索引数据目录
        File dataDir = new File("/Users/xingshulin/Tools/lucene/data");

        Directory directory = FSDirectory.open(indexDir.toPath());
        IndexWriterConfig config = new IndexWriterConfig();
        try (IndexWriter indexWriter = new IndexWriter(directory, config)) {

            File[] files = dataDir.listFiles();
            for (File file : files) {
                Document document = new Document();
                document.add(new TextField("fileName", file.getName(), Field.Store.YES));
                document.add(new TextField("fileContent", FileUtils.readFileToString(file, Charset.forName("UTF-8")), Field.Store.YES));
                document.add(new StringField("filePath", file.getCanonicalPath(), Field.Store.YES));
                document.add(new TextField("fileSize", String.valueOf(FileUtils.sizeOf(file)), Field.Store.YES));
                indexWriter.addDocument(document);
            }
            indexWriter.commit();
            indexWriter.flush();
        }
    }

    private static void query() throws IOException {
        String queryStr = "lucene";
        //This is the directory that hosts the Lucene index
        File indexDir = new File("/Users/xingshulin/Tools/lucene/index");
        FSDirectory directory = FSDirectory.open(indexDir.toPath());
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(directoryReader);
        if (!indexDir.exists()) {
            System.out.println("The Lucene index is not exist");
            return;
        }
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig();
        Term term = new Term("fileContent", queryStr.toLowerCase());
        TermQuery luceneQuery = new TermQuery(term);
        TopDocs docs = searcher.search(luceneQuery, 5);
        try (IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig)) {
            for (ScoreDoc scoreDoc : docs.scoreDocs) {
                System.out.println("indexDocID: " + scoreDoc.doc);
                Document document = searcher.doc(scoreDoc.doc);
                System.out.println(document.getFields());
                document.removeField("fileName");
                document.add(new TextField("fileName", "test.txt", Field.Store.YES));
                System.out.println(document.get("fileName"));
                System.out.println(document.get("fileContent"));
                System.out.println(document.get("filePath"));
                System.out.println(document.get("fileSize"));
            }
        }

    }
}
