import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ResizeThread implements Runnable {
    private String dstFolder;
    private long start;
    private File[] files;

    public ResizeThread(String dstFolder, File[] files, long start){
        this.dstFolder = dstFolder;
        this.files = files;
        this.start = start;
    }

    public ResizeThread(String dstFolder, File file, long start) {
        this.dstFolder = dstFolder;
        files = new File[1];
        files[0] = file;
        this.start = start;
    }
    @Override
    public void run() {
        try
        {
            for(File file : files)
            {
                BufferedImage image = ImageIO.read(file);
                if(image == null) {
                    continue;
                }

                int newWidth = 300;
                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                BufferedImage newImage = Scalr.resize(image, newWidth, newHeight);

                File newFile = new File(dstFolder + "/" + file.getName());
                String format = newFile.getName().substring(newFile.getName().lastIndexOf(".") + 1);
                System.out.println(format);
                ImageIO.write(newImage, format, newFile);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Duration: " + (System.currentTimeMillis() - start));
    }
}
