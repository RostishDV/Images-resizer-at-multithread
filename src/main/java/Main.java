import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        String srcFolder = "src/main/resources/src";
        String dstFolder = "src/main/resources/dst";

        File srcDir = new File(srcFolder);

        File[] srcFiles = srcDir.listFiles();

        int unusedCores = Runtime.getRuntime().availableProcessors();

        assert srcFiles != null;
        int srcFilesLength = srcFiles.length;
        if (srcFilesLength <= unusedCores) {
            for (File srcFile : srcFiles) {
                long start = System.currentTimeMillis();
                ResizeThread thread = new ResizeThread(dstFolder, srcFile, start);
                new Thread(thread).start();
            }
        } else {
            int unusedFiles = srcFilesLength;
            int srcPos = 0;
            while (unusedFiles > 0) {
                File[] files;
                int currentArrayLength = 0;
                if (unusedFiles % unusedCores == 0) {
                    currentArrayLength = unusedFiles / unusedCores;
                } else {
                    if (unusedCores == 1) {
                        currentArrayLength = unusedFiles;
                    } else {
                        currentArrayLength = (unusedFiles / unusedCores) + 1;
                    }
                }
                files = new File[currentArrayLength];
                System.arraycopy(srcFiles, srcPos, files, 0, currentArrayLength);

                long start = System.currentTimeMillis();
                ResizeThread thread = new ResizeThread(dstFolder, files, start);
                new Thread(thread).start();

                unusedFiles -= currentArrayLength;
                unusedCores--;
            }
        }
    }
}
