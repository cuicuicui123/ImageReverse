package example;

import com.luciad.imageio.webp.WebPReadParam;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class ImageReverse {

    public static ArrayList<String> files = new ArrayList<String>();
    public static ArrayList<String> files_9 = new ArrayList<String>();

    public static void main(String[] args) {
         String fileDir = "/Users/cuiweicong/Desktop/需要反色";
         getFiles(fileDir);
         if (files != null && files.size() > 0) {
             for (String filePath : files) {
                 BufferedImage bi = file2img(filePath);  //读取图片
                 BufferedImage bii = img_inverse(bi,false);

                 String filePathName = getFileName(filePath);
                 String newFile = "/Users/cuiweicong/Desktop/图片反色/" + filePathName;
                 createFile(newFile);
                 img2file(bii, filePath.split("\\.")[1], newFile);  //生成图片
             }
         }
    }


    //图片反色
    public static BufferedImage img_inverse(BufferedImage imgsrc, boolean isNice) {
        try {
            //创建一个不透明度的图片
            BufferedImage back = new BufferedImage(imgsrc.getWidth(), imgsrc.getHeight(), BufferedImage.TYPE_INT_ARGB);
            int width = imgsrc.getWidth();
            int height = imgsrc.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = imgsrc.getRGB(j, i);
                    int alpha = (pixel >> 24) & 0x00FFFFFF;
                    int red = (pixel >> 16) & 0x0ff;
                    int green = (pixel >> 8) & 0x0ff;
                    int blue = pixel & 0x0ff;
                    System.out.println("i=" + i + ",j=" + j + ",pixel=" + pixel + "     ,alpha = " + alpha + "，red = " + red + "，green = " + green + "，blue = " + blue);
                    // back.setRGB(j,i,0xFFFFFF-pixel);

                    red = 255 - red;
                    green = 255 - green;
                    blue = 255 - blue;
                    // int result = (0xFFFFFF - pixel);
                    int result = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    System.out.println("result = " + result + "，red = " + red + "，green = " + green + "，blue = " + blue);
                    if(isNice && (i == 0 || i == (height-1) || j == 0| j == (width-1))){
                        back.setRGB(j, i, pixel);
                    }else {
                        back.setRGB(j, i, result);
                    }

                }
            }
            return back;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取文件夹下的所有文件
    public static ArrayList<String> getFiles(String path) {

        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                System.out.println("文     件：" + tempList[i]);
                if(getFileName(tempList[i].toString()).contains(".9")){
                    files_9.add(tempList[i].toString());
                }else{
                    files.add(tempList[i].toString());
                }
            }
            if (tempList[i].isDirectory()) {
                System.out.println("文件夹：" + tempList[i]);
            }
        }
        return files;
    }

    //获取文件名
    public static String getFileName(String path) {
        File tempFile = new File(path.trim());
        String fileName = tempFile.getName();
        return fileName;
    }

    //创建文件
    public static void createFile(String path) {
        /**//*查找目录，如果不存在，就创建*/
        try{
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                if (!dirFile.mkdir())
                    System.out.println("目录不存在，创建失败！");
            }
            /**//*查找文件，如果不存在，就创建*/
            File file = new File(path);
            if (!file.exists())
                if (!file.createNewFile())
                    System.out.println("文件不存在，创建失败！");
        }catch(Exception e){

        }

    }


    //读取图片
    public static BufferedImage file2img(String imgpath) {
        try {
            BufferedImage bufferedImage;
            if (imgpath.endsWith("webp")) {
                // Configure decoding parameters
                ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
                // Configure decoding parameters
                WebPReadParam readParam = new WebPReadParam();
                readParam.setBypassFiltering(true);
                reader.setInput(new FileImageInputStream(new File(imgpath)));
                bufferedImage = reader.read(0, readParam);
            } else {
                bufferedImage = ImageIO.read(new File(imgpath));
            }
            return bufferedImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //保存图片,extent为格式，"jpg"、"png"等
    public static void img2file(BufferedImage img, String extent, String newfile) {
        try {
            ImageIO.write(img, extent, new File(newfile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

