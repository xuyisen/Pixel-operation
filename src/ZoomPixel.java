import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

/**
 * Created by sen on 2016/10/14.
 */
public class ZoomPixel {
    /**
     *
     * @param sourcePath  文件输入路径
     * @param k1           x轴的缩放比例
     * @param k2           y轴的缩放比例
     * @param targetPath  文件输出路径                    
     * @return             文件的输出路径
     * @throws IOException
     */
    public static String amplify(String sourcePath, String targetPath,float k1, float k2) throws IOException {
        
        File imageFile=new File(sourcePath);
        
        if(!imageFile.exists()) {
            throw new IOException("Not found the images:" + sourcePath);
        }//end if
        
        int imgType=sourcePath.lastIndexOf(".");
        String imageTypeStr=sourcePath.substring(imgType);
        //判断图片的格式
        if(!imageTypeStr.equals(".jpg")&&!imageTypeStr.equals(".jpeg")&&
                !imageTypeStr.equals(".png")&&!imageTypeStr.equals(".bmp")){
            System.out.println("图片格式错误");
            return null;
        }//end if
        //判断文件的输出路径是否为空
        if(targetPath==null||targetPath.isEmpty()){
            targetPath=sourcePath;
        }
        //输出格式
        String format=sourcePath.substring(sourcePath.lastIndexOf(".")+1,sourcePath.length());
        BufferedImage image= ImageIO.read(imageFile);
        if(k1<0.28f||k2<0.28){
            System.out.println("缩放比例过小");
            return null;
        }
        
        //采取双线性差值法 
        int scrW = image.getWidth();                                   //原图片的宽
        int scrH= image.getHeight();                                   //原图片的宽
        int tarW = Math.round(k1*scrW);                                //放大或者缩小后图片的宽
        int tarH = Math.round(k2*scrH);                                //放大或者缩小后图片的长
        int[] pix = new int[scrW*scrH];                                //像素
        pix = image.getRGB(0, 0, scrW, scrH, pix, 0, scrW);
        /*System.out.println(scrW + " * " + scrH);
        System.out.println(tarW + " * " + tarH);*/
        int[] newPix = new int[tarW*tarH];

        for(int j=0; j<scrH-1; j++){
            for(int i=0; i<scrW-1; i++) {
                //先找出两个像素点，两个点之间的差值D，为后来插值做准备
                int x0 = Math.round(i*k1);
                int y0 = Math.round(j*k2);
                int x1, y1;
                if(i == scrW-2) {
                    x1 = tarW-1;
                } else {
                    x1 = Math.round((i+1)*k1);
                }
                if(j == scrH-2) {
                    y1 = tarH-1;
                } else {
                    y1 = Math.round((j+1)*k2);
                }
                int d1 = x1 - x0;
                int d2 = y1 - y0;

                //初始化
                if(0 == newPix[y0*tarW + x0]) {
                    newPix[y0*tarW + x0] =  pix[j*scrW+i];
                }
                //到图片结尾时
                if(0 == newPix[y0*tarW + x1]) {
                    if(i == scrW-2) {
                        newPix[y0*tarW + x1] = pix[j*scrW+scrW-1];
                    } else {
                        newPix[y0*tarW + x1] =  pix[j*scrW+i+1];
                    }//end else
                }//end if
                if(0 == newPix[y1*tarW + x0]){
                    if(j == scrH-2) {
                        newPix[y1*tarW + x0] = pix[(scrH-1)*scrW+i];
                    } else {
                        newPix[y1*tarW + x0] =  pix[(j+1)*scrW+i];
                    }//end if
                }
                if(0 == newPix[y1*tarW + x1]) {
                    if(i==scrW-2 && j==scrH-2) {
                        newPix[y1*tarW + x1] = pix[(scrH-1)*scrW+scrW-1];
                    } else {
                        newPix[y1*tarW + x1] = pix[(j+1)*scrW+i+1];
                    }//end else
                }//end if

                int red, green, blue;
                float c;
                ColorModel cm = ColorModel.getRGBdefault();
                for(int l=0; l<d2; l++) {
                    for(int k=0; k<d1; k++)
                        if (0 == l) {
                            //f(x,0) = f(0,0) + c1*(f(1,0)-f(0,0))
                            if (j < scrH - 1 && newPix[y0 * tarW + x0 + k] == 0) {
                                c = (float) k / d1;
                                red = cm.getRed(newPix[y0 * tarW + x0]) + (int) (c * (cm.getRed(newPix[y0 * tarW + x1]) - cm.getRed(newPix[y0 * tarW + x0])));//newPix[(y0+l)*tarW + k]
                                green = cm.getGreen(newPix[y0 * tarW + x0]) + (int) (c * (cm.getGreen(newPix[y0 * tarW + x1]) - cm.getGreen(newPix[y0 * tarW + x0])));
                                blue = cm.getBlue(newPix[y0 * tarW + x0]) + (int) (c * (cm.getBlue(newPix[y0 * tarW + x1]) - cm.getBlue(newPix[y0 * tarW + x0])));
                                newPix[y0 * tarW + x0 + k] = new Color(red, green, blue).getRGB();
                            }//end if
                            if (j + 1 < scrH && newPix[y1 * tarW + x0 + k] == 0) {
                                c = (float) k / d1;
                                red = cm.getRed(newPix[y1 * tarW + x0]) + (int) (c * (cm.getRed(newPix[y1 * tarW + x1]) - cm.getRed(newPix[y1 * tarW + x0])));
                                green = cm.getGreen(newPix[y1 * tarW + x0]) + (int) (c * (cm.getGreen(newPix[y1 * tarW + x1]) - cm.getGreen(newPix[y1 * tarW + x0])));
                                blue = cm.getBlue(newPix[y1 * tarW + x0]) + (int) (c * (cm.getBlue(newPix[y1 * tarW + x1]) - cm.getBlue(newPix[y1 * tarW + x0])));
                                newPix[y1 * tarW + x0 + k] = new Color(red, green, blue).getRGB();
                            }//end if
                            //System.out.println(c);
                        } else {
                            //f(x,y) = f(x,0) + c2*f(f(x,1)-f(x,0))
                            c = (float) l / d2;
                            red = cm.getRed(newPix[y0 * tarW + x0 + k]) + (int) (c * (cm.getRed(newPix[y1 * tarW + x0 + k]) - cm.getRed(newPix[y0 * tarW + x0 + k])));
                            green = cm.getGreen(newPix[y0 * tarW + x0 + k]) + (int) (c * (cm.getGreen(newPix[y1 * tarW + x0 + k]) - cm.getGreen(newPix[y0 * tarW + x0 + k])));
                            blue = cm.getBlue(newPix[y0 * tarW + x0 + k]) + (int) (c * (cm.getBlue(newPix[y1 * tarW + x0 + k]) - cm.getBlue(newPix[y0 * tarW + x0 + k])));
                            newPix[(y0 + l) * tarW + x0 + k] = new Color(red, green, blue).getRGB();
                            //System.out.println((int)(c*(cm.getRed(newPix[y1*tarW + x0+k]) - cm.getRed(newPix[y0*tarW + x0+k]))));
                        }//end else
                    if(i==scrW-2 || l==d2-1) { //最后一列的计算
                        //f(1,y) = f(1,0) + c2*f(f(1,1)-f(1,0))
                        c = (float)l/d2;
                        red = cm.getRed(newPix[y0*tarW + x1]) + (int)(c*(cm.getRed(newPix[y1*tarW + x1]) - cm.getRed(newPix[y0*tarW + x1])));
                        green = cm.getGreen(newPix[y0*tarW + x1]) + (int)(c*(cm.getGreen(newPix[y1*tarW + x1]) - cm.getGreen(newPix[y0*tarW + x1])));
                        blue = cm.getBlue(newPix[y0*tarW + x1]) + (int)(c*(cm.getBlue(newPix[y1*tarW + x1]) - cm.getBlue(newPix[y0*tarW + x1])));
                        newPix[(y0+l)*tarW + x1] = new Color(red,green,blue).getRGB();
                    }//end if
                }//end for
            }//end for
        }//end for

        BufferedImage imgOut = new BufferedImage( tarW, tarH, imgType);
        imgOut.setRGB(0, 0, tarW, tarH, newPix, 0, tarW);
        ImageIO.write(imgOut, format, new File(targetPath));
        return targetPath;
    }//end amplify

}//end class ZoomPixel
