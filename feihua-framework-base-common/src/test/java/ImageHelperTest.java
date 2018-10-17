import com.feihua.framework.utils.ImageHelper;
import com.feihua.utils.collection.CollectionUtils;
import com.feihua.utils.graphic.ImageUtils;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by yangwei
 * Created at 2018/9/17 11:21
 */
public class ImageHelperTest {
    public static void main(String[] args) throws IOException {
        // 图片质量压缩
        String intpuPath = "d:/C67233EA-1577-41A1-B524-E7DF792207FA.jpeg";
        // String intpuPath = "d:/test.jpg";


        Thumbnails.of(intpuPath).scale(1).outputQuality(0.3).toFile("d:/test.png");
    }
}
