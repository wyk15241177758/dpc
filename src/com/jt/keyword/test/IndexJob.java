//package com.jt.keyword.test;
//
//import java.io.File;
//import java.util.Date;
//
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.store.Directory;
//
//public class IndexJob {  
//    public static Date beginTime;  
//  
//    //读取TXT文件内容  
//    private static String loadFileToString(File file) {  
//        try {  
//            InputStreamReader isr = new InputStreamReader(new FileInputStream(  
//                    file), "UTF-8");  
//            BufferedReader br = new BufferedReader(isr);  
//            StringBuffer sb = new StringBuffer();  
//            String line = br.readLine();  
//            while (line != null) {  
//                sb.append(line);  
//                line = br.readLine();  
//            }  
//            br.close();  
//            return sb.toString();  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//            return null;  
//        }  
//    }  
//      
//    //提取HTML文件的文本内容  
//    private static String getDocument(File html) {  
//        String text = "";  
//        try {  
//            //设置编码集  
////          org.jsoup.nodes.Document doc = Jsoup.parse(html, "UTF-8");  
//            org.jsoup.nodes.Document doc = Jsoup.parse(html,"GBK");  
//  
//            //提取标题信息  
//            Elements title = doc.select("title");  
//            for (org.jsoup.nodes.Element link : title) {  
//                text += link.text() + " ";  
//            }  
//              
//            //提取table中的文本信息  
//            Elements links = doc.select("table");  
//            for (org.jsoup.nodes.Element link : links) {  
//                text += link.text() + " ";  
//            }  
//              
//            //提取div中的文本信息  
//            Elements divs = doc.select("div[class=post]");  
//            for (org.jsoup.nodes.Element link : divs) {  
//                text += link.text() + " ";  
//            }  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        }  
//  
//        return text;  
//    }  
//      
//    public static void createIndex(String inputDir) {  
//        Directory directory = null;  
//        IndexWriter writer = null;  
//        IndexSearcher searcher = null;  
//        // 索引创建开始时间  
//        Date beginTime = new Date();  
//        try {  
//              
//            //从配置文件中读取索引存放路径  
//            String indexPath = IndexUtil.getValue(IndexUtil.INDEX_ROOT);  
//            directory = FSDirectory.open(new File(indexPath));  
//            File indexFile = new File(indexPath + "\\segments.gen");  
//              
//            //根据索引文件segments.gen是否存在判断是否是第一次创建索引  
//            if (indexFile.exists()) {  
//                  
//                //增量添加索引信息  
//                writer = new IndexWriter(directory, new IKAnalyzer(), false,  
//                        IndexWriter.MaxFieldLength.LIMITED);  
//                writer.setMergeFactor(1000);  
//                writer.setMaxBufferedDocs(100);  
//                writer.setMaxMergeDocs(Integer.MAX_VALUE);  
//            } else {  
//                  
//                //新建索引信息  
//                writer = new IndexWriter(directory, new IKAnalyzer(), true,  
//                        IndexWriter.MaxFieldLength.LIMITED);  
//                writer.setMergeFactor(1000);  
//                writer.setMaxBufferedDocs(100);  
//                writer.setMaxMergeDocs(Integer.MAX_VALUE);  
//            }  
//  
//            File fileDir = new File(inputDir);  
//            File[] files = fileDir.listFiles();  
//            if (files.length > 1) {  
//  
//                //当前最新上传的文件夹名字（时间降序第二个，因为最新的可能还未上传完毕）  
////              String newLastFolder = files[files.length - 2].getName();  
//                  
//                //将上次任务记录的文件夹名称取出  
////              String lastFolder = IndexUtil  
////                      .getValue(IndexUtil.LAST_FOLDER_PATH);  
//  
//                  
//                //索引文件中存储的索引字段的定义  
//                Field fieldName = new Field("id", "", Field.Store.YES,  
//                        Field.Index.UN_TOKENIZED);  
//                Field fieldPath = new Field("path", "", Field.Store.YES,  
//                        Field.Index.NO);  
//                Field fieldContent = new Field("content", "", Field.Store.COMPRESS,  
//                        Field.Index.ANALYZED);  
//                Document doc = null;  
//  
//                //遍历文件根目录下所有子目录并创建索引  
//                for (int i = 0; i < files.length - 1; i++) {  
//                    if (files[i].isDirectory()) {  
//                          
//                        //这里需要根据当前文件夹的命名规律和上次建索引后记录的文件夹名字比较  
//                        //避免出现对文件重复创建索引  
//                          
//                                File fileDirs = new File(files[i]  
//                                        .getAbsolutePath());  
//                                File[] file = fileDirs.listFiles();  
//                                for (int j = 0; j < file.length; j++) {  
//                                      
//                                    String fileName = file[j].getName();  
//  
//                                    String lastName = "";  
//                                    if (fileName.lastIndexOf(".") != -1) {  
//                                        lastName = fileName  
//                                                .substring(fileName  
//                                                        .lastIndexOf("."));  
//                                    }  
//                                    if (lastName.equals(".txt")) {  
//                                        doc = new Document();  
//                                        fieldName.setValue(fileName.substring(0, fileName.indexOf(".")));  
//                                        doc.add(fieldName);  
//                                        fieldPath.setValue(file[j]  
//                                                .getAbsolutePath());  
//                                        doc.add(fieldPath);  
//                                        fieldContent  
//                                                .setValue(loadFileToString(file[j]));  
//                                        doc.add(fieldContent);  
//                                        writer.addDocument(doc);  
//                                    }  
//                                        else if (lastName.equals(".html") ||lastName.equals(".htm")) {  
//                                            doc = new Document();  
//                                            String htmlCont = getDocument(file[j]);  
//                                            fieldName.setValue(file[j].getName());  
//                                            doc.add(fieldName);  
//                                            fieldPath.setValue(file[j]  
//                                                    .getAbsolutePath());  
//                                            doc.add(fieldPath);  
//                                            fieldContent  
//                                                    .setValue(htmlCont);  
//                                            doc.add(fieldContent);  
//                                            writer.addDocument(doc);  
//                                              
//                                        }  
//                                }  
//                                Date endTime1 = new Date();  
//                                long timeOfSearch1 = endTime1.getTime()  
//                                        - beginTime.getTime();  
//                                System.out.println("--->" + files[i]  
//                                        + "建立索引时间 " + timeOfSearch1 / 1000 / 60  
//                                        + " Minute" + "(" + timeOfSearch1  
//                                        + " ms )");  
//                    }  
//                }  
//  
//                //创建索引完成后记录下本次创建索引的最后一个目录名称  
////              if (!lastFolder.equals(newLastFolder)) {  
////                  String path = PropertyUtil.class.getResource(  
////                          "/index.properties").toURI().getPath();  
////                  PropertyUtil.updateValue(path, IndexUtil.LAST_FOLDER_PATH,  
////                          LAST_FILE_PATH);  
////                  IndexUtil.indexMap.put(IndexUtil.LAST_FOLDER_PATH,  
////                          LAST_FILE_PATH);  
////              }  
//            }  
//  
//        } catch (Exception e) {  
//            //清空writer中的索引信息，否则writer在close时会将信息写入索引文件  
//            writer = null;  
//            e.printStackTrace();  
//        } finally {  
//  
//            if (searcher != null) {  
//                try {  
//                    searcher.close();  
//                } catch (Exception e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//  
//            if (writer != null) {  
//                try {  
//                    //优化索引并合并索引文件  
//                    writer.optimize();  
//                    Date endTime1 = new Date();  
//                    long timeOfSearch1 = endTime1.getTime()  
//                            - beginTime.getTime();  
//                    System.out.println("--->"  
//                            + "合并索引时间 " + timeOfSearch1 / 1000 / 60  
//                            + " Minute" + "(" + timeOfSearch1  
//                            + " ms )");  
//                    writer.close();  
//                } catch (Exception e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//            if (directory != null) {  
//                try {  
//                    directory.close();  
//                } catch (Exception e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//        }  
//  
//  
//    }  
//  
///** 
//     * 将小索引文件合并到大的索引文件中去 
//     *  
//     * @param from 
//     *            将要合并到to文件的文件 
//     * @param to 
//     *            将from文件合并到该文件 
//     */  
//    public static void mergeIndex() {  
//        IndexWriter writer = null;  
//        Directory toDirectory = null;  
//        Directory fromDirectory = null;  
//        try {  
//            File from = new File(INDEX_THREAD1);  
//            File to = new File(IndexUtil.getValue(IndexUtil.INDEX_ROOT));  
//            toDirectory = FSDirectory.open(to);  
//            fromDirectory = FSDirectory.open(from);  
//            writer = new IndexWriter(toDirectory, new IKAnalyzer(), false,  
//                    IndexWriter.MaxFieldLength.LIMITED);  
//            writer.setMergeFactor(100);  
//            writer.setMaxBufferedDocs(100);  
//            writer.setMaxMergeDocs(Integer.MAX_VALUE);  
//            writer.addIndexes(IndexReader.open(fromDirectory));  
//            writer.optimize();  
//            writer.close();  
//        } catch (Exception e) {  
//            writer = null;  
//            e.printStackTrace();  
//        } finally {  
//            try {  
//                if (writer != null)  
//                    writer.close();  
//            } catch (Exception e) {  
//  
//            }  
//            if (toDirectory != null) {  
//                try {  
//                    toDirectory.close();  
//                } catch (Exception e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//  
//            if (fromDirectory != null) {  
//                try {  
//                    fromDirectory.close();  
//                } catch (Exception e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//        }  
//  
//        //索引合并完成时间  
//        Date endTime = new Date();  
//        //索引合并所耗时间  
//        long timeOfSearch = endTime.getTime() - beginTime.getTime();  
//        System.out.println("The total time For index creat is " + timeOfSearch  
//                + " ms");  
//    }  
//  
//  
//    //测试代码  
//    public static void main(String[] args) {  
//         IndexJob processor = new IndexJob();  
//         processor.createIndex("g:\\file4");  
//    }  
//      
//} 