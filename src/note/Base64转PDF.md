~~~
    /**
     * Description: 将base64编码内容转换为Pdf
     *
     * Create Date: 2015年7月30日 上午9:40:23
     */
    public static void main(String[] args) {
        BASE64Decoder decoder = new BASE64Decoder();
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        String filePath = "D:\\test\\test.pdf";
        InputStream in;
        try {
            byte[] aaa = new byte[100];//base64编码内容转换为字节数组
            in = new FileInputStream("D:\\test\\11.txt");
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            byte[] bytes = decoder.decodeBuffer(in);
            System.out.println(Arrays.toString(aaa));
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                fos.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
~~~