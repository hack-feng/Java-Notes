package myjava;

import java.io.*;
import java.util.Objects;

/**
 * @author 笑小枫
 * @date 2020/5/19 11:14
 **/
public class IoDemo {

    public static void main(String[] args) {
        String path = "D:" + File.separator + "1111io";
        String fileName = "test.txt";
        // 创建一个新文件夹
        createPath(path);
//        // 创建一个新文件
//        createFile(path, fileName);
//        // 列出指定目录的全部文件（包括隐藏文件）
//        listFile(path);
//        // 判断是否为文件夹
//        isDirectory(path);
//        // 判断是否为文件
//        isFile(path, fileName);
//        // 删除一个文件
//        deleteFile(path, fileName);
//
//        // 写文件
//        writeFile(path, fileName);
//        // 在文件中追加内容
//        appendFileContent(path, fileName);
//        // 读取文件
//        readFile(path, fileName);
    }

    private static void readFile(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int count = 0;
            int temp;
            while((temp = inputStream.read()) != -1){
                bytes[count++] = (byte) temp;
            }
            inputStream.close();
            System.out.println("读取到的长度为：" + bytes.length + "   内容为：" + new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendFileContent(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        try {
            OutputStream outputStream = new FileOutputStream(file, true);
            String str = "你好呀";
            byte[] bytes = str.getBytes();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile(String path, String fileName){
        createPath(path);
        File file = new File(path + File.separator + fileName);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            String str = "笑小枫";
            byte[] bytes = str.getBytes();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void isFile(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        System.out.println(fileName + " --- isFile result:" + file.isFile());
    }

    private static void isDirectory(String path) {
        File file = new File(path);
        System.out.println(path + " --- isDirectory result:" + file.isDirectory());
    }

    private static void listFile(String path) {
        File file = new File(path);
        if(file.list() != null){
            System.out.println("listFile file list：");
            // 如果只看文件名，使用file.list()即可，返回String[] fileName
            for (File f : Objects.requireNonNull(file.listFiles())) {
                System.out.println(f);
            }
        }else{
            System.out.println("listFile No files!");
        }

    }

    private static void deleteFile(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        if(file.exists()){
            System.out.println(file.getName() +  " --- deleteFile result" + file.delete());
        }
    }

    private static void createPath(String path) {
        File pathFile = new File(path);
        if(pathFile.mkdirs()){
            System.out.println(path + " --- createPath result");
        }
    }

    private static void createFile(String path, String fileName){
        File pathFile = new File(path);
        if(pathFile.mkdirs()){
            System.out.println(path + "is not exsit, this is auto create.");
        }
        try {
            File file = new File(path + File.separator + fileName);
            System.out.println(fileName + " --- createFile result：" + file.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
