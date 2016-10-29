
import java.io.IOException;

/**
 * Created by sen on 2016/10/13.
 */
public class PictureProcessor {
    public static void main(String[] args) throws IOException{
        CutImage.cutImage("data/1.jpg","data/2.jpg",200,200,1000,1000);
        ZoomPixel.amplify("data/1.jpg","data/3.jpg",0.5f,0.5f);

    }
}
