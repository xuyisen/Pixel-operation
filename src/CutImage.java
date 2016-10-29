import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by sen on 2016/10/12.
 * 遍历像素切割图片
 */

/**
 * @x，y:开始切割时的坐标
 * @width,height:切割的长度和宽度
 *
 */
public class CutImage {
    public static String cutImage(String sourcePath,String targetPath,
                                  int x, int y,int width,int height) throws IOException{
        //判断文件是否存在
        File imageFile=new File(sourcePath);
        if(!imageFile.exists()) {
            throw new IOException("Not found the images:" + sourcePath);
        }
        int imgType=sourcePath.lastIndexOf(".");
        //判断类型
        String imageTypeStr=sourcePath.substring(imgType);
        if(!imageTypeStr.equals(".jpg")&&!imageTypeStr.equals(".jpeg")&&
                !imageTypeStr.equals(".png")&&!imageTypeStr.equals(".bmp")){
            System.out.println("图片格式错误");
            return null;
        }
        if(targetPath==null||targetPath.isEmpty()){
            targetPath=sourcePath;
        }
        //输出格式
        String format=sourcePath.substring(sourcePath.lastIndexOf(".")+1,sourcePath.length());
        BufferedImage image= ImageIO.read(imageFile);

        if((image.getWidth()-x)>=width&&(image.getHeight()-y)>=height) {
            int []imageArry=new int[width*height];
            //获取切割部分的像素点
            image.getRGB(x,y,width,height,imageArry,0,width);
            BufferedImage imageNew=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            //写入新的图像中
            imageNew.setRGB(0,0,width,height,imageArry,0,width);
            ImageIO.write(imageNew, format, new File(targetPath));
            return targetPath;
        }else{
            System.out.println("图片切割错误！");
            return null;
        }//end else

    }//end CutImage
}
