import java.io.File;
import java.io.FileInputStream;

public class RecognitionAPI implements Runnable {
    private File imageFile;

    public RecognitionAPI(File imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public void run() {
        VisionAPI RhinoTeam = new VisionAPI(imageFile.getAbsolutePath());
        try {
            VisionAPI.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
